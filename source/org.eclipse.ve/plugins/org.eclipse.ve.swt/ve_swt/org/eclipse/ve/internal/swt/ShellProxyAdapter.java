package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

public class ShellProxyAdapter extends CompositeProxyAdapter {

public ShellProxyAdapter(IBeanProxyDomain domain) {
	super(domain);
}
protected void primInstantiateBeanProxy() {
	// Old way of doing things relying on the Environment to be the owner of the display thread
	setBeanProxy((IBeanProxy) invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
		public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
			IBeanTypeProxy shellBeanTypeProxy =
				displayProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Shell");
			IConstructorProxy createShellProxy =
				shellBeanTypeProxy.getConstructorProxy(new IBeanTypeProxy[] { displayProxy.getTypeProxy()});
			IBeanProxy shellProxy = createShellProxy.newInstance(new IBeanProxy[] { displayProxy });

			// Position the shell off screen for the moment
			// TODO this needs to be done properly so that the location can be set in the model and ignored
			// likewise for the visibility
			IIntegerBeanProxy intBeanProxy =
				displayProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(-5000);
			IMethodProxy setlocationMethodProxy =
				shellBeanTypeProxy.getMethodProxy("setLocation", new String[] {"int", "int"});
			setlocationMethodProxy.invoke(shellProxy, new IBeanProxy[] {intBeanProxy, intBeanProxy});

			IMethodProxy openMethodProxy = shellBeanTypeProxy.getMethodProxy("open");
			openMethodProxy.invoke(shellProxy);
			return shellProxy;			
		}
	}));
//		IBeanTypeProxy shellBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Shell");
//		IConstructorProxy createShellProxy = shellBeanTypeProxy.getConstructorProxy(new IBeanTypeProxy[] {displayProxy.getTypeProxy()});
//		IBeanProxy shellProxy = getEnvironmentInvoke1ArgConstructorProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {createShellProxy,displayProxy});
		
//		// Position the shell off screen for the moment
//		// TODO this needs to be done properly so that the location can be set in the model and ignored
//		// likewise for the visibility
//		IIntegerBeanProxy intBeanProxy = domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(-5000);
//		IBeanTypeProxy intBeanTypeProxy = intBeanProxy.getTypeProxy();
//		IMethodProxy setlocationMethodProxy = shellBeanTypeProxy.getMethodProxy("setLocation",new IBeanTypeProxy[] {intBeanTypeProxy,intBeanTypeProxy});
//		getEnvironmentInvoke2ArgMethodProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {setlocationMethodProxy,shellProxy,intBeanProxy,intBeanProxy});		
//		
//		IMethodProxy openMethodProxy = shellBeanTypeProxy.getMethodProxy("open");		
//		getEnvironmentInvoke0ArgMethodProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {openMethodProxy,shellProxy});
//		setBeanProxy(shellProxy);			
				
}

}
