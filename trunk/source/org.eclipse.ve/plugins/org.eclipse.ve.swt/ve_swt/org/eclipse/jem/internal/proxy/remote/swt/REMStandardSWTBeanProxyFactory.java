package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.swt.*;
import org.eclipse.jem.internal.proxy.awt.*;
import org.eclipse.jem.internal.proxy.core.*;

public class REMStandardSWTBeanProxyFactory implements IStandardSWTBeanProxyFactory {
	
	final IStandardBeanTypeProxyFactory fBeanTypeFactory;	
	
public REMStandardSWTBeanProxyFactory(ProxyFactoryRegistry factory) {
	factory.registerBeanProxyFactory(IStandardSWTBeanProxyFactory.REGISTRY_KEY, this);
	fBeanTypeFactory = factory.getBeanTypeProxyFactory();
}		

public IPointBeanProxy createPointBeanProxyWith(int x, int y) {
	try {
		return (IPointBeanProxy) fBeanTypeFactory.getBeanTypeProxy("org.eclipse.swt.graphics.Point").newInstance("new org.eclipse.swt.graphics.Point("+x+","+y+")"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (ThrowableProxy e) {
		return null;
	} catch (InstantiationException e) {
		return null;	// Shouldn't occur
	}	
}

public IRectangleBeanProxy createBeanProxyWith(int x, int y, int width, int height) {
	try {
		return (IRectangleBeanProxy) fBeanTypeFactory.getBeanTypeProxy("org.eclipse.swt.graphics.Rectangle").newInstance("new org.eclipse.swt.graphics.Rectangle("+x+","+y+","+width+","+height+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	} catch (ThrowableProxy e) {
		return null;
	} catch (InstantiationException e) {
		return null;	// Shouldn't occur
	}
}

public void terminateFactory() {
	// TODO Auto-generated method stub
} 

}
