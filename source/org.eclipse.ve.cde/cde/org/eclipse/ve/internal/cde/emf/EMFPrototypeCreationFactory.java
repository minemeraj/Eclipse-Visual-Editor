package org.eclipse.ve.internal.cde.emf;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EMFPrototypeCreationFactory.java,v $
 *  $Revision: 1.4 $  $Date: 2005-05-18 19:31:37 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.requests.CreationFactory;

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
public class EMFPrototypeCreationFactory implements CreationFactory, IDomainedFactory {

	protected URI prototypeURI;
	protected List prototypes; // All of the prototypes from the extent
	protected EObject prototype; // The prototype to return.

	/**
	 * Constructor for EMFPrototypeCreationFactory.
	 * @param aPrototype
	 */
	public EMFPrototypeCreationFactory(String prototypeURIString) {
		setPrototypeURI(URI.createURI(prototypeURIString));
	}
	
	/**
	 * Create using the given resource and the fragment referring to the main prototype.
	 * @param protoTypeFragment
	 * @param prototypeResource
	 * 
	 * @since 1.1.0
	 */
	public EMFPrototypeCreationFactory(String protoTypeFragment, Resource prototypeResource) {
		getPrototypes(protoTypeFragment, prototypeResource);
	}

	public void setPrototypeURI(URI prototypeURI) {
		this.prototypeURI = prototypeURI;
	}

	/**
	 * @see IDomainedFactory#setEditDomain(EditDomain)
	 */
	public void setEditDomain(EditDomain domain) {
		if (prototypeURI != null) {
			// Now that we have a domain, find and set the actual prototype
			// We are being activated, so this is a good time to do it.

			prototype = null;
			prototypes = null;
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
		prototype = res.getEObject(prototypeFragment);
		if (prototype != null) {
			if (prototype.eContainer() != null) {
				prototype = null;
				System.out.println("Prototype from URI is not contained directly by the Resource. Res="+res.getURI()+ " Fragment=" + prototypeFragment); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				prototypes = new ArrayList(res.getContents()); // Make of copy of the list of all of the prototypes.
			}
		}
	}

	/**
	 * @see Factory#getNewObject()
	 */
	public Object getNewObject() {
		if (prototype != null) {
			EcoreUtil.Copier copier = new EcoreUtil.Copier();
			copier.copyAll(prototypes);
			copier.copyReferences();
			return copier.get(prototype);	// Now return the copied prototype, anything it references from the cg will be dragged along (should only be annotations).
		} else
			return null;
	}

	/**
	 * @see Factory#getObjectType()
	 */
	public Object getObjectType() {
		return prototype != null ? prototype.eClass() : null;
	}

}
