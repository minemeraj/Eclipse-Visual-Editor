package org.eclipse.ve.internal.cde.commands;
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
 *  $RCSfile: CancelVisualConstraintCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
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
