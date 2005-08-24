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
 *  $RCSfile: CompanyContentsGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.examples.cdm.dept.*;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.EditPart;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
/**
 * The Contents Graphical Edit Part for a Company.
 */
public class CompanyContentsGraphicalEditPart extends ContentsGraphicalEditPart {
	
	protected PropertyChangeListener companyListener;
	
	public CompanyContentsGraphicalEditPart(Company company) {
		setModel(company);
	}
	
	protected List getModelChildren() {
		Company company = (Company) getModel();
		return company.getDepartments();
	}
	
	/**
	 * We want a TitleBarBorder on the figure that is normally the freeform surface.
	 */
	protected IFigure createFigure() {
		IFigure fig = super.createFigure();
		TitleBarBorder border = new TitleBarBorder();
		border.setBackgroundColor(ColorConstants.lightBlue);
		border.setPadding(new Insets(10, 10, 0, 0));
		fig.setBorder(border);
		return fig;
	}

	public void activate() {
		super.activate();
		Company company = (Company) getModel();
		companyListener = new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		    	if (Company.DEPARTMENTS.equals(evt.getPropertyName()))
		    		refreshChildren();
		    	else if (Company.COMPANY_NAME.equals(evt.getPropertyName()))
		    		refreshCompanyName((String) evt.getNewValue());
		    }
		};
		company.addPropertyChangeListener(companyListener);
	}
	
	public void deactivate() {
		super.deactivate();
		((Company) getModel()).removePropertyChangeListener(companyListener);
	}
	
	protected void createEditPolicies() {
		VisualInfoXYLayoutEditPolicy ep = new VisualInfoXYLayoutEditPolicy(new CompanyContainerPolicy(EditDomain.getEditDomain(this)));
		ep.setZoomable(true);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, ep);
	}
	
	protected void refreshChildren() {
		// This method is here so that it is visible to the companyListener.
		super.refreshChildren();
	}
	
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshCompanyName(((Company) getModel()).getName());
	}
	
	private void refreshCompanyName(String newName) {
		TitleBarBorder border = (TitleBarBorder) getFigure().getBorder();
		border.setLabel(newName);
		getFigure().repaint();
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
		return new DepartmentGraphicalEditPart(childModel);
	}	
		

}
