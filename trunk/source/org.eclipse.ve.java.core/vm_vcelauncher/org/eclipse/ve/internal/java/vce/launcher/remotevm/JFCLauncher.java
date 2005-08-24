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
package org.eclipse.ve.internal.java.vce.launcher.remotevm;
/*
 *  $RCSfile: JFCLauncher.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/**
 * This class is designed to help test VCE generated JavaBeans
 * This launcher is specific to launching JFC Java Beans.
 */
public class JFCLauncher implements ILauncher {
	
	public static Point OFF_SCREEN = new Point(-10000,-10000);
	public static Point ON_SCREEN = new Point(0,0);
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#supportsLaunching(java.lang.Class, java.lang.Object)
	 */
	public boolean supportsLaunching(Class clazz) {
		return (JComponent.class.isAssignableFrom(clazz) || Component.class.isAssignableFrom(clazz) || Window.class.isAssignableFrom(clazz) || Applet.class.isAssignableFrom(clazz));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#launch(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
		
	public void launch(Class clazz, String[] args) {
		Object javaBean = null;
		try {
			// new up an instance of the java bean
			Constructor ctor = clazz.getDeclaredConstructor(null);
			// Make sure we can intantiate it in case the class it not public
			ctor.setAccessible(true);
			javaBean = ctor.newInstance(null);
		} catch (SecurityException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		} catch (IllegalArgumentException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();		
			System.exit(0);	
		} catch (NoSuchMethodException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		} catch (InstantiationException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		} catch (IllegalAccessException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();		
			System.exit(0);	
		} catch (InvocationTargetException e1) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
			e1.printStackTrace();	
			System.exit(0);	
		}
		
		// Set the look and feel if required	
		String lookAndFeelArg = System.getProperty("vce.launcher.lookandfeel"); //$NON-NLS-1$
		// <default> means don't change the L&F from the one the JVM has used
		if (lookAndFeelArg != null && !(lookAndFeelArg.equals(""))){ //$NON-NLS-1$
			try { 
				UIManager.setLookAndFeel(lookAndFeelArg);
				SwingUtilities.updateComponentTreeUI((Component) javaBean);
			} catch ( Exception exc ) {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.SettingLookAndFeel_ERROR_"), new Object[] {lookAndFeelArg})); //$NON-NLS-1$
				exc.printStackTrace();
			}
		}
		
		// If the JavaBean is an instance of a Frame then we should make it visible
		if ( javaBean instanceof Applet ) {
			String projectURL = System.getProperty("vce.launcher.projecturl"); //$NON-NLS-1$
			launchApplet(clazz, projectURL);
		} else if ( javaBean instanceof Window ) {
			launchWindow((Window)javaBean,(Component)javaBean,((Component)javaBean).getSize());
		} else if ( javaBean instanceof JComponent ) {
			launchJComponent((JComponent)javaBean);
		} else if ( javaBean instanceof Component ) {
			launchComponent((Component)javaBean);
		}
	}
	
protected static void launchJComponent(JComponent aJComponent){
	JFrame frame = new JFrame(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.FrameTitle.LaunchJComponent"), new Object[]{aJComponent.getClass().getName()})); //$NON-NLS-1$
	Dimension aComponentSize = aJComponent.getSize();
	frame.getContentPane().add(aJComponent);
	launchWindow(frame,aJComponent,aComponentSize);
}
protected static void launchWindow(Window aWindow,Component aComponent, Dimension aComponentSize){
	if ( aWindow instanceof JFrame) {
		((JFrame)aWindow).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} else if ( aWindow instanceof JDialog) {	
		((JDialog)aWindow).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	
	// Make sure when the window is closing we exit
	aWindow.addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent anEvent){
			System.exit(0);
		}
	});
	aWindow.setLocation(OFF_SCREEN);
	aWindow.setVisible(true);	
	sizeWindow(aWindow,aComponent,aComponentSize);	
	aWindow.setLocation(ON_SCREEN);
	aWindow.validate();
}
protected static void launchComponent(Component aComponent){
	Frame frame = new Frame(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.FrameTitle.LaunchComponent"), new Object[]{aComponent.getClass().getName()})); //$NON-NLS-1$
	// After adding the component to the frame its size is changed.  We must get the size on construction
	// so that the overall window can be sized correctly
	Dimension aComponentSize = aComponent.getSize();
	frame.add(aComponent);
	launchWindow(frame,aComponent,aComponentSize);
}
protected static void sizeWindow(Window aWindow, Component aComponent, Dimension componentSize){
	// Size the frame based on the configuration settings
	String pack = System.getProperty("pack"); //$NON-NLS-1$
	if ( pack != null && pack.equals("true")){ //$NON-NLS-1$
		aWindow.pack();			
	} else if ( aComponent == aWindow ){
		// If the window is the component then don't do anything
	} else {
		// For AWT and Swing we have to get the inset of the window ( that is the border and the title
		// bar and adjust by these )
		Insets insets = aWindow.getInsets();		
		componentSize.width += insets.left + insets.right;
		componentSize.height += insets.top + insets.bottom;
		aWindow.setSize(componentSize);
	}
}
protected static void launchApplet(Class anAppletClass, String projectLocation){
	
	// Get the applet parms - first see how many there are
	String numberOfParmsString = System.getProperty("appletparmsnumber"); //$NON-NLS-1$
	Map appletParms = new HashMap();	
	// The parms come in names and values separately, e.g.
	// -Dappletparmname0=ABC and -Dappletparmvalue=DEF
	if ( numberOfParmsString != null ) {
		int numberOfParms = Integer.parseInt(numberOfParmsString);
		for ( int i=1 ; i<=numberOfParms ; i++ ){
			String name = System.getProperty("appletparmname" + i); //$NON-NLS-1$
			String value = System.getProperty("appletparmvalue" + i); //$NON-NLS-1$
			appletParms.put(name,value);
		}
	}
	
	// Concatenate the project location and applet class name 
	Object object = null;
	try {
		Constructor ctor = anAppletClass.getDeclaredConstructor(null);
		ctor.setAccessible(true);
		object = ctor.newInstance(null);
	} catch ( Exception exc ) {
		exc.printStackTrace();
	}
	final Applet applet = (Applet)object;
	Frame frame = null;
	if ( applet instanceof JApplet ) {
		frame = new JAppletFrame(VCELauncherMessages.getString("BeansLauncher.FrameTitle.LaunchApplet"),(JApplet) applet,appletParms); //$NON-NLS-1$
		((JFrame)frame).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} else {
		frame = new AppletFrame (VCELauncherMessages.getString("BeansLauncher.FrameTitle.LaunchApplet"),applet,appletParms); //$NON-NLS-1$
	    frame.addWindowListener (new WindowAdapter(){
			public void windowClosing (WindowEvent event){
				applet.stop();
				applet.destroy();
				System.exit(0);
			}
		});
	}

	// This needs though to get the applet arguments into the running applet
	// that are specified in the IDE and come across as VM arguments
	applet.init();
	Dimension aComponentSize = applet.getSize();	
	frame.setLocation(OFF_SCREEN);
	frame.setVisible(true);
	Dimension additionalSize = ((IAppletFrame)frame).getAdditionalSize();
	if (additionalSize != null) {
		aComponentSize.height += additionalSize.height;
		if (aComponentSize.width < additionalSize.width) {
			aComponentSize.width = additionalSize.width;
		}
	}
	if ( aComponentSize.width == 0 && aComponentSize.height == 0 ) {
		aComponentSize = new Dimension(250,250);
	}
	sizeWindow(frame,applet,aComponentSize);	
	frame.setLocation(ON_SCREEN);
	// Start the applet
	applet.start();			
	System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.AppletStarted_INFO_"), new Object[]{anAppletClass.getName()}));	 //$NON-NLS-1$
	// To get the applet launcher to show the components we must do a invalidate and layout correctly
	frame.validate();
}
}
