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
 *  $RCSfile: CancelAttributeSettingCommand.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

public class CancelAttributeSettingCommand extends AbstractAttributeCommand {
	protected Object oldValue; // Old value when not many feature.
	protected boolean oldSet; // Was Old actually set?
	protected int[] oldIndexes; // Indexes of removed values so that they may be put back in correct locations.

	public CancelAttributeSettingCommand(String name) {
		super(name);
	}
	public CancelAttributeSettingCommand() {
		super();
	}

	public boolean prepare() {
		// Can execute if super.canExecute() and it is either a single-valued feature or
		// it is multi-valued and there are settings to cancel out.
		return super.prepare() && (!feature.isMany() || !getAttributeSettingValues().isEmpty());
	}

	public void execute() {
		org.eclipse.emf.ecore.EObject target = getTarget();
		if (!feature.isMany()) {
			if (oldSet = target.eIsSet(feature))
				oldValue = target.eGet(feature);
			try {
				target.eUnset(feature);
			} catch (RuntimeException e) {
				try {				
					// Try to clean up and put old value back.
					undo();
				}catch (RuntimeException e2) {
					// Another happened, so just log this one and throw the original.				}
					CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e2)); //$NON-NLS-1$
				}					
				throw e;
			}
		} else {
			List values = getAttributeSettingValues();
			ArrayList orderedValues = new ArrayList(values.size());
			// The values will be reordered into their order within the settings list.
			oldIndexes = new int[values.size()];
			int oldI = -1;
			// Walk through the setting list and remove values as they are found. Save their index so that they 
			// can be put back in the same place on undo.
			ListIterator itr = ((List) target.eGet(feature)).listIterator();
			Object v = null;
			try {
				while (!values.isEmpty() && itr.hasNext()) {
					v = itr.next();
					if (values.remove(v)) {
						// It was found in the list to be canceled.												
						oldIndexes[++oldI] = itr.previousIndex(); // Loc to restore it to
						// Actual value to add back is the one from the setting, not the one from values
						// because the one from values is semantically equal, while the one from the setting
						// is the actual identity to restore.
						orderedValues.add(v);
						itr.remove();
					}
				}

				setAttributeSettingValue(orderedValues); // Save the ordered values for undo.
			} catch (RuntimeException e) {
				// An itr.remove() failed. Try to clean up.
				setAttributeSettingValue(orderedValues);
				try {
					undo(); // Add back all those so far removed.
					values.addAll(orderedValues); // Add back those that were removed.
					setAttributeSettingValue(values);
				}catch (RuntimeException e2) {
					// Another happened, so just log this one and throw the original.				}
					CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e2)); //$NON-NLS-1$
				}
				throw e;
			}
		}
	}

	public void undo() {
		EObject target = getTarget();
		if (!feature.isMany()) {
			if (oldSet)
				try {
					target.eSet(feature, oldValue);
				} catch (RuntimeException e) {
					// Try to restore to executed state.
					target.eUnset(feature);
					throw e;
				} else
				target.eUnset(feature);
		} else {
			if (getAttributeSettingValues().isEmpty())
				return; // Nothing to undo. This could happen if the cancel execute didn't find any to remove.			
			// Walk backwards through the list adding the oldValues back in at their appropriate place.
			// Going backwards so that the index values match them in the same order as they came out.
			int oldI = getAttributeSettingValues().size();
			ListIterator oldItr = getAttributeSettingValues().listIterator(oldI);
			int nextI = oldIndexes[--oldI];
			ListIterator itr = ((List) target.eGet(feature)).listIterator(nextI); // Start with last one to restore from oldIndexes.
			while (true) {
				if (nextI == itr.nextIndex()) {
					// We found a spot to add
					itr.add(oldItr.previous());
					if (--oldI < 0)
						break; // We have no more to restore
					nextI = oldIndexes[oldI];
				}
				itr.previous();
			}
		}

		oldIndexes = null; // Don't hold onto it.
		oldValue = null; // Don't hold onto it.
	}
}
