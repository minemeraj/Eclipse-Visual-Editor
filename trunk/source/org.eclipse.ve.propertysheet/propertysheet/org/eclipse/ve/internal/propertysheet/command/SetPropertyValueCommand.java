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
package org.eclipse.ve.internal.propertysheet.command;
/*
 *  $RCSfile: SetPropertyValueCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */



import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

public class SetPropertyValueCommand
	extends AbstractCommand {
protected Object propertyValue;
protected Object propertyId;
protected Object undoValue;
protected boolean resetOnUndo;
protected IPropertySource target;

public SetPropertyValueCommand(){
}

public SetPropertyValueCommand(String label){
	super(label);
}

public boolean canExecute() {
	return true;
}

public void execute() {
	resetOnUndo = !getTarget().isPropertySet(propertyId);
	if (!resetOnUndo) {
		undoValue = getTarget().getPropertyValue(propertyId);
		// Since getPropertyValue turns things into IPropertySources, we
		// need to unwrapper it so that the actual value can be applied back.		
		if (undoValue instanceof IPropertySource)
			undoValue = ((IPropertySource) undoValue).getEditableValue();		
	} else
		undoValue = null;
	getTarget().setPropertyValue(propertyId, propertyValue);
}

public IPropertySource getTarget() { return target;}

public void setTarget(IPropertySource aTarget) {target = aTarget;}

public void redo() {
	execute();
}
public void setPropertyId(Object pId) {
	propertyId = pId;
}

public void setPropertyValue(Object val) {
	propertyValue = val;
}
public void undo() {
	if (resetOnUndo)
		getTarget().resetPropertyValue(propertyId);
	else
		getTarget().setPropertyValue(propertyId,undoValue);
}

}
