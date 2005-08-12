package org.eclipse.ve.sweet2;

import java.util.HashMap;
import java.util.Map;

public class DomainProviderFactory {
	
	public static Map domainProviders = new HashMap();
	static{
		domainProviders.put(Integer.class,new IntegerDomainProvider());
	}
	
	public static IDomainProvider getDefaultDomainProvider(Class aClass){
		return (IDomainProvider) domainProviders.get(aClass);
	}

}
