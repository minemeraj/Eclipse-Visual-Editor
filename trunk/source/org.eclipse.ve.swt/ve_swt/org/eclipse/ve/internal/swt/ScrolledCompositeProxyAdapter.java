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
 *  $Revision: 1.2 $  $Date: 2005-05-11 22:41:37 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.java.core.*;


public class ScrolledCompositeProxyAdapter extends CompositeProxyAdapter {

	private EReference sf_containerContent;

	public ScrolledCompositeProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sf_containerContent = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_SCROLLEDCOMPOSITE_CONTENT);
		}
	}

	public void releaseBeanProxy() {
		if (isBeanProxyInstantiated()) {
            IJavaInstance content = (IJavaInstance) ((IJavaObjectInstance) getTarget()).eGet(sf_containerContent);
			
			IBeanProxyHost value = BeanProxyUtilities.getBeanProxyHost(content);
			if (value != null)
				value.releaseBeanProxy();
		}
		super.releaseBeanProxy();
	}
}
