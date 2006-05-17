/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.java.core;


import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.StopRequestException;
import org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * @author richkulp
 *
 * This is an base class for container policies within Java VE. What it does special is
 * to handle using the appropriate rules for creating/deleting children.
 */
public class BaseJavaContainerPolicy extends AbstractEMFContainerPolicy {

	/**
	 * Construct with feature and domain. Used when this should be a single feature container.
	 * @param feature
	 * @param domain
	 * 
	 * @since 1.1.0.1
	 */
	public BaseJavaContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(feature, domain);
	}

	/**
	 * Constructor for BaseJavaContainerPolicy.
	 * @param domain
	 */
	public BaseJavaContainerPolicy(EditDomain domain) {
		super(domain);
	}
	
	/**
	 * Called to process for an allocation of the form <code>{factory:factory-class}.methodInvocation(arguments,...)</code>. In this case it
	 * will find the beaninfo for the factory-class, and if it is a factory, it will try to change to the proper factory. Using the criteria:
	 * <ol>
	 * <li>See if parent is also a factory invocation of the same type. If it is, use the parent's factory.
	 * <li>Find valid factories of the given type. A valid factoryis one that is in the same member container as the parent, or any global ones.
	 * <li>If only one factory is valid, then use that one. If more than one, then at drop time ask the client which one to use.
	 * <li>If none, then create one.
	 * </ol>
	 * <p><b>Note:</b>This is experimental API. It will change.
	 * @param child
	 * @param parent
	 * @param reqType
	 * @param preCmds
	 * @param ed
	 * @throws StopRequestException
	 * 
	 * @since 1.2.0
	 */
	public static void processForFactory(Object child, Object parent, int reqType, CommandBuilder preCmds, EditDomain ed) throws StopRequestException {
		if (reqType == CREATE_REQ || reqType == ADD_REQ) {
			if (child instanceof IJavaInstance && parent instanceof IJavaInstance) {
				IJavaInstance jChild = (IJavaInstance) child;
				JavaAllocation allocation = jChild.getAllocation();
				if (allocation instanceof ParseTreeAllocation) {
					ParseTreeAllocation pta = (ParseTreeAllocation) allocation;
					PTExpression pte = pta.getExpression();
					if (pte instanceof PTMethodInvocation) {
						PTMethodInvocation ptmi = (PTMethodInvocation) pte;
						if (ptmi.getReceiver() instanceof PTName) {
							PTName recv = (PTName) ptmi.getReceiver();
							if (recv.getName().startsWith(EnsureFactoryCommand.FACTORY_PREFIX_FLAG)) {
								preCmds.append(new EnsureFactoryCommand(jChild, (IJavaInstance) parent, ed));
							}
						}
					}
				}
			}
		}
	}
	
	public static void processforRequiredImplicits(Object child, int reqType, CommandBuilder preCmds, EditDomain ed) {
		if (child instanceof IJavaInstance) {
			IJavaInstance javaInstance = (IJavaInstance) child;
			if (BeanUtilities.getSetBeanDecoratorFeatureAttributeValue(javaInstance.getJavaType(), IBaseBeanInfoConstants.REQUIRED_IMPLICIT_PROPERTIES) != null)
				preCmds.append(new EnsureRequiredImplicitCommand(javaInstance, ed));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getTrueChild(java.lang.Object, int, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder)
	 */
	public Object getTrueChild(Object child, int reqType, CommandBuilder preCmds, CommandBuilder postCmds) throws StopRequestException {
		Object trueChild = super.getTrueChild(child, reqType, preCmds, postCmds);
		processForFactory(trueChild, getContainer(), reqType, preCmds, getEditDomain());
		processforRequiredImplicits(trueChild, reqType, preCmds, getEditDomain());
		return trueChild;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getAddCommand(java.util.List, java.lang.Object, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command getAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		// Test done here and not in isValidChild because bean location only makes sense for add, not create.
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!isValidBeanLocation(child))
				return UnexecutableCommand.INSTANCE;
		}
		return super.getAddCommand(children, positionBeforeChild, containmentSF);
	}
	
	/**
	 * Is the child in valid bean location for the add. For example, if the child is located at LOCAL, can the
	 * child be validly added to this container. The default is true, but subclasses can override to provide
	 * their own tests.
	 * @param child
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	protected boolean isValidBeanLocation (Object child) {
		return true;
	}
	
	/**
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getDeleteDependentCommand(Object, EStructuralFeature)
	 */
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		RuledCommandBuilder cBld = new RuledCommandBuilder(domain);
		cBld.setPropertyRule(false);	// Doing child right now.
		cBld.cancelAttributeSetting((EObject) container, containmentSF, child);
		return cBld.getCommand(); // No annotations if not composite because the child is owned by someone else and is not going away.
	}

	/**
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#primCreateCommand(Object, Object, EStructuralFeature)
	 */
	protected Command primCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && ((EObject) container).eIsSet(containmentSF))
			return UnexecutableCommand.INSTANCE; // This is a single valued feature, and it is already set.

		RuledCommandBuilder cBld = new RuledCommandBuilder(domain); 
		cBld.setPropertyRule(false);
		if (!containmentSF.isMany())
			cBld.applyAttributeSetting((EObject) container, containmentSF, child);
		else
			cBld.applyAttributeSetting((EObject) container, containmentSF, child, positionBeforeChild);
			
		return cBld.getCommand();
	}
	
	protected Command primCreateCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		CommandBuilder cb = new CommandBuilder();
		for (Iterator iter = children.iterator(); !cb.isDead() && iter.hasNext();) {
			Object child = iter.next();
			cb.append(primCreateCommand(child, positionBeforeChild, containmentSF));
		}
		return cb.getCommand();
	}
	
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		// Change to use RuledCommandBuilder to add children.
		if (!containmentSF.isMany() && (((EObject) container).eIsSet(containmentSF) || children.size() > 1)) {
			return UnexecutableCommand.INSTANCE;	// This is a single valued feature, and it is already set or there is more than one child being added.
		} else {
			RuledCommandBuilder cbldr = new RuledCommandBuilder(domain);
			cbldr.setPropertyRule(false);	// These are children.
			cbldr.applyAttributeSettings((EObject) container, containmentSF, children, positionBeforeChild);
			return cbldr.getCommand();
		}
	}

}
