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
 *  $RCSfile: ContainerInitializer.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * Initializer for a JAVA_BEANS container
 * This is part of an experimental API for palette extensions and is not guarantted to be supported in future releases
 */
public class ContainerInitializer extends ClasspathContainerInitializer {
	public static final String JAVA_BEANS = "JAVA_BEANS"; //$NON-NLS-1$
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		
		// We are going to remove the container path and ensure that in its place there is a variable that points to the jar files
		// This is only because BeanInfo can't handle containers right yet
		// The containerPath should contain two segments, the first is JAVA_BEANS and the second is the variable name of the user's JavaBeans
		// First - create the variable if we need to		
		String variableName = containerPath.segment(1);
		if ( JavaCore.getClasspathVariable(variableName) == null){
			try {
				// If the variable doesn't exist it needs creating
				IConfigurationElement configElement = JavaVEPlugin.getPlugin().getRegistrations(containerPath.removeFirstSegments(1))[0];
				URL install = Platform.resolve(configElement.getDeclaringExtension().getDeclaringPluginDescriptor().getInstallURL());	
				IPath variablePath = new Path(install.getFile());
				JavaCore.setClasspathVariable(variableName, variablePath , new NullProgressMonitor());
			} catch ( IOException exc ) {
			}
		}
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		boolean wasAutobuild= ws.getDescription().isAutoBuilding();
		ws.getDescription().setAutoBuilding(false);
		try { 
			// Remove the exising variable with the container path from the classpath
			// This is automatically added by the container wizard and we are going to remove it and replace it
			// with variable extensions that actually point to the runtime and source code jars
			removeFromClasspath(project,containerPath);		
			// The JBCF plugin should have registered the extensions for the JavaBeans 
			Path javaBeansPath = new Path(variableName);
			IConfigurationElement[] configData = JavaVEPlugin.getPlugin().getRegistrations(javaBeansPath);
			// The configData holds children of type <entries> that contain details of the jar to add and where its source is, e.g.
			// <entries>
	        //   <extend runtime="examplebeans.jar" source="examplebeans-src.zip" prefix="src"/>
			// </entries>
			IConfigurationElement[] extensions= configData[0].getChildren(JavaVEPlugin.PI_EXTEND);
			String pathAttribute = configData[0].getAttributeAsIs(JavaVEPlugin.PI_PATH);				
			// Iterate over the entries and create classpath entries for each one
			for (int i = 0; i < extensions.length; i++) {
				// The source and runtime don't have the variable in front of them, however we should put this there so that later when Path statements are created
				// these are correctly qualified
				final IPath runtimePath = new Path(pathAttribute + "/" + extensions[i].getAttributeAsIs(JavaVEPlugin.PI_RUNTIME)); //$NON-NLS-1$
				String sourceAttribute = extensions[i].getAttributeAsIs(JavaVEPlugin.PI_SOURCE);
				IPath sourcePath = null;
				if ( sourceAttribute != null ) {
					sourcePath = new Path(pathAttribute + "/" + sourceAttribute); //$NON-NLS-1$
				}
				// Get the extension for the source prefix
				String sourcePrefix = extensions[i].getAttributeAsIs(JavaVEPlugin.PI_SOURCEPREFIX);
				IPath sourcePrefixPath = null;
				if ( sourcePrefix != null ) {
					// The source path is a variable with the word _SRCROOT added to the end.  See if we have one and create one if not
					String sourceRootVariableName = variableName + "_SRCROOT";  //$NON-NLS-1$
					if ( JavaCore.getClasspathVariable(sourceRootVariableName) == null){
						// Create a new variable entry because one doesn't already exist
						JavaCore.setClasspathVariable(sourceRootVariableName, new Path(sourcePrefix), new NullProgressMonitor());
					}
					sourcePrefixPath = JavaCore.getClasspathVariable(sourceRootVariableName);
				}
				IClasspathEntry newVariableEntry = JavaCore.newVariableEntry(runtimePath, sourcePath , sourcePrefixPath ,true);
				// Now ensure that the variable entry is in the path
				addToClasspath(project, newVariableEntry);			
			}
			project.getProject().build(IncrementalProjectBuilder.FULL_BUILD,null);
		} finally {
			ws.getDescription().setAutoBuilding(wasAutobuild);
		}
	}
	protected void addToClasspath(IJavaProject project, IClasspathEntry newEntry) throws JavaModelException {
		
		IClasspathEntry[] raw = project.getRawClasspath();
		List classpathEntries = new ArrayList(raw.length);
		for (int i = 0; i < raw.length; i++) {
			// Create a list of existing classpath entries
			classpathEntries.add(raw[i]);
		}
		
		// It is possible that some of the entrie we have been asked to add already exist, if so don't add them so we must filter the newEntries down to just the new ones
		Iterator iter = classpathEntries.iterator();
		while(iter.hasNext()){
			IClasspathEntry existingEntry = (IClasspathEntry) iter.next();
			// If one of the existing entries equals the one we are going to add then skip to the next new entry
			if ( (existingEntry.equals(newEntry))) { 
				return;
			}
		}

		// Classpath entries contains the existing entries.  Add the new one to be added as well before changing the raw classpath		
		classpathEntries.add(newEntry);
		IClasspathEntry[] classpathEntriesArray = (IClasspathEntry[])classpathEntries.toArray(new IClasspathEntry[0]);
		project.setRawClasspath(classpathEntriesArray,null);

	}	
	protected void removeFromClasspath(IJavaProject project,IPath containerPath) throws JavaModelException {
		IClasspathEntry[] raw = project.getRawClasspath();
		List classpathEntries = new ArrayList(raw.length);
		for (int i = 0; i < raw.length; i++) {
			// Create a list of existing classpath entries without the container path
			if (!raw[i].getPath().equals(containerPath)){
				classpathEntries.add(raw[i]);
			}
		}
		
		// Now replace the classpath of the project if anything was removed
		if ( classpathEntries.size() != raw.length ) {
			IClasspathEntry[] classpathEntriesArray = (IClasspathEntry[])classpathEntries.toArray(new IClasspathEntry[0]);
			project.setRawClasspath(classpathEntriesArray,null);	
		}		
	}
}
