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
 *  $RCSfile: JavaBeanTab.java,v $
 *  $Revision: 1.21 $  $Date: 2006-02-15 16:11:47 $ 
 */
 
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.UIManager;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin.FoundIDs;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferencePage;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

import com.ibm.icu.util.ULocale;

/**
 * This tab appears in the LaunchConfigurationDialog for launch configurations that
 * require Java-specific launching information such as a main type and JRE.
 */
public class JavaBeanTab extends JavaLaunchTab {
		
	// Project UI widgets
	private Label fProjLabel;
	private Text fProjText;
	private Button fProjButton;

	// Main class UI widgets
	private Label fMainLabel;
	private Text fMainText;
	private Button fSearchButton;
	private Button fSearchExternalJarsCheckButton;

	// Launch arguments for target VM	
	protected List fLookAndFeelList;
	protected ArrayList fLookAndFeelClasses;
	
	// Controls to specify whether the component should be packed
	// or launched at a fixed size
	protected Button fPackWindow;
	protected Button fPackSWTWindow;
	
	// Controls for the locale of the VM to launch
	protected Text fLocaleText;
	protected Button fDefaultLocaleButton;
			
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	protected Composite comp;
	
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		
		comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		comp.setLayout(topLayout);		
		GridData gd;
		
		createVerticalSpacer(comp);
		
		Composite projComp = new Composite(comp, SWT.NONE);
		GridLayout projLayout = new GridLayout();
		projLayout.numColumns = 2;
		projLayout.marginHeight = 0;
		projLayout.marginWidth = 0;
		projComp.setLayout(projLayout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projComp.setLayoutData(gd);
		
		fProjLabel = new Label(projComp, SWT.NONE);
		fProjLabel.setText(VCELauncherMessages.BeanTab_project_label); 
		gd = new GridData();
		gd.horizontalSpan = 2;
		fProjLabel.setLayoutData(gd);
		
		fProjText = new Text(projComp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fProjText.setLayoutData(gd);
		fProjText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});
		
