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
 *  $RCSfile: CancelVisualConstraintCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */



import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Delete the visual constraint. This will work whether there is a visual constraint or not.
 */
public class CancelVisualConstraintCommand extends CommandWrapper {
	protected KeyedValueHolder fTarget;
	
public CancelVisualConstraintCommand() {
}

public CancelVisualConstraintCommand(String desc) {
	super(desc);
}
public void setTarget(KeyedValueHolder target) {
	fTarget = target;
}
protected boolean prepare() {
	return fTarget != null;
}

public void execute() {
	CommandBuilder cbld = new CommandBuilder(getLabel());
	cbld.cancelKeyedAttributeSetting(fTarget, CDMModelConstants.VISUAL_CONSTRAINT_KEY);
	command = cbld.getCommand();
	command.execute();
}

}
