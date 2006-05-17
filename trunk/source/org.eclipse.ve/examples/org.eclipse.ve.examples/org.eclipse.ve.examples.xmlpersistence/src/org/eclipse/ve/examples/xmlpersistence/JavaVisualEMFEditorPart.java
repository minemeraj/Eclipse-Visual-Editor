/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.xmlpersistence;

import org.eclipse.core.resources.IMarker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.ve.internal.cde.emf.EMFGraphicalEditorPart;
import org.eclipse.ve.internal.cdm.CDMFactory;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.jcm.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.jem.internal.beaninfo.adapters.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ve.internal.cde.properties.*;
import org.eclipse.jem.internal.instantiation.base.*;

public class JavaVisualEMFEditorPart extends EMFGraphicalEditorPart {
	
	protected BeanProxyAdapterFactory beanProxyAdapterFactory;	

	protected Resource createEmptyResource(String filename, ResourceSet rset) {
		Resource resource = rset.createResource(URI.createURI(filename));
		BeanComposition fRoot = JCMFactory.eINSTANCE.createBeanSubclassComposition();
		Diagram d = CDMFactory.eINSTANCE.createDiagram();
		d.setId(Diagram.PRIMARY_DIAGRAM_ID);
		fRoot.getDiagrams().add(d);
		resource.getContents().add(fRoot);					
		return resource;
	}

	protected EObject getModel(Resource doc) {
		return (EObject) doc.getContents().get(0);
	}

	protected EditPart getEditPart(EObject rootModel) {
		EditPart result = new FreeformGraphicalRootEditPart();
		result.setModel(rootModel);
		return result;
		
	}
	


	protected void initialize(IFile file){

		IProject proj = file.getProject();
		BeaninfoNature nature = BeaninfoNature.getRuntime(proj);
		ResourceSet rs = nature.newResourceSet();
		EMFEditDomainHelper.setResourceSet(rs, getEditDomain());
		getEditDomain().setData(new Integer(1), nature);	// Need to save the nature so we can check for validity later if project is renamed.
		
		// 	Save the JavaProject for later use
		JavaEditDomainHelper.setJavaProject(JavaCore.create(proj), getEditDomain());
	
		// Add the property source and property descriptor adapter factories
		rs.getAdapterFactories().add(new PropertySourceAdapterFactory(getEditDomain()));
		rs.getAdapterFactories().add(new PropertyDescriptorAdapterFactory());
	
		// Initialize the nature for the project that creates a remote VM and registers the 
		// 	java:/ protocol in MOF
		JavaInstantiation.initialize(nature.getResourceSet());
	
		beanProxyAdapterFactory = new BeanProxyAdapterFactory(null, getEditDomain(), new BasicAllocationProcesser());		
		// 	Make sure that there is an AdaptorFactory for BeanProxies installed
		rs.getAdapterFactories().add(beanProxyAdapterFactory);
	
		// Create the target VM that is used for instances within the document we open
		startCreateProxyFactoryRegistry(file);
				
		if (modelSynchronizer != null) {
			modelSynchronizer.setProject(JavaCore.create(proj));
		}
	}

	private EditDomain getEditDomain() {
		return (EditDomain)domain;
	}

	protected EditPart getTreeEditPart(EObject rootModel) { 
		return null;
	}

	public void gotoMarker(IMarker marker) {
		// TODO Auto-generated method stub

	}

	protected void handleResourceChanged() {
		// TODO Auto-generated method stub

	}

	protected void validateModel(EObject model) {
		// TODO Auto-generated method stub

	}

}
