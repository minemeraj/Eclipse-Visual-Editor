/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTLauncher.java,v $
 *  $Revision: 1.1 $  $Date: 2004-05-12 15:58:26 $ 
 */
package org.eclipse.ve.internal.java.vce.launcher.remotevm;

import java.lang.reflect.*;
import java.text.MessageFormat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
 

/**
 * 
 * @since 1.0.0
 */
public class SWTLauncher {

	public static void main(String[] args) {
		
		String nameOfClassToLaunch = System.getProperty("vce.launcher.class"); //$NON-NLS-1$
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
				
				// Look for a shell to mock up.
				Field shellField = null;
				Method createMethod = null;
				String methodName = null;
				Field[] fields = aClass.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					if (Shell.class.isAssignableFrom(fields[i].getType())) {
						
						// get the name of the create method: field sShell -> createSShell
						StringBuffer nameBuff = new StringBuffer(fields[i].getName());
						nameBuff.setCharAt(0, Character.toUpperCase(nameBuff.charAt(0)));
						nameBuff.insert(0, "create"); //$NON-NLS-1$
						
						methodName = nameBuff.toString();
						try {
							createMethod = aClass.getDeclaredMethod(methodName, null);
						} catch (NoSuchMethodException e) {}
						if (createMethod != null) {
							shellField = fields[i];
							break;
						}
					}
				}
				if (createMethod != null) {
					shellField.setAccessible(true);
					createMethod.setAccessible(true);
					
					System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithShellMethod_INFO_"), new Object[]{nameOfClassToLaunch, methodName})); //$NON-NLS-1$
					
					createMethod.invoke(javaBean, null);
					Shell beanShell = (Shell)shellField.get(javaBean);
					runEventLoop(beanShell);
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
	
	protected static void runEventLoop(Shell beanShell) {
		Display display = beanShell.getDisplay();
		
		// Size the shell based on the configuration settings
		String pack = System.getProperty("pack"); //$NON-NLS-1$
		if ( pack != null && pack.equals("true")){ //$NON-NLS-1$
			beanShell.pack();
		}
		
		beanShell.open();
		
		while (!beanShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep ();
		}
		display.dispose();	
	}
}
