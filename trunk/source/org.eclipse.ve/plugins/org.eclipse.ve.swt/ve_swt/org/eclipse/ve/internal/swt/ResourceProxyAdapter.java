package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.*;

public class ResourceProxyAdapter extends BeanProxyAdapter {

public ResourceProxyAdapter(IBeanProxyDomain domain) {
	super(domain);
}
/**
 * The initString is evaluated using a static method on the Environment target VM class
 * that ensures it is evaluated on the Display thread
 */
protected IBeanProxy instantiateWithString(IBeanTypeProxy targetClass, String initString) throws ThrowableProxy, InstantiationException {
	
	IBeanTypeProxy environmentBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment"); //$NON-NLS-1$
	IMethodProxy evaluateInitStringMethod = environmentBeanTypeProxy.getMethodProxy("evaluate","java.lang.String");
	IBeanProxy initStringProxy = domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(initString);
	return evaluateInitStringMethod.invoke(environmentBeanTypeProxy,initStringProxy);
	

}	

}
