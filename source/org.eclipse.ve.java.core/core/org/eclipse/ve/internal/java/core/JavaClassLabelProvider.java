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
 *  $RCSfile: JavaClassLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-04 19:29:45 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

public class JavaClassLabelProvider extends LabelProvider {
	
public String getText(Object element){
	
	if (element instanceof IJavaObjectInstance) {
		String className = ((IJavaObjectInstance)element).getJavaType().getQualifiedName();
		return BeanProxyUtilities.getUnqualifiedClassName(className);
	} else
		return super.getText(element);
}
}