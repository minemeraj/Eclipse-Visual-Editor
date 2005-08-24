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
 *  $RCSfile: RuledRestoreDefaultPropertyCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

/**
 * Restore Default Property command that works with the Property Rule.
 */
public class RuledRestoreDefaultPropertyCommand extends AbstractCommand {
	protected EditDomain domain;
	protected Object propertyId;
	protected Object undoValue;
	protected boolean originallySet;
	protected IPropertySource target;
	protected Command postSetCmd;

	public RuledRestoreDefaultPropertyCommand(EditDomain domain, IPropertySource target, Object propertyId) {
		this(null, domain, target, propertyId);
	}
	
	public RuledRestoreDefaultPropertyCommand(String label, EditDomain domain, IPropertySource target, Object propertyId) {
		super(label);
		this.domain = domain;
		this.target = target;
		this.propertyId = propertyId;
	}

	public boolean prepare() {
		return true;
	}

	public void execute() {
		originallySet = target.isPropertySet(propertyId);
		if (originallySet) {
			undoValue = target.getPropertyValue(propertyId);
			// Since getPropertyValue turns things into IPropertySources, we
			// need to unwrapper it so that the actual value can be applied back.		
			if (undoValue instanceof IPropertySource)
				undoValue = ((IPropertySource) undoValue).getEditableValue();
			target.resetPropertyValue(propertyId);
			if (target.getEditableValue() instanceof EObject && undoValue instanceof EObject) {
				postSetCmd = ((IPropertyRule) domain.getRuleRegistry().getRule(IPropertyRule.RULE_ID)).postSet(domain, (EObject) undoValue);
				postSetCmd.execute();
			}
		} else
			undoValue = null;

	}

	public void redo() {
		target.resetPropertyValue(propertyId);
		if (postSetCmd != null)
			postSetCmd.redo();
	}

	public void undo() {
		if (originallySet) {
			if (postSetCmd != null)
				postSetCmd.undo();
			target.setPropertyValue(propertyId, undoValue);
		} else
			target.resetPropertyValue(propertyId);
	}

}
