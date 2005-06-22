/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LayoutDataPropertyDescriptor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-22 16:03:02 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.*;
 
/**
 * For a Composite that is in RowLayout there might not be any RowData on its controls.  Likewise for GridData and GridData, etc...
 * For the property sheet however we want the user to be able to expand layoutData and set a value on it which in a single gesture
 * creates a layoutData of the right type and sets the sub-propety value
 * This is unlike normal property sheet behavior where  
 * 
 * @since 1.0.0
 */
public class LayoutDataPropertyDescriptor extends EToolsPropertyDescriptor implements ISourcedPropertyDescriptor, INeedData  {
	
	private String layoutDataClassName;
	private EditDomain fEditDomain;
	

	public LayoutDataPropertyDescriptor(String aLayoutDataClassName, EStructuralFeature sf, boolean nullsInvalid){
	    super(sf, "layoutData"); //$NON-NLS-1$
	    setNullInvalid(nullsInvalid);
		layoutDataClassName = aLayoutDataClassName;
	}

	public Object getValue(IPropertySource source) {
		// See whether the actual value is set
		Object actualValue = source.getPropertyValue(getId());
		// If there is no layout data class name return null		
		if(actualValue != null || layoutDataClassName == null) return actualValue;
		// If we have no value then create a dummy one based on the layoutData class so the property sheet gets
		// faked out into thinking there is a child which allows sub properties to be set
		String initString = "new " + layoutDataClassName + "()"; //$NON-NLS-1$ //$NON-NLS-2$
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
		return source.isPropertySet(getId());
	}


	public void setData(Object domain) {
		fEditDomain = (EditDomain)domain;
	}

	public ILabelProvider getLabelProvider() {
		return new JavaClassLabelProvider();
	}

	public boolean isPropertyResettable(IPropertySource source) {
		return true;
	}
	
}
