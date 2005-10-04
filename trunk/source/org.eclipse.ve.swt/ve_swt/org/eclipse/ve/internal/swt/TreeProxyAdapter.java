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
 *  $RCSfile: TreeProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 15:41:47 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
 

/**
 * swt Tree proxy adapter.
 * @since 1.2.0
 */
public class TreeProxyAdapter extends CompositeProxyAdapter {

	protected EStructuralFeature sfTreeItems, sfTreeColumns;
	
	/**
	 * @param domain
	 * 
	 * @since 1.2.0
	 */
	public TreeProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfTreeItems = JavaInstantiation.getReference(rset, SWTConstants.SF_TREE_ITEMS);
		sfTreeColumns = JavaInstantiation.getReference(rset, SWTConstants.SF_TREE_COLUMNS);
	}
	
	protected boolean isItemFeature(EStructuralFeature feature) {
		return sfTreeItems == feature || sfTreeColumns == feature;
	}

}
