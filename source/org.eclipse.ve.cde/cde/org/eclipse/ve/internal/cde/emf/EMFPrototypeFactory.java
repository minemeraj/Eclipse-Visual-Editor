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
 *  $RCSfile: EMFPrototypeFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:58 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.requests.CreationFactory;
/**
 * Creation factory for creating EMF objects from prototypes.
 */
public class EMFPrototypeFactory implements CreationFactory {
	protected EObject fPrototype;

	public EMFPrototypeFactory(EObject aPrototype) {
		setPrototype(aPrototype);
	}

	public void setPrototype(EObject aPrototype) {
		fPrototype = aPrototype;
	}

	/**
	 * getNewObject method comment. 
	 */
	public Object getNewObject() {
		if (fPrototype == null)
			return null;
		return EcoreUtil.copy(fPrototype);
	}

	public Object getObjectType() {

		return fPrototype.eClass();

	}

}
