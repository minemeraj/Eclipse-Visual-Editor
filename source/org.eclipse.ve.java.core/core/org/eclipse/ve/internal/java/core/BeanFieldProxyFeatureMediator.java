package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanFieldProxyFeatureMediator.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:54 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;

public class BeanFieldProxyFeatureMediator implements IBeanProxyFeatureMediator {

	public void applied(EStructuralFeature sf, IBeanProxy host, IBeanProxy settingBean) {
		BeanProxyUtilities.setBeanField(sf.getName(), host, settingBean);
	}

	public IBeanProxy getValue(EStructuralFeature sf, IBeanProxy aHost) {
		return BeanProxyUtilities.getBeanField(sf.getName(), aHost);
	}

}