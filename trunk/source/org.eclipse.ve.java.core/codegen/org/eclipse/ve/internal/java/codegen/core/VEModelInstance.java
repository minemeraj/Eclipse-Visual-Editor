/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VEModelInstance.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-16 20:55:59 $ 
 */
package org.eclipse.ve.internal.java.codegen.core;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cdm.CDMFactory;
import org.eclipse.ve.internal.cdm.Diagram;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMFactory;

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class VEModelInstance implements IVEModelInstance {
		
	Resource	fResource=null;
	BeanSubclassComposition fRoot = null;
	String		fUri;  // Src File URI
	EditDomain	fEDomain;
	IFile		fInputFile;
	
	
	
	

	public VEModelInstance (IFile file, EditDomain domain) {
		super();
		fInputFile=file;
		fUri = file.getFullPath().toString();
		fEDomain=domain;
	}
	public BeanSubclassComposition getModelRoot() {
		return fRoot;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IVEModelInstance#getModelResource()
	 */
	public Resource getModelResource() {
			return fResource;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IVEModelInstance#getModelResourceSet()
	 */
	public ResourceSet getModelResourceSet() {
		return EMFEditDomainHelper.getResourceSet(fEDomain);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IVEModelInstance#clearModel()
	 */
	public void clearModel() {
		fResource = null;
		if (fRoot != null)
			fRoot.eAdapters().clear();
		fRoot = null;
		fUri = null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IVEModelInstance#createEmptyComposition()
	 */
	public EObject createEmptyComposition() throws CodeGenException {

			if (fUri == null)
				throw new CodeGenException("Model URI is not set"); //$NON-NLS-1$

			ResourceSet rs = getModelResourceSet();
			Resource cr = rs.getResource(URI.createURI(fUri), false);
			if (cr != null)
				rs.getResources().remove(cr);

			fResource = rs.createResource(URI.createURI(fUri));
			fRoot = JCMFactory.eINSTANCE.createBeanSubclassComposition();
			Diagram d = CDMFactory.eINSTANCE.createDiagram();
			d.setId(Diagram.PRIMARY_DIAGRAM_ID);
			fRoot.getDiagrams().add(d);
			fResource.getContents().add(fRoot);
			return fRoot;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IVEModelInstance#getDiagram()
	 */
	public Diagram getDiagram() {
		if (fRoot == null)
			return null;
		List diagrams = fRoot.getDiagrams();
		for (int i = 0; i < diagrams.size(); i++) {
			Diagram element = (Diagram) diagrams.get(i);
			if (Diagram.PRIMARY_DIAGRAM_ID.equals(element.getId())) {
				return element;
			}
		}
		return null;
	}
	public String getURI() {
		return fUri;
	}
	public IFile getFile() {
		return fInputFile;
	}
	
}
