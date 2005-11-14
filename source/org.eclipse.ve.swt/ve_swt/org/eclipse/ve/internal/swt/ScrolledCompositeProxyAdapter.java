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
 *  $RCSfile: ScrolledCompositeProxyAdapter.java,v $
 *  $Revision: 1.5 $  $Date: 2005-11-14 22:32:15 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.java.core.*;


public class ScrolledCompositeProxyAdapter extends CompositeProxyAdapter {

	private EReference sf_containerContent;

	public ScrolledCompositeProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sf_containerContent = JavaInstantiation.getReference(rset, SWTConstants.SF_SCROLLEDCOMPOSITE_CONTENT);
	}

	protected void primPrimReleaseBeanProxy(IExpression expression) {
		boolean releaseChild = isBeanProxyInstantiated();
		super.primPrimReleaseBeanProxy(expression);
		if (releaseChild) {
            IJavaInstance content = (IJavaInstance) ((IJavaObjectInstance) getTarget()).eGet(sf_containerContent);
			
			IBeanProxyHost value = getSettingBeanProxyHost(content);
			if (value != null)
				value.releaseBeanProxy(expression);
		}		

	}
}
