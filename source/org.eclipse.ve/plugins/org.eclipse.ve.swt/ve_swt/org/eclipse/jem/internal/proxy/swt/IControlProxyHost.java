package org.eclipse.jem.internal.proxy.swt;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.java.core.IBeanProxyHost;

public interface IControlProxyHost extends IBeanProxyHost{
	
void childValidated(IControlProxyHost childProxy);

IControlProxyHost getParentProxyHost();	

IBeanProxy getVisualControlBeanProxy();

void setParentProxyHost(IControlProxyHost aParentProxyHost);

}
