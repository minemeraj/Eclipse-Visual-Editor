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
 *  $RCSfile: VEModelCacheUtility.java,v $
 *  $Revision: 1.16 $  $Date: 2005-06-21 22:18:28 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.ve.internal.jcm.JCMFactory;
import org.eclipse.ve.internal.jcm.JavaCacheData;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;
 

/**
 * 
 * @since 1.0.0
 */
public class VEModelCacheUtility {	
	private static final Map XML_CACHE_SAVE_OPTIONS;
	static {
		// Normally focus on speed not readability
		XML_CACHE_SAVE_OPTIONS = new HashMap(4);
        XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
		XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_SAVE_TYPE_INFORMATION, Boolean.TRUE);
		// /debug/consolelog option in the ve.java.core plugin
		String option = JavaVEPlugin.getPlugin().getBundle().getSymbolicName()+VCEPreferences.DEBUG_CONSOLE_ECHO;
		if ("true".equalsIgnoreCase(Platform.getDebugOption(option)))	     //$NON-NLS-1$
           XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));
		else
		   XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_FORMATTED, Boolean.FALSE);
	}
	
	private static boolean isCacheDirectory = false;
	
	public static IPath getCacheDirectory(IFile f) {
		IProject p = f.getProject();
		if (!isCacheDirectory) {
			File  dir =   JavaVEPlugin.getEMFModelCacheDestination(p).toFile();
			if (dir.exists())
				isCacheDirectory=true;
			else
				isCacheDirectory= dir.mkdirs();
		}
		return isCacheDirectory?JavaVEPlugin.getEMFModelCacheDestination(p) : null;			
	}
	
	public static boolean isValidCache (IFile f) {
		File dest = getCachedPath(f).toFile();		
		if (/* dest.exists() && */dest.canRead()) {
			if (dest.lastModified()>f.getLocalTimeStamp())
				return true;
		}		
	    return false;
	}
	
	protected static IPath getCachedPath(IFile f) {
		IPath savedPath = getCacheDirectory(f).append(f.getProjectRelativePath());
		return savedPath.removeLastSegments(1).append(savedPath.lastSegment().substring(0,savedPath.lastSegment().indexOf('.'))+".xmi"); //$NON-NLS-1$
	}
	
	public static URI getCacheURI (IFile f) {
		return URI.createFileURI(getCachedPath(f).toString());
	}
	
	public static Resource doLoadFromCache (IVEModelInstance model, IProgressMonitor monitor) {
		if (monitor == null)
			monitor = new NullProgressMonitor();
		Resource r = null;
		monitor.beginTask(Messages.VEModelCacheUtility_2+ model.getFile().getName(),3); 
		if (isValidCache(model.getFile())) {		 
		  monitor.worked(1);
		  try {
		  	  URI uri = getCacheURI(model.getFile());
		  	  monitor.worked(1);
			  r = model.getModelResourceSet().getResource(uri,true);			  			  
		  } catch (Exception e) {
			getCachedPath(model.getFile()).toFile().delete();
			JavaVEPlugin.log(e);
		  }
		}
		monitor.done();
		return r;
	}
	
	/*
	 * We need to store in the model a mapping between instance variable and EMF
	 * IJavaObject instance, so that when we load the model up, we can restore instance/local variables
	 * to EMF instances.
	 */
	public static void annotateEMFModelWithCache(IBeanDeclModel bdm) {
		IVEModelInstance model = bdm.getCompositionModel();
		JavaCacheData cache = JCMFactory.eINSTANCE.createJavaCacheData();
		Iterator itr = bdm.getBeans().iterator();
		while (itr.hasNext()) {
			BeanPart bp = (BeanPart) itr.next();
			cache.getNamesToBeans().put(bp.getUniqueName(),bp.getEObject());
		}
		Resource r = model.getModelResource();
		r.getContents().add(cache);
	}
	
	public static JavaCacheData getJavaCacheData (IVEModelInstance model) {
		Resource r = model.getModelResource();		
		for (int i=r.getContents().size()-1; i>=0; i--) {
			Object next = r.getContents().get(i);
			if (next instanceof JavaCacheData) {
				return (JavaCacheData) next;				
			}
		}
		return null;
	}
	
	public static void removeCacheAnnotationFromEMFModel (IVEModelInstance model) {
		Resource r = model.getModelResource();
		r.getContents().remove(getJavaCacheData(model));
	}
	
	public static void doSaveCache (IBeanDeclModel bdm, IProgressMonitor monitor) {
		if (monitor == null)
			monitor = new NullProgressMonitor();		
		monitor.beginTask(Messages.VEModelCacheUtility_3,3); 
		monitor.worked(1);		
		if (bdm!=null && bdm.getCompositionModel()!=null) {
			IVEModelInstance model = bdm.getCompositionModel();
			try {	
				annotateEMFModelWithCache(bdm);
				monitor.worked(1);
				if (model.getModelResource().getURI().equals(getCacheURI(model.getFile()))) {				   				   
				   monitor.worked(1);
				   model.getModelResource().save(XML_CACHE_SAVE_OPTIONS);
				}
				else {
				  File f = getCachedPath(model.getFile()).toFile();
				  monitor.worked(1);
				  monitor.subTask(Messages.VEModelCacheUtility_4+f.getName()); 
				  FileOutputStream os = new FileOutputStream(f);				
				  model.getModelResource().save(os, XML_CACHE_SAVE_OPTIONS);
				  os.close();
				}
			} catch (Exception e) {
				getCachedPath(model.getFile()).toFile().delete();
				JavaVEPlugin.log(e);
			}
			finally{
				removeCacheAnnotationFromEMFModel(model);
			}
		}		
		monitor.done();
	}
	
	public static void removeCache (IVEModelInstance model) {
		if (model!=null) {
		   removeCache(model.getFile());		   
		}
	}
	public static void removeCache (IFile file) {
		if (file != null) {
			getCachedPath(file).toFile().delete();
		}
	}
	
		
}
