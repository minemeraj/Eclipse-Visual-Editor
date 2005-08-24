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
 *  $RCSfile: AbstractApplyAttributeCommand.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
/**
 * Abstract command for applying attributes.
 */
public abstract class AbstractApplyAttributeCommand extends AbstractAttributeCommand {
	
	// If the feature being applied is a containment reference, then we need to query the
	// old containments, references, and indexes so that upon undo the settings can be placed
	// back into the old containers in the right place. For any setting that was not contained
	// already, the entry will be null.
	protected Notifier[] containers;	// Old containers that contained the settings (use Notifier because it could be in a Resource directly, this covers both EObject and Resource).
	protected EReference[] containmentReferences;	// Old references that contained the settings
	protected int[] containmentIndexes;	// Old indexes for the contained settings.
	
	public AbstractApplyAttributeCommand(String name) {
		super(name);
	}

	public AbstractApplyAttributeCommand() {
		super();
	}

	public boolean prepare() {
		List values = getAttributeSettingValues();

		// Super.canExecute() or Must have at least one setting and can't set multiple values on a single valued feature.
		if (!super.prepare() || (values == null || values.isEmpty() || (!feature.isMany() && values.size() > 1))) {
			return false;
		}

		EClassifier mo = feature.getEType();
		Iterator itr = values.iterator();
		while (itr.hasNext()) {
			Object n = itr.next();
			if (n != null && !mo.isInstance(n))
				return false;
		}
		return true;
	}
	
	protected void cancelOldContainment() {
		if (feature instanceof EReference && ((EReference) feature).isContainment()) {
			// To be able to accurately undo the containments, need to cancel the containments first.
			// Can't let the apply of the containments do it in one step. This is because for indexed
			// containment we need to accurately retrieve the old indexes so that they can be reapplied
			// correctly. That can only be done by gathering them up one at a time.
			containers = new EObject[getAttributeSettingValues().size()];
			containmentReferences = new EReference[containers.length];			
			containmentIndexes = new int[containers.length];

			List values = getAttributeSettingValues();
			for (int i=0; i<containers.length; i++) {
				EObject eo = (EObject) values.get(i);
				Notifier container = eo.eContainer();
				if (container == null)
					container = eo.eResource();	// See if in resource instead.
				if (container != null) {
					// It is contained.
					if (container instanceof EObject) {
						EReference containmentFeature = eo.eContainmentFeature();
						if (container != getTarget() || containmentFeature != feature) {
							// It won't be a touch. Don't bother canceling touch containment.
							containers[i] = container;							
							containmentReferences[i] = containmentFeature;
							if (containmentFeature.isMany()) {
								List s = (List) ((EObject) container).eGet(containmentFeature);
								int n = containmentIndexes[i] = s.indexOf(eo);
								if (n != -1)
									s.remove(n);
								else {
									// Something is out of wack and we can't restore it.
									containers[i] = null;
									containmentReferences[i] = null;
								}
							} else {
								containmentIndexes[i] = -1;
								((EObject) container).eUnset(containmentFeature);
							}
						}
					} else {
						// It is in a Resource. So this can't be a touch since we only set on EObjects.
						containers[i] = container;
						List s = ((Resource) container).getContents();
						int n = containmentIndexes[i] = s.indexOf(eo);
						if (n != -1)
							s.remove(n);
						else {
							// Something is out of wack and we can't restore it.
							containers[i] = null;
							containmentReferences[i] = null;
						}													
					}
				}
			}						
		}
	}
	
	protected void restoreOldContainment() {
		// Need to restore the old containments.
		if (containers != null) {
			List values = getAttributeSettingValues();
			// Need to walk in reverse order so that indexes are applied correctly.
			for (int i=containers.length-1; i>=0; i--) {
				if (containers[i] != null) {
					if (containers[i] instanceof EObject) {
						if (containmentReferences[i].isMany())
							((List) ((EObject) containers[i]).eGet(containmentReferences[i])).add(containmentIndexes[i], values.get(i));
						else
							((EObject) containers[i]).eSet(containmentReferences[i], values.get(i));
					} else {
						((Resource) containers[i]).getContents().add(containmentIndexes[i], values.get(i));
					}
				}
			}
			
			// Now get rid of the holdings so as not to hold onto them.
			containers = null;
			containmentReferences = null;			
			containmentIndexes = null;
		}
	}
}
