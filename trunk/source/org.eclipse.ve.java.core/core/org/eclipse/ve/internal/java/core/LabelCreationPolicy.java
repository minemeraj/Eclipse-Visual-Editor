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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: LabelCreationPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:23:54 $ 
 */

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFCreationTool;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
import org.eclipse.jem.internal.proxy.core.*;

public class LabelCreationPolicy implements EMFCreationTool.CreationPolicy , IExecutableExtension {
	
	protected String fLabelKey = "Label"; //$NON-NLS-1$
	protected String fPropertyKey = "text"; //$NON-NLS-1$
	
public Command getCommand(Command aCommand, final EditDomain domain, final CreateRequest aCreateRequest){
	
	Command setLabelCommand = new CommandWrapper(){
		
		protected boolean prepare() {
			return true;
		}		
		
		public void execute(){
			// Query the live label to see whether there is one or not
			Object newObject = aCreateRequest.getNewObject();
			IBeanProxy labelProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance)newObject);
			// Check to see whether the type exists in case the label failed to be instantaited correctly
			if ( labelProxy.getTypeProxy() != null ) {
				IMethodProxy getLabelMethodProxy = labelProxy.getTypeProxy().getMethodProxy("getText"); //$NON-NLS-1$
				IBeanProxy existingLabelProxy = getLabelMethodProxy.invokeCatchThrowableExceptions(labelProxy);
				// Create a new label string if there is no label proxy, or if there is one for an empty string
				if ( existingLabelProxy == null || ((IStringBeanProxy)existingLabelProxy).stringValue().trim().equals("")) { //$NON-NLS-1$
					// Thew new label will be "Label".  This is held externally
					// The key to use is LabelPolicy.text.xxx where xxx is a piece of inializationData
					String labelString = JavaMessages.getString("LabelPolicy.text." + fLabelKey); //$NON-NLS-1$
					EObject refNewObject = (EObject)newObject;
					ResourceSet resourceSet = refNewObject.eResource().getResourceSet();
					IJavaInstance newLabel = BeanUtilities.createJavaObject("java.lang.String" , resourceSet , "\"" + labelString + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					EStructuralFeature sf_label = refNewObject.eClass().getEStructuralFeature(fPropertyKey); //$NON-NLS-1$
					RuledCommandBuilder cb = new RuledCommandBuilder(domain);
					cb.applyAttributeSetting((EObject) newObject, sf_label, newLabel);
					command = cb.getCommand();
					command.execute();
				}
			}
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
public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
	String s = (String)initData;
	int index = s.indexOf(","); //$NON-NLS-1$
	if (index >= 0) {
		fLabelKey = s.substring(0, index);
		fPropertyKey = s.substring(index+1);
	} else {
		fLabelKey = s;
	}
}
    /**
     * @see CreationPolicy#getDefaultSuperString(EClass)
     */
    public String getDefaultSuperString(EClass superClass) {
        return null;
    }

}
