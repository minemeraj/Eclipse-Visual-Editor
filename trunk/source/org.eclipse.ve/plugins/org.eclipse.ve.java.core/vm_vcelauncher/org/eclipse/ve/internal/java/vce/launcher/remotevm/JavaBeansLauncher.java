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
 *  $RCSfile: JavaBeansLauncher.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:30:48 $ 
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
		
		List launchers = getLaunchers();
		ILauncher selected = null;
		ILauncher current = null;
		Iterator itr = launchers.iterator();
		while(itr.hasNext()) {
			current = (ILauncher)itr.next();
			if (current.supportsLaunching(aClass)) {
				selected = current;
				break;
			}
		}
		if (selected != null) {			
			selected.launch(aClass, args);
		} else {
			Method mainMethod = null;
			try {
				mainMethod = aClass.getDeclaredMethod("main", new Class[] {String[].class}); //$NON-NLS-1$
			} catch (NoSuchMethodException e) {}
			if (mainMethod != null && Modifier.isStatic(mainMethod.getModifiers())) {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithMain_INFO_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
				// Static method call to the main method
				mainMethod.invoke(null, new Object[]{ args });
			} else {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithNullConstructor_INFO_"), new Object[]{nameOfClassToLaunch})); //$NON-NLS-1$
				// new up an instance of the java bean
				Constructor ctor = aClass.getDeclaredConstructor(null);
				// Make sure we can intantiate it in case the class it not public
				ctor.setAccessible(true);
				ctor.newInstance(null);
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
	
	String launcherprop = System.getProperty("vce.launchers"); //$NON-NLS-1$
	if (launcherprop != null) {
		String[] classes = launcherprop.split(","); //$NON-NLS-1$
		for (int i = 0; i < classes.length; i++) {
			try {
				Class aClass = Class.forName(classes[i]);
				Constructor ctor = aClass.getDeclaredConstructor(null);
				ctor.setAccessible(true);
				Object launcherObj = ctor.newInstance(null);
				if (launcherObj instanceof ILauncher) {
					launchers.add(launcherObj);
				} else {
					System.err.println(MessageFormat.format(VCELauncherMessages.getString("JavaBeansLauncher.InvalidLauncherClass_ERROR_"), new Object[]{classes[i]})); //$NON-NLS-1$
				}
			} catch (Exception e) {
				System.err.println(MessageFormat.format(VCELauncherMessages.getString("JavaBeansLauncher.ErrorCreatingLauncherClass_ERROR_"), new Object[]{classes[i], e.getMessage()})); //$NON-NLS-1$
			}
		}
	}
	return launchers;
}
}
