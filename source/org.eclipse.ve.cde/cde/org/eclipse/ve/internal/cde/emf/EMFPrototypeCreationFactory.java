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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: EMFPrototypeCreationFactory.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.util.logger.proxy.Logger;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * This is a prototype factory that takes the URI to the prototype
 * and using the EditDomain to find the resourceset, will load it
 * an instance. It will not keep the resource set loaded, it will
 * load it each time and then after getting prototype out of the
 * set, it will unload it. That way we don't keep infrequent objects
 * loaded when not needed. The URI should be in a disposable XMI file,
 * i.e. not in one that the editor may need later. It should be entirely
 * self-contained and should have at least one object directly in the resource, the prototype.
 * The resource may contain other objects, these will be copied when the
 * prototype is copied.
 * @version 	1.0
 * @author
 */
public class EMFPrototypeCreationFactory extends EMFPrototypeFactory implements IDomainedFactory {

	protected URI prototypeURI;

	/**
	 * Constructor for EMFPrototypeCreationFactory.
	 * @param aPrototype
	 */
	public EMFPrototypeCreationFactory(String prototypeURIString) {
		setPrototypeURI(URI.createURI(prototypeURIString));
	}
	
	/**
	 * Create using the given resource and the fragment referring to the main prototype.
	 * Unlike a prototype string, this will access the resource immediately to retrieve the prototypes.
	 * @param protoTypeFragment
	 * @param prototypeResource
	 * 
	 * @since 1.1.0
	 */
	public EMFPrototypeCreationFactory(String protoTypeFragment, Resource prototypeResource) {
		getPrototypes(protoTypeFragment, prototypeResource);
	}

	/**
	 * Set the URI for the prototype.
	 * @param prototypeURI
	 * 
	 * @since 1.1.0
	 */
	public void setPrototypeURI(URI prototypeURI) {
		this.prototypeURI = prototypeURI;
	}

	public void setEditDomain(EditDomain domain) {
		if (prototypeURI != null) {
			// Now that we have a domain, find and set the actual prototype
			// We are being activated, so this is a good time to do it.

			setPrototype(null, null);
			if (prototypeURI != null) {
				ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
				Resource res = rset.getResource(prototypeURI.trimFragment(), true);
				if (res != null) {
					getPrototypes(prototypeURI.fragment(), res);
					rset.getResources().remove(res); //Dispose of the resource.
				}
			}
		}
	}

	/**
	 * @param res
	 * 
	 * @since 1.1.0
	 */
	private void getPrototypes(String prototypeFragment, Resource res) {
		EObject prototype = res.getEObject(prototypeFragment);
		if (prototype != null) {
			List prototypes = null;
			if (prototype.eContainer() != null) {
				prototype = null;
				Logger logger = CDEPlugin.getPlugin().getLogger();
				if (logger.isLoggingLevel(Level.INFO))
					logger.log("Prototype from URI is not contained directly by the Resource. Res="+res.getURI()+ " Fragment=" + prototypeFragment, Level.INFO); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				prototypes = res.getContents();
			}
		
			// Make a copy so that they are not pointing to the resource. That would hold the resource in memory too.
			EcoreUtil.Copier copier = new EcoreUtil.Copier();
			if (prototypes == null || !prototypes.contains(prototype))
				copier.copy(prototype);
			if (prototypes != null)
				copier.copyAll(prototypes);
			copier.copyReferences();
			List copyPrototypes = null;
			if (prototypes != null && !prototypes.isEmpty()) {
				copyPrototypes = new ArrayList(prototypes.size());
				for (int i = 0; i < prototypes.size(); i++) {
					copyPrototypes.add(copier.get(prototypes.get(i)));
				}
			}
			
			setPrototype((EObject) copier.get(prototype), copyPrototypes);
		}
	}

}
