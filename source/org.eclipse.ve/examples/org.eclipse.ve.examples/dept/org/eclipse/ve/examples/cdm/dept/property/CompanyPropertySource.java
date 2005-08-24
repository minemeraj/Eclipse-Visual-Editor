/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.cdm.dept.property;
/*
 *  $RCSfile: CompanyPropertySource.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.ve.examples.cdm.dept.*;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ve.internal.propertysheet.StringPropertyDescriptor;

/**
 * This is the property source for an Company.
 */
public class CompanyPropertySource implements IPropertySource {
	protected Company company;
	protected IPropertyDescriptor[] descriptors;
	
	public CompanyPropertySource(Company company) {
		this.company = company;
	}

	public Object getEditableValue() {
		return company;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = new IPropertyDescriptor[] {
				new StringPropertyDescriptor(Company.COMPANY_NAME, "company name")
			};
		};
				
		return descriptors;
	}
	
	public Object getPropertyValue(Object propertyKey) {
		return (Company.COMPANY_NAME.equals(propertyKey)) ? company.getName() : null;
	}
	
	public boolean isPropertySet(Object propertyKey) {
		if (Company.COMPANY_NAME.equals(propertyKey)) {
			return company.getName() != null;
		}
		return false;
	}
	
	public void resetPropertyValue(Object propertyKey) {
		if (Company.COMPANY_NAME.equals(propertyKey)) {
			company.setName(null);
		}
	}
	
	public void setPropertyValue(Object propertyKey, Object value) {
		if (value instanceof String && Company.COMPANY_NAME.equals(propertyKey)) {
			company.setName((String) value);
		}
	}
	
}


