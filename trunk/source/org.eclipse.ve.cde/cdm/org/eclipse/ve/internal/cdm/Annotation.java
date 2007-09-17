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
package org.eclipse.ve.internal.cdm;
/*
 *  $RCSfile: Annotation.java,v $
 *  $Revision: 1.6 $  $Date: 2007-09-17 14:17:13 $ 
 */


import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.Annotation#getVisualInfos <em>Visual Infos</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getAnnotation()
 * @model abstract="true"
 * @generated
 */
public interface Annotation extends KeyedValueHolder{

	/**
	 * Returns the value of the '<em><b>Visual Infos</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cdm.VisualInfo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Visual Infos</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Visual Infos</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getAnnotation_VisualInfos()
	 * @model containment="true"
	 * @generated
	 */
	EList<VisualInfo> getVisualInfos();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	VisualInfo getVisualInfo(Diagram aDiagram);

}
