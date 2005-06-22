package org.eclipse.ve.internal.propertysheet.command;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CommandStackPropertySheetEntry.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-22 15:21:41 $ 
 */


import java.text.MessageFormat;
import java.util.Arrays;
import java.util.EventObject;

import org.eclipse.gef.commands.*;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import org.eclipse.ve.internal.propertysheet.*;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * This is an implementation of the property sheet entry which will use
 * the standard commands to set and reset properties. It will
 * also handle the special descriptors ISourcedPropertyDescriptor and ICommandPropertyDescriptor.
 */
public class CommandStackPropertySheetEntry extends AbstractPropertySheetEntry {
	protected final MessageFormat sApplyFormat, sResetFormat;
	{
		sApplyFormat = new MessageFormat(PropertysheetMessages.getString("apply_value"));
		sResetFormat = new MessageFormat(PropertysheetMessages.getString("reset_value"));		
	}
	
	public CommandStackPropertySheetEntry(CommandStack stack, CommandStackPropertySheetEntry parent, IPropertySourceProvider provider) {
		super(parent, provider);
		fStack = stack;
		if (parent == null) {
			// We are the root, so listen for stack changes so that we can refresh.
			fStack.addCommandStackListener(fStackListener = new CommandStackListener(){
				public void commandStackChanged(EventObject e){
					refreshFromRoot();
				}
			});
		}
	}

	private CommandStackListener fStackListener = null;
	protected CommandStack fStack;
	
	/* (non-Javadoc)
	 * Method declared on IPropertySheetEntry.
	 */
	public void dispose() {
		if (fStackListener != null)
			fStack.removeCommandStackListener(fStackListener);
		super.dispose();
	}
	
	/**
	 * Create a property sheet entry of the desired type.
	 * Use the provider passed in.
	 *
	 * It is used to create children of this current entry.
	 */
	protected IDescriptorPropertySheetEntry createPropertySheetEntry(IPropertySourceProvider provider) {
		return new CommandStackPropertySheetEntry(fStack, this, provider);
	}
		
	protected boolean isPropertySet() {
		if (parent != null) {
			IPropertySource[] propSources = parent.getPropertySources();
			if (propSources.length == 0)
				return true;
			boolean isSet = false;
			if (fDescriptors[0] instanceof ISourcedPropertyDescriptor)
				isSet = ((ISourcedPropertyDescriptor) fDescriptors[0]).isSet(propSources[0]);
			else
				isSet = propSources[0].isPropertySet(fDescriptors[0].getId());
			return isSet;
		} else
			return true;
	}
			
	/**
	 * Apply all of the values to thier sources.
	 * We will use commands.
	 *
	 * This will send in a FowardUndoCompoundCommand to have the
	 * settings built into, and then whenit returns it will do
	 * an unwrap (in case there is only one entry, which is probably
	 * 90% of the time), and then execute that resulting command.
	 */
	protected void primApplyValues() {	
		ForwardUndoCompoundCommand fwdcmd = new ForwardUndoCompoundCommand(sApplyFormat.format(new Object[] {fDescriptors[0].getDisplayName()}));
		applyTo(fwdcmd);
		fStack.execute(fwdcmd.unwrap());
	}
	
	/**
	 * Apply the values into a new compound command and add to the foward undo compound command passed in.
	 * Then send to the parent to let it add in its updates.
	 */
	public void applyTo(ForwardUndoCompoundCommand fwdcmd) {
		CompoundCommand cmd = new CompoundCommand();
		
		IPropertySource[] propSources = parent.getPropertySources();	// Get parent sources to apply against
		String applyString = sApplyFormat.format(new Object[] {fDescriptors[0].getDisplayName()});
		for (int i = 0; i < propSources.length; i++) {
			if (fDescriptors[i] instanceof ICommandPropertyDescriptor) {
				// It's a command property descriptor. It will come up with the command.
				ICommandPropertyDescriptor d = (ICommandPropertyDescriptor) fDescriptors[i];
				cmd.append(d.setValue(propSources[i], getEditValue(i)));
			} else {
				// It is default descriptor
				cmd.append(createSetPropertyCommand(propSources[i], applyString, fDescriptors[i].getId(), getEditValue(i)));
			}
		}
		
		fwdcmd.append(cmd.unwrap());
		((CommandStackPropertySheetEntry) parent).childChanged(fwdcmd);	
	}
	
