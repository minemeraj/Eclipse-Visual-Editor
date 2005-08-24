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
 *  $RCSfile: EMFClassCreationFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * This is a creation factory that takes a string referencing
 * the EMF class to create. The string is in the form of an
 * href (i.e. "xyz.xmi#XYZClass"). This is called the URI form.
 * This is useful when there are a large number of classes in the palette and
 * not all are to be loaded at startup. This factory won't load the EClass until
 * actually required.
 * @version 	1.0
 * @author
 */
public class EMFClassCreationFactory extends EMFCreationFactory implements IDomainedFactory {

	protected String className;

	/**
	 * Constructor for EMFCreationStringFactory.
	 * @param aClassName
	 */
	public EMFClassCreationFactory(String aClassName) {
		super(null);

		setObjectTypeName(aClassName);
	}

	public void setObjectTypeName(String aClassName) {
		className = aClassName;
	}

	/**
	 * @see IDomainedFactory#setEditDomain(EditDomain)
	 */
	public void setEditDomain(EditDomain domain) {
		// Now that we have a domain, find and set the actual class.
		// We are being activated, so this is a good time to do it.

		ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
		setObjectType((EClass) rset.getEObject(URI.createURI(className), true));
	}

}
