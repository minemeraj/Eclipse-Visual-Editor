package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaNls.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.ResourceBundle;
/**
 * General VCE Bean NLS Constants
 * Creation date: (4/13/00 10:46:58 AM)
 * @author: Administrator
 */
public interface JavaNls {
	// TODO This needs to be merged into JavaMessages. No need for two of them in the same package. Need to wait until we Translaters available.
	
	// Resource Bundle to use for basic VCE Bean resources.
	static public final ResourceBundle RESBUNDLE = ResourceBundle.getBundle("org.eclipse.ve.internal.java.core.jcm"); //$NON-NLS-1$

	// Keys for messages/strings within the resource bundle.
	static public final String
		
		BEANFEATUREEDITOR_ERRSETWITHMSG = RESBUNDLE.getString("BeanFeatureEditor.errSetWithMsg_ERROR_"), //$NON-NLS-1$
		BEANFEATUREEDITOR_ERRSETWITHNOMSG = RESBUNDLE.getString("BeanFeatureEditor.errSetWithNoMsg_ERROR_"), //$NON-NLS-1$
		BEANCLASS_ERRNOCREATE_WARN_ = "BeanClass.errNoCreate", //$NON-NLS-1$
		CONSTRAINTEDITOR_INVALID = "ConstraintEditor.invalid_ERROR_", //$NON-NLS-1$
		PROXYFACTORY_NOBEANPROXY = "ProxyFactory.noBeanProxy_WARN_", //$NON-NLS-1$
		PROXYFACTORY_NOBEANTYPEPROXY = "ProxyFactory.noBeanTypeProxy_WARN_", //$NON-NLS-1$

		BEANCELLEDITORS_INVALIDVALUE = "BeanCellEditors.invalidValue_ERROR_"; //$NON-NLS-1$
}
