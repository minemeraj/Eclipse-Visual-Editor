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
 *  $RCSfile: DepartmentGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.*;
import org.eclipse.ve.examples.cdm.dept.*;
import org.eclipse.draw2d.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.EditPart;
import org.eclipse.ve.internal.cde.core.*;
import java.util.*;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
/**
 * Graphical editpart for a department.
 */
public class DepartmentGraphicalEditPart extends AbstractGraphicalEditPart implements NodeEditPart {
	private String deptName;
	private String managerName;
	protected PropertyChangeListener departmentListener, managerListener;
	protected Employee manager;
	
	public DepartmentGraphicalEditPart(Department model) {
		setModel(model);
	}

	protected List getModelChildren() {
		return ((Department) getModel()).getEmployees();
	}
	
	public void activate() {
		super.activate();
		Department department = (Department) getModel();
		departmentListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
			if (Department.EMPLOYEES.equals(evt.getPropertyName()))
		    		refreshChildren();
		    	else if (Department.DEPARTMENT_NAME.equals(evt.getPropertyName()))
					refreshDeptName((String) evt.getNewValue());
		    	else if (Department.MANAGER.equals(evt.getPropertyName())) {
		    		refreshManager((Employee) evt.getNewValue());
		    	}
		    }
		};
		department.addPropertyChangeListener(departmentListener);
		if (department.getManager() != null)
			listenManager(department.getManager());
	}
	
	protected void refreshSourceConnections() {
		// This is here so that EmployeeGraphicalEditPart can call it when its manages property changes.
		super.refreshSourceConnections();
	}
	
	public void deactivate() {
		super.deactivate();
		((Department) getModel()).removePropertyChangeListener(departmentListener);
		listenManager(null);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy(new DepartmentContainerPolicy(EditDomain.getEditDomain(this))));
	}
	
	protected IFigure createFigure() {
		Figure f = new LabeledContainer(new FrameBorder());
		((TitleBarBorder) ((FrameBorder) f.getBorder()).getInnerBorder()).setBackgroundColor(ColorConstants.lightGreen);
		f.setLayoutManager( new FlowLayout(false) );
		return f;
	}

	protected List getModelSourceConnections(){
		// Get the model source connections. This is the ID's of all of the connections that have 
		// an employee that is a manager within this department.
		ArrayList sources = new ArrayList();
		Iterator employees = ((Department) getModel()).getEmployees().iterator();
		while (employees.hasNext()) {
			Employee emp = (Employee) employees.next();
			Department dept = emp.getManages();
			if (dept != null) {
				sources.add(new CompanyAnnotationLinkagePolicy.ManagedConnection(dept));
			}
		}
		
		return sources;
	}
	
	protected List getModelTargetConnections(){
		// Get the model target connections. This is the ID of this department, if it has a manager.
		Employee manager =  ((Department) getModel()).getManager();
		if (manager == null || manager.getDepartment() == null)
			return Collections.EMPTY_LIST;	// No manager, or the manager is not in any dept (this occurs only for top-level department.
			
		ArrayList targets = new ArrayList(1);
		targets.add(new CompanyAnnotationLinkagePolicy.ManagedConnection((Department) getModel()));
		return targets;
	}
		
				
	protected void refreshChildren() {
		super.refreshChildren();
	}
	
	protected void refreshVisuals() {
		super.refreshVisuals();
		Employee manager = ((Department) getModel()).getManager();
		if (manager != null)
			managerName = manager.getName();
		else
			managerName = null;
		deptName = ((Department) getModel()).getDepartmentName();
		setTitle();
	}
	
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class)
			return PropertySupport.getPropertySource(getModel());
		else
			return super.getAdapter(key);
	}
	
	private void setTitle() {
		String deptString = deptName != null ? deptName : "?";
		String mngString = managerName != null ? managerName : "?";
		((LabeledContainer) getFigure()).setLabel(deptString + ": " + mngString);
		getFigure().repaint();

	}
	
	private void refreshDeptName(String deptName) {
		this.deptName = deptName;
		setTitle();
	}
	
	private void listenManager(Employee manager) {
		if (managerListener != null) {
			this.manager.removePropertyChangeListener(Employee.NAME, managerListener);
			this.manager = null;
			managerListener = null;
		}
		
		this.manager = manager;
		if (manager != null) {
			managerListener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
				    	if (Employee.NAME.equals(evt.getPropertyName()))
				    		refreshManagerName((String) evt.getNewValue());
				}
			};
			manager.addPropertyChangeListener(Employee.NAME, managerListener);
		}
	}
	
	private void refreshManager(Employee manager) {
		listenManager(manager);
		if (manager != null) {
			refreshManagerName(manager.getName());
		} else
			refreshManagerName(null);
			
		refreshTargetConnections();	// New manager or no manager means new target connection
	}
	
	private void refreshManagerName(String name) {
		managerName = name;
		setTitle();
	}

	/**
	 * Create the child edit part. It will be a Department in our case.
	 */
	protected EditPart createChild(Object child) {
		Employee childModel = (Employee) child;
		return new EmployeeGraphicalEditPart(childModel);
	}
	
	protected ConnectionEditPart createConnection(Object managedConnection){
		ConnectionEditPart connectionEP = (ConnectionEditPart)getRoot().getViewer().getEditPartRegistry().get(managedConnection);
		if (connectionEP == null) {
			connectionEP = new ManagedConnectionEditPart();
			connectionEP.setModel(managedConnection);
		}
		return connectionEP;
	}
	
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}
	
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connEditPart) {
		return new ChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}
	
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connEditPart) {
		return new ChopboxAnchor(getFigure());
	}



}
