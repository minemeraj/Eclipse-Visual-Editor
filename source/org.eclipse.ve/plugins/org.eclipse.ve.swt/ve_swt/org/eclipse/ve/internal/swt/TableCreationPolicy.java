/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
 *  $Revision: 1.6 $  $Date: 2005-09-21 10:39:14 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.CDECreationTool.CreationPolicy;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * Creation policy for SWT Table.
 * <p>
 * It, by default, sets header visible and lines visible. Unless those features are not already set.
 * 
 * @since 1.1.0
 */
public class TableCreationPolicy implements CreationPolicy {

	protected static final String fHeaderVisible = "headerVisible"; //$NON-NLS-1$
	protected static final String fLinesVisible = "linesVisible"; //$NON-NLS-1$

	public Command getPostCreateCommand(Command aCommand, final EditDomain domain, final CreateRequest createRequest) {
		Command setTableSettingsCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				IJavaObjectInstance model = (IJavaObjectInstance) createRequest.getNewObject();
				EStructuralFeature sf_headerVisible = model.eClass().getEStructuralFeature(fHeaderVisible);
				EStructuralFeature sf_linesVisible = model.eClass().getEStructuralFeature(fLinesVisible);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				if (!model.eIsSet(sf_headerVisible)) {
					IJavaInstance booleanObj = BeanUtilities.createJavaObject((JavaHelpers) sf_headerVisible.getEType(), JavaEditDomainHelper.getResourceSet(domain), "true"); //$NON-NLS-1$
					cb.applyAttributeSetting(model, sf_headerVisible, booleanObj);
				}
				if (!model.eIsSet(sf_linesVisible)) {
					IJavaInstance booleanObj = BeanUtilities.createJavaObject((JavaHelpers) sf_linesVisible.getEType(), JavaEditDomainHelper.getResourceSet(domain), "true"); //$NON-NLS-1$
					cb.applyAttributeSetting(model, sf_linesVisible, booleanObj);
				}
				command = cb.getCommand();
				if (command != null)
					command.execute();

			}
		};
		CompoundCommand result = new CompoundCommand(aCommand.getLabel());
		result.append(setTableSettingsCommand);
		result.append(aCommand);
		return result;
	}

	public Command getPreCreateCommand(EditDomain domain, CreateRequest createRequest) {
		return null;
	}

}
