package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.jem.internal.instantiation.InitStringAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

public class ResourceProxyAdapter extends BeanProxyAdapter {

public ResourceProxyAdapter(IBeanProxyDomain domain) {
	super(domain);
}
/**
 * The initString is evaluated using a static method on the Environment target VM class
 * that ensures it is evaluated on the Display thread
 */
protected void basicInitializationStringAllocation(String aString, IBeanTypeProxy targetClass){
	// TODO Get rid of this hack as soon as we can
	IBeanTypeProxy environmentBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment"); //$NON-NLS-1$
	IMethodProxy evaluateInitStringMethod = environmentBeanTypeProxy.getMethodProxy("evaluate","java.lang.String");
	IBeanProxy initStringProxy = domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(aString);
	try {
		setupBeanProxy(evaluateInitStringMethod.invoke(environmentBeanTypeProxy,initStringProxy));
	} catch (ThrowableProxy e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}	
protected void beanProxyAllocation(IBeanProxyDomain beanProxyDomain, JavaAllocation allocation)  {
	
	if(allocation instanceof InitStringAllocation){
		InitStringAllocation initStringAllocation = (InitStringAllocation)allocation;
		String qualifiedClassName = ((IJavaInstance) initStringAllocation.eContainer()).getJavaType().getQualifiedNameForReflection();
		basicInitializationStringAllocation(initStringAllocation.getInitString(), beanProxyDomain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(qualifiedClassName));
	}
}

}
