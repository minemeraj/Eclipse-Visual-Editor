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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: ModifyAttributesCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

public class ModifyAttributesCommand 
	extends AbstractCommand {

protected EAttribute feature = null;
protected EObject target = null;
protected Object data = null;
protected Object oldData = null;
protected byte operation = 0;

public static byte FEATURE_MODIFY = 11;
public static byte FEATURE_REMOVE = 22;
public static byte FEATURE_ADD = 33;

public ModifyAttributesCommand(String label){
	super(label);
}

public ModifyAttributesCommand(String label, String description){
	super(label, description);
}

public boolean canExecute(){
	if((operation==FEATURE_MODIFY || operation==FEATURE_ADD) && target!=null && feature!=null && data!=null)
		return true;
	if(operation==FEATURE_REMOVE && target!=null && feature!=null)
		return true;
	return false;
}

public void execute(){
	if(operation==FEATURE_REMOVE){
		oldData = target.eGet(feature);
		target.eUnset(feature);
	}
	if(operation==FEATURE_MODIFY){
		oldData = target.eGet(feature);
		target.eSet(feature, data);
	}
	if(operation==FEATURE_ADD){
		target.eSet(feature, data);
	}
}

public void undo(){
	if(operation==FEATURE_REMOVE)
		target.eSet(feature, oldData);
	if(operation==FEATURE_MODIFY)
		target.eSet(feature, oldData);
	if(operation==FEATURE_ADD)
		target.eUnset(feature);
}

public void redo(){
	execute();
}

public void setOperation(byte operation){
	this.operation = operation;
}

public void setTarget(EObject target){
	this.target = target;
}

public void setData(Object data){
	this.data = data;
}

public void setFeature(EAttribute feature){
	this.feature = feature;
}

}
