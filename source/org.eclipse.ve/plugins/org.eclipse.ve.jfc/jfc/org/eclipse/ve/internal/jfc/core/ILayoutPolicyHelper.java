package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ILayoutPolicyHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.List;

import org.eclipse.gef.commands.Command;

/**
 * This is for awt Container layouts. It supplies the
 * common functions for layout constraint management.
 *
 * Note: The constraints are often an IDE format of the constraint
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
 */
public static final Object NO_CONSTRAINT_VALUE = new Object();

/**
 * Create the children at these constraints and before the position.
 * childComponent: The child to create.
 * constraint: The constraint to be applied.
 * position: The child to be placed before (null if at end).
 */
public Command getCreateChildCommand(Object childComponent, Object constraint, Object position);

/**
 * Add the children at these constraints and before the position.
 * childrenComponent: The children to add.
 * constraints: The constraints to be applied.
 * position: The children to be placed before (null if at end).
 * ContainerEditPolicy: The container edit policy.
 */
public Command getAddChildrenCommand(List childrenComponents, List constraints, Object position);

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
public Command getOrphanChildrenCommand(List children);

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
 * Determine what the default constraint(s) are for this layout manager
 * and assign a constraint to each child.
 * Return a List with a constraint for each child.
 */
public List getDefaultConstraint(List children);

/**
 * Set ContainerEditPolicy to use.
 */
public void setContainerPolicy(ContainerPolicy policy);
}


