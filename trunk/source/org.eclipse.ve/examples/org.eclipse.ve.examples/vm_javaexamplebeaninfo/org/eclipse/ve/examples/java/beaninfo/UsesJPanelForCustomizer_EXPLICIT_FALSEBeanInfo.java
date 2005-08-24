/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

import org.eclipse.ve.examples.java.vm.UsesJPanelForCustomizer_EXPLICIT_FALSE;

public class UsesJPanelForCustomizer_EXPLICIT_FALSEBeanInfo extends SimpleBeanInfo {
/**
 * Get the bean descriptor with the customizer 
 */
public BeanDescriptor getBeanDescriptor(){
	  BeanDescriptor result = new BeanDescriptor(UsesJPanelForCustomizer_EXPLICIT_FALSE.class,SwingJPanelCustomizer.class);
	  return result;
}
}
