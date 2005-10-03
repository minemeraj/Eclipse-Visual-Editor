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
 *  $RCSfile: DefaultJFCLayoutPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-03 19:21:01 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanUtilities;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * A static helper to help with dropping a Panel/JPanel with a default layout from the preferences.
 * 
 * @since 1.2.0
 */
public class DefaultJFCLayoutPolicy {

	private DefaultJFCLayoutPolicy() {

	}

	/**
	 * This handles setting the default layout according the preferences. This MUST only be called on
	 * creation and BEFORE the child has been added to the model. Otherwise the layout will not
	 * be located correctly.
	 * @param domain
	 * @param model
	 * @param defaultLayout layout classname to use instead of the default. Use <code>null</code> to use the default from preferences.
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public static Command processDefaultLayout(final EditDomain domain, final IJavaObjectInstance model, final String defaultLayout) {
		return new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				EStructuralFeature layout_SF = model.eClass().getEStructuralFeature("layout");
				if (!model.eIsSet(layout_SF)) {
					// Get the type of layout to set
					String defaultLayoutTypeName = defaultLayout == null ? JFCVisualPlugin.getPlugin().getPluginPreferences()
							.getString(JFCVisualPlugin.DEFAULT_LAYOUTMANAGER) : defaultLayout;
					if (defaultLayoutTypeName != null && defaultLayoutTypeName.length() > 0) {
						CommandBuilder bldr = new CommandBuilder();
						IJavaInstance layoutInstance = BeanUtilities.createJavaObject(defaultLayoutTypeName, EMFEditDomainHelper.getResourceSet(domain),
								(String) null);
						bldr.applyAttributeSetting(model, layout_SF, layoutInstance);
						command = bldr.getCommand();
						command.execute();
					}
				}
			}
		};
	}
}
