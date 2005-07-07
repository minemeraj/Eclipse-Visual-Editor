/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.core;
/*
 *  $RCSfile: IVEModelInstance.java,v $
 *  $Revision: 1.5 $  $Date: 2005-02-15 23:28:35 $ 
 */

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

/**
 *  A place holder of a Composition Model
 */
public interface IVEModelInstance {
	
		
	
	// Get the root element of the Model	
	public BeanSubclassComposition getModelRoot() ; 
	
	// Get the model's MOF document resource	
	public Resource    getModelResource() ;  
	
	// Get the Resource Set
	public ResourceSet getModelResourceSet () ;
	
	// Erase the current Model
	public void		clearModel () ;
	
	// Create an empty composition Model.
	public EObject	createComposition(boolean ignoreCache) 	throws CodeGenException ;	 
	
	public Diagram getDiagram() ;
	
	public String getURI() ;
	
	public IFile getFile() ;
	
	// Was this model isloaded from a cache
	public boolean isFromCache();
	
	// clear Cache informtion
	public void loadFromCacheComplete();
	
	

}

