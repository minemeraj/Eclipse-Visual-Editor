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
 *  $RCSfile: RuledPropertySetCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

/**
 * Property Set Command that works with the Property Rule.
 */
public class RuledPropertySetCommand extends AbstractCommand {
	protected EditDomain domain;
	protected Object propertyValue;
	protected Object propertyId;
	protected Object undoValue;
	protected boolean originallyNotSet;
	protected IPropertySource target;
	protected Command presetCmd, postsetCmd;

	public RuledPropertySetCommand(EditDomain domain, IPropertySource target, Object propertyId, Object propertyValue) {
		this(null, domain, target, propertyId, propertyValue);
	}
	
	public RuledPropertySetCommand(String label, EditDomain domain, IPropertySource target, Object propertyId, Object propertyValue) {
		super(label);
		this.domain = domain;
		this.target = target;
		this.propertyId = propertyId;
		this.propertyValue = propertyValue;
	}

	public boolean prepare() {
		originallyNotSet = !target.isPropertySet(propertyId);		
		if (!originallyNotSet) {
			undoValue = target.getPropertyValue(propertyId);
			// Since getPropertyValue turns things into IPropertySources, we
			// need to unwrapper it so that the actual value can be applied back.		
			if (undoValue instanceof IPropertySource)
				undoValue = ((IPropertySource) undoValue).getEditableValue();
		} else
			undoValue = null;
		
		// Only worry about preset/postset if target is an EObject.
		if (target.getEditableValue() instanceof EObject) {
			// See if should set up a pre set command.
			// If this is a touch (i.e. same (identity) value as before and value was previously set), then no pre set
			// Can't be touch if originally unset because then it is not set and this is a new value that needs scoping.			
			if ((propertyValue == null || propertyValue instanceof EObject) && (originallyNotSet || undoValue != propertyValue)) {
				// It is not a touch.
				presetCmd =
					((IPropertyRule) domain.getRuleRegistry().getRule(IPropertyRule.RULE_ID)).preSet(
						domain,
						(EObject) target.getEditableValue(),
						(EObject) propertyValue,
						null);						
			}
			
			if (!originallyNotSet && undoValue instanceof EObject && undoValue != propertyValue) {
				// It is not a touch, and it was set. So need to do postset.
				postsetCmd = ((IPropertyRule) domain.getRuleRegistry().getRule(IPropertyRule.RULE_ID)).postSet(domain, (EObject) undoValue);		
			}			
		}

		return (presetCmd == null || presetCmd.canExecute()) && (postsetCmd == null || postsetCmd.canExecute());
	}

	public void execute() {

		if (presetCmd != null)
			presetCmd.execute();
			
		target.setPropertyValue(propertyId, propertyValue);

		if (postsetCmd != null) 		
			postsetCmd.execute();
	}

	public void redo() {
		if (presetCmd != null)
			presetCmd.redo();

		target.setPropertyValue(propertyId, propertyValue);

		if (postsetCmd != null)
			postsetCmd.redo();
	}

	public void undo() {
		if (postsetCmd != null)
			postsetCmd.undo();

		if (originallyNotSet)
			target.resetPropertyValue(propertyId);
		else
			target.setPropertyValue(propertyId, undoValue);

		if (presetCmd != null)
			presetCmd.undo();
	}

}
