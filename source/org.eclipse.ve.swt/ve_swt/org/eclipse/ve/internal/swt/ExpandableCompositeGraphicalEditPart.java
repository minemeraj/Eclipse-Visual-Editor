/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.ForwardedRequest;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

public class ExpandableCompositeGraphicalEditPart extends CompositeGraphicalEditPart {

	private EStructuralFeature sf_client;

	public ExpandableCompositeGraphicalEditPart(Object model) {
		super(model);
		sf_client = getBean().eClass().getEStructuralFeature("client");
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		// When a child is added it must be a direct child of this composite (that the superclass handles)
		// but it also must be part of the "client" relationship.  Only one "client" can exist per ExpandableComposite
		installEditPolicy("EXPANDABLE_COMPOSITE_CLIENT", new LayoutEditPolicy(){

			protected EditPolicy createChildEditPolicy(EditPart child) {
				return new NonResizableEditPolicy();
			}

			protected Command getCreateCommand(CreateRequest request) {
				// If there is already a client we can't drop the new object
				if (getBean().eGet(sf_client) == null){
					RuledCommandBuilder bldr = new RuledCommandBuilder(EditDomain.getEditDomain(ExpandableCompositeGraphicalEditPart.this));
					bldr.applyAttributeSetting(getBean(), sf_client, request.getNewObject());
					return bldr.getCommand();
				} else {
					return UnexecutableCommand.INSTANCE;
				}
			}
			
			protected Command getDeleteDependantCommand(Request request) {
				if (RequestConstants.REQ_DELETE_DEPENDANT.equals(request.getType())) {
				  // Get the object who asked to be deleted
				  Object beanToDelete = ((ForwardedRequest) request).getSender().getModel();
                  if (getBean().eGet(sf_client) == beanToDelete){
                	  RuledCommandBuilder bldr = new RuledCommandBuilder(EditDomain.getEditDomain(ExpandableCompositeGraphicalEditPart.this));
                	  bldr.cancelAttributeSetting(getBean(), sf_client, beanToDelete);
                	  return bldr.getCommand();
                  }
				}
				return null;				                
			}

			protected Command getMoveChildrenCommand(Request request) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
	}

}
