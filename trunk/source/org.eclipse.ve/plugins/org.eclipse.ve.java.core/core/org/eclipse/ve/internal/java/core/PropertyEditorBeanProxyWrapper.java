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
 *  $RCSfile: PropertyEditorBeanProxyWrapper.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.logging.Level;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.common.Common;

public class PropertyEditorBeanProxyWrapper {

	protected IBeanProxy propertyEditorProxy;
	protected IMethodProxy fGetTagsMethodProxy;
	protected IMethodProxy fSetAsTextMethodProxy;
	protected IMethodProxy fGetAsTextMethodProxy;
	protected IMethodProxy fGetValueMethodProxy;
	protected IMethodProxy fSetValueMethodProxy;
	protected IMethodProxy fGetJavaInitializationStringMethodProxy;
	protected IMethodProxy fSupportsCustomEditorMethodProxy;
	protected IMethodProxy fGetCustomEditorMethodProxy;	
	protected IBeanTypeProxy fStringBeanTypeProxy;
	
public PropertyEditorBeanProxyWrapper(IBeanProxy aJavaPropertyEditorProxy){
	propertyEditorProxy = aJavaPropertyEditorProxy;
}

public void dispose() {
	// Get rid of the IBeanProxy.
	if (propertyEditorProxy != null) {
		ProxyFactoryRegistry reg = propertyEditorProxy.getProxyFactoryRegistry();
		reg.releaseProxy(propertyEditorProxy);
		propertyEditorProxy = null;
	}
}

public String getAsText(){
	if ( propertyEditorProxy == null ) return ""; //$NON-NLS-1$
	if ( fGetAsTextMethodProxy == null ){
		fGetAsTextMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("getAsText"); //$NON-NLS-1$
	}
	try {
		IStringBeanProxy textProxy = (IStringBeanProxy)fGetAsTextMethodProxy.invoke(propertyEditorProxy);
		if ( textProxy == null ) {
			return ""; //$NON-NLS-1$
		} else {
			return textProxy.stringValue();	
		}
	} catch ( ThrowableProxy exc ){
		exc.printProxyStackTrace();
		return "ERROR - " + exc.getMessage(); //$NON-NLS-1$
	}
}
public boolean supportsCustomEditor(){
	if ( propertyEditorProxy == null ) return false;
	if ( fSupportsCustomEditorMethodProxy == null ) {
		fSupportsCustomEditorMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("supportsCustomEditor"); //$NON-NLS-1$
	}	
	try { 
		return ((IBooleanBeanProxy)fSupportsCustomEditorMethodProxy.invoke(propertyEditorProxy)).booleanValue();
	} catch ( ThrowableProxy exc ) {
		exc.printProxyStackTrace();
		return false;
	}
}
public String[] getTags(){
	if ( propertyEditorProxy == null ) return null;	
	if ( fGetTagsMethodProxy == null ){
		fGetTagsMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("getTags"); //$NON-NLS-1$
	};
	try {
		IArrayBeanProxy tagsProxy = (IArrayBeanProxy)fGetTagsMethodProxy.invoke(propertyEditorProxy);
		// If there are no tags then return null
		if ( tagsProxy == null ) return null;
		int numberOfTags = tagsProxy.getLength();
		String[] tags = new String[numberOfTags];
		for (int i=0 ; i < numberOfTags ; i++){
			IStringBeanProxy tag = (IStringBeanProxy)tagsProxy.get(i);
			tags[i] = tag.stringValue();
		};
		return tags;
	} catch ( ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return null;
	}
}
public IBeanProxy getCustomEditor(){
	if ( fGetCustomEditorMethodProxy == null ) {
		fGetCustomEditorMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("getCustomEditor"); //$NON-NLS-1$
	}
	return fGetCustomEditorMethodProxy.invokeCatchThrowableExceptions(propertyEditorProxy);
}
public IBeanProxy getValue(){
	if ( propertyEditorProxy == null ) return null;
	if ( fGetValueMethodProxy == null ) {
		fGetValueMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("getValue"); //$NON-NLS-1$
	}
	return fGetValueMethodProxy.invokeCatchThrowableExceptions(propertyEditorProxy);
}

public void setAsText(String aString){
	if ( fSetAsTextMethodProxy == null ) {
		fSetAsTextMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("setAsText" , "java.lang.String"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	try {
		IStringBeanProxy textProxy = propertyEditorProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(aString);
		fSetAsTextMethodProxy.invoke(propertyEditorProxy,textProxy);
	} catch ( ThrowableProxy exc ) {
		// Rethrow this an an IllegalArgumentException with the string of the original exception
		IStringBeanProxy messageProxy = BeanProxyUtilities.invoke_getMessage(exc);
		throw new IllegalArgumentException(messageProxy.toBeanString());		
	}
}

public void setValue(IBeanProxy anObjectProxy){
	if ( propertyEditorProxy == null ) return;
	if ( fSetValueMethodProxy == null ) {
		fSetValueMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("setValue" , "java.lang.Object"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	fSetValueMethodProxy.invokeCatchThrowableExceptions(propertyEditorProxy,anObjectProxy);
}
public String getJavaInitializationString(){
	if ( propertyEditorProxy == null ) return "???"; //$NON-NLS-1$
	if ( fGetJavaInitializationStringMethodProxy==null ) {
		fGetJavaInitializationStringMethodProxy = propertyEditorProxy.getTypeProxy().getMethodProxy("getJavaInitializationString"); //$NON-NLS-1$
	}
	try {
		IStringBeanProxy initStringProxy = (IStringBeanProxy)fGetJavaInitializationStringMethodProxy.invoke(propertyEditorProxy);
		return initStringProxy.stringValue();
	} catch ( ThrowableProxy exc ) {
		JavaVEPlugin.log(exc, Level.SEVERE);
		return "???"; //$NON-NLS-1$
	}
}
public ImageData getImageData(int width, int height){
	// Return the value of painting the awt graphics into an SWT image data from the remote VM
	// TODO Needs writing"); //$NON-NLS-1$
	return null;
}
public boolean isPaintable(){
	// TODO Need to go to the proxy to get the correct answer"); //$NON-NLS-1$
	return false;
}
/**
 * Open a custom editor on the target VM
 */
public int launchCustomEditor(Shell parentShell) {

	int state = Common.DLG_CANCEL;
	
	IBeanTypeProxy windowLauncherType = propertyEditorProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.WindowLauncher"); //$NON-NLS-1$
	IBeanProxy windowLauncherProxy = null;
	WindowLauncher windowLauncher = null;
	IBeanProxy customEditor = null;
	try {
		customEditor = getCustomEditor();		
		if (customEditor == null)
			return state;	// It didn't return an editor, so we can't launch.
		// Create the launcher with the constructor of the component.  The launcher will select the appropiate 
		// environment to open the component in, e.g. a Swing shell with OK / Cancel buttons for Swing,
		// an AWT one for AWT, and other ones for components that are themselves shells
		IConstructorProxy ctor = windowLauncherType.getConstructorProxy(new String[] {"java.awt.Component"}); //$NON-NLS-1$
		windowLauncherProxy = ctor.newInstance(new IBeanProxy[] {customEditor});
		// Now we have a dialog open on the target VM we need to listen to when 
		// it is closed and spin in a tight loop as long as it is open
		windowLauncher = new WindowLauncher(windowLauncherProxy,parentShell);
		windowLauncher.waitUntilWindowCloses();	
		state = windowLauncher.getWindowState();
	} catch ( ThrowableProxy exc ){
		JavaVEPlugin.log(exc, Level.WARNING);
	} finally {
		if (windowLauncher != null)
			windowLauncher.dispose();
	}
	
	return state;		
}
}
