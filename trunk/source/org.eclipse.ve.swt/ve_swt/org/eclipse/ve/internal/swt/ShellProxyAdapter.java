package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class ShellProxyAdapter extends CompositeProxyAdapter {

	public ShellProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#beanProxyAllocation(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation) throws AllocationException {
		AllocationException[] allocExc = new AllocationException[1];
		Object result = invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				try {
					IBeanProxy shellProxy = superBeanProxyAllocation(allocation);
					// Position the shell off screen for the moment
					// TODO this needs to be done properly so that the location can be set in the model and ignored
					// likewise for the visibility
					IIntegerBeanProxy intBeanProxy =
						displayProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(-5000);
					IMethodProxy setlocationMethodProxy =
						shellProxy.getTypeProxy().getMethodProxy("setLocation", new String[] {"int", "int"});
					setlocationMethodProxy.invoke(shellProxy, new IBeanProxy[] {intBeanProxy, intBeanProxy});
		
					IMethodProxy openMethodProxy = shellProxy.getTypeProxy().getMethodProxy("open");
					openMethodProxy.invoke(shellProxy);
					return shellProxy;			
				} catch (AllocationException e) {
					return "allocationexception";
				}
			}
		});
		if (result instanceof String)
			throw allocExc[0];
		else
			return (IBeanProxy) result;
	}

}
