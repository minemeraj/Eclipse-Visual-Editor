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
 *  $RCSfile: FrameConstructorProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

public class FrameConstructorProxyAdapter extends WindowProxyAdapter {
	protected IBeanProxy fFrameBeanProxy;

	/*
	 * We need to create an awt Frame needed to construct the Dialog later on since
	 * Dialog does not have a null ctor.
	 */
	public FrameConstructorProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	/*
	 * The initString for Dialog and Window can be "new java.awt.Dialog(new java.awt.Frame());
	 * and it can also be "new java.awt.Dialog(this)" if the composition is a Frame subclass
	 * Whatever the string is right now just use the default constructor
	 * In theory the InitStringParser should be able to cope with "new java.awt.Dialog(new java.awt.Frame())"
	 * but it can't
	 */
	protected IBeanProxy instantiateWithString(IBeanTypeProxy targetClass, String initString) throws ThrowableProxy {
		return defaultInstantiate(targetClass);
	}	

	protected IBeanProxy defaultInstantiate(IBeanTypeProxy typeClass) throws ThrowableProxy {
		// See whether or not there is a null constuctor - this is in case of subclassing
		IConstructorProxy nullConstructor = typeClass.getNullConstructorProxy();
		if ( nullConstructor != null ) {
			return nullConstructor.newInstance();
		} else {
			// Get the constructor that takes a Frame argument
			IConstructorProxy constructorWithFrame =
				typeClass.getConstructorProxy(new String[] { "java.awt.Frame" }); //$NON-NLS-1$
			// Create the dialog using the Frame we created earlier as the argument.
			if ( constructorWithFrame != null ) {
				if(fFrameBeanProxy == null){
					try {			
						IBeanTypeProxy frameClass = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Frame"); //$NON-NLS-1$
						fFrameBeanProxy = frameClass.newInstance();
					} catch (ThrowableProxy e) {
						return null;
					}
				}
				// Create a frame if required to use as an argument
				return constructorWithFrame.newInstance(new IBeanProxy[] { fFrameBeanProxy });
			} else {
				return typeClass.newInstance();
			}
		} 
	}
	/**
	 * releaseBeanProxy: Get rid of the Frame bean proxy being held.
	 */
	public void releaseBeanProxy() {
		super.releaseBeanProxy();
		if (fFrameBeanProxy != null && fFrameBeanProxy.isValid()) {
			// Invoke a method to dispose of the Frame.
			BeanAwtUtilities.invoke_dispose(fFrameBeanProxy);
			fFrameBeanProxy.getProxyFactoryRegistry().releaseProxy(fFrameBeanProxy);
		};
		fFrameBeanProxy = null;		
	}
}