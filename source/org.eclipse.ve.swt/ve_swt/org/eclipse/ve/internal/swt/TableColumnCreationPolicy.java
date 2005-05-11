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
 *  $RCSfile: TableColumnCreationPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-11 22:41:37 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFCreationTool.CreationPolicy;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class TableColumnCreationPolicy implements CreationPolicy {

	protected String fWidth = "width"; //$NON-NLS-1$
	protected String fDefaultWidth = "60"; //$NON-NLS-1$
	protected JavaHelpers fIntHelper; // The appropriate classifer for int.

	public Command getCommand(Command aCommand, final EditDomain domain, final CreateRequest createRequest) {
		Command setTableColumnSettingCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				IJavaObjectInstance model = (IJavaObjectInstance) createRequest.getNewObject();
				fIntHelper = JavaRefFactory.eINSTANCE.reflectType("int", JavaEditDomainHelper.getResourceSet(domain)); //$NON-NLS-1$
				IJavaInstance intObj = BeanUtilities.createJavaObject(fIntHelper, JavaEditDomainHelper.getResourceSet(domain), fDefaultWidth);
				EStructuralFeature sf_headerVisible = model.eClass().getEStructuralFeature(fWidth);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting(model, sf_headerVisible, intObj);
				cb.getCommand().execute();

			}
		};
		CompoundCommand result = new CompoundCommand(aCommand.getLabel());
		result.append(setTableColumnSettingCommand);
		result.append(aCommand);
		return result;
	}

	public String getDefaultSuperString(EClass superClass) {
		return null;
	}

}
