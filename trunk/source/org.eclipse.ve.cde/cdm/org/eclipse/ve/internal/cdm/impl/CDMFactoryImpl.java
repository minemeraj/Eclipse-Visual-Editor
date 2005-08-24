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
package org.eclipse.ve.internal.cdm.impl;
/*
 *  $RCSfile: CDMFactoryImpl.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:49 $ 
 */

import org.eclipse.ve.internal.cdm.*;

import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.ve.internal.cdm.model.Dimension;
import org.eclipse.ve.internal.cdm.model.Point;
import org.eclipse.ve.internal.cdm.model.Rectangle;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CDMFactoryImpl extends EFactoryImpl implements CDMFactory {
	/**
	 * Creates and instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CDMFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case CDMPackage.DIAGRAM_DATA: return createDiagramData();
			case CDMPackage.DIAGRAM: return createDiagram();
			case CDMPackage.VISUAL_INFO: return createVisualInfo();
			case CDMPackage.KEYED_LOCATION: return (EObject)createKeyedLocation();
			case CDMPackage.KEYED_SIZE: return (EObject)createKeyedSize();
			case CDMPackage.KEYED_CONSTRAINT: return (EObject)createKeyedConstraint();
			case CDMPackage.KEYED_POINTS: return (EObject)createKeyedPoints();
			case CDMPackage.ANNOTATION_EMF: return createAnnotationEMF();
			case CDMPackage.ANNOTATION_GENERIC: return createAnnotationGeneric();
			case CDMPackage.DIAGRAM_FIGURE: return createDiagramFigure();
			case CDMPackage.KEYED_GENERIC: return (EObject)createKeyedGeneric();
			case CDMPackage.KEYED_INTEGER: return (EObject)createKeyedInteger();
			case CDMPackage.KEYED_DYNAMIC: return (EObject)createKeyedDynamic();
			case CDMPackage.KEYED_BOOLEAN: return (EObject)createKeyedBoolean();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case CDMPackage.VIEW_DIMENSION:
				return createViewDimensionFromString(eDataType, initialValue);
			case CDMPackage.VIEW_POINT:
				return createViewPointFromString(eDataType, initialValue);
			case CDMPackage.VIEW_RECTANGLE:
				return createViewRectangleFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case CDMPackage.VIEW_DIMENSION:
				return convertViewDimensionToString(eDataType, instanceValue);
			case CDMPackage.VIEW_POINT:
				return convertViewPointToString(eDataType, instanceValue);
			case CDMPackage.VIEW_RECTANGLE:
				return convertViewRectangleToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramData createDiagramData() {
		DiagramDataImpl diagramData = new DiagramDataImpl();
		return diagramData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diagram createDiagram() {
		DiagramImpl diagram = new DiagramImpl();
		return diagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VisualInfo createVisualInfo() {
		VisualInfoImpl visualInfo = new VisualInfoImpl();
		return visualInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedLocation() {
		KeyedLocationImpl keyedLocation = new KeyedLocationImpl();
		return keyedLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedSize() {
		KeyedSizeImpl keyedSize = new KeyedSizeImpl();
		return keyedSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedConstraint() {
		KeyedConstraintImpl keyedConstraint = new KeyedConstraintImpl();
		return keyedConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedPoints() {
		KeyedPointsImpl keyedPoints = new KeyedPointsImpl();
		return keyedPoints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotationEMF createAnnotationEMF() {
		AnnotationEMFImpl annotationEMF = new AnnotationEMFImpl();
		return annotationEMF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotationGeneric createAnnotationGeneric() {
		AnnotationGenericImpl annotationGeneric = new AnnotationGenericImpl();
		return annotationGeneric;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramFigure createDiagramFigure() {
		DiagramFigureImpl diagramFigure = new DiagramFigureImpl();
		return diagramFigure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedGeneric() {
		KeyedGenericImpl keyedGeneric = new KeyedGenericImpl();
		return keyedGeneric;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedInteger() {
		KeyedIntegerImpl keyedInteger = new KeyedIntegerImpl();
		return keyedInteger;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedDynamic() {
		KeyedDynamicImpl keyedDynamic = new KeyedDynamicImpl();
		return keyedDynamic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createKeyedBoolean() {
		KeyedBooleanImpl keyedBoolean = new KeyedBooleanImpl();
		return keyedBoolean;
	}

	public Dimension createViewDimensionFromString(EDataType eDataType, String initialValue) {

		StringTokenizer t = new StringTokenizer(initialValue, ","); //$NON-NLS-1$
		int width, height;
		try {
			if (t.hasMoreTokens())
				width = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
			if (t.hasMoreTokens())
				height = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
		} catch (NumberFormatException e) {
			return null;	// Not "n,n" format.
	}

		return new Dimension(width, height);
	
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertViewDimensionToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public Point createViewPointFromString(EDataType eDataType, String initialValue) {

		StringTokenizer t = new StringTokenizer(initialValue, ","); //$NON-NLS-1$
		int x, y;
		try {
			if (t.hasMoreTokens())
				x = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
			if (t.hasMoreTokens())
				y = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
		} catch (NumberFormatException e) {
			return null;	// Not "n,n" format.
	}

		return new Point(x, y);
	
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertViewPointToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	public Rectangle createViewRectangleFromString(EDataType eDataType, String initialValue) {

		StringTokenizer t = new StringTokenizer(initialValue, ","); //$NON-NLS-1$
		int x, y, width, height;
		try {
			if (t.hasMoreTokens())
				x = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
			if (t.hasMoreTokens())
				y = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
			if (t.hasMoreTokens())
				width = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
			if (t.hasMoreTokens())
				height = Integer.parseInt(t.nextToken());
			else
				return null;	// Not enough tokens
		} catch (NumberFormatException e) {
			return null;	// Not "n,n" format.
	}

		return new Rectangle(x, y, width, height);
	
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertViewRectangleToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CDMPackage getCDMPackage() {
		return (CDMPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static CDMPackage getPackage() {
		return CDMPackage.eINSTANCE;
	}
} //CDMFactoryImpl
