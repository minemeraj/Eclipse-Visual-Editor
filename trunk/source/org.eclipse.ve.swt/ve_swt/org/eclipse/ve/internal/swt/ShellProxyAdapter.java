/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: ShellProxyAdapter.java,v $ $Revision: 1.10 $ $Date: 2004-09-03 22:12:34 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanTypeProxy;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
import org.eclipse.jem.internal.proxy.core.IMethodProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.core.IModelChangeController;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.swt.NullLayoutPolicyHelper.NullConstraint;

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
						IBeanTypeProxy listenerType = displayProxy
								.getProxyFactoryRegistry()
								.getBeanTypeProxyFactory()
								.getBeanTypeProxy(
										"org.eclipse.ve.internal.swt.targetvm.PreventShellCloseMinimizeListener"); //$NON-NLS-1$
						if (listenerType != null) {
							IBeanProxy shellListenerBean = listenerType
									.newInstance();
							IMethodProxy addShellListenerMethodProxy = shellProxy
									.getTypeProxy()
									.getMethodProxy(
											"addShellListener", new String[] { "org.eclipse.swt.events.ShellListener" }); //$NON-NLS-1$  //$NON-NLS-2$
							if (shellListenerBean != null
									&& addShellListenerMethodProxy != null) {
								addShellListenerMethodProxy.invoke(shellProxy,
										new IBeanProxy[] { shellListenerBean });
							}
						}

						IMethodProxy openMethodProxy = shellProxy
								.getTypeProxy().getMethodProxy("open"); //$NON-NLS-1$
						openMethodProxy.invoke(shellProxy);
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
						IModelChangeController controller = (IModelChangeController) getBeanProxyDomain()
								.getEditDomain()
								.getData(
										IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
						ResourceSet rset = JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain());
						AnnotationLinkagePolicy policy = getBeanProxyDomain().getEditDomain().getAnnotationLinkagePolicy();
						Annotation ann = policy.getAnnotation(getJavaObject());
						String name = null;
						if (ann != null) {
							name = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
						}	
						final IJavaInstance titleInstance = BeanUtilities.createString(rset, name);
						controller.run(new Runnable() {
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
}
