package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class ResourceProxyAdapter extends BeanProxyAdapter {

public ResourceProxyAdapter(IBeanProxyDomain domain) {
	super(domain);
}
/**
 * The initString is evaluated using a static method on the Environment target VM class
 * that ensures it is evaluated on the Display thread
 */
protected IBeanProxy basicInitializationStringAllocation(final String aString, final IBeanTypeProxy targetClass) throws IAllocationProcesser.AllocationException {
	final IAllocationProcesser.AllocationException[] allocExcept = new IAllocationProcesser.AllocationException[1];
	try {
		Object result = JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), new DisplayManager.DisplayRunnable() {
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				try {
					return ResourceProxyAdapter.super.basicInitializationStringAllocation(aString, targetClass);
				} catch (AllocationException e) {
					allocExcept[0] = e;
					return "allocationException";
				}
			}
		});
		if (result instanceof String)
			throw allocExcept[0];
		else
			return (IBeanProxy) result;
	} catch (ThrowableProxy e) {
		throw new IAllocationProcesser.AllocationException(e);
	}
}

protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation) throws IAllocationProcesser.AllocationException {
	final IAllocationProcesser.AllocationException[] allocExcept = new IAllocationProcesser.AllocationException[1];
	try {
		Object result = JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), new DisplayManager.DisplayRunnable() {
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				try {
					return ResourceProxyAdapter.super.beanProxyAllocation(allocation);
				} catch (AllocationException e) {
					allocExcept[0] = e;
					return "allocationException";
				}
			}
		});
		if (result instanceof String)
			throw allocExcept[0];
		else
			return (IBeanProxy) result;
	} catch (ThrowableProxy e) {
		throw new IAllocationProcesser.AllocationException(e);
	}
}

}
