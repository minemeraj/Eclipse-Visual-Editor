package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AppletProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */


import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
/* Applets have to have special code run when they are created and destroyed
 */
public class AppletProxyAdapter extends ContainerProxyAdapter {
	
public AppletProxyAdapter(IBeanProxyDomain domain){
	super(domain);
}
/* When an applet is first loaded the spec states that the init method should be called
 */ 
protected void primInstantiateBeanProxy(){
	super.primInstantiateBeanProxy();

	if (getErrorStatus() != ERROR_SEVERE && !((IJavaObjectInstance)target).isSetInstantiateUsing()) {
		// We didn't use instantiateUsing, so we need to use the DummyAppletStub.
		IBeanTypeProxy dummyStubType = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.DummyAppletStub"); //$NON-NLS-1$
		dummyStubType.getMethodProxy("initializeApplet", "java.applet.Applet").invokeCatchThrowableExceptions(null, getBeanProxy()); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
/* When an applet is removed from the system it should be called with destroy so it 
 * can free up any resources it allocated
 */
public void releaseBeanProxy(){
	if (isBeanProxyInstantiated()) {
		IMethodProxy destroyMethod = getBeanProxy().getTypeProxy().getMethodProxy("destroy"); //$NON-NLS-1$
		destroyMethod.invokeCatchThrowableExceptions(getBeanProxy());
	}
	super.releaseBeanProxy();	
}

}


