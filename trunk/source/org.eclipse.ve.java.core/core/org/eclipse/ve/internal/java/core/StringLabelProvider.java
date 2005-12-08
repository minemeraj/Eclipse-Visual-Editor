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
 *  $Revision: 1.3 $  $Date: 2005-12-08 17:15:54 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.text.MessageFormat;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
 

public class StringLabelProvider extends DefaultJavaBeanLabelProvider {
	public String getText(Object element) {
		String str = super.getText(element);
		if (element instanceof IJavaInstance) {
		   IBeanProxy h = BeanProxyUtilities.getBeanProxy((IJavaInstance)element, EMFEditDomainHelper.getResourceSet(domain));
		   String post = h.toBeanString();
		   str = MessageFormat.format("{0} - \"{1}\"", new Object[] { str, post });		   
		}		
		return str;
	}
}
