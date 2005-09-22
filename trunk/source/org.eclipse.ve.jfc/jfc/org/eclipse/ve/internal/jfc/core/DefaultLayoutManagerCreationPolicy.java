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
 *  $RCSfile: DefaultLayoutManagerCreationPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-09-22 12:55:58 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.CDECreationTool.CreationPolicy;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
 

public class DefaultLayoutManagerCreationPolicy implements CreationPolicy , IExecutableExtension {

	private String typeName;

	public Command getPostCreateCommand(Command aCommand, final EditDomain domain, final CreateRequest createRequest) {
		Command setLayoutCommand = new CommandWrapper(){
			protected boolean prepare() {
				return true;
			}
			public void execute() {
				IJavaObjectInstance model = (IJavaObjectInstance) createRequest.getNewObject();	
				// Only change the layout for composite explicitly.  Do not do for subclasses as this is how
				// custom widgets are created
				if(model.getJavaType().getQualifiedName().equals(typeName)){ 
					// Get the type of layout to set
					String defaultLayoutTypeName = JFCVisualPlugin.getPlugin().getPluginPreferences().getString(JFCVisualPlugin.DEFAULT_LAYOUTMANAGER);
					if(defaultLayoutTypeName != null && defaultLayoutTypeName.length() >0){	
						RuledCommandBuilder bldr = new RuledCommandBuilder(domain);
						IJavaInstance layoutInstance = BeanUtilities.createJavaObject(defaultLayoutTypeName,model.eResource().getResourceSet(),(String)null);
						EStructuralFeature layout_SF = model.eClass().getEStructuralFeature("layout");
						bldr.applyAttributeSetting(model,layout_SF,layoutInstance);
						bldr.getCommand().execute();
					}
				}
			}
		};
		CompoundCommand result = new CompoundCommand("Set layout manager to default");
		result.append(aCommand);
		result.append(setLayoutCommand);
		return result.unwrap();
	}

	public Command getPreCreateCommand(EditDomain domain, CreateRequest createRequest) {
		return null;
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		// The type name must be explicitly set to avoid inheritance between Container and subclasses that aren't containers
		// (such as JButton, etc...)
		typeName = (String) data;		
	}

}
