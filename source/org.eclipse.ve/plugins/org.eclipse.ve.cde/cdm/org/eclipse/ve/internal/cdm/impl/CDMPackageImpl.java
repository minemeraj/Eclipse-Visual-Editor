package org.eclipse.ve.internal.cdm.impl;
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
 *  $RCSfile: CDMPackageImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.AnnotationEMF;
import org.eclipse.ve.internal.cdm.AnnotationGeneric;
import org.eclipse.ve.internal.cdm.CDMFactory;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;
import org.eclipse.ve.internal.cdm.DiagramFigure;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.cdm.VisualInfo;
import org.eclipse.ve.internal.cdm.model.Dimension;
import org.eclipse.ve.internal.cdm.model.Point;
import org.eclipse.ve.internal.cdm.model.Rectangle;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CDMPackageImpl extends EPackageImpl implements CDMPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass visualInfoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedValueHolderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedLocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedSizeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedPointsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotationEMFEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotationGenericEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramFigureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedGenericEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedIntegerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedDynamicEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType viewDimensionEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType viewPointEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType viewRectangleEDataType = null;
	
	private EReference eObject_ParentAnnotation = null;	// Structural Feature for parent annotation.


	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CDMPackageImpl() {
		super(eNS_URI, CDMFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CDMPackage init() {
		if (isInited) return (CDMPackage)EPackage.Registry.INSTANCE.get(CDMPackage.eNS_URI);

		// Obtain or create and register package.
		CDMPackageImpl theCDMPackage = (CDMPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EPackage ? EPackage.Registry.INSTANCE.get(eNS_URI) : new CDMPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackageImpl.init();

		// Obtain or create and register interdependencies

		// Step 1: create meta-model objects
		theCDMPackage.createPackageContents();

		// Step 2: complete initialization
		theCDMPackage.initializePackageContents();

		return theCDMPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramData() {
		return diagramDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_Diagrams() {
		return (EReference)diagramDataEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_Annotations() {
		return (EReference)diagramDataEClass.getEReferences().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagram() {
		return diagramEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagram_Name() {
		return (EAttribute)diagramEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagram_Id() {
		return (EAttribute)diagramEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagram_DiagramData() {
		return (EReference)diagramEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagram_VisualInfos() {
		return (EReference)diagramEClass.getEReferences().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagram_Figures() {
		return (EReference)diagramEClass.getEReferences().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVisualInfo() {
		return visualInfoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVisualInfo_Diagram() {
		return (EReference)visualInfoEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedValueHolder() {
		return keyedValueHolderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKeyedValueHolder_KeyedValues() {
		return (EReference)keyedValueHolderEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedLocation() {
		return keyedLocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedLocation_Value() {
		return (EAttribute)keyedLocationEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedLocation_Key() {
		return (EAttribute)keyedLocationEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedSize() {
		return keyedSizeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedSize_Value() {
		return (EAttribute)keyedSizeEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedSize_Key() {
		return (EAttribute)keyedSizeEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedConstraint() {
		return keyedConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedConstraint_Value() {
		return (EAttribute)keyedConstraintEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedConstraint_Key() {
		return (EAttribute)keyedConstraintEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotation() {
		return annotationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotation_VisualInfos() {
		return (EReference)annotationEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedPoints() {
		return keyedPointsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedPoints_Value() {
		return (EAttribute)keyedPointsEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedPoints_Key() {
		return (EAttribute)keyedPointsEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotationEMF() {
		return annotationEMFEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotationEMF_Annotates() {
		return (EReference)annotationEMFEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotationGeneric() {
		return annotationGenericEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotationGeneric_AnnotatesID() {
		return (EAttribute)annotationGenericEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramFigure() {
		return diagramFigureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramFigure_Type() {
		return (EAttribute)diagramFigureEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramFigure_ChildFigures() {
		return (EReference)diagramFigureEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedGeneric() {
		return keyedGenericEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedGeneric_Key() {
		return (EAttribute)keyedGenericEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKeyedGeneric_Value() {
		return (EReference)keyedGenericEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedInteger() {
		return keyedIntegerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedInteger_Value() {
		return (EAttribute)keyedIntegerEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedInteger_Key() {
		return (EAttribute)keyedIntegerEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMapEntry() {
		return mapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMapEntry_Key() {
		return (EAttribute)mapEntryEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMapEntry_Value() {
		return (EAttribute)mapEntryEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedDynamic() {
		return keyedDynamicEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedDynamic_Key() {
		return (EAttribute)keyedDynamicEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedDynamic_Value() {
		return (EAttribute)keyedDynamicEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getViewDimension() {
		return viewDimensionEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getViewPoint() {
		return viewPointEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getViewRectangle() {
		return viewRectangleEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CDMFactory getCDMFactory() {
		return (CDMFactory)getEFactoryInstance();
	}

	public EReference getEObject_ParentAnnotation() {
		return eObject_ParentAnnotation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;
	
	public void createPackageContents() {
		if (isCreated) return;
		createPackageContentsGen();
		
		// Create the special parent annotation. It doesn't reside in any EClass.
		eObject_ParentAnnotation = EcoreFactory.eINSTANCE.createEReference();	
	}

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContentsGen() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		diagramDataEClass = createEClass(DIAGRAM_DATA);
		createEReference(diagramDataEClass, DIAGRAM_DATA__DIAGRAMS);
		createEReference(diagramDataEClass, DIAGRAM_DATA__ANNOTATIONS);

		diagramEClass = createEClass(DIAGRAM);
		createEAttribute(diagramEClass, DIAGRAM__NAME);
		createEAttribute(diagramEClass, DIAGRAM__ID);
		createEReference(diagramEClass, DIAGRAM__DIAGRAM_DATA);
		createEReference(diagramEClass, DIAGRAM__VISUAL_INFOS);
		createEReference(diagramEClass, DIAGRAM__FIGURES);

		visualInfoEClass = createEClass(VISUAL_INFO);
		createEReference(visualInfoEClass, VISUAL_INFO__DIAGRAM);

		keyedValueHolderEClass = createEClass(KEYED_VALUE_HOLDER);
		createEReference(keyedValueHolderEClass, KEYED_VALUE_HOLDER__KEYED_VALUES);

		keyedLocationEClass = createEClass(KEYED_LOCATION);
		createEAttribute(keyedLocationEClass, KEYED_LOCATION__VALUE);
		createEAttribute(keyedLocationEClass, KEYED_LOCATION__KEY);

		keyedSizeEClass = createEClass(KEYED_SIZE);
		createEAttribute(keyedSizeEClass, KEYED_SIZE__VALUE);
		createEAttribute(keyedSizeEClass, KEYED_SIZE__KEY);

		keyedConstraintEClass = createEClass(KEYED_CONSTRAINT);
		createEAttribute(keyedConstraintEClass, KEYED_CONSTRAINT__VALUE);
		createEAttribute(keyedConstraintEClass, KEYED_CONSTRAINT__KEY);

		annotationEClass = createEClass(ANNOTATION);
		createEReference(annotationEClass, ANNOTATION__VISUAL_INFOS);

		keyedPointsEClass = createEClass(KEYED_POINTS);
		createEAttribute(keyedPointsEClass, KEYED_POINTS__VALUE);
		createEAttribute(keyedPointsEClass, KEYED_POINTS__KEY);

		annotationEMFEClass = createEClass(ANNOTATION_EMF);
		createEReference(annotationEMFEClass, ANNOTATION_EMF__ANNOTATES);

		annotationGenericEClass = createEClass(ANNOTATION_GENERIC);
		createEAttribute(annotationGenericEClass, ANNOTATION_GENERIC__ANNOTATES_ID);

		diagramFigureEClass = createEClass(DIAGRAM_FIGURE);
		createEAttribute(diagramFigureEClass, DIAGRAM_FIGURE__TYPE);
		createEReference(diagramFigureEClass, DIAGRAM_FIGURE__CHILD_FIGURES);

		keyedGenericEClass = createEClass(KEYED_GENERIC);
		createEAttribute(keyedGenericEClass, KEYED_GENERIC__KEY);
		createEReference(keyedGenericEClass, KEYED_GENERIC__VALUE);

		keyedIntegerEClass = createEClass(KEYED_INTEGER);
		createEAttribute(keyedIntegerEClass, KEYED_INTEGER__VALUE);
		createEAttribute(keyedIntegerEClass, KEYED_INTEGER__KEY);

		mapEntryEClass = createEClass(MAP_ENTRY);
		createEAttribute(mapEntryEClass, MAP_ENTRY__KEY);
		createEAttribute(mapEntryEClass, MAP_ENTRY__VALUE);

		keyedDynamicEClass = createEClass(KEYED_DYNAMIC);
		createEAttribute(keyedDynamicEClass, KEYED_DYNAMIC__KEY);
		createEAttribute(keyedDynamicEClass, KEYED_DYNAMIC__VALUE);

		// Create data types
		viewDimensionEDataType = createEDataType(VIEW_DIMENSION);
		viewPointEDataType = createEDataType(VIEW_POINT);
		viewRectangleEDataType = createEDataType(VIEW_RECTANGLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;
	
	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */	
	public void initializePackageContents() {
	    if (isInitialized) return;
	    initializePackageContentsGen();
	    
	    // Now initialize the special feature parent annotation.
		eObject_ParentAnnotation.setName("parentAnnotation"); //$NON-NLS-1$
		((EStructuralFeatureImpl) eObject_ParentAnnotation).setContainerClass(EcorePackage.eINSTANCE.getEObject().getInstanceClass());
		eObject_ParentAnnotation.setTransient(true);
		eObject_ParentAnnotation.setVolatile(true);
		eObject_ParentAnnotation.setChangeable(true);
		eObject_ParentAnnotation.setLowerBound(0);
		eObject_ParentAnnotation.setUpperBound(1);
		eObject_ParentAnnotation.setEType(getAnnotation());
		eObject_ParentAnnotation.setContainment(false);
		eObject_ParentAnnotation.setResolveProxies(true);	    
	}

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContentsGen() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackageImpl theEcorePackage = (EcorePackageImpl)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Add supertypes to classes
		diagramEClass.getESuperTypes().add(this.getKeyedValueHolder());
		visualInfoEClass.getESuperTypes().add(this.getKeyedValueHolder());
		annotationEClass.getESuperTypes().add(this.getKeyedValueHolder());
		annotationEMFEClass.getESuperTypes().add(this.getAnnotation());
		annotationGenericEClass.getESuperTypes().add(this.getAnnotation());
		diagramFigureEClass.getESuperTypes().add(this.getKeyedValueHolder());

		// Initialize classes and features; add operations and parameters
		initEClass(diagramDataEClass, DiagramData.class, "DiagramData", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getDiagramData_Diagrams(), this.getDiagram(), this.getDiagram_DiagramData(), "diagrams", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);
		initEReference(getDiagramData_Annotations(), this.getAnnotation(), null, "annotations", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(diagramEClass, Diagram.class, "Diagram", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getDiagram_Name(), ecorePackage.getEString(), "name", " ", 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getDiagram_Id(), ecorePackage.getEString(), "id", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEReference(getDiagram_DiagramData(), this.getDiagramData(), this.getDiagramData_Diagrams(), "diagramData", null, 0, 1, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);
		initEReference(getDiagram_VisualInfos(), this.getVisualInfo(), this.getVisualInfo_Diagram(), "visualInfos", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);
		initEReference(getDiagram_Figures(), this.getDiagramFigure(), null, "figures", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(visualInfoEClass, VisualInfo.class, "VisualInfo", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getVisualInfo_Diagram(), this.getDiagram(), this.getDiagram_VisualInfos(), "diagram", null, 1, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(keyedValueHolderEClass, KeyedValueHolder.class, "KeyedValueHolder", IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getKeyedValueHolder_KeyedValues(), this.getMapEntry(), null, "keyedValues", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(keyedLocationEClass, Map.Entry.class, "KeyedLocation", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getKeyedLocation_Value(), this.getViewPoint(), "value", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getKeyedLocation_Key(), ecorePackage.getEString(), "key", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(keyedSizeEClass, Map.Entry.class, "KeyedSize", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getKeyedSize_Value(), this.getViewDimension(), "value", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getKeyedSize_Key(), ecorePackage.getEString(), "key", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(keyedConstraintEClass, Map.Entry.class, "KeyedConstraint", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getKeyedConstraint_Value(), this.getViewRectangle(), "value", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getKeyedConstraint_Key(), ecorePackage.getEString(), "key", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(annotationEClass, Annotation.class, "Annotation", IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getAnnotation_VisualInfos(), this.getVisualInfo(), null, "visualInfos", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		EOperation op = addEOperation(annotationEClass, this.getVisualInfo(), "getVisualInfo");
		addEParameter(op, this.getDiagram(), "aDiagram");

		initEClass(keyedPointsEClass, Map.Entry.class, "KeyedPoints", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getKeyedPoints_Value(), this.getViewPoint(), "value", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getKeyedPoints_Key(), ecorePackage.getEString(), "key", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(annotationEMFEClass, AnnotationEMF.class, "AnnotationEMF", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getAnnotationEMF_Annotates(), theEcorePackage.getEObject(), null, "annotates", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(annotationGenericEClass, AnnotationGeneric.class, "AnnotationGeneric", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getAnnotationGeneric_AnnotatesID(), ecorePackage.getEString(), "annotatesID", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(diagramFigureEClass, DiagramFigure.class, "DiagramFigure", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getDiagramFigure_Type(), ecorePackage.getEString(), "type", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEReference(getDiagramFigure_ChildFigures(), this.getDiagramFigure(), null, "childFigures", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(keyedGenericEClass, Map.Entry.class, "KeyedGeneric", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getKeyedGeneric_Key(), ecorePackage.getEString(), "key", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEReference(getKeyedGeneric_Value(), theEcorePackage.getEObject(), null, "value", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(keyedIntegerEClass, Map.Entry.class, "KeyedInteger", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getKeyedInteger_Value(), ecorePackage.getEIntegerObject(), "value", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getKeyedInteger_Key(), ecorePackage.getEString(), "key", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(mapEntryEClass, Map.Entry.class, "MapEntry", IS_ABSTRACT, IS_INTERFACE);
		initEAttribute(getMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getMapEntry_Value(), ecorePackage.getEString(), "value", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(keyedDynamicEClass, Map.Entry.class, "KeyedDynamic", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getKeyedDynamic_Key(), ecorePackage.getEString(), "key", null, 0, 1, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getKeyedDynamic_Value(), theEcorePackage.getEJavaObject(), "value", null, 0, 1, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		// Initialize data types
		initEDataType(viewDimensionEDataType, Dimension.class, "ViewDimension", IS_SERIALIZABLE);
		initEDataType(viewPointEDataType, Point.class, "ViewPoint", IS_SERIALIZABLE);
		initEDataType(viewRectangleEDataType, Rectangle.class, "ViewRectangle", IS_SERIALIZABLE);

		// Create resource
		createResource(eNS_URI);
	}
} //CDMPackageImpl
