/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: JSplitPaneContainerPolicy.java,v $
 *  $Revision: 1.9 $  $Date: 2005-06-24 18:57:15 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

/**
 * Container policy for JSplitPane
 *
 * General information about JSplitPane:
 * JSplitPane's children are really set as the left or top component and the right or bottom component. 
 * If the JSplitPane's orientation is HORIZONTAL_SPLIT, the components will be set left and right.
 * If the JSplitPane's orientation is VERTICAL_SPLIT, the components will be set top and bottom.
 * We'll check for both since we don't know the orientation... and even if we did, Sun lets you set
 * the components regardless of what the orientation is set to. In other words, you can set the left
 * and right components even though the orientation is vertical... and the opposite is true as well. 
 */
public class JSplitPaneContainerPolicy extends AbstractJavaContainerPolicy {
	public static final int VERTICAL_SPLIT = 0;
	public static final int HORIZONTAL_SPLIT = 1;

	protected JavaClass classComponent;
	protected EReference sfLeftComponent,
		sfRightComponent,
		sfTopComponent,
		sfBottomComponent,
		sf_constraintComponent,
		sf_constraintConstraint,
		sf_containerComponents;
		
	protected static final int LEFT_COMP =0, RIGHT_COMP=1, TOP_COMP=2, BOTTOM_COMP=3;

