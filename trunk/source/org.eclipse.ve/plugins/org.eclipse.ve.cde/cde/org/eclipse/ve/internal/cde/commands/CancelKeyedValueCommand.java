package org.eclipse.ve.internal.cde.commands;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CancelKeyedValueCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.BasicEMap.Entry;

import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * This command cancel a keyed value to the keyed value holder.
 */

public class CancelKeyedValueCommand extends AbstractCommand {

	protected KeyedValueHolder target;
	protected Object key;
	protected BasicEMap.Entry oldValue;

	public CancelKeyedValueCommand(String name) {
		super(name);
	}

	public CancelKeyedValueCommand() {
	}

	public void setTarget(KeyedValueHolder target) {
		this.target = target;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public boolean canExecute() {
		return target != null && key != null;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		int keyPos = target.getKeyedValues().indexOfKey(key);
		if (keyPos != -1)
			oldValue = (Entry) target.getKeyedValues().remove(keyPos);
	}

	public void redo() {
		execute();
	}

	public void undo() {
		if (oldValue != null)
			target.getKeyedValues().add(oldValue);
		else
			target.getKeyedValues().removeKey(key);

		oldValue = null; // Don't hold onto old value
	}

}