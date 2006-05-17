/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDEActionFilter.java,v $
 *  $Revision: 1.10 $  $Date: 2006-05-17 20:13:53 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.*;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.IActionFilter;

/**
 * This action filter is used by EditParts that implement IJavaBeanEditPart
 * to filter out objects that meet the criteria specified in the plugin
 * extension point="org.eclipse.ui.popupMenus". This enables context menu capability
 * for those editparts that contain models that meet this criteria.
 * 
 * Editparts that wish to use the filter need to implement IAdaptable and in the 
 * getAdapter(Class type) method, return this filter for the type IAdapter.class.  
 * 
 *
 * The valid tests are: 
 *   (name)        - (value)
 *   CHANGABLE     - "true" if the model can be changed (is ready), "false" if it is not ready (busy, read-only, paused, etc.).
 *   DOMAIN        - "nameOfDomainDataKey" tests if the given key/data in the editdomain has been set. It doesn't test the value value, only
 *                   that the key has been set. 
 *   PROPERTY      - "nameOfFeature" tests if the given feature is an available property of the model of the editpart. (Model needs to be EMF, does not need to be JavaModel).
 *   EDITPOLICY#   - This means redirect the request to the Editpolicies. The portion of "name" after the "#" becomes that "name" and
 *                   value is left as is, and then request to filter is sent to each of the edit policies that can handle IActionFilter or adapt to it.
 *   PARENT#       - This means redirect the request to the parent editpart. The portion of "name" after the "#" becomes that "name" and
 *                   value is left as is, and then request to filter is sent to parent editpart if it can handle IActionFilter or adapt to it.
 *   ANCESTOR#     - This means redirect the request to all of the parent editparts up to root. The portion of "name" after the "#" becomes that "name" and
 *                   value is left as is, and then request to filter is sent to parent editparts if they can handle IActionFilter or adapt to it.
 */
public class CDEActionFilter implements IActionFilter {
	
	public static final CDEActionFilter INSTANCE = new CDEActionFilter();	// Only one is needed. All the info it needs comes from the input.
	
	public final static String
		CHANGEABLE_STRING = "CHANGEABLE", //$NON-NLS-1$
		DOMAIN_STRING = "DOMAIN",	//$NON-NLS-1$
		EDITPOLICY_STRING = "EDITPOLICY#", //$NON-NLS-1$
		PARENT_STRING = "PARENT#", //$NON-NLS-1$
		ANCESTOR_STRING = "ANCESTOR#", //$NON-NLS-1$
		PROPERTY_STRING = "PROPERTY"; //$NON-NLS-1$
	
	/*
	 * Protected so that only singleton INSTANCE, or a subclass can instantiate it.
	 */	
	protected CDEActionFilter() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (!(target instanceof EditPart))	//Can only handle edit parts
			return false;
		
		if (name.equals(CHANGEABLE_STRING)) {
			// This tests the model to see if it is changable.
			return Boolean.valueOf(value).booleanValue() == (CDEUtilities.getHoldState(EditDomain.getEditDomain((EditPart) target)) == ModelChangeController.READY_STATE);
		} if (name.equals(DOMAIN_STRING)) {
			return EditDomain.getEditDomain((EditPart) target).getData(value) != null;
		} else if (name.equals(PROPERTY_STRING)) {
			// This allows an extension so that a popup action could be provided if a component
			// had a specific property with its name equal to 'value'.			
			return testAttributeForPropertyName(target, value);
		} else if (name.startsWith(EDITPOLICY_STRING) && target instanceof ICDEContextMenuContributor) {
			// Iterate through the edit policies and call the testAttribute method for 
			// the edit policy that implements an action filter or returns implements IAdaptable
			// with a type of IActionFilter. 
			// Return the first true condition else return false.
			String arg = name.substring(EDITPOLICY_STRING.length());
			List editpolicies = ((ICDEContextMenuContributor)target).getEditPolicies();
			for (int i = 0; i < editpolicies.size(); i++) {
				EditPolicy ep = (EditPolicy) editpolicies.get(i);
				if (ep instanceof IActionFilter) {
					if (((IActionFilter)ep).testAttribute(target, arg, value))
						return true;					
				} else if (ep instanceof IAdaptable) {
					IActionFilter af = (IActionFilter) ((IAdaptable)ep).getAdapter(IActionFilter.class);
					if (af != null && af.testAttribute(target, arg, value))
						return true;
				}
			}
		} else if (name.startsWith(PARENT_STRING)) {
			// Delegate the test to the editpart's parent if it has an IActionFilter
			String arg = name.substring(PARENT_STRING.length());
			EditPart parent = ((EditPart)target).getParent();
			if (parent != null)
				return editPartFilterTest(target, value, arg, parent);
		} else if (name.startsWith(ANCESTOR_STRING)) {
			String arg = name.substring(ANCESTOR_STRING.length());
			for (EditPart parent = ((EditPart)target).getParent(); parent != null; parent = parent.getParent()) {
				if (editPartFilterTest(target, value, arg, parent))
					return true;			
			}
		}

		return false;
	}
	
	protected boolean editPartFilterTest(Object target, String value, String arg, EditPart ep) {
		if (ep instanceof IActionFilter)
			return ((IActionFilter)ep).testAttribute(ep, arg, value);
		else {
			IActionFilter af = (IActionFilter)((IAdaptable)ep).getAdapter(IActionFilter.class);
			if (af != null)
				return af.testAttribute(ep, arg, value);
		}
		return false;
	}
	
	/**
	 * Return true if this target bean has a structural feature name equal to 'value'
	 */
	public boolean testAttributeForPropertyName(Object target, String value) {
		if (((EditPart) target).getModel() instanceof EObject) {
			EClass modelType = ((EObject)((EditPart) target).getModel()).eClass();
			EStructuralFeature textSF = modelType.getEStructuralFeature(value);
			if (textSF != null)
				return true;
		}
		return false;
	}

}
