/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ViewPartTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-04-03 06:04:11 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;
import org.eclipse.ve.internal.swt.SwtPlugin;


public class ViewPartTreeEditPart extends JavaBeanTreeEditPart {
	
	private EReference sf_delegate_control;	

	public ViewPartTreeEditPart(Object aModel) {
		super(aModel);
	}
	
	public void setModel(Object aModel) {
		super.setModel(aModel);
		ResourceSet rset = ((IJavaObjectInstance) aModel).eResource().getResourceSet();
		sf_delegate_control = JavaInstantiation.getReference(
				rset, 
				URI.createURI("java:/org.eclipse.ui.part#WorkbenchPach/delegate_control"));		 //$NON-NLS-1$						
	}
	
	protected List getChildJavaBeans() {
		List result = new ArrayList(1);
		IJavaInstance delegate_control = BeanUtilities.getFeatureValue((IJavaInstance)getModel(),SwtPlugin.DELEGATE_CONTROL);
		if(delegate_control != null){
			result.add(delegate_control);
			return result;			
		} else {
			return Collections.EMPTY_LIST;
		}
	}

}