	protected Command createSetPropertyCommand(IPropertySource propertySource, String applyString, Object descriptorID, Object value) {
		SetPropertyValueCommand setCommand;
		setCommand = new SetPropertyValueCommand(applyString);
		setCommand.setTarget(propertySource);
		setCommand.setPropertyId(descriptorID);
		setCommand.setPropertyValue(value);
		return setCommand;
	}
	
	/**
	 * A child has changed. It is passing in the command being built.
	 * If there is a parent to this one, then applyTo the command the values
	 * of this entry. If there is no parent, then this is the root and there is
	 * nothing to apply.
	 */
	public void childChanged(ForwardUndoCompoundCommand fwdcmd) {
		if (parent != null)
			applyTo(fwdcmd);
	}
	
	/**
	 * Given the edit value, fill in the passed in array
	 * with the appropriate values. This is used to take
	 * the value from the editor and to propagate it to
	 * all of the values for this entry.
	 */
	protected void primFillValues(Object newEditValue, Object[] valuesArray) {
		Arrays.fill(valuesArray, newEditValue);
	}
	
	/**
	 * Retrieve new values and return the array.
	 */
	protected Object[] primGetValues() {
		IPropertySource[] propSources = parent.getPropertySources();	// Get parent sources to apply against
		Object[] newValues = new Object[propSources.length];
		for (int i = 0; i < propSources.length; i++) {
			if (fDescriptors[i] instanceof ISourcedPropertyDescriptor) {
				// It is a source descriptor, so ask it for the values.
				ISourcedPropertyDescriptor d = (ISourcedPropertyDescriptor) fDescriptors[i];
				newValues[i] = d.getValue(propSources[i]);
			} else {
				// Else it is standard
				Object id = fDescriptors[i].getId();		
				newValues[i] = propSources[i].getPropertyValue(id);
			}
		}
		return newValues;
	}
	
	/**
	 * Reset all of the values to thier sources.
	 * This will fill in a compound command for all of the reset values, then
	 * add this to a FowardUndoCompoundCommand and tell the parent it has changed,
	 * and let it add in its updates. Finally unwrap and execute since 90% of the
	 * time it will be only one command.
	 */
	protected boolean primResetPropertyValues() {
		CompoundCommand cmd = new CompoundCommand();
		
		IPropertySource[] propSources = parent.getPropertySources();	// Get parent sources to apply against
		String resetString = sResetFormat.format(new Object[] {fDescriptors[0].getDisplayName()});		
		boolean changed = false;		
		for (int i = 0; i < propSources.length; i++) {
			Object id = fDescriptors[i].getId();			
			boolean isSet;
			if (fDescriptors[i] instanceof ISourcedPropertyDescriptor)
				isSet = ((ISourcedPropertyDescriptor) fDescriptors[i]).isSet(propSources[i]);
			else
				isSet = propSources[i].isPropertySet(id);
			if (isSet) {
				changed = true;				
				if (fDescriptors[i] instanceof ICommandPropertyDescriptor) {
					// It's a command descriptor. Let the descriptor handle it.
					ICommandPropertyDescriptor d = (ICommandPropertyDescriptor) fDescriptors[i];
					cmd.append(d.resetValue(propSources[i]));
				} else {
					// It's standard.
					cmd.append(createRestorePropertyCommand(propSources[i], resetString, fDescriptors[i].getId()));
				}
			}
		}

		// If it didn't change then there are no commands to add, nor is there any parent
		// that needs notification.
		if (changed) {
			ForwardUndoCompoundCommand fwdcmd = new ForwardUndoCompoundCommand(resetString);		
			fwdcmd.append(cmd.unwrap());
			((CommandStackPropertySheetEntry) parent).childChanged(fwdcmd);
			fStack.execute(fwdcmd.unwrap());
		}
		return changed;
	}
	
	protected Command createRestorePropertyCommand(
		IPropertySource propertySource,
		String resetString,
		Object descriptorID) {
		RestoreDefaultPropertyValueCommand rsetCommand;
		rsetCommand = new RestoreDefaultPropertyValueCommand(resetString);
		rsetCommand.setTarget(propertySource);
		rsetCommand.setPropertyId(descriptorID);
		return rsetCommand;
	}		
}

