package org.eclipse.ve.internal.java.vce;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaBeanLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.propertysheet.INeedData;

/**
 */
public class JavaBeanLabelProvider extends LabelProvider implements INeedData {
	
	protected EditDomain editDomain;
	
public String getText(Object element){
	// The label used for the icon is the same one used by the JavaBeans tree view
	if ( element instanceof IJavaObjectInstance && editDomain != null) {
		IJavaObjectInstance javaComponent = (IJavaObjectInstance)element;
		ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(javaComponent.getJavaType());
		if ( labelProvider != null ) {
			return labelProvider.getText(javaComponent);
		} else { 
			// If no label provider exists use the toString of the target VM JavaBean itself
			return BeanProxyUtilities.getBeanProxy(javaComponent).toBeanString();
		}
	}
	return ""; //$NON-NLS-1$
}
public void setData(Object anObject){
	editDomain = (EditDomain) anObject;
}


}
