/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.properties;
/*
 *  $RCSfile: PropertySourceAdapterFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-21 21:43:42 $ 
 */

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * Adapter factory for PropertySource adapters.
 *
 * The key for PropertySources is IPropertySource.class
 *
 * Note: This will give the EditDomain to those adapters that implement INeedData.
 * Creation date: (10/28/99 9:27:57 AM)
 * @author: Joe Winchester
 */
public class PropertySourceAdapterFactory extends AdapterFactoryImpl {
	protected Class fDefaultPropertySourceClass;
	protected EditDomain domain;

	public PropertySourceAdapterFactory(EditDomain domain) {
		this.domain = domain;
	}

	public PropertySourceAdapterFactory(EditDomain domain, Class defaultPropertySourceClass) {
		this(domain);
		fDefaultPropertySourceClass = defaultPropertySourceClass;
	}

	/**
	 * Adapt us based upon the decorator. The decorator will be found on the metaObject, or one
	 * of its super types. If none found, then the default class, if any will be used.
	 * Otherwise use a default adapter.
	 *
	 * If the propertySourceAdapterClass value is null for a decorator, the next decorator
	 * will be found and used.
	 */
	public Adapter createAdapter(Notifier target) {
		if (!(target instanceof EObject))
			return null;
		// Can't adapt something that is not an EObject as a PropertySource. It is probably a datatype then, and those aren't property sources. Property sources can have properties, only EObjects can have properties.

		PropertySourceAdapterInformation decr =
			(PropertySourceAdapterInformation) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
				((EObject) target).eClass(),
				PropertySourceAdapterInformation.class,
				DecoratorsPackage.eINSTANCE.getPropertySourceAdapterInformation_PropertySourceAdapterClassname());
		if (decr != null) {
			if (decr.getPropertySourceAdapterClassname() == null)
				return null;	// Explicitly set to null, which means no property source adapter, treat as a direct editable element.
			try {
				Adapter a = (Adapter) CDEPlugin.createInstance(null, decr.getPropertySourceAdapterClassname());
				if (a instanceof INeedData)
					 ((INeedData) a).setData(domain);
				return a;
			} catch (Exception exc) {
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", exc)); //$NON-NLS-1$
			}
		}

		if (fDefaultPropertySourceClass != null) {
			try {
				Adapter a = (Adapter) fDefaultPropertySourceClass.newInstance();
				if (a instanceof INeedData)
					 ((INeedData) a).setData(domain);
				return a;
			} catch (Exception e) {
				String msg =
					MessageFormat.format(
						CDEMessages.Object_noinstantiate_EXC_, 
						new Object[] { fDefaultPropertySourceClass });
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
			}
		}

		return new PropertySourceAdapter(); // Can't find one, so do default.
	}
	/**
	 * @see org.eclipse.emf.common.notify.AdapterFactory#isFactoryForType(Object)
	 */
	public boolean isFactoryForType(Object type) {
		return IPropertySource.class == type;
	}

}
