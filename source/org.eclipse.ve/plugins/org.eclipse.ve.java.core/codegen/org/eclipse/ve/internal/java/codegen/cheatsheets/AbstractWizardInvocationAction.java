/*
 * Created on May 13, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.cheatsheets;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AbstractWizardInvocationAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

// Wrapper for wizard.

/**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class AbstractWizardInvocationAction extends Action {
	
	public AbstractWizardInvocationAction(){
		super();
	}
	
	public static void invoke(String method, Object[] parameters, Object targetObject){
		Method[] methods = targetObject.getClass().getMethods();
		for(int mc=0;mc<methods.length;mc++){
			Method m = methods[mc];
			if(!m.getName().equals(method))
				continue;
			if(m.getParameterTypes().length!=parameters.length)
				continue;
			try {
				m.invoke(targetObject, parameters);
				return;
			} catch (IllegalArgumentException e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
			} catch (IllegalAccessException e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
			} catch (InvocationTargetException e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
			}
		}
	}
	
	public static Object createInstanceOf(String pluginID, String className){
		try {
			IPluginDescriptor desc = Platform.getPlugin(pluginID).getDescriptor();
			return desc.getPluginClassLoader().loadClass(className).newInstance();
		} catch (InstantiationException e1) {
			JavaVEPlugin.log(e1, MsgLogger.LOG_FINE);
		} catch (IllegalAccessException e1) {
			JavaVEPlugin.log(e1, MsgLogger.LOG_FINE);
		}catch (ClassNotFoundException e1) {
			JavaVEPlugin.log(e1, MsgLogger.LOG_FINE);
		}
		return null;
	}
	// The method the sub class need to implement to return an new instance
	// of the wizard - new wizard instance will always be used.
	// Also - sub class should initizlie the wizard here.
	protected abstract IWizard createWizard();
	
	// This is where additional set - before dialog is created..
	// This is invoked before wizardDialog.create()..
	// Subclass can overwrite to supply additional setup information.
	protected void setUp(IWizard wizard, WizardDialog dialog) {
	}

	// retrieve information from the wizard when done.
	// Sub classes should overwrite this to retrieve information.
	protected void cancelPressed(IWizard wizard) {
	}
	
	//Sub class should overwrite this to retrieve information when ok Pressed
	protected void okPressed(IWizard wizard) {
	}
	

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		/*
		Runnable localRunnable = new Runnable() {
			 public void run() {
			 */
				try {
					IWizard wizard = createWizard();  // don't reuse!
					if (wizard == null) return;
					
					// get a display...
					Shell shell = Display.getCurrent().getActiveShell();
					WizardDialog wizardDialog = new WizardDialog(shell, wizard);
					
					// additional setup - if required.
					setUp(wizard, wizardDialog);
					
					wizardDialog.create();
					wizardDialog.open();
					int ret = wizardDialog.getReturnCode();
					switch (ret) {
						case WizardDialog.CANCEL: {
							cancelPressed(wizard);
							break;
						}
						case WizardDialog.OK : {
							okPressed(wizard);
							break;
						}
					}
							
					
				} catch (Exception e) {
					JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
				}
				/*
			 };
		};
		
		// get the display - to run with a runnable display
		Display display = Display.getDefault();
		BusyIndicator.showWhile(display, localRunnable);
		*/
	}


}
