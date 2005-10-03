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
package org.eclipse.ve.internal.java.core;
/*
 * $RCSfile: EnumeratedLabelProvider.java,v $ $Revision: 1.11 $ $Date: 2005-10-03 19:20:57 $
 */
import java.util.logging.Level;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * Label Provider for Enumerated Java Beans. This uses the enumerated values that are retrieved from the PropertyDescriptor
 * as an array.
 * 
 * @since 1.0.0
 */
public class EnumeratedLabelProvider extends org.eclipse.jface.viewers.LabelProvider implements INeedData {
	protected String[] fDisplayNames;
	protected String[] fInitStrings;
	protected JavaHelpers fFeatureType;
	protected EditDomain editDomain;
	
	/*
	 * Get the enumerated values out of the registry constants for the current registry associated with the
	 * current edit domain. These values are stored with actual label provider as the key since each label provider
	 * needs an unique set of enumeration values. They are stored in the registry because each label provider instance is
	 * cached and shared for all editors within a project. But the data needs to be per reqistry. Also, since stored as
	 * registry constants, these will be automatically cleaned up when the registry is closed.
	 */
	protected IBeanProxy[] getEnumeratedValues() {
		ProxyFactoryRegistry registry = JavaEditDomainHelper.getBeanProxyDomain(editDomain).getProxyFactoryRegistry();
		IBeanProxy[] enumeratedValues = (IBeanProxy[]) registry.getConstants(this);
		if (enumeratedValues == null) {
			IBeanTypeProxy aBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(
					fFeatureType.getQualifiedNameForReflection());
			enumeratedValues = new IBeanProxy[fInitStrings.length];
			int index = 0;
			try {
				for (int i = 0; i < fInitStrings.length; i++) {
					index = i;
					enumeratedValues[i] = aBeanTypeProxy.newInstance(fInitStrings[i]);
				}
			} catch (ThrowableProxy exc) {
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
					JavaVEPlugin.log("Unable to create enumeration value for " + fInitStrings[index], Level.WARNING); //$NON-NLS-1$
					JavaVEPlugin.log(exc, Level.WARNING);
				}
			} catch (InstantiationException exc) {
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
					JavaVEPlugin.log("Unable to create enumeration value for " + fInitStrings[index], Level.WARNING); //$NON-NLS-1$
					JavaVEPlugin.log(exc, Level.WARNING);
				}
			}
			registry.registerConstants(this, enumeratedValues);
		}
		return enumeratedValues;
	}
	
	public EnumeratedLabelProvider(Object[] aBeanInfoValuesArray, JavaHelpers aFeatureType) {
		fFeatureType = aFeatureType;
		// Iterate over the array of values, these are stored in the format
		// displayName, object, initString
		// We only care about storing the displayName and the initStrings
		int length = aBeanInfoValuesArray.length;
		int j = 0;
		fDisplayNames = new String[length / 3];
		fInitStrings = new String[length / 3];
		for (int i = 0; i < length; i += 3) {
			fDisplayNames[j] = (String) aBeanInfoValuesArray[i];
			fInitStrings[j] = (String) aBeanInfoValuesArray[i+2];
			// NOTE - We cannot in any way use the object that came in from the values array
			// This came from the VM that did introspection that is NOT the same as the
			// one the editor is necessarily running in so we must re-create the
			// bean proxies from the init string each time
			j++;
		}
	}
	
	public String getText(Object aJavaInstance) {
		IBeanProxy[] enumeratedValues = getEnumeratedValues();
		if (enumeratedValues == null)
			return JavaMessages.LabelProvider_Enumerated_getText_ERROR_; 
		IBeanProxy valueProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) aJavaInstance, JavaEditDomainHelper
				.getResourceSet(editDomain));
		// Now that we have an array of bean proxies compare the value against the entries
		for (int i = 0; i < enumeratedValues.length; i++) {
			if (enumeratedValues[i] == null) {
				if (valueProxy == null) {
					return fDisplayNames[i];
				}
			} else if (enumeratedValues[i].equals(valueProxy)) {
				return fDisplayNames[i];
			}
		}
		// TODO This needs more thought"); //$NON-NLS-1$
		return JavaMessages.LabelProvider_Enumerated_getText_ERROR_; 
	}
	
	/*
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}
}
