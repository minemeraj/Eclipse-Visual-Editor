package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.*;

public class REMStandardSWTBeanTypeProxyFactory implements IREMBeanTypeProxyFactory {
	
	static final String BEAN_TYPE_FACTORY_KEY = "org.eclipse.swt.graphics"; //$NON-NLS-1$
	
	protected final REMProxyFactoryRegistry fFactoryRegistry;
	
public REMStandardSWTBeanTypeProxyFactory(REMProxyFactoryRegistry aRegistry) {
	fFactoryRegistry = aRegistry;	
	fFactoryRegistry.registerBeanTypeProxyFactory(BEAN_TYPE_FACTORY_KEY, this);
}
public IREMBeanTypeProxy getExtensionBeanTypeProxy(String typeName) {
	return null;
}
public IREMBeanTypeProxy getExtensionBeanTypeProxy(String className, Integer classID, IBeanTypeProxy superType) {
	
	if ("org.eclipse.swt.graphics.Rectangle".equals(className)) //$NON-NLS-1$
		return new REMSWTRectangleBeanTypeProxy(fFactoryRegistry, classID, className, superType);
	else if ("org.eclipse.swt.graphics.Point".equals(className)) //$NON-NLS-1$
		return new REMSWTPointBeanTypeProxy(fFactoryRegistry, classID, className, superType);			
	else 
		return null;
}
public void terminateFactory(boolean wait) {
}
}
