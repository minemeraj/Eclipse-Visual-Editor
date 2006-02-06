/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ExpandableCompositeProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:41 $ 
 */
package org.eclipse.ve.internal.forms;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

import org.eclipse.ve.internal.swt.CompositeProxyAdapter;
 

/**
 * ExpandableComposite proxy adapter.
 * @since 1.2.0
 */
public class ExpandableCompositeProxyAdapter extends CompositeProxyAdapter {

	/**
	 * @param domain
	 * 
	 * @since 1.2.0
	 */
	public ExpandableCompositeProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter#primCanceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void primCanceled(EStructuralFeature feature, Object value, int index, IExpression expression) {
		// Can't cancel client. setClient(null) is an error. We need reinstantiate instead.
		if (!feature.getName().equals("client"))
			super.primCanceled(feature, value, index, expression);
		else
			reinstantiate(expression);
	}	

}
