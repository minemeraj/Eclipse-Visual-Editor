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
 *  $RCSfile: CompanyContainerPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-11-04 17:30:47 $ 
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
	public Result getAddCommand(List children, Object position) {
		Result result = new Result(children);
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Department)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
		}
		
		result.setCommand(new AddDepartmentsCommand((Company) container, children, (Department) position));
		return result;
	}
	
	/**
	 * Create a  child.
	 */
	public Result getCreateCommand(List children, Object position) {
		Result result = new Result(children);
		for (Iterator itr=children.iterator(); itr.hasNext(); ) {
			if (!(itr.next() instanceof Department)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
		}
			
		Company parent = (Company) container;
		
		CompoundCommand cc = new CompoundCommand();
		cc.append(new UniqueDepartmentName(parent, children));	// Since this is a new department, need to verify the names are unique.
		cc.append(new AddDepartmentsCommand(parent, children, (Department) position));
		result.setCommand(AnnotationPolicy.getCreateRequestCommand(AnnotationPolicy.getAllAnnotations(new ArrayList(), children, domain.getAnnotationLinkagePolicy()), cc, domain));
		return result;
	}
	
	/**
	 * Delete a  child.
	 */
	public Result getDeleteDependentCommand(List children) {
		Result result = new Result(children);
		for (Iterator itr=children.iterator(); itr.hasNext(); ) {
			if (!(itr.next() instanceof Department)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
		}

		List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), children, domain.getAnnotationLinkagePolicy());
				
		CompoundCommand cmd = new CompoundCommand();	
		Company parent = (Company) container;
		cmd.append(new RemoveDepartmentsCommand(parent, children));
		for (Iterator itr = children.iterator(); itr.hasNext();) {
			Department child = (Department) itr.next();
			if (child.getManager() != null) {
				// Need to cancel out the manager selection so that the manager doesn't point to a non-existent department.
				RestoreDefaultPropertyValueCommand reset = new RestoreDefaultPropertyValueCommand();
				reset.setTarget(PropertySupport.getPropertySource(child));
				reset.setPropertyId(Department.MANAGER);
				cmd.append(reset);
			}
			
			// Also, any employees of this department that are managers need to be unset also.
			Iterator itrEmployees = child.getEmployees().iterator();
			while (itrEmployees.hasNext()) {
				Employee emp = (Employee) itrEmployees.next();
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
			Annotation mgrConn = domain.getAnnotationLinkagePolicy().getAnnotation(new CompanyAnnotationLinkagePolicy.ManagedConnection(child));
			if (mgrConn != null)
				annotations.add(mgrConn);
		}
		
		

		result.setCommand(AnnotationPolicy.getDeleteDependentCommand(annotations, cmd.unwrap(), domain.getDiagramData()));
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
	protected Result getOrphanTheChildrenCommand(List children) {
		Result result = new Result(children);
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!(child instanceof Department)) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
		}
		result.setCommand(new RemoveDepartmentsCommand((Company) container, children));
		return result;
	}	
}
