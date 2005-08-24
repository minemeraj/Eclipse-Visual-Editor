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
 * $RCSfile: BeanCellRenderer.java,v $ $Revision: 1.9 $ $Date: 2005-08-24 23:30:46 $
 */

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;

/**
 * Standard Cell Renderer (Label Provider) to give up labels for features that use a standard java.beans.PropertyEditor to get the text (label).
 * <p>
 * BeanCellRender's are cached in the BeanPropertyDescriptor. Since this is system wide, we need to cache any proxy registry info in the registry
 * constants of the current registry at time of rendering.
 * 
 * @since 1.1.0
 */
public class BeanCellRenderer extends LabelProvider implements IExecutableExtension, INeedData, ISourced {

	protected static Image BEAN_IMAGE;

	protected String fJavaBeansPropertyEditorClassName;

	protected EditDomain editDomain;

	protected boolean rebuildWrapper = true;

	/**
	 * This is called when this is on the BasePropertyDecorator for a type or if none is specified at all. If none specified, then just toBeanString
	 * will be used. It will later be called with the property editor class name in the initialization data. This is when there is no specific label
	 * provider or property editor on the feature.
	 */
	public BeanCellRenderer() {
	}

	/**
	 * Construct with a property editor class name. It is the java beans property editor. It will run on the proxy registry.
	 * 
	 * @param aPropertyEditorClassName
	 * 
	 * @since 1.1.0
	 */
	public BeanCellRenderer(String aPropertyEditorClassName) {
		fJavaBeansPropertyEditorClassName = aPropertyEditorClassName;
	}

	public String getText(Object element) {
		if (element == null)
			return ""; //$NON-NLS-1$
		if (!(element instanceof IJavaInstance))
			return element.toString();

		IBeanProxy elementProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, JavaEditDomainHelper.getResourceSet(editDomain));
		if (elementProxy == null)
			return ""; //$NON-NLS-1$

		PropertyEditorBeanProxyWrapper proxyWrapper = calculateJavaBeanPropertyEditor(elementProxy.getProxyFactoryRegistry());

		if (proxyWrapper == null) {
			// The property editor class not given, or could not be instantiate
			// Look for a label provider on the class of object itself
			if (element instanceof IJavaObjectInstance && editDomain != null) {
				IJavaObjectInstance javaComponent = (IJavaObjectInstance) element;
				ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(javaComponent.getJavaType());
				if (labelProvider != null) {
					if (labelProvider instanceof ISourced)
						((ISourced) labelProvider).setSources(sources, pos, des);
					return labelProvider.getText(javaComponent);
				}
			}
			return elementProxy.toBeanString();
		}
		// The PropertyEditorBeanProxyWrapper wraps the java.beans.PropertyEditor on the remote VM
		// Use its setValue(Object) and getAsText() API to get a string
		proxyWrapper.setValue(elementProxy);
		return proxyWrapper.getAsText();
	}

	/**
	 * Get/create the property editor wrapper for the given registry.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected PropertyEditorBeanProxyWrapper calculateJavaBeanPropertyEditor(ProxyFactoryRegistry registry) {
		if (fJavaBeansPropertyEditorClassName == null)
			return null;

		PropertyEditorBeanProxyWrapper wrapper = (PropertyEditorBeanProxyWrapper) registry.getConstants(this);
		if (!rebuildWrapper)
			return wrapper; // no rebuild requested.

		rebuildWrapper = false;
		registry.deregisterConstants(this);
		if (wrapper != null)
			wrapper.dispose();
		// We should of been given a property editor class name, if not, we don't set one up.
		if (fJavaBeansPropertyEditorClassName != null) {
			IConstructorProxy ctor = null;
			IBeanTypeProxy fPropertyEditorTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(fJavaBeansPropertyEditorClassName);
			try {
				// if there is an ISourced object, see if the property editor has a ctor that takes an object and pass it in.
				if (sourceIsValid && sources[0] != null) {
					ctor = fPropertyEditorTypeProxy.getConstructorProxy(new String[] { "java.lang.Object"}); //$NON-NLS-1$
					if (ctor != null) {
						// got a ctor? let's instantiate the property editor
						wrapper = new PropertyEditorBeanProxyWrapper(ctor.newInstance(new IBeanProxy[] { BeanProxyUtilities.getBeanProxy(
								(IJavaInstance) sources[0], JavaEditDomainHelper.getResourceSet(editDomain))}));
					} else {
						// just instantiate with a null ctor
						wrapper = new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
					}
				} else {
					// no ISourced object... just instantiate with a null ctor
					wrapper = new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
				}
			} catch (ThrowableProxy exc) {
				// The ctor that takes an object may have failed because we gave it an object it didn't expect,
				// so let's try with the default ctor. But don't try again if the default ctor is the one that threw the exception.
				if (ctor != null) {
					try {
						wrapper = new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
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
		registry.registerConstants(this, wrapper);
		return wrapper;
	}

	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		// This will only expect initData to be a string that contains the qualified name
		// of the java.beans.PropertyEditor class that is used to edit the feature
		if (initData instanceof String) {
			fJavaBeansPropertyEditorClassName = ((String) initData).trim();
		}
	}

	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

	private Object[] sources;

	private IPropertySource[] pos;

	private IPropertyDescriptor[] des;

	private boolean sourceIsValid;

	public void setSources(Object[] sources, IPropertySource[] pos, IPropertyDescriptor[] des) {
		this.pos = pos;
		this.des = des;
		if (sources[0] instanceof IJavaObjectInstance) {
			sourceIsValid = true;
			rebuildWrapper = this.sources == null || !(this.sources[0].equals(sources[0]));
		} else {
			sourceIsValid = false;
			rebuildWrapper = true;
		}
		this.sources = sources;
	}

}
