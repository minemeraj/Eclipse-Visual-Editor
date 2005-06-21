package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DefaultLabelProviderWithNameAndAttribute.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-21 22:53:48 $ 
 */
import java.text.MessageFormat;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;
/**
 * This is label provider that decorates the default label with the
 * value of an attribute. The name of the attribute is passed in either
 * as a setter or as configuration data. The attribute must result in a string
 * or a an instance of java.lang.String (in Java Model, not JRE).
 */
public class DefaultLabelProviderWithNameAndAttribute extends DefaultJavaBeanLabelProvider implements IExecutableExtension {
	
	protected String attributeName = ""; //$NON-NLS-1$
	
	public DefaultLabelProviderWithNameAndAttribute(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public DefaultLabelProviderWithNameAndAttribute() {
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @see IExecutableExtension#setInitializationData(IConfigurationElement, String, Object)
	 * 
	 * It is expected that the data be a string, and it is the attribute name.
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data instanceof String)
			attributeName = (String) data; 
	}

	/**
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) {
		if (element instanceof EObject) {
			String mainPart = super.getText(element);
			EObject ref = (EObject) element;
			EClass meta = ref.eClass();
			EStructuralFeature sf = meta.getEStructuralFeature(attributeName);
			if (sf != null) {
				Object setting = ref.eGet(sf);
				if (setting instanceof IJavaInstance)
					setting = BeanProxyUtilities.getBeanProxy((IJavaInstance) setting, JavaEditDomainHelper.getResourceSet(domain));
				if (setting instanceof IStringBeanProxy)
					setting = ((IStringBeanProxy) setting).stringValue();
				if (setting instanceof String) {
					String label = (String) setting;
					if (label.length() > 20)
						label = MessageFormat.format(JavaMessages.DefaultLabelProvider_Label_DottedVersion, new Object[] {label.substring(0, 20)});	// Too large, so truncate it. //$NON-NLS-1$
					return MessageFormat.format(JavaMessages.DefaultLabelProvider_Label_FullVersion, new Object[] {mainPart, label}); 
				}
			}
			return mainPart;
		} else
			return super.getText(element);	
	}

	/**
	 * @see IBaseLabelProvider#isLabelProperty(Object, String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return attributeName.equals(property) || super.isLabelProperty(element, property);
	}

}