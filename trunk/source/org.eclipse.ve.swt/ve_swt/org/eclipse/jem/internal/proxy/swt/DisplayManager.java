/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DisplayManager.java,v $
 *  $Revision: 1.1 $  $Date: 2004-02-05 23:11:19 $ 
 */
package org.eclipse.jem.internal.proxy.swt;

import java.io.InputStream;
import java.util.Stack;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ICallback;
import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;
 
/**
 * This class is for managing the display.
 * @since 1.0.0
 */
public class DisplayManager {
	
	/**
	 * This is the base class for doing a display exec.
	 * Subclasses will provide the actual function required.
	 * 
	 * @since 1.0.0
	 */
	public static abstract class DisplayRunnable implements ICallback {
		
		// TODO These need to go into a Common that is available to both IDE and remote vm.
		protected static final int RUN_EXEC = 0;
		protected IBeanProxy displayExecProxy;
		
		/*
		 * Set the displayExec proxy for this runnable.
		 * <package-protected> because only DisplayManager should set this. 
		 * @param displayExecProxy
		 * 
		 * @since 1.0.0
		 */
		void setDisplayExecProxy(IBeanProxy displayExecProxy) {
			this.displayExecProxy = displayExecProxy;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, org.eclipse.jem.internal.proxy.core.IBeanProxy)
		 */
		public Object calledBack(int msgID, IBeanProxy parm) {
			try {
				if (msgID == RUN_EXEC) {					
					try {
						return run(parm);
					} catch (ThrowableProxy e) {
						return e;
					}	
				}
			} finally {
				// Clean up. This is a one-shot deal.
				ProxyFactoryRegistry registry = displayExecProxy.getProxyFactoryRegistry();				
				registry.getCallbackRegistry().deregisterCallback(displayExecProxy);				
				Constants.getConstants(registry).returnDisplayExec(displayExecProxy);				
			}
		
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, java.lang.Object)
		 */
		public final Object calledBack(int msgID, Object parm) {
			return null;	// Never called, so should not be overridden.
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, java.lang.Object[])
		 */
		public final Object calledBack(int msgID, Object[] parms) {
			return null;	// Never called, so should not be overridden.
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBackStream(int, java.io.InputStream)
		 */
		public final void calledBackStream(int msgID, InputStream is) {
			// Never called, so should not be overridden.
		}
		
		/**
		 * Execute the run from the display async or sync request. Any proxy calls (using the registry from
		 * <code>displayProxy.getProxyFactoryRegistry()</code> is guarenteed to be executed on the UI thread 
		 * of the display on the proxy vm.
		 *  
		 * @param displayProxy A proxy to the actual Display object that this runnable is executing on.
		 * @return An IBeanProxy or a simple object like String  or Integer to return from a syncExec call or <code>null</code> if nothing to return.
		 * @since 1.0.0
		 */
		public abstract Object run(IBeanProxy displayProxy) throws ThrowableProxy;

	}
	
	/*
	 * Constants stored in the registry that are required by this DisplayManager.
	 * 
	 * @since 1.0.0
	 */
	private static class Constants {
		public static final String REGISTRY_KEY = "DISPLAYMANAGERPROXYSWTCONSTANTS:"; //$NON-NLS-1$
		
		public static Constants getConstants(ProxyFactoryRegistry registry) {
			Constants constants = (Constants) registry.getConstants(REGISTRY_KEY);
			if (constants == null)
				registry.registerConstants(REGISTRY_KEY, constants = new Constants(registry));
			return constants;
		}
		
		private IBeanTypeProxy displayType;
		private IBeanTypeProxy displayExecType;
		private IMethodProxy getDefaultProxy;
		private IMethodProxy findDisplayProxy;
		private IMethodProxy beepProxy;
		private IMethodProxy activeShellProxy;
		private IMethodProxy getBoundsProxy;
		private IMethodProxy getClientAreaProxy;
		private IMethodProxy updateProxy;
		private IMethodProxy displayExecAsyncExec;
		private IMethodProxy displayExecSyncExec;		
		
		private Stack displayExecPool;	// Stack of free displayExec objects.
		protected static int NUMBER_FREE_DISPLAYEXEC = 2;	// MAx number of free display execs to keep open.		
		
		protected Constants(ProxyFactoryRegistry registry) {
			displayType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Display");
		}
		
		public IMethodProxy getDefault() {
			if (getDefaultProxy == null)
				getDefaultProxy = displayType.getMethodProxy("getDefault");
			return getDefaultProxy;
		}
		
		public IMethodProxy getFindDisplay() {
			if (findDisplayProxy == null)
				findDisplayProxy = displayType.getMethodProxy("findDisplay", "java.lang.Thread");
			return findDisplayProxy;
		}	

		public IMethodProxy getBeep() {
			if (beepProxy == null)
				beepProxy = displayType.getMethodProxy("beep");
			return beepProxy;
		}
		
