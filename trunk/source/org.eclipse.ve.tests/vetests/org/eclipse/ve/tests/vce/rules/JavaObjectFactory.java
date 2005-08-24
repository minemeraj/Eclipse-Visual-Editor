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
 *  $RCSfile: JavaObjectFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
package org.eclipse.ve.tests.vce.rules;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.jem.internal.instantiation.base.JavaObjectInstance;
 

/**
 * 
 * @since 1.0.0
 */
public class JavaObjectFactory extends EFactoryImpl {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.impl.EFactoryImpl#basicCreate(org.eclipse.emf.ecore.EClass)
	 */
	  protected EObject basicCreate(EClass eClass) 
	  {
	    JavaObjectInstance result = new JavaObjectInstance();
	    result.eSetClass(eClass);
	    return result;
	  }
}
