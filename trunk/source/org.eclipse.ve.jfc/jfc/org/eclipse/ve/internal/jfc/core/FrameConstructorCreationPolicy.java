package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: FrameConstructorCreationPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.beaninfo.MethodProxy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFCreationTool;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.Method;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class FrameConstructorCreationPolicy implements EMFCreationTool.CreationPolicy {
		
public Command getCommand(Command aCommand, EditDomain domain, final CreateRequest aCreateRequest){
	
	Command setInitStringCommand = new AbstractCommand(){
		public void execute(){
			JavaClass javaClass = (JavaClass)aCreateRequest.getNewObjectType();
			// Look at the constructors to see whether there is a null constructor or not
			// If so then just leasve the initString blank
			// Remember that the absence of any contructors means that there is an implicit null constructor
			Iterator behaviors = javaClass.getEOperations().iterator();
			boolean hasNullConstructor = false;
			while ( behaviors.hasNext() ) {
				Method javaMethod = ((MethodProxy)behaviors.next()).getMethod();
				// If we have a constructor then flag this
				if (javaMethod.isConstructor()){
					List methodInputParms = javaMethod.getParameters();
					// See whether we have a null constructor
					if (methodInputParms.isEmpty()){
						hasNullConstructor = true;
						break;
					} else if ( methodInputParms.size() == 1 ) {
						// See whether or not there is a constructor that takes a frame argument						
						Object inputParm = methodInputParms.iterator().next();
						inputParm.toString();
					}
				}
			}
			// If we have a null constructor then leave the initializationString alone and do nothing
			if (hasNullConstructor) return;
			// Create the frame argument constructor
			// For some reason the behaviors does not return anything other than set methods - it has no constructors
			// so right now always do this
			IJavaObjectInstance newJavaObject = (IJavaObjectInstance)aCreateRequest.getNewObject();
			// For a dialog or window set the initializationString to be new java.awt.Dialog(new java.awt.Frame());
			String qualifiedClassName = ((JavaClass)newJavaObject.getJavaType()).getQualifiedNameForReflection();			
			String initString = "new " + qualifiedClassName + "(new java.awt.Frame())"; //$NON-NLS-1$ //$NON-NLS-2$
			newJavaObject.setInitializationString(initString);
		}
		// We don't really care about redo and undo for now
		public void redo(){
		}
		public boolean canExecute(){
		    return true;
		}
	};
	
	// Put the command to set the initString BEFORE the other commands, so that by the time the BeanProxyAdapter
	// is called the correct string is in place
	CompoundCommand result = new CompoundCommand(aCommand.getLabel());
	result.append(setInitStringCommand);
	result.append(aCommand);
	return result;
}
    /**
     * @see CreationPolicy#getDefaultSuperString(EClass)
     */
    public String getDefaultSuperString(EClass superClass) {    
        // Look for a null constructor up the stack, if there is one,
        // return super(), else return Frame() constructor
        Iterator behaviors = superClass.getEOperations().iterator();
        boolean hasNullConstructor = false;
        boolean hasAnyConstructors = false ;
        while ( behaviors.hasNext() ) {
            Method javaMethod = ((MethodProxy)behaviors.next()).getMethod();
            // If we have a constructor then flag this
            if (javaMethod.isConstructor()){  
                hasAnyConstructors = true ;              
                List methodInputParms = javaMethod.getParameters();
                // See whether we have a null constructor
                if (methodInputParms==null || methodInputParms.isEmpty()){
                    hasNullConstructor = true;
                    break;
                }
            }
        }
        if (hasNullConstructor)
           return ("super()") ; //$NON-NLS-1$
        else { // No null constructor
            if (!hasAnyConstructors  &&
                ((JavaClass) superClass).getSupertype() != null) 
               return getDefaultSuperString(((JavaClass) superClass).getSupertype()) ;
            else
               return ("super(new java.awt.Frame())") ;  //$NON-NLS-1$
        }
                      
    }

}
