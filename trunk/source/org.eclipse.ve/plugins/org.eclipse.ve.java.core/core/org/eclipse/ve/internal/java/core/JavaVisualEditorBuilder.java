/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Jan 20, 2005 by Gili Mendel
 * 
 *  $RCSfile: JavaVisualEditorBuilder.java,v $
 *  $Revision: 1.10 $  $Date: 2005-06-21 22:56:33 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.beaninfo.adapters.BeaninfoAdapterFactory;
import org.eclipse.jem.internal.beaninfo.adapters.BeaninfoNature;
import org.eclipse.jem.internal.beaninfo.core.BeanInfoCacheController;
import org.eclipse.jem.internal.java.beaninfo.IIntrospectionAdapter;
 

/**
 * @since 1.1.0
 * 
 * @deprecated with Eclipse 3.1 we should not require a builder... a clean
 *             notification can be used.
 */
public class JavaVisualEditorBuilder extends IncrementalProjectBuilder {

	private File cacheDir = null;
	private File cacheJet = null;
	
	IPath	currentProjectPath = null;
	IProgressMonitor currentMonitor = null;
	
	protected File getModelCacheDirectory() {
		if (cacheDir!=null)
			return cacheDir;
		cacheDir = JavaVEPlugin.getEMFModelCacheDestination(getProject()).toFile();
		return cacheDir;
	}
	protected File getJetDirectory() {
		if (cacheJet!=null)
			return cacheJet;
		cacheJet = JavaVEPlugin.VE_GENERATED_OBJECTs_DESTINATION.toFile();
		return cacheJet;
	}

	/**
	 * Will delete a File Root Tree
	 * 
	 * @param root  Root of the file tree to delete (can be a directory or a file)
	 * @param deleteRoot  If true, will delete to root and any of it childrens.
	 *                    if false, delete root's content (if a directory) and it childrent
	 * 					  but not root.
	 * 
	 * @since 1.1.0
	 */
	protected boolean deleteDirectoryConntent(File root, boolean deleteRoot) {
		boolean error = false;
		if (root.canRead()) {
			if (root.isDirectory()) {
			 File[] files = root.listFiles();
			 for (int i = 0; i < files.length; i++) { 
				if (files[i].isDirectory())
					error|= deleteDirectoryConntent(files[i],true);
				else {
					currentMonitor.subTask(JavaMessages.JavaVisualEditorBuilder_0+files[i].getName()); 
					error |= !files[i].delete();
					currentMonitor.worked(1);
				}
			 }
			}
			if (deleteRoot) {
			  currentMonitor.subTask(JavaMessages.JavaVisualEditorBuilder_1+root.getName()); 
			  error |= !root.delete();
			  currentMonitor.worked(1);
			}
		}
		else {
			error = true;
		}
		return error;
	}
	protected File appendProjectPath(File root, IPath project) {
		IPath target = new Path(root.getPath());
		target = target.append(project);
		return target.toFile();
	}
	protected boolean  cleanMetaDirectory(IPath root, boolean delRoot) {
			return deleteDirectoryConntent(getModelCacheDirectory(),delRoot);			
	}
	protected void processDelta(IResourceDelta delta) {
		if (delta!=null) {
			currentMonitor.beginTask(JavaMessages.JavaVisualEditorBuilder_2,50); 
			switch (delta.getKind()) {
				// CHANGEs can be taken care of with a time stamp... skip processing folders
				// Keep the build light
			    case IResourceDelta.REMOVED:
			    	if (delta.getResource() instanceof IFolder ||
			    		delta.getResource() instanceof IProject)
			    	   cleanMetaDirectory(currentProjectPath.append(delta.getProjectRelativePath()),true);
			    	else {
			    	   if (delta.getResource() instanceof IFile) {
			    	   	  IFile f = (IFile)delta.getResource();
			    	   	  String name = f.getFileExtension();
			    	   	  if (name.equals("java")) { //$NON-NLS-1$
			    	   	    name = f.getName().substring(0,f.getName().length()-name.length())+"xmi"; //$NON-NLS-1$			    	   	    
			    	   	    cleanMetaDirectory(currentProjectPath.append(delta.getProjectRelativePath().removeLastSegments(1).append(name)),true);
			    	   	  }
			    	   }
			    	}
			}		
			if (delta.getKind()==IResourceDelta.REMOVED && delta.getResource() instanceof IFolder)
				return;  // At least for the delete point of view 
			
			IResourceDelta kids[] = delta.getAffectedChildren();
			for (int i = 0; i < kids.length; i++) {
				processDelta(kids[i]);
			}
			currentMonitor.done();
		}
	}
	/* Warning:	The Visual Editor will add this builder spec. to a project if it does not exists on
	 * 			an editor open.  
	 * 			If added to a project, build() will be called immediatly with a 
	 * 			FULL_BUILD request.... we do not want to add oil to the (first time open) fire, if not
	 * 			really needed.
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		currentProjectPath = new Path(getProject().getLocation().lastSegment());
		currentMonitor = monitor != null ? monitor : new NullProgressMonitor();		
		switch (kind) {
			case INCREMENTAL_BUILD:
			case AUTO_BUILD:
				processDelta(getDelta(getProject()));
		}		
		currentProjectPath=null;
		currentMonitor=null;
		return null;
	}


	/**
	 * Not trying to be over efficient here, clean is called manually by a user
	 */
	protected void clean(IProgressMonitor monitor) throws CoreException {
		currentProjectPath = getProject()!=null ? new Path(getProject().getLocation().lastSegment()):null;
		currentMonitor = monitor != null ? monitor : new NullProgressMonitor();
		monitor.beginTask(JavaMessages.JavaVisualEditorBuilder_3,50); 
		
		if (currentProjectPath!=null) {			
		   cleanMetaDirectory(currentProjectPath,false);
		   // this is an over kill, but will take it on a clean
		   deleteDirectoryConntent(getJetDirectory(),false);
		   // TODO: This is a temporary hack to delete BeanInfo cache
		   deleteDirectoryConntent(BeanInfoCacheController.getCacheDir(getProject()).toFile(),false);
		   deleteDirectoryConntent(BeanInfoCacheController.getCacheDir(null).toFile(),false);
		   markInMemoryBeaninfoStale(getProject());// Mark in-memory beaninfo stale
		   deleteDirectoryConntent(JavaVEPlugin.VE_GENERATED_LIBRARIES_CACHE.toFile(),true);
		}		
		monitor.done();
		currentProjectPath=null;
		currentMonitor=null;
	}
	
	/*
	 * Cleaning the meta directories is not enough - beaninfo is already loaded into the 
	 * memory and it needs to be marked stale also. This API will mark all in-memory 
	 * beaninfo of the project stale.
	 */
	private void markInMemoryBeaninfoStale(IProject project) {
		try {
			BeaninfoNature beaninfoNature = (BeaninfoNature) project.getNature(BeaninfoNature.NATURE_ID);
			if(beaninfoNature!=null && beaninfoNature.getResourceSet()!=null){
				AdapterFactory adapterFactory = EcoreUtil.getAdapterFactory(beaninfoNature.getResourceSet().getAdapterFactories(), IIntrospectionAdapter.ADAPTER_KEY);
				if (adapterFactory instanceof BeaninfoAdapterFactory) {
					BeaninfoAdapterFactory beaninfoAdapterFactory = (BeaninfoAdapterFactory) adapterFactory;
					beaninfoAdapterFactory.markAllStale();
				}
			}
		} catch (CoreException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
	}
}
