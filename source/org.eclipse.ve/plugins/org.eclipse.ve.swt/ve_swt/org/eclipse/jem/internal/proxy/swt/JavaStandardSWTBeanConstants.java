package org.eclipse.jem.internal.proxy.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */


import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

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
	
	environmentBeanTypeProxy = typeFactory.getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment"); //$NON-NLS-1$
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
		displayProxy = environmentBeanTypeProxy.getMethodProxy("getDisplay").invokeCatchThrowableExceptions(null);
	return displayProxy;
}

public static Object invokeSyncExec(ProxyFactoryRegistry registry, DisplayManager.DisplayRunnable runnable) throws ThrowableProxy {
	JavaStandardSWTBeanConstants constants = getConstants(registry);
	return DisplayManager.syncExec(constants.getDisplayProxy(), runnable);
}

public static Object invokeSyncExecCatchThrowableExceptions(ProxyFactoryRegistry registry, DisplayManager.DisplayRunnable runnable) {
	try {
		return invokeSyncExec(registry, runnable);
	} catch (ThrowableProxy e) {
		JavaVEPlugin.getPlugin().getLogger().log(e);
		return null;
	}
}
}
