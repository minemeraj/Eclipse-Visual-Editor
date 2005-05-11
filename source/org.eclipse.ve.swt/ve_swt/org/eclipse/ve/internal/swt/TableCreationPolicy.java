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
 *  $RCSfile: TableCreationPolicy.java,v $
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

public class TableCreationPolicy implements CreationPolicy {

	protected String fHeaderVisible = "headerVisible"; //$NON-NLS-1$
	protected String fLinesVisible = "linesVisible"; //$NON-NLS-1$
	protected JavaHelpers fBooleanHelper; // The appropriate classifer for the kind of boolean.

	public Command getCommand(Command aCommand, final EditDomain domain, final CreateRequest createRequest) {
		Command setTableSettingsCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				IJavaObjectInstance model = (IJavaObjectInstance) createRequest.getNewObject();
				fBooleanHelper = JavaRefFactory.eINSTANCE.reflectType("boolean", JavaEditDomainHelper.getResourceSet(domain)); //$NON-NLS-1$
				IJavaInstance booleanObj = BeanUtilities.createJavaObject(fBooleanHelper, JavaEditDomainHelper.getResourceSet(domain), "true"); //$NON-NLS-1$
				EStructuralFeature sf_headerVisible = model.eClass().getEStructuralFeature(fHeaderVisible);
				EStructuralFeature sf_linesVisible = model.eClass().getEStructuralFeature(fLinesVisible);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting(model, sf_headerVisible, booleanObj);
				booleanObj = BeanUtilities.createJavaObject(fBooleanHelper, JavaEditDomainHelper.getResourceSet(domain), "true"); //$NON-NLS-1$
				cb.applyAttributeSetting(model, sf_linesVisible, booleanObj);
				cb.getCommand().execute();

			}
		};
		CompoundCommand result = new CompoundCommand(aCommand.getLabel());
		result.append(setTableSettingsCommand);
		result.append(aCommand);
		return result;
	}

	public String getDefaultSuperString(EClass superClass) {
		return null;
	}

}
