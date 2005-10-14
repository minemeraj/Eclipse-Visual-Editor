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
package org.eclipse.ve.internal.java.vce.launcher;
/*
 *  $RCSfile: JavaBeanShortcut.java,v $
 *  $Revision: 1.15 $  $Date: 2005-10-14 17:45:07 $ 
 */
 
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.*;
import org.eclipse.debug.ui.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin.FoundIDs;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

/**
 * Performs single click launching for local java applications.
 */
public class JavaBeanShortcut implements ILaunchShortcut {

	public void searchAndLaunch(Object[] search, String mode) {
		IType[] types = null;
		if (search != null) {			
			try {			
				types = JavaBeanFinder.findTargets(PlatformUI.getWorkbench().getProgressService(), search);
			} catch (InterruptedException e) {
				return;
			} catch (InvocationTargetException e) {
				MessageDialog.openError(getShell(), VCELauncherMessages.Shortcut_ErrDlg_LaunchFailed_Title, e.getMessage()); 
				return;
			}
			IType type = null;
			if (types.length == 0) {
				MessageDialog.openError(getShell(), VCELauncherMessages.Shortcut_ErrDlg_LaunchFailed_Title, VCELauncherMessages.Shortcut_ErrDlg_LaunchFailed_Msg_NoBeanFound_ERROR_); 
			} else if (types.length > 1) {
				type = chooseType(types, mode);
			} else {
				type = types[0];
			}
			if (type != null) {
				launch(type, mode);
			}
		}

	}	

