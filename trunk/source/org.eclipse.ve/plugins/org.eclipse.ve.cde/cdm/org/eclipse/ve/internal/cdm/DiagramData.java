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
 *  $RCSfile: DiagramData.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */


import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.DiagramData#getDiagrams <em>Diagrams</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.DiagramData#getAnnotations <em>Annotations</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagramData()
 * @model 
 * @generated
 */
public interface DiagramData extends EObject{

	/**
	 * Returns the value of the '<em><b>Diagrams</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cdm.Diagram}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.cdm.Diagram#getDiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagrams</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagrams</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagramData_Diagrams()
	 * @see org.eclipse.ve.internal.cdm.Diagram#getDiagramData
	 * @model type="org.eclipse.ve.internal.cdm.Diagram" opposite="diagramData" containment="true"
	 * @generated
	 */
	EList getDiagrams();

	/**
	 * Returns the value of the '<em><b>Annotations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cdm.Annotation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annotations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annotations</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagramData_Annotations()
	 * @model type="org.eclipse.ve.internal.cdm.Annotation" containment="true"
	 * @generated
	 */
	EList getAnnotations();

}
