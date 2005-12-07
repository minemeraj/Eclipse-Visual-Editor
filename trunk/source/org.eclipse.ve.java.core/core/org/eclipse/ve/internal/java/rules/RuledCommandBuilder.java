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
 *  $RCSfile: RuledCommandBuilder.java,v $
 *  $Revision: 1.10 $  $Date: 2005-12-07 23:12:35 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * This is like a command builder but it handles the IPropertyRule and IChildRule for you.
 * 
 * The contract is that when applyRules is true, the preset will be done and then the actual
 * request will done. And the postset will be queued up to the end. This allows the build up
 * of a tree of settings, and then the applyRule is turned on so that the next setting, which
 * will be the top of the tree, will have a preset, and then the apply. The preset will then
 * take care of the entire tree at once. All postset will gathered to the end. That way
 * the tree can be deconstructed (for example a sub-setting is actually moving and not going
 * away) before the post set comes in to clean up.
 * 
 * Note it is tricky for ForwardUndoCommands being built. Postset and the grouping of preset and apply must
 * be undone in reverse order, while each individual grouping must be undone in forward order. So
 * each preset/apply in a forward undo command will become a CompoundCommand added to maintain proper undo order.
 */
public class RuledCommandBuilder extends CommandBuilder {

	protected EditDomain domain;
	protected CompoundCommand postCommand;
	private boolean regularCmd = true;
	private boolean propertyRule = true;
	private boolean applyRules = true;
	private boolean applyRulesOnTouch;
	
	/**
	 * Constructor for RuledCommandBuilder.
	 * @param name
	 * @param regularCmd
	 */
	public RuledCommandBuilder(EditDomain domain, String name, boolean regularCmd) {
		super(name, regularCmd);
		this.domain = domain;
		this.regularCmd = regularCmd;
	}
	 
	/**
	 * Constructor for RuledCommandBuilder.
	 * @param name
	 */
	public RuledCommandBuilder(EditDomain domain, String name) {
		super(name);
		this.domain = domain;		
	}

	/**
	 * Constructor for RuledCommandBuilder.
	 */
	public RuledCommandBuilder(EditDomain domain) {
		super();
		this.domain = domain;		
	}

	/**
	 * Constructor for RuledCommandBuilder.
	 * @param regularCmd
	 */
	public RuledCommandBuilder(EditDomain domain, boolean regularCmd) {
		this(domain, null, regularCmd);
	}
	
	/**
	 * Allows appending to the post command section. These commands
	 * are guarenteed to be executed after any other commands appended
	 * or added in the normal way.
	 * 
	 * @param post The post command to be appended.
	 */
	public void appendPost(Command post) {
		if (!isDead() && post != null) {
			if (postCommand == null)
				postCommand = new CompoundCommand("Postset of properties"); //$NON-NLS-1$
			postCommand.append(post);
		}
	}
	
	protected CompoundCommand appendPre(Command preCmd, CompoundCommand buildUp) {
		if (regularCmd)
			internalAppend(preCmd);
		else {
			if (!isDead()) {
				if (buildUp == null)
					buildUp = new CompoundCommand();
				buildUp.append(preCmd);
			}
		}
		return buildUp;
	}

	protected CompoundCommand propertySet(EObject target, EStructuralFeature feature, Object newValue, CompoundCommand buildUp) {
		if (feature instanceof EReference) {
			// If feature is not an EReference, then pre/post aren't in effect.
			boolean isSet = target.eIsSet(feature);
			EObject oldValue = (EObject) target.eGet(feature);
			if (!isSet || (oldValue != newValue || applyRulesOnTouch)) {
				// Not a Touch (or a touch but apply rules anyway). Need a preSet command.
				buildUp = appendPre(createPre(target, feature, newValue), buildUp);
			}
	
			if (isSet && oldValue != newValue)
				appendPost(createPost(oldValue));
		}
		
		return buildUp;
	}


	protected Command createPre(EObject target, EStructuralFeature feature, Object newValue) {
		return isPropertyRule() ? 
			((IPropertyRule) domain.getRuleRegistry().getRule(IPropertyRule.RULE_ID)).preSet(domain, target, (EObject) newValue, (EReference) feature) :
			((IChildRule) domain.getRuleRegistry().getRule(IChildRule.RULE_ID)).preCreateChild(domain, target, (EObject) newValue, (EReference) feature);
	}
	
