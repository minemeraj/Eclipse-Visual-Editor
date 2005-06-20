package org.eclipse.ve.internal.cde.emf;
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
/*
 *  $RCSfile: EMFPrototypeFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-20 23:54:40 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.requests.CreationFactory;
/**
 * Creation factory for creating EMF objects from prototypes.
 * <p>
 * It can have auxiliary prototype objects (typically annotations) that will be copied too.
 * 
 * @since 1.0.0
 */
public class EMFPrototypeFactory implements CreationFactory {
	private EObject prototype;	// The main object
	private List prototypes;	// Any auxiliary objects such as annotations.

	/**
	 * Create with no prototype. 
	 * <p>
	 * {@link EMFPrototypeFactory#setPrototype(EObject, List)} must be called
	 * before getNewObject is called.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public EMFPrototypeFactory() {
		
	}
	
	/**
	 * Create with just the prototype.
	 * @param aPrototype
	 * 
	 * @since 1.1.0
	 */
	public EMFPrototypeFactory(EObject aPrototype) {
		this(aPrototype, null);
	}
	
	/**
	 * Create with prototype and prototypes.
	 * @param aPrototype
	 * @param prototypes
	 * 
	 * @since 1.1.0
	 */
	public EMFPrototypeFactory(EObject aPrototype, List prototypes) {
		setPrototype(aPrototype, prototypes);
	}

	/**
	 * Set the prototype/prototypes.
	 * @param prototype
	 * @param prototypes auxiliary prototypes such as annotations. Prototype <b>MAY</b> be in the list also. It is not an error if it is. If no auxiliary prototypes, this may be <code>null</code>.
	 * 
	 * @since 1.1.0
	 */
	public void setPrototype(EObject prototype, List prototypes) {
		this.prototype = prototype;
		this.prototypes = prototypes;
	}


	public Object getNewObject() {
		if (prototype != null) {
			EcoreUtil.Copier copier = new EcoreUtil.Copier();
			if (prototypes == null || !prototypes.contains(prototype))
				copier.copy(prototype);
			if (prototypes != null)
				copier.copyAll(prototypes);
			copier.copyReferences();
			return copier.get(prototype);	// Now return the copied prototype, anything it references from the cg will be dragged along (should only be annotations).
		} else
			return null;
	}

	public Object getObjectType() {
		return prototype != null ? prototype.eClass() : null;
	}
	
}
