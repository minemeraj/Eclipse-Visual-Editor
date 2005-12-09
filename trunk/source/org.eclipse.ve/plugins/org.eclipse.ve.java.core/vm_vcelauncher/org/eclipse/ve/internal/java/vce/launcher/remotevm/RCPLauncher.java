/*******************************************************************************
 * Copyright (c) 2001,2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RCPLauncher.java,v $
 *  $Revision: 1.13 $  $Date: 2005-12-09 21:55:57 $ 
 */

package org.eclipse.ve.internal.java.vce.launcher.remotevm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.WorkbenchPart;

public class RCPLauncher implements ILauncher {

	private String viewName;
	private String iconPath;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#supportsLaunching(java.lang.Class, java.lang.Object)
	 */
	public boolean supportsLaunching(Class clazz) {
		return WorkbenchPart.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#launch(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
	public void launch(Class clazz, String[] args) {

		Display display = Display.getDefault();
		Object javaBean = null;
		
		try {
			
			// setup the jface color preferences for this launch
			setupColorPreferences(display);
			setupFontPreferences(display);
			
			// Set up the view/editorPart title and icon
			viewName = System.getProperty("rcp.launcher.viewName"); //$NON-NLS-1$		
			iconPath = System.getProperty("rcp.launcher.iconPath"); //$NON-NLS-1$		
			
			// new up an instance of the java bean
			Constructor ctor = clazz.getDeclaredConstructor(null);
			// Make sure we can intantiate it in case the class it not public
			ctor.setAccessible(true);
			javaBean = ctor.newInstance(null);
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.ViewPartHost_INFO_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
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
		
		// Get the RCP View Tab Preferences
		int fTabPosition = 0;
		boolean fTraditionalTabs = false;
		
		try {
			fTabPosition = Integer.parseInt(System.getProperty("rcp.launcher.tabPosition")); //$NON-NLS-1$
			if( "true".equalsIgnoreCase(System.getProperty("rcp.launcher.traditionalTabs"))){ //$NON-NLS-1$ //$NON-NLS-2$
				fTraditionalTabs = true;
			}
		} catch (NumberFormatException e1){
			e1.printStackTrace();	
			System.exit(0);	
		} catch (SecurityException e1){
			e1.printStackTrace();	
			System.exit(0);			
		} catch (IllegalArgumentException e1){
			e1.printStackTrace();	
			System.exit(0);
		} catch (NullPointerException e1){
			e1.printStackTrace();
			System.exit(0);
		}
		
		WorkbenchPart workbenchPart = (WorkbenchPart) javaBean;
		ViewPartHost viewPartHost = new ViewPartHost();
		String className = workbenchPart.getClass().getName();
		if(className.indexOf(".") != -1){ //$NON-NLS-1$
			className = className.substring(className.lastIndexOf(".") + 1); //$NON-NLS-1$
		}
		viewPartHost.setDetails(fTraditionalTabs, fTabPosition, clazz.getName());
		// The arguments are the workbench part, the title and the icon path
		String title = viewName == null ? className : viewName; 
		viewPartHost.addViewPart(workbenchPart, title, iconPath);
		runEventLoop((Shell)viewPartHost.getWorkbenchShell());
		
	}
	
	private RGB stringToRGB(String str) {

		str = str.substring(str.indexOf("{") + 1, str.indexOf("}")); //$NON-NLS-1$ //$NON-NLS-2$
		StringTokenizer tokens = new StringTokenizer(str, ","); //$NON-NLS-1$
		
		int red = 0;
		int green = 0;
		int blue = 0;

		try{
			red = Integer.parseInt(tokens.nextToken().trim());
			green = Integer.parseInt(tokens.nextToken().trim());
			blue = Integer.parseInt(tokens.nextToken().trim());
		} catch(Exception e){
			return null;
		}
		
		return new RGB(red, green, blue);
	}
	
	private void setupColorPreferences(Display display){
		
		String activeLinkPref = ""; //$NON-NLS-1$
		String errorPref = ""; //$NON-NLS-1$
		String linkPref = ""; //$NON-NLS-1$
		
		RGB activeLinkRGB = null;
		RGB errorRGB = null;
		RGB linkRGB = null;
		
		try{
			activeLinkPref = System.getProperty("rcp.launcher.activeLink"); //$NON-NLS-1$
			errorPref = System.getProperty("rcp.launcher.error"); //$NON-NLS-1$
			linkPref = System.getProperty("rcp.launcher.link"); //$NON-NLS-1$
			
			activeLinkRGB = stringToRGB(activeLinkPref);
			errorRGB = stringToRGB(errorPref);
			linkRGB = stringToRGB(linkPref);
			
			if(activeLinkRGB != null)
				JFaceResources.getColorRegistry().put(JFacePreferences.ACTIVE_HYPERLINK_COLOR, activeLinkRGB);
			if(errorRGB != null)
				JFaceResources.getColorRegistry().put(JFacePreferences.ERROR_COLOR, errorRGB);
			if(linkRGB != null)
				JFaceResources.getColorRegistry().put(JFacePreferences.HYPERLINK_COLOR, linkRGB);
			
			display.update();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void setupFontPreferences(Display display){
		
		String defaultFont = ""; //$NON-NLS-1$
		String dialogFont = ""; //$NON-NLS-1$
		String bannerFont = ""; //$NON-NLS-1$
		String headerFont = ""; //$NON-NLS-1$
		String textFont = ""; //$NON-NLS-1$
		
		try{
			defaultFont = System.getProperty("rcp.launcher.defaultFont"); //$NON-NLS-1$
			dialogFont = System.getProperty("rcp.launcher.dialogFont"); //$NON-NLS-1$
			bannerFont = System.getProperty("rcp.launcher.bannerFont"); //$NON-NLS-1$
			headerFont = System.getProperty("rcp.launcher.headerFont"); //$NON-NLS-1$
			textFont = System.getProperty("rcp.launcher.textFont"); //$NON-NLS-1$
			
			FontData[] defaultFontData = { new FontData(defaultFont) };
			FontData[] dialogFontData = { new FontData(dialogFont) };
			FontData[] bannerFontData = { new FontData(bannerFont) };
			FontData[] headerFontData = { new FontData(headerFont) };
			FontData[] textFontData = { new FontData(textFont) };
			
			if(defaultFontData != null)
				JFaceResources.getFontRegistry().put(JFaceResources.DEFAULT_FONT, defaultFontData);
			if(dialogFontData != null)
				JFaceResources.getFontRegistry().put(JFaceResources.DIALOG_FONT, dialogFontData);
			if(bannerFontData != null)
				JFaceResources.getFontRegistry().put(JFaceResources.BANNER_FONT, bannerFontData);
			if(headerFontData != null)
				JFaceResources.getFontRegistry().put(JFaceResources.HEADER_FONT, headerFontData);
			if(textFontData != null)
				JFaceResources.getFontRegistry().put(JFaceResources.TEXT_FONT, textFontData);
			
			display.update();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	protected void runEventLoop(Shell beanShell) {
		Display display = beanShell.getDisplay();
		beanShell.pack();
		beanShell.open();
		
		try{
			while (!beanShell.isDisposed()) {
				try{
					if (!display.readAndDispatch()) display.sleep ();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
			
		display.dispose();

	}
}
