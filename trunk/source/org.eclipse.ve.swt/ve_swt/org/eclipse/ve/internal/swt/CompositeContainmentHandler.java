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
 *  $RCSfile: CompositeContainmentHandler.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-09 15:22:12 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.core.runtime.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
 

/**
 * Containment handler for SWT Composite.
 * <p>
 * It should be used with any standard composite that wants to have a default layout applied to it
 * when it is created. It should have the type itself (fully qualified) sent in as the extension data,
 * otherwise it will not add it. This is because subclasses cannot participate in the default layout.
 * They can't because developers of the subclasses may already have applied their own layout and
 * we shouldn't force in another one.
 * <p>
 * Subclasses that need to supply their own customization of containment handler should call
 * the {@link org.eclipse.ve.internal.swt.DefaultSWTLayoutPolicy} on their own.
 * @since 1.2.0
 */
public class CompositeContainmentHandler extends WidgetContainmentHandler implements IExecutableExtension {

	private String typeForLayoutHandling;
	
	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public CompositeContainmentHandler(Object model) {
		super(model);
	}
	
	/**
	 * Key for a Boolean (KeyedBoolean) annotation on the child to override standard default layout processing.
	 * If this key is added to an instance annotation when created then it will use the boolean value to determine
	 * whether to set the default layout or to leave it not set.
	 *  
	 * @since 1.2.0
	 */
	public static final String DEFAULT_LAYOUT_KEY = "org.eclipse.ve.swt.DefaultLayout"; //$NON-NLS-1$
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
		// Only for creation do we do layout modification. Add assumes layout already handled.
		if (creation && child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			// Only classes that are of the class passed in through the IExtension data will have the default layout added. This
			// is because subclasses may already have their layout specified. No way to know that developer wanted the layout
			// to be supplied by users or the developer supplied the layout.
			//
			// OR the annotation on the child itself has the "DEFAULT_LAYOUT" boolean key. It will then respect that key.
			Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(jo);
			if (annotation != null) {
				try {
					Boolean il = (Boolean) annotation.getKeyedValues().get(DEFAULT_LAYOUT_KEY);
					if (il != null) {
						if (il.booleanValue())
							preCmds.append(DefaultSWTLayoutPolicy.processDefaultLayout(domain, jo, null));
						return child;
					}
				} catch (ClassCastException e) {
					// In case incorrectly declared the key type.
				}				
			} 
			
			if (jo.getJavaType().getQualifiedNameForReflection().equals(typeForLayoutHandling)) {
				preCmds.append(DefaultSWTLayoutPolicy.processDefaultLayout(domain, jo, null));
			}
		}
		return child;
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		try {
			typeForLayoutHandling = (String) data;
		} catch (ClassCastException e) {
		}
	}

}
