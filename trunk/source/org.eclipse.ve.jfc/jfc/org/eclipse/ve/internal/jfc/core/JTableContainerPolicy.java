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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JTableContainerPolicy.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:09 $ 
 */


import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBooleanBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;
/**
 * Container Edit Policy for Bean Compositions.
 */
public class JTableContainerPolicy extends BaseJavaContainerPolicy {
	
	EStructuralFeature sfAutoCreateColumns;
	
	public JTableContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), JFCConstants.SF_JTABLE_COLUMNS),domain);
		
		sfAutoCreateColumns = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), JFCConstants.SF_JTABLE_AUTOCREATECOLUMNSFROMMODEL);		
	}
	/**
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#isValidChild(Object, EStructuralFeature)
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		// We'll add in the check that the table has autoCreateColumns false. Don't want to add columns if it is true.
		if (super.isValidChild(child, containmentSF)) {
			IBeanProxyHost jtable = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getContainer());
			if (!jtable.isBeanProxyInstantiated())
				return false;	// It can't be instantiated, so don't allow adds, we just don't know anything.
			IBeanProxy proxy = jtable.getBeanPropertyProxyValue(sfAutoCreateColumns);
			// In case there was an instantiation error, proxy will be null.
			return (proxy instanceof IBooleanBeanProxy) ? !((IBooleanBeanProxy) proxy).booleanValue() : false;
		}
		return false;
	}
	
	public Command getCreateCommand(Object constraintComponent, Object childComponent, Object position) {
		return getCreateCommand(childComponent, position);
	}
	
	public Command getAddCommand(List componentConstraints, List childrenComponents, Object position) {
		return getAddCommand(childrenComponents, position);
	}
}
