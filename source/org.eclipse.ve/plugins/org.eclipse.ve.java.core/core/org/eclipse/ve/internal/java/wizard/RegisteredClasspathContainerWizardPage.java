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
/*
 *  $RCSfile: RegisteredClasspathContainerWizardPage.java,v $
 *  $Revision: 1.8 $  $Date: 2005-09-19 21:36:27 $ 
 */
package org.eclipse.ve.internal.java.wizard;

import java.text.MessageFormat;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 *  This is part of an experimental API for simplified classpath containers and is not guaranteed to be supported in future release
 */
public class RegisteredClasspathContainerWizardPage extends WizardPage implements IClasspathContainerPage , IExecutableExtension {
	
	protected IClasspathEntry fContainerEntry;
	protected String containerid;
	protected IConfigurationElement configData;
	protected static Image fVariableImage;	
	protected static Image fExternalJarImage;
	protected static Image fExternalJarWithSourceImage;	
	
	public RegisteredClasspathContainerWizardPage(){
		super(InternalMessages.ClasspathWizardPage_PageName); 
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
			setErrorMessage(MessageFormat.format(InternalMessages.ClasspathWizardPage_NoConfigData_ErrorMessage_ERROR_, new Object[]{containerid})); 
			new Label(c,SWT.NONE).setText(MessageFormat.format(InternalMessages.ClasspathWizardPage_NoConfigData_Label_ErrorMessage_ERROR_, new Object[]{containerid})); 
			setControl(c);
			return;
		}

		// Create a group that spans two columns - the width of the composite
		Group g = createGroup(c,2);
		g.setText(InternalMessages.ClasspathWizardPage_Group_Buildpath_Includes); 
		if ( fExternalJarImage == null ) {
			fExternalJarImage = JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_EXTERNAL_ARCHIVE);
		}
		if ( fExternalJarWithSourceImage == null ) {
			fExternalJarWithSourceImage = JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_EXTERNAL_ARCHIVE_WITH_SOURCE);			
		}
		// Get the entries
		IConfigurationElement[] libraries = configData.getChildren(JavaVEPlugin.PI_LIBRARY);
		for (int i = 0; i < libraries.length; i++) {
			IConfigurationElement library = libraries[i];
			String runtime = library.getAttributeAsIs(JavaVEPlugin.PI_RUNTIME);
			String source = library.getAttributeAsIs(JavaVEPlugin.PI_SOURCE);								
			// Put a graphic in front of the variable to show whether it is a JAR with or without source
			if ( source == null ) {
				new Label(g,SWT.NONE).setImage(fExternalJarImage);			
			} else { 
				new Label(g,SWT.NONE).setImage(fExternalJarWithSourceImage);				
			}
			// Followed by the variable name
			new Label(g,SWT.NONE).setText(containerid + "/" + runtime);			 //$NON-NLS-1$
			// If we have source put it on the next line and indent it
			if ( source != null ) {
				createSpacer(g,1);
				Label l = createIndentedLabel(g);
				l.setText(MessageFormat.format(InternalMessages.ClasspathWizardPage_BuildPath_SourceAttachments_Message, new Object[]{containerid + "/" + source})); 
			} 
		}

		// Show the user any palette extensions that occur
//		String paletteExtension = configData.getAttributeAsIs(JavaVEPlugin.PI_PALETTECATS);
//		if ( paletteExtension!= null){
//			// Read the palette file and crawl it to see what categories and JavaBeans it includes
//			ResourceSet rs = new ResourceSetImpl();
//			try { 
//				Resource r = rs.createResource(URI.createURI(paletteExtension));
//				r.load(Collections.EMPTY_MAP);
//				// Get the categories from the palette extension
//				Collection categories = EcoreUtil.getObjectsByType(r.getContents(), PalettePackage.eINSTANCE.getCategory());
//				// Deal with one category right now
//				Iterator iter = categories.iterator();
//				// Remember the Images we create, so that we can dispose them when the page is disposed
//				final ArrayList images = new ArrayList();
//				while(iter.hasNext()){
//					Category category = (Category) iter.next();
//					Group g2 = createGroup(c,4);
//					// Show the user the palette extensions that exist
//					g2.setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.Palette.Category.Added.Message"), new Object[]{category.getCategoryLabel().getStringValue()})); //$NON-NLS-1$
//					// Now show the individual category entries - as labels for now,  maybe in a list later - JRW
//					Iterator groups = category.getGroups().iterator();
//					while( groups.hasNext() ) {
//						org.eclipse.ve.internal.cde.palette.Group group = (org.eclipse.ve.internal.cde.palette.Group) groups.next();
//						Iterator items = group.getEntries().iterator();
//						while (items.hasNext()) {
//							Entry element = (Entry) items.next();
//							// Create a label that shows the user the palette icon and name
//							String iconName = element.getIcon16Name();
//							try {
//								URL url = new URL(iconName);					
//								Image image = ImageDescriptor.createFromURL(url).createImage();
//								new Label(g2,SWT.NONE).setImage(image);
//								images.add(image);
//							} catch ( MalformedURLException exc ) {
//								// TODO Log this
//								new Label(g2,SWT.NONE);
//							}
//							new Label(g2,SWT.NONE).setText(element.getEntryLabel().getStringValue());
//							// Put some code in here so that if the graphic is an undefined one we tell the user why we think this is
//						}
//					}
//					// When the compoiste is disposed throw away the images we've created
//					parent.addDisposeListener(new DisposeListener(){
//						public void widgetDisposed(DisposeEvent event){
//							Iterator iter = images.iterator();
//							while(iter.hasNext()){
//								((Image)iter.next()).dispose();
//							}
//						}
//					}); 					
//				}
//			} catch ( Exception exc ) {
//				// Some kind of error occured.  Tell the user about this
//				new Label(c,SWT.NONE).setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.LoadPaletteExtension.Exception.Message1_EXC_"), new Object[]{paletteExtension})); //$NON-NLS-1$
//				new Label(c,SWT.NONE).setText(MessageFormat.format(InternalMessages.getString("ClasspathWizardPage.LoadPaletteExtension.Exception.Message2"), new Object[]{exc.getMessage()})); //$NON-NLS-1$
//				new Label(c,SWT.NONE).setText(InternalMessages.getString("ClasspathWizardPage.LoadPaletteExtension.Exception.Message3")); //$NON-NLS-1$
//			}
//		}
	}

	public boolean finish() {
		Path path = new Path(containerid);
		setSelection(JavaCore.newContainerEntry(path));			
		return true;		
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
		// The container id is the id from the config element. This is because this the config element is a "classpathContainerPage", and the
		// id attribute is the container this is for.
		containerid = config.getAttributeAsIs("id"); //$NON-NLS-1$
		String name = config.getAttribute("name");  //$NON-NLS-1$
		// Get the registration element for this container. It will be the first one found. Can't handle more than one.
		IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor(JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), JavaVEPlugin.PI_JBCF_REGISTRATIONS);
		for (int i = 0; i < configs.length; i++) {
			String ctrid = configs[i].getAttributeAsIs(JavaVEPlugin.PI_CONTAINER);
			if (containerid.equals(ctrid)) {
				configData = configs[i];
				setTitle(MessageFormat.format(InternalMessages.RegisteredClasspathContainerWizardPage_Title, new Object[] {name, containerid}));				 
				break;
			} 
		}				
	}
}
