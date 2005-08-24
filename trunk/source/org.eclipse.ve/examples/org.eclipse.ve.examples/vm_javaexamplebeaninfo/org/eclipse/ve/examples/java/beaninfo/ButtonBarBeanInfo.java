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

import org.eclipse.ve.examples.java.vm.ButtonBar;
/**
 * Insert the type's description here.
 * Creation date: (07/11/00 10:16:39 AM)
 * @author: Joe Winchester
 */
public class ButtonBarBeanInfo extends SimpleBeanInfo {
/**
 * Get the bean descriptor with the customizer
 */
public BeanDescriptor getBeanDescriptor(){
	return new BeanDescriptor(ButtonBar.class,ButtonBarCustomizer.class);
}
}
