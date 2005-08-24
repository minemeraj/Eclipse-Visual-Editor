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
 *  $RCSfile: ClearAttributeSettingListCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.ArrayList;
import java.util.List;
/**
 * Command to clear out an entire attribute setting list for a many-valued
 * feature.
 */

public class ClearAttributeSettingListCommand extends AbstractAttributeCommand {
	public ClearAttributeSettingListCommand(String name) {
		super(name);
	}

	public ClearAttributeSettingListCommand() {
		super();
	}

	public boolean prepare() {
		return super.prepare() && feature.isMany();
	}

	public void execute() {
		List values = (List) getTarget().eGet(feature);
		setAttributeSettingValue(new ArrayList(values)); // Save the old values, the ArrayList ctor makes a copy of the list
		values.clear();
	}

	public void undo() {
		try {
			((List) getTarget().eGet(feature)).addAll(getAttributeSettingValues());
			setAttributeSettingValue(null); // Get rid of it so we don't hold onto it.
		} catch (RuntimeException e) {
			((List) getTarget().eGet(feature)).clear(); // Clear it out to back out undo.
			throw e;
		}
	}

}
