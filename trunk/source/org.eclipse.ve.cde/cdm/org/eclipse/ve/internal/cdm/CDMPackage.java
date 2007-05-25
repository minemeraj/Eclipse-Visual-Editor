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
 *  $RCSfile: CDMPackage.java,v $
 *  $Revision: 1.9 $  $Date: 2007-05-25 04:09:36 $ 
 */

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.cdm.CDMFactory
 * @model kind="package"
 * @generated
 */
public interface CDMPackage extends EPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "";

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "cdm"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/ve/internal/cdm.ecore"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.ve.internal.cdm"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CDMPackage eINSTANCE = org.eclipse.ve.internal.cdm.impl.CDMPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.DiagramDataImpl <em>Diagram Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.DiagramDataImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getDiagramData()
	 * @generated
	 */
	int DIAGRAM_DATA = 0;

	/**
	 * The feature id for the '<em><b>Diagrams</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__DIAGRAMS = 0;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__ANNOTATIONS = 1;

	/**
	 * The number of structural features of the '<em>Diagram Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedValueHolderImpl <em>Keyed Value Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedValueHolderImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedValueHolder()
	 * @generated
	 */
	int KEYED_VALUE_HOLDER = 3;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_VALUE_HOLDER__KEYED_VALUES = 0;

	/**
	 * The number of structural features of the '<em>Keyed Value Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_VALUE_HOLDER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.DiagramImpl <em>Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.DiagramImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getDiagram()
	 * @generated
	 */
	int DIAGRAM = 1;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DIAGRAM__KEYED_VALUES = KEYED_VALUE_HOLDER__KEYED_VALUES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__NAME = KEYED_VALUE_HOLDER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__ID = KEYED_VALUE_HOLDER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Diagram Data</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__DIAGRAM_DATA = KEYED_VALUE_HOLDER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visual Infos</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__VISUAL_INFOS = KEYED_VALUE_HOLDER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Figures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__FIGURES = KEYED_VALUE_HOLDER_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FEATURE_COUNT = KEYED_VALUE_HOLDER_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.VisualInfoImpl <em>Visual Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.VisualInfoImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getVisualInfo()
	 * @generated
	 */
	int VISUAL_INFO = 2;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int VISUAL_INFO__KEYED_VALUES = KEYED_VALUE_HOLDER__KEYED_VALUES;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VISUAL_INFO__DIAGRAM = KEYED_VALUE_HOLDER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Visual Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VISUAL_INFO_FEATURE_COUNT = KEYED_VALUE_HOLDER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedLocationImpl <em>Keyed Location</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedLocationImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedLocation()
	 * @generated
	 */
	int KEYED_LOCATION = 4;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_LOCATION__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_LOCATION__KEY = 1;

	/**
	 * The number of structural features of the '<em>Keyed Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_LOCATION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedSizeImpl <em>Keyed Size</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedSizeImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedSize()
	 * @generated
	 */
	int KEYED_SIZE = 5;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_SIZE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_SIZE__KEY = 1;

	/**
	 * The number of structural features of the '<em>Keyed Size</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_SIZE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedConstraintImpl <em>Keyed Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedConstraintImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedConstraint()
	 * @generated
	 */
	int KEYED_CONSTRAINT = 6;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_CONSTRAINT__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_CONSTRAINT__KEY = 1;

	/**
	 * The number of structural features of the '<em>Keyed Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_CONSTRAINT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.AnnotationImpl <em>Annotation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.AnnotationImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getAnnotation()
	 * @generated
	 */
	int ANNOTATION = 7;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int ANNOTATION__KEYED_VALUES = KEYED_VALUE_HOLDER__KEYED_VALUES;

	/**
	 * The feature id for the '<em><b>Visual Infos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__VISUAL_INFOS = KEYED_VALUE_HOLDER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_FEATURE_COUNT = KEYED_VALUE_HOLDER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedPointsImpl <em>Keyed Points</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedPointsImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedPoints()
	 * @generated
	 */
	int KEYED_POINTS = 8;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_POINTS__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_POINTS__KEY = 1;

	/**
	 * The number of structural features of the '<em>Keyed Points</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_POINTS_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.AnnotationEMFImpl <em>Annotation EMF</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.AnnotationEMFImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getAnnotationEMF()
	 * @generated
	 */
	int ANNOTATION_EMF = 9;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int ANNOTATION_EMF__KEYED_VALUES = ANNOTATION__KEYED_VALUES;

	/**
	 * The feature id for the '<em><b>Visual Infos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_EMF__VISUAL_INFOS = ANNOTATION__VISUAL_INFOS;

	/**
	 * The feature id for the '<em><b>Annotates</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_EMF__ANNOTATES = ANNOTATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Annotation EMF</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_EMF_FEATURE_COUNT = ANNOTATION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.AnnotationGenericImpl <em>Annotation Generic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.AnnotationGenericImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getAnnotationGeneric()
	 * @generated
	 */
	int ANNOTATION_GENERIC = 10;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int ANNOTATION_GENERIC__KEYED_VALUES = ANNOTATION__KEYED_VALUES;

	/**
	 * The feature id for the '<em><b>Visual Infos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_GENERIC__VISUAL_INFOS = ANNOTATION__VISUAL_INFOS;

	/**
	 * The feature id for the '<em><b>Annotates ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_GENERIC__ANNOTATES_ID = ANNOTATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Annotation Generic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_GENERIC_FEATURE_COUNT = ANNOTATION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.DiagramFigureImpl <em>Diagram Figure</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.DiagramFigureImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getDiagramFigure()
	 * @generated
	 */
	int DIAGRAM_FIGURE = 11;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DIAGRAM_FIGURE__KEYED_VALUES = KEYED_VALUE_HOLDER__KEYED_VALUES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FIGURE__TYPE = KEYED_VALUE_HOLDER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Child Figures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FIGURE__CHILD_FIGURES = KEYED_VALUE_HOLDER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Figure</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FIGURE_FEATURE_COUNT = KEYED_VALUE_HOLDER_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedGenericImpl <em>Keyed Generic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedGenericImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedGeneric()
	 * @generated
	 */
	int KEYED_GENERIC = 12;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_GENERIC__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_GENERIC__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Keyed Generic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_GENERIC_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedIntegerImpl <em>Keyed Integer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedIntegerImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedInteger()
	 * @generated
	 */
	int KEYED_INTEGER = 13;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_INTEGER__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_INTEGER__KEY = 1;

	/**
	 * The number of structural features of the '<em>Keyed Integer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_INTEGER_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link java.util.Map.Entry <em>Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Map.Entry
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getMapEntry()
	 * @generated
	 */
	int MAP_ENTRY = 14;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedDynamicImpl <em>Keyed Dynamic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedDynamicImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedDynamic()
	 * @generated
	 */
	int KEYED_DYNAMIC = 15;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_DYNAMIC__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_DYNAMIC__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Keyed Dynamic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_DYNAMIC_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedBooleanImpl <em>Keyed Boolean</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.impl.KeyedBooleanImpl
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedBoolean()
	 * @generated
	 */
	int KEYED_BOOLEAN = 16;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_BOOLEAN__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_BOOLEAN__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Keyed Boolean</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_BOOLEAN_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '<em>View Dimension</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.model.Dimension
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getViewDimension()
	 * @generated
	 */
	int VIEW_DIMENSION = 17;

	/**
	 * The meta object id for the '<em>View Point</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.model.Point
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getViewPoint()
	 * @generated
	 */
	int VIEW_POINT = 18;

	/**
	 * The meta object id for the '<em>View Rectangle</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cdm.model.Rectangle
	 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getViewRectangle()
	 * @generated
	 */
	int VIEW_RECTANGLE = 19;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.DiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Data</em>'.
	 * @see org.eclipse.ve.internal.cdm.DiagramData
	 * @generated
	 */
	EClass getDiagramData();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cdm.DiagramData#getDiagrams <em>Diagrams</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Diagrams</em>'.
	 * @see org.eclipse.ve.internal.cdm.DiagramData#getDiagrams()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_Diagrams();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cdm.DiagramData#getAnnotations <em>Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Annotations</em>'.
	 * @see org.eclipse.ve.internal.cdm.DiagramData#getAnnotations()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_Annotations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.Diagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram</em>'.
	 * @see org.eclipse.ve.internal.cdm.Diagram
	 * @generated
	 */
	EClass getDiagram();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cdm.Diagram#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ve.internal.cdm.Diagram#getName()
	 * @see #getDiagram()
	 * @generated
	 */
	EAttribute getDiagram_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cdm.Diagram#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ve.internal.cdm.Diagram#getId()
	 * @see #getDiagram()
	 * @generated
	 */
	EAttribute getDiagram_Id();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.ve.internal.cdm.Diagram#getDiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Diagram Data</em>'.
	 * @see org.eclipse.ve.internal.cdm.Diagram#getDiagramData()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_DiagramData();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.cdm.Diagram#getVisualInfos <em>Visual Infos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Visual Infos</em>'.
	 * @see org.eclipse.ve.internal.cdm.Diagram#getVisualInfos()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_VisualInfos();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cdm.Diagram#getFigures <em>Figures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Figures</em>'.
	 * @see org.eclipse.ve.internal.cdm.Diagram#getFigures()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_Figures();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.VisualInfo <em>Visual Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Visual Info</em>'.
	 * @see org.eclipse.ve.internal.cdm.VisualInfo
	 * @generated
	 */
	EClass getVisualInfo();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.cdm.VisualInfo#getDiagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Diagram</em>'.
	 * @see org.eclipse.ve.internal.cdm.VisualInfo#getDiagram()
	 * @see #getVisualInfo()
	 * @generated
	 */
	EReference getVisualInfo_Diagram();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.KeyedValueHolder <em>Keyed Value Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Value Holder</em>'.
	 * @see org.eclipse.ve.internal.cdm.KeyedValueHolder
	 * @generated
	 */
	EClass getKeyedValueHolder();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.ve.internal.cdm.KeyedValueHolder#getKeyedValues <em>Keyed Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Keyed Values</em>'.
	 * @see org.eclipse.ve.internal.cdm.KeyedValueHolder#getKeyedValues()
	 * @see #getKeyedValueHolder()
	 * @generated
	 */
	EReference getKeyedValueHolder_KeyedValues();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Location</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="org.eclipse.ve.internal.cdm.model.Point" valueDataType="org.eclipse.ve.internal.cdm.ViewPoint"
	 *        keyType="java.lang.String"
	 * @generated
	 */
	EClass getKeyedLocation();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedLocation()
	 * @generated
	 */
	EAttribute getKeyedLocation_Value();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedLocation()
	 * @generated
	 */
	EAttribute getKeyedLocation_Key();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Size</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="org.eclipse.ve.internal.cdm.model.Dimension" valueDataType="org.eclipse.ve.internal.cdm.ViewDimension"
	 *        keyType="java.lang.String"
	 * @generated
	 */
	EClass getKeyedSize();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedSize()
	 * @generated
	 */
	EAttribute getKeyedSize_Value();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedSize()
	 * @generated
	 */
	EAttribute getKeyedSize_Key();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Constraint</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="org.eclipse.ve.internal.cdm.model.Rectangle" valueDataType="org.eclipse.ve.internal.cdm.ViewRectangle"
	 *        keyType="java.lang.String"
	 * @generated
	 */
	EClass getKeyedConstraint();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedConstraint()
	 * @generated
	 */
	EAttribute getKeyedConstraint_Value();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedConstraint()
	 * @generated
	 */
	EAttribute getKeyedConstraint_Key();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.Annotation <em>Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation</em>'.
	 * @see org.eclipse.ve.internal.cdm.Annotation
	 * @generated
	 */
	EClass getAnnotation();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cdm.Annotation#getVisualInfos <em>Visual Infos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Visual Infos</em>'.
	 * @see org.eclipse.ve.internal.cdm.Annotation#getVisualInfos()
	 * @see #getAnnotation()
	 * @generated
	 */
	EReference getAnnotation_VisualInfos();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Points</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Points</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="org.eclipse.ve.internal.cdm.model.Point" valueDataType="org.eclipse.ve.internal.cdm.ViewPoint" valueMany="true"
	 *        keyType="java.lang.String"
	 * @generated
	 */
	EClass getKeyedPoints();

	/**
	 * Returns the meta object for the attribute list '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedPoints()
	 * @generated
	 */
	EAttribute getKeyedPoints_Value();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedPoints()
	 * @generated
	 */
	EAttribute getKeyedPoints_Key();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.AnnotationEMF <em>Annotation EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation EMF</em>'.
	 * @see org.eclipse.ve.internal.cdm.AnnotationEMF
	 * @generated
	 */
	EClass getAnnotationEMF();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.cdm.AnnotationEMF#getAnnotates <em>Annotates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Annotates</em>'.
	 * @see org.eclipse.ve.internal.cdm.AnnotationEMF#getAnnotates()
	 * @see #getAnnotationEMF()
	 * @generated
	 */
	EReference getAnnotationEMF_Annotates();

  /**
   * Returns the meta object for the reference ParentAnnotation.
   * <!-- begin-user-doc -->
   * This is a special reference. It is not code-generated. Nor can it be asked of getEAllStructuralFeature(). This is stored in
   * an adapter on an EObject. It allows listening for notifications of changes, but is not actually stored on the EObject. It is
   * for pointing back to the Annotation for this EObject.
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>ParentAnnotation</em>'.
   * @see org.eclipse.ve.internal.cdm.AnnotationEMF
   * @see #getAnnotationEMF()
   */
    EReference getEObject_ParentAnnotation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.AnnotationGeneric <em>Annotation Generic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation Generic</em>'.
	 * @see org.eclipse.ve.internal.cdm.AnnotationGeneric
	 * @generated
	 */
	EClass getAnnotationGeneric();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cdm.AnnotationGeneric#getAnnotatesID <em>Annotates ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Annotates ID</em>'.
	 * @see org.eclipse.ve.internal.cdm.AnnotationGeneric#getAnnotatesID()
	 * @see #getAnnotationGeneric()
	 * @generated
	 */
	EAttribute getAnnotationGeneric_AnnotatesID();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cdm.DiagramFigure <em>Diagram Figure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Figure</em>'.
	 * @see org.eclipse.ve.internal.cdm.DiagramFigure
	 * @generated
	 */
	EClass getDiagramFigure();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cdm.DiagramFigure#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.ve.internal.cdm.DiagramFigure#getType()
	 * @see #getDiagramFigure()
	 * @generated
	 */
	EAttribute getDiagramFigure_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cdm.DiagramFigure#getChildFigures <em>Child Figures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Child Figures</em>'.
	 * @see org.eclipse.ve.internal.cdm.DiagramFigure#getChildFigures()
	 * @see #getDiagramFigure()
	 * @generated
	 */
	EReference getDiagramFigure_ChildFigures();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Generic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Generic</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="java.lang.String"
	 *        valueType="org.eclipse.emf.ecore.EObject" valueContainment="true"
	 * @generated
	 */
	EClass getKeyedGeneric();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedGeneric()
	 * @generated
	 */
	EAttribute getKeyedGeneric_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedGeneric()
	 * @generated
	 */
	EReference getKeyedGeneric_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Integer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Integer</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="int"
	 *        keyType="java.lang.String"
	 * @generated
	 */
	EClass getKeyedInteger();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedInteger()
	 * @generated
	 */
	EAttribute getKeyedInteger_Value();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedInteger()
	 * @generated
	 */
	EAttribute getKeyedInteger_Key();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="java.lang.String"
	 *        valueType="java.lang.String"
	 * @generated
	 */
	EClass getMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getMapEntry()
	 * @generated
	 */
	EAttribute getMapEntry_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getMapEntry()
	 * @generated
	 */
	EAttribute getMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Dynamic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Dynamic</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="java.lang.String" keyTransient="true" keyVolatile="true"
	 *        valueType="java.lang.Object" valueUnsettable="true" valueTransient="true" valueVolatile="true"
	 * @generated
	 */
	EClass getKeyedDynamic();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedDynamic()
	 * @generated
	 */
	EAttribute getKeyedDynamic_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedDynamic()
	 * @generated
	 */
	EAttribute getKeyedDynamic_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Boolean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Boolean</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="java.lang.String"
	 *        valueType="boolean"
	 * @generated
	 */
	EClass getKeyedBoolean();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedBoolean()
	 * @generated
	 */
	EAttribute getKeyedBoolean_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedBoolean()
	 * @generated
	 */
	EAttribute getKeyedBoolean_Value();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.ve.internal.cdm.model.Dimension <em>View Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>View Dimension</em>'.
	 * @see org.eclipse.ve.internal.cdm.model.Dimension
	 * @model instanceClass="org.eclipse.ve.internal.cdm.model.Dimension"
	 * @generated
	 */
	EDataType getViewDimension();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.ve.internal.cdm.model.Point <em>View Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>View Point</em>'.
	 * @see org.eclipse.ve.internal.cdm.model.Point
	 * @model instanceClass="org.eclipse.ve.internal.cdm.model.Point"
	 * @generated
	 */
	EDataType getViewPoint();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.ve.internal.cdm.model.Rectangle <em>View Rectangle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>View Rectangle</em>'.
	 * @see org.eclipse.ve.internal.cdm.model.Rectangle
	 * @model instanceClass="org.eclipse.ve.internal.cdm.model.Rectangle"
	 * @generated
	 */
	EDataType getViewRectangle();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CDMFactory getCDMFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals  {
		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.DiagramDataImpl <em>Diagram Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.DiagramDataImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getDiagramData()
		 * @generated
		 */
		EClass DIAGRAM_DATA = eINSTANCE.getDiagramData();

		/**
		 * The meta object literal for the '<em><b>Diagrams</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__DIAGRAMS = eINSTANCE.getDiagramData_Diagrams();

		/**
		 * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__ANNOTATIONS = eINSTANCE.getDiagramData_Annotations();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.DiagramImpl <em>Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.DiagramImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getDiagram()
		 * @generated
		 */
		EClass DIAGRAM = eINSTANCE.getDiagram();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM__NAME = eINSTANCE.getDiagram_Name();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM__ID = eINSTANCE.getDiagram_Id();

		/**
		 * The meta object literal for the '<em><b>Diagram Data</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__DIAGRAM_DATA = eINSTANCE.getDiagram_DiagramData();

		/**
		 * The meta object literal for the '<em><b>Visual Infos</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__VISUAL_INFOS = eINSTANCE.getDiagram_VisualInfos();

		/**
		 * The meta object literal for the '<em><b>Figures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__FIGURES = eINSTANCE.getDiagram_Figures();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.VisualInfoImpl <em>Visual Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.VisualInfoImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getVisualInfo()
		 * @generated
		 */
		EClass VISUAL_INFO = eINSTANCE.getVisualInfo();

		/**
		 * The meta object literal for the '<em><b>Diagram</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VISUAL_INFO__DIAGRAM = eINSTANCE.getVisualInfo_Diagram();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedValueHolderImpl <em>Keyed Value Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedValueHolderImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedValueHolder()
		 * @generated
		 */
		EClass KEYED_VALUE_HOLDER = eINSTANCE.getKeyedValueHolder();

		/**
		 * The meta object literal for the '<em><b>Keyed Values</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEYED_VALUE_HOLDER__KEYED_VALUES = eINSTANCE.getKeyedValueHolder_KeyedValues();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedLocationImpl <em>Keyed Location</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedLocationImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedLocation()
		 * @generated
		 */
		EClass KEYED_LOCATION = eINSTANCE.getKeyedLocation();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_LOCATION__VALUE = eINSTANCE.getKeyedLocation_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_LOCATION__KEY = eINSTANCE.getKeyedLocation_Key();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedSizeImpl <em>Keyed Size</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedSizeImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedSize()
		 * @generated
		 */
		EClass KEYED_SIZE = eINSTANCE.getKeyedSize();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_SIZE__VALUE = eINSTANCE.getKeyedSize_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_SIZE__KEY = eINSTANCE.getKeyedSize_Key();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedConstraintImpl <em>Keyed Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedConstraintImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedConstraint()
		 * @generated
		 */
		EClass KEYED_CONSTRAINT = eINSTANCE.getKeyedConstraint();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_CONSTRAINT__VALUE = eINSTANCE.getKeyedConstraint_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_CONSTRAINT__KEY = eINSTANCE.getKeyedConstraint_Key();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.AnnotationImpl <em>Annotation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.AnnotationImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getAnnotation()
		 * @generated
		 */
		EClass ANNOTATION = eINSTANCE.getAnnotation();

		/**
		 * The meta object literal for the '<em><b>Visual Infos</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATION__VISUAL_INFOS = eINSTANCE.getAnnotation_VisualInfos();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedPointsImpl <em>Keyed Points</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedPointsImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedPoints()
		 * @generated
		 */
		EClass KEYED_POINTS = eINSTANCE.getKeyedPoints();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_POINTS__VALUE = eINSTANCE.getKeyedPoints_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_POINTS__KEY = eINSTANCE.getKeyedPoints_Key();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.AnnotationEMFImpl <em>Annotation EMF</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.AnnotationEMFImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getAnnotationEMF()
		 * @generated
		 */
		EClass ANNOTATION_EMF = eINSTANCE.getAnnotationEMF();

		/**
		 * The meta object literal for the '<em><b>Annotates</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATION_EMF__ANNOTATES = eINSTANCE.getAnnotationEMF_Annotates();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.AnnotationGenericImpl <em>Annotation Generic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.AnnotationGenericImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getAnnotationGeneric()
		 * @generated
		 */
		EClass ANNOTATION_GENERIC = eINSTANCE.getAnnotationGeneric();

		/**
		 * The meta object literal for the '<em><b>Annotates ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION_GENERIC__ANNOTATES_ID = eINSTANCE.getAnnotationGeneric_AnnotatesID();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.DiagramFigureImpl <em>Diagram Figure</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.DiagramFigureImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getDiagramFigure()
		 * @generated
		 */
		EClass DIAGRAM_FIGURE = eINSTANCE.getDiagramFigure();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_FIGURE__TYPE = eINSTANCE.getDiagramFigure_Type();

		/**
		 * The meta object literal for the '<em><b>Child Figures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_FIGURE__CHILD_FIGURES = eINSTANCE.getDiagramFigure_ChildFigures();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedGenericImpl <em>Keyed Generic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedGenericImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedGeneric()
		 * @generated
		 */
		EClass KEYED_GENERIC = eINSTANCE.getKeyedGeneric();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_GENERIC__KEY = eINSTANCE.getKeyedGeneric_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEYED_GENERIC__VALUE = eINSTANCE.getKeyedGeneric_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedIntegerImpl <em>Keyed Integer</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedIntegerImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedInteger()
		 * @generated
		 */
		EClass KEYED_INTEGER = eINSTANCE.getKeyedInteger();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_INTEGER__VALUE = eINSTANCE.getKeyedInteger_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_INTEGER__KEY = eINSTANCE.getKeyedInteger_Key();

		/**
		 * The meta object literal for the '{@link java.util.Map.Entry <em>Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.Map.Entry
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getMapEntry()
		 * @generated
		 */
		EClass MAP_ENTRY = eINSTANCE.getMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAP_ENTRY__KEY = eINSTANCE.getMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAP_ENTRY__VALUE = eINSTANCE.getMapEntry_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedDynamicImpl <em>Keyed Dynamic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedDynamicImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedDynamic()
		 * @generated
		 */
		EClass KEYED_DYNAMIC = eINSTANCE.getKeyedDynamic();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_DYNAMIC__KEY = eINSTANCE.getKeyedDynamic_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_DYNAMIC__VALUE = eINSTANCE.getKeyedDynamic_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.ve.internal.cdm.impl.KeyedBooleanImpl <em>Keyed Boolean</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.impl.KeyedBooleanImpl
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getKeyedBoolean()
		 * @generated
		 */
		EClass KEYED_BOOLEAN = eINSTANCE.getKeyedBoolean();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_BOOLEAN__KEY = eINSTANCE.getKeyedBoolean_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEYED_BOOLEAN__VALUE = eINSTANCE.getKeyedBoolean_Value();

		/**
		 * The meta object literal for the '<em>View Dimension</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.model.Dimension
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getViewDimension()
		 * @generated
		 */
		EDataType VIEW_DIMENSION = eINSTANCE.getViewDimension();

		/**
		 * The meta object literal for the '<em>View Point</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.model.Point
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getViewPoint()
		 * @generated
		 */
		EDataType VIEW_POINT = eINSTANCE.getViewPoint();

		/**
		 * The meta object literal for the '<em>View Rectangle</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.ve.internal.cdm.model.Rectangle
		 * @see org.eclipse.ve.internal.cdm.impl.CDMPackageImpl#getViewRectangle()
		 * @generated
		 */
		EDataType VIEW_RECTANGLE = eINSTANCE.getViewRectangle();

	}

} //CDMPackage
