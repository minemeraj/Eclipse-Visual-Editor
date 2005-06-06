/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 *  $Revision: 1.1 $  $Date: 2005-06-06 00:52:37 $ 
 */
package org.eclipse.ve.internal.jface;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

import org.apache.crimson.parser.XMLReaderImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IFileEditorInput;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import org.eclipse.ve.internal.cde.core.EditDomain;


public class PDEUtilities {
	
	EditDomain fEditDomain;
	private XMLReaderImpl xmlReader;
	private HashMap viewClasses = new HashMap();
	private IProject currentProject;
	private static String VIEW_PART_ICON_PATH;	// Location on disk of the project resource /icons/full/clcl16/rcp_app.gif
	private static String EDITOR_PART_ICON_PATH; // Location on disk of the project resource /icons/full/clcl16/rcp_editor.gif
	private long pluginTimeStamp;	// Keep the time stamp so if the plugin.xml changes under us we can refresh it
	private long propertiesTimeStamp;
	
	private PDEUtilities(EditDomain anEditDomain){
		fEditDomain = anEditDomain;
		initialize();
	}
	
	public static PDEUtilities getUtilities(EditDomain anEditDomain){
		PDEUtilities aPDEUtilities = (PDEUtilities)anEditDomain.getData(PDEUtilities.class);
		if(aPDEUtilities == null){
			aPDEUtilities = new PDEUtilities(anEditDomain);
			anEditDomain.setData(PDEUtilities.class,aPDEUtilities);
		}
		return aPDEUtilities;		
	}
	
	public String getViewName(String viewClassName){
		checkCacheNotStale();		
		String[] pluginData = (String[]) viewClasses.get(viewClassName);
		if(pluginData != null){
			// The view name could come from plugin.propeties entry
			String viewName = pluginData[0];
			if(viewName.startsWith("%") && viewName.endsWith("%")){
				// We must open the plugin.properties and find the value associated with the key as the name has been externalized
				IFile propertiesFile = currentProject.getFile("plugin.properties");
				if(!propertiesFile.exists()) return viewName;
				propertiesTimeStamp = propertiesFile.getLocalTimeStamp();
				IPath propertiesFileInSystem = currentProject.getLocation().append(propertiesFile.getProjectRelativePath());
				try{				
					InputStream inputStream = new FileInputStream(propertiesFileInSystem.toString());
					Properties properties = new Properties();
					properties.load(inputStream);
					// Strip the leading % and trailing % before looking up the properties value
					String bundleValue = properties.getProperty(viewName.substring(1,viewName.length()-1));
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
		IFile pluginXMLFile = currentProject.getFile("plugin.xml");
		// If we have a plugin XML file now and it doesn't match the time stamp of the last read one we are stale
		if(pluginXMLFile.exists() && pluginXMLFile.getLocalTimeStamp() != pluginTimeStamp){
			initialize();  // Re read the plugin.xml
			return;
		}
		// If we are not stale from the plugin.xml it is possible the plugin.properties has changed
		// If the properties time stamp is zero we don't need to flush the cache as if required it will just be read fresh
		IFile propertiesXMLFile = currentProject.getFile("plugin.properties");
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
			IFile gifFile = currentProject.getFile("icons/full/clcl16/rcp_app.gif");
			IPath gifLocation = currentProject.getLocation().append(gifFile.getProjectRelativePath());
			VIEW_PART_ICON_PATH = gifLocation.toString();
		}
		return VIEW_PART_ICON_PATH;
	}
	
	public String getEditorPartIconPath(){
		if(EDITOR_PART_ICON_PATH == null){
			IFile gifFile = currentProject.getFile("icons/full/clcl16/rcp_editor.gif");
			IPath gifLocation = currentProject.getLocation().append(gifFile.getProjectRelativePath());
			EDITOR_PART_ICON_PATH = gifLocation.toString();
		}
		return EDITOR_PART_ICON_PATH;
	}
	
	private void initialize(){
		viewClasses = new HashMap();		
		// Find the plugin.xml file if one exists
		IFileEditorInput fileEditorInput = (IFileEditorInput) fEditDomain.getEditorPart().getEditorInput();
		IFile fileBeingEdited = fileEditorInput.getFile();
		currentProject = fileBeingEdited.getProject();		
		IFile pluginXMLFile = currentProject.getFile("plugin.xml");
		if (pluginXMLFile.exists()){
			try{
				pluginTimeStamp = pluginXMLFile.getLocalTimeStamp();
				InputStream pluginXMLis = pluginXMLFile.getContents(true);
				InputSource pluginXMLsource = new InputSource(pluginXMLis);
				xmlReader = new XMLReaderImpl();
				xmlReader.setContentHandler(new DefaultHandler(){
					public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {					
						String tag = localName.trim();
						if (tag.equalsIgnoreCase("view")) {			
							processView(attributes);
						}
					}
				});
				xmlReader.parse(pluginXMLsource);	
				pluginXMLis.close();
			} catch (CoreException e) {
			} catch (IOException e) {
			} catch (SAXException e) {
			}
		}
	}
	private void processView(Attributes attributes){
		// class, icon and name are what we are interested in
		String nameName = attributes.getValue("name");
		String iconName = attributes.getValue("icon"); 
		String className = attributes.getValue("class");
		viewClasses.put(className,new String[] {nameName,iconName});
	}	
}
