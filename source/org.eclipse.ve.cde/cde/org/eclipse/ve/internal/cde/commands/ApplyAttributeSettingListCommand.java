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
 *  $RCSfile: ApplyAttributeSettingListCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.ArrayList;
import java.util.List;
/**
 * Command to replace an entire multi-valued attribute setting list for a many-valued
 * feature with a new list of values.
 */

public class ApplyAttributeSettingListCommand extends AbstractApplyAttributeCommand {
	protected List oldValues;

	public ApplyAttributeSettingListCommand(String name) {
		super(name);
	}

	public ApplyAttributeSettingListCommand() {
		super();
	}

	public boolean prepare() {
		return super.prepare() && feature.isMany();
	}

	public void execute() {
		cancelOldContainment();
		
		List values = (List) getTarget().eGet(feature);
		oldValues = new ArrayList(values); // Save the old values
		try {
			values.clear();
			values.addAll(getAttributeSettingValues());
		} catch (RuntimeException e) {
			undo();
			throw e;
		}
	}

	public void undo() {
		try {
			List values = (List) getTarget().eGet(feature);
			values.clear();
			if (!oldValues.isEmpty())
				values.addAll(oldValues);
			oldValues = null; // Get rid of it so we don't hold onto it.
			
			restoreOldContainment();
		} catch (RuntimeException e) {
			getTarget().eSet(feature, getAttributeSettingValues());
			throw e;
		}
	}

}
