/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTCLabelCreationPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-22 16:24:10 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;


public class SWTCLabelCreationPolicy extends SWTLabelCreationPolicy {
	public Command getCommand(Command aCommand, final EditDomain domain, final CreateRequest aCreateRequest){
		
		Command setCLabelCommand = new CommandWrapper(){
			
			protected boolean prepare() {
				return true;
			}		
			
			public void execute(){
				// SWT objects shoudn't be subclassed, so we don't need to check for an existing value.
				Object newObject = aCreateRequest.getNewObject();
				// Thew new clabel will be "CLabel".  This is held externally
				// The key to use is CLabelPolicy.text.xxx where xxx is a piece of inializationData
				String cLabelString = SWTMessages.CLabelPolicy_text_CLabel; 
				EObject refNewObject = (EObject)newObject;
				ResourceSet resourceSet = refNewObject.eResource().getResourceSet();
				IJavaInstance newCLabel = BeanUtilities.createJavaObject("java.lang.String" , resourceSet , "\"" + cLabelString + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				EStructuralFeature sf_clabel = refNewObject.eClass().getEStructuralFeature("text"); //$NON-NLS-1$
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting((EObject) newObject, sf_clabel, newCLabel);
				command = cb.getCommand();
				command.execute();
			}
		};
		
		if ( aCommand instanceof CompoundCommand ) {
			((CompoundCommand)aCommand).append(setCLabelCommand);
			return aCommand;
		} else {
			CompoundCommand result = new CompoundCommand();
			result.append(aCommand);
			result.append(setCLabelCommand);
			return result;
		}
	}
}
