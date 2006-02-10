/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CompositePropertySourceAdapter.java,v $
 *  $Revision: 1.9 $  $Date: 2006-02-10 21:53:46 $ 
 */

package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;

import org.eclipse.ve.internal.java.core.BeanUtilities;

import org.eclipse.ve.swt.common.SWTBeanInfoConstants;


/**
 * Property source adapter for composites. It will remove the layout feature for those that the BeanInfo indicates should not allow change of layout.
 * 
 * @since 1.2.0
 */
public class CompositePropertySourceAdapter extends ControlPropertySourceAdapter {
	
	protected boolean includeFeature(EStructuralFeature sfeature) {
		
		// Do not include the "layout" feature if the bean decorator says layout should not be changed.
		if (sfeature.getName().equals("layout")) { //$NON-NLS-1$
			FeatureAttributeValue defLayout = BeanUtilities.getSetBeanDecoratorFeatureAttributeValue(getBean().getJavaType(),
					SWTBeanInfoConstants.DEFAULT_LAYOUT);
			if (defLayout != null && defLayout.getValue() instanceof Boolean && !((Boolean) defLayout.getValue()).booleanValue())
				return false;
		}		
		return super.includeFeature(sfeature);
	}
}
