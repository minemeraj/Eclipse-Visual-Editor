/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabItemContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-12-15 14:55:17 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanUtilities;

/**
 * TabItem containment handler. This handles dropping a TabItem onto a Composite or a TabFolder
 * In case of a composite it creates a Control.  In case of a TabFolder it creates a TabItem that references the control
 *  
 * @since 1.2.0
 */
public class TabItemContainmentHandler extends NoFFModelAdapter {
	
	public TabItemContainmentHandler(Object model) {
		super(model);
	}

	protected String controlFeatureName;
	private JavaClass classTabFolder;
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		if (creation) {
			if (parent instanceof IJavaObjectInstance) {
				IJavaObjectInstance javaParent = (IJavaObjectInstance)parent;
				ResourceSet rSet = ((IJavaObjectInstance)parent).eResource().getResourceSet();
				// See whether the parent is a TabItem or a Composite
				if(classTabFolder == null){
					classTabFolder = Utilities.getJavaClass("org.eclipse.swt.widgets.TabFolder",rSet);
				} 				
				if(classTabFolder.isAssignableFrom(javaParent.eClass())){
					// Drop the TabItem onto the TabFolder
					WidgetContainmentHandler.processAllocation(parent,child, preCmds);
					// If the TabItem has a control (as occurs during copy and paste) then we must ensure the control is reparented
					IJavaInstance tabItemControl = BeanUtilities.getFeatureValue((IJavaInstance) child, "control");
					if(tabItemControl != null){
						WidgetContainmentHandler.processAllocation(parent, tabItemControl, preCmds);
					}
					return child;
				} else {
					// Get the control from the TabItem and make this the child to be dropped
					IJavaInstance controlChild = BeanUtilities.getFeatureValue((IJavaObjectInstance)child, "control");					
					WidgetContainmentHandler.processAllocation(parent, controlChild, preCmds);
					WidgetContainmentHandler.processAllocation(parent, child, preCmds);					
					return controlChild;
				}
			} else {
				return super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);	// Let super handle is not on FF.
			}
		}
		throw new StopRequestException("Parent not valid for a TabItem");
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#contributeToRemoveRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToRemoveRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean orphan, EditDomain domain) throws StopRequestException {
		if (orphan)
			postCmds.append(new EnsureOrphanFromParentCommand((IJavaObjectInstance) child, (IJavaObjectInstance) parent));
		return null;
	}

}
