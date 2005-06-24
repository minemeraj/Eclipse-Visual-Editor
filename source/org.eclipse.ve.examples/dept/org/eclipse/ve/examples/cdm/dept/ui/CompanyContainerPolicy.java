package org.eclipse.ve.examples.cdm.dept.ui;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CompanyContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-24 18:57:16 $ 
 */

import java.util.*;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.examples.cdm.dept.*;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
import org.eclipse.ve.examples.cdm.dept.property.UniqueDepartmentName;

import org.eclipse.ve.internal.propertysheet.command.RestoreDefaultPropertyValueCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
/**
 * Container policy for Company.
 */
public class CompanyContainerPolicy extends ContainerPolicy {
	
	public CompanyContainerPolicy(EditDomain domain) {
		super(domain);
	}
	
	/**
	 * Add a  child.
	 */
	public Command getAddCommand(List children, Object position) {
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Department))
				return UnexecutableCommand.INSTANCE;
		}
		
		return new AddDepartmentsCommand((Company) container, children, (Department) position);
	}
	
	/**
	 * Create a  child.
	 */
	public Command getCreateCommand(Object child, Object position) {
		if (!(child instanceof Department))
			return UnexecutableCommand.INSTANCE;
			
		Company parent = (Company) container;
		Department dept = (Department) child;
		
		List children = new ArrayList(1);
		children.add(child);
		CompoundCommand cc = new CompoundCommand();
		cc.append(new UniqueDepartmentName(parent, dept));	// Since this is a new department, need to verify the names are unique.
		cc.append(new AddDepartmentsCommand(parent, children, (Department) position));
		return AnnotationPolicy.getCreateRequestCommand(AnnotationPolicy.getAllAnnotations(new ArrayList(), child, domain.getAnnotationLinkagePolicy()), cc, domain);
	}
	
	/**
	 * Delete a  child.
	 */
	public Command getDeleteDependentCommand(Object child) {
		if (!(child instanceof Department))
			return UnexecutableCommand.INSTANCE;

		List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), child, domain.getAnnotationLinkagePolicy());
				
		CompoundCommand cmd = new CompoundCommand();	
		Company parent = (Company) container;
		List list = Collections.singletonList(child);
		cmd.append(new RemoveDepartmentsCommand(parent, list));
		if (((Department) child).getManager() != null) {
			// Need to cancel out the manager selection so that the manager doesn't point to a non-existent department.
			RestoreDefaultPropertyValueCommand reset = new RestoreDefaultPropertyValueCommand();
			reset.setTarget(PropertySupport.getPropertySource(child));
			reset.setPropertyId(Department.MANAGER);
			cmd.append(reset);
		}
		
		// Also, any employees of this department that are managers need to be unset also.
		Iterator itr = ((Department) child).getEmployees().iterator();
		while (itr.hasNext()) {
			Employee emp = (Employee) itr.next();
			if (emp.getManages() != null) {
				// Need to cancel out the manager selection so that the manager doesn't point to a non-existent employee.
				RestoreDefaultPropertyValueCommand reset = new RestoreDefaultPropertyValueCommand();
				reset.setTarget(PropertySupport.getPropertySource(emp.getManages()));
				reset.setPropertyId(Department.MANAGER);
				cmd.append(reset);
				Annotation mgrConn = domain.getAnnotationLinkagePolicy().getAnnotation(new CompanyAnnotationLinkagePolicy.ManagedConnection(emp.getManages()));
				if (mgrConn != null)
					annotations.add(mgrConn);
			}
		}
		
		// Also need to delete any annotations associated with the manager connection.
		Annotation mgrConn = domain.getAnnotationLinkagePolicy().getAnnotation(new CompanyAnnotationLinkagePolicy.ManagedConnection((Department) child));
		if (mgrConn != null)
			annotations.add(mgrConn);

		return AnnotationPolicy.getDeleteDependentCommand(annotations, cmd.unwrap(), domain.getDiagramData());
	}
	
	/**
	 * Move the  children.
	 */
	public Command getMoveChildrenCommand(List children, Object position) {
		CommandBuilder cBld = new CommandBuilder("");
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Department))
				return UnexecutableCommand.INSTANCE;
		}
		
		if (children.contains(position))
			return UnexecutableCommand.INSTANCE;
			
		Company parent = (Company) container;
		cBld.append(new RemoveDepartmentsCommand(parent, children));
		cBld.append(new AddDepartmentsCommand(parent, children, (Department) position));
		return cBld.getCommand();
	}
	
	/**
	 * Orphan  children.
	 */
	protected Command getOrphanTheChildrenCommand(List children) {
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Department))
				return UnexecutableCommand.INSTANCE;
		}
		return new RemoveDepartmentsCommand((Company) container, children);
	}	
}