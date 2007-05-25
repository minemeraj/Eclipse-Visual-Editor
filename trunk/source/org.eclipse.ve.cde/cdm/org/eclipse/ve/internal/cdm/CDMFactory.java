/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *  $RCSfile: CDMFactory.java,v $
 *  $Revision: 1.6 $  $Date: 2007-05-25 04:09:36 $ 
 */

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.cdm.CDMPackage
 * @generated
 */
public interface CDMFactory extends EFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "";
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CDMFactory eINSTANCE = org.eclipse.ve.internal.cdm.impl.CDMFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Diagram Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Data</em>'.
	 * @generated
	 */
	DiagramData createDiagramData();

	/**
	 * Returns a new object of class '<em>Diagram</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram</em>'.
	 * @generated
	 */
	Diagram createDiagram();

	/**
	 * Returns a new object of class '<em>Visual Info</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Visual Info</em>'.
	 * @generated
	 */
	VisualInfo createVisualInfo();

	/**
	 * Returns a new object of class '<em>Annotation EMF</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotation EMF</em>'.
	 * @generated
	 */
	AnnotationEMF createAnnotationEMF();

	/**
	 * Returns a new object of class '<em>Annotation Generic</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotation Generic</em>'.
	 * @generated
	 */
	AnnotationGeneric createAnnotationGeneric();

	/**
	 * Returns a new object of class '<em>Diagram Figure</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Figure</em>'.
	 * @generated
	 */
	DiagramFigure createDiagramFigure();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CDMPackage getCDMPackage();

} //CDMFactory
