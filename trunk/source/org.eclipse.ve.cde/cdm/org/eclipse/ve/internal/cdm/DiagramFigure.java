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
 *  $RCSfile: DiagramFigure.java,v $
 *  $Revision: 1.7 $  $Date: 2007-05-25 04:09:36 $ 
 */

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Figure</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A figure on the diagram. This is used when the structure of objects in the diagram are not determined by the object model but are specified on a per diagram basis.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.DiagramFigure#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.DiagramFigure#getChildFigures <em>Child Figures</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagramFigure()
 * @model
 * @generated
 */
public interface DiagramFigure extends KeyedValueHolder{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "";

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Indicate the type of the figure. This is used by the view to know the type of figure. It can be used to prevent the wrong type of figure being dropped on another figure. Or it can be used to determine the type of EditPart to create for it.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagramFigure_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cdm.DiagramFigure#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Child Figures</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cdm.DiagramFigure}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child Figures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Figures</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagramFigure_ChildFigures()
	 * @model type="org.eclipse.ve.internal.cdm.DiagramFigure" containment="true"
	 * @generated
	 */
	EList<DiagramFigure> getChildFigures();

} // DiagramFigure
