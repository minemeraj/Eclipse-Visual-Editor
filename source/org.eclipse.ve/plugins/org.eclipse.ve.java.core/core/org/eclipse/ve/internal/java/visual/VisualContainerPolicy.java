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
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: VisualContainerPolicy.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-11 21:23:48 $ 
 */
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.NoAddException;

import org.eclipse.ve.internal.java.core.BaseJavaContainerPolicy;

/**
 * A Java container policy for handling visuals with constraints.
 * <p>
 * It is abstract.
 * @since 1.1.0
 */
public abstract class VisualContainerPolicy extends BaseJavaContainerPolicy {
	
	/**
	 * A convenience method to create a CorelatedResult that is marked as not executable. Many of the layout policy helpers
	 * need to return this before actually calling any container policy add/creates.
	 * 
	 * @param child
	 * @param constraint
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public static CorelatedResult createUnexecutableResult(Object child, Object constraint) {
		CorelatedResult result = new CorelatedResult(Collections.singletonList(child), Collections.singletonList(constraint));
		result.setCommand(UnexecutableCommand.INSTANCE);
		return result;
	}
	
	/**
	 * A convenience method to create a CorelatedResult that is marked as not executable. Many of the layout policy helpers
	 * need to return this before actually calling any container policy add/creates.
	 * 
	 * @param children
	 * @param constraints
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public static CorelatedResult createUnexecutableResult(List children, List constraints) {
		CorelatedResult result = new CorelatedResult(children, constraints);
		result.setCommand(UnexecutableCommand.INSTANCE);
		return result;
	}
	
	/**
	 * A constraint wrapper. This can be used for any constraint in the add/create command's constraints list. This would be
	 * used only in special cases where you want the constraint in the list to be more than the actual constraint. If the
	 * constraint value is an instance of this class then the true constraint will be pulled from it and used.
	 * <p>
	 * An example of where this concept could be used would be for AWT add controls. For example when adding to a BorderLayout,
	 * the constraint could be the straight string constraint. No need for the ConstraintWrapper. But if adding to a null layout,
	 * the true constraint would be null, but there are the location and size. They are not actually used in the add() request but
	 * are applied later. But since it is possible that a {@link org.eclipse.ve.internal.cde.core.IConstraintHandler} could of 
	 * totally handled the child, there would be no way for the caller to know the child was handled and no location and size should 
	 * be applied. So this is when a ConstraintWrapper would be used. The caller would subclass constraint wrapper, and have the location and
	 * size also within the subclass. Then pass the constraint wrapper as the constraint. If the handler did completly handle the child, then
	 * the constraint would of been removed from the list of constraints. So the caller would now know it had been handled because the constraint
	 * wrapper is now not in the list.
	 * <p>
	 * A typical pattern for say null layout would be:
	 * <pre><code>
	 * List children = list of children to add
	 * List constraints = list of constraint wrappers that contain both a null constraint and the bounds for the coorresponding child
	 * getAddCommand(constraints, children, beforeChild);
	 * Iterator childItr = children.iterator(); 
	 * Iterator ccItr=constraints.iterator();
	 * while (childItr.hasNext()) {
	 *   Child child = (Child) childItr.next();
	 *   ConstraintWrapperSubclass c = (ConstraintWrapperSubclass) ccItr.next();
	 *   Rectangle bounds = c.getBounds();
	 *   ... apply the bounds to the child command ...
	 * }
	 * </code></pre>
	 * <p>
	 * This will work because if the add processing had totally handled a child, it would of removed both the child and the constraint from the
	 * two lists. 
	 * 
	 * @since 1.2.0
	 */
	public static class ConstraintWrapper {
		private final Object constraint;

		public ConstraintWrapper(Object constraint) {
			this.constraint = constraint;
		}
		
		public final Object getConstraint() {
			return constraint;
		}
	}

