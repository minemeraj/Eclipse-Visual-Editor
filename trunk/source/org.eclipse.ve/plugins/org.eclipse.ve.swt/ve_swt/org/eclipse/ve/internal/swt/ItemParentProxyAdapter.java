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
 *  $RCSfile: ItemParentProxyAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2005-05-18 16:48:00 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * 
 * @since 1.0.2
 */
public class ItemParentProxyAdapter extends CompositeProxyAdapter {

	/**
	 * @param domain
	 * 
	 * @since 1.0.2
	 */
	public ItemParentProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.swt.CompositeProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 */
	protected void applied(EStructuralFeature as, Object newValue, int position) {
		// To create the item just instantiate it. It will detect us as its parent because that is part
		// of its parse tree allocation statement
		if (as.getName().equals("items")) { //$NON-NLS-1$
			IBeanProxyHost valueProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) newValue);
			valueProxyHost.instantiateBeanProxy();
		} else {
			super.applied(as, newValue, position);
		}
	}

	public void releaseBeanProxy() {
		if (isBeanProxyInstantiated()) {
			List controls = (List) ((IJavaObjectInstance) getTarget()).eGet(getJavaObject().eClass().getEStructuralFeature("items")); //$NON-NLS-1$
			Iterator iter = controls.iterator();
			while (iter.hasNext()) {
				IBeanProxyHost value = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) iter.next());
				if (value != null)
					value.releaseBeanProxy();
			}
		}
		super.releaseBeanProxy();
	}

}
