package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: InitStringAllocationAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-12 21:44:11 $ 
 */
 
import java.text.MessageFormat;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.InitStringAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;


/**
 * This adapter processes the <code>InitStringAllocation</code> allocation.
 * 
 * <p>This class is a singleton. It doesn't store the target and can be on more than one target.
 * 
 * @see org.eclipse.jem.internal.instantiation.InitStringAllocation
 * @since 1.0.0
 */
public class InitStringAllocationAdapter implements IAllocationAdapter {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IAllocationAdapter#allocate(org.eclipse.jem.internal.instantiation.JavaAllocation, IBeanProxyDomain)
	 */
	public IBeanProxy allocate(JavaAllocation allocation, IBeanProxyDomain domain) throws AllocationException {
		InitStringAllocation initStringAllocation = (InitStringAllocation) allocation;
		// The container of the allocation is the IJavaInstance being instantiated.
		String qualifiedClassName = ((IJavaInstance) allocation.eContainer()).getJavaType().getQualifiedNameForReflection(); 
		return instantiateWithString(initStringAllocation.getAllocString(), domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(qualifiedClassName));
	}
	
	/**
	 * A helper method. There are just times where you need to instantiate using an init string and it is not a IJavaObject.
	 * Passing in null for initializationString will result in default ctor being used.
	 * 
	 * @param initializationString - <code>null</code> means use default ctor, otherwise use initialization string.
	 * @param targetClass
	 * @return created proxy
	 * @throws AllocationException
	 */
	public static IBeanProxy instantiateWithString(
		String initializationString,
		IBeanTypeProxy targetClass)
		throws AllocationException {
		if (targetClass == null || targetClass.getInitializationError() != null) {
			// The target class is invalid.
			Throwable exc = new ExceptionInInitializerError(targetClass != null ? targetClass.getInitializationError() : MessageFormat.format(JavaMessages.getString("Proxy_Class_has_Errors_ERROR_"), new Object[] {"unknown"})); //$NON-NLS-1$
			JavaVEPlugin.log("Could not instantiate " + (targetClass != null ? targetClass.getTypeName() : "unknown") + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			throw new AllocationException(exc);			
		}
		
		try { 					
			return initializationString != null ? targetClass.newInstance(initializationString) : targetClass.newInstance();
		} catch ( ThrowableProxy exc ) {
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			throw new AllocationException(exc);
		} catch (InstantiationException exc) {
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			throw new AllocationException(exc);
		} catch (ClassCastException exc){				
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$				
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			throw new AllocationException(exc);			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return null;	// We don't know since we are singleton.
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return type == IAllocationAdapter.class;
	}

}
