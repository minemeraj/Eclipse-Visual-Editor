package org.eclipse.ve.internal.java.vce.launcher.remotevm;
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
 *  $RCSfile: JavaLauncherAppletStub.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.applet.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class JavaLauncherAppletStub implements AppletStub {

	private Map fProperties;
	private Applet fApplet;
	protected AppletContext fAppletContext;
	protected URL codeBase, docBase;

public JavaLauncherAppletStub(Map properties, Applet a, AppletContext context) {
	fApplet = a;
	fProperties = properties;

	fAppletContext = context;
	String classfilename = (a.getClass().getName().replace('.', '/').concat(".class")); //$NON-NLS-1$
	URL classURL = a.getClass().getResource('/'+classfilename);
	if (classURL != null) {
		try {
			String eForm = classURL.toExternalForm();
			docBase = new URL(eForm.substring(0, eForm.length()-classfilename.length()));	// Up to but package fragment root of the class.
			int lastSlash = eForm.lastIndexOf('/');	// Find where the class file itself is at.
			if (lastSlash >= 0)
				codeBase = new URL(eForm.substring(0, lastSlash+1));	// Up to the class file itself
		} catch(MalformedURLException e) {
		}
	}		
}

public void appletResize (int width, int height) {
	fApplet.resize (width, height);
}
 
public AppletContext getAppletContext () {
	return fAppletContext;
}

public URL getCodeBase() {
	return codeBase;
}
public URL getDocumentBase() {
	return docBase;
}

public String getParameter (String p) {
	return (String)fProperties.get(p);
}
public boolean isActive () {
	return true;
}
}
