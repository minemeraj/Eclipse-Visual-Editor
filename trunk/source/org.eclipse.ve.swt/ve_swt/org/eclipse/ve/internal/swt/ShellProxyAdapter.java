package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.proxy.core.*;

public class ShellProxyAdapter extends CompositeProxyAdapter {

public ShellProxyAdapter(IBeanProxyDomain domain) {
	super(domain);
}
protected void primInstantiateBeanProxy() {
	try {
		IBeanTypeProxy environmentBeanTypeProxy = getEnvironmentBeanTypeProxy();
		// Old way of doing things relying on the Environment to be the owner of the display thread
		// Get the static method to create the shell
		IBeanProxy displayProxy = environmentBeanTypeProxy.getFieldProxy("display").get(environmentBeanTypeProxy);			
		IBeanTypeProxy shellBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Shell");
		IConstructorProxy createShellProxy = shellBeanTypeProxy.getConstructorProxy(new IBeanTypeProxy[] {displayProxy.getTypeProxy()});
		IBeanProxy shellProxy = getEnvironmentInvoke1ArgConstructorProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {createShellProxy,displayProxy});
		
		// Position the shell off screen for the moment
		// TODO this needs to be done properly so that the location can be set in the model and ignored
		// likewise for the visibility
		IIntegerBeanProxy intBeanProxy = domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(-5000);
		IBeanTypeProxy intBeanTypeProxy = intBeanProxy.getTypeProxy();
		IMethodProxy setlocationMethodProxy = shellBeanTypeProxy.getMethodProxy("setLocation",new IBeanTypeProxy[] {intBeanTypeProxy,intBeanTypeProxy});
		getEnvironmentInvoke2ArgMethodProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {setlocationMethodProxy,shellProxy,intBeanProxy,intBeanProxy});		
		
		IMethodProxy openMethodProxy = shellBeanTypeProxy.getMethodProxy("open");		
		getEnvironmentInvoke0ArgMethodProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {openMethodProxy,shellProxy});
		setBeanProxy(shellProxy);			
	} catch (ThrowableProxy exc) {
		JavaVEPlugin.log("Unable to set create the Shell Bean Proxy on the target VM");
		JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
	}				
}
}
