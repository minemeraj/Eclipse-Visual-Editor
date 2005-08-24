/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: SetTextPropertyObjectActionDelegate.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

/**
 * Object action delegate that brings up a dialog to change the 'label' property for
 * a component.
 */
public class SetTextPropertyObjectActionDelegate implements IObjectActionDelegate {
	
	private static final Request DIRECT_EDIT_REQUEST = new Request(RequestConstants.REQ_DIRECT_EDIT);
	
	private EditPart fEditPart;
	private String propertyName = null;
	
	public SetTextPropertyObjectActionDelegate(String prop) {
		propertyName = prop;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 * 
	 * Invokes a direct edit request on the slected edit part.
	 */
	public void run(IAction action) {
		if (fEditPart == null)
			return;
		fEditPart.performRequest(DIRECT_EDIT_REQUEST);
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// Test to see if action is enabled, if it is, that means we've already passed the enablesFor test.
		if (!action.isEnabled())
			return;

        // return if the target property is not set properly
		if (propertyName == null || propertyName.length() <= 0 ) {
			action.setEnabled(false);
			return;
		}
		
		EStructuralFeature sfProperty = null;
		fEditPart = null;
		if (((IStructuredSelection) selection).size() != 1)
			action.setEnabled(false); // Can only set the text on one component
		else {
			action.setEnabled(true);
			fEditPart = (EditPart) ((IStructuredSelection) selection).getFirstElement();
			IJavaObjectInstance component = (IJavaObjectInstance) fEditPart.getModel();
			JavaClass modelType = (JavaClass) component.eClass();
			sfProperty = modelType.getEStructuralFeature(propertyName); //$NON-NLS-1$
			if (sfProperty == null) {
				// no structural feature
				action.setEnabled(false);
			}
		}

	}

	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
	/**
	 * @return
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param string
	 */
	public void setPropertyName(String string) {
		propertyName = string;
	}

}