		fProjButton = createPushButton(projComp, VCELauncherMessages.BeanTab_browse_label, null); 
		fProjButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleProjectButtonSelected();
			}
		});
		
		Composite mainComp = new Composite(comp, SWT.NONE);
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 3;
		mainLayout.marginHeight = 0;
		mainLayout.marginWidth = 0;
		mainComp.setLayout(mainLayout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		mainComp.setLayoutData(gd);
		
		fMainLabel = new Label(mainComp, SWT.NONE);
		fMainLabel.setText(VCELauncherMessages.BeanTab_bean_label); 
		gd = new GridData();
		gd.horizontalSpan = 3;
		fMainLabel.setLayoutData(gd);

		fMainText = new Text(mainComp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fMainText.setLayoutData(gd);
		fMainText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});
		
		fSearchButton = createPushButton(mainComp,VCELauncherMessages.BeanTab_search_label, null); 
		fSearchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleSearchButtonSelected();
			}
		});
		
		fSearchExternalJarsCheckButton = new Button(mainComp, SWT.CHECK);
		fSearchExternalJarsCheckButton.setText(VCELauncherMessages.BeanTab_externaljars_label); 
		fSearchExternalJarsCheckButton.setToolTipText(VCELauncherMessages.BeanTab_externaljars_tooltip); 

		// Label for locale
		Label fLocaleLabel = new Label(mainComp, SWT.NONE);
		fLocaleLabel.setText(VCELauncherMessages.BeanTab_locale_label); 
		gd = new GridData();
		gd.horizontalSpan = 3;
		fLocaleLabel.setLayoutData(gd);
		
		// For locale create a new group that has 3 columns
		Composite localeComp = new Composite(mainComp, SWT.NONE);
		GridLayout localeLayout = new GridLayout();
		localeLayout.numColumns = 3;
		localeLayout.marginHeight = 0;
		localeLayout.marginWidth = 0;
		localeComp.setLayout(localeLayout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		localeComp.setLayoutData(gd);
		


		// Controls for the locale
			
		fDefaultLocaleButton = new Button(localeComp,SWT.CHECK);
		gd = new GridData();
		fDefaultLocaleButton.setData(gd);
		fDefaultLocaleButton.setText(VCELauncherMessages.BeanTab_default_label); 
		fDefaultLocaleButton.setSelection(true);
		
		fLocaleText = new Text(localeComp,SWT.BORDER);
		fLocaleText.setEnabled(false);
		fLocaleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fLocaleText.setText(ULocale.getDefault().toString());
		
		Label fSuggestionLabel = new Label(localeComp,SWT.NONE);
		fSuggestionLabel.setText(VCELauncherMessages.BeanTab_localesuggest_label); 
		fSuggestionLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		fDefaultLocaleButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event){
				// disable or enable the text box based on the selection
				fLocaleText.setEnabled(!fDefaultLocaleButton.getSelection());
			}
		});
		
		// Validate the locale entered
		fLocaleText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event){
				updateLaunchConfigurationDialog();
			}
		});
		
		TabFolder widgetSetsTabFolder = new TabFolder(comp, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		widgetSetsTabFolder.setLayoutData(gd);
		
		// Provide options specific to the Swing/AWT widget sets
		TabItem swingItem = new TabItem(widgetSetsTabFolder, SWT.NONE);
		swingItem.setText(VCELauncherMessages.BeanTab_swingtab_text); 
		swingItem.setToolTipText(VCELauncherMessages.BeanTab_swingtab_tooltip); 
		
		Composite swingOptionsComp = new Composite(widgetSetsTabFolder, SWT.NONE);
		GridLayout swingLayout = new GridLayout();
		swingOptionsComp.setLayout(swingLayout);
		swingItem.setControl(swingOptionsComp);
		
		// The user can specify which look and feel they want
		Label fJavaBeanLabel = new Label(swingOptionsComp, SWT.NONE);
		fJavaBeanLabel.setText(VCELauncherMessages.BeanTab_lookfeel_label); 
		gd = new GridData();
		gd.horizontalSpan = 3;
		fJavaBeanLabel.setLayoutData(gd);
		
		fLookAndFeelClasses = new ArrayList();
		fLookAndFeelList = new List(swingOptionsComp,SWT.BORDER);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessVerticalSpace = true;
//		data.horizontalSpan = 3;		
		fLookAndFeelList.setLayoutData(data);
		
		// Let the user specify the size of the component to open 
		// or whether to pack it
		Label fSizeLabel = new Label(swingOptionsComp,SWT.NONE);
		fSizeLabel.setText(VCELauncherMessages.BeanTab_size_label); 
		
		Composite visualComp = new Composite(swingOptionsComp, SWT.NONE);
		GridLayout visualLayout = new GridLayout();
		visualLayout.numColumns = 1;
		visualLayout.marginHeight = 0;
		visualLayout.marginWidth = 0;
		visualComp.setLayout(visualLayout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		visualComp.setLayoutData(gd);		
		
		fPackWindow = new Button(visualComp,SWT.CHECK);
		fPackWindow.setText(VCELauncherMessages.BeanTab_pack_label); 
		fPackWindow.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});

		// Provide options specific to the SWT widget set
		TabItem swtItem = new TabItem(widgetSetsTabFolder, SWT.NONE);
		swtItem.setText(VCELauncherMessages.BeanTab_swttab_text); 
		swtItem.setToolTipText(VCELauncherMessages.BeanTab_swttab_tooltip); 
		
		Composite swtOptionsComp = new Composite(widgetSetsTabFolder, SWT.NONE);
		GridLayout swtLayout = new GridLayout();
		swtOptionsComp.setLayout(swtLayout);
		swtItem.setControl(swtOptionsComp);
		
		// Let the user specify the size of the component to open 
		// or whether to pack it
		Label fSWTSizeLabel = new Label(swtOptionsComp,SWT.NONE);
		fSWTSizeLabel.setText(VCELauncherMessages.BeanTab_swtsize_label); 
		
		Composite swtVisualComp = new Composite(swtOptionsComp, SWT.NONE);
		GridLayout swtVisualLayout = new GridLayout();
		swtVisualLayout.numColumns = 1;
		swtVisualLayout.marginHeight = 0;
		swtVisualLayout.marginWidth = 0;
		swtVisualComp.setLayout(swtVisualLayout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		swtVisualComp.setLayoutData(gd);		
		
		fPackSWTWindow = new Button(swtVisualComp,SWT.CHECK);
		fPackSWTWindow.setText(VCELauncherMessages.BeanTab_pack_label); 
		fPackSWTWindow.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});
		
	}
	
	protected boolean validateLocale(){
		// Validate the local entered
		String newLocaleText = fLocaleText.getText();
		if ( newLocaleText.equals("") ){ //$NON-NLS-1$
			setErrorMessage(null);			
			return true;
		}		
		boolean localeIsValid = false;
		for ( int i=0; i < ULocale.getAvailableLocales().length ; i++){
			if(newLocaleText.trim().equals(ULocale.getAvailableLocales()[i].toString().trim())){
				localeIsValid = true;
				setErrorMessage(null);
			}
		}
		if ( !localeIsValid ) {
			setErrorMessage(VCELauncherMessages.BeanTab_badlocale_msg_ERROR_ + fLocaleText.getText()); 
		}
		return localeIsValid;
	}
		
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration config) {
		super.initializeFrom(config);
		updateProjectFromConfig(config);
		updateMainTypeFromConfig(config);
		// Put the look and feels into the list having cleared it out first.  The widget is persisted as the user
		// clicks between different launch configurations
		fLookAndFeelList.removeAll();
		
		// Add default option
		fLookAndFeelList.add( VCEPreferencePage.LnF_DEFAULT );
		
		// Dynamically query Swing for the available L&Fs
		// This is better than the static list used before, but has the problem that it
		// reflects the L&Fs available in the IDE's JRE, not the remote vm.
		UIManager.LookAndFeelInfo[] lnfs = UIManager.getInstalledLookAndFeels();
		for ( int i = 0; i < lnfs.length; i++ ) {
			// TODO: Hack to remove the Windows L&F from Linux (Sun bug 4843282)
			if (lnfs[i].getClassName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel") && //$NON-NLS-1$
					!Platform.getOS().equals(Platform.OS_WIN32))
				continue; 
			
		    fLookAndFeelList.add(lnfs[i].getName());
		    fLookAndFeelClasses.add(lnfs[i].getClassName());
		}

		// Get the user defined ones
		String[][] userLookAndFeelClasses = VCEPreferences.getUserLookAndFeelClasses();
		for (int i = 0; i < userLookAndFeelClasses.length; i++) {
			fLookAndFeelList.add(userLookAndFeelClasses[i][0]);
			fLookAndFeelClasses.add(userLookAndFeelClasses[i][1]);				
		}
		
		// Get the plugin defined defined ones
		String[][] pluginLookAndFeelClasses = VCEPreferences.getPluginLookAndFeelClasses();
		for (int i = 0; i < pluginLookAndFeelClasses.length; i++) {
			fLookAndFeelList.add(pluginLookAndFeelClasses[i][0]);
			fLookAndFeelClasses.add(pluginLookAndFeelClasses[i][1]);
		}		
		
		try {
			// Set the look and feel to be the save one in the configuration, otherwise the default
			String lookAndFeel = config.getAttribute(VCEPreferences.SWING_LOOKANDFEEL,""); //$NON-NLS-1$
			if ( lookAndFeel.equals("")) { //$NON-NLS-1$
				// If the configuration has no look and feel, default to the one in the preferences
				lookAndFeel = VCEPreferences.getPlugin().getPluginPreferences().getString(VCEPreferences.SWING_LOOKANDFEEL);
			}
			if (lookAndFeel.equals("")){ //$NON-NLS-1$
				fLookAndFeelList.setSelection(0);
			} else {
				// Match the look and feel class against the list of known ones
				// and remember that the list has <default> added at the top so adjust by +1
				for (int i=0;i < fLookAndFeelClasses.size() ; i++){
					if (fLookAndFeelClasses.get(i).equals(lookAndFeel)){
						fLookAndFeelList.setSelection(i + 1);
						break;
					}
				}
			}
			fLookAndFeelList.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					updateLaunchConfigurationDialog();
				}
			});
			// Set the saved locale
			String locale = config.getAttribute(JavaBeanLaunchConfigurationDelegate.LOCALE, ""); //$NON-NLS-1$
			if ( locale.equals("") ){ //$NON-NLS-1$
				fDefaultLocaleButton.setSelection(true);
//				fLocaleText.setText(""); //$NON-NLS-1$
			} else {
				fDefaultLocaleButton.setSelection(false);
				fLocaleText.setEnabled(true);
				fLocaleText.setText(locale);
			}			
			// Set the saved size
			boolean pack = config.getAttribute(JavaBeanLaunchConfigurationDelegate.PACK,false);
			// TODO: another SWT hack
			if (isSWTProject(getJavaProject())) {
				fPackSWTWindow.setSelection(pack);
			} else {
				fPackWindow.setSelection(pack);
			}
			
		} catch ( CoreException exc ) {
			fLookAndFeelList.setSelection(0);
		}
		
		// relayout to reflect the new look and feel list size
		comp.layout();
	}
	
	protected void updateProjectFromConfig(ILaunchConfiguration config) {
		String projectName = ""; //$NON-NLS-1$
		try {
			projectName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, EMPTY_STRING);	
		} catch (CoreException ce) {
			JavaVEPlugin.log(ce.getStatus(), Level.WARNING);
		}
		fProjText.setText(projectName);
	}
	
	protected void updateMainTypeFromConfig(ILaunchConfiguration config) {
		String mainTypeName = ""; //$NON-NLS-1$
		try {
			mainTypeName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, EMPTY_STRING);
		} catch (CoreException ce) {
			JavaVEPlugin.log(ce.getStatus(), Level.WARNING);
		}	
		fMainText.setText(mainTypeName);	
	}
		
	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, fProjText.getText());
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, fMainText.getText());
		// Try and set the look and feel class for the target VM
		// For <default> this is blank, 
		// otherwise get the class from the preferenes page static, remembering that
		// the list index if 1 higher than the classes list because of the <default> entry
		// added at the beginning
		if ( fLookAndFeelList.getSelectionIndex() == 0 ){
			config.setAttribute(VCEPreferences.SWING_LOOKANDFEEL, ""); //$NON-NLS-1$
		} else {
			config.setAttribute(
				VCEPreferences.SWING_LOOKANDFEEL,
				(String)fLookAndFeelClasses.get(fLookAndFeelList.getSelectionIndex() -1));
		}
		// Save the locale
		if ( fDefaultLocaleButton.getSelection() ) {
			config.setAttribute(JavaBeanLaunchConfigurationDelegate.LOCALE, ""); //$NON-NLS-1$
		} else {
			config.setAttribute(JavaBeanLaunchConfigurationDelegate.LOCALE,fLocaleText.getText());
		}
		// Save details about whether to pack or what size to use
		config.setAttribute(JavaBeanLaunchConfigurationDelegate.PACK, fPackWindow.getSelection() || fPackSWTWindow.getSelection());
		
		// TODO: Remove this swt specific code later
		config.setAttribute(JavaBeanLaunchConfigurationDelegate.IS_SWT, isSWTProject(getJavaProject()));
	}
			
	/**
	 * Create some empty space 
	 */
	protected void createVerticalSpacer(Composite comp) {
		new Label(comp, SWT.NONE);
	}
		
	/**
	 * Show a dialog that lists all Java Beans
	 */
	protected void handleSearchButtonSelected() {
		
		IJavaProject javaProject = getJavaProject();
		IJavaElement elem = null;
		if ((javaProject == null) || !javaProject.exists())
			;
		else
			elem = javaProject;
		
		int constraints = IJavaElementSearchConstants.CONSIDER_BINARIES;
		if (fSearchExternalJarsCheckButton.getSelection()) {
			constraints |= IJavaElementSearchConstants.CONSIDER_EXTERNAL_JARS;
		}		
		
		Shell shell = getShell();
		JavaBeanTypeFinderDialog dialog = new JavaBeanTypeFinderDialog(shell, getLaunchConfigurationDialog(), elem, constraints);
		dialog.setFilter(fMainText.getText());
		dialog.setTitle(VCELauncherMessages.BeanTab_search_title); 
		dialog.setMessage(VCELauncherMessages.BeanTab_search_msg); 
		if (dialog.open() == Window.CANCEL) {
			return;
		}
	
		Object[] results = dialog.getResult();
		if ((results == null) || (results.length < 1)) {
			return;
		}		
		IType type = (IType)results[0];
		if (type != null) {
			fMainText.setText(type.getFullyQualifiedName());
			javaProject = type.getJavaProject();
			fProjText.setText(javaProject.getElementName());
		}
	}
		
	/**
	 * Show a dialog that lets the user select a project.  This in turn provides
	 * context for the Java Bean type, allowing the user to key a Java Bean name, or
	 * constraining the search for Java Beans to the specified project.
	 */
	protected void handleProjectButtonSelected() {
		IJavaProject project = chooseJavaProject();
		if (project == null) {
			return;
		}
		
		String projectName = project.getElementName();
		fProjText.setText(projectName);		
	}
	
	/**
	 * Realize a Java Project selection dialog and return the first selected project,
	 * or null if there was none.
	 */
	protected IJavaProject chooseJavaProject() {
		IJavaProject[] projects;
		try {
			projects= JavaCore.create(getWorkspaceRoot()).getJavaProjects();
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e.getStatus(), Level.WARNING);
			projects= new IJavaProject[0];
		}
		
		ILabelProvider labelProvider= new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setTitle(VCELauncherMessages.BeanTab_project_search_title); 
		dialog.setMessage(VCELauncherMessages.BeanTab_project_search_msg); 
		dialog.setElements(projects);
		
		IJavaProject javaProject = getJavaProject();
		if (javaProject != null) {
			dialog.setInitialSelections(new Object[] { javaProject });
		}
		if (dialog.open() == Window.OK) {			
			return (IJavaProject) dialog.getFirstResult();
		}			
		return null;		
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
				value = foundIds.containerIds.get("SWT_CONTAINER") != null || foundIds.pluginIds.containsKey("org.eclipse.swt"); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (JavaModelException e) {
			}
		}
		return value;
	}
	
	/**
	 * Return the IJavaProject corresponding to the project name in the project name
	 * text field, or null if the text does not match a project name.
	 */
	protected IJavaProject getJavaProject() {
		String projectName = fProjText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return getJavaModel().getJavaProject(projectName);		
	}
	
	/**
	 * Convenience method to get the workspace root.
	 */
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	/**
	 * Convenience method to get access to the java model.
	 */
	private IJavaModel getJavaModel() {
		return JavaCore.create(getWorkspaceRoot());
	}


	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration config) {
		
		setErrorMessage(null);
		setMessage(null);
		
		String name = fProjText.getText().trim();
		if (name.length() > 0) {
			if (!ResourcesPlugin.getWorkspace().getRoot().getProject(name).exists()) {
				setErrorMessage(VCELauncherMessages.BeanTab_badproject_msg_ERROR_); 
				return false;
			}
		}

		name = fMainText.getText().trim();
		if (name.length() == 0) {
			setErrorMessage(VCELauncherMessages.BeanTab_nobean_msg_ERROR_); 
			return false;
		}
		IJavaProject jp = getJavaProject();
		if (jp != null) {
			// only verify type exists if Java project is specified
			if (getMainType(name, jp) == null) {
				setErrorMessage(VCELauncherMessages.BeanTab_badbean_msg_ERROR_);
				return false;
			}
		}	
		
		boolean isValidLocale = validateLocale();
		if (!isValidLocale){
			return false;
		}
		
		// No errors found
		return true;
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		IJavaElement je = getContext();
		if (je != null) {
			initializeJavaProject(je, config);		
		} else {
			// We set empty attributes for project & main type so that when one config is
			// compared to another, the existence of empty attributes doesn't cause an
			// incorrect result (the performApply() method can result in empty values
			// for these attributes being set on a config if there is nothing in the
			// corresponding text boxes)
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
		}
		
		initializeHardCodedDefaults(config);		
		initializeMainTypeAndName(je, config);			
	}

	/**
	 * Set the main type & name attributes on the working copy based on the IJavaElement
	 */
	protected void initializeMainTypeAndName(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
		String name = null;
		if (javaElement instanceof IMember) {
			IMember member = (IMember)javaElement;
			if (member.isBinary()) {
				javaElement = member.getClassFile();
			} else {
				javaElement = member.getCompilationUnit();
			}
		}
		if (javaElement instanceof ICompilationUnit || javaElement instanceof IClassFile) {
			try {
				IType[] types = JavaBeanFinder.findTargets(PlatformUI.getWorkbench().getProgressService(), new Object[] {javaElement});
				if (types != null && (types.length > 0)) {
					// Simply grab the first bean type found in the searched element
					name = types[0].getFullyQualifiedName();
				}
			} catch (InterruptedException ie) {
			} catch (InvocationTargetException ite) {
			}
		}
		
		if (name == null) {
			name= ""; //$NON-NLS-1$
		}
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, name);
		if (name.length() > 0) {
			int index = name.lastIndexOf('.');
			if (index > 0) {
				name = name.substring(index + 1);
			}		
			name = getLaunchConfigurationDialog().generateName(name);
			config.rename(name);
		}		
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return VCELauncherMessages.BeanTab_title; 
	}
	/**
	 * Initialize those attributes whose default values are independent of any context.
	 */
	protected void initializeHardCodedDefaults(ILaunchConfigurationWorkingCopy config) {
		String lookAndFeel = VCEPreferences.getPlugin().getPluginPreferences().getString(VCEPreferences.SWING_LOOKANDFEEL);
		if ( lookAndFeel != null ) {
			config.setAttribute(VCEPreferences.SWING_LOOKANDFEEL,lookAndFeel);
		}
		// Assume that the default locale is to be used
		config.setAttribute(JavaBeanLaunchConfigurationDelegate.LOCALE, ""); //$NON-NLS-1$
	}
	public Image getImage() {
		return JavaVEPlugin.getJavaBeanImage();
	}		
	/**
	 * Return the <code>IType</code> referenced by the specified name and contained in the specified project
	 * Return null if not found.
	 * 
	 * @since 1.2.0
	 */
	private IType getMainType(String mainTypeName, IJavaProject javaProject) {
		if ((mainTypeName == null) || (mainTypeName.trim().length() < 1))
			return null;
		IType mainType = null;
		try {
			String pathStr = mainTypeName.replace('.', '/') + ".java"; //$NON-NLS-1$
			IJavaElement javaElement = javaProject.findElement(new Path(pathStr));
			if (javaElement == null) {
				return null;
			} else if (javaElement instanceof IType) {
				mainType = (IType) javaElement;
			} else if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
				String simpleName = Signature.getSimpleName(mainTypeName);
				mainType = ((ICompilationUnit) javaElement).getType(simpleName);
			} else if (javaElement.getElementType() == IJavaElement.CLASS_FILE) {
				mainType = ((IClassFile) javaElement).getType();
			}
		} catch (JavaModelException jme) {
		}
		return mainType;
	}	
	
}
