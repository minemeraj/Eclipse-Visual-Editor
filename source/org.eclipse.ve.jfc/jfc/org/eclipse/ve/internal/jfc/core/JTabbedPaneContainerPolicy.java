package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: JTabbedPaneContainerPolicy.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-18 21:54:36 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * Container Edit Policy for Swing JTabbedPanes.
 */
public class JTabbedPaneContainerPolicy extends BaseJavaContainerPolicy {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.JavaContainerPolicy#isValidBeanLocation(java.lang.Object)
	 */
	protected boolean isValidBeanLocation(Object child) {
		return child instanceof EObject && BeanUtilities.isValidBeanLocation(domain, (EObject)child);
	}
	protected EClass classJComponent;
	protected EClass classJTabComponent;
	protected EReference sfTabTitle;
	protected EReference sfTabIcon;
	protected EReference sfTabTooltip;
	protected EReference sfComponent;
	protected EFactory visualsFact;

	public JTabbedPaneContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), JFCConstants.SF_JTABBEDPANE_TABS), domain);

		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_COMPONENT);
		sfTabTitle = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_TITLE);
		sfTabIcon = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_ICON);
		sfTabTooltip = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_TOOLTIP);
		classJTabComponent = (EClass) rset.getEObject(JFCConstants.CLASS_JTABBEDPANE_JTABCOMPONENT, true);
		classJComponent = (EClass) sfComponent.getEType();
		visualsFact = JFCConstants.getFactory(classJTabComponent);
	}

	/*
	 * The child in this case is the JComponent and not the JTabComponent. The isValidChild
	 * in this case is called by super classes and they are passed the JComponent at that
	 * point in time. We will later wrapper it into a JTabComponent after it has gone through
	 * this test.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		return classJComponent.isInstance(child);
	}

	/**
	 * Delete the dependent. The child is the component, not the JTabComponent.
	 */
	public Command getDeleteDependentCommand(Object child) {
		return super.getDeleteDependentCommand(InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfComponent, (EObject) child));
	}
	/**
	 * Get the move children command for the list. The children
	 * are the components, not the JTabComponents.
	 */	
	public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
		// We need to convert to the JTabComponents, not the components.
		List jtabComponents = new ArrayList(children.size());
		Iterator itr = children.iterator();
		while(itr.hasNext()) {
			jtabComponents.add(InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfComponent, (EObject) itr.next()));
		}
		return super.getMoveChildrenCommand(jtabComponents, positionBeforeChild != null ? InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfComponent, (EObject) positionBeforeChild): null);
	}
	/**
	 * Get the orphan command for the list. The children
	 * are the components, not the JTabComponents.
	 */
	protected Command getOrphanTheChildrenCommand(List children) {
		// We need to unset the components from the JTabComponent after
		// orphaning the JTabComponents so that they are free of any
		// containment when they are added to their new parent. If we
		// didn't unset the components, then upon undo the component
		// would not be in the JTabCompoent when it is added back in since that
		// would be lost and not in a command stack.
		//
		// It is required that when orphaning, the JTabComponents themselves
		// are not reused on the subsequent add. They will be thrown away. 

		List jtabComponents = new ArrayList(children.size());
		Iterator itr = children.iterator();
		while(itr.hasNext()) {
			EObject jtabcomponent = InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfComponent, (EObject) itr.next());
			jtabComponents.add(jtabcomponent);
		}		
		
		// The order of below will result in:
		//   1) Remove all of the jtabcomponents from the jtabbedpane.
		//   2) Remove all of the components from the jtabcomponents.
		//   3) Post set will handle the tab settings that were removed (but since by this time the components have been remove
		//      from the jtabcomponents, they won't be processed.
		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		cb.cancelAttributeSettings((EObject) container, containmentSF, jtabComponents); // Delete the jtabcompoents under rule control so that they will go away.
		cb.setApplyRules(false);
		cb.cancelGroupAttributeSetting(jtabComponents, sfComponent);	// Cancel out all of the component settings not under rule control since we are keeping them.
		return cb.getCommand();
	}
		
	/**
	 * Add children to the JTabbedPane. Each component will part of a JTabComponent.
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		ArrayList jtabComponents = new ArrayList(children.size());
		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		cb.setApplyRules(false);	// While building tree of new jtabcomponents, don't apply rules.
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			EObject jtabComponent = createJTabComponent();
			// Add the child to the component.
			cb.applyAttributeSetting(jtabComponent, sfComponent, child);
			jtabComponents.add(jtabComponent);
		}
		
		cb.setApplyRules(true);	// Apply the rules when adding the tab components.
		cb.applyAttributeSettings((EObject) container, containmentSF, jtabComponents, positionBeforeChild != null ? InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfComponent, (EObject) positionBeforeChild) : null);
		return cb.getCommand();
	}
		
	/**
	 * Create a jtabcomponent with the title/icon/tooltip set to null.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private EObject createJTabComponent() {
		EObject jtabComponent = visualsFact.create(classJTabComponent);
		// Explicitly set the tabxxx to null. Needed so we know we are not using the "default" kind of
		// tab component. We don't generate those, we only add those.
		jtabComponent.eSet(sfTabTitle, null);
		jtabComponent.eSet(sfTabIcon, null);
		jtabComponent.eSet(sfTabTooltip, null);
		return jtabComponent;
	}

	/**
	 * Create a new child which in this case is part of a JTabComponent.
	 */
	protected Command primCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		EObject jtabComponent = createJTabComponent();

		// Add the child to the component.
		CommandBuilder cb = new CommandBuilder();
		cb.applyAttributeSetting(jtabComponent, sfComponent, child);	
		cb.append(super.primCreateCommand(jtabComponent, positionBeforeChild != null ? InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfComponent, (EObject) positionBeforeChild) : null, containmentSF));
		return cb.getCommand();
	}
}