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
 *  $RCSfile: Diagram.java,v $
 *  $Revision: 1.5 $  $Date: 2007-05-25 04:09:36 $ 
 */

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.Diagram#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.Diagram#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.Diagram#getDiagramData <em>Diagram Data</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.Diagram#getVisualInfos <em>Visual Infos</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.Diagram#getFigures <em>Figures</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagram()
 * @model
 * @generated
 */
public interface Diagram extends KeyedValueHolder{

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "";
	// A default id to use for a diagram. It can be used for the primary diagram, if desired.	
	// It is how the diagram is found. It would usually be used for the diagram that goes with
	// the graph viewer of the model itself. This would be an easy way to find it, but it is not necessary.	
	public static final String PRIMARY_DIAGRAM_ID = "org.eclipse.ve.internal.cdm.primarydiagram"; //$NON-NLS-1$
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * The default value is <code>" "</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagram_Name()
	 * @model default=" "
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cdm.Diagram#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagram_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cdm.Diagram#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Diagram Data</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.cdm.DiagramData#getDiagrams <em>Diagrams</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Data</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Data</em>' container reference.
	 * @see #setDiagramData(DiagramData)
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagram_DiagramData()
	 * @see org.eclipse.ve.internal.cdm.DiagramData#getDiagrams
	 * @model opposite="diagrams"
	 * @generated
	 */
	DiagramData getDiagramData();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cdm.Diagram#getDiagramData <em>Diagram Data</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Data</em>' container reference.
	 * @see #getDiagramData()
	 * @generated
	 */
	void setDiagramData(DiagramData value);

	/**
	 * Returns the value of the '<em><b>Visual Infos</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cdm.VisualInfo}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.cdm.VisualInfo#getDiagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Visual Infos</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Visual Infos</em>' reference list.
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagram_VisualInfos()
	 * @see org.eclipse.ve.internal.cdm.VisualInfo#getDiagram
	 * @model type="org.eclipse.ve.internal.cdm.VisualInfo" opposite="diagram"
	 * @generated
	 */
	EList<VisualInfo> getVisualInfos();

	/**
	 * Returns the value of the '<em><b>Figures</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cdm.DiagramFigure}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Figures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Figures</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getDiagram_Figures()
	 * @model type="org.eclipse.ve.internal.cdm.DiagramFigure" containment="true"
	 * @generated
	 */
	EList<DiagramFigure> getFigures();

}
