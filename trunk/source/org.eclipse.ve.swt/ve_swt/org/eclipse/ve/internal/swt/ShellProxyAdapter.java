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
/*
 * $RCSfile: ShellProxyAdapter.java,v $ $Revision: 1.16 $ $Date: 2005-02-23 23:19:40 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * Proxy adapter for SWT Shell
 * 
 * @since 1.0.0
 */
public class ShellProxyAdapter extends CompositeProxyAdapter {

	public ShellProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#beanProxyAllocation(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation)
			throws AllocationException {
		try {
			Object result = invokeSyncExec(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy)
						throws ThrowableProxy, RunnableException {
					try {
						IBeanProxy shellProxy = beanProxyAdapterBeanProxyAllocation(allocation);
						finishAllocation(displayProxy, shellProxy);
						return shellProxy;
					} catch (AllocationException e) {
						throw new RunnableException(e);
					}
				}
			});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (AllocationException) e.getCause(); // Because we only put
			// allocation exceptions
			// into the cause.
		}
	}
	
	protected IBeanProxy basicInitializationStringAllocation(final String aString, final IBeanTypeProxy targetClass) throws IAllocationProcesser.AllocationException {
		try {
			Object result = invokeSyncExec(new DisplayManager.DisplayRunnable() {
				public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					try {
						IBeanProxy shellProxy = ShellProxyAdapter.super.beanProxyAdapterInitializationStringAllocation(aString, targetClass);
						finishAllocation(displayProxy, shellProxy);
						return shellProxy;
					} catch (AllocationException e) {
						throw new RunnableException(e);
					}
				}
			});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (AllocationException) e.getCause();
		}
	}
	

	protected void primInstantiateBeanProxy() {
		super.primInstantiateBeanProxy();
		if (isBeanProxyInstantiated()) {

			IJavaObjectInstance shell = getJavaObject();
			final EReference sf = JavaInstantiation.getReference(shell,
					SWTConstants.SF_DECORATIONS_TEXT);
			if (!shell.eIsSet(sf)) {
				Display.getDefault().asyncExec(new Runnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						ResourceSet rset = JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain());
						AnnotationLinkagePolicy policy = getBeanProxyDomain().getEditDomain().getAnnotationLinkagePolicy();
						Annotation ann = policy.getAnnotation(getJavaObject());
						String name = null;
						if (ann != null) {
							name = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
						}	
						final IJavaInstance titleInstance = BeanUtilities.createString(rset, name);
						getModelChangeController().doModelChanges(new Runnable() {
							public void run() {
								RuledCommandBuilder cbld = new RuledCommandBuilder(getBeanProxyDomain().getEditDomain());
								cbld.applyAttributeSetting((EObject) target,sf, titleInstance);
								cbld.getCommand().execute();
							}
						}, true);
					}
				});
			}
		}
	}

	/**
	 * Finish the allocation (i.e. put it in right place).
	 * @param displayProxy
	 * @param shellProxy
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.2
	 */
	protected void finishAllocation(IBeanProxy displayProxy, IBeanProxy shellProxy) throws ThrowableProxy {
		// Position the shell off screen for the moment
		// TODO this needs to be done properly so that the
		// location can be set in the model and ignored
		// likewise for the visibility
		Point offscreen = BeanSWTUtilities
				.getOffScreenLocation();
		IIntegerBeanProxy intXBeanProxy = displayProxy
				.getProxyFactoryRegistry()
				.getBeanProxyFactory().createBeanProxyWith(
						offscreen.x);
		IIntegerBeanProxy intYBeanProxy = displayProxy
				.getProxyFactoryRegistry()
				.getBeanProxyFactory().createBeanProxyWith(
						offscreen.y);
		IMethodProxy setlocationMethodProxy = shellProxy
				.getTypeProxy()
				.getMethodProxy(
						"setLocation", new String[] { "int", "int" }); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
		setlocationMethodProxy
				.invoke(shellProxy, new IBeanProxy[] {
						intXBeanProxy, intYBeanProxy });

		IMethodProxy openMethodProxy = shellProxy
				.getTypeProxy().getMethodProxy("open"); //$NON-NLS-1$
		openMethodProxy.invoke(shellProxy);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#setupBeanProxy(org.eclipse.jem.internal.proxy.core.IBeanProxy)
	 */
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		super.setupBeanProxy(beanProxy);
		IMethodProxy addShellListenerMethodProxy = 	fControlManager
					.fControlManagerProxy
					.getTypeProxy()
					.getMethodProxy(
						"addShellListener", new String[] { "org.eclipse.swt.widgets.Shell" }); //$NON-NLS-1$  //$NON-NLS-2$
		if(addShellListenerMethodProxy != null) {
			addShellListenerMethodProxy.invokeCatchThrowableExceptions(
					fControlManager.fControlManagerProxy,
					new IBeanProxy[] { beanProxy });
		}
	}

}
