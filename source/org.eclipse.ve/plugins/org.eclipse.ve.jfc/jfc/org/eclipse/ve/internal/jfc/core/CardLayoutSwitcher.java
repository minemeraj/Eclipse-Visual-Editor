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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: CardLayoutSwitcher.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;


/**
 * @version 	1.0
 * @author
 */
public class CardLayoutSwitcher extends LayoutSwitcher {

	protected CardLayoutPolicyHelper helper;

	public CardLayoutSwitcher(VisualContainerPolicy cp) {
		super(cp);
		helper = new CardLayoutPolicyHelper(cp);
	}
	/*
	 * @see LayoutSwitcher#getChangeConstraintsCommand(List)
	 */
	protected Command getChangeConstraintsCommand(List children) {
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
		// First get rid of any previous constraints 
		cb.append(getCancelConstraintsCommand(children));

		List constraints = helper.getDefaultConstraint(children);
		cb.append(helper.getChangeConstraintCommand(children, constraints));	// Now let the helper add them back correctly.
		return cb.getCommand();
	}

	/*
	 * Get the commands to set the respective constraints of the children to null.
	 */
	public Command getCancelConstraintsCommand(List children) {
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(policy.getEditDomain());
		EReference sfConstraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		EReference sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		EReference sfComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
		
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
		Iterator childs = children.iterator();
		while (childs.hasNext()) {
			EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainerBean(), sfComponents, sfConstraintComponent, (EObject) childs.next());
			cb.applyAttributeSetting(constraintComponent, sfConstraintConstraint, null);
		}
		return cb.getCommand();
	}
}
