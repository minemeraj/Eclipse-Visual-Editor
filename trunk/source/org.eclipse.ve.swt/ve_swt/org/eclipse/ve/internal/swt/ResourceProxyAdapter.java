package org.eclipse.ve.internal.swt;

import java.text.MessageFormat;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.InitStringAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

public class ResourceProxyAdapter extends BeanProxyAdapter {

public ResourceProxyAdapter(IBeanProxyDomain domain) {
	super(domain);
}
protected Adapter getRegisteredAdapter(EObject eo, Object adapterType) {
	
	Adapter result = super.getRegisteredAdapter(eo,adapterType);
	if(result instanceof InitStringAllocationAdapter){
		eo.eAdapters().remove(result);
		result = new InitStringAllocationAdapter(){
			public IBeanProxy allocate(JavaAllocation allocation, IBeanProxyDomain domain) throws AllocationException {
				InitStringAllocation initStringAllocation = (InitStringAllocation) allocation;
				// The container of the allocation is the IJavaInstance being instantiated.
				String qualifiedClassName = ((IJavaInstance) allocation.eContainer()).getJavaType().getQualifiedNameForReflection();
				String initString = initStringAllocation.getAllocString();
				IBeanTypeProxy targetClass = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(qualifiedClassName);
				if (targetClass == null || targetClass.getInitializationError() != null) {
					// The target class is invalid.
					Throwable exc = new ExceptionInInitializerError(targetClass != null ? targetClass.getInitializationError() : MessageFormat.format(JavaMessages.getString("Proxy_Class_has_Errors_ERROR_"), new Object[] {"unknown"})); //$NON-NLS-1$
					JavaVEPlugin.log("Could not instantiate " + (targetClass != null ? targetClass.getTypeName() : "unknown") + " with initialization string=" + initString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$
					JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
					throw new AllocationException(exc);			
				}
				try { 					
					IBeanTypeProxy environmentBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment"); //$NON-NLS-1$
					IMethodProxy evaluateInitStringMethod = environmentBeanTypeProxy.getMethodProxy("evaluate","java.lang.String");
					IBeanProxy initStringProxy = domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(initString);
					return evaluateInitStringMethod.invoke(environmentBeanTypeProxy,initStringProxy);
				} catch (Exception exc) {
				}
				return null;
			}
		};
		result.setTarget(eo);
		eo.eAdapters().add(result);
		return result;
	}
	return result;
}	

}
