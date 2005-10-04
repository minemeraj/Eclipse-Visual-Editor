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
 *  $RCSfile: TableTreeColumnContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 15:41:47 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Containment Handler for a SWT TableColumn
 * 
 * @since 1.2.0
 */
public class TableTreeColumnContainmentHandler extends WidgetContainmentHandler {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public TableTreeColumnContainmentHandler(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation,
			final EditDomain domain) throws NoAddException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
		if (creation && child instanceof IJavaObjectInstance) {
			final IJavaObjectInstance jo = (IJavaObjectInstance) child;
			final EStructuralFeature sf_defaultWidth = jo.eClass().getEStructuralFeature("width");
			if (!jo.eIsSet(sf_defaultWidth)) {

				preCmds.append(new CommandWrapper() {

					protected boolean prepare() {
						return true;
					}

					public void execute() {
						IJavaInstance intObj = BeanUtilities.createJavaObject((JavaHelpers) sf_defaultWidth.getEType(), JavaEditDomainHelper
								.getResourceSet(domain), "60");
						RuledCommandBuilder cb = new RuledCommandBuilder(domain);
						cb.applyAttributeSetting(jo, sf_defaultWidth, intObj);
						command = cb.getCommand();
						command.execute();
					}
				});
			}
		}
		return child;
	}

}
