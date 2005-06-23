/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTLinkCreationPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-23 01:48:08 $ 
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
import org.eclipse.ve.internal.java.core.LabelCreationPolicy;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class SWTLinkCreationPolicy extends LabelCreationPolicy {
	public Command getCommand(Command aCommand, final EditDomain domain, final CreateRequest aCreateRequest){
		
		Command setLinkCommand = new CommandWrapper(){
			
			protected boolean prepare() {
				return true;
			}		
			
			public void execute(){
				// SWT objects shoudn't be subclassed, so we don't need to check for an existing value.
				Object newObject = aCreateRequest.getNewObject();
				// Thew new link will be "Link".  This is held externally
				// The key to use is LinkPolicy.text.xxx where xxx is a piece of inializationData
				String linkString = SWTMessages.LinkPolicy_text_Link; 
				EObject refNewObject = (EObject)newObject;
				ResourceSet resourceSet = refNewObject.eResource().getResourceSet();
				IJavaInstance newLink = BeanUtilities.createString(resourceSet , linkString);
				EStructuralFeature sf_link = refNewObject.eClass().getEStructuralFeature("text"); //$NON-NLS-1$
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting((EObject) newObject, sf_link, newLink);
				command = cb.getCommand();
				command.execute();
			}
		};
		
		if ( aCommand instanceof CompoundCommand ) {
			((CompoundCommand)aCommand).append(setLinkCommand);
			return aCommand;
		} else {
			CompoundCommand result = new CompoundCommand();
			result.append(aCommand);
			result.append(setLinkCommand);
			return result;
		}
	}
}
