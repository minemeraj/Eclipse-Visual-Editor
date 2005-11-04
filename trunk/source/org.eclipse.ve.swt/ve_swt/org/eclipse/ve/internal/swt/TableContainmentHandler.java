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
 *  $RCSfile: TableContainmentHandler.java,v $
 *  $Revision: 1.2 $  $Date: 2005-11-04 17:30:52 $ 
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

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Containment handler for SWT Table.
 * 
 * @since 1.2.0
 */
public class TableContainmentHandler extends WidgetContainmentHandler {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public TableContainmentHandler(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation,
			final EditDomain domain) throws StopRequestException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
		if (creation && child instanceof IJavaObjectInstance) {
			final IJavaObjectInstance jo = (IJavaObjectInstance) child;
			final EStructuralFeature sf_headerVisible = jo.eClass().getEStructuralFeature("headerVisible");
			final EStructuralFeature sf_linesVisible = jo.eClass().getEStructuralFeature("linesVisible");
			if (!jo.eIsSet(sf_headerVisible) || !jo.eIsSet(sf_linesVisible)) {
				preCmds.append(new CommandWrapper() {

					protected boolean prepare() {
						return true;
					}

					public void execute() {
						CommandBuilder cb = new CommandBuilder();
						if (!jo.eIsSet(sf_headerVisible)) {
							IJavaInstance booleanObj = BeanUtilities.createJavaObject((JavaHelpers) sf_headerVisible.getEType(), JavaEditDomainHelper
									.getResourceSet(domain), "true"); //$NON-NLS-1$
							cb.applyAttributeSetting(jo, sf_headerVisible, booleanObj);
						}
						if (!jo.eIsSet(sf_linesVisible)) {
							IJavaInstance booleanObj = BeanUtilities.createJavaObject((JavaHelpers) sf_linesVisible.getEType(), JavaEditDomainHelper
									.getResourceSet(domain), "true"); //$NON-NLS-1$
							cb.applyAttributeSetting(jo, sf_linesVisible, booleanObj);
						}
						command = cb.getCommand();
						if (command != null)
							command.execute();
					}
				});
			}
		}
		return child;
	}

}
