/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: PropertyDescriptorAdapterFactory.java,v $
 *  $Revision: 1.8 $  $Date: 2006-02-21 17:16:32 $ 
 */

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EModelElement;
import org.osgi.framework.Bundle;

import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation;

/**
 * Adapter factory for PropertyDescriptor adapters.
 * This will use the PropertyDescriptorInformation decorator on the Feature to
 * determine what adapter to use, and if none found, use DecoratedPropertyDescriptorAdapter.
 * The process is:
 * 1) Use PropertyDescriptorInformation
 * 2) If none, then see if there is a decorator of a type in the extension list, if there
 *    is, then use that adapter specified on the extension point. This allows extensions
 *    to override the default for all features of a specific type. The type is the java.lang.Class
 *    of the Decorator itself, not the type of the feature.
 * 3) Else use the default DecoratedPropertyDescriptionAdapter.
 */
public class PropertyDescriptorAdapterFactory extends AdapterFactoryImpl {
	/**
	 * Inner class to point to adapters by type.
	 */
	private static class AdapterType {
		protected Class fDecoratorType;
		protected String fDecoratorTypeName;
		protected Bundle decoratingTypeBundle;
		protected String fFullDecoratorTypeName;
		protected Bundle declaringBundle;
		public IConfigurationElement fConfigElement;

		public AdapterType(String typeName, IConfigurationElement config) {
			// Strip out just the classname portion.
			int slashNdx = typeName.indexOf('/');
			int colonNdx = typeName.indexOf(':', slashNdx + 1);
			fDecoratorTypeName = typeName.substring(slashNdx + 1, colonNdx == -1 ? typeName.length() : colonNdx);
			fFullDecoratorTypeName = typeName;
			fConfigElement = config;
			declaringBundle = Platform.getBundle(config.getDeclaringExtension().getContributor().getName());

			String decTypePlugin = typeName.substring(0, slashNdx == -1 ? 0 : slashNdx);
			if (decTypePlugin.length() == 0)
				decoratingTypeBundle = declaringBundle;
			else {
				decoratingTypeBundle = Platform.getBundle(decTypePlugin);
				if (decoratingTypeBundle == null)
					fDecoratorTypeName = null; // The plugin doesn't exist, so don't allow matches ever.
			}
		}

		/**
		 * Because this is built from the configs, the plugins pointed
		 * to in the type names may not yet exist. We don't want to force
		 * initialization of plugins until absolutely needed. So if we 
		 * haven't yet retrieved the fDecoratorType yet, we will test to
		 * see if the plugin has been activated yet. If it has, we will
		 * load the class and retry the test.
		 */
		public boolean matches(Object instance) {
			if (fDecoratorTypeName == null)
				return false; // We deactivated this entry, can't find the class to compare against.

			if (fDecoratorType != null)
				return fDecoratorType.isInstance(instance);

			// Type not yet loaded, see if the plugin is now active, if it is load the class
			if (decoratingTypeBundle.getState() == Bundle.ACTIVE) {
				// Load the class now to compare against.
				try {
					fDecoratorType = CDEPlugin.getClassFromString(declaringBundle, fFullDecoratorTypeName);
					return fDecoratorType.isInstance(instance);
				} catch (ClassNotFoundException e) {
					fDecoratorTypeName = null; // Set it to null, we can never match against it, so don't try.
				}
			}
			return false;
		}
	}

	protected AdapterType[] fTypes;

	public PropertyDescriptorAdapterFactory() {
		// Build the adapter type cross-ref list.
		IExtensionPoint point =
			Platform.getExtensionRegistry().getExtensionPoint(CDEPlugin.getPlugin().getPluginID(), CDEPlugin.ADAPTER_BY_TYPE_ID);
		if (point == null) {
			fTypes = new AdapterType[0];
			return;
		}

		IConfigurationElement[] elements = point.getConfigurationElements();
		ArrayList types = new ArrayList(elements.length);
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getName().equals(CDEPlugin.ADAPTER_ELEMENT)) {
				String typeName = elements[i].getAttributeAsIs(CDEPlugin.DECORATOR_TYPE_CLASS);
				types.add(new AdapterType(typeName, elements[i]));
			}
		}

		fTypes = (AdapterType[]) types.toArray(new AdapterType[types.size()]);
	}
	/**
	 * Adapt us based upon the decorator.
	 * Otherwise use a default adapter
	 */
	public Adapter createAdapter(Notifier target) {
		PropertyDescriptorInformation decor = findDecorator((EModelElement) target);
		if (decor != null && decor.isAdapter() && decor.getPropertyDescriptorClassname() != null) {
			String cname = decor.getPropertyDescriptorClassname();
			try {
				return (Adapter) CDEPlugin.createInstance(null, cname);
			} catch (Exception e) {
				String msg = MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { cname }); 
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
			}
		}

		// Walk the extension list to see if we have a decorator of that type. It is assumed the list is
		// small so a linear lookup is used.
		Iterator itr = ((EModelElement) target).getEAnnotations().iterator();
		while (itr.hasNext()) {
			Object next = itr.next();
			for (int i = 0; i < fTypes.length; i++) {
				if (fTypes[i].matches(next))
					try {
						return (Adapter) fTypes[i].fConfigElement.createExecutableExtension(CDEPlugin.ADAPTER_CLASS);
					} catch (CoreException e) {
						String msg =
							MessageFormat.format(
								CDEMessages.Object_noinstantiate_EXC_, 
								new Object[] { fTypes[i].fConfigElement.getAttributeAsIs(CDEPlugin.ADAPTER_CLASS)});
						CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
					}
			}
		}

		return new DecoratedPropertyDescriptorAdapter(); // Can't find one, so do default.
	}

	protected PropertyDescriptorInformation findDecorator(EModelElement model) {
		Iterator itr = model.getEAnnotations().iterator();
		while (itr.hasNext()) {
			Object next = itr.next();
			if (next instanceof PropertyDescriptorInformation)
				return (PropertyDescriptorInformation) next;
		}
		return null;
	}
	public boolean isFactoryForType(Object type) {
		return AbstractPropertyDescriptorAdapter.IPROPERTYDESCRIPTOR_TYPE == type;
	}

}
