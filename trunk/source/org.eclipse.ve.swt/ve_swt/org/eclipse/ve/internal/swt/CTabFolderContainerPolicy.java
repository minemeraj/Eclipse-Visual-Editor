/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabFolderContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-24 18:57:12 $ 
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
 * @since 1.1
 */
public class CTabFolderContainerPolicy extends CompositeContainerPolicy {

	private EReference sf_cTabItems, sf_cTabItemControl, sf_compositeControls;

	protected EClass classControl;

	protected EClass classCTabItem;

	protected EFactory visualsFact;

	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public CTabFolderContainerPolicy(EditDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sf_cTabItems = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABFOLDER_ITEMS);
		sf_cTabItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_CTABITEM_CONTROL);
		sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
		classControl = (EClass) sf_compositeControls.getEType();
		classCTabItem = (EClass) sf_cTabItems.getEType();
		visualsFact = classCTabItem.getEPackage().getEFactoryInstance();
	}

	/*
	 * The child in this case is the Control and not the CTabItem. The isValidChild in this case is called by super classes and they are passed the
	 * Control at that point in time. We will later wrapper it into a CTabItem after it has gone through this test.
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
		return super.getCreateCommand(child, positionBeforeChild).chain(getCreateCTabItemCommand(child, (EObject) positionBeforeChild));
	}

	/*
	 * Create the command to create the parse tree allocation for the CTabItem, the command to set the child as the 'control' property setting of the
	 * CTabItem, and the command to set the CTabItem as a child of the CTabFolder.
	 */
	private Command getCreateCTabItemCommand(final Object child, final EObject positionBeforeChild) {
		Command setCTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				EObject positionBeforeItem = null;
				if (positionBeforeChild != null)
					positionBeforeItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_cTabItems, sf_cTabItemControl,
							positionBeforeChild);
				IJavaObjectInstance cTabItem = createCTabItem();
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting(cTabItem, sf_cTabItemControl, child);
				cb.applyAttributeSetting((EObject) getContainer(), sf_cTabItems, cTabItem, positionBeforeItem);
				command = cb.getCommand();
				command.execute();
			}
		};
		return setCTabItemCommand;
	}

	/**
	 * Delete the dependent. The child is the component, not the CTabItem.
	 */
	public Command getDeleteDependentCommand(Object child) {
		return getDeleteCTabItemCommand(child).chain(super.getDeleteDependentCommand(child));
	}
	/**
	 * 
	 * @param child
	 * @return
	 * 
	 * @since 1.1
	 */
	private Command getDeleteCTabItemCommand(final Object child) {
		Command deleteCTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				EObject cTabItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_cTabItems, sf_cTabItemControl,
						(EObject) child);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.cancelAttributeSetting(cTabItem, sf_cTabItemControl);
				cb.cancelAttributeSetting((EObject) getContainer(), sf_cTabItems, cTabItem);
				command = cb.getCommand();
				command.execute();
			}
		};
		return deleteCTabItemCommand;
	}

	protected Command getOrphanTheChildrenCommand(List children) {
		Command orphanCmd = super.getOrphanTheChildrenCommand(children);
		if (orphanCmd == null || !orphanCmd.canExecute())
			return UnexecutableCommand.INSTANCE;
		return getOrphanCTabItemCommand(children).chain(orphanCmd);
	}

	private Command getOrphanCTabItemCommand(final List children) {
		Command orphanItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// Process throught the list and remove the CTabItem from the parent
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				for (int i = 0; i < children.size(); i++) {
					EObject child = (EObject) children.get(i);
					cb.append(getDeleteCTabItemCommand(child));
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
		return getMoveCTabItemCommand(children, (EObject) positionBeforeChild);
	}

	private Command getMoveCTabItemCommand(final List children, final EObject positionBeforeChild) {
		Command moveCTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// First get the CTabItem of the positionBeforeChild
				EObject positionBeforeItem = null;
				if (positionBeforeChild != null)
					positionBeforeItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_cTabItems, sf_cTabItemControl,
							positionBeforeChild);
				// Process through the list and cancel/apply each CTabItem before the positional CTabItem
				for (int i = 0; i < children.size(); i++) {
					EObject child = (EObject) children.get(i);
					EObject cTabItem = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainer(), sf_cTabItems, sf_cTabItemControl,
							child);
					RuledCommandBuilder cb = new RuledCommandBuilder(domain);
					cb.cancelAttributeSetting((EObject) getContainer(), sf_cTabItems, cTabItem);
					cb.applyAttributeSetting((EObject) getContainer(), sf_cTabItems, cTabItem, positionBeforeItem);
					command = cb.getCommand();
					command.execute();
				}
			}
		};
		return moveCTabItemCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#primAddCommand(java.util.List, java.lang.Object,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		return super.primAddCommand(children, positionBeforeChild, containmentSF)
				.chain(getAddCTabItemCommand(children, (EObject) positionBeforeChild));
	}

	private Command getAddCTabItemCommand(final List children, final EObject positionBeforeChild) {
		Command addCTabItemCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				// Process throught the list and create a CTabItem, apply the child to its 'control' feature
				// apply it before the positional CTabItem
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				for (int i = 0; i < children.size(); i++) {
					EObject child = (EObject) children.get(i);
					cb.append(getCreateCTabItemCommand(child, positionBeforeChild));
				}
				command = cb.getCommand();
				command.execute();
			}
		};
		return addCTabItemCommand;
	}

	/**
	 * Create a new CTabItem and the necessary Parse tree allocation
	 * 
	 * @return
	 * 
	 * @since 1.1
	 */
	private IJavaObjectInstance createCTabItem() {
		IJavaObjectInstance cTabItem = (IJavaObjectInstance) visualsFact.create(classCTabItem);
		PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation();
		ic.setType(cTabItem.getJavaType().getJavaName());

		// set the arguments
		PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference();
		ir.setObject((IJavaObjectInstance) getContainer());
		PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();
		PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT"); //$NON-NLS-1$
		fa.setField("CLOSE"); //$NON-NLS-1$
		fa.setReceiver(name);
		ic.getArguments().add(ir);
		ic.getArguments().add(fa);

		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
		cTabItem.setAllocation(alloc);
		return cTabItem;
	}
}