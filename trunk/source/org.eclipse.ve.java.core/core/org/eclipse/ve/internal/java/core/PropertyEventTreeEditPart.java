/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: PropertyEventTreeEditPart.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-23 21:14:16 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.ForwardedRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.jcm.PropertyEvent;

/**
 */
public class PropertyEventTreeEditPart extends AbstractTreeEditPart implements PropertyEventEditPart {
	
	protected PropertyEvent propertyChange;
	
	public PropertyEventTreeEditPart(PropertyEvent anEventCallback){
		super(anEventCallback);
		propertyChange = anEventCallback;
	}
	/**
	 * Treat this as a child of the parent so that delete requests are forwarded to the parent
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy(){
				protected Command createDeleteCommand(GroupRequest deleteRequest) {
					ForwardedRequest forwardedRequest = new ForwardedRequest(JavaBeanEventUtilities.REQ_DELETE_EVENT_DEPENDANT, getHost());
					return getHost().getParent().getCommand(forwardedRequest);
				}			
			});
	}	
	
	public String getText(){
		// The eventInvocation is either from an EventSetDescripriptor, e.g. Button>actionPerformed
		// For NLS we must find the actual property itself, however this would involve iterating over every property to find the one that matches
		// so we'll just use the name for now
		IJavaObjectInstance javaBean = (IJavaObjectInstance) propertyChange.eContainer().eContainer();
		
		EStructuralFeature eStructuralFeature = ((JavaClass)javaBean.getJavaType()).getEStructuralFeature(propertyChange.getPropertyName());
		if (eStructuralFeature == null) {
			return propertyChange.getPropertyName();			
		} else {
			return Utilities.getPropertyDecorator(eStructuralFeature).getDisplayName();
		}
	}
	public Image getImage(){
		if (propertyChange.isUseIfExpression())
		    return JavaBeanEventUtilities.getPropertyArrowImage();
		else
		    return JavaBeanEventUtilities.getPropertyShrdArrowImage() ; 
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IJavaElement.class) {
			// KLUDGE: Need to go to the Composition EP to get the JE. If we don't do this then the 
			// JavaBrowsing perspective will go blank when this EP is selected. This is annoying.
			return getRoot().getContents().getAdapter(key);
		}
		return super.getAdapter(key);
	}
	/* Select us and return true if the argument if for us
	 */
	public boolean selectPropertyEvent(PropertyEvent aPropertyEvent) {
		if(aPropertyEvent == propertyChange){
			getViewer().flush();
			getViewer().select(this);
			return true;
		}
		return false;
	}
	
}
