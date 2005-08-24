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
package org.eclipse.ve.internal.java.rules;
/*
 *  $RCSfile: IChildRule.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * This rule is used for managing children.
 * It will handle the annotations and scoping. Applications can override and provide
 * their own rules which may handle more than just the annotations.
 */
public interface IChildRule extends IRule {
	
	public static final String RULE_ID = IChildRule.class.getName();	
	
	/**
	 * Rule to handle pre-creation of a child. The rule will normally handle annotations, 
	 * but overrides can handle more. At the time the command the rule returns is 
	 * executed, the annotations need to be attached to the child (or grandchildren)
	 * so that they can be found. They don't need to be attached at the time the
	 * command is returned from the rule.
	 * 
	 * The command should be placed before the actual add of the child to the parent
	 * so that annotation and scoping can be done correctly before the actual add child
	 * is done.
	 * 
	 * @param  domain The domain of the application.
	 * @param  target The target of the child. It must already be contained in a resource
	 *                 by the time the command is executed. It does not
	 *                 need to be contained when requesting the command thru the rule. Therefore
	 *                 any rule override MUST return a command that will not do any checking of containment
	 *                 until actual execution. It cannot do containment checks during prepare/canExecute.
	 *                 If not contained by time of execution it may or may not permit scoping. That
	 *                 is rule implementation specific, a specific implementation may be able to handle this.
	 * @param  newValue The new child that needs to be added.
	 * @param  feature The feature that is being set with the new value. 
	 * 
	 * @return a command that will perform the preCreate.	
	 */
	public Command preCreateChild(EditDomain domain, EObject target, EObject newChild, EReference feature);
	
	/**
	 * Post delete of a child. Called after setting or unsetting of a child. This
	 * allows the old child to be unscoped properly, plus anything else needed to remove
	 * something from the domain. It will walk through all of the
	 * children and PostSet them too.
	 * 
	 * This should be called only if the child had been set and was not a touch.
	 * If the child wasn't set, or it was a touch, then the old value is still valid.
	 * 
     * Since it will walk through children it is best to call this at the
     * highest point in the tree of properties as possible. If you are unsetting
     * a child which contains other properties, don't call postSet on any
     * of the children properties because that would be duplicate effort and
     * not needed.
     * 
     * Note: This will handle removing any annotations of any child that was removed.
     * 
	 * @param  domain The domain of the application so that the rule can handle annotations. 
	 * @param  oldValue The old value that needs to be unscoped.
	 * 
	 * @return a command that will perform the postset.
	 */
	public Command postDeleteChild(EditDomain domain, EObject oldChild);	

}
