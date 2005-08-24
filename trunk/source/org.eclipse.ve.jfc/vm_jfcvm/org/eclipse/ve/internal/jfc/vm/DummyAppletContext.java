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
 *  $RCSfile: DummyAppletContext.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.applet.*;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.net.URL;
import java.util.*;
import java.util.Collections;
import java.util.Enumeration;
import java.io.*;

/**
 * Dummy Applet Context used when applet not instantiated with one.
 * This is created by DummyAppletStub.applyApplet(...). That is why it is
 * private.
 */
class DummyAppletContext implements AppletContext {

	private Applet targetApplet;

	DummyAppletContext(Applet targetApplet) {
		this.targetApplet = targetApplet;
	}

	/**
	 * @see AppletContext#getAudioClip(URL)
	 */
	public AudioClip getAudioClip(URL url) {
		return null;	// Don't support audio on devtime. 
	}

	private Hashtable images;
	/**
	 * @see AppletContext#getImage(URL)
	 */
	public Image getImage(URL url) {
		// In case applet being tested ever needs an image, see if we can get one.
		if (images == null)
			images = new Hashtable();

		Object image = images.get(url);
		if (image != null) {
			return (Image) image;
		}

		try {
			image = url.getContent();
			if (image == null) {
				return null;
			}
			if (image instanceof Image) {
				images.put(url, image);
				return (Image) image;
			}

			Image img = targetApplet.createImage((ImageProducer) image);
			images.put(url, img);
			return img;

		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @see AppletContext#getApplet(String)
	 */
	public Applet getApplet(String name) {
		return null;
	}

	/**
	 * @see AppletContext#getApplets()
	 */
	public Enumeration getApplets() {
		return Collections.enumeration(Collections.singletonList(targetApplet));
	}

	/**
	 * @see AppletContext#showDocument(URL)
	 */
	public void showDocument(URL url) {
	}

	/**
	 * @see AppletContext#showDocument(URL, String)
	 */
	public void showDocument(URL url, String target) {
	}

	/**
	 * @see AppletContext#showStatus(String)
	 */
	public void showStatus(String status) {
	}
	/* 
	 * Required for 1.4
	 */
	public Iterator getStreamKeys() {
		return null;
	}
	/* 
	 * Required for 1.4
	 */
	public InputStream getStream(String key) {
		return null;
	}
	/* 
	 * Required for 1.4
	 */
	 public void setStream(String key, InputStream stream) throws IOException {		
	}	
}