	/**
	 * Construct the policy.
	 * @param feature
	 * @param domain
	 * 
	 * @since 1.1.0.1
	 */
	public VisualContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(feature,domain);
	}
	
	/**
	 * Construct with just the domain.
	 * @param domain
	 * 
	 * @since 1.2.0
	 */
	public VisualContainerPolicy(EditDomain domain) {
		super(domain);
	}
	
	/**
	 * Convenience method for when there is only one child/constraint.

	 * @param constraint
	 * @param child
	 * @param position
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public CorelatedResult getCreateCommand(Object constraint, Object child, Object position) {
		return getCreateCommand(Collections.singletonList(constraint), Collections.singletonList(child), position);
	}
	
	/**
	 * Create the children with the given constraints before the given child.
	 * <p>
	 * @param constraints list of constraints (or {@link ConstraintWrapper}) for each child in the children list. The result will have the 
	 * modified list of constraints in it if the {@link org.eclipse.ve.internal.cde.core.IContainmentHandler} decides to change the list
	 * of children. Use {@link CorelatedResult#getCorelatedList()} to get the list of constraints. If not modified it will have the original list.
	 * @param children list of children to create.  The result will have the 
	 * modified list of children in it if the {@link org.eclipse.ve.internal.cde.core.IContainmentHandler} decides to change the list
	 * of children. Use {@link Result#getChildren()()} to get the list of children. If not modified it will have the original list.
	 * @param position the child to be placed before or <code>null</code> if to add at end.
	 * @return the result to actually do the create. It will have the command and the list of children created and the list of constraints for these children.
	 * 
	 * @since 1.2.0
	 */
	public final CorelatedResult getCreateCommand(List constraints, List children, Object position) {
		CorelatedResult result = new CorelatedResult(children, constraints);
		CommandBuilder preCmds = createCommandBuilder(true);
		CommandBuilder postCmds = createCommandBuilder(true);

		int origSize = children.size();
		try {
			getTrueChildren(result, true, preCmds, postCmds);
		} catch (NoAddException e) {
			preCmds.markDead();
		}
		
		if (preCmds.isDead() || postCmds.isDead()) {
			result.setCommand(UnexecutableCommand.INSTANCE);
			return result;
		}
		
		if (!result.getChildren().isEmpty() || origSize == 0) {
			// Either there are children.
			// Or it  could be that the handlers simply removed them all. If they did, then this is valid and we shouldn't go on to getAddCommand.
			// This is because if we sent an empty list into getAddCommand some subclasses treat this as invalid. But the handlers have determined
			// that it is valid. If children is empty and original is empty then the handlers didn't do it, so let normal processing handle it.
			getCreateCommand(result.getCorelatedList(), result.getChildren(), position, preCmds);
		}
		
		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			result.setCommand(UnexecutableCommand.INSTANCE);
		else
			result.setCommand(preCmds.getCommand());
		return result;
		

	}

	/**
	 * Called with the modified list of children/constraints to create. The {@link org.eclipse.ve.internal.cde.core.IConstraintHandler} has
	 * all ready been called. And the constraints list may have {@link ConstraintWrapper} instances in it, so implementers should be aware of this.
	 * 
	 * @param constraints list of constraints (possible {@link ConstraintWrapper} will be in the list).
	 * @param children list of coorresponding children.
	 * @param position child to go before or <code>null</code> if add to end.
	 * @param cbld command builder to build up commands into it.
	 * 
	 * @since 1.2.0
	 */
	protected abstract void getCreateCommand(List constraints, List children, Object position, CommandBuilder cbld);
	
	/**
	 * A convenience method to add one child/constraint.
	 *
	 * @param constraint
	 * @param child
	 * @param positionBeforeChild
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public CorelatedResult getAddCommand(Object constraint, Object child, Object positionBeforeChild) {
		return getAddCommand(Collections.singletonList(constraint), Collections.singletonList(child), positionBeforeChild);
	}
	/**
	 * Add the children with the given constraints before the given child.
	 * <p>
	 * The result will contain the modified list of children and constraints if the {@link org.eclipse.ve.internal.cde.core.IContainmentHandler} decided
	 * that it wanted to modify the children being added.
	 * @param constraints list of constraints (or {@link ConstraintWrapper}) for each child in the children list. 
	 * @param children list of children to add. 
	 * @param position the child to be placed before or <code>null</code> if to add at end.
	 * @return the result with the command to actually do the add. And the modified children and constraints ((@link CorelatedResult#getCorelatedList()} for the constraints).
	 * 
	 * @since 1.2.0
	 */
	public final CorelatedResult getAddCommand(List constraints, List children, Object position) {
		CorelatedResult result = new CorelatedResult(children, constraints);
		CommandBuilder preCmds = createCommandBuilder(true);
		CommandBuilder postCmds = createCommandBuilder(true);

		int origSize = children.size();
		try {
			getTrueChildren(result , false, preCmds, postCmds);
		} catch (NoAddException e) {
			preCmds.markDead();
		}
		
		if (preCmds.isDead() || postCmds.isDead()) {
			result.setCommand(UnexecutableCommand.INSTANCE);
			return result;
		}
		
		if (!result.getChildren().isEmpty() || origSize == 0) {
			// Either there are children.
			// Or it  could be that the handlers simply removed them all. If they did, then this is valid and we shouldn't go on to getAddCommand.
			// This is because if we sent an empty list into getAddCommand some subclasses treat this as invalid. But the handlers have determined
			// that it is valid. If children is empty and original is empty then the handlers didn't do it, so let normal processing handle it.
			getAddCommand(result.getCorelatedList(), result.getChildren(), position, preCmds);
		}
		
		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			result.setCommand(UnexecutableCommand.INSTANCE);
		else
			result.setCommand(preCmds.getCommand());
		return result;
	}
	
	/**
	 * Called with the modified list of children/constraints to add. The {@link org.eclipse.ve.internal.cde.core.IConstraintHandler} has
	 * already been called. And the constraints list may have {@link ConstraintWrapper} instances in it, so implementers should be aware of this.
	 * 
	 * @param constraints list of constraints (possible {@link ConstraintWrapper} will be in the list).
	 * @param children list of coorresponding children.
	 * @param position child to go before or <code>null</code> if add to end.
	 * @param cbld command builder to build up commands into it.
	 * 
	 * @since 1.2.0
	 */
	protected abstract void getAddCommand(List constraints, List children, Object position, CommandBuilder cbld);
	
}
