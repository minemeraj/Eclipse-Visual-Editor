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
 *  $RCSfile: JavaBeansLauncher.java,v $
 *  $Revision: 1.2 $  $Date: 2004-05-18 13:56:08 $ 
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
/**
 * This class is designed to help test VCE generated JavaBeans
 * This launcher will run the main method, if available.  Otherwise it delegates the launching to
 * the ILauncher classes passed in via the "vce.launchers" system property, (comma seperated).
 * If none of the ILauncher classes support launching of the given class, the default case is simply
 * calling the bean's null constructor.
 */
public class JavaBeansLauncher {
	
public static void main(String[] args){

	String nameOfClassToLaunch = System.getProperty("vce.launcher.class"); //$NON-NLS-1$
	
	// Set the locale
	String localeArg = System.getProperty("locale"); //$NON-NLS-1$
	if ( localeArg != null && !(localeArg.equals(""))){ //$NON-NLS-1$
		// We must tokenize the locale to create one with the three arg constructor
		StringTokenizer tokenizer = new StringTokenizer(localeArg,"_"); //$NON-NLS-1$
		String language = ""; //$NON-NLS-1$
		String country = ""; //$NON-NLS-1$
		String variant = ""; //$NON-NLS-1$
		if ( tokenizer.hasMoreElements() ) {
			language = tokenizer.nextToken();
		}
		if ( tokenizer.hasMoreElements() ) {
			country = tokenizer.nextToken();
		}
		if ( tokenizer.hasMoreElements() ) {
			country = tokenizer.nextToken();
		}
		Locale locale = new Locale(language,country,variant);
		Locale.setDefault(locale);
	}

	// Try a number of different ways to launch the JavaBean
	try {	
		Class aClass = Class.forName(nameOfClassToLaunch);		
		Method mainMethod = null;
		try {
			mainMethod = aClass.getDeclaredMethod("main", new Class[] {String[].class}); //$NON-NLS-1$
		} catch (NoSuchMethodException e) {}
		if (mainMethod != null && Modifier.isStatic(mainMethod.getModifiers())) {
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithMain_INFO_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
			// Static method call to the main method
			mainMethod.invoke(null, new Object[]{ args });
		} else {			
			// new up an instance of the java bean
			Constructor ctor = aClass.getDeclaredConstructor(null);
			// Make sure we can intantiate it in case the class it not public
			ctor.setAccessible(true);
			Object javaBean = ctor.newInstance(null);
			
			List launchers = getLaunchers();
			ILauncher selected = null;
			ILauncher current = null;
			Iterator itr = launchers.iterator();
			while(itr.hasNext()) {
				current = (ILauncher)itr.next();
				if (current.supportsLaunching(aClass, javaBean)) {
					selected = current;
					break;
				}
			}
			if (selected != null) {
				selected.launch(aClass, javaBean, args);
			} else {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithNullConstructor_INFO_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
			}
		}
	} catch ( ClassNotFoundException exc ){
		System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.UnableToLoad_ERROR_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
		System.exit(0);
	} catch ( NoSuchMethodException exc ) {
		System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.NoDefaultConstructor_ERROR_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
		System.exit(0);		
	} catch ( InstantiationException exc ) {
		System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InstantiationException_ERROR_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
		exc.printStackTrace();
		System.exit(0);		
	} catch ( InvocationTargetException exc ) {
		System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
		exc.printStackTrace();	
		System.exit(0);			
	} catch ( IllegalAccessException exc ) {
		System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
		exc.printStackTrace();		
		System.exit(0);		
	}
}

private static List getLaunchers() {
	List launchers = new ArrayList();
	
	String launcherprop = System.getProperty("vce.launchers");
	if (launcherprop != null) {
		String[] classes = launcherprop.split(",");
		for (int i = 0; i < classes.length; i++) {
			try {
				Class aClass = Class.forName(classes[i]);
				Constructor ctor = aClass.getDeclaredConstructor(null);
				ctor.setAccessible(true);
				Object launcherObj = ctor.newInstance(null);
				if (launcherObj instanceof ILauncher) {
					launchers.add(launcherObj);
				} else {
					System.err.println("Invalid launcher class " + classes[i]);
				}
			} catch (Exception e) {
				System.err.println("Error creating launcher class " + classes[i] +": " + e.getMessage());
			}
		}
	}
	return launchers;
}
}