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
 *  $RCSfile: BasePropertyDecorator.java,v $
 *  $Revision: 1.8 $  $Date: 2007-05-25 04:09:36 $ 
 */


import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Property Decorator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is base information for properties. This will be on an EClassifier for how to edit a property of the type of the EClassifier.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorValidatorClassnames <em>Cell Editor Validator Classnames</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorClassname <em>Cell Editor Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isNullInvalid <em>Null Invalid</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isEntryExpandable <em>Entry Expandable</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getBasePropertyDecorator()
 * @model
 * @generated
 */
public interface BasePropertyDecorator extends EAnnotation{


	/**
	 * Returns the value of the '<em><b>Cell Editor Validator Classnames</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cell Editor Validator Classnames</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cell Editor Validator Classnames</em>' attribute list.
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getBasePropertyDecorator_CellEditorValidatorClassnames()
	 * @model type="java.lang.String"
	 * @generated
	 */
	EList<String> getCellEditorValidatorClassnames();

	/**
	 * Returns the value of the '<em><b>Label Provider Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Label Provider Classname. If set, but is null, then that means there explicitly isn't one and don't use default.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Label Provider Classname</em>' attribute.
	 * @see #isSetLabelProviderClassname()
	 * @see #unsetLabelProviderClassname()
	 * @see #setLabelProviderClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getBasePropertyDecorator_LabelProviderClassname()
	 * @model unsettable="true"
	 * @generated
	 */
	String getLabelProviderClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label Provider Classname</em>' attribute.
	 * @see #isSetLabelProviderClassname()
	 * @see #unsetLabelProviderClassname()
	 * @see #getLabelProviderClassname()
	 * @generated
	 */
	void setLabelProviderClassname(String value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetLabelProviderClassname()
	 * @see #getLabelProviderClassname()
	 * @see #setLabelProviderClassname(String)
	 * @generated
	 */
	void unsetLabelProviderClassname();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Label Provider Classname</em>' attribute is set.
	 * @see #unsetLabelProviderClassname()
	 * @see #getLabelProviderClassname()
	 * @see #setLabelProviderClassname(String)
	 * @generated
	 */
	boolean isSetLabelProviderClassname();

	/**
	 * Returns the value of the '<em><b>Cell Editor Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cell Editor Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Cell Editor Classname. If set, but is null, then that means there explicitly isn't one and don't use default.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Cell Editor Classname</em>' attribute.
	 * @see #isSetCellEditorClassname()
	 * @see #unsetCellEditorClassname()
	 * @see #setCellEditorClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getBasePropertyDecorator_CellEditorClassname()
	 * @model unsettable="true"
	 * @generated
	 */
	String getCellEditorClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorClassname <em>Cell Editor Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cell Editor Classname</em>' attribute.
	 * @see #isSetCellEditorClassname()
	 * @see #unsetCellEditorClassname()
	 * @see #getCellEditorClassname()
	 * @generated
	 */
	void setCellEditorClassname(String value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorClassname <em>Cell Editor Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCellEditorClassname()
	 * @see #getCellEditorClassname()
	 * @see #setCellEditorClassname(String)
	 * @generated
	 */
	void unsetCellEditorClassname();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#getCellEditorClassname <em>Cell Editor Classname</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cell Editor Classname</em>' attribute is set.
	 * @see #unsetCellEditorClassname()
	 * @see #getCellEditorClassname()
	 * @see #setCellEditorClassname(String)
	 * @generated
	 */
	boolean isSetCellEditorClassname();

	/**
	 * Returns the value of the '<em><b>Null Invalid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Null Invalid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Are null settings invalid. If this is true, then they will be prevented. If this is false, then the cell editor and validator will be used to determine if null is valid or not.
	 * 
	 * The search order will be find it explicitly set on:
	 *   1) The BasePropertyDescriptor on the feature, then if not set,
	 *   2) The BasePropertyDescriptor on the class, then if not set,
	 *   3) use false as the value.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Null Invalid</em>' attribute.
	 * @see #isSetNullInvalid()
	 * @see #unsetNullInvalid()
	 * @see #setNullInvalid(boolean)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getBasePropertyDecorator_NullInvalid()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isNullInvalid();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isNullInvalid <em>Null Invalid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Null Invalid</em>' attribute.
	 * @see #isSetNullInvalid()
	 * @see #unsetNullInvalid()
	 * @see #isNullInvalid()
	 * @generated
	 */
	void setNullInvalid(boolean value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isNullInvalid <em>Null Invalid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNullInvalid()
	 * @see #isNullInvalid()
	 * @see #setNullInvalid(boolean)
	 * @generated
	 */
	void unsetNullInvalid();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isNullInvalid <em>Null Invalid</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Null Invalid</em>' attribute is set.
	 * @see #unsetNullInvalid()
	 * @see #isNullInvalid()
	 * @see #setNullInvalid(boolean)
	 * @generated
	 */
	boolean isSetNullInvalid();

	/**
	 * Returns the value of the '<em><b>Entry Expandable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entry Expandable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * If this returns true, then the entry will expand in the property sheet if 
	 * there are child entries. If this returns false then it won't expand. This is useful when the cell editor handles the complete customization
	 * and it is not desired to allow sub-property customization.
	 * 
	 * The search order will be find it explicitly set on:
	 *   1) The BasePropertyDescriptor on the feature, then if not set,
	 *   2) The BasePropertyDescriptor on the class, then if not set,
	 *   3) use true as the value.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Entry Expandable</em>' attribute.
	 * @see #isSetEntryExpandable()
	 * @see #unsetEntryExpandable()
	 * @see #setEntryExpandable(boolean)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getBasePropertyDecorator_EntryExpandable()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isEntryExpandable();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isEntryExpandable <em>Entry Expandable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entry Expandable</em>' attribute.
	 * @see #isSetEntryExpandable()
	 * @see #unsetEntryExpandable()
	 * @see #isEntryExpandable()
	 * @generated
	 */
	void setEntryExpandable(boolean value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isEntryExpandable <em>Entry Expandable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEntryExpandable()
	 * @see #isEntryExpandable()
	 * @see #setEntryExpandable(boolean)
	 * @generated
	 */
	void unsetEntryExpandable();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator#isEntryExpandable <em>Entry Expandable</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Entry Expandable</em>' attribute is set.
	 * @see #unsetEntryExpandable()
	 * @see #isEntryExpandable()
	 * @see #setEntryExpandable(boolean)
	 * @generated
	 */
	boolean isSetEntryExpandable();

	/** 
	 * @param source for the object whose property the label provider is going to be used for 
	 * @return An instantiated label provider
	 */
	ILabelProvider getLabelProvider(IPropertyDescriptor aPropertyDescriptor);
	
}
