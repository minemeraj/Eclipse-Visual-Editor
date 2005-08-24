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
package org.eclipse.ve.internal.jfc.vm;
/*
 *  $RCSfile: DummyAppletStub.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.applet.*;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Dummy Applet Stub for when none was provided.
 */

public class DummyAppletStub implements AppletStub {
	
	/**
	 * This will apply a new stub to the applet and initialized it.
	 * Used when Applet in devtime was not instatiated through the Beans.instantiate.
	 */
	public static void initializeApplet(Applet applet) {
		
		// Create a base for it. Use the location of the applet class itself.
		URL docBase = null, codeBase = null;
		
		String classfilename = (applet.getClass().getName().replace('.', '/').concat(".class")); //$NON-NLS-1$
		URL classURL = applet.getClass().getResource('/'+classfilename);
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
		
		AppletStub stub = new DummyAppletStub(new DummyAppletContext(applet), docBase, codeBase);
		applet.setStub(stub);
		
		applet.setSize(100, 100);	// Just give it an initial size so they won't bomb. It will probably be changed later.
		applet.init();
	}
		
	private URL docBase, codeBase;
	private AppletContext context;
	
	private DummyAppletStub(AppletContext context, URL docBase, URL codeBase) {
		this.context = context;
		this.docBase = docBase;
		this.codeBase = codeBase;
	}
		

	/**
	 * @see AppletStub#isActive()
	 */
	public boolean isActive() {
		return true;
	}

	/**
	 * @see AppletStub#getDocumentBase()
	 */
	public URL getDocumentBase() {
		return docBase;
	}

	/**
	 * @see AppletStub#getCodeBase()
	 */
	public URL getCodeBase() {
		return codeBase;
	}

	/**
	 * @see AppletStub#getParameter(String)
	 */
	public String getParameter(String name) {
		return null;
	}

	/**
	 * @see AppletStub#getAppletContext()
	 */
	public AppletContext getAppletContext() {
		return context;
	}

	/**
	 * @see AppletStub#appletResize(int, int)
	 */
	public void appletResize(int width, int height) {
	}

}
