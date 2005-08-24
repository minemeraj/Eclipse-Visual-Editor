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
 *  $RCSfile: ApplyAttributeSettingCommand.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

/**
 * Command to apply MOF attributes at indexed locations.
 *
 * All of the settings will be between the given index and the
 * next item. So if current setting was:
 *   0 1 2 3 4
 *
 * And the settings were, with an insertion index of 2:
 *   a b c
 *
 * The result would be:
 *   0 1 a b c 2 3 4
 *
 * The undo will remove the entries.
 *
 * The default index is -1, which is add all at the end.
 *
 * You can also set a insertionValue, and it will insert before that value,
 * if it is found, else it will add at the end. Insertion index and Insertion value
 * are mutually exclusive.
 *
 */
public class ApplyAttributeSettingCommand extends AbstractApplyAttributeCommand {
	protected Object oldValue;
	protected boolean oldSet;
	protected int fInsertionIndex = -1;
	protected Object fInsertBeforeValue;

	public ApplyAttributeSettingCommand(String name) {
		super(name);
	}

	public ApplyAttributeSettingCommand() {
		super();
	}

	public void execute() {
		cancelOldContainment();
		
		EObject target = getTarget();
		if (!feature.isMany()) {
			//Remember previous values for single-valued attributes.
			if (oldSet = target.eIsSet(feature))
				oldValue = target.eGet(feature);
			try {
				target.eSet(feature, getAttributeSettingValues().get(0));
			} catch (RuntimeException e) {
				// Try to put the old one back.
				try {
					undo();
					throw e;
				}catch (RuntimeException e2) {
					// Another happened, so just log this one and throw the original.				}
					CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e2)); //$NON-NLS-1$
				}					
			}
		} else {
			int insertAt = fInsertionIndex;
			List featureList = (List) target.eGet(feature);
			if (fInsertBeforeValue != null) {
				// We want to insert before a value. Find the position
				// of that value.
				insertAt = featureList.indexOf(fInsertBeforeValue);
			}

			try {
				if (insertAt == -1) {
					featureList.addAll(getAttributeSettingValues());
				} else {
					featureList.addAll(insertAt, getAttributeSettingValues());
				}
			} catch (RuntimeException e) {
				// Try to clean up to a pre-execute state. Some may of already been added, need to remove them.
				featureList.removeAll(getAttributeSettingValues());
				throw e;
			}
		}
	}

	public void undo() {		
		EObject target = getTarget();

		if (feature.isMany()) {
			((List) target.eGet(feature)).removeAll(getAttributeSettingValues());
		} else if (!oldSet) {
			target.eUnset(feature);
		} else {
			target.eSet(feature, oldValue);
		}

		oldValue = null; // Don't hold onto it.	
		
		restoreOldContainment();
		
	}

	/**
	 * Set the insertion index. -1 means at to the end.
	 */
	public void setInsertionIndex(int newIndex) {
		fInsertionIndex = newIndex;
		fInsertBeforeValue = null; // Reset it to indicate index type requested.
	}
	/**
	 * Set the insert before value. This is mutually exclusive with the
	 * setInsertionIndex() method.
	 */
	public void setInsertBeforeValue(Object insertBefore) {
		fInsertionIndex = -1; // Reset it to indicate insertion value requested.
		fInsertBeforeValue = insertBefore;
	}

}
