package org.eclipse.ve.internal.java.wizard;

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
 *  $RCSfile: ClasspathWizardPage.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import org.eclipse.ve.internal.cde.palette.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 *  This is part of an experimental API for palette extensions and is not guarantted to be supported in future release
 */
public class ClasspathWizardPage extends WizardPage implements IClasspathContainerPage , IExecutableExtension {
	
	protected IClasspathEntry fContainerEntry;
	protected String variableName;
	protected IConfigurationElement configData;
	protected static Image fVariableImage;	
	protected static Image fExternalJarImage;
	protected static Image fExternalJarWithSourceImage;	
	
	public ClasspathWizardPage(){
		super(InternalMessages.getString("ClasspathWizardPage.PageName")); //$NON-NLS-1$
		setImageDescriptor(JavaVEPlugin.getWizardTitleImageDescriptor());
	}
	public void createControl(Composite parent) {

		Composite c = new Composite(parent,SWT.NONE);
		setControl(c);
		GridLayout layout = new GridLayout(2,false);
		c.setLayout(layout);
		// Create a double width spacer
//		createSpacer(c, 2);

		// See if we can find configuration data for the extension
		if ( configData == null ) {
			setErrorMessage(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.NoConfigData.ErrorMessage_ERROR_"), new Object[]{variableName})); //$NON-NLS-1$
			new Label(c,SWT.NONE).setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.NoConfigData.Label.ErrorMessage_ERROR_"), new Object[]{variableName})); //$NON-NLS-1$
			setControl(c);
			return;
		}

		// Show an image of a variable and let them user know whether the variable will be created or already exists
		// and what the variable points to
		if ( fVariableImage == null ) {
			fVariableImage = org.eclipse.jdt.internal.ui.JavaPlugin.getDefault().getImageRegistry().get(JavaPluginImages.IMG_OBJS_ENV_VAR);
		}		
		new Label(c,SWT.NONE).setImage(fVariableImage);
		// From here we get the variable and also the details to expand the classpath to
		// and show the user these in nice labels and list
		// we also show them the extension to the palette
		Label label = new Label(c,SWT.NONE);
		// See whether or not the variable exists so we can tell the user whether it exists or will be created
		String[] existingVariableNames = JavaCore.getClasspathVariableNames();
		boolean variableExists = false;
		for (int i = 0; i < existingVariableNames.length; i++) {
			if ( existingVariableNames[i].equals(variableName) ) {
				variableExists = true;
				break;
			}
		}
		if ( !variableExists ) {
			label.setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.JavaVariable.Created"), new Object[]{variableName})); //$NON-NLS-1$
		} else { 
			label.setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.JavaVariable.Used"), new Object[]{variableName})); //$NON-NLS-1$
			// Beneath the variable create a spacer and tell the user where the variable is located
			createSpacer(c,1);
			Label l2 = createIndentedLabel(c);
			l2.setText(JavaCore.getClasspathVariable(variableName).toString());
		}
		
		// Create a group that spans two columns - the width of the composite
		Group g = createGroup(c,2);
		g.setText(InternalMessages.getString("ClasspathWizardPage.Group.Buildpath.Includes")); //$NON-NLS-1$
		if ( fExternalJarImage == null ) {
			fExternalJarImage = org.eclipse.jdt.internal.ui.JavaPlugin.getDefault().getImageRegistry().get(JavaPluginImages.IMG_OBJS_EXTJAR);
		}
		if ( fExternalJarWithSourceImage == null ) {
			fExternalJarWithSourceImage = org.eclipse.jdt.internal.ui.JavaPlugin.getDefault().getImageRegistry().get(JavaPluginImages.IMG_OBJS_EXTJAR_WSRC);			
		}
		// Get the entries
		IConfigurationElement[] entries = configData.getChildren(JavaVEPlugin.PI_EXTEND);
		for (int i = 0; i < entries.length; i++) {
			IConfigurationElement iConfigurationElement = entries[i];
			String runtime = iConfigurationElement.getAttributeAsIs(JavaVEPlugin.PI_RUNTIME);
			String source = iConfigurationElement.getAttributeAsIs(JavaVEPlugin.PI_SOURCE);								
			// Put a graphic in front of the variable to show whether it is a JAR with or without source
			if ( source == null ) {
				new Label(g,SWT.NONE).setImage(fExternalJarImage);			
			} else { 
				new Label(g,SWT.NONE).setImage(fExternalJarWithSourceImage);				
			}
			// Followed by the variable name
			new Label(g,SWT.NONE).setText(variableName + "/" + runtime);			 //$NON-NLS-1$
			// If we have source put it on the next line and indent it
			if ( source != null ) {
				createSpacer(g,1);
				Label l = createIndentedLabel(g);
				l.setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.BuildPath.SourceAttachments.Message"), new Object[]{variableName + "/" + source})); //$NON-NLS-1$ //$NON-NLS-2$
			} 
		}

		// Show the user any palette extensions that occur
		String paletteExtension = configData.getAttributeAsIs(JavaVEPlugin.PI_PALETTECATS);
		if ( paletteExtension!= null){
			// Read the palette file and crawl it to see what categories and JavaBeans it includes
			ResourceSet rs = new ResourceSetImpl();
			try { 
				Resource r = rs.createResource(URI.createURI(paletteExtension));
				r.load(Collections.EMPTY_MAP);
				// Get the categories from the palette extension
				Collection categories = EcoreUtil.getObjectsByType(r.getContents(), PalettePackage.eINSTANCE.getCategory());
				// Deal with one category right now
				Iterator iter = categories.iterator();
				// Remember the Images we create, so that we can dispose them when the page is disposed
				final ArrayList images = new ArrayList();
				while(iter.hasNext()){
					Category category = (Category) iter.next();
					Group g2 = createGroup(c,4);
					// Show the user the palette extensions that exist
					g2.setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.Palette.Category.Added.Message"), new Object[]{category.getCategoryLabel().getStringValue()})); //$NON-NLS-1$
					// Now show the individual category entries - as labels for now,  maybe in a list later - JRW
					Iterator groups = category.getGroups().iterator();
					while( groups.hasNext() ) {
						org.eclipse.ve.internal.cde.palette.Group group = (org.eclipse.ve.internal.cde.palette.Group) groups.next();
						Iterator items = group.getEntries().iterator();
						while (items.hasNext()) {
							Entry element = (Entry) items.next();
							// Create a label that shows the user the palette icon and name
							String iconName = element.getIcon16Name();
							try {
								URL url = new URL(iconName);					
								Image image = ImageDescriptor.createFromURL(url).createImage();
								new Label(g2,SWT.NONE).setImage(image);
								images.add(image);
							} catch ( MalformedURLException exc ) {
								// TODO Log this
								new Label(g2,SWT.NONE);
							}
							new Label(g2,SWT.NONE).setText(element.getEntryLabel().getStringValue());
							// Put some code in here so that if the graphic is an undefined one we tell the user why we think this is
						}
					}
					// When the compoiste is disposed throw away the images we've created
					parent.addDisposeListener(new DisposeListener(){
						public void widgetDisposed(DisposeEvent event){
							Iterator iter = images.iterator();
							while(iter.hasNext()){
								((Image)iter.next()).dispose();
							}
						}
					}); 					
				}
			} catch ( Exception exc ) {
				// Some kind of error occured.  Tell the user about this
				new Label(c,SWT.NONE).setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.LoadPaletteExtension.Exception.Message1_EXC_"), new Object[]{paletteExtension})); //$NON-NLS-1$
				new Label(c,SWT.NONE).setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.LoadPaletteExtension.Exception.Message2"), new Object[]{exc.getMessage()})); //$NON-NLS-1$
				new Label(c,SWT.NONE).setText(InternalMessages.getString("ClasspathWizardPage.LoadPaletteExtension.Exception.Message3")); //$NON-NLS-1$
			}
		}
	}

	public boolean finish() {
		
		if ( getErrorMessage() != null ) {
			return false;
		}
		// We need to know the runtime and the source location.  
		if (configData != null) {
			// Just by asking for the classpath variable will create it
			JavaCore.getClasspathVariable(variableName);
			// We are going to create a single container called JAVA_BEANS that has the name of the variable appended to it, e.g. JAVA_BEANS/WIZZO_WIDGETS
			// The ContainerInitializer registered for JAVA_BEANS will then pick this up, read the variable name and then do the appropiate manipulation of the
			// project's build path
			String pathAttribute = configData.getAttributeAsIs(JavaVEPlugin.PI_PATH);	
			final IPath containerPath = new Path(ContainerInitializer.JAVA_BEANS).append(pathAttribute);
			// Now append the name of the variable the user has supplied in the initialization data to the container
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
					try {
						monitor.beginTask(InternalMessages.getString("ClasspathWizardPage.Finish.task.SettingProject"), 2000); //$NON-NLS-1$
						setSelection(JavaCore.newContainerEntry(containerPath));
					} finally {
						monitor.done();
					}
				}
			};			
			
			try {
				getContainer().run(true, true, op);
			} catch (InterruptedException e) {
				return false;
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof CoreException) {
					setErrorMessage(((CoreException) e.getTargetException()).getStatus().getMessage());
				}
				else {
					// CoreExceptions are handled above, but unexpected runtime exceptions and errors may still occur.
					setErrorMessage(e.getTargetException().getLocalizedMessage());
				}
				return false;
			}			
		}
		return true;			
	}
	protected IClasspathEntry[] addVariableEntry(IClasspathEntry[] raw, IClasspathEntry newEntry) {
		for (int i = 0; i < raw.length; i++) {
			if (newEntry.equals(raw[i])) {
				return raw;
			}
		}
		
		// Add it at the end.
		IClasspathEntry[] newRaw = new IClasspathEntry[raw.length + 1];
		System.arraycopy(raw, 0, newRaw, 0, raw.length);
		newRaw[raw.length] = newEntry;
		return newRaw;
	}	

	public IClasspathEntry getSelection() {
		return fContainerEntry;
	}
	
	protected Label createIndentedLabel(Composite c){
		Label result = new Label(c,SWT.NONE);
		GridData indentData = new GridData();
		indentData.horizontalIndent = 30;
		result.setLayoutData(indentData);			
		return result;
	}
	
	protected Group createGroup(Composite c, int rows){
		Group g = new Group(c,SWT.NONE);
		g.setLayout(new GridLayout(rows,false));
		GridData gData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gData.horizontalSpan = 2;
		g.setLayoutData(gData);
		return g;
	}
	
	protected void createSpacer(Composite c, int span){
		Label label = new Label(c,SWT.NONE);
		if ( span > 1 ) {
			GridData data = new GridData();
			data.horizontalSpan = 2;
			label.setLayoutData(data);
		}
	}

	public void setSelection(IClasspathEntry containerEntry) {
		fContainerEntry = containerEntry;
	}
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		// Parse the initialization data to find all variable/jar combinations.  These are , delimited
		if ( data instanceof String ) {
			variableName = (String) data;
			// Need to collect extension points details.  The variableName will be the variable name that we must ensure is initialize and also correctly 
			// added to the project
			// These variable names are defined by the extension point <extension point="org.eclipse.ve.internal.java.core.registrations"> and collected by JavaVEPlugin
			IConfigurationElement[] configDataArray = JavaVEPlugin.getPlugin().getRegistrations(new Path(variableName));		
			if ( configDataArray != null && configDataArray.length == 1 ){
				configData = configDataArray[0];
			}
			// See if the config Data has a title 
			setTitle(variableName);
		}
	}
}
