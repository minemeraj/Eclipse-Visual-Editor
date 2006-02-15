/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PDEUtilities.java,v $
 *  $Revision: 1.7 $  $Date: 2006-02-15 16:11:47 $ 
 */
package org.eclipse.ve.internal.java.vce;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IFileEditorInput;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.eclipse.ve.internal.cde.core.EditDomain;

import com.ibm.icu.util.ULocale;


public class PDEUtilities {
	// Note: This should not really be in the package it is.  The problem is the JavaBeanLaunchConfigurationDelegate and
	// the fact that this has to prep all of the -D arguments to the target VM launchers from a common point.  Until we refactor and
	// publish the launcher API this will have to live here for now
	private HashMap viewClasses = new HashMap();
	private IProject currentProject;
	private static String VIEW_PART_ICON_PATH;	// Location on disk of the project resource /icons/full/clcl16/rcp_app.gif
	private static String EDITOR_PART_ICON_PATH; // Location on disk of the project resource /icons/full/clcl16/rcp_editor.gif
	private long pluginTimeStamp;	// Keep the time stamp so if the plugin.xml changes under us we can refresh it
	private long propertiesTimeStamp;
	
	private PDEUtilities(EditDomain anEditDomain){
		// Find the plugin.xml file if one exists
		IFileEditorInput fileEditorInput = (IFileEditorInput) anEditDomain.getEditorPart().getEditorInput();
		IFile fileBeingEdited = fileEditorInput.getFile();
		currentProject = fileBeingEdited.getProject();		
	}
	
	private PDEUtilities(IProject aProject){
		currentProject = aProject;
	}
	
	public static PDEUtilities getUtilities(EditDomain anEditDomain){
		PDEUtilities aPDEUtilities = (PDEUtilities)anEditDomain.getData(PDEUtilities.class);
		if(aPDEUtilities == null){
			aPDEUtilities = new PDEUtilities(anEditDomain);
			anEditDomain.setData(PDEUtilities.class,aPDEUtilities);
			aPDEUtilities.initialize();			
		}
		return aPDEUtilities;		
	}
	
	public static PDEUtilities getUtilities(IProject aProject){
		PDEUtilities aPDEUtilities = new PDEUtilities(aProject);
		aPDEUtilities.initialize();
		return aPDEUtilities;
	}
	
	public String getViewName(String viewClassName){
		checkCacheNotStale();		
		String[] pluginData = (String[]) viewClasses.get(viewClassName);
		if(pluginData != null){
			// The view name could come from plugin.propeties entry
			String viewName = pluginData[0];
			if(viewName.startsWith("%")){ //$NON-NLS-1$
				// We must open the plugin.properties and find the value associated with the key as the name has been externalized
				IFile propertiesFile = currentProject.getFile("plugin.properties"); //$NON-NLS-1$
				if(!propertiesFile.exists()) return viewName;
				propertiesTimeStamp = propertiesFile.getLocalTimeStamp();
				IPath propertiesFileInSystem = currentProject.getLocation().append(propertiesFile.getProjectRelativePath());
				try{				
					InputStream inputStream = new FileInputStream(propertiesFileInSystem.toString());
					Properties properties = new Properties();
					properties.load(inputStream);
					// Strip the leading % and trailing % before looking up the properties value
					String propertyKey = viewName.substring(1);
					if(propertyKey.endsWith("%")) propertyKey = propertyKey.substring(0,propertyKey.length()-1); //$NON-NLS-1$
					String bundleValue = properties.getProperty(propertyKey);
					if(bundleValue == null) return viewName;
					// We have a bundle value.  Substitute it in the map so we don't have to re-retrieve it
					pluginData[0] = bundleValue;
					return bundleValue;
				} catch (IOException e){
					return viewName;					
				}
			} else {
				return viewName;
			}
		} else {
			return null;
		}
	}
	
