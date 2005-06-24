/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JProgressBarProxyAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-24 16:45:10 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * JProgressBar proxy adapter.
 * 
 * @since 1.1.0
 */
public class JProgressBarProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfString;

	/**
	 * Construct JProgressBarProxyAdapter
	 * 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public JProgressBarProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfString = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JPROGRESSBAR_STRING);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#cancelSetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		// When 'restoring default' for the 'string' feature, it uses the "original setting" for the string.
		// This isn't right because when in default mode, this feature value actually depends on the value feature.
		// However, setting the feature to "null" will cause this default function to occur. See the setString()
		// of JProgressBar to see this. So we just change the "original setting" to null, and let it proceed.
		if (feature == sfString && isSettingInOriginalSettingsTable(feature))
			setOriginalValue(feature, null);
		super.cancelSetting(feature, oldValue, index, expression);
	}

}