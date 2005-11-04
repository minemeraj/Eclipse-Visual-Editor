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
 *  $RCSfile: DepartmentContainerPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-11-04 17:30:46 $ 
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
	public Result getAddCommand(List children, Object position) {
		Result result = new Result(children);
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Employee)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
			if (container.equals(((Employee) child).getManages())) {
				result.setCommand(UnexecutableCommand.INSTANCE);	// Can't add an employee to the same she manages.
				return result;
			}
		}
		
		result.setCommand(new AddEmployeesCommand((Department) container, children, (Employee) position));
		return result;
	}
	
	/**
	 * Create a  child.
	 */
	public Result getCreateCommand(List children, Object position) {
		Result result = new Result(children);
		for (Iterator itr=children.iterator(); itr.hasNext(); ) {
			if (!(itr.next() instanceof Employee)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
		}
			
		Department parent = (Department) container;
		CompoundCommand cc = new CompoundCommand();
		cc.append(new UniqueEmployeeName(parent.getCompany(), children));	// Since this is a new employee, need to verify the name is unique.
		cc.append(new AddEmployeesCommand(parent, children, (Employee) position));
		result.setCommand(AnnotationPolicy.getCreateRequestCommand(AnnotationPolicy.getAllAnnotations(new ArrayList(), children, domain.getAnnotationLinkagePolicy()), cc, domain));
		return result;
	}
	
	/**
	 * Delete a  child.
	 */
	public Result getDeleteDependentCommand(List children) {
		Result result = new Result(children);
		for (Iterator itr=children.iterator(); itr.hasNext(); ) {
			if (!(itr.next() instanceof Employee)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
		}
		
		Department parent = (Department) container;
		List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), children, domain.getAnnotationLinkagePolicy());
		Command cmd = new RemoveEmployeesCommand(parent, children);
		for (Iterator itr = children.iterator(); itr.hasNext();) {
			Employee child = (Employee) itr.next();
			if (child.getManages() != null) {
				// Need to unmanage it too.
				RestoreDefaultPropertyValueCommand umCmd = new RestoreDefaultPropertyValueCommand();
				umCmd.setTarget(PropertySupport.getPropertySource(child.getManages()));
				umCmd.setPropertyId(Department.MANAGER);
				cmd = umCmd.chain(cmd); // Remove as manager, then remove employee.
				// Remove any annotation associated with the manager connection
				Annotation mgrConn = domain.getAnnotationLinkagePolicy().getAnnotation(
						new CompanyAnnotationLinkagePolicy.ManagedConnection(child.getManages()));
				if (mgrConn != null)
					annotations.add(mgrConn);

			}
		}
		result.setCommand(AnnotationPolicy.getDeleteDependentCommand(annotations, cmd, domain.getDiagramData()));
		return result;
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
	protected Result getOrphanTheChildrenCommand(List children) {
		Result result = new Result(children);
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Employee)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
		}
		result.setCommand(new RemoveEmployeesCommand((Department) container, children));
		return result;
	}	
}