	private void checkCacheNotStale(){		
		IFile pluginXMLFile = currentProject.getFile("plugin.xml"); //$NON-NLS-1$
		// If we have a plugin XML file now and it doesn't match the time stamp of the last read one we are stale
		if(pluginXMLFile.exists() && pluginXMLFile.getLocalTimeStamp() != pluginTimeStamp){
			initialize();  // Re read the plugin.xml
			return;
		}
		// If we are not stale from the plugin.xml it is possible the plugin.properties has changed
		// If the properties time stamp is zero we don't need to flush the cache as if required it will just be read fresh
		IFile propertiesXMLFile = currentProject.getFile("plugin.properties"); //$NON-NLS-1$
		if(propertiesXMLFile.exists() && propertiesTimeStamp != 0 && propertiesXMLFile.getLocalTimeStamp() != propertiesTimeStamp){
			// Reparse the plugin.xml which will load up the the names that when accessed will be read from the changed .properties file
			initialize();
		}
	}
	
	public String getIconPath(String viewClassName){
		checkCacheNotStale();
		String[] pluginData = (String[]) viewClasses.get(viewClassName);
		if(pluginData != null){
			String iconLocationRelativeToProject = pluginData[1];
			if(iconLocationRelativeToProject != null){
				IFile iconFile = currentProject.getFile(iconLocationRelativeToProject);
				IPath locationInSystem = currentProject.getLocation().append(iconFile.getProjectRelativePath());
				return locationInSystem.toString();
			}
		} 
		return null;		
	}
	
	public String getViewPartIconPath(){
		if(VIEW_PART_ICON_PATH == null){
			IFile gifFile = currentProject.getFile("icons/full/clcl16/rcp_app.gif"); //$NON-NLS-1$
			IPath gifLocation = currentProject.getLocation().append(gifFile.getProjectRelativePath());
			VIEW_PART_ICON_PATH = gifLocation.toString();
		}
		return VIEW_PART_ICON_PATH;
	}
	
	public String getEditorPartIconPath(){
		if(EDITOR_PART_ICON_PATH == null){
			IFile gifFile = currentProject.getFile("icons/full/clcl16/rcp_editor.gif"); //$NON-NLS-1$
			IPath gifLocation = currentProject.getLocation().append(gifFile.getProjectRelativePath());
			EDITOR_PART_ICON_PATH = gifLocation.toString();
		}
		return EDITOR_PART_ICON_PATH;
	}
	
	private synchronized void initialize(){
		viewClasses = new HashMap();		
		IFile pluginXMLFile = currentProject.getFile("plugin.xml"); //$NON-NLS-1$
		if (pluginXMLFile.exists()){
			try{
				pluginTimeStamp = pluginXMLFile.getLocalTimeStamp();
				InputStream pluginXMLis = pluginXMLFile.getContents(true);
				DefaultHandler xmlHandler = new DefaultHandler(){
					static final int VIEW = 1;
					static final int EDITOR = 2;					
					static final int DEFAULT = 0;				
					private int processing = DEFAULT;	// Flag to show what kind of extension point we are parsing currently					
					public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
						String tag = qName.trim().toLowerCase(ULocale.US.toLocale()); // Always use US locale for system strings
						// If we are an extension of "org.eclipse.ui.views" then flag this as our next set of tags
						if(tag.equals("extension")){ //$NON-NLS-1$
							String extensionPointName = attributes.getValue("point"); //$NON-NLS-1$
							if(extensionPointName == null) return;
							if(extensionPointName.equalsIgnoreCase("org.eclipse.ui.views")){ //$NON-NLS-1$
								processing = VIEW;
							} else if (extensionPointName.equalsIgnoreCase("org.eclipse.ui.editors")){ //$NON-NLS-1$
								processing = EDITOR;								
							} else {
								processing = DEFAULT;
							}
						}
						if (tag.equalsIgnoreCase("view") && processing == VIEW){			 //$NON-NLS-1$
							processView(attributes);
						} else if (tag.equalsIgnoreCase("editor") && processing == EDITOR){ //$NON-NLS-1$
							processView(attributes);
						}
					}
				};
				SAXParserFactory.newInstance().newSAXParser().parse(pluginXMLis, xmlHandler);
			} catch (IOException exc){
			} catch (SAXException e) {
			} catch (ParserConfigurationException e) {
			} catch (FactoryConfigurationError e) {
			} catch (CoreException e) {
			}
		}
	}	
	private void processView(Attributes attributes){
		// class, icon and name are what we are interested in
		String nameName = attributes.getValue("name"); //$NON-NLS-1$
		String iconName = attributes.getValue("icon");  //$NON-NLS-1$
		String className = attributes.getValue("class"); //$NON-NLS-1$
		viewClasses.put(className,new String[] {nameName,iconName});
	}
}
