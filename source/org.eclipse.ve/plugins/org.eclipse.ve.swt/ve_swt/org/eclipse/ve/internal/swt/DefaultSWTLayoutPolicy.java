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
 *  $RCSfile: DefaultSWTLayoutPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-06 17:14:41 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanUtilities;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
 
/**
 * A static helper to help with dropping a Composite with a default layout from the preferences.
 * 
 * @since 1.2.0
 */
public class DefaultSWTLayoutPolicy {
	private DefaultSWTLayoutPolicy() {
		
	}
	
	/**
	 *  This handles setting the default layout according the preferences. This MUST only be called on
	 * creation and BEFORE the child has been added to the model. Otherwise the layout will not
	 * be located correctly. 
	 * @param domain
	 * @param model
	 * @param defaultLayout layout classname to use instead of the default. Use <code>null</code> to use the default from preferences.
	 * @since 1.2.0
	 */
	public static Command processDefaultLayout(final EditDomain domain, final IJavaInstance model, final String defaultLayout) {
		return new CommandWrapper(){
			protected boolean prepare() {
				return true;
			}
			public void execute() {
				EStructuralFeature layout_SF = model.eClass().getEStructuralFeature("layout"); //$NON-NLS-1$
				if (!model.eIsSet(layout_SF)) {
					// Get the type of layout to set
					String defaultLayoutTypeName = defaultLayout == null ? SwtPlugin.getDefault().getPluginPreferences().getString(SwtPlugin.DEFAULT_LAYOUT) : defaultLayout;
					if(defaultLayoutTypeName != null && defaultLayoutTypeName.length()>0 && !defaultLayoutTypeName.equals(SwtPlugin.NULL_LAYOUT)){	
						CommandBuilder bldr = new CommandBuilder();
						IJavaInstance layoutInstance = BeanUtilities.createJavaObject(defaultLayoutTypeName, EMFEditDomainHelper.getResourceSet(domain), (String)null);
						bldr.applyAttributeSetting(model,layout_SF,layoutInstance);
						command = bldr.getCommand();
						command.execute();
					}
				}
			}
		};
	}
}
