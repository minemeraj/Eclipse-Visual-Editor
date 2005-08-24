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
 *  $RCSfile: TableColumnCreationPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:55 $ 
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
 * Creation policy for SWT TableColumns.
 * <p>
 * It will by default set default width of the column, if not already set.
 * 
 * @since 1.1.0
 */
public class TableColumnCreationPolicy implements CreationPolicy {

	protected static final String fWidth = "width"; //$NON-NLS-1$

	protected static final String fDefaultWidth = "60"; //$NON-NLS-1$

	public Command getCommand(Command aCommand, final EditDomain domain, final CreateRequest createRequest) {
		Command setTableColumnSettingCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				IJavaObjectInstance model = (IJavaObjectInstance) createRequest.getNewObject();
				EStructuralFeature sf_defaultWidth = model.eClass().getEStructuralFeature(fWidth);
				if (!model.eIsSet(sf_defaultWidth)) {
					IJavaInstance intObj = BeanUtilities.createJavaObject((JavaHelpers) sf_defaultWidth.getEType(), JavaEditDomainHelper
							.getResourceSet(domain), fDefaultWidth);
					RuledCommandBuilder cb = new RuledCommandBuilder(domain);
					cb.applyAttributeSetting(model, sf_defaultWidth, intObj);
					command = cb.getCommand();
					command.execute();
				}
			}
		};
		CompoundCommand result = new CompoundCommand(aCommand.getLabel());
		result.append(setTableColumnSettingCommand);
		result.append(aCommand);
		return result;
	}

}
