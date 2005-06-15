/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DisplayManager.java,v $
 *  $Revision: 1.10 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.jem.internal.proxy.swt;

import java.io.InputStream;
import java.util.Stack;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;
 
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
		
		/**
		 * This is to be thrown by syncExec runnables to indicate that a special exception
		 * has occurred. syncExec will throw this RunnableException, which wrappers the real
		 * exception, through the getCause() method, when syncExec is finished. Users can then
		 * get the real cause out of it.
		 *  
		 * @since 1.0.0
		 * @see Throwable#getCause()
		 */
		public static class RunnableException extends Exception {
		
			/**
			 * @param cause
			 * 
			 * @since 1.0.0
			 */
			public RunnableException(Throwable cause) {
				super(cause);
			}
		};
		
		/*
		 * This RunnableRuntimeException, which wrappers the real
		 * runtime exception, through the getCause() method, when syncExec is finished, will be thrown
		 * if a RuntimeException occurs during the syncExec runnable processing.
		 * <package-protected> because only DisplayManager accesses it. 
		 * @since 1.0.0
		 * @see Throwable#getCause()
		 */		
		static class RunnableRuntimeException extends Exception {
			/**
			 * @param cause
			 * 
			 * @since 1.0.0
			 */
			public RunnableRuntimeException(Throwable cause) {
				super(cause);
			}			
		}
		
		IBeanProxy displayExecProxy;
		Exception exception;
		Object runnableReturn;
		
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
		
		/*
		 * Return the runnable exception, if one thrown.
		 * <package-protected> because only DisplayManager should get this. 
		 * @return runnable exception, or <code>null</code> if no runnable exception thrown.
		 * 
		 * @since 1.0.0
		 */
		Exception getException() {
			return exception;
		}
		
		/*
		 * Return the runnable return. This is the value that the run() returned. It is
		 * an IDE value. 
		 * <pacakge-protected> because only DisplayManager should get this.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		Object getRunnableReturn() {
			return runnableReturn;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, org.eclipse.jem.internal.proxy.core.IBeanProxy)
		 */
		public final Object calledBack(int msgID, IBeanProxy parm) {
			// Set so that we now we are running under a UI thread callback.
			Constants.getConstants(displayExecProxy.getProxyFactoryRegistry()).setThreadSyncDisplay(parm);
			try {
				if (msgID == RUN_EXEC) {					
					try {
						runnableReturn = run(parm);
						return null;
					} catch (ThrowableProxy e) {
						return e;
					} catch (RunnableException e) {
						exception = e;	// This is a specific exception that users wants to go through. Considered a good return for the callback.
					} catch (RuntimeException e) {
						exception = new RunnableRuntimeException(e);	
						throw e;	// This is a shouldn't occur exception, so log as runnable so can be rethrown later, but also rethrow here so that it will be logged too.
					}
				}
			} finally {
				// Clean up. This is a one-shot deal.
				ProxyFactoryRegistry registry = displayExecProxy.getProxyFactoryRegistry();
				Constants.getConstants(displayExecProxy.getProxyFactoryRegistry()).setThreadSyncDisplay(null);	// No longer under UI thread callback control.				
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
		 * @return An object to return from the syncExec call or <code>null</code> if nothing to return.
		 * @since 1.0.0
		 * @throws ThrowableProxy if a remote vm error occurred
		 * @throws RunnableException if any other special exception occurs that needs to be sent back to called, if syncExec, wrapper it in a RunnableException.
		 */
		public abstract Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException;

	}
	
	/**
	 * Use a subclass of this when you are using an expression in the display runnable. This class will make sure
	 * that the expression is transfered between the threads correctly when used with {@link DisplayManager#syncExec(IBeanProxy, ExpressionDisplayRunnable)
	 * 
	 * @since 1.1.0
	 */
	public static abstract class ExpressionDisplayRunnable extends DisplayManager.DisplayRunnable {
		protected Expression expression;
		
		/**
		 * Construct with the expression.
		 * @param expression
		 * 
		 * @since 1.1.0
		 */
		public ExpressionDisplayRunnable(IExpression expression) {
			this.expression = (Expression) expression;
		}
		
		public final Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
			expression.transferThread();
			try {
				return doRun(displayProxy);
			} finally {
				expression.beginTransferThread();
			}
		}
		
		/**
		 * Subclasses will do the actual work within this method.
		 * 
		 * @param displayProxy
		 * @return
		 * @throws ThrowableProxy
		 * @throws RunnableException
		 * 
		 * @since 1.1.0
		 */
		protected abstract Object doRun(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException;
		
	}
	
	/**
	 * Used when expressions are being evaluated accross the thread boundaries.
	 * 
	 * @param runnable
	 * @return
	 * @throws ThrowableProxy
	 * @throws RunnableException
	 * 
	 * @since 1.1.0
	 */
	public static Object syncExec(IBeanProxy displayProxy, ExpressionDisplayRunnable runnable) throws ThrowableProxy, RunnableException {
		runnable.expression.beginTransferThread();
		try {
			return syncExec(displayProxy, (DisplayRunnable) runnable);
		} finally {
			if (runnable.expression.isValid())
				runnable.expression.transferThread();
		}
	}

	/*
	 * Constants stored in the registry that are required by this DisplayManager.
	 * 
	 * @since 1.0.0
	 */
	private static class Constants {
		public static final Object REGISTRY_KEY = new Object();
		
		public static Constants getConstants(ProxyFactoryRegistry registry) {
			Constants constants = (Constants) registry.getConstants(REGISTRY_KEY);
			if (constants == null)
				registry.registerConstants(REGISTRY_KEY, constants = new Constants(registry));
			return constants;
		}
		
		/*
		 * This stores for the current thread what displayProxy for a syncExec in use for the thread. This
		 * is set on the callback so that any calls from that thread to the same displayProxy will know that
		 * it is already connected to the UI thread and so syncExec's don't need to get a new DisplayExec and
		 * create a new thread to handle the runnable. Just execute it.
		 * 
		 * The value return from this is the IBeanProxy (for displayProxy) for the current thread for the current
		 * registry (since Constants are stored on a per-registry basis. There can only be one display callback per-thread
		 * per-registry. If another display is desired for an callback thread from a UI call, then a new callback
		 * thread will be required, so we would have a new ThreadLocal value.
		 */
		private ThreadLocal threadInSyncExec = new ThreadLocal();
		
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
			displayType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Display"); //$NON-NLS-1$
		}
		
		public IMethodProxy getDefault() {
			if (getDefaultProxy == null)
				getDefaultProxy = displayType.getMethodProxy("getDefault"); //$NON-NLS-1$
			return getDefaultProxy;
		}
		
		public IMethodProxy getFindDisplay() {
			if (findDisplayProxy == null)
				findDisplayProxy = displayType.getMethodProxy("findDisplay", "java.lang.Thread"); //$NON-NLS-1$ //$NON-NLS-2$
			return findDisplayProxy;
		}	

		public IMethodProxy getBeep() {
			if (beepProxy == null)
				beepProxy = displayType.getMethodProxy("beep"); //$NON-NLS-1$
			return beepProxy;
		}
		
		public IMethodProxy getActiveShell() {
			if (activeShellProxy == null)
				activeShellProxy = displayType.getMethodProxy("activeShell"); //$NON-NLS-1$
			return activeShellProxy;
		}	
		
		public IMethodProxy getBounds() {
			if (getBoundsProxy == null)
				getBoundsProxy = displayType.getMethodProxy("getBounds"); //$NON-NLS-1$
			return getBoundsProxy;
		}
		
		public IMethodProxy getClientArea() {
			if (getClientAreaProxy == null)
				getClientAreaProxy = displayType.getMethodProxy("getClientArea"); //$NON-NLS-1$
			return getClientAreaProxy;
		}
		
		public IMethodProxy getUpdate() {
			if (updateProxy == null)
				updateProxy = displayType.getMethodProxy("update"); //$NON-NLS-1$
			return updateProxy;
		}		
		
		private IBeanTypeProxy getDisplayExecType() {
			if (displayExecType == null)
				displayExecType = displayType.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.DisplayExec"); //$NON-NLS-1$
			return displayExecType;
		}

		public IMethodProxy getDisplayExecAsyncExec() {
			if (displayExecAsyncExec == null)
				displayExecAsyncExec = getDisplayExecType().getMethodProxy("asyncExec", "org.eclipse.swt.widgets.Display"); //$NON-NLS-1$ //$NON-NLS-2$
			return displayExecAsyncExec;
		}
		
		public IMethodProxy getDisplayExecSyncExec() {
			if (displayExecSyncExec == null)
				displayExecSyncExec = getDisplayExecType().getMethodProxy("syncExec", "org.eclipse.swt.widgets.Display"); //$NON-NLS-1$ //$NON-NLS-2$
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
		
		/*
		 * Get the Display proxy for the current thread and registry (since this Constants instance is per-registry).
		 * The Display proxy will only be set on a syncExec callback. This is so that we know that we are in a syncExec
		 * for the given display.
		 * @return displayProxy for the current thread if in a syncExec callback, or <code>null</code> if not in a callback.
		 * 
		 * @since 1.0.0
		 */
		public IBeanProxy getTheadSyncDisplay() {
			return (IBeanProxy) threadInSyncExec.get();
		}
		
		/*
		 * Set the Display proxy for the current thread. 
		 * @param displayProxy the display proxy to use for current thread, or <code>null</code> if no longer in callback.
		 * 
		 * @since 1.0.0
		 * @see DisplayManager#getThreadSyncDisplay
		 */
		public void setThreadSyncDisplay(IBeanProxy displayProxy) {
			threadInSyncExec.set(displayProxy);
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
	 * Return the current display. If current thread is executing within an UI callback, then this is the display that the callback is working with.
	 * If not currently within a callback then this will be <code>null</code>.
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IBeanProxy getCurrentDisplay(ProxyFactoryRegistry registry) {
		return Constants.getConstants(registry).getTheadSyncDisplay();
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
	 * 
	 * @param registry
	 * @param runnable
	 * @return result of the runnable
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 * @see DisplayManager#syncExec(IBeanProxy, DisplayRunnable)
	 * @see DisplayManager.DisplayRunnable.RunnableException
	 */
	public static Object syncExec(ProxyFactoryRegistry registry, DisplayRunnable runnable) throws ThrowableProxy, RunnableException {
		return syncExec(null, runnable, registry);
	}
	
	
	/**
	 * Do a syncExec on the given display and using the given runnable.
	 * 
	 * @param displayProxy The display to syncExec onto. It must be set, it cannot be <code>null</code>.
	 * @param runnable
	 * @return result of the runnable. It will be either a IBeanProxy, IBeanProxy[], or <code>null</code>.
	 * @throws ThrowableProxy if there was an exception thrown on the remote vm.
	 * @throws RunnableException if there was expected type of exception on this side in the runnable.
	 * 
	 * @since 1.0.0
	 * @see DisplayManager.DisplayRunnable.RunnableException
	 */
	public static Object syncExec(IBeanProxy displayProxy, DisplayRunnable runnable) throws ThrowableProxy, RunnableException {
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
	protected static Object syncExec(IBeanProxy displayProxy, DisplayRunnable runnable, ProxyFactoryRegistry registry) throws ThrowableProxy, DisplayRunnable.RunnableException {
		Constants constants = Constants.getConstants(registry);
		IBeanProxy inSyncProxy = constants.getTheadSyncDisplay();
		if (inSyncProxy != null && displayProxy == null) 
			displayProxy = getDefault(registry);	// We must get the default because we are currently within a UI callback for this thread and we MUST verify if different display.
		if (inSyncProxy == null || inSyncProxy != displayProxy) {
			// Either we are not within a current display UI callback, or we are, but the display we want is a different one, then need a new callback.
			IBeanProxy displayExecProxy = constants.getFreeDisplayExec();
			try {
				runnable.setDisplayExecProxy(displayExecProxy);
				registry.getCallbackRegistry().registerCallback(displayExecProxy, runnable);
				Object result = constants.getDisplayExecSyncExec().invoke(displayExecProxy, displayProxy);
				displayExecProxy = null; // Ended well, so runnable took care of clean up.
				if (result instanceof ThrowableProxy)
					throw (ThrowableProxy) result;
				else if (runnable.getException() instanceof DisplayRunnable.RunnableRuntimeException)
					throw (RuntimeException) runnable.getException().getCause();	// Runnable runtime exception occurred.
				else if (runnable.getException() != null)
					throw (DisplayRunnable.RunnableException) runnable.getException();	// Runnable exception occurred.
				else
					return runnable.getRunnableReturn();
			} finally {
				if (displayExecProxy != null) {
					registry.getCallbackRegistry().deregisterCallback(displayExecProxy);
					constants.returnDisplayExec(displayExecProxy);
				}
			}
		} else {
			// We are in the UI callback for the same display, so just execute the runnable.
			try {
				return runnable.run(displayProxy);
			} catch (ThrowableProxy e) {
				throw e;
			}
		}
	}
	
	/**
	 * Do an asyncExec on the default display. It will return immediately.
	 * 
	 * @param registry
	 * @param runnable <b>Note:</b> Do not use a ExpressionDisplayRunnable. Expressions cannot be used in asyncExecs.
	 * @throws ThrowableProxy
	 * 
	 * @since 1.0.0
	 * @see DisplayManager#syncExec(IBeanProxy, DisplayRunnable)
	 */
	public static void asyncExec(ProxyFactoryRegistry registry, DisplayRunnable runnable) throws ThrowableProxy {
		asyncExec(null, runnable, registry);
	}
	
	
	/**
	 * Do an asyncExec on the given display and using the given runnable. It will return immediately.
	 * 
	 * @param displayProxy The display to asyncExec onto. It must be set, it cannot be <code>null</code>.
	 * @param runnable <b>Note:</b> Do not use a ExpressionDisplayRunnable. Expressions cannot be used in asyncExecs.
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
		// Note: We aren't testing if currently under sync exec because these are ALWAYS to be farmed off to 
		// next time UI thread processes the queue. Since we don't know when that is, we need to have a
		// displayExec proxy around to actually process it.
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
