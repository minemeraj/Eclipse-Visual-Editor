package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanCellRenderer.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-19 12:20:47 $ 
 */

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;
import org.eclipse.jem.internal.proxy.core.*;

public class BeanCellRenderer extends LabelProvider implements IExecutableExtension, INeedData, ISourced {

	protected static Image BEAN_IMAGE;
	protected String fJavaBeansPropertyEditorClassName;
	protected PropertyEditorBeanProxyWrapper fPropertyEditorProxyWrapper;
	protected EditDomain editDomain;
	protected IJavaObjectInstance source;
	protected boolean rebuildWrapper = true;

	/**
	 * This is called when this is on the BasePropertyDecorator for a type or if none is specified
	 * at all. If none specified, then just toBeanString will be used. It will later be
	 * called with the property editor class name in the initialization data. This is when
	 * there is no specific label provider or property editor on the feature.
	 */
	public BeanCellRenderer() {
	}

	public BeanCellRenderer(String aPropertyEditorClassName) {
		fJavaBeansPropertyEditorClassName = aPropertyEditorClassName;
	}
	/**
	 * By default use the toString() method on the bean
	 */
	public String getText(Object element) {
		if (element == null)
			return ""; //$NON-NLS-1$
		if (!(element instanceof IJavaInstance))
			return element.toString();

		IBeanProxy elementProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, JavaEditDomainHelper.getResourceSet(editDomain));
		if (elementProxy == null)
			return ""; //$NON-NLS-1$
	
		calculateJavaBeanPropertyEditor();
		
		if (fPropertyEditorProxyWrapper == null) {			
			// The property editor class not given, or could not be instantiate
			// Look for a label provider on the class of object itself
			if ( element instanceof IJavaObjectInstance && editDomain != null) {
				IJavaObjectInstance javaComponent = (IJavaObjectInstance)element;
				ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(javaComponent.getJavaType());
				if ( labelProvider != null ) {
					return labelProvider.getText(javaComponent);
				}
			} 
			return elementProxy.toBeanString();
		} 
		// The PropertyEditorBeanProxyWrapper wraps the java.beans.PropertyEditor on the remote VM
		// Use its setValue(Object) and getAsText() API to get a string		
		fPropertyEditorProxyWrapper.setValue(elementProxy);
		// For debug purposes puts a * in front of the string so we can tell it came from a java property editor
		return fPropertyEditorProxyWrapper.getAsText();
	}
	
	
	protected void calculateJavaBeanPropertyEditor() {
		if (fPropertyEditorProxyWrapper != null && !rebuildWrapper)
			return;	// We got one and no rebuild requested.

		rebuildWrapper = false;
		// We should of been given a property editor class name, if not, we don't set one up.
		if (fJavaBeansPropertyEditorClassName != null) {
			IConstructorProxy ctor = null;
			IBeanTypeProxy fPropertyEditorTypeProxy =
				JavaEditDomainHelper.getBeanProxyDomain(editDomain).getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					fJavaBeansPropertyEditorClassName);
			try {
				// if there is an ISourced object, see if the property editor has a ctor that takes an object and pass it in.
				if (source != null) {
					ctor = fPropertyEditorTypeProxy.getConstructorProxy(new String[] { "java.lang.Object" }); //$NON-NLS-1$
					if (ctor != null) {
						// got a ctor? let's instantiate the property editor
						fPropertyEditorProxyWrapper =
							new PropertyEditorBeanProxyWrapper(
								ctor.newInstance(new IBeanProxy[] { BeanProxyUtilities.getBeanProxy(source, JavaEditDomainHelper.getResourceSet(editDomain))}));
					} else {
						// just instantiate with a null ctor
						fPropertyEditorProxyWrapper =
							new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
					}
				} else {
					// no ISourced object... just instantiate with a null ctor
					fPropertyEditorProxyWrapper =
						new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
				}
			} catch (ThrowableProxy exc) {
				// The ctor that takes an object may have failed because we gave it an object it didn't expect,
				// so let's try with the default ctor. But don't try again if the default ctor is the one that threw the exception.
				if (ctor != null) {
					try {
						fPropertyEditorProxyWrapper =
							new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
					} catch (ThrowableProxy e) {
						// Still failed, so log it.
						JavaVEPlugin.log(e);
					}
				} else {
					// It failed and we had used the default ctor, so log it.
					JavaVEPlugin.log(exc);
				}
					
			}
		}
	}

	
	
	/**
	 * This will only expect initData to be a string that contains the qualified name
	 * of the java.beans.PropertyEditor class that is used to edit the feature
	 */
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		if (initData instanceof String) {
			fJavaBeansPropertyEditorClassName = ((String) initData).trim();
		}
	}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}
	
	public void setSources(Object[] sources, IPropertySource[] pos, IPropertyDescriptor[] des) {
		if (sources[0] instanceof IJavaObjectInstance) {
			rebuildWrapper = !(sources[0].equals(source));
			source = (IJavaObjectInstance) sources[0];
		}
	}	

}