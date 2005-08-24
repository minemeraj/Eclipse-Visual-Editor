/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.commands;
/*
 *  $RCSfile: ApplyKeyedValueCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * This command applies a keyed value (BasicEMap.Entry) to the keyed value holder.\
 */

public class ApplyKeyedValueCommand extends AbstractCommand {

	protected KeyedValueHolder target;
	protected BasicEMap.Entry newValue;
	protected BasicEMap.Entry oldValue;

	public ApplyKeyedValueCommand(String name) {
		super(name);
	}

	public ApplyKeyedValueCommand() {
	}

	public void setTarget(KeyedValueHolder target) {
		this.target = target;
	}

	public void setValue(BasicEMap.Entry value) {
		newValue = value;
	}

	public boolean canExecute() {
		return target != null && newValue != null && newValue.getKey() != null;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		EMap kvs = target.getKeyedValues();
		oldValue = updateKeyedValue(kvs, newValue);
	}

	protected BasicEMap.Entry updateKeyedValue(EMap kvs, BasicEMap.Entry newValue) {
		int keyPos = kvs.indexOfKey(newValue.getKey());
		BasicEMap.Entry old = null;
		if (keyPos != -1)
			old = (BasicEMap.Entry) kvs.set(keyPos, newValue);	// Replace of entry with new entry
		else
			kvs.add(newValue);
		return old;
	}

	public void redo() {
		execute();
	}

	public void undo() {
		if (oldValue != null)
			updateKeyedValue(target.getKeyedValues(), oldValue);
		else
			target.getKeyedValues().removeKey(newValue.getKey());
		oldValue = null; // So that we don't hold onto it.
	}

}
