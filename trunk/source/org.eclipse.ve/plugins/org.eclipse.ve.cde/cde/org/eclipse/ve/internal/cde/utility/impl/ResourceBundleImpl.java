package org.eclipse.ve.internal.cde.utility.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ResourceBundleImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.ve.internal.cde.utility.ResourceBundle;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Bundle</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class ResourceBundleImpl extends EObjectImpl implements ResourceBundle {

	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected ResourceBundleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UtilityPackage.eINSTANCE.getResourceBundle();
	}

	public java.util.ResourceBundle getBundle() {
		throw new IllegalStateException("Subclasses must implement this"); //$NON-NLS-1$
	}

}
