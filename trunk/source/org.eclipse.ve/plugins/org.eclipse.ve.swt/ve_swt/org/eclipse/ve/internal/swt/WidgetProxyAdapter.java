package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.internal.beaninfo.*;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;
import org.eclipse.jem.java.Method;

public class WidgetProxyAdapter extends BeanProxyAdapter {
	
	private IBeanTypeProxy environmentBeanTypeProxy;
	private IMethodProxy environmentInvoke0ArgMethodProxy;
	private IMethodProxy environmentInvoke1ArgMethodProxy;	
	private IMethodProxy environmentInvoke2ArgMethodProxy;	
	private IMethodProxy environmentInvoke1ArgConstructorProxy;	
	private IMethodProxy environmentInvoke2ArgConstructorProxy;	
	
	public WidgetProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	protected IBeanProxy primReadBeanFeature(PropertyDecorator propDecor, IBeanProxy aSource) throws ThrowableProxy{
	
		if (!aSource.isValid()) return null;	// Not valid to read.
		Method method = propDecor.getReadMethod();
		// Cope with properties that have no get method
		if (method == null)
			return null;
		ProxyFactoryRegistry registry = aSource.getProxyFactoryRegistry();
		// Now get the method proxy on the same VM as the source
		IMethodProxy getMethodProxy = BeanProxyUtilities.getMethodProxy(method, registry);
		// We must run this method through the environment class
		return getEnvironmentInvoke0ArgMethodProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {getMethodProxy,aSource});
	}
	
	protected IMethodProxy getEnvironmentInvoke0ArgMethodProxy(){
		if(environmentInvoke0ArgMethodProxy == null){
			environmentInvoke0ArgMethodProxy = getEnvironmentBeanTypeProxy().getMethodProxy("invoke",new String[] {"java.lang.reflect.Method","java.lang.Object"});
		}
		return environmentInvoke0ArgMethodProxy;
	}
	
	protected IMethodProxy getEnvironmentInvoke1ArgMethodProxy(){
		if(environmentInvoke1ArgMethodProxy == null){
			environmentInvoke1ArgMethodProxy = getEnvironmentBeanTypeProxy().getMethodProxy("invoke",new String[] {"java.lang.reflect.Method","java.lang.Object","java.lang.Object"});
		}
		return environmentInvoke1ArgMethodProxy;
	}
	
	protected IMethodProxy getEnvironmentInvoke2ArgMethodProxy(){
		if(environmentInvoke2ArgMethodProxy == null){
			environmentInvoke2ArgMethodProxy = getEnvironmentBeanTypeProxy().getMethodProxy("invoke",new String[] {"java.lang.reflect.Method","java.lang.Object","java.lang.Object","java.lang.Object"});
		}
		return environmentInvoke2ArgMethodProxy;
	}	
	
	protected IMethodProxy getEnvironmentInvoke1ArgConstructorProxy(){
		if(environmentInvoke1ArgConstructorProxy == null){
			environmentInvoke1ArgConstructorProxy = getEnvironmentBeanTypeProxy().getMethodProxy("invoke",new String[] {"java.lang.reflect.Constructor","java.lang.Object"});
		}
		return environmentInvoke1ArgConstructorProxy;
	}
	
	protected IMethodProxy getEnvironmentInvoke2ArgConstructorProxy(){
		if(environmentInvoke2ArgConstructorProxy == null){
			environmentInvoke2ArgConstructorProxy = getEnvironmentBeanTypeProxy().getMethodProxy("invoke",new String[] {"java.lang.reflect.Constructor","java.lang.Object","java.lang.Object"});
		}
		return environmentInvoke2ArgConstructorProxy;
	}			
	
	protected final IBeanTypeProxy getEnvironmentBeanTypeProxy(){
		if(environmentBeanTypeProxy == null){	
			environmentBeanTypeProxy = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment"); //$NON-NLS-1$		
		}
		return environmentBeanTypeProxy;
	}
	
	protected void primApplyBeanFeature(EStructuralFeature sf , PropertyDecorator propDecor , BeanFeatureDecorator featureDecor, IBeanProxy settingBeanProxy) throws Exception {
		if (propDecor != null) {
			if (propDecor.needIntrospection())
				throw new ReinstantiationNeeded();	// We need to reinstantiate. That will then cause re-introspection to occur.
			 if (propDecor.getWriteMethod() != null) {
				Method method = propDecor.getWriteMethod();
				ProxyFactoryRegistry registry = getBeanProxy().getProxyFactoryRegistry();
				// Find the set method on the same VM as the source
				IMethodProxy setMethodProxy = BeanProxyUtilities.getMethodProxy(method, registry);
				getEnvironmentInvoke1ArgMethodProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {setMethodProxy,getBeanProxy(),settingBeanProxy});				 	
				return;
			 }
		}		
	}	
	
	public void releaseBeanProxy() {
		if(isBeanProxyInstantiated()){
			try{
				IBeanProxy widgetBeanProxy = getBeanProxy();
				IMethodProxy disposeWidgetMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("dispose");
				getEnvironmentInvoke0ArgMethodProxy().invoke(getEnvironmentBeanTypeProxy(),new IBeanProxy[] {disposeWidgetMethodProxy,widgetBeanProxy});
			} catch (ThrowableProxy exc){
				JavaVEPlugin.log("Unable to set dispose the Widget Bean Proxy on the target VM");
				JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);			
			}
		}
		super.releaseBeanProxy();
	}		

}