	public JSplitPaneContainerPolicy(EditDomain domain) {
		super(domain);

		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfLeftComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT);
		sfRightComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT);
		sfBottomComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		sfTopComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT);
			
		sf_constraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sf_constraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		sf_containerComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
		classComponent = Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
	}
	/**
	 * Determine which components have been set in the Splitpane
	 */
	protected EObject[] determineSplitpaneOccupants() {
		EObject[] result = new EObject[] {
			(EObject) ((EObject) container).eGet(sfLeftComponent),
			(EObject) ((EObject) container).eGet(sfRightComponent),
			(EObject) ((EObject) container).eGet(sfTopComponent),			
			(EObject) ((EObject) container).eGet(sfBottomComponent)};

		/*
		 * Since Sun allows JSplitPane to contain children that have been added with constraints (i.e. add(aComponent, "left")),
		 * we must look for any constraint components and include them as part of the possibly settings.
		 */
		List constraintChildren = (List) ((EObject) container).eGet(sf_containerComponents);
		Iterator itr = constraintChildren.iterator();
		while (itr.hasNext()) {
			EObject constraintComponent = (EObject) itr.next();
			EObject component = (EObject) constraintComponent.eGet(sf_constraintComponent);
			// See whether the component is in severe error.  If so then don't include it here
			if (BeanProxyUtilities.getBeanProxyHost((IJavaInstance)component).isBeanProxyInstantiated()) {
				IJavaObjectInstance constraintString = (IJavaObjectInstance) constraintComponent.eGet(sf_constraintConstraint);
				// We know the constraints value should be a bean so we can use its toString to get the string value
				String constraint = BeanProxyUtilities.getBeanProxy(constraintString).toBeanString();
				if (constraint != null) {
					if (constraint.equals("left"))  //$NON-NLS-1$
						result[LEFT_COMP] = component;
					else if (constraint.equals("top"))  //$NON-NLS-1$
						result[TOP_COMP] = component;
					else if (constraint.equals("right"))  //$NON-NLS-1$
						result[RIGHT_COMP] = component;
					else if (constraint.equals("bottom"))  //$NON-NLS-1$
						result[BOTTOM_COMP] = component;
				}
			}
		}
		
		return result;
	}
	
	public Command getAddCommand(List children, Object positionBeforeChild) {
		// The code logic can only handle setting one component at a time.
		if (children == null || children.isEmpty() || children.size() > 1)
			return UnexecutableCommand.INSTANCE;
		Object child = children.get(0);
		// Determine the structural feature to use for this child based on what is already set in the JSplitPane 
		// and the positionBeforeChild component (it could be null). Also add commands to a command builder
		// for any repositioning of the current component. 
		// If null is returned, no SF was return, just return the CommandBuilder
		CommandBuilder cBldr = new CommandBuilder();
		EStructuralFeature containmentSF = getStructuralFeature(child, positionBeforeChild, cBldr);
		if (containmentSF != null)
			cBldr.append(primAddCommand(children, positionBeforeChild, containmentSF));
		return cBldr.getCommand();
	}
	/**
	 * Determine whether the component should be added as the left or right component
	 */
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		// Determine the structural feature to use for this child based on what is already set in the JSplitPane 
		// and the positionBeforeChild component (it could be null). Also add commands to a command builder
		// for any repositioning of the current component. 
		// If null is returned, no SF was return, just return the CommandBuilder
		CommandBuilder cBldr = new CommandBuilder(); //$NON-NLS-1$
		EStructuralFeature containmentSF = getStructuralFeature(child, positionBeforeChild, cBldr);
		if (containmentSF != null)
			cBldr.append(primCreateCommand(child, positionBeforeChild, containmentSF));
		return cBldr.getCommand();
	}
	
	/**
	 * Return a structural feature to use to add or create a child and based on the positionBeforeChild, 
	 * and create any other commands that are needed to reposition the current left or right component.
	 *
	 * Determine the structural feature to use for this child based on what is already set in the JSplitPane 
	 * and the positionBeforeChild component (it could be null). Also add commands to a command builder
	 * for any repositioning of the current component. For example, if the right component is set and the positionBeforeChild
	 * is null, this would mean the new child component wants to be at the end (set in the right component position).
	 * Therefore we have to issue a cancel and set to left component for the current child , and return the SF for
	 * setting the new child to the right component which is done by the caller.
	 */
	protected EStructuralFeature getStructuralFeature(Object child, Object positionBeforeChild, CommandBuilder cmdbldr) {
		if (!isValidChild(child, null) || !isParentAcceptable(child)) {
			cmdbldr.append(UnexecutableCommand.INSTANCE);
			return null;
		}
		EObject[] components = determineSplitpaneOccupants();

		// If both components are occupied, we can't add any more.
		if ((components[LEFT_COMP] != null || components[TOP_COMP] != null) && (components[RIGHT_COMP] != null || components[BOTTOM_COMP] != null)) {
			cmdbldr.append(UnexecutableCommand.INSTANCE);
			return null;
		}
		// Get the orientation from the live JSplitPane and set the SF's based on this setting.
		IBeanProxyHost proxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) container);
		IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.invoke(proxyHost.getBeanProxy(), "getOrientation"); //$NON-NLS-1$
		int orientation = intProxy.intValue();
		EStructuralFeature leftTopSF, rightBottomSF;
		if (orientation == HORIZONTAL_SPLIT) {
			leftTopSF = sfLeftComponent;
			rightBottomSF = sfRightComponent;
		} else {
			leftTopSF = sfTopComponent;
			rightBottomSF = sfBottomComponent;
		}
		// If nothing is occupied, set the left/top component.
		if (components[LEFT_COMP] == null && components[TOP_COMP] == null && components[RIGHT_COMP] == null && components[BOTTOM_COMP] == null) {
			return leftTopSF;
		}
		// Handle the cases where the positionBeforeChild is null
		EStructuralFeature containmentSF;
		if (positionBeforeChild == null) {
			// If the left/top is already occupied and the positionBeforeChild is null, set the right/bottom component.
			if (components[LEFT_COMP] != null || components[TOP_COMP] != null) {
				return rightBottomSF;
			}
			// If the right/bottom is occupied, this R/B component needs to be moved to the left/top
			// and this new child needs to be set as the R/B. This requires a cancel & add for the existing 
			// R/B component, and an add for the new child.
			if (components[LEFT_COMP] == null && components[TOP_COMP] == null && (components[RIGHT_COMP] != null || components[BOTTOM_COMP] != null)) {
				EObject component = null;
				if (components[RIGHT_COMP] != null) {
					containmentSF = sfRightComponent;
					component = components[RIGHT_COMP];
				} else {
					containmentSF = sfBottomComponent;
					component = components[BOTTOM_COMP];
				}
				cancelComponent(cmdbldr, containmentSF, component);	// Cancel from current location so that it can be moved.
				cmdbldr.applyAttributeSetting((EObject) container, leftTopSF, component);	// Apply it to the left
				return rightBottomSF;
			}
		} else {
			// Handle the cases where positionBeforeChild is not null

			// If the right/bottom is occupied, just set the left component
			if (components[LEFT_COMP] == null && components[TOP_COMP] == null && (components[RIGHT_COMP] != null || components[BOTTOM_COMP] != null)) {
				return leftTopSF;
			}
			// If the left/top is occupied, this L/T component needs to be moved to the bottom/right
			// and this new child needs to be set as the L/T. This requires a cancel & add for the existing 
			// L/T component, and an add for the new child.
			if (components[RIGHT_COMP] == null && components[BOTTOM_COMP] == null && (components[LEFT_COMP] != null || components[TOP_COMP] != null)) {
				EObject component = null;
				if (components[LEFT_COMP] != null) {
					containmentSF = sfLeftComponent;
					component = components[LEFT_COMP];
				} else {
					containmentSF = sfTopComponent;
					component = components[TOP_COMP];
				}
				cancelComponent(cmdbldr, containmentSF, component);	// Cancel the component out of the current spot so that it can be moved.
				cmdbldr.applyAttributeSetting((EObject) container, rightBottomSF, component);	// Apply it to the right
				return leftTopSF;
			}
		}
		return null;
	}
	
	/*
	 * Cancels a component out of the current spot so that it can be moved to a new location on the split pane.
	 * If this component is a constraintComponent component, then handle getting rid of the constraintComponent too.
	 */
	protected void cancelComponent(CommandBuilder cmdbldr, EStructuralFeature containmentSF, EObject component) {
		// Need to handle the case where the Component has a constraint which means it is 
		// contained in a ConstraintComponent. Need to remove the constraint. Else we need
		// to move the component to the new position. Since this is just a move, no Rules are
		// required for the move part.
		EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject) container, sf_containerComponents, sf_constraintComponent, component);
		if (constraintComponent != null) {
			// Need to remove constraintComponent (under rule control) then remove component from constraint component. Finally
			// postset will clean up constraintComponent (which at that time will no longer have the component in it).
			RuledCommandBuilder rcb = new RuledCommandBuilder(domain);
			rcb.cancelAttributeSetting((EObject) container, sf_containerComponents, constraintComponent);
			rcb.setApplyRules(false);
			rcb.cancelAttributeSetting(constraintComponent, sf_constraintComponent);
			cmdbldr.append(rcb.getCommand());
		} else {
			// Just remove out of the container, no rules because scoping isn't changing.
			cmdbldr.cancelAttributeSetting((EObject) container, containmentSF, component);
		}
	}
	/**
	 * Delete the dependent. The child could be the component or the constraintComponent.
	 */
	public Command getDeleteDependentCommand(Object child) {
		// Need to handle the possibility that the child was added with a constraint (i.e. add(thisComponent, "left"))
		EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject) container, sf_containerComponents, sf_constraintComponent, (EObject) child);
		if (constraintComponent != null)
			return super.getDeleteDependentCommand(constraintComponent, sf_containerComponents);
			
		EReference delReference = containmentSF((EObject) child);
		if (delReference != null)	
			return super.getDeleteDependentCommand(child, delReference);
			
		return UnexecutableCommand.INSTANCE;
	}

	/*
	 * Return the feature (left, right, top, bottom) that this child is contained by.
	 */	
	protected EReference containmentSF(EObject child) {
		EReference[] referencedBy = InverseMaintenanceAdapter.getReferencesFrom((EObject) container, child);
		if (referencedBy != null) {
			List rby = Arrays.asList(referencedBy);
			if (rby.contains(sfLeftComponent))
				return sfLeftComponent;
			else if (rby.contains(sfRightComponent))
				return sfRightComponent;
			else if (rby.contains(sfTopComponent))
				return sfTopComponent;
			else if (rby.contains(sfBottomComponent))
				return sfBottomComponent;
		}
		return null;
	}
		
	/**
	 * Get the move children command for the list. The children
	 * are the components, not the constraintComponents.
	 */
	public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
		if (children.size() > 1)
			return UnexecutableCommand.INSTANCE;
		Object child = children.get(0);
		// If we trying to move a component before itself do nothing.
		if (positionBeforeChild != null && positionBeforeChild == child)
			return UnexecutableCommand.INSTANCE;
			
		EObject[] components = determineSplitpaneOccupants();
		
		// Get the orientation from the live JSplitPane and set the SF's based on this setting.
		IBeanProxyHost proxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) container);
		IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.invoke(proxyHost.getBeanProxy(), "getOrientation");	//$NON-NLS-1$
		int orientation = intProxy.intValue();
		EStructuralFeature leftTopSF, rightBottomSF;
		if (orientation == HORIZONTAL_SPLIT) {
			leftTopSF = sfLeftComponent;
			rightBottomSF = sfRightComponent;
		} else {
			leftTopSF = sfTopComponent;
			rightBottomSF = sfBottomComponent;
		}
		// If we trying to move the right/bottom component to the last position (which is already the rightbottom component), 
		// do nothing.
		if (positionBeforeChild == null && (components[RIGHT_COMP] == child || components[BOTTOM_COMP] == child))
			return UnexecutableCommand.INSTANCE;
		// If we trying to move the right/bottom component to the first position (which is the left/top component), 
		// or move the left/top component to the last position ((which is the rightbottom component), switch the components.
		if ((components[LEFT_COMP] != null || components[TOP_COMP] != null) && (components[RIGHT_COMP] != null || components[BOTTOM_COMP] != null)) {
			CommandBuilder cmdbldr = new CommandBuilder("Move children"); //$NON-NLS-1$
			EObject leftTopComp, rightBottomComp = null;
			EStructuralFeature containmentSF;
			
			// First unset the structural feature for both components
			if (components[LEFT_COMP] != null) {
				containmentSF = sfLeftComponent;
				leftTopComp = components[LEFT_COMP];
			} else {
				containmentSF = sfTopComponent;
				leftTopComp = components[TOP_COMP];
			}
			cancelComponent(cmdbldr, containmentSF, leftTopComp);
				
			if (components[RIGHT_COMP] != null) {
				containmentSF = sfRightComponent;
				rightBottomComp = components[RIGHT_COMP];
			} else {
				containmentSF = sfBottomComponent;
				rightBottomComp = components[BOTTOM_COMP];
			}
			cancelComponent(cmdbldr, containmentSF, rightBottomComp);
			
			// Then set the components in the reverse positions
			cmdbldr.applyAttributeSetting((EObject) container, leftTopSF, rightBottomComp);
			cmdbldr.applyAttributeSetting((EObject) container, rightBottomSF, leftTopComp);
			return cmdbldr.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}
	/**
	 * Get the orphan command for the list.
	 * The children could be a mix which may have constraints (i.e. add(aComponent, "left")
	 * or may have been added using JSplitPane setters (i.e. setLeftComponent(Component)).
	 */
	protected Command getOrphanTheChildrenCommand(List children) {
		// We need to unset the components from the constraints after
		// orphaning the constraints so that they are free of any
		// containment when they are added to their new parent. If we
		// didn't unset the components, then upon undo the component
		// would not be in the constraint when it is added back in.
		// This is only for those that are in constraint components.
		// Otherwise we just orphan the children
		//
		RuledCommandBuilder compBldr = new RuledCommandBuilder(domain);
		compBldr.setApplyRules(false);
		List constraints = new ArrayList(children.size());
		Iterator itr = children.iterator();
		while(itr.hasNext()) {
			EObject component = (EObject) itr.next();
			EObject constraint = InverseMaintenanceAdapter.getIntermediateReference((EObject) container, sf_containerComponents, sf_constraintComponent, component);
			if (constraint != null)
				constraints.add(constraint);
			else {
				EReference containmentSF = containmentSF(component);
				if (containmentSF != null)
					compBldr.cancelAttributeSetting((EObject) container, containmentSF);
			}
		}
		// Handle the case in which there may be constraints. i.e. instead of setLeftComponent(Component), add(Component. "left")
		if (!constraints.isEmpty()) {
			// The order of below will result in:
			//   1) Remove all of the constraints from the container under rule control.
			//   2) Remove all of the components from the constraints and from the container (for those not in constraints). Not under rule control.
			//   3) Post set will handle the constraints that were removed (but since by this time the components have been remove
			//      from the constraints, they won't be processed.
			compBldr.setApplyRules(true);
			compBldr.cancelAttributeSettings((EObject) container, sf_containerComponents, constraints); // Delete the constraint components under rule control so that they will go away.
			compBldr.setApplyRules(false);
			compBldr.cancelGroupAttributeSetting(constraints, sf_constraintComponent);	// Cancel out all of the component settings not under rule control since we are keeping them.
		}
		return compBldr.getCommand();
	}
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		return (
			classComponent.isInstance(child)
				&& ((!((EObject) getContainer()).eIsSet(sfLeftComponent) && !((EObject) getContainer()).eIsSet(sfTopComponent))
					|| (!((EObject) getContainer()).eIsSet(sfRightComponent)
						&& !((EObject) getContainer()).eIsSet(sfBottomComponent))));
	}
	/**
	 * Need this for the JSplitPaneLayoutEditPolicy to handle the special case in which
	 * the right/bottom position is occupied and the only component.
	 */
	public boolean isRightComponentOccupiedAndOnlyComponent() {
		EObject[] components = determineSplitpaneOccupants();
		if (components[TOP_COMP] == null && components[LEFT_COMP] == null && (components[BOTTOM_COMP] != null || components[RIGHT_COMP] != null))
			return true;
		return false;
	}
	
}
