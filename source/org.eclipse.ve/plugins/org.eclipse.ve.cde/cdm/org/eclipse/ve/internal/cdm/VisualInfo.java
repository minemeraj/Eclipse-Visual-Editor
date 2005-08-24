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
 *  $RCSfile: VisualInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Visual Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.VisualInfo#getDiagram <em>Diagram</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getVisualInfo()
 * @model 
 * @generated
 */
public interface VisualInfo extends KeyedValueHolder{
	/**
	 * Returns the value of the '<em><b>Diagram</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.cdm.Diagram#getVisualInfos <em>Visual Infos</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram</em>' reference.
	 * @see #setDiagram(Diagram)
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getVisualInfo_Diagram()
	 * @see org.eclipse.ve.internal.cdm.Diagram#getVisualInfos
	 * @model opposite="visualInfos" required="true"
	 * @generated
	 */
	Diagram getDiagram();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cdm.VisualInfo#getDiagram <em>Diagram</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram</em>' reference.
	 * @see #getDiagram()
	 * @generated
	 */
	void setDiagram(Diagram value);

} // VisualInfo
