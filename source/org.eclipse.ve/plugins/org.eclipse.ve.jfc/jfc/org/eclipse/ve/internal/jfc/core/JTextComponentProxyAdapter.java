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
/*
 *  $RCSfile: JTextComponentProxyAdapter.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:10 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * JTextComponent proxy adapter.
 * 
 * @since 1.1.0
 */
public class JTextComponentProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfSelectionStart, sfSelectionEnd;

	/**
	 * Construct JTextComponentProxyAdapter
	 * 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public JTextComponentProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfSelectionStart = JavaInstantiation.getSFeature(rset, URI.createURI("java:/javax.swing.text#JTextComponent/selectionStart")); //$NON-NLS-1$
		sfSelectionEnd = JavaInstantiation.getSFeature(rset, URI.createURI("java:/javax.swing.text#JTextComponent/selectionEnd")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int,
	 *      org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void canceled(EStructuralFeature feature, Object value, int index, IExpression expression) {
		// If selection start or selection end are canceled, need to reinstantiate,
		// so do apply if feature is neither start nor end, or reinstantiate didn't occur.
		if ((feature != sfSelectionStart && feature != sfSelectionEnd) || !reinstantiate(expression))
			super.canceled(feature, value, index, expression);
	}

}
