/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: SWTLabelCreationPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-22 16:24:10 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.LabelCreationPolicy;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class SWTLabelCreationPolicy extends LabelCreationPolicy {
	
public Command getCommand(Command aCommand, final EditDomain domain, final CreateRequest aCreateRequest){
	
	Command setLabelCommand = new CommandWrapper(){
		
		protected boolean prepare() {
			return true;
		}		
		
		public void execute(){
			// SWT objects shoudn't be subclassed, so we don't need to check for an existing value.
			Object newObject = aCreateRequest.getNewObject();
			// Thew new label will be "Label".  This is held externally
			// The key to use is LabelPolicy.text.xxx where xxx is a piece of inializationData
			String labelString = SWTMessages.LabelPolicy_text_Label;
			EObject refNewObject = (EObject)newObject;
			ResourceSet resourceSet = refNewObject.eResource().getResourceSet();
			IJavaInstance newLabel = BeanUtilities.createJavaObject("java.lang.String" , resourceSet , "\"" + labelString + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			EStructuralFeature sf_label = refNewObject.eClass().getEStructuralFeature(fPropertyKey); //$NON-NLS-1$
			RuledCommandBuilder cb = new RuledCommandBuilder(domain);
			cb.applyAttributeSetting((EObject) newObject, sf_label, newLabel);
			command = cb.getCommand();
			command.execute();
		}
	};
	
	if ( aCommand instanceof CompoundCommand ) {
		((CompoundCommand)aCommand).append(setLabelCommand);
		return aCommand;
	} else {
		CompoundCommand result = new CompoundCommand();
		result.append(aCommand);
		result.append(setLabelCommand);
		return result;
	}
}

}
