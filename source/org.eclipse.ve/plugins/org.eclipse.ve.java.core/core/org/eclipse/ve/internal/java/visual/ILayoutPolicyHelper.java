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
 *  $RCSfile: ILayoutPolicyHelper.java,v $
 *  $Revision: 1.9 $  $Date: 2006-08-17 15:31:59 $ 
 */

import java.util.List;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.ContainerPolicy.Result;

/**
 * This is for Visual Containers with layouts. It supplies the
 * common functions for layout constraint management.
 * <p>
 * <b>Note:</b> The constraints are often an IDE format of the constraint
 * instead of a true java object instance. However, this is up to
 * each layout manager helper to determine if it wants to have the
 * constraints be an IDE format or a java object instance.
 *
 * Creation date: (11/2/00 4:23:49 PM)
 * @author: Peter Walker
 */
public interface ILayoutPolicyHelper {
	
/**
 * Use this value for a constraint when NO CONSTRAINT is to be applied.
 * A constraint of null will be set in the add statement. If this
 * value is used, the constraint will not be set. This means that
 * if null, the code used would be add(Component, null). If NO_CONSTRAINT_VALUE
 * is used, then add(Component) will be used in bean proxy, but code generation
 * has decided to use add(Component, component.getName()).
 * 
 * @since 1.0.0
 */
public static final Object NO_CONSTRAINT_VALUE = new Object();

/**
 * Create the child with the given constraint.
 * 
 * @param childComponent
 * @param constraint constraint to create with. The type of this is implementation dependent.
 * @param position
 * @return
 * 
 * @since 1.2.0
 */
public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position);

/**
 * Add the children with the given constraints.
 * 
 * @param childrenComponents children to add. 
 * @param constraints constraints for the children. The type is implementation dependent. 
 * @param position
 * @return
 * 
 * @since 1.2.0
 */
public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position);

/**
 * Change the constraint for the children.
 *
 * Note: This is only guaranteed to work if the children are contained
 * within their appropriate ConstraintComponent (it will look for these
 * so that they can be changed).
 */
public Command getChangeConstraintCommand(List children, List constraints);

/**
 * Orphan the children. 
 *
 * Note: This is only guaranteed to work if the children are contained
 * within their appropriate ConstraintComponent (it will look for these
 * so that they can be changed).
 */
public Result getOrphanChildrenCommand(List children);

/**
 * Orphan the constraints. This is used for pre-switch of constraints.
 * It means orphan the constraints because we are to switch to new ones.
 * Typically nothing needs to be done because the constraints are simply
 * the constraint settings, and they will be changed. So no need to do 
 * anything. However, null layout is special in that the constraints are
 * actually stored as separate settings. So in that case this command will
 * need to acutally do something, cancel all of the bounds, etc. settings.
 */
public Command getOrphanConstraintsCommand(List children);

/**
 * Delete the dependent children.
 * @param children
 * @return
 * 
 * @since 1.2.0
 */
public Command getDeleteDependentCommand(List children);

/**
 * Delete just one child.
 * @param child
 * @return
 * 
 * @since 1.2.0
 */
public Command getDeleteDependentCommand(Object child);


/**
 * Move the children to before the given child.
 * @param children
 * @param beforeChild
 * @return
 * 
 * @since 1.2.0
 */
public Command getMoveChildrenCommand(List children, Object beforeChild);

/**
 * Determine what the default constraint(s) are for this layout manager
 * and assign a constraint to each child.
 * Return a List with a constraint for each child.
 */
public List getDefaultConstraint(List children);

/**
 * Set ContainerEditPolicy to use. This will be called
 * on activation with a policy that has the model set, and
 * it will be called again on deactivation, this time with
 * policy being null.
 * 
 * @param policy on activation, a policy with the model set, on deactivation <code>null</code> will be passed in.
 */
public void setContainerPolicy(VisualContainerPolicy policy);
}


