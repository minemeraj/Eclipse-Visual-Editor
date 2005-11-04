/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

;
/**
 * Null layout policy helper.
 */
public class NullLayoutPolicyHelper extends LayoutPolicyHelper {

	/**
	 * Constraint object to be passed into this class. rect: the constraint rectangle moved: True if the location portion of the rect should be
	 * applied resized: True if the size portion of the rect should be applied.
	 */
	public static class NullConstraint {

		public Rectangle rect;

		public boolean moved, resized;

		public NullConstraint(Rectangle rect, boolean moved, boolean resized) {
			this.rect = rect;
			this.moved = moved;
			this.resized = resized;
		}
	}
	
	private static class NullConstraintWrapper extends VisualContainerPolicy.ConstraintWrapper {

		public final NullConstraint nullConstraint;

		public NullConstraintWrapper(NullConstraint nullConstraint) {
			super(null);	// For SWT null layout the swt layout data (i.e. constraint) is always null.
			this.nullConstraint = nullConstraint;
		}
		
	}

	public NullLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
	}

	public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {
		VisualContainerPolicy.CorelatedResult result = policy.getCreateCommand(null, childComponent, position);
		Command createContributionCmd = result.getCommand();
		if (createContributionCmd == null || !createContributionCmd.canExecute()) {
			result.setCommand(UnexecutableCommand.INSTANCE); // It can't be created
			return result;
		}

		// Now if child was actually added:
		if (!result.getChildren().isEmpty()) {
			// check if this child has the constraint already set, or even has such constraint to set (it may not have a bounds feature).
			IJavaObjectInstance child = (IJavaObjectInstance) result.getChildren().get(0);
			EStructuralFeature boundsSF = JavaInstantiation.getSFeature(child, SWTConstants.SF_CONTROL_BOUNDS);
			if (child.eClass().getEStructuralFeature(boundsSF.getName()) != null) {
				CompoundCommand command = new CompoundCommand();
				NullConstraint new_constraint = (NullConstraint) constraint;
				Rectangle bounds = new Rectangle();

				IJavaObjectInstance existing_constraint = (IJavaObjectInstance) child.eGet(boundsSF);
				// During a copy the constraint might be set before the child is in a state ready to be instantiated
				// if it was copied with a constraint so we must check the eContainer() before doing target VM work
				if (existing_constraint != null && child.eContainer() != null) {
					IBeanProxy rect = BeanProxyUtilities.getBeanProxy(child);
					IRectangleBeanProxy preferredSize = BeanSWTUtilities.invoke_getBounds(rect);
					bounds = new Rectangle(preferredSize.getX(), preferredSize.getY(), preferredSize.getWidth(), preferredSize.getHeight());
					new_constraint = new NullConstraint(bounds, true, false);
				}
				command.append(createChangeConstraintCommand(child, new_constraint));
				// command.append(createChangeConstraintCommand((IJavaObjectInstance) childComponent, (NullConstraint)constraint));
				command.append(createContributionCmd);
				result.setCommand(command.unwrap());
			}
		}
		return result;
	}
	
	public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
		ArrayList wrapperedConstraints = new ArrayList(constraints.size());
		for (int i=0; i<constraints.size(); i++) {
			wrapperedConstraints.add(new NullConstraintWrapper((NullConstraint) constraints.get(i)));
		}
		
		VisualContainerPolicy.CorelatedResult result = policy.getAddCommand(wrapperedConstraints, childrenComponents, position);		
		CompoundCommand command = new CompoundCommand();
		command.append(getChangeConstraintCommand(result.getChildren(), result.getCorelatedList()));
		command.append(result.getCommand());
		result.setCommand(command.unwrap());
		return result;
	}

	protected void cancelConstraints(CommandBuilder cb, List children) {
		IJavaObjectInstance parent = (IJavaObjectInstance) policy.getContainer();
		EStructuralFeature sfComponentLocation = JavaInstantiation.getSFeature(parent, SWTConstants.SF_CONTROL_LOCATION), sfComponentSize = JavaInstantiation
				.getSFeature(parent, SWTConstants.SF_CONTROL_SIZE), sfComponentBounds = JavaInstantiation.getSFeature(parent,
				SWTConstants.SF_CONTROL_BOUNDS);

		Iterator childrenItr = children.iterator();
		while (childrenItr.hasNext()) {
			EObject child = (EObject) childrenItr.next();
			if (child.eIsSet(sfComponentBounds))
				cb.cancelAttributeSetting(child, sfComponentBounds);
			if (child.eIsSet(sfComponentSize))
				cb.cancelAttributeSetting(child, sfComponentSize);
			if (child.eIsSet(sfComponentLocation))
				cb.cancelAttributeSetting(child, sfComponentLocation);

		}
	}

	/**
	 * Determine what the default constraint(s) are for this layout manager and assign a constraint to each child. Return a List with a constraint for
	 * each child.
	 */
	public List getDefaultConstraint(List children) {
		return Collections.nCopies(children.size(), new NullConstraint(new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, -1, -1), true, true));
	}

	public Command getChangeConstraintCommand(List children, List constraints) {
		Iterator childItr = children.iterator();
		Iterator conItr = constraints.iterator();
		CompoundCommand cmd = new CompoundCommand();
		while (childItr.hasNext()) {
			Object constraint = conItr.next();
			if (constraint instanceof NullConstraintWrapper)
				constraint = ((NullConstraintWrapper) constraint).nullConstraint;
			cmd.append(createChangeConstraintCommand((IJavaObjectInstance) childItr.next(), (NullConstraint) constraint));
		}

		return !cmd.isEmpty() ? cmd.unwrap() : null;
	}

	protected Command createChangeConstraintCommand(IJavaObjectInstance child, NullConstraint constraint) {
		ApplyNullLayoutConstraintCommand cmd = new ApplyNullLayoutConstraintCommand();
		cmd.setTarget(child);
		cmd.setDomain(policy.getEditDomain());
		if (constraint.moved || constraint.resized)
			cmd.setConstraint(constraint.rect, constraint.moved, constraint.resized);
		else
			return null;
		return cmd;
	}

	private IBeanTypeProxy environmentBeanTypeProxy;

	private IMethodProxy getFieldMethodProxy;

	protected final IBeanTypeProxy getEnvironmentBeanTypeProxy(ProxyFactoryRegistry aProxyFactoryRegistry) {
		if (environmentBeanTypeProxy == null) {
			environmentBeanTypeProxy = aProxyFactoryRegistry.getBeanTypeProxyFactory().getBeanTypeProxy(
					"org.eclipse.ve.internal.swt.targetvm.Environment"); //$NON-NLS-1$		
		}
		return environmentBeanTypeProxy;
	}

	protected final IMethodProxy getGetFieldMethodProxy(ProxyFactoryRegistry aProxyFactoryRegistry) {
		if (getFieldMethodProxy == null) {
			getFieldMethodProxy = getEnvironmentBeanTypeProxy(aProxyFactoryRegistry).getMethodProxy("java.lang.reflect.field", "java.lang.Object"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return getFieldMethodProxy;
	}

}