	protected Command createPost(Object oldValue) {
		return isPropertyRule() ? 
			((IPropertyRule) domain.getRuleRegistry().getRule(IPropertyRule.RULE_ID)).postSet(domain, (EObject) oldValue) :
			((IChildRule) domain.getRuleRegistry().getRule(IChildRule.RULE_ID)).postDeleteChild(domain, (EObject) oldValue);			
	}	
	
	protected void propertyUnset(EObject target, EStructuralFeature feature) {
		// Unset a single valued feature.
		if (feature instanceof EReference) {
			// If feature is not an EReference, then post isn't in effect.
			if (target.eIsSet(feature))
				appendPost(createPost(target.eGet(feature)));
		}
	}
	
	protected void propertyUnset(EObject target, EStructuralFeature feature, Object oldValue) {
		// Unset a specific value from a many-valued feature.
		if (!isDead() && feature instanceof EReference) {
			// If feature is not an EReference, then post isn't in effect.
			appendPost(createPost(oldValue));
		}
	}	
	
	protected CompoundCommand propertyAdd(EObject target, EStructuralFeature feature, Object newValue, CompoundCommand buildUp) {
		// An add is always a pre, with no post. It can be never be a touch.
		if (feature instanceof EReference) {
			// If feature is not an EReference, then pre isn't in effect.
			buildUp = appendPre(createPre(target, feature, newValue), buildUp);
		}
		
		return buildUp;
	}	
	
	protected CompoundCommand propertyAdds(EObject target, EStructuralFeature feature, List values, CompoundCommand buildUp) {
		if (!isDead() && feature instanceof EReference) {
			// If feature is not an EReference, then pre isn't in effect.
			Iterator itr = values.iterator();
			while (itr.hasNext()) {
				buildUp = appendPre(createPre(target, feature, itr.next()), buildUp);
			}
		}
		return buildUp;
	}	
	
