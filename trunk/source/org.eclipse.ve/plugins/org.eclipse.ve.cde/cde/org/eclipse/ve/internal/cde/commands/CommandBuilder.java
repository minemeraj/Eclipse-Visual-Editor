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
package org.eclipse.ve.internal.cde.commands;
/*
 *  $RCSfile: CommandBuilder.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-03 19:21:04 $ 
 */



import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.propertysheet.command.ForwardUndoCompoundCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * This is a helper class that builds up a CompoundCommand and has
 * methods to add Apply and Cancel AttributeSetting commands to it.
 *
 * It can also create a preorder compound command.
 */

public class CommandBuilder {
	private CompoundCommand cmpCmd;
	private boolean appendAndExecute;
	private boolean dead;
	private String reasonDead;

	/**
	 * Create a command builder. The parm regularCmd says create
	 * a regular compound command or a preorder compound command.
	 */
	public CommandBuilder(String name, boolean regularCmd){
		cmpCmd = regularCmd ? new CompoundCommand(name) : new ForwardUndoCompoundCommand(name);
	}
	
	/**
	 * Create a command builder for a regular compound command.
	 */
	public CommandBuilder(String name) {
		this(name, true);
	}
	
	/**
	 * Create a regular compound command with no label
	 */
	public CommandBuilder() {
		this(null, true);
	}
	
	/**
	 * Create a command builder. The parm regularCmd says create
	 * a regular compound command or a preorder compound command.
	 */
	public CommandBuilder(boolean regularCmd){
		this(null, regularCmd);
	}
	
	/**
	 * Answer if the command is empty. 
	 */
	public boolean isEmpty() {
		return cmpCmd.isEmpty();
	}
	
	/**
	 * Answer if this builder is dead.
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public final boolean isDead() {
		return dead;
	}
	
	/**
	 * Mark this builder as dead.
	 * <p>
	 * This is used to mark it as unexecutable in a quicker manner than using getCommand().canExecute().
	 * Once marked dead it won't add any more and will return an unexecutable command. The compound
	 * command being created will be thrown away and any already executed commands will be undone. 
	 * 
	 * @see #markDead(String)
	 * @since 1.2.0
	 */
	public final void markDead() {
		if (!dead) {
			dead = true;
			if (appendAndExecute && !cmpCmd.isEmpty()) {
				cmpCmd.undo();
				cmpCmd.dispose();
			}
			cmpCmd.append(UnexecutableCommand.INSTANCE);
		}
	}
	
	/**
	 * Mark dead with a reason.
	 * <p>
	 * The reason can be queried. In the future the VE may be able to display why to the customer and this reason will then be shown.
	 * <p>
	 * If called when not dead, the reason will be saved. If called when already dead, only the first call will store the reason, the
	 * rest of the calls the reason will be ignored.
	 * @param reason
	 * 
	 * @see #getReason()
	 * @since 1.2.0
	 */
	public final void markDead(String reason) {
		if (!dead) {
			this.reasonDead = reason;
			markDead();
		} else if (reasonDead == null)
			this.reasonDead = reason;
	}
	
	/**
	 * Get the reason it is dead.
	 * <p>
	 * It will return <code>null</code> if not dead or not marked dead with a reason.
	 * @return reason pr <code>null</code> if not dead or not marked dead with a reason.
	 * 
	 * @since 1.2.0
	 */
	public final String getReason() {
		return reasonDead;
	}
	
	/**
	 * Return the command, unwrapping if only one command. If no commands, then
	 * null is returned.
	 */
	public Command getCommand(){
		if (!isDead())
			return cmpCmd.isEmpty() ? null : cmpCmd.unwrap();
		else
			return UnexecutableCommand.INSTANCE;
	}
	
	/**
	 * Return the compound command, it allows furter additions to it.
	 */
	public CompoundCommand getCompoundCommand() {
		return cmpCmd;
	}
	
	/**
	 * Append any old command to the currently built list.
	 */
	public void append(Command cmd) {
		internalAppend(cmd);
	}
	
	/**
	 * Add value at index.
	 */ 
	public void applyAttributeSetting(EObject target , EStructuralFeature feature , Object value , int index){
		internalAppend(internalApplyAttributeSetting(target, feature, value, index));
	}