		public IMethodProxy getActiveShell() {
			if (activeShellProxy == null)
				activeShellProxy = displayType.getMethodProxy("activeShell");
			return activeShellProxy;
		}	
		
		public IMethodProxy getBounds() {
			if (getBoundsProxy == null)
				getBoundsProxy = displayType.getMethodProxy("getBounds");
			return getBoundsProxy;
		}
		
		public IMethodProxy getClientArea() {
			if (getClientAreaProxy == null)
				getClientAreaProxy = displayType.getMethodProxy("getClientArea");
			return getClientAreaProxy;
		}
		
		public IMethodProxy getUpdate() {
			if (updateProxy == null)
				updateProxy = displayType.getMethodProxy("update");
			return updateProxy;
		}		
		
		private IBeanTypeProxy getDisplayExecType() {
			if (displayExecType == null)
				displayExecType = displayType.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.DisplayExec");
			return displayExecType;
		}

		public IMethodProxy getDisplayExecAsyncExec() {
			if (displayExecAsyncExec == null)
				displayExecAsyncExec = getDisplayExecType().getMethodProxy("asyncExec", "org.eclipse.swt.widgets.Display");
			return displayExecAsyncExec;
		}
		
		public IMethodProxy getDisplayExecSyncExec() {
			if (displayExecSyncExec == null)
				displayExecSyncExec = getDisplayExecType().getMethodProxy("syncExec", "org.eclipse.swt.widgets.Display");
			return displayExecSyncExec;
		}
		
		public synchronized IBeanProxy getFreeDisplayExec() throws ThrowableProxy {
			if (displayExecPool == null)
				displayExecPool = new Stack();
			if (displayExecPool.isEmpty()) {
				// Return a new one.
				return getDisplayExecType().newInstance();
			} else
				return (IBeanProxy) displayExecPool.pop();
		}
		
		public synchronized void returnDisplayExec(IBeanProxy displayExecProxy) {
			if (displayExecProxy.isValid() && displayExecPool != null && displayExecPool.size() < NUMBER_FREE_DISPLAYEXEC) {
				displayExecPool.push(displayExecProxy);
			} else
				displayExecProxy.getProxyFactoryRegistry().releaseProxy(displayExecProxy);
		}
	}

	/*
	 * Never constructed. 
	 * 
	 * @since 1.0.0
	 */
	private DisplayManager() {
		super();
	}
	
	/**
	 * Get the default display.
	 * 
	 * @param registry
	 * @return The Display proxy.
	 * 
	 * @see org.eclipse.swt.widgets.Display#getDefault()
	 * @since 1.0.0
	 */
	public static IBeanProxy getDefault(ProxyFactoryRegistry registry) {
		return Constants.getConstants(registry).getDefault().invokeCatchThrowableExceptions(null);
	}
	
	/**
	 * Find the display for the given thread.
	 * @param threadProxy
	 * @return The Display proxy.
	 * 
	 * @see org.eclipse.swt.widgets.Display#findDisplay(java.lang.Thread)
	 * @since 1.0.0
	 */
	public static IBeanProxy findDisplay(IBeanProxy threadProxy) {
		return Constants.getConstants(threadProxy.getProxyFactoryRegistry()).getFindDisplay().invokeCatchThrowableExceptions(null, threadProxy);
	}
	
	/**
	 * Beep the given display.
	 * @param displayProxy
	 * @throws ThrowableProxy Thrown if not executed on UI thread for the display given, or if the display is disposed.
	 * 
	 * @see org.eclipse.swt.widgets.Display#beep()
	 * @since 1.0.0
	 */
	public static void beep(IBeanProxy displayProxy) throws ThrowableProxy {
		Constants.getConstants(displayProxy.getProxyFactoryRegistry()).getBeep().invoke(displayProxy);
	}
	
	/**
	 * Get the active shell for the given display.
	 * 
	 * @param displayProxy
	 * @return The Shell proxy.
	 * @throws ThrowableProxy Thrown if not executed on UI thread for the display given, or if the display is disposed.
	 *
	 * @see org.eclipse.swt.widgets.Display#getActiveShell() 
	 * @since 1.0.0
	 */
	public static IBeanProxy getActiveShell(IBeanProxy displayProxy) throws ThrowableProxy {
		return Constants.getConstants(displayProxy.getProxyFactoryRegistry()).getActiveShell().invoke(displayProxy);
	}

	/**
	 * Return the bounds for the given display.
	 * 
	 * @param displayProxy
	 * @return The Rectangle bounds proxy.
	 * @throws ThrowableProxy Thrown if not executed on UI thread for the display given, or if the display is disposed.
	 * 
	 * @see org.eclipse.swt.widgets.Display#getBounds() 
	 * @since 1.0.0
	 */
	public static IBeanProxy getBounds(IBeanProxy displayProxy) throws ThrowableProxy {
		return Constants.getConstants(displayProxy.getProxyFactoryRegistry()).getBounds().invoke(displayProxy);
	}
	
