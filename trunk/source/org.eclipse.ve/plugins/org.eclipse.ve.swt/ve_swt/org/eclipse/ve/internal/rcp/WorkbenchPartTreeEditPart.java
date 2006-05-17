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
 *  $RCSfile: WorkbenchPartTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2006-05-17 20:15:54 $ 
 */
package org.eclipse.ve.internal.rcp;

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;

import org.eclipse.ve.internal.swt.ControlTreeEditPart;
import org.eclipse.ve.internal.swt.SwtPlugin;

/**
 * RCP WorkbenchPart Tree Editpart.
 * 
 * @since 1.1.0
 */
public class WorkbenchPartTreeEditPart extends JavaBeanTreeEditPart {
	
	private EStructuralFeature sf_delegate_control;

	public WorkbenchPartTreeEditPart(Object aModel) {
		super(aModel);
	}
	
	protected List getChildJavaBeans() {
		List result = new ArrayList(1);
		IJavaInstance delegate_control = (IJavaInstance) ((IJavaInstance)getModel()).eGet(sf_delegate_control);
		if(delegate_control != null){
			result.add(delegate_control);
			return result;			
		} else {
			return Collections.EMPTY_LIST;
		}
	}
	
	public void setModel(Object aModel) {
		sf_delegate_control = ((EObject)aModel).eClass().getEStructuralFeature(SwtPlugin.DELEGATE_CONTROL);
		super.setModel(aModel);
	}
	
	protected EditPart createChildEditPart(Object model) {
		EditPart childEP = super.createChildEditPart(model);
		childEP.installEditPolicy(EditPolicy.CONTAINER_ROLE, new WorkbenchParentArgumentEditPolicy());	//This will override the Tree_Container_Role that is added by the child.
		// Create special property source adapter for the only child to return no descriptors
		if (childEP instanceof ControlTreeEditPart)
			((ControlTreeEditPart)childEP).setPropertySource(new WorkbenchPartChildPropertySourceAdapter());
		return childEP;
	}

}
