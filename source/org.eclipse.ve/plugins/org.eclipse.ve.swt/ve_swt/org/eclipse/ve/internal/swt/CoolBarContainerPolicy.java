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
package org.eclipse.ve.internal.swt;

/*
 *  $RCSfile: CoolBarContainerPolicy.java,v $
 *  $Revision: 1.12 $  $Date: 2005-10-03 19:20:48 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Container Edit Policy for CoolBar/CoolItems.
 */
public class CoolBarContainerPolicy extends CompositeContainerPolicy {

	private EReference sf_coolbarItems, sf_coolItemControl, sf_compositeControls;

	protected EClass classControl;

	protected EClass classCoolItem;

	protected EFactory visualsFact;

	protected JavaClass classComponent;

	public CoolBarContainerPolicy(EditDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sf_coolbarItems = JavaInstantiation.getReference(rset, SWTConstants.SF_COOLBAR_ITEMS);
		sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
		sf_coolItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_COOLITEM_CONTROL);
		classControl = (EClass) sf_compositeControls.getEType();
		classCoolItem = (EClass) sf_coolbarItems.getEType();
		visualsFact = classCoolItem.getEPackage().getEFactoryInstance();

		// Override the containment feature from CompositeContainer
		//containmentSF = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), SWTConstants.SF_COOLBAR_ITEMS);
	}

	/*
	 * The child in this case is the Control and not the CoolItem. The isValidChild in this case is called by super classes and they are passed the
	 * Control at that point in time. We will later wrapper it into a TabItem after it has gone through this test.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		return classControl.isInstance(child);
	}


	protected Command getCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		return super.getCreateCommand(child, positionBeforeChild, containmentSF).chain(getCreateCoolItemCommand(child, (EObject) positionBeforeChild));
	}

	/*
	 * Create the command to create the parse tree allocation for the CoolItem, the command to set the child as the 'control' property setting of the
	 * CoolItem, and the command to set the CoolItem as a child of the CoolBar.
	 */
	private Command getCreateCoolItemCommand(final Object child, final EObject positionBeforeChild) {
		Command setCoolItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// First get the CoolItem of the positionBeforeChild
				EObject positionBeforeItem = null;
				if (positionBeforeChild != null)
					positionBeforeItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_coolbarItems,
							sf_coolItemControl, positionBeforeChild);
				IJavaObjectInstance coolItem = (IJavaObjectInstance) visualsFact.create(classCoolItem);
				PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation();
				ic.setType(coolItem.getJavaType().getJavaName());

				// set the arguments
				PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference();
				ir.setReference((IJavaObjectInstance) getContainer());
				PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();
				PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT"); //$NON-NLS-1$
				fa.setField("NONE"); //$NON-NLS-1$
				fa.setReceiver(name);
				ic.getArguments().add(ir);
				ic.getArguments().add(fa);

				JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
				coolItem.setAllocation(alloc);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting(coolItem, sf_coolItemControl, child);
				cb.applyAttributeSetting((EObject) getContainer(), sf_coolbarItems, coolItem, positionBeforeItem);
				command = cb.getCommand();
				command.execute();
			}
		};
		return setCoolItemCommand;
	}

	private Command getMoveCoolItemCommand(final List children, final EObject positionBeforeChild) {
		Command moveTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// First get the CoolItem of the positionBeforeChild
				EObject positionBeforeItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_coolbarItems,
						sf_coolItemControl, positionBeforeChild);
				// Process throught the list and cancel/apply each CoolItem before the positional CoolItem
				for (int i = 0; i < children.size(); i++) {
					EObject child = (EObject) children.get(i);
					EObject coolItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_coolbarItems,
							sf_coolItemControl, child);
					RuledCommandBuilder cb = new RuledCommandBuilder(domain);
					cb.cancelAttributeSetting((EObject) getContainer(), sf_coolbarItems, coolItem);
					cb.applyAttributeSetting((EObject) getContainer(), sf_coolbarItems, coolItem, positionBeforeItem);
					command = cb.getCommand();
					command.execute();
				}
			}
		};
		return moveTabItemCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getMoveChildrenCommand(java.util.List, java.lang.Object,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command getMoveChildrenCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		return super.getMoveChildrenCommand(children, positionBeforeChild, containmentSF).chain(
				getMoveCoolItemCommand(children, (EObject) positionBeforeChild));
	}

	private Command getDeleteCoolItemCommand(final Object child) {
		Command deleteCoolItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				EObject coolItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_coolbarItems, sf_coolItemControl,
						(EObject) child);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.cancelAttributeSetting(coolItem, sf_coolItemControl);
				cb.cancelAttributeSetting((EObject) getContainer(), sf_coolbarItems, coolItem);
				command = cb.getCommand();
				command.execute();
			}
		};
		return deleteCoolItemCommand;
	}

	/**
	 * Delete the dependent. The child is the component, not the JTabComponent.
	 */
	public Command getDeleteDependentCommand(Object child) {
		return getDeleteCoolItemCommand(child).chain(super.getDeleteDependentCommand(child));
	}


	protected Command getOrphanTheChildrenCommand(List children) {
		Command orphanCmd = super.getOrphanTheChildrenCommand(children);
		if (orphanCmd == null || !orphanCmd.canExecute())
			return UnexecutableCommand.INSTANCE;
		return getOrphanCoolItemCommand(children).chain(orphanCmd);
	}

	private Command getOrphanCoolItemCommand(final List children) {
		Command orphanCoolItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// Remove the TabItems from the TabFolder. The controls of the TabItems will
				// still be left to be handles by the new parent.
				List items = new ArrayList(children.size());
				Iterator itr = children.iterator();
				while (itr.hasNext()) {
					EObject item = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_coolbarItems, sf_coolItemControl,
							(EObject) itr.next());
					items.add(item);
				}
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.cancelAttributeSettings((EObject) getContainer(), sf_coolbarItems, items); // Delete the TabItems under rule control so that they
																							  // will go away.
				command = cb.getCommand();
				command.execute();
			}
		};
		return orphanCoolItemCommand;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#primAddCommand(java.util.List, java.lang.Object, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		return super.primAddCommand(children, positionBeforeChild, containmentSF).chain(getCreateCoolItemCommand(children.get(0),(EObject)positionBeforeChild));
	}
}
