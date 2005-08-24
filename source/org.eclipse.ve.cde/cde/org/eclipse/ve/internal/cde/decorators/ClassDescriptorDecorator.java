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
 *  $RCSfile: ClassDescriptorDecorator.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:12:51 $ 
 */
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ve.internal.cde.utility.Graphic;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class Descriptor Decorator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The descriptor decorator for a class. Currently it adds nothing over FeatureDescriptorDecortor.
 * <!-- end-model-doc -->
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
public interface ClassDescriptorDecorator extends FeatureDescriptorDecorator, KeyedValueHolder{
	/**
	 * Returns the value of the '<em><b>Customizer Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Customizer Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Classname string of the Customizer class.
	 * 
	 * The format of the string is "classname:initdata" or "plugin/classname:initdata"
	 * <!-- end-model-doc -->
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
	 * <!-- begin-model-doc -->
	 * Classname of the TreeView EditPart.
	 * 
	 * The format of the string is "classname:initdata" or "plugin/classname:initdata"
	 * <!-- end-model-doc -->
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
	 * <!-- begin-model-doc -->
	 * Classname of the GraphView EditPart.
	 * 
	 * The format of the string is "classname:initdata" or "plugin/classname:initdata"
	 * <!-- end-model-doc -->
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
	 * <!-- begin-model-doc -->
	 * Classname of the Model Adapter. This is the class that either implements all of the interfaces required by the model adapter, or it should be an IAdaptable that will return such adapters. See IModelAdapterFactory.
	 * 
	 * The format of the string is "classname:initdata" or "plugin/classname:initdata"
	 * <!-- end-model-doc -->
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
	 * <!-- begin-model-doc -->
	 * The default palette to use for this class if not specified by other means. It is in the form a MOF reference, i.e. "filename#id of palette".
	 * <!-- end-model-doc -->
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
	 * <!-- begin-model-doc -->
	 * The classname for the label provider to be used for objects of the class that this decorator decorates. This label provider will be used in cases other than property sheet. In that case the labelProvider from the BasePropertyDecorator on this class will be used. (Though the default if there is no BasePropertyDecorator will be to use the string from this label provider, but not the image).
	 * 
	 * The format of the string is "classname:initdata" or "plugin/classname:initdata"
	 * <!-- end-model-doc -->
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

	/**
	 * @param object
	 * @return GraphicalEditPart
	 */
  GraphicalEditPart createGraphicalEditPart(Object object);

	/**
	 * @param object
	 * @return TreeEditPart
	 */
  TreeEditPart createTreeEditPart(Object object);
  
  
	/**
	 * @return LabelProvider
	 */
	ILabelProvider getLabelProvider();

} // ClassDescriptorDecorator
