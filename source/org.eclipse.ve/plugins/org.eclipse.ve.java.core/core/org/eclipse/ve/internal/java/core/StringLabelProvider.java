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
 *  Created May 23, 2005 by Gili Mendel
 * 
 *  $RCSfile: StringLabelProvider.java,v $
 *  $Revision: 1.5 $  $Date: 2006-08-18 14:35:10 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.text.MessageFormat;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.propertysheet.PropertysheetMessages;
 

public class StringLabelProvider extends DefaultJavaBeanLabelProvider {
	public String getText(Object element) {
		String str = super.getText(element);
		if (element instanceof IJavaInstance) {
		   IBeanProxy h = BeanProxyUtilities.getBeanProxy((IJavaInstance)element, EMFEditDomainHelper.getResourceSet(domain));
		   String post = h != null ? h.toBeanString() : PropertysheetMessages.display_null;
		   str = MessageFormat.format(JavaMessages.StringLabelProvider_BeanName_Extension, new Object[] { str, post });		   
		}		
		return str;
	}
}