	protected Command internalApplyAttributeSetting(EObject target, EStructuralFeature feature, Object value, int index) {
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(target);
		applyCmd.setAttribute(feature);
		applyCmd.setAttributeSettingValue(value);
		applyCmd.setInsertionIndex(index);
		return applyCmd;
	}
	
	/**
	 * Add value before specified value.
	 */
	public void applyAttributeSetting(EObject target , EStructuralFeature feature , Object value , Object before){
		internalAppend(internalApplyAttributeSetting(target, feature, value, before));
	}

	protected Command internalApplyAttributeSetting(
		EObject target,
		EStructuralFeature feature,
		Object value,
		Object before) {
		
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(target);
		applyCmd.setAttribute(feature);
		applyCmd.setAttributeSettingValue(value);
		applyCmd.setInsertBeforeValue(before);
		return applyCmd;
	}
	
	/**
	 * Add values at specified index.
	 */
	public void applyAttributeSettings(EObject target , EStructuralFeature feature , List values , int index){
		internalAppend(internalApplyAttributeSettings(target, feature, values, index));
	}

	protected Command internalApplyAttributeSettings(EObject target, EStructuralFeature feature, List values, int index) {
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(target);
		applyCmd.setAttribute(feature);
		applyCmd.setAttributeSettingValue(values);
		applyCmd.setInsertionIndex(index);
		return applyCmd;
	}
	
	/**
	 * Add values before specified value.
	 */
	public void applyAttributeSettings(EObject target , EStructuralFeature feature , List values , Object before){
		internalAppend(internalApplyAttributeSettings(target, feature, values, before));
	}

	protected Command internalApplyAttributeSettings(
		EObject target,
		EStructuralFeature feature,
		List values,
		Object before) {
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(target);
		applyCmd.setAttribute(feature);
		applyCmd.setAttributeSettingValue(values);
		applyCmd.setInsertBeforeValue(before);
		return applyCmd;
	}	
	
	/**
	 * Apply specified value (if multi-valued, add at end)
	 */
	public void applyAttributeSetting(EObject target , EStructuralFeature feature , Object value){
		internalAppend(internalApplyAttributeSetting(target, feature, value));
	}

	protected Command internalApplyAttributeSetting(EObject target, EStructuralFeature feature, Object value) {
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(target);
		applyCmd.setAttribute(feature);
		applyCmd.setAttributeSettingValue(value);
		return applyCmd;
	}

	/**
	 * Add mapentry to holder.
	 */
	public void applyAttributeSetting(KeyedValueHolder holder, BasicEMap.Entry value) {
		ApplyKeyedValueCommand applyCmd = new ApplyKeyedValueCommand();
		applyCmd.setTarget(holder);
		applyCmd.setValue(value);
		internalAppend(applyCmd);
	}
	
	/**
	 * Add values to end of feature.
	 */	
	public void applyAttributeSettings(EObject target , EStructuralFeature feature , List values){
		internalAppend(internalApplyAttributeSettings(target, feature, values));
	}

	protected Command internalApplyAttributeSettings(EObject target, EStructuralFeature feature, List values) {
		
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(target);
		applyCmd.setAttribute(feature);
		applyCmd.setAttributeSettingValue(values);
		return applyCmd;
	}
	
	/**
	 * Apply a complete new list of values to a many-valued list, replacing those already there.
	 */
	public void replaceEntireAttributeSettingList(EObject target , EStructuralFeature feature , List list){
		internalAppend(internalReplaceEntireAttributeSettingList(target, feature, list));
	}

	protected Command internalReplaceEntireAttributeSettingList(
		EObject target,
		EStructuralFeature feature,
		List list) {
		ApplyAttributeSettingListCommand applyCmd = new ApplyAttributeSettingListCommand();
		applyCmd.setTarget(target);
		applyCmd.setAttribute(feature);
		applyCmd.setAttributeSettingValue(list);
		return applyCmd;
	}
		
	/**
	 * Cancel the attribute setting value. If it is a single-valued feature it will unset it,
	 * if it is a multi-valued feature, it will clear out the entire list.
	 */
	public void cancelAttributeSetting(EObject target , EStructuralFeature feature){
	
		AbstractAttributeCommand cmd = !feature.isMany() ?
			(AbstractAttributeCommand) new CancelAttributeSettingCommand() : (AbstractAttributeCommand) new ClearAttributeSettingListCommand();
		cmd.setTarget(target);
		cmd.setAttribute(feature);
		internalAppend(cmd);
	}
	
