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
 *  $RCSfile: VEModelCacheUtility.java,v $
 *  $Revision: 1.19 $  $Date: 2005-09-27 15:12:09 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.jem.util.plugin.JEMUtilPlugin;

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
	
	public static IPath getCacheDirectory(IFile f) {
		addCacheResourceListener();	// Make sure we are listening.
		
		IProject p = f.getProject();
		IPath projectCachePath = VEModelCacheUtility.getEMFModelCacheDestination(p);
		File  dir =  projectCachePath.toFile();
		if (!dir.exists())
			if (dir.mkdirs())
				return projectCachePath;
			else
				return null;
		else
			return projectCachePath;			
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
		return savedPath.removeFileExtension().addFileExtension("xmi"); //$NON-NLS-1$
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
		Iterator itr = bdm.getBeans(false).iterator();
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
	
	private static JEMUtilPlugin.CleanResourceChangeListener cacheResourceListener;
	
	public static void addCacheResourceListener() {
		if (cacheResourceListener == null) {
			JEMUtilPlugin.addCleanResourceChangeListener(cacheResourceListener = new CacheResourceListener(), IResourceChangeEvent.POST_CHANGE);
		}
	}
	
	public static void removeCacheListener() {
		if (cacheResourceListener != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(cacheResourceListener);
			cacheResourceListener = null;
		}
	}
	
	/**
	 * Cache listener to handle the VE model cache.
	 * 
	 * @since 1.1.0
	 */
	private static class CacheResourceListener extends JEMUtilPlugin.CleanResourceChangeListener implements IResourceDeltaVisitor {

		public CacheResourceListener() {
			// Create a cleanup job to handle any that may already be sitting around.
			Job cleanup = new Job(Messages.VEModelCacheUtility_VEModelCache_ReconcileJob) {
				protected IStatus run(IProgressMonitor monitor) {
					Thread currentThread = Thread.currentThread();
					int oldPrio = currentThread.getPriority();
					try {
						currentThread.setPriority(Thread.MIN_PRIORITY);
						// Basically, walk through all open projects, see if there is a ve cache, if there is, see if the folders still exist, 
						// and if not, get rid of them. Finally if the files are gone, get rid of the file entry.
						IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
						monitor.beginTask("", projects.length); //$NON-NLS-1$
						for (int i = 0; i < projects.length; i++) {
							IProject project = projects[i];
							if (project.isOpen())
								processDir(VEModelCacheUtility.getEMFModelCacheDestination(project).toFile(), project, new SubProgressMonitor(monitor, 1));
							monitor.worked(1);
						}
						monitor.done();
						
					} finally {
						currentThread.setPriority(oldPrio);
					}
					return Status.OK_STATUS;
				}
				
				private void processDir(File dir, IContainer folder, IProgressMonitor monitor) {
					if (dir.canRead()) {
						File[] files = dir.listFiles();
						monitor.beginTask("", files.length); //$NON-NLS-1$
						for (int i = 0; i < files.length; i++) {
							File file = files[i];
							if (file.isDirectory()) {
								IResource resource = folder.findMember(file.getName());
								// See if it exists as folder in the workspace. If it does, go on and process it. IF not, then delete the cache folder.
								if (resource != null && resource.getType() == IResource.FOLDER)
									processDir(file, (IContainer) resource, new SubProgressMonitor(monitor, 1));	
								else
									JEMUtilPlugin.deleteDirectoryContent(file, true, new SubProgressMonitor(monitor, 1));	
									
							} else {
								// See if the extension is "xmi". If so then see if there is a cooresponding "java" in the workspace.
								IPath path = new Path(file.getName());
								if ("xmi".equals(path.getFileExtension())) { //$NON-NLS-1$
									IResource resource = folder.findMember(path.removeFileExtension().addFileExtension("java")); //$NON-NLS-1$
									if (resource == null || resource.getType() != IResource.FILE)
										JEMUtilPlugin.deleteDirectoryContent(file, true, new SubProgressMonitor(monitor, 1));
								}
							}
							monitor.worked(1);
						}
						monitor.done();
					}					
				}
			};
			cleanup.setPriority(Job.LONG);
			cleanup.schedule(60*1000);	// Schedule for one minute later.
		}
		
		protected void cleanProject(IProject project) {
			File projectCacheDir = VEModelCacheUtility.getEMFModelCacheDestination(project).toFile();
			JEMUtilPlugin.deleteDirectoryContent(projectCacheDir, false, new NullProgressMonitor());
		}
		
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				IResourceDelta delta = event.getDelta();
				if (delta != null) {
					try {
						delta.accept(this);
					} catch (CoreException e) {
						JavaVEPlugin.log(e);
					}
				}
			} else
				super.resourceChanged(event);
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource res = delta.getResource();
			switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					return false;	// Adds notifications may be too late. We don't want to erase an old cache entry because it may already of been recreated and is good.
				case IResourceDelta.REMOVED:
					switch (res.getType()) {
						case IResource.PROJECT:
							return false;	// removed projects will automatically have their cache cleaned up.
						case IResource.FOLDER:
							// If the folder/project are removed, get rid of any old cache that might of been laying around.
							JEMUtilPlugin.deleteDirectoryContent(VEModelCacheUtility.getEMFModelCacheDestination(res.getProject()).append(res.getProjectRelativePath()).toFile(), true, new NullProgressMonitor());
							return false;	// Don't bother with children.
						case IResource.FILE:
							if ("java".equals(res.getFileExtension())) { //$NON-NLS-1$
								// If the file is removed, get rid of any old cache that might of been laying around.
								JEMUtilPlugin.deleteDirectoryContent(VEModelCacheUtility.getEMFModelCacheDestination(res.getProject()).append(res.getProjectRelativePath()).removeFileExtension().addFileExtension("xmi").toFile(), true, new NullProgressMonitor());	//$NON-NLS-1$ //$NON-NLS-2$							
							}
							return false;
					}
					break;
				case IResourceDelta.CHANGED:
					break;
			}
			return true;
		}
	}

	protected static IPath getEMFModelCacheDestination(IProject p){
		return p.getWorkingLocation(JavaVEPlugin.getPlugin().getBundle().getSymbolicName()).append(VEModelCacheUtility.VE_PROJECT_MODEL_CACHE_ROOT);
	}

	protected static final IPath VE_PROJECT_MODEL_CACHE_ROOT = JavaVEPlugin.VE_CACHE_ROOT_NAME.append("emfmodel"); //$NON-NLS-1$ 
		
}
