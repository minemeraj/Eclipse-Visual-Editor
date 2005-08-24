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
 *  $RCSfile: ReplaceAttributeSettingCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * Command to replace MOF attributes at indexed location.
 * It will replace the one already at that location.
 */
public class ReplaceAttributeSettingCommand extends AbstractApplyAttributeCommand {
	protected Object oldValue;
	protected int fReplaceIndex = -1;
	protected Object fReplaceValue;

	public ReplaceAttributeSettingCommand(String name) {
		super(name);
	}

	public ReplaceAttributeSettingCommand() {
		super();
	}

	public boolean prepare() {
		return (fReplaceIndex != -1 || fReplaceValue != null)
			&& feature.isMany()
			&& getAttributeSettingValues().size() == 1
			&& super.prepare();
	}

	public void execute() {
		cancelOldContainment();
		
		EObject target = getTarget();
		List featureList = (List) target.eGet(feature);
		if (fReplaceValue != null) {
			// We want to replace a value. Find the position of that value.
			fReplaceIndex = featureList.indexOf(fReplaceValue);
		}

		oldValue = featureList.set(fReplaceIndex, getAttributeSettingValues().get(0));
	}

	public void undo() {
		EObject target = getTarget();

		((List) target.eGet(feature)).set(fReplaceIndex, oldValue);

		oldValue = null; // Don't hold onto it.
		restoreOldContainment();	
	}

	/**
	 * Set the replacement index.
	 */
	public void setReplaceIndex(int newIndex) {
		fReplaceIndex = newIndex;
		fReplaceValue = null; // Reset it to indicate index type requested.
	}
	/**
	 * Set the insert before value. This is mutually exclusive with the
	 * setInsertionIndex() method.
	 */
	public void setReplaceValue(Object replaceValue) {
		fReplaceIndex = -1; // Reset it to indicate replace value requested.
		fReplaceValue = replaceValue;
	}

}
