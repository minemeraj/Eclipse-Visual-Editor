package org.eclipse.jem.internal.proxy.swt;

import org.eclipse.ve.internal.java.core.IBeanProxyHost;

public interface IControlProxyHost extends IBeanProxyHost{
	
void childValidated(IControlProxyHost childProxy);

IControlProxyHost getParentProxyHost();	

void setParentProxyHost(IControlProxyHost aParentProxyHost);

}
