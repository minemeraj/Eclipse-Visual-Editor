/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.internal.proxy.swt;


import java.util.logging.Level;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;

import org.eclipse.ve.internal.swt.SwtPlugin;

/**
 * Standard SWT Constants
 *
 * Use the static accessor method (getConstants()) to get the appropriate 
 * constants instance for the registry. This is done so that until the
 * the constants are needed, they aren't registered.
 *
 * NOTE: Since everything in here just proxies, there is
 *       no need to have them separated by VM. That is why
 *       this in the Proxy package.
 *
 * This is final because this can't be extended. This specific one
 * will be registered with the factory for this key. Any extensions
 * must be done in their own constants and registry key.
 *
 */
public final class JavaStandardSWTBeanConstants {
		
	public static final String REGISTRY_KEY = "STANDARDPROXYSWTCONSTANTS:"; //$NON-NLS-1$
			
	final IBeanTypeProxy environmentBeanTypeProxy;
	IBeanProxy displayProxy;
	final IFieldProxy pointXProxy;
	final IFieldProxy pointYProxy;
	final IFieldProxy rectangleHeightProxy;
	final IFieldProxy rectangleWidthProxy;
	final IFieldProxy rectangleXProxy;
	final IFieldProxy rectangleYProxy;
/**
 * Get the constants instance for the specified registry.
 */
public static JavaStandardSWTBeanConstants getConstants(ProxyFactoryRegistry registry) {
	JavaStandardSWTBeanConstants constants = (JavaStandardSWTBeanConstants) registry.getConstants(REGISTRY_KEY);
	if (constants == null)
		registry.registerConstants(REGISTRY_KEY, constants = new JavaStandardSWTBeanConstants(registry, registry.getBeanProxyFactoryExtension(IStandardSWTBeanProxyFactory.REGISTRY_KEY) != null));
	return constants;
}
		

public JavaStandardSWTBeanConstants(ProxyFactoryRegistry registry, boolean isRegistered) {
	super();
	
	IStandardBeanTypeProxyFactory typeFactory = registry.getBeanTypeProxyFactory();
	
	IBeanTypeProxy pointTypeProxy = typeFactory.getBeanTypeProxy("org.eclipse.swt.graphics.Point");//$NON-NLS-1$
		
	pointXProxy = pointTypeProxy.getFieldProxy("x");//$NON-NLS-1$
	pointYProxy = pointTypeProxy.getFieldProxy("y");//$NON-NLS-1$
	
	IBeanTypeProxy rectangleTypeProxy = typeFactory.getBeanTypeProxy("org.eclipse.swt.graphics.Rectangle");//$NON-NLS-1$
	rectangleHeightProxy = rectangleTypeProxy.getFieldProxy("height");//$NON-NLS-1$
	rectangleWidthProxy = rectangleTypeProxy.getFieldProxy("width");//$NON-NLS-1$
	rectangleXProxy = rectangleTypeProxy.getFieldProxy("x");//$NON-NLS-1$
	rectangleYProxy = rectangleTypeProxy.getFieldProxy("y");//$NON-NLS-1$
	
	environmentBeanTypeProxy = typeFactory.getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.Environment"); //$NON-NLS-1$
}
/**
 * getPointXFieldProxy method comment.
 */
public IFieldProxy getPointXFieldProxy() {
	return pointXProxy;
}
/**
 * getPointYFieldProxy method comment.
 */
public IFieldProxy getPointYFieldProxy() {
	return pointYProxy;
}
/**
 * getRectangleHeightFieldProxy method comment.
 */
public IFieldProxy getRectangleHeightFieldProxy() {
	return rectangleHeightProxy;
}
/**
 * getRectangleWidthFieldProxy method comment.
 */
public IFieldProxy getRectangleWidthFieldProxy() {
	return rectangleWidthProxy;
}
/**
 * getRectangleXFieldProxy method comment.
 */
public IFieldProxy getRectangleXFieldProxy() {
	return rectangleXProxy;
}
/**
 * getRectangleYFieldProxy method comment.
 */
public IFieldProxy getRectangleYFieldProxy() {
	return rectangleYProxy;
}

public IBeanTypeProxy getEnvironmentBeanTypeProxy(){
	return environmentBeanTypeProxy;
}

public IBeanProxy getDisplayProxy() {
	if (displayProxy == null)
		displayProxy = environmentBeanTypeProxy.getMethodProxy("getDisplay").invokeCatchThrowableExceptions(null); //$NON-NLS-1$
	return displayProxy;
}

/**
 * Invoke the runnable on the display thread on the given display. It will not return until completed. It will use
 * the display associated with the vm that was started for this editor. There is a default one created for this.
 * 
 * @param registry the proxy registry to identify the vm to talk to.
 * @param runnable the runnable to execute
 * @return the result, it will be either a IBeanProxy, IBeanProxy[], or <code>null</code>.
 * @throws ThrowableProxy if a remote vm error occurred.
 * @throws RunnableException if either a RuntimeException, or another specifically caught exception had occurred on this side.
 * 
 * @since 1.0.0
 */
public static Object invokeSyncExec(ProxyFactoryRegistry registry, DisplayManager.DisplayRunnable runnable) throws ThrowableProxy, RunnableException {
	JavaStandardSWTBeanConstants constants = getConstants(registry);
	return DisplayManager.syncExec(constants.getDisplayProxy(), runnable);
}

/**
 * Invoke the runnable on the display thread on the given display. It will not return until completed. It will use
 * the display associated with the vm that was started for this editor. There is a default one created for this.
 * <p>
 * This one will catch and log all exceptions, either on this side or the VM side. It will return <code>null</code> in this case.
 * 
 * @param registry the proxy registry to identify the vm to talk to.
 * @param runnable the runnable to execute
 * @return the result, an <code>IBeanProxy</code>, <code>null</code>. If there were any exceptions, <code>null</code> will also be returned.
 * 
 * @since 1.0.0
 */
public static Object invokeSyncExecCatchThrowableExceptions(ProxyFactoryRegistry registry, DisplayManager.DisplayRunnable runnable) {
	try {
		return invokeSyncExec(registry, runnable);
	} catch (ThrowableProxy e) {
		SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
	} catch (DisplayManager.DisplayRunnable.RunnableException e) {
		SwtPlugin.getDefault().getLogger().log(e.getCause(), Level.WARNING);
	}
	return null;
}
}
