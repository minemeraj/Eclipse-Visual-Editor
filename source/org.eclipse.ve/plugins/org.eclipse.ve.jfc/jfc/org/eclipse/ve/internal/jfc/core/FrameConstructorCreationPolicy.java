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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: FrameConstructorCreationPolicy.java,v $
 *  $Revision: 1.11 $  $Date: 2005-09-21 10:39:45 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.PTClassInstanceCreation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.VECreationPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class FrameConstructorCreationPolicy extends VECreationPolicy {

	public Command getPostCreateCommand(Command aCommand, final EditDomain domain, final CreateRequest aCreateRequest) {

		IJavaObjectInstance newJavaObject = (IJavaObjectInstance) aCreateRequest.getNewObject();
		if (newJavaObject.getAllocation() != null)
			return aCommand;	// There is already an allocation. We don't want to touch it.
		
		Command setAllocationCommand = new CommandWrapper() {
			
			/* (non-Javadoc)
			 * @see org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper#execute()
			 */
			public void execute() {
				// TODO This is really bad code. We need to understand and create the
				// init method as 
				//   getDialog(Frame parent) or getDialog(Dialog parent) {
				//     dialog1 = new Dialog(parent);
				//
				JavaClass javaClass = (JavaClass) aCreateRequest.getNewObjectType();
				JavaClass frameClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("java.awt.Frame", javaClass); //$NON-NLS-1$
				// Look at the constructors to see whether there is a null constructor or not
				// If so then just leasve the allocation alone.
				// Remember that the absence of any contructors means that there is an implicit null constructor
				Iterator methods = javaClass.getMethods().iterator();
				boolean hasNullConstructor = false;
				while (methods.hasNext()) {
					Method javaMethod = (Method) methods.next();
					// If we have a constructor then flag this
					if (javaMethod.isConstructor() && JavaVisibilityKind.PUBLIC_LITERAL == javaMethod.getJavaVisibility()) {
						List methodInputParms = javaMethod.getParameters();
						// See whether we have a null constructor
						if (methodInputParms.isEmpty()) {
							hasNullConstructor = true;
							break;
						} else if (methodInputParms.size() == 1) {
							// See whether or not there is a constructor that takes a frame argument						
							JavaParameter inputParm = (JavaParameter) methodInputParms.iterator().next();
							JavaHelpers type = inputParm.getJavaType(); 
							if (type.isAssignableFrom(frameClass)) {
								break;	// Found out we have one that will work.
							}
						}
					}
				}
				// If we have a null constructor then leave the initializationString alone and do nothing
				if (hasNullConstructor)
					return;
				// Create the frame argument constructor
				PTClassInstanceCreation newFrame = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation("java.awt.Frame", null); //$NON-NLS-1$
				PTClassInstanceCreation newExpr = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(javaClass.getQualifiedNameForReflection(), Collections.singletonList(newFrame));
				IJavaObjectInstance newJavaObject = (IJavaObjectInstance) aCreateRequest.getNewObject();
				ApplyAttributeSettingCommand applycommand = new ApplyAttributeSettingCommand();
				applycommand.setTarget(newJavaObject);
				applycommand.setAttribute(JavaInstantiation.getAllocationFeature(newJavaObject));
				applycommand.setAttributeSettingValue(InstantiationFactory.eINSTANCE.createParseTreeAllocation(newExpr));
				command = applycommand;
				command.execute();
			}
			
			
			/* (non-Javadoc)
			 * @see org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand#canExecute()
			 */
			public boolean canExecute() {
				return true;	// Override super because we don't want to go through the effort of creating the command everytime we move. Just when we execute.
			}

		};

		// Put the command to set the initString BEFORE the other commands, so that by the time the BeanProxyAdapter
		// is called the correct string is in place
		CompoundCommand result = new CompoundCommand(aCommand.getLabel());
		result.append(setAllocationCommand);
		result.append(aCommand);
		return result;
	}
	
	
	public String getDefaultSuperString(EClass superClass) {
		if (superClass instanceof JavaClass) {
			// Look for a null constructor up the stack, if there is one,
			// return super(), else return Frame() constructor
			Iterator methods = ((JavaClass) superClass).getMethods().iterator();
			boolean hasNullConstructor = false;
			boolean hasAnyConstructors = false;
			while (methods.hasNext()) {
				Method javaMethod = (Method) methods.next();
				// If we have a constructor then flag this. Need to check for visiblity of the constructor.
				// Assuming not in same package so not checking for package protected.
				JavaVisibilityKind visibility = javaMethod.getJavaVisibility();
				if (javaMethod.isConstructor() && (visibility == JavaVisibilityKind.PROTECTED_LITERAL || visibility == JavaVisibilityKind.PUBLIC_LITERAL)) {
					hasAnyConstructors = true;
					List methodInputParms = javaMethod.getParameters();
					// See whether we have a null constructor
					if (methodInputParms.isEmpty()) {
						hasNullConstructor = true;
						break;
					}
				}
			}
			if (!hasNullConstructor) {
				// No null constructor
				// TODO This isn't really right. You can't go to the super.super and look at its CTOR's. You
				// can't bypass the first super. If there are no ctors at all, then just use super().
				// If there are any, then see if there is one that takes a frame.
				if (!hasAnyConstructors && ((JavaClass) superClass).getSupertype() != null)
					return getDefaultSuperString(((JavaClass) superClass).getSupertype());
				else
					return ("super(new java.awt.Frame())"); //$NON-NLS-1$
			}
		}
		return ("super()"); //$NON-NLS-1$
	}


	public Command getPreCreateCommand(EditDomain domain, CreateRequest createRequest) {
		return null;
	}

}
