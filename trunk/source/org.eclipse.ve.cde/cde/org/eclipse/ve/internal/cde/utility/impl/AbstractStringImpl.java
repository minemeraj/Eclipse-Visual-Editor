package org.eclipse.ve.internal.cde.utility.impl;
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
 *  $RCSfile: AbstractStringImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:00 $ 
 */
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.ve.internal.cde.utility.AbstractString;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract String</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class AbstractStringImpl extends EObjectImpl implements AbstractString {

	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected AbstractStringImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UtilityPackage.eINSTANCE.getAbstractString();
	}

	public String getStringValue() {
		throw new UnsupportedOperationException("Subclasses must implement"); //$NON-NLS-1$
	}

}