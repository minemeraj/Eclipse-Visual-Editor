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
package org.eclipse.ve.internal.cde.decorators;
/*
 *  $RCSfile: DecoratorsPackage.java,v $
 *  $Revision: 1.4 $  $Date: 2005-09-15 21:27:15 $ 
 */


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

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
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsFactory
 * @model kind="package"
 * @generated
 */
public interface DecoratorsPackage extends EPackage{


	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "decorators"; //$NON-NLS-1$

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.decorators.impl.BasePropertyDecoratorImpl <em>Base Property Decorator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.decorators.impl.BasePropertyDecoratorImpl
	 * @see org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl#getBasePropertyDecorator()
	 * @generated
	 */
	int BASE_PROPERTY_DECORATOR = 0;
	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__EANNOTATIONS = EcorePackage.EANNOTATION__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__SOURCE = EcorePackage.EANNOTATION__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__DETAILS = EcorePackage.EANNOTATION__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__EMODEL_ELEMENT = EcorePackage.EANNOTATION__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__CONTENTS = EcorePackage.EANNOTATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__REFERENCES = EcorePackage.EANNOTATION__REFERENCES;

	/**
	 * The feature id for the '<em><b>Cell Editor Validator Classnames</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES = EcorePackage.EANNOTATION_FEATURE_COUNT + 0;
	/**
	 * The feature id for the '<em><b>Label Provider Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME = EcorePackage.EANNOTATION_FEATURE_COUNT + 1;
	/**
	 * The feature id for the '<em><b>Cell Editor Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME = EcorePackage.EANNOTATION_FEATURE_COUNT + 2;
	/**
	 * The feature id for the '<em><b>Null Invalid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__NULL_INVALID = EcorePackage.EANNOTATION_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Entry Expandable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE = EcorePackage.EANNOTATION_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the the '<em>Base Property Decorator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_PROPERTY_DECORATOR_FEATURE_COUNT = EcorePackage.EANNOTATION_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl <em>Class Descriptor Decorator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl
	 * @see org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl#getClassDescriptorDecorator()
	 * @generated
	 */
	int CLASS_DESCRIPTOR_DECORATOR = 5;
	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl <em>Feature Descriptor Decorator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.decorators.impl.FeatureDescriptorDecoratorImpl
	 * @see org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl#getFeatureDescriptorDecorator()
	 * @generated
	 */
	int FEATURE_DESCRIPTOR_DECORATOR = 4;
	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl <em>Property Descriptor Decorator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorDecoratorImpl
	 * @see org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl#getPropertyDescriptorDecorator()
	 * @generated
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR = 2;
	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.decorators.impl.PropertySourceAdapterInformationImpl <em>Property Source Adapter Information</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.decorators.impl.PropertySourceAdapterInformationImpl
	 * @see org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl#getPropertySourceAdapterInformation()
	 * @generated
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION = 1;
	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION__EANNOTATIONS = EcorePackage.EANNOTATION__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION__SOURCE = EcorePackage.EANNOTATION__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION__DETAILS = EcorePackage.EANNOTATION__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT = EcorePackage.EANNOTATION__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION__CONTENTS = EcorePackage.EANNOTATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION__REFERENCES = EcorePackage.EANNOTATION__REFERENCES;