	/**
	 * Cancel a single attribute setting value in a multi-valued feature.
	 */
	public void cancelAttributeSetting(EObject target , EStructuralFeature feature , Object value ){
	
		CancelAttributeSettingCommand cancelCmd = new CancelAttributeSettingCommand();
		cancelCmd.setTarget(target);
		cancelCmd.setAttribute(feature);
		cancelCmd.setAttributeSettingValue(value);
		internalAppend(cancelCmd);
	}	
	
	/**
	 * Remove the map entry of key from holder.
	 */
	public void cancelKeyedAttributeSetting(KeyedValueHolder holder, Object key) {
		CancelKeyedValueCommand cancelCmd = new CancelKeyedValueCommand();
		cancelCmd.setTarget(holder);
		cancelCmd.setKey(key);
		internalAppend(cancelCmd);
	}	
	
	/**
	 * Cancel a list attribute setting value. Cancel out the entries from the values list.
	 * <p>
	 * <b>Note:</b> The values list will be modified by the execution of the command. Because
	 * of this if the list is needed otherwise, then a copy of the list should be 
	 * sent in instead.
	 */
	public void cancelAttributeSettings(EObject target , EStructuralFeature feature , List values){
	
		CancelAttributeSettingCommand cancelCmd = new CancelAttributeSettingCommand();
		cancelCmd.setTarget(target);
		cancelCmd.setAttribute(feature);
		cancelCmd.setAttributeSettingValue(values);
		internalAppend(cancelCmd);
	}
	
	/**
	 * Cancel a single attribute setting for all elements in the list
	 */
	public void cancelGroupAttributeSetting(List targets , EStructuralFeature feature){
		
		Iterator iter = targets.iterator();
		while ( iter.hasNext() ) {
			EObject object = (EObject) iter.next();
			cancelAttributeSetting(object , feature);
		}
	}
	
	/**
	 * Replace a single setting within a list with a new value at the given index.
	 */
	public void replaceAttributeSetting(EObject target, EStructuralFeature feature, Object newValue, int index) {
		internalAppend(internalReplaceAttributeSetting(target, feature, newValue, index));		
	}

	protected Command internalReplaceAttributeSetting(
		EObject target,
		EStructuralFeature feature,
		Object newValue,
		int index) {
		ReplaceAttributeSettingCommand replaceCmd = new ReplaceAttributeSettingCommand();
		replaceCmd.setTarget(target);
		replaceCmd.setAttribute(feature);
		replaceCmd.setAttributeSettingValue(newValue);
		replaceCmd.setReplaceIndex(index);
		return replaceCmd;
	}
	
	protected void internalAppend(Command cmd) {
		if (cmd != null) {
			if (!isDead())
				if (cmd.canExecute()) {
					if (appendAndExecute)
						cmpCmd.appendAndExecute(cmd);
					else
						cmpCmd.append(cmd);
				} else
					markDead(); // Mark it dead so that further appends aren't added.
		}
	}
		
	/**
	 * Set mode to execute and Append. This means any commands appended will be
	 * executed first and then appended. If the compound command being built has
	 * not yet been executed at the time of the first append, the compound command
	 * will first be executed up to the current command and then the new command will
	 * be executed and appended. Once this occurs, it can't be switched back to false because
	 * the command has already started to be executed.
	 * 
	 * It is also important that you know that this was done because the command should
	 * not be executed again. Look up appendAndExecute on CompoundCommand to determine
	 * how to work with this concept.
	 */
	public void setExecuteAndAppend(boolean appendAndExecute) {
		if (!isDead() && (appendAndExecute || (this.appendAndExecute && cmpCmd.isEmpty()))) {
			if (!this.appendAndExecute && !cmpCmd.isEmpty())
				cmpCmd.execute();	// This is the switch to append and execute and we have something to execute.
			this.appendAndExecute = appendAndExecute;
		}
	}

	public boolean isExecuteAndAppend() {
		return appendAndExecute;
	}

}