	/**
	 * Prompts the user to select a type
	 * 
	 * @return the selected type or <code>null</code> if none.
	 */
	protected IType chooseType(IType[] types, String mode) {
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider());
		dialog.setElements(types);
		dialog.setTitle(VCELauncherMessages.Shortcut_TypeDlg_Title); 
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			dialog.setMessage(VCELauncherMessages.Shortcut_TypeDlg_ChooseDebugType); 
		} else {
			dialog.setMessage(VCELauncherMessages.Shortcut_TypeDlg_ChooseRunType); 
		}
		dialog.setMultipleSelection(false);
		if (dialog.open() == Window.OK) {
			return (IType)dialog.getFirstResult();
		}
		return null;
	}
	
	/**
	 * Launches a configuration for the given type
	 */
	protected void launch(IType type, String mode) {
		try { 
			ILaunchConfiguration config = findLaunchConfiguration(type, mode);
			if (config != null) {
				DebugUITools.saveAndBuildBeforeLaunch();
				config.launch(mode, null);
			}			
		} catch (CoreException e) {
			Shell shell = getShell();
			if (shell != null) {
				ErrorDialog.openError(shell, VCELauncherMessages.ErrorDialog_Title, VCELauncherMessages.Shortcut_ErrDlg_Msg_LaunchFailed_ERROR_, e.getStatus());  
			}
		}
	}
	
	protected Shell getShell() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null)
			return window.getShell();
		return null;
	}
	
	/**
	 * Locate a configuration to relaunch for the given type.  If one cannot be found, create one.
	 * 
	 * @return a re-useable config or <code>null</code> if none
	 */
	protected ILaunchConfiguration findLaunchConfiguration(IType type, String mode) {
		ILaunchConfigurationType configType = getJavaLaunchConfigType();
		List candidateConfigs = Collections.EMPTY_LIST;
		try {
			ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(configType);
			candidateConfigs = new ArrayList(configs.length);
			for (int i = 0; i < configs.length; i++) {
				ILaunchConfiguration config = configs[i];
				if (config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "").equals(type.getFullyQualifiedName())) { //$NON-NLS-1$
					if (config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "").equals(type.getJavaProject().getElementName())) { //$NON-NLS-1$
						candidateConfigs.add(config);
					}
				}
			}
		} catch (CoreException e) {
			JavaVEPlugin.log(e.getStatus(), Level.WARNING);
		}
		
		// If there are no existing configs associated with the IType, create one.
		// If there is exactly one config associated with the IType, return it.
		// Otherwise, if there is more than one config associated with the IType, prompt the
		// user to choose one.
		int candidateCount = candidateConfigs.size();
		if (candidateCount < 1) {
			return createConfiguration(type);
		} else if (candidateCount == 1) {
			return (ILaunchConfiguration) candidateConfigs.get(0);
		} else {
			// Prompt the user to choose a config.  A null result means the user
			// cancelled the dialog, in which case this method returns null,
			// since cancelling the dialog should also cancel launching anything.
			ILaunchConfiguration config = chooseConfiguration(candidateConfigs, mode);
			if (config != null) {
				return config;
			}
		}
		
		return null;
	}
	
	/**
	 * Show a selection dialog that allows the user to choose one of the specified
	 * launch configurations.  Return the chosen config, or <code>null</code> if the
	 * user cancelled the dialog.
	 */
	protected ILaunchConfiguration chooseConfiguration(List configList, String mode) {
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setElements(configList.toArray());
		dialog.setTitle(VCELauncherMessages.Shortcut_ConfigDlg_Title); 
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			dialog.setMessage(VCELauncherMessages.Shortcut_ConfigDlg_Msg_DebugConfiguration); 
		} else {
			dialog.setMessage(VCELauncherMessages.Shortcut_ConfigDlg_Msg_RunConfiguration); 
		}
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		return null;		
	}
	
	/**
	 * Create & return a new configuration based on the specified <code>IType</code>.
	 */
	protected ILaunchConfiguration createConfiguration(IType type) {
		ILaunchConfiguration config = null;
		try {
			ILaunchConfigurationType configType = getJavaLaunchConfigType();
			ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, getLaunchManager().generateUniqueLaunchConfigurationNameFrom(type.getElementName())); 
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, type.getFullyQualifiedName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, type.getJavaProject().getElementName());
			// TODO: remove swt hack
			if (isSWTProject(type.getJavaProject())) {
				wc.setAttribute(JavaBeanLaunchConfigurationDelegate.IS_SWT, true); //$NON-NLS-1$
			}
			config = wc.doSave();		
		} catch (CoreException ce) {
			JavaVEPlugin.log(ce.getStatus(), Level.WARNING);			
		}
		return config;
	}
	
	/*
	 * TODO: Refactor this out when we extract the SWT specific stuff from the launcher. 
	 * @param project
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private boolean isSWTProject(IJavaProject project) {
		boolean value = false;
		if (project != null) {
			try {
				FoundIDs foundIds = ProxyPlugin.getPlugin().getIDsFound(project);
				// It is considered an SWT project even if SWT is hidden because someone needs it and we need to setup the launch correctly.
				value = foundIds.containerIds.get("SWT_CONTAINER") != null || foundIds.pluginIds.containsKey("org.eclipse.swt"); //$NON-NLS-1$
			} catch (JavaModelException e) {
			}
		}
		return value;
	}
	
	/**
	 * Returns the local java launch config type
	 */
	protected ILaunchConfigurationType getJavaLaunchConfigType() {
		return getLaunchManager().getLaunchConfigurationType(VCEPreferences.getPlugin().getBundle().getSymbolicName() + ".launcher.JavaBean");		 //$NON-NLS-1$
	}
	
	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
	
	/**
	 * @see ILaunchShortcut#launch(IEditorPart, String)
	 */
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IJavaElement je = (IJavaElement) input.getAdapter(IJavaElement.class);
		if (je != null) {
			searchAndLaunch(new Object[] {je}, mode);
		} else {
			// error
		}
		
	}

	/**
	 * @see ILaunchShortcut#launch(ISelection, String)
	 */
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			searchAndLaunch(((IStructuredSelection)selection).toArray(), mode);
		} else {
			// error
		}
	}

}
