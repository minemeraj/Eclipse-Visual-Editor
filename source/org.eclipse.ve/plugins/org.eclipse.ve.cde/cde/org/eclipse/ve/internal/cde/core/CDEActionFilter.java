/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDEActionFilter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-05-26 18:23:27 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
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
		EDITPOLICY_STRING = "EDITPOLICY#", //$NON-NLS-1$
		PARENT_STRING = "PARENT#", //$NON-NLS-1$
		ANCESTOR_STRING = "ANCESTOR#"; //$NON-NLS-1$
	
	/*
	 * Protected so that only singleton INSTANCE, or a subclass can instantiate it.
	 */	
	protected CDEActionFilter() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith(EDITPOLICY_STRING) && target instanceof ICDEContextMenuContributor) {
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
			return ((IActionFilter)ep).testAttribute(target, arg, value);
		else if (ep instanceof IAdaptable) {
			IActionFilter af = (IActionFilter)((IAdaptable)ep).getAdapter(IActionFilter.class);
			if (af != null)
				return af.testAttribute(target, arg, value);
		}
		return false;
	}

}
