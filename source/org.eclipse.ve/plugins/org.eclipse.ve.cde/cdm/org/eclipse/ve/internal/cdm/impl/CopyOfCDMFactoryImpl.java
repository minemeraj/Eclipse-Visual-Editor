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
package org.eclipse.ve.internal.cdm.impl;
/*
 *  $RCSfile: CopyOfCDMFactoryImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2007-09-17 14:17:13 $ 
 */

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.model.Dimension;
import org.eclipse.ve.internal.cdm.model.Point;
import org.eclipse.ve.internal.cdm.model.Rectangle;

import com.ibm.icu.util.StringTokenizer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CopyOfCDMFactoryImpl extends EFactoryImpl implements CDMFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "";

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CDMFactory init() {
		try {
			CDMFactory theCDMFactory = (CDMFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/ve/internal/cdm.ecore"); 
			if (theCDMFactory != null) {
				return theCDMFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CopyOfCDMFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CopyOfCDMFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
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
	public Map.Entry<String, Point> createKeyedLocation() {
		KeyedLocationImpl keyedLocation = new KeyedLocationImpl();
		return keyedLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Dimension> createKeyedSize() {
		KeyedSizeImpl keyedSize = new KeyedSizeImpl();
		return keyedSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Rectangle> createKeyedConstraint() {
		KeyedConstraintImpl keyedConstraint = new KeyedConstraintImpl();
		return keyedConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, EList<Point>> createKeyedPoints() {
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
	public Map.Entry<String, EObject> createKeyedGeneric() {
		KeyedGenericImpl keyedGeneric = new KeyedGenericImpl();
		return keyedGeneric;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Integer> createKeyedInteger() {
		KeyedIntegerImpl keyedInteger = new KeyedIntegerImpl();
		return keyedInteger;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Object> createKeyedDynamic() {
		KeyedDynamicImpl keyedDynamic = new KeyedDynamicImpl();
		return keyedDynamic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Boolean> createKeyedBoolean() {
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
		//TODO: 33 port. contributed by Erik Hecht. REVIEWME Nulls should not happen
		return instanceValue != null ? instanceValue.toString() : null;
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
		//TODO: 33 port. contributed by Erik Hecht. REVIEWME Nulls should not happen
	    return instanceValue != null ? instanceValue.toString() : null;
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
		return instanceValue != null ? instanceValue.toString() : null;
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
	@Deprecated
	public static CDMPackage getPackage() {
		return CDMPackage.eINSTANCE;
	}

} //CDMFactoryImpl
