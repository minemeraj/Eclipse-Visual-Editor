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
 *  $RCSfile: TableProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-03-09 15:36:24 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.*;

/**
 * 
 * @since 1.0.0
 */
public class TableProxyAdapter extends CompositeProxyAdapter {

	protected IMethodProxy headHeightMethodProxy;

	protected IMethodProxy removeColumnMethodProxy;

	private EReference sf_columns;

	public TableProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected void applied(EStructuralFeature as, Object newValue, int position) {
		super.applied(as, newValue, position);
		if (as == sf_columns) {
			IBeanProxyHost valueProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) newValue);
			valueProxyHost.instantiateBeanProxy();
			revalidateBeanProxy();
		} else {
			super.applied(as, newValue, position);
		}
	}

	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if (sf == sf_columns && oldValue instanceof IJavaObjectInstance) {
			IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)oldValue);
			controlProxyHost.releaseBeanProxy();
			revalidateBeanProxy();
		} else
			super.canceled(sf, oldValue, position);
	}

	protected IMethodProxy getHeaderHeightMethodProxy() {
		if (headHeightMethodProxy == null) {
			headHeightMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("getHeaderHeight"); //$NON-NLS-1$
		}
		return headHeightMethodProxy;
	}

	public IIntegerBeanProxy getHeaderHeight() {
		if (isBeanProxyInstantiated()) {
			return (IIntegerBeanProxy) invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy)getHeaderHeightMethodProxy().invoke(getBeanProxy());
					return intProxy;
				}
			});
		} else
			return null;
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sf_columns = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_TABLE_COLUMNS);
		}
	}
}