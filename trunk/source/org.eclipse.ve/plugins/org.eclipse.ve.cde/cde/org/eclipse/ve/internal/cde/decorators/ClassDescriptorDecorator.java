package org.eclipse.ve.internal.cde.decorators;
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
 *  $RCSfile: ClassDescriptorDecorator.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import org.eclipse.ve.internal.cde.utility.Graphic;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class Descriptor Decorator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getCustomizerClassname <em>Customizer Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getTreeViewClassname <em>Tree View Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphViewClassname <em>Graph View Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getModelAdapterClassname <em>Model Adapter Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getDefaultPalette <em>Default Palette</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphic <em>Graphic</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator()
 * @model 
 * @generated
 */
public interface ClassDescriptorDecorator extends FeatureDescriptorDecorator, KeyedValueHolder {
	/**
	 * Returns the value of the '<em><b>Customizer Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Customizer Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Customizer Classname</em>' attribute.
	 * @see #setCustomizerClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator_CustomizerClassname()
	 * @model 
	 * @generated
	 */
  String getCustomizerClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getCustomizerClassname <em>Customizer Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Customizer Classname</em>' attribute.
	 * @see #getCustomizerClassname()
	 * @generated
	 */
  void setCustomizerClassname(String value);

	/**
	 * Returns the value of the '<em><b>Tree View Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tree View Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tree View Classname</em>' attribute.
	 * @see #setTreeViewClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator_TreeViewClassname()
	 * @model 
	 * @generated
	 */
  String getTreeViewClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getTreeViewClassname <em>Tree View Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tree View Classname</em>' attribute.
	 * @see #getTreeViewClassname()
	 * @generated
	 */
  void setTreeViewClassname(String value);

	/**
	 * Returns the value of the '<em><b>Graph View Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Graph View Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Graph View Classname</em>' attribute.
	 * @see #setGraphViewClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator_GraphViewClassname()
	 * @model 
	 * @generated
	 */
  String getGraphViewClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphViewClassname <em>Graph View Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Graph View Classname</em>' attribute.
	 * @see #getGraphViewClassname()
	 * @generated
	 */
  void setGraphViewClassname(String value);

	/**
	 * Returns the value of the '<em><b>Model Adapter Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Adapter Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Adapter Classname</em>' attribute.
	 * @see #setModelAdapterClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator_ModelAdapterClassname()
	 * @model 
	 * @generated
	 */
  String getModelAdapterClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getModelAdapterClassname <em>Model Adapter Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Adapter Classname</em>' attribute.
	 * @see #getModelAdapterClassname()
	 * @generated
	 */
  void setModelAdapterClassname(String value);

	/**
	 * Returns the value of the '<em><b>Default Palette</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Palette</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Palette</em>' attribute.
	 * @see #setDefaultPalette(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator_DefaultPalette()
	 * @model 
	 * @generated
	 */
  String getDefaultPalette();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getDefaultPalette <em>Default Palette</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Palette</em>' attribute.
	 * @see #getDefaultPalette()
	 * @generated
	 */
  void setDefaultPalette(String value);

	/**
	 * Returns the value of the '<em><b>Label Provider Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label Provider Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label Provider Classname</em>' attribute.
	 * @see #setLabelProviderClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator_LabelProviderClassname()
	 * @model 
	 * @generated
	 */
  String getLabelProviderClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getLabelProviderClassname <em>Label Provider Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label Provider Classname</em>' attribute.
	 * @see #getLabelProviderClassname()
	 * @generated
	 */
  void setLabelProviderClassname(String value);

	/**
	 * Returns the value of the '<em><b>Graphic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Graphic</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Graphic</em>' containment reference.
	 * @see #setGraphic(Graphic)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getClassDescriptorDecorator_Graphic()
	 * @model containment="true"
	 * @generated
	 */
  Graphic getGraphic();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator#getGraphic <em>Graphic</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Graphic</em>' containment reference.
	 * @see #getGraphic()
	 * @generated
	 */
  void setGraphic(Graphic value);

} // ClassDescriptorDecorator
