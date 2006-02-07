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
package org.eclipse.ve.internal.cde.utility.impl;
/*
 *  $RCSfile: ResourceBundleImpl.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-07 17:21:33 $ 
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
		return UtilityPackage.Literals.RESOURCE_BUNDLE;
	}

	public java.util.ResourceBundle getBundle() {
		throw new IllegalStateException("Subclasses must implement this"); //$NON-NLS-1$
	}

}
