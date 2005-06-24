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
 *  $RCSfile: DepartmentContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-24 18:57:16 $ 
 */

import java.util.*;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.examples.cdm.dept.Employee;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
import org.eclipse.ve.examples.cdm.dept.property.UniqueEmployeeName;

import org.eclipse.ve.internal.propertysheet.command.RestoreDefaultPropertyValueCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
/**
 * Container policy for Company.
 */
public class DepartmentContainerPolicy extends ContainerPolicy {
	
	public DepartmentContainerPolicy(EditDomain domain) {
		super(domain);
	}
	
	/**
	 * Add a  child.
	 */
	public Command getAddCommand(List children, Object position) {
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Employee))
				return UnexecutableCommand.INSTANCE;
			if (container.equals(((Employee) child).getManages()))
				return UnexecutableCommand.INSTANCE;	// Can't add an employee to the same she manages.			
		}
		
		return new AddEmployeesCommand((Department) container, children, (Employee) position);
	}
	
	/**
	 * Create a  child.
	 */
	public Command getCreateCommand(Object child, Object position) {
		if (!(child instanceof Employee))
			return UnexecutableCommand.INSTANCE;
			
		Department parent = (Department) container;
		List children = Collections.singletonList(child);
		CompoundCommand cc = new CompoundCommand();
		cc.append(new UniqueEmployeeName(parent.getCompany(), (Employee) child));	// Since this is a new employee, need to verify the name is unique.
		cc.append(new AddEmployeesCommand(parent, children, (Employee) position));
		return AnnotationPolicy.getCreateRequestCommand(AnnotationPolicy.getAllAnnotations(new ArrayList(), child, domain.getAnnotationLinkagePolicy()), cc, domain);
	}
	
	/**
	 * Delete a  child.
	 */
	public Command getDeleteDependentCommand(Object child) {
		if (!(child instanceof Employee))
			return UnexecutableCommand.INSTANCE;
		
		Department parent = (Department) container;
		List list = Collections.singletonList(child);
		List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), child, domain.getAnnotationLinkagePolicy());
		Command cmd = new RemoveEmployeesCommand(parent, list);
		if (((Employee) child).getManages() != null) {
			// Need to unmanage it too.
			RestoreDefaultPropertyValueCommand umCmd = new RestoreDefaultPropertyValueCommand();
			umCmd.setTarget(PropertySupport.getPropertySource(((Employee) child).getManages()));
			umCmd.setPropertyId(Department.MANAGER);
			cmd = umCmd.chain(cmd);	// Remove as manager, then remove employee.
			// Remove any annotation associated with the manager connection
			Annotation mgrConn = domain.getAnnotationLinkagePolicy().getAnnotation(new CompanyAnnotationLinkagePolicy.ManagedConnection(((Employee) child).getManages()));
			if (mgrConn != null)
				annotations.add(mgrConn);

		}
		return AnnotationPolicy.getDeleteDependentCommand(annotations, cmd, domain.getDiagramData());
	}
	
	/**
	 * Move the  children.
	 */
	public Command getMoveChildrenCommand(List children, Object position) {
		CommandBuilder cBld = new CommandBuilder("");
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Employee))
				return UnexecutableCommand.INSTANCE;
		}
		
		if (children.contains(position))
			return UnexecutableCommand.INSTANCE;
			
		Department parent = (Department) container;
		cBld.append(new RemoveEmployeesCommand(parent, children));
		cBld.append(new AddEmployeesCommand(parent, children, (Employee) position));
		return cBld.getCommand();
	}
	
	/**
	 * Orphan  children.
	 */
	protected Command getOrphanTheChildrenCommand(List children) {
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Employee))
				return UnexecutableCommand.INSTANCE;
		}
		return new RemoveEmployeesCommand((Department) container, children);
	}	
}