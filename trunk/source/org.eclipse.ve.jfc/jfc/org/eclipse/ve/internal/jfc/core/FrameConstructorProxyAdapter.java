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
 *  $Revision: 1.4 $  $Date: 2004-07-16 15:37:25 $ 
 */

import java.util.List;
import java.util.logging.Level;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * A Proxy adapter for classes that require a Frame to construct. E.G. awt.Dialog. Will use
 * a frame if no allocation is provided to get one.
 * 
 * @since 1.0.0
 */
public class FrameConstructorProxyAdapter extends WindowProxyAdapter {
	protected boolean disposeParentOnRelease;

	/*
	 * We need to create an awt Frame needed to construct the Dialog later on since
	 * Dialog does not have a null ctor.
	 */
	public FrameConstructorProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#beanProxyAllocation(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	protected IBeanProxy beanProxyAllocation(JavaAllocation allocation) throws AllocationException {
		// Override to see if the allocation has a "new java.awt.Frame" in it. If it does, we need to
		// grab that frame so that we can dispose it later. 
		// TODO This is really a bad way to do this. We should never have a temporary like this.
		disposeParentOnRelease = false;
		IBeanProxy result = super.beanProxyAllocation(allocation);
		if (allocation instanceof ParseTreeAllocation) {
			// Can only handle parse tree, and only if Frame is first argument.
			PTExpression allocExp = ((ParseTreeAllocation) allocation).getExpression();
			if (allocExp instanceof PTClassInstanceCreation) {
				PTClassInstanceCreation newClass = (PTClassInstanceCreation) allocExp;
				List args = newClass.getArguments();
				if (args.size() == 1) {
					PTExpression arg1 = (PTExpression) args.get(0);
					disposeParentOnRelease = arg1 instanceof PTClassInstanceCreation && "java.awt.Frame".equals(((PTClassInstanceCreation) arg1).getType()); 
				}
			}
		}
		return result;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#basicInitializationStringAllocation(java.lang.String, org.eclipse.jem.internal.proxy.core.IBeanTypeProxy)
	 */
	protected IBeanProxy basicInitializationStringAllocation(String aString, IBeanTypeProxy targetClass) throws AllocationException {
		disposeParentOnRelease = false;

		// If the aString is null, then we are creating using a default Ctor either the class or the superclass.
		// Either way check if there is a default ctor, if so, then just go on up and let it go. Else we
		// need to use a new passing in a frame as the first arg. If the string is not null, then that
		// means this is an old way and we need to support it and let it go on.
		if (aString != null)
			return super.basicInitializationStringAllocation(aString, targetClass);
		
		// See if there is a default ctor for the targetclass. If there is, just go on, it will work.
		if (targetClass.getConstructorProxy((String[]) null) != null)
			return super.basicInitializationStringAllocation(aString, targetClass);
		
		// We need to create using one frame.
		IBeanTypeProxy frameType = targetClass.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Frame");		
		IConstructorProxy ctor = targetClass.getConstructorProxy(new IBeanTypeProxy[] {frameType});
		if (ctor == null)
			return super.basicInitializationStringAllocation(aString, targetClass);	// None that takes a frame, just go on and take the hit.
		
		disposeParentOnRelease = true;
		try {
			return ctor.newInstance(new IBeanProxy[] {frameType.newInstance()});
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.WARNING);
			disposeParentOnRelease = false;
			return null;
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		if (disposeParentOnRelease && isBeanProxyInstantiated()) {
			// Get the parent frame and invoke a method to dispose of the Frame.
			IBeanProxy frameBeanProxy = BeanAwtUtilities.invoke_getParent(getBeanProxy());
			if (frameBeanProxy != null) {
				BeanAwtUtilities.invoke_dispose(frameBeanProxy);
				frameBeanProxy.getProxyFactoryRegistry().releaseProxy(frameBeanProxy);
			}
		};
		disposeParentOnRelease = false;
		super.releaseBeanProxy();
	}
}