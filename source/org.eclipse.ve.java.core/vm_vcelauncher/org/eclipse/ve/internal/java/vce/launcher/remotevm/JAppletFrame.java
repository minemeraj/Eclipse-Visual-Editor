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
 *  $RCSfile: JAppletFrame.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import javax.swing.*;
import java.applet.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.*;
/**
 * Frame to host an applet that allows control over starting, stopping, etc...
 */
public class JAppletFrame extends JFrame implements AppletContext {
	
	protected JApplet fApplet;
	protected Map fAppletParms;
	protected JLabel fLabel;
	protected AppletStub fAppletStub;

	public JAppletFrame(String title, JApplet anApplet,Map appletParms) {
		super(title);
		fApplet = anApplet;
		fAppletStub = new JavaLauncherAppletStub(appletParms,fApplet, this);	
		fApplet.setStub(fAppletStub);			
		fAppletParms = appletParms;
		getContentPane().add(anApplet);
		fLabel = new JLabel();
		getContentPane().add(fLabel,BorderLayout.SOUTH);
		createMenu();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				fApplet.destroy();
			}
		});		
	}
	protected void createMenu(){

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu appletMenu = new JMenu(VCELauncherMessages.getString("BeansLauncher.Menu.Applet")); //$NON-NLS-1$
		appletMenu.setMnemonic(VCELauncherMessages.getChar("BeansLauncher.Menu.Applet.Shortcut")); //$NON-NLS-1$
		menuBar.add(appletMenu);
		appletMenu.getPopupMenu().setLightWeightPopupEnabled(false);	// Need menu to be heavyweight because JApplet is really heavyweight, it doesn't expect to be on a JComponent.
		
		JMenuItem startApplet = new JMenuItem(VCELauncherMessages.getString("BeansLauncher.Menu.Start")); //$NON-NLS-1$
		startApplet.setMnemonic(VCELauncherMessages.getChar("BeansLauncher.Menu.Start.Shortcut")); //$NON-NLS-1$
		appletMenu.add(startApplet);
		startApplet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fApplet.start();
			}
		});
		
		
		JMenuItem stopApplet = new JMenuItem(VCELauncherMessages.getString("BeansLauncher.Menu.Stop")); //$NON-NLS-1$
		stopApplet.setMnemonic(VCELauncherMessages.getChar("BeansLauncher.Menu.Stop.Shortcut")); //$NON-NLS-1$
		appletMenu.add(stopApplet);
		stopApplet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fApplet.stop();
			}
		});			
				
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
