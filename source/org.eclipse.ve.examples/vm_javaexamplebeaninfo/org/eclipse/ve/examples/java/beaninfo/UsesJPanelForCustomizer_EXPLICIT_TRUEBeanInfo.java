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

import java.beans.*;

import org.eclipse.ve.examples.java.vm.UsesJPanelForCustomizer_EXPLICIT_TRUE;

public class UsesJPanelForCustomizer_EXPLICIT_TRUEBeanInfo extends SimpleBeanInfo {
/**
 * Get the bean descriptor with the customizer 
 */
public BeanDescriptor getBeanDescriptor(){
	  BeanDescriptor result = new BeanDescriptor(UsesJPanelForCustomizer_EXPLICIT_TRUE.class,SwingJPanelCustomizer.class);
	  result.setValue("EXPLICIT_PROPERTY_CHANGE",Boolean.TRUE);
	  return result;
}
}
