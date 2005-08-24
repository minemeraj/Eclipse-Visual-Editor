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
package org.eclipse.ve.examples.cdm.dept.ui;
/*
 *  $RCSfile: CompanyContentsTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy;
import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
/**
 * Company Edit Part for the Tree Viewer.
 */
public class CompanyContentsTreeEditPart extends AbstractTreeEditPart {

	protected PropertyChangeListener companyListener;
			
	public CompanyContentsTreeEditPart(Company model) {
		setModel(model);
	}
	
	protected List getModelChildren() {
		Company company = (Company) getModel();
		return company.getDepartments();
	}
	
	public void activate() {
		super.activate();
		Company company = (Company) getModel();
		companyListener = new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		    	if (Company.DEPARTMENTS.equals(evt.getPropertyName()))
		    		refreshChildren();
		    }
		};
		company.addPropertyChangeListener(Company.DEPARTMENTS, companyListener);
	}
	
	public void deactivate() {
		super.deactivate();
		((Company) getModel()).removePropertyChangeListener(Company.DEPARTMENTS, companyListener);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new TreeContainerEditPolicy(new CompanyContainerPolicy(EditDomain.getEditDomain(this))));
	}
	
	protected void refreshChildren() {
		// This method is here so that it is visible to the companyListener.
		super.refreshChildren();
	}
	
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class)
			return PropertySupport.getPropertySource(getModel());
		else
			return super.getAdapter(key);
	}	
					
	/**
	 * Create the child edit part. It will be a Department in our case.
	 */
	protected EditPart createChild(Object child) {
		Department childModel = (Department) child;
		return new DepartmentTreeEditPart(childModel);
	}

}