	/**
	 * Add value at index.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#applyAttributeSetting(EObject, EStructuralFeature, Object, int)
	 */
	public void applyAttributeSetting(EObject target, EStructuralFeature feature, Object value, int index) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules)
				buildUp = propertyAdd(target, feature, value, buildUp);

			if (buildUp == null)
				super.applyAttributeSetting(target, feature, value, index);
			else {
				buildUp.append(internalApplyAttributeSetting(target, feature, value, index));
				internalAppend(buildUp);
			} 
		}		
	}

	public void applyAttributeSetting(EObject target, String featureName, Object value){
		
		applyAttributeSetting(target,target.eClass().getEStructuralFeature(featureName),value);
		
	}
	
	/**
	 * Add value before specified value.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#applyAttributeSetting(EObject, EStructuralFeature, Object, Object)
	 */
	public void applyAttributeSetting(EObject target, EStructuralFeature feature, Object value, Object before) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules)
				buildUp = propertyAdd(target, feature, value, buildUp);

			if (buildUp == null)
				super.applyAttributeSetting(target, feature, value, before);
			else {
				buildUp.append(internalApplyAttributeSetting(target, feature, value, before));
				internalAppend(buildUp);
			} 
		}		
	}

	/**
	 * Apply specified value (if multi-valued, add at end).
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#applyAttributeSetting(EObject, EStructuralFeature, Object)
	 */
	public void applyAttributeSetting(EObject target, EStructuralFeature feature, Object value) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules) {
				if (!feature.isMany())
					buildUp = propertySet(target, feature, value, buildUp);
				else
					buildUp = propertyAdd(target, feature, value, buildUp);
			}

			if (buildUp == null)
				super.applyAttributeSetting(target, feature, value);
			else {
				buildUp.append(internalApplyAttributeSetting(target, feature, value));
				internalAppend(buildUp);
			}
		}
	}

	/**
	 * Add values at specified index.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#applyAttributeSettings(EObject, EStructuralFeature, List, int)
	 */
	public void applyAttributeSettings(EObject target, EStructuralFeature feature, List values, int index) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules)
				buildUp = propertyAdds(target, feature, values, buildUp);

			if (buildUp == null)
				super.applyAttributeSettings(target, feature, values, index);
			else {
				buildUp.append(internalApplyAttributeSettings(target, feature, values, index));
				internalAppend(buildUp);
			}
		}
	}

	/**
	 * Add values before specified value.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#applyAttributeSettings(EObject, EStructuralFeature, List, Object)
	 */
	public void applyAttributeSettings(EObject target, EStructuralFeature feature, List values, Object before) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules)
				buildUp = propertyAdds(target, feature, values, buildUp);

			if (buildUp == null)
				super.applyAttributeSettings(target, feature, values, before);
			else {
				buildUp.append(internalApplyAttributeSettings(target, feature, values, before));
				internalAppend(buildUp);
			} 
		}		
	}

	/**
	 * Add values to end of feature.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#applyAttributeSettings(EObject, EStructuralFeature, List)
	 */
	public void applyAttributeSettings(EObject target, EStructuralFeature feature, List values) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules)
				buildUp = propertyAdds(target, feature, values, buildUp);

			if (buildUp == null)
				super.applyAttributeSettings(target, feature, values);
			else {
				buildUp.append(internalApplyAttributeSettings(target, feature, values));
				internalAppend(buildUp);
			} 
		}		
	}

	/**
	 * Cancel a single attribute setting value in a multi-valued feature.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#cancelAttributeSetting(EObject, EStructuralFeature, Object)
	 */
	public void cancelAttributeSetting(EObject target, EStructuralFeature feature, Object value) {
		if (!isDead()) {
			if (applyRules)
				propertyUnset(target, feature, value);
			super.cancelAttributeSetting(target, feature, value);
		}
	}
	
	// Special class to do clear of entire list. Need to do this special because
	// don't know the true list until execute time of clear command. So there
	// will be two commands, one goes on the primary command stack to gather the
	// list and give it to the post command. Then when the post command runs it
	// can clear the list.
	protected static class ClearAttributeSettingPostCommand extends CommandWrapper {
		protected List unsetValues;
		protected EditDomain domain;
		protected boolean isProperty;
		
		public ClearAttributeSettingPostCommand(EditDomain domain, boolean isProperty) {
			this.domain = domain;	
			this.isProperty = isProperty;
		}
		
		/**
		 * @see com.ibm.etools.common.command.Command#execute()
		 */
		public void execute() {
			if (unsetValues != null && !unsetValues.isEmpty()) {
				CompoundCommand c = new CompoundCommand();
				Iterator itr = unsetValues.iterator();
				while (itr.hasNext()) {
					c.append(isProperty ?
						((IPropertyRule) domain.getRuleRegistry().getRule(IPropertyRule.RULE_ID)).postSet(domain, (EObject) itr.next()) :
						((IChildRule) domain.getRuleRegistry().getRule(IChildRule.RULE_ID)).postDeleteChild(domain, (EObject) itr.next()));
				}
				if (c.isEmpty())
					command = NoOpCommand.INSTANCE;
				else
					command = c.unwrap();				
			} else
				command = NoOpCommand.INSTANCE;
				
			super.execute();
		}

		/**
		 * @see com.ibm.etools.common.command.AbstractCommand#prepare()
		 */
		protected boolean prepare() {
			return true;
		}

	}
	
	protected static class GatherClearAttributeSettingCommand extends CommandWrapper {
		protected List values;
		protected ClearAttributeSettingPostCommand clearPostCmd;
		
		public GatherClearAttributeSettingCommand(List values, ClearAttributeSettingPostCommand clearPostCmd) {
			this.values = values;
			this.clearPostCmd = clearPostCmd;
		}
		
		
		/**
		 * @see com.ibm.etools.common.command.Command#execute()
		 */
		public void execute() {
			if (!values.isEmpty())
				clearPostCmd.unsetValues = new ArrayList(values);	// Send the current values over.
				
			command = NoOpCommand.INSTANCE;
			super.execute();
		}

		/**
		 * @see com.ibm.etools.common.command.AbstractCommand#prepare()
		 */
		protected boolean prepare() {
			return true;
		}

	}

	/**
	 * Cancel the attribute setting value. If it is a single-valued feature it will unset it,
	 * if it is a multi-valued feature, it will clear out the entire list.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#cancelAttributeSetting(EObject, EStructuralFeature)
	 */
	public void cancelAttributeSetting(EObject target, EStructuralFeature feature) {
		if (!isDead()) {
			if (applyRules) {
				if (feature instanceof EReference) {
					if (feature.isMany()) {
						listClear(target, feature);
					} else {
						if (target.eIsSet(feature))
							appendPost(createPost(target.eGet(feature)));
					}
				}
			}
			super.cancelAttributeSetting(target, feature);
		}
	}

	protected void listClear(EObject target, EStructuralFeature feature) {
		if (!isDead()) {
			// If feature is many, this means clear out the entire feature. Need to use the special commands for this.
			ClearAttributeSettingPostCommand post = new ClearAttributeSettingPostCommand(domain, isPropertyRule());
			internalAppend(new GatherClearAttributeSettingCommand((List) target.eGet(feature), post)); // So that it gives the list over to the post command before the list is cleared.
			appendPost(post);
		}
	}

	/**
	 * Cancel a list attribute setting value. Cancel out the entries from the values list.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#cancelAttributeSettings(EObject, EStructuralFeature, List)
	 */
	public void cancelAttributeSettings(EObject target, EStructuralFeature feature, List values) {
		if (!isDead()) {
			if (applyRules) {
				if (feature instanceof EReference) {
					// If feature is not an EReference, then post isn't in effect.
					Iterator itr = values.iterator();
					while (itr.hasNext()) {
						appendPost(createPost(itr.next()));
					}
				}
			}
			super.cancelAttributeSettings(target, feature, values);
		}
	}

	/**
	 * Cancel a single attribute setting for all targets in the list for the specified feature.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#cancelGroupAttributeSetting(List, EStructuralFeature)
	 */
	public void cancelGroupAttributeSetting(List targets, EStructuralFeature feature) {
		if (!isDead()) {
			if (applyRules) {
				Iterator iter = targets.iterator();
				while (iter.hasNext()) {
					EObject object = (EObject) iter.next();
					cancelAttributeSetting(object, feature);
				}
			} else
				super.cancelGroupAttributeSetting(targets, feature);
		}
	}

	/**
	 * Return the command, unwrapping if only one command. If no commands, then
	 * null is returned.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#getCommand()
	 */
	public Command getCommand() {
		if (!isDead()) {
			if (postCommand != null) {
				CompoundCommand c = new CompoundCommand();
				c.append(super.getCommand());
				c.append(postCommand.unwrap());
				return c.unwrap();
			} else
				return super.getCommand();
		} else
			return super.getCommand();
	}

	/**
	 * Answer if the command is empty. 
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#isEmpty()
	 */
	public boolean isEmpty() {
		return super.isEmpty() && (postCommand == null || postCommand.isEmpty());
	}

	/**
	 * Replace a single setting within a list with a new value at the given index.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#replaceAttributeSetting(EObject, EStructuralFeature, Object, int)
	 */
	public void replaceAttributeSetting(EObject target, EStructuralFeature feature, Object newValue, int index) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules) {
				buildUp = propertyAdd(target, feature, newValue, buildUp); // Treat it as an add, we will get the old value here to unset it.
				propertyUnset(target, feature, ((List) target.eGet(feature)).get(index));
			}

			if (buildUp == null)
				super.replaceAttributeSetting(target, feature, newValue, index);
			else {
				buildUp.append(internalReplaceAttributeSetting(target, feature, newValue, index));
				internalAppend(buildUp);
			} 
		}		
	}

	/**
	 * Apply a complete new list of values to a many-valued list, replacing those already there.
	 * 
	 * @see org.eclipse.ve.internal.cde.commands.CommandBuilder#replaceEntireAttributeSettingList(EObject, EStructuralFeature, List)
	 */
	public void replaceEntireAttributeSettingList(EObject target, EStructuralFeature feature, List list) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules) {
				// This is tricky. It is like a clear list followed by an apply all.
				buildUp = propertyAdds(target, feature, list, buildUp);
				listClear(target, feature);
			}
			if (buildUp == null)
				super.replaceEntireAttributeSettingList(target, feature, list);
			else {
				buildUp.append(internalReplaceEntireAttributeSettingList(target, feature, list));
				internalAppend(buildUp);
			}
		}
	}
	
	/**
	 * This is used to assign membership to values that aren't being referenced directly but need to 
	 * have membership assigned. For example a visual controller that returns a visual and the visual
	 * is what is hooked into the model as a child of some other visual. In this case the controller
	 * would not be child of something and it would not be on the freeform, so it would not normally
	 * be dragged in via standard settings. This method will assign membership through a command to
	 * the most logical place.
	 * @param target object that needs membership assignment
	 * @param relativeTo object that the target should be relative. In other words if the target is
	 * normally local to something else, this object is what it should be local to.
	 * 
	 * @since 1.2.0
	 */
	public void assignMembership(EObject target, EObject relativeTo) {
		if (!isDead()) {
			CompoundCommand buildUp = null;
			if (applyRules) {
				buildUp = appendPre(createPre(relativeTo, null, target), null);
			}

			if (buildUp == null)
				return;
			else {
				internalAppend(buildUp.unwrap());
			}
		}		
	}
	
	/**
	 * This is used to cancel membership of values that aren't being referenced directly but
	 * need to be deleted. For example a visual controller that returns a visual and the visual
	 * is what is hooked into the model as a child of some other visual. In this case the controller
	 * would not be child of something and it would not be on the freeform, so it would not normally
	 * be dragged in via standard settings. This method will cancel the membership through a command.
	 * @param target
	 * 
	 * @since 1.2.0
	 */
	public void cancelMembership(EObject target) {
		if (!isDead()) {
			if (applyRules) {
				appendPost(createPost(target));
			}
		}		
	}

	/**
	 * Set whether in property or child mode. If true, then
	 * in property mode, where all settings will use the IPropertyRule.
	 * If false, then all settings will use IChildRule.
	 * 
	 * This is a mode, so that if changed, then the next attribute settings/cancels
	 * will use that mode. The mode can be switched back and forth and it applies
	 * to only the settings that were done while it was set.
	 * 
	 * @param isProperty true if this should use IPropertyRule.
	 * @return The previous setting.
	 */
	public boolean setPropertyRule(boolean propertyRule) {
		boolean oldPropertyRule = this.propertyRule;
		this.propertyRule = propertyRule;
		return oldPropertyRule;
	}

	/**
	 * Return the current property setting.
	 * 
	 * @return true if using IPropertyRule.
	 * @see RuleCommandBuilder#setPropertyRule(boolean)
	 * 
	 */
	public boolean isPropertyRule() {
		return propertyRule;
	}

	/**
	 * Set whether to use the apply rules or not.
	 * This is a mode. When true, which is the default, the
	 * rules will be applied. When false, the rules will not
	 * be applied. This allows intermixing ruled and unruled
	 * in the same command. 
	 * 
	 * @param applyRules true means use the rules.
	 * @return The previous setting.
	 */
	public boolean setApplyRules(boolean applyRules) {
		boolean oldApplyRules = this.applyRules;
		this.applyRules = applyRules;
		return oldApplyRules;
	}

	/**
	 * Return the current applyRules setting.
	 * 
	 * @return true if applying rules..
	 * @see RuleCommandBuilder#setApplyRules(boolean)
	 * 
	 */
	public boolean isApplyRules() {
		return applyRules;
	}

	/**
	 * Is the apply rules on touch on. This means that even on a touch that rules should applied.
	 * Normally (default is <code>false</code>) on a touch the rules are not applied. If this is <code>true</code> and {@link #isApplyRules()} also
	 * <code>true</code> then the rules will be applied on a touch.
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public boolean isApplyRulesOnTouch() {
		return applyRulesOnTouch;
	}

	/**
	 * Set the apply rules on touch setting.
	 * @param applyRulesOnTouch
	 * @return old setting value
	 * 
	 * @see #isApplyRulesOnTouch()
	 * @since 1.2.0
	 */
	public boolean setApplyRulesOnTouch(boolean applyRulesOnTouch) {
		boolean old = this.applyRulesOnTouch;
		this.applyRulesOnTouch = applyRulesOnTouch;
		return old;
	}

}