	/**
	 * The feature id for the '<em><b>Property Source Adapter Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME = EcorePackage.EANNOTATION_FEATURE_COUNT + 0;
	/**
	 * The number of structural features of the the '<em>Property Source Adapter Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_SOURCE_ADAPTER_INFORMATION_FEATURE_COUNT = EcorePackage.EANNOTATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS = EcorePackage.EANNOTATION__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__SOURCE = EcorePackage.EANNOTATION__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__DETAILS = EcorePackage.EANNOTATION__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT = EcorePackage.EANNOTATION__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__CONTENTS = EcorePackage.EANNOTATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__REFERENCES = EcorePackage.EANNOTATION__REFERENCES;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__HIDDEN = EcorePackage.EANNOTATION_FEATURE_COUNT + 0;
	/**
	 * The feature id for the '<em><b>Help Context Ids String</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING = EcorePackage.EANNOTATION_FEATURE_COUNT + 1;
	/**
	 * The feature id for the '<em><b>Preferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__PREFERRED = EcorePackage.EANNOTATION_FEATURE_COUNT + 2;
	/**
	 * The feature id for the '<em><b>Category String</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING = EcorePackage.EANNOTATION_FEATURE_COUNT + 3;
	/**
	 * The feature id for the '<em><b>Filter Flag Strings</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS = EcorePackage.EANNOTATION_FEATURE_COUNT + 4;
	/**
	 * The feature id for the '<em><b>Display Name String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING = EcorePackage.EANNOTATION_FEATURE_COUNT + 5;
	/**
	 * The feature id for the '<em><b>Description String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING = EcorePackage.EANNOTATION_FEATURE_COUNT + 6;
	/**
	 * The number of structural features of the the '<em>Feature Descriptor Decorator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT = EcorePackage.EANNOTATION_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__EANNOTATIONS = FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__SOURCE = FEATURE_DESCRIPTOR_DECORATOR__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__DETAILS = FEATURE_DESCRIPTOR_DECORATOR__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT = FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__CONTENTS = FEATURE_DESCRIPTOR_DECORATOR__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__REFERENCES = FEATURE_DESCRIPTOR_DECORATOR__REFERENCES;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__HIDDEN = FEATURE_DESCRIPTOR_DECORATOR__HIDDEN;
	/**
	 * The feature id for the '<em><b>Help Context Ids String</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING = FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING;
	/**
	 * The feature id for the '<em><b>Preferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__PREFERRED = FEATURE_DESCRIPTOR_DECORATOR__PREFERRED;
	/**
	 * The feature id for the '<em><b>Category String</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__CATEGORY_STRING = FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING;
	/**
	 * The feature id for the '<em><b>Filter Flag Strings</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS = FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS;
	/**
	 * The feature id for the '<em><b>Display Name String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING = FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING;
	/**
	 * The feature id for the '<em><b>Description String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING = FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING;
	/**
	 * The feature id for the '<em><b>Cell Editor Validator Classnames</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 0;
	/**
	 * The feature id for the '<em><b>Label Provider Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 1;
	/**
	 * The feature id for the '<em><b>Cell Editor Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__CELL_EDITOR_CLASSNAME = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 2;
	/**
	 * The feature id for the '<em><b>Null Invalid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__NULL_INVALID = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Entry Expandable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__ENTRY_EXPANDABLE = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Designtime Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 5;
	/**
	 * The feature id for the '<em><b>Always Incompatible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR__ALWAYS_INCOMPATIBLE = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the the '<em>Property Descriptor Decorator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_DECORATOR_FEATURE_COUNT = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorInformationImpl <em>Property Descriptor Information</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorInformationImpl
	 * @see org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl#getPropertyDescriptorInformation()
	 * @generated
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION = 3;
	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__EANNOTATIONS = EcorePackage.EANNOTATION__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__SOURCE = EcorePackage.EANNOTATION__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__DETAILS = EcorePackage.EANNOTATION__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT = EcorePackage.EANNOTATION__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__CONTENTS = EcorePackage.EANNOTATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__REFERENCES = EcorePackage.EANNOTATION__REFERENCES;

	/**
	 * The feature id for the '<em><b>Adapter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER = EcorePackage.EANNOTATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Property Descriptor Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME = EcorePackage.EANNOTATION_FEATURE_COUNT + 1;
	/**
	 * The number of structural features of the the '<em>Property Descriptor Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_DESCRIPTOR_INFORMATION_FEATURE_COUNT = EcorePackage.EANNOTATION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__EANNOTATIONS = FEATURE_DESCRIPTOR_DECORATOR__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__SOURCE = FEATURE_DESCRIPTOR_DECORATOR__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__DETAILS = FEATURE_DESCRIPTOR_DECORATOR__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT = FEATURE_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__CONTENTS = FEATURE_DESCRIPTOR_DECORATOR__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__REFERENCES = FEATURE_DESCRIPTOR_DECORATOR__REFERENCES;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__HIDDEN = FEATURE_DESCRIPTOR_DECORATOR__HIDDEN;
	/**
	 * The feature id for the '<em><b>Help Context Ids String</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING = FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING;
	/**
	 * The feature id for the '<em><b>Preferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__PREFERRED = FEATURE_DESCRIPTOR_DECORATOR__PREFERRED;
	/**
	 * The feature id for the '<em><b>Category String</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__CATEGORY_STRING = FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING;
	/**
	 * The feature id for the '<em><b>Filter Flag Strings</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS = FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS;
	/**
	 * The feature id for the '<em><b>Display Name String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING = FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING;
	/**
	 * The feature id for the '<em><b>Description String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING = FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING;
	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 0;
	/**
	 * The feature id for the '<em><b>Customizer Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__CUSTOMIZER_CLASSNAME = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 1;
	/**
	 * The feature id for the '<em><b>Tree View Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 2;
	/**
	 * The feature id for the '<em><b>Graph View Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__GRAPH_VIEW_CLASSNAME = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 3;
	/**
	 * The feature id for the '<em><b>Model Adapter Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__MODEL_ADAPTER_CLASSNAME = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 4;
	/**
	 * The feature id for the '<em><b>Default Palette</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__DEFAULT_PALETTE = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 5;
	/**
	 * The feature id for the '<em><b>Label Provider Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 6;
	/**
	 * The feature id for the '<em><b>Graphic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR__GRAPHIC = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 7;
	/**
	 * The number of structural features of the the '<em>Class Descriptor Decorator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_DESCRIPTOR_DECORATOR_FEATURE_COUNT = FEATURE_DESCRIPTOR_DECORATOR_FEATURE_COUNT + 8;


	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/ve/internal/cde/decorators.ecore"; //$NON-NLS-1$
	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.ve.internal.cde.decorators"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DecoratorsPackage eINSTANCE = org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl.init();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator <em>Base Property Decorator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Property Decorator</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator
	 * @generated
	 */
	EClass getBasePropertyDecorator();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorValidatorClassnames <em>Cell Editor Validator Classnames</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Cell Editor Validator Classnames</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorValidatorClassnames()
	 * @see #getBasePropertyDecorator()
	 * @generated
	 */
	EAttribute getBasePropertyDecorator_CellEditorValidatorClassnames();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label Provider Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getLabelProviderClassname()
	 * @see #getBasePropertyDecorator()
	 * @generated
	 */
	EAttribute getBasePropertyDecorator_LabelProviderClassname();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorClassname <em>Cell Editor Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cell Editor Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorClassname()
	 * @see #getBasePropertyDecorator()
	 * @generated
	 */
	EAttribute getBasePropertyDecorator_CellEditorClassname();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isNullInvalid <em>Null Invalid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Null Invalid</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isNullInvalid()
	 * @see #getBasePropertyDecorator()
	 * @generated
	 */
	EAttribute getBasePropertyDecorator_NullInvalid();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isEntryExpandable <em>Entry Expandable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Entry Expandable</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isEntryExpandable()
	 * @see #getBasePropertyDecorator()
	 * @generated
	 */
	EAttribute getBasePropertyDecorator_EntryExpandable();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator <em>Class Descriptor Decorator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Class Descriptor Decorator</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator
	 * @generated
	 */
	EClass getClassDescriptorDecorator();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getCustomizerClassname <em>Customizer Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Customizer Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getCustomizerClassname()
	 * @see #getClassDescriptorDecorator()
	 * @generated
	 */
	EAttribute getClassDescriptorDecorator_CustomizerClassname();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getTreeViewClassname <em>Tree View Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tree View Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getTreeViewClassname()
	 * @see #getClassDescriptorDecorator()
	 * @generated
	 */
	EAttribute getClassDescriptorDecorator_TreeViewClassname();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphViewClassname <em>Graph View Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Graph View Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphViewClassname()
	 * @see #getClassDescriptorDecorator()
	 * @generated
	 */
	EAttribute getClassDescriptorDecorator_GraphViewClassname();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getModelAdapterClassname <em>Model Adapter Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Adapter Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getModelAdapterClassname()
	 * @see #getClassDescriptorDecorator()
	 * @generated
	 */
	EAttribute getClassDescriptorDecorator_ModelAdapterClassname();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getDefaultPalette <em>Default Palette</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Palette</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getDefaultPalette()
	 * @see #getClassDescriptorDecorator()
	 * @generated
	 */
	EAttribute getClassDescriptorDecorator_DefaultPalette();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphic <em>Graphic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Graphic</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphic()
	 * @see #getClassDescriptorDecorator()
	 * @generated
	 */
	EReference getClassDescriptorDecorator_Graphic();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label Provider Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getLabelProviderClassname()
	 * @see #getClassDescriptorDecorator()
	 * @generated
	 */
	EAttribute getClassDescriptorDecorator_LabelProviderClassname();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator <em>Feature Descriptor Decorator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Descriptor Decorator</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator
	 * @generated
	 */
	EClass getFeatureDescriptorDecorator();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDisplayNameString <em>Display Name String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Display Name String</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDisplayNameString()
	 * @see #getFeatureDescriptorDecorator()
	 * @generated
	 */
	EReference getFeatureDescriptorDecorator_DisplayNameString();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDescriptionString <em>Description String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Description String</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDescriptionString()
	 * @see #getFeatureDescriptorDecorator()
	 * @generated
	 */
	EReference getFeatureDescriptorDecorator_DescriptionString();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isHidden <em>Hidden</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hidden</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isHidden()
	 * @see #getFeatureDescriptorDecorator()
	 * @generated
	 */
	EAttribute getFeatureDescriptorDecorator_Hidden();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getHelpContextIdsString <em>Help Context Ids String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Help Context Ids String</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getHelpContextIdsString()
	 * @see #getFeatureDescriptorDecorator()
	 * @generated
	 */
	EAttribute getFeatureDescriptorDecorator_HelpContextIdsString();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isPreferred <em>Preferred</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Preferred</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isPreferred()
	 * @see #getFeatureDescriptorDecorator()
	 * @generated
	 */
	EAttribute getFeatureDescriptorDecorator_Preferred();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getCategoryString <em>Category String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Category String</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getCategoryString()
	 * @see #getFeatureDescriptorDecorator()
	 * @generated
	 */
	EReference getFeatureDescriptorDecorator_CategoryString();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getFilterFlagStrings <em>Filter Flag Strings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Filter Flag Strings</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getFilterFlagStrings()
	 * @see #getFeatureDescriptorDecorator()
	 * @generated
	 */
	EReference getFeatureDescriptorDecorator_FilterFlagStrings();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator <em>Property Descriptor Decorator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Descriptor Decorator</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator
	 * @generated
	 */
	EClass getPropertyDescriptorDecorator();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isDesigntimeProperty <em>Designtime Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Designtime Property</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isDesigntimeProperty()
	 * @see #getPropertyDescriptorDecorator()
	 * @generated
	 */
	EAttribute getPropertyDescriptorDecorator_DesigntimeProperty();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isAlwaysIncompatible <em>Always Incompatible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Always Incompatible</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isAlwaysIncompatible()
	 * @see #getPropertyDescriptorDecorator()
	 * @generated
	 */
	EAttribute getPropertyDescriptorDecorator_AlwaysIncompatible();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation <em>Property Source Adapter Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Source Adapter Information</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation
	 * @generated
	 */
	EClass getPropertySourceAdapterInformation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation#getPropertySourceAdapterClassname <em>Property Source Adapter Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property Source Adapter Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation#getPropertySourceAdapterClassname()
	 * @see #getPropertySourceAdapterInformation()
	 * @generated
	 */
	EAttribute getPropertySourceAdapterInformation_PropertySourceAdapterClassname();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation <em>Property Descriptor Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Descriptor Information</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation
	 * @generated
	 */
	EClass getPropertyDescriptorInformation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#isAdapter <em>Adapter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Adapter</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#isAdapter()
	 * @see #getPropertyDescriptorInformation()
	 * @generated
	 */
	EAttribute getPropertyDescriptorInformation_Adapter();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#getPropertyDescriptorClassname <em>Property Descriptor Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property Descriptor Classname</em>'.
	 * @see org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#getPropertyDescriptorClassname()
	 * @see #getPropertyDescriptorInformation()
	 * @generated
	 */
	EAttribute getPropertyDescriptorInformation_PropertyDescriptorClassname();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DecoratorsFactory getDecoratorsFactory();

}
