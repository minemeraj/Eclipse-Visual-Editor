/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LayoutDataPropertyDescriptor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-04 19:28:57 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapterFactory;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.BeanPropertyDescriptorAdapter;
import org.eclipse.ve.internal.java.core.BeanPropertySourceAdapter;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourcedPropertyDescriptor;
 
/**
 * For a Composite that is in RowLayout there might not be any RowData on its controls.  Likewise for GridData and GridData, etc...
 * For the property sheet however we want the user to be able to expand layoutData and set a value on it which in a single gesture
 * creates a layoutData of the right type and sets the sub-propety value
 * This is unlike normal property sheet behavior where  
 * 
 * @since 1.0.0
 */
public class LayoutDataPropertyDescriptor extends BeanPropertyDescriptorAdapter implements ISourcedPropertyDescriptor  {
	
	private String layoutDataClassName;
	private IPropertyDescriptor propertyDescriptor;
	private EditDomain fEditDomain;

	public LayoutDataPropertyDescriptor(String aLayoutDataClassName){
		layoutDataClassName = aLayoutDataClassName;
	}

	public Object getValue(IPropertySource source) {
		// See whether the actual value is set
		Object actualValue = source.getPropertyValue(propertyDescriptor.getId());
		// If there is no layout data class name return null		
		if(actualValue != null || layoutDataClassName == null) return actualValue;
		// If we have no value then create a dummy one based on the layoutData class so the property sheet gets
		// faked out into thinking there is a child which allows sub properties to be set
		String initString = "new " + layoutDataClassName + "()";
		// To allow expandable properties we must return the fake layout data object wrapped up to be a valid IPropertySource
		ResourceSet rs = JavaEditDomainHelper.getResourceSet(fEditDomain);
		IJavaInstance fakeLayoutData = BeanUtilities.createJavaObject(
				layoutDataClassName,rs,initString);	
		Object propertySourceAdpterType = IPropertySource.class;
		AdapterFactory factory =
			EcoreUtil.getAdapterFactory(
				rs.getAdapterFactories(),
				propertySourceAdpterType);
		return factory.adaptNew(fakeLayoutData,propertySourceAdpterType);		
	}

	public boolean isSet(IPropertySource source) {
		return source.isPropertySet(propertyDescriptor.getId());
	}

	public void setPropertyDescriptor(IPropertyDescriptor pd) {
		propertyDescriptor = pd;
		setTarget(((BeanPropertyDescriptorAdapter)pd).getTarget());
	}

	public void setEditDomain(EditDomain domain) {
		fEditDomain = domain;
	}

	public ILabelProvider getLabelProvider() {
		return new JavaClassLabelProvider();
	}
	
}
