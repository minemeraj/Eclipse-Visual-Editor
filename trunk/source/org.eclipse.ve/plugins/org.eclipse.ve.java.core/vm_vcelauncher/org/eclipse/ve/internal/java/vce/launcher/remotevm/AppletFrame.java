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
 *  $RCSfile: AppletFrame.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.applet.*;
import java.util.*;

/**
 * Frame to host an applet that allows control over starting, stopping, etc...
 */
public class AppletFrame extends Frame implements AppletContext {
	
	protected Applet fApplet;
	protected Label fLabel;
	protected Map fAppletParms;
	protected AppletStub fAppletStub;

	public AppletFrame(String title,Applet anApplet,Map appletParms) {
		super(title);
		fApplet = anApplet;
		fAppletParms = appletParms;
		fAppletStub = new JavaLauncherAppletStub(appletParms,fApplet, this);
		fApplet.setStub(fAppletStub);
		add(anApplet);
		fLabel = new Label();
		add(fLabel,BorderLayout.SOUTH);
		createMenu();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				fApplet.destroy();
			}
		});
	}
	
	protected void createMenu(){

		MenuBar menuBar = new MenuBar();
		setMenuBar(menuBar);
		Menu appletMenu = new Menu((VCELauncherMessages.getString("BeansLauncher.Menu.Applet")),true); //$NON-NLS-1$
		appletMenu.setShortcut(new MenuShortcut(VCELauncherMessages.getChar("BeansLauncher.Menu.Applet.Shortcut"))); //$NON-NLS-1$
		menuBar.add(appletMenu);
		
		MenuItem startApplet = new MenuItem(VCELauncherMessages.getString("BeansLauncher.Menu.Start"),new MenuShortcut(VCELauncherMessages.getChar("BeansLauncher.Menu.Start.Shortcut"))); //$NON-NLS-1$ //$NON-NLS-2$
		startApplet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fApplet.start();
			}
		});
		appletMenu.add(startApplet);
		
		MenuItem stopApplet = new MenuItem(VCELauncherMessages.getString("BeansLauncher.Menu.Stop"),new MenuShortcut(VCELauncherMessages.getChar("BeansLauncher.Menu.Stop.Shortcut"))); //$NON-NLS-1$ //$NON-NLS-2$
		stopApplet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fApplet.stop();
			}
		});			
		appletMenu.add(stopApplet);
		
	}
	public Applet getApplet(String name) {
		return null;
	}
	public Enumeration getApplets() {
		return null;
	}
	public AudioClip getAudioClip(URL url) {
		return null;
	}
	public Image getImage(URL url) {
		return null;
	}
	public void showDocument(URL url, String target) {
	}
	public void showDocument(URL url) {
	}
	public void showStatus(String status) {
		fLabel.setText(status);
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
