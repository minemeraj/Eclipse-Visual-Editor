package org.eclipse.ve.internal.cdm;
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
 *  $RCSfile: AnnotationGeneric.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */
 
 import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation Generic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.AnnotationGeneric#getAnnotatesID <em>Annotates ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getAnnotationGeneric()
 * @model 
 * @generated
 */
public interface AnnotationGeneric extends Annotation {


	/**
	 * Returns the value of the '<em><b>Annotates ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annotates ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annotates ID</em>' attribute.
	 * @see #isSetAnnotatesID()
	 * @see #unsetAnnotatesID()
	 * @see #setAnnotatesID(String)
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getAnnotationGeneric_AnnotatesID()
	 * @model unsettable="true"
	 * @generated
	 */
	String getAnnotatesID();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cdm.AnnotationGeneric#getAnnotatesID <em>Annotates ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annotates ID</em>' attribute.
	 * @see #isSetAnnotatesID()
	 * @see #unsetAnnotatesID()
	 * @see #getAnnotatesID()
	 * @generated
	 */
	void setAnnotatesID(String value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.cdm.AnnotationGeneric#getAnnotatesID <em>Annotates ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAnnotatesID()
	 * @see #getAnnotatesID()
	 * @see #setAnnotatesID(String)
	 * @generated
	 */
	void unsetAnnotatesID();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.cdm.AnnotationGeneric#getAnnotatesID <em>Annotates ID</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Annotates ID</em>' attribute is set.
	 * @see #unsetAnnotatesID()
	 * @see #getAnnotatesID()
	 * @see #setAnnotatesID(String)
	 * @generated
	 */
	boolean isSetAnnotatesID();

}
