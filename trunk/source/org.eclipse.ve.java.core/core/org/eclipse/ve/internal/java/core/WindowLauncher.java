/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: WindowLauncher.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:46 $ 
 */

import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.common.Common;

/**
 * A Utility to launch an remote VM WindowLauncher
 * and control its life cycle from open/dispose. The
 * actual proxy is not owned by this utility so it
 * should be released on its own. This will dispose
 * it though to close the window. This is so that
 * the same window can be launched again and again
 */

public class WindowLauncher implements ICallback {
	
	protected Shell fShell;
	protected IBeanProxy fWindowLauncherProxy;
	protected IMethodProxy fIsVisibleMethodProxy;
	protected IMethodProxy fShowMethodProxy;
	protected IMethodProxy fToFrontMethodProxy;
	protected IMethodProxy fHideMethodProxy;
	protected IMethodProxy fGetRemoteWindowState;
	
	public interface Listener{
		void propertyChange(String propertyName);
	};
	protected List fListeners = new ArrayList(1);
	
public WindowLauncher(IBeanProxy aWindowLauncherProxy, Shell aShell){
	fShell = aShell;
	fWindowLauncherProxy = aWindowLauncherProxy;
	fIsVisibleMethodProxy = aWindowLauncherProxy.getTypeProxy().getMethodProxy("isVisible"); //$NON-NLS-1$
	fShowMethodProxy = aWindowLauncherProxy.getTypeProxy().getMethodProxy("show"); //$NON-NLS-1$
	fHideMethodProxy = aWindowLauncherProxy.getTypeProxy().getMethodProxy("hide");	 //$NON-NLS-1$
	fToFrontMethodProxy = aWindowLauncherProxy.getTypeProxy().getMethodProxy("toFront"); //$NON-NLS-1$
	fGetRemoteWindowState = aWindowLauncherProxy.getTypeProxy().getMethodProxy("getWindowState"); //$NON-NLS-1$
	if(fToFrontMethodProxy==null)
		fToFrontMethodProxy = aWindowLauncherProxy.getTypeProxy().getMethodProxy("show"); //$NON-NLS-1$
		
	// Create a callback so that the target VM calls us back when it receives property change
	// events from the window it launches
	aWindowLauncherProxy.getProxyFactoryRegistry().getCallbackRegistry().registerCallback(aWindowLauncherProxy,this);
		
}
/**
 * Spin on a tight loop and wait until the launcher proxy is either
 * not visible or it signals an explicit close
 * ?? Need to add some kind of timeout as well in case of trouble ??
 */
public void waitUntilWindowCloses(){
	//windowState = Common.WIN_OPENED;
	final Display display = fShell.getDisplay();
	// Now listen for activation of the Shell so that if it is activated, focus
	// can be transferred back to the dialog. This way it will emulated modal.
	// Also when Eclipse is iconified (minimized) the AWT dialog will also be minimized, when
	// Eclipse is restored the AWT dialog is restored, etc...
	ShellListener shellListener = new ShellAdapter() {
		public void shellActivated(ShellEvent e) {
			// Invoke later so that focus change can be completed before doing a new one.
			display.asyncExec(new Runnable() {
				public void run() {
					try {
						if (fWindowLauncherProxy != null && ((IBooleanBeanProxy)fIsVisibleMethodProxy.invoke(fWindowLauncherProxy)).booleanValue()) {
							// Still up and running
							fToFrontMethodProxy.invoke(fWindowLauncherProxy);
						}
					} catch ( ThrowableProxy exc ) {
						JavaVEPlugin.log(exc, Level.WARNING);
					}
				}
			});
		}
		public void shellIconified(ShellEvent e) {
			// Awt doesn't have a way to minimize, so we will hide it instead.
			display.asyncExec(new Runnable() {
				public void run() {
					try {
						if (fWindowLauncherProxy != null && ((IBooleanBeanProxy)fIsVisibleMethodProxy.invoke(fWindowLauncherProxy)).booleanValue() ) {
							try {
								// Still up and running
								// We are in try/catch because setVisible throws exception because of SWT
								// when it is running in the same VM
								fHideMethodProxy.invoke(fWindowLauncherProxy);
							} catch (Throwable e) {
							}
						} 
					} catch ( ThrowableProxy exc ) {
						JavaVEPlugin.log(exc, Level.WARNING);
					}
				}
			});
		}
		public void shellDeiconified(ShellEvent e) {
			// Awt doesn't have a way to restore, so we will show it instead.
			display.asyncExec(new Runnable() {
				public void run() {
					try {
						if (fWindowLauncherProxy != null) {
							try {
								// We are in try/catch because setVisible throws exception because of SWT
								// when it is running in the same VM
								fShowMethodProxy.invoke(fWindowLauncherProxy);
							} catch (Throwable e) {
							}
							fToFrontMethodProxy.invoke(fWindowLauncherProxy);
						}
					} catch ( ThrowableProxy exc ) {
						JavaVEPlugin.log(exc, Level.WARNING);
					}
				}
			});
		}
	};
	fShell.addShellListener(shellListener);
	// Now try and suspent the current thread until the AWT window is no longer visible ( i.e. the user has closed it )
	try {
		fShowMethodProxy.invoke(fWindowLauncherProxy);
		// Some times the focus doesn't shift correctly, so we will queue up a shift of
		// focus to it as the first thing that occurs when the dispatch loop runs.
		display.asyncExec(new Runnable() {
			public void run() {
				try {
				fToFrontMethodProxy.invoke(fWindowLauncherProxy);
				} catch ( ThrowableProxy exc ) {
					JavaVEPlugin.log(exc, Level.WARNING);
				}
			}
		});
		// We need to simulate modality by SWT spinning here until the dialog is no
		// longer visible.  Also, disable the parentShell so that changes can't be made to it.
		fShell.setEnabled(false);
		boolean propertyDialogVisible = true;
		int windowState = getWindowState();
		while (propertyDialogVisible && (windowState==Common.WIN_OPENED)) {
			propertyDialogVisible = ((IBooleanBeanProxy)fIsVisibleMethodProxy.invoke(fWindowLauncherProxy)).booleanValue();
			windowState = ((INumberBeanProxy)fGetRemoteWindowState.invoke(fWindowLauncherProxy)).intValue();
			if (!display.readAndDispatch())
				display.sleep();
		}
	} catch ( ThrowableProxy exc ) {
		JavaVEPlugin.log(exc, Level.WARNING);
	} finally {
		// Make sure it gets removed. We don't want it hanging around for any reason.
		fShell.removeShellListener(shellListener);
		fShell.setEnabled(true);
		fShell.forceActive();	// This should force it to the top. Kludge until we get driver which has the force to top in it.
	}
}
public int getWindowState(){
	try{
		return ((INumberBeanProxy)fGetRemoteWindowState.invoke(fWindowLauncherProxy)).intValue();
	}catch(Exception e){
		// Safely return closed. Maybe the removevm died, or something
		// bad happened on the remotevm. This would ensure that there
		// will not be indefinite sleep.
		return Common.WIN_CLOSED;
	}
}
public void addListener(Listener aListener){
	fListeners.add(aListener);
}
public void removeListener(Listener aListener){
	fListeners.remove(aListener);
}
protected void firePropertyChange(String propertyName){
	Iterator iter = fListeners.iterator();
	while(iter.hasNext()){
		Listener listener = (Listener)iter.next();
		listener.propertyChange(propertyName);
	}
}
public Object calledBack(int msgID, IBeanProxy parm){
	if ( msgID == Common.PROP_CHANGED ){
		firePropertyChange(((IStringBeanProxy)parm).stringValue());
	}
	return null;	
}
public Object calledBack(int msgID, Object[] parms){
	return null;
}
public Object calledBack(int msgID, Object parm){
	throw new RuntimeException("A window launcher has been called back incorrectly"); //$NON-NLS-1$
}
public void calledBackStream(int msgID, InputStream is){
	throw new RuntimeException("A window launcher has been called back incorrectly"); //$NON-NLS-1$
}
public void dispose(){
	fWindowLauncherProxy.getProxyFactoryRegistry().getCallbackRegistry().deregisterCallback(fWindowLauncherProxy);
	fWindowLauncherProxy.getProxyFactoryRegistry().releaseProxy(fWindowLauncherProxy);	
	fListeners = null;
	fWindowLauncherProxy = null;
}
}