	/**
	 * Return the client area for the given display.
	 * 
	 * @param displayProxy
	 * @return The Rectangle client area proxy.
	 * @throws ThrowableProxy Thrown if not executed on UI thread for the display given, or if the display is disposed.
	 * 
	 * @see org.eclipse.swt.widgets.Display#getClientArea() 
	 * @since 1.0.0
	 */
	public static IBeanProxy getClientArea(IBeanProxy displayProxy) throws ThrowableProxy {
		return Constants.getConstants(displayProxy.getProxyFactoryRegistry()).getClientArea().invoke(displayProxy);
	}
	
	/**
	 * Update the display
	 * 
	 * @param displayProxy
	 * @throws ThrowableProxy Thrown if not executed on UI thread for the display given, or if the display is disposed.
	 * 
	 * @see org.eclipse.swt.widgets.Display#update() 
	 * @since 1.0.0
	 */
	public static void update(IBeanProxy displayProxy) throws ThrowableProxy {
		Constants.getConstants(displayProxy.getProxyFactoryRegistry()).getUpdate().invoke(displayProxy);
	}	

	/**
	 * Do a syncExec on the default display.
	 * @param registry
	 * @param runnable
	 * @return result of the runnable
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	public static Object syncExec(ProxyFactoryRegistry registry, DisplayRunnable runnable) throws ThrowableProxy {
		return syncExec(null, runnable, registry);
	}
	
	
	/**
	 * Do a syncExec on the given display and using the given runnable.
	 * 
	 * @param displayProxy The display to syncExec onto. It must be set, it cannot be <code>null</code>.
	 * @param runnable
	 * @return result of the runnable
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	public static Object syncExec(IBeanProxy displayProxy, DisplayRunnable runnable) throws ThrowableProxy {
		return syncExec(displayProxy, runnable, displayProxy.getProxyFactoryRegistry());
	}
	
	/*
	 * Internal version that takes both a displayProxy, which may be <code>null</code>, and a registry.
	 * This is because we need a registry, and if displayProxy was null, then we couldn't get it.
	 * 
	 * @param displayProxy
	 * @param runnable
	 * @param registry
	 * @return result of the runnable
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	protected static Object syncExec(IBeanProxy displayProxy, DisplayRunnable runnable, ProxyFactoryRegistry registry) throws ThrowableProxy {
		Constants constants = Constants.getConstants(registry);
		IBeanProxy displayExecProxy = constants.getFreeDisplayExec();
		try {
			runnable.setDisplayExecProxy(displayExecProxy);			
			registry.getCallbackRegistry().registerCallback(displayExecProxy, runnable);
			Object result = constants.getDisplayExecSyncExec().invoke(displayExecProxy, displayProxy);
			displayExecProxy = null;	// Ended well, so runnable took care of clean up.
			if (result instanceof ThrowableProxy)
				throw (ThrowableProxy) result;
			return result;
		} finally {
			if (displayExecProxy != null) {
				registry.getCallbackRegistry().deregisterCallback(displayExecProxy);				
				constants.returnDisplayExec(displayExecProxy);
			}
		}
	}
	
	/**
	 * Do an asyncExec on the default display. It will return immediately.
	 * 
	 * @param registry
	 * @param runnable
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	public static void asyncExec(ProxyFactoryRegistry registry, DisplayRunnable runnable) throws ThrowableProxy {
		asyncExec(null, runnable, registry);
	}
	
	
	/**
	 * Do an asyncExec on the given display and using the given runnable. It will return immediately.
	 * 
	 * @param displayProxy The display to asyncExec onto. It must be set, it cannot be <code>null</code>.
	 * @param runnable
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	public static void asyncExec(IBeanProxy displayProxy, DisplayRunnable runnable) throws ThrowableProxy {
		asyncExec(displayProxy, runnable, displayProxy.getProxyFactoryRegistry());
	}
	
	/*
	 * Internal version that takes both a displayProxy, which may be <code>null</code>, and a registry.
	 * This is because we need a registry, and if displayProxy was null, then we couldn't get it.
	 * 
	 * @param displayProxy
	 * @param runnable
	 * @param registry
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	protected static void asyncExec(IBeanProxy displayProxy, DisplayRunnable runnable, ProxyFactoryRegistry registry) throws ThrowableProxy {
		Constants constants = Constants.getConstants(registry);
		IBeanProxy displayExecProxy = constants.getFreeDisplayExec();
		try {
			runnable.setDisplayExecProxy(displayExecProxy);
			registry.getCallbackRegistry().registerCallback(displayExecProxy, runnable);
			constants.getDisplayExecAsyncExec().invoke(displayExecProxy, displayProxy);
			displayExecProxy = null;	// Ended well, so runnable will take care of clean up. (Though it may fail, and so never clean up. Risk we have to take).
		} finally {
			if (displayExecProxy != null) {
				registry.getCallbackRegistry().deregisterCallback(displayExecProxy);				
				constants.returnDisplayExec(displayExecProxy);
			}
		}
	}
	
}
