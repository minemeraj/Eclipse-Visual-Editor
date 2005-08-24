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
 *  $RCSfile: IPropertyRule.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.rules.IRule;

/**
 * This rule is used for managing properties in the JBCF application.
 * For commands to change an IPropertySource property, use RuledSetPropertyCommand/RuledResetPropertyCommand. It will
 * call the pre/post sets automatically when needed. This can be used for
 * simple single property. If many properties are being set/unset, then these
 * commands will needed to be compounded together by code.
 */
public interface IPropertyRule extends IRule {
	
	public static final String RULE_ID = IPropertyRule.class.getName();
	
	/**
	 * PreSet of a property. Called before setting of a property. This
	 * allows the new property to be scoped properly, plus anything else
	 * needed to add a property to the domain.
     * It will walk through all children and preSet them too.
     * 
     * This should only be called if this is a new setting. It should not
     * be called for "touch" because no need to change scoping.
     * 
     * Since it will walk through children it is best to call this at the
     * highest point in the tree of properties as possible. If you are setting
     * a property which contains other properties, don't call preSet on any
     * of the children properties because that would be duplicate effort and
     * not needed.
     * 
     * Note: This will also handle adding the annotations in case any of
     * the child settings have annotations.
     * 
	 * @param  domain The domain of the application so that the rule can handle
	 *                 proper scoping of the new property.
	 * @param  target The target of the property. Used to determine scoping. It must
	 *                 already be scoped by the time the command is executed. It does not
	 *                 need to be scoped when requesting the command thru the rule. Therefore
	 *                 any rule override MUST return a command that will not do any checking of scoping
	 *                 until actual execution. It cannot do scoping checks during prepare/canExecute.
	 *                 If not scoped by time of execution it may or may not permit scoping. That
	 *                 is rule implementation specific, a specific implementation may be able to handle this.
	 * @param  newValue The new value that needs to be scoped.
	 * @param  feature The feature that is being set with the new value. Used to help determine scoping, e.g.
	 *                  if the feature is a containment, then the new value itself will not be scoped, though
	 *                  children will be. There are times the feature isn't known (e.g. when dealing with IPropertySources
	 *                  IPropertyDescriptors) then pass in null. This will be treated as non-containment. This would
	 *                  cause a slight problem because it may cause an extra scoping to occur, which will correct itself
	 *                  in the end.
	 * 
	 * @return a command that will perform the preset.
	 */
	public Command preSet(EditDomain domain, EObject target, EObject newValue, EReference feature);

	/**
	 * PostSet of a property. Called after setting or unsetting of a property. This
	 * allows the old property to be unscoped properly, plus anything else needed to remove
	 * something from the domain. It will walk through all of the
	 * children and PostSet them to.
	 * 
	 * This should be called only if the property had been set and was not a touch.
	 * If the property wasn't set, or it was a touch, then the old value is still valid.
	 * 
     * Since it will walk through children it is best to call this at the
     * highest point in the tree of properties as possible. If you are unsetting
     * a property which contains other properties, don't call postSet on any
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
	public Command postSet(EditDomain domain, EObject oldValue);

}
