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
 *  $Revision: 1.2 $  $Date: 2004-05-18 13:56:08 $ 
 */
package org.eclipse.ve.internal.java.vce.launcher.remotevm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
 

/**
 * 
 * @since 1.0.0
 */
public class SWTLauncher implements ILauncher {
	
	protected Field shellField = null; 
	protected Method createMethod = null;
	protected String methodName = null;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#supportsLaunching(java.lang.Class, java.lang.Object)
	 */
	public boolean supportsLaunching(Class clazz, Object bean) {
		// Look for a shell to mock up.
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (Shell.class.isAssignableFrom(fields[i].getType())) {
				
				// get the name of the create method: field sShell -> createSShell
				StringBuffer nameBuff = new StringBuffer(fields[i].getName());
				nameBuff.setCharAt(0, Character.toUpperCase(nameBuff.charAt(0)));
				nameBuff.insert(0, "create"); //$NON-NLS-1$
				
				methodName = nameBuff.toString();
				try {
					createMethod = clazz.getDeclaredMethod(methodName, null);
				} catch (NoSuchMethodException e) {}
				if (createMethod != null) {
					shellField = fields[i];
					break;
				}
			}
		}
		return (createMethod != null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.vce.launcher.remotevm.ILauncher#launch(java.lang.Class, java.lang.Object, java.lang.String[])
	 */
	public void launch(Class clazz, Object bean, String[] args) {
		// should already be initialized by supportsLaunching, but be safe.
		if (createMethod == null) {
			if (!supportsLaunching(clazz, bean)) {
				return;
			}
		}
		if (createMethod != null) {
			shellField.setAccessible(true);
			createMethod.setAccessible(true);
			
			System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Msg.BeanWithShellMethod_INFO_"), new Object[]{clazz.getName(), methodName})); //$NON-NLS-1$
			
			try {
				createMethod.invoke(bean, null);
				Shell beanShell = (Shell)shellField.get(bean);
				runEventLoop(beanShell);
			} catch (IllegalArgumentException e) {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
				e.printStackTrace();		
				System.exit(0);	
			} catch (IllegalAccessException e) {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.IllegalAccessException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
				e.printStackTrace();		
				System.exit(0);	
			} catch (InvocationTargetException e) {
				System.out.println(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.Err.InvocationException_ERROR_"), new Object[]{clazz.getName()})); //$NON-NLS-1$
				e.printStackTrace();	
				System.exit(0);	
			}
		}
	}
	
	
	protected void runEventLoop(Shell beanShell) {
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
