package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.*;

public class SWTREMProxyRegistration {
	
public static void initialize(ProxyFactoryRegistry registry){
	REMProxyFactoryRegistry remRegistry = (REMProxyFactoryRegistry)registry;
	new REMStandardSWTBeanTypeProxyFactory(remRegistry);
	new REMStandardSWTBeanProxyFactory(remRegistry);		
}

}
