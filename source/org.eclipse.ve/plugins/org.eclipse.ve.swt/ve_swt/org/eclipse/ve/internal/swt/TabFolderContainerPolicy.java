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
/*
 *  $RCSfile: TabFolderContainerPolicy.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * 
 * @since 1.0.0
 */
public class TabFolderContainerPolicy extends CompositeContainerPolicy {

	private EReference sf_tabItems, sf_tabItemControl, sf_compositeControls;

	protected EClass classControl;

	protected EClass classTabItem;

	protected EFactory visualsFact;

	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public TabFolderContainerPolicy(EditDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sf_tabItems = JavaInstantiation.getReference(rset, SWTConstants.SF_TABFOLDER_ITEMS);
		sf_tabItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_TABITEM_CONTROL);
		sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
		classControl = (EClass) sf_compositeControls.getEType();
		classTabItem = (EClass) sf_tabItems.getEType();
		visualsFact = classTabItem.getEPackage().getEFactoryInstance();
	}

	/*
	 * The child in this case is the Control and not the TabItem. The isValidChild in this case is called by super classes and they are passed the
	 * Control at that point in time. We will later wrapper it into a TabItem after it has gone through this test.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		return classControl.isInstance(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#getCreateCommand(java.lang.Object, java.lang.Object)
	 */
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		return super.getCreateCommand(child, positionBeforeChild).chain(getCreateTabItemCommand(child, (EObject) positionBeforeChild));
	}

	/*
	 * Create the command to create the parse tree allocation for the TabItem, the command to set the child as the 'control' property setting of the
	 * TabItem, and the command to set the TabItem as a child of the TabFolder.
	 */
	private Command getCreateTabItemCommand(final Object child, final EObject positionBeforeChild) {
		Command setTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				EObject positionBeforeItem = null;
				if (positionBeforeChild != null)
					positionBeforeItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_tabItems, sf_tabItemControl,
							positionBeforeChild);
				IJavaObjectInstance tabItem = createTabItem();
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting(tabItem, sf_tabItemControl, child);
				cb.applyAttributeSetting((EObject) getContainer(), sf_tabItems, tabItem, positionBeforeItem);
				command = cb.getCommand();
				command.execute();
			}
		};
		return setTabItemCommand;
	}

	/**
	 * Delete the dependent. The child is the component, not the JTabComponent.
	 */
	public Command getDeleteDependentCommand(Object child) {
		return getDeleteTabItemCommand(child).chain(super.getDeleteDependentCommand(child));
	}
	/**
	 * 
	 * @param child
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private Command getDeleteTabItemCommand(final Object child) {
		Command deleteTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				EObject tabItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_tabItems, sf_tabItemControl,
						(EObject) child);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.cancelAttributeSetting(tabItem, sf_tabItemControl);
				cb.cancelAttributeSetting((EObject) getContainer(), sf_tabItems, tabItem);
				command = cb.getCommand();
				command.execute();
			}
		};
		return deleteTabItemCommand;
	}

	protected Command getOrphanTheChildrenCommand(List children) {
		Command orphanCmd = super.getOrphanTheChildrenCommand(children);
		if (orphanCmd == null || !orphanCmd.canExecute())
			return UnexecutableCommand.INSTANCE;
		return getOrphanTabItemCommand(children).chain(orphanCmd);
	}

	private Command getOrphanTabItemCommand(final List children) {
		Command orphanItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// Process throught the list and remove the TabItem from the parent
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				for (int i = 0; i < children.size(); i++) {
					EObject child = (EObject) children.get(i);
					cb.append(getDeleteTabItemCommand(child));
				}
				command = cb.getCommand();
				command.execute();
			}
		};
		return orphanItemCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getMoveChildrenCommand(java.util.List, java.lang.Object,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command getMoveChildrenCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		return getMoveTabItemCommand(children, (EObject) positionBeforeChild);
	}

	private Command getMoveTabItemCommand(final List children, final EObject positionBeforeChild) {
		Command moveTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// First get the TabItem of the positionBeforeChild
				EObject positionBeforeItem = null;
				if (positionBeforeChild != null)
					positionBeforeItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_tabItems, sf_tabItemControl,
							positionBeforeChild);
				// Process throught the list and cancel/apply each TabItem before the positional TabItem
				for (int i = 0; i < children.size(); i++) {
					EObject child = (EObject) children.get(i);
					EObject tabItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_tabItems, sf_tabItemControl,
							child);
					RuledCommandBuilder cb = new RuledCommandBuilder(domain);
					cb.cancelAttributeSetting((EObject) getContainer(), sf_tabItems, tabItem);
					cb.applyAttributeSetting((EObject) getContainer(), sf_tabItems, tabItem, positionBeforeItem);
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
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#primAddCommand(java.util.List, java.lang.Object,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		return super.primAddCommand(children, positionBeforeChild, containmentSF)
				.chain(getAddTabItemCommand(children, (EObject) positionBeforeChild));
	}

	private Command getAddTabItemCommand(final List children, final EObject positionBeforeChild) {
		Command addTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// Process throught the list and create a TabItem, apply the child to its 'control' feature
				// apply it before the positional TabItem
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				for (int i = 0; i < children.size(); i++) {
					EObject child = (EObject) children.get(i);
					cb.append(getCreateTabItemCommand(child, positionBeforeChild));
				}
				command = cb.getCommand();
				command.execute();
			}
		};
		return addTabItemCommand;
	}

	/**
	 * Create a new TabItem and the necessary Parse tree allocation
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private IJavaObjectInstance createTabItem() {
		IJavaObjectInstance tabItem = (IJavaObjectInstance) visualsFact.create(classTabItem);
		PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation();
		ic.setType(tabItem.getJavaType().getJavaName());

		// set the arguments
		PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference();
		ir.setObject((IJavaObjectInstance) getContainer());
		PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();
		PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT"); //$NON-NLS-1$
		fa.setField("NONE"); //$NON-NLS-1$
		fa.setReceiver(name);
		ic.getArguments().add(ir);
		ic.getArguments().add(fa);

		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
		tabItem.setAllocation(alloc);
		return tabItem;
	}
}
