/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.ve.internal.cde.decorators.impl;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ClassDescriptorDecoratorImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:17:52 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;
import org.eclipse.ve.internal.cde.utility.Graphic;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.cdm.impl.MapEntryImpl;
import org.eclipse.ve.internal.cdm.model.KeyedValueHolderHelper;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Class Descriptor Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getKeyedValues <em>Keyed Values</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getCustomizerClassname <em>Customizer Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getTreeViewClassname <em>Tree View Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getGraphViewClassname <em>Graph View Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getModelAdapterClassname <em>Model Adapter Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getDefaultPalette <em>Default Palette</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getLabelProviderClassname <em>Label Provider Classname</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.ClassDescriptorDecoratorImpl#getGraphic <em>Graphic</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ClassDescriptorDecoratorImpl extends FeatureDescriptorDecoratorImpl implements ClassDescriptorDecorator
{
	/*
	 * This methods are here only because generation of KeyedValueHolders implementations
	 * have an import for MapEntryImpl, even though never actually used. This gets rid
	 * of the unused import warning that would occur after every generation.
	 */
	private static MapEntryImpl dummy() {
		return null;
	}
	
	ClassDescriptorDecoratorImpl(int notused) {
		this();
		dummy();
	}
	
	/**
	 * The cached value of the '{@link #getKeyedValues() <em>Keyed Values</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyedValues()
	 * @generated
	 * @ordered
	 */
	protected EMap keyedValues = null;


	/**
	 * The default value of the '{@link #getCustomizerClassname() <em>Customizer Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCustomizerClassname()
	 * @generated
	 * @ordered
	 */
  protected static final String CUSTOMIZER_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCustomizerClassname() <em>Customizer Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCustomizerClassname()
	 * @generated
	 * @ordered
	 */
  protected String customizerClassname = CUSTOMIZER_CLASSNAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTreeViewClassname() <em>Tree View Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTreeViewClassname()
	 * @generated
	 * @ordered
	 */
  protected static final String TREE_VIEW_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTreeViewClassname() <em>Tree View Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTreeViewClassname()
	 * @generated
	 * @ordered
	 */
  protected String treeViewClassname = TREE_VIEW_CLASSNAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getGraphViewClassname() <em>Graph View Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraphViewClassname()
	 * @generated
	 * @ordered
	 */
  protected static final String GRAPH_VIEW_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGraphViewClassname() <em>Graph View Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraphViewClassname()
	 * @generated
	 * @ordered
	 */
  protected String graphViewClassname = GRAPH_VIEW_CLASSNAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getModelAdapterClassname() <em>Model Adapter Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelAdapterClassname()
	 * @generated
	 * @ordered
	 */
  protected static final String MODEL_ADAPTER_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModelAdapterClassname() <em>Model Adapter Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelAdapterClassname()
	 * @generated
	 * @ordered
	 */
  protected String modelAdapterClassname = MODEL_ADAPTER_CLASSNAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultPalette() <em>Default Palette</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultPalette()
	 * @generated
	 * @ordered
	 */
  protected static final String DEFAULT_PALETTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultPalette() <em>Default Palette</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultPalette()
	 * @generated
	 * @ordered
	 */
  protected String defaultPalette = DEFAULT_PALETTE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLabelProviderClassname() <em>Label Provider Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabelProviderClassname()
	 * @generated
	 * @ordered
	 */
  protected static final String LABEL_PROVIDER_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabelProviderClassname() <em>Label Provider Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabelProviderClassname()
	 * @generated
	 * @ordered
	 */
  protected String labelProviderClassname = LABEL_PROVIDER_CLASSNAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGraphic() <em>Graphic</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraphic()
	 * @generated
	 * @ordered
	 */
  protected Graphic graphic = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  protected ClassDescriptorDecoratorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  protected EClass eStaticClass() {
		return DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EMap getKeyedValues() {
		if (keyedValues == null) {
			keyedValues = KeyedValueHolderHelper.createKeyedValuesEMap(this, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES);
		}
		return keyedValues;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public String getCustomizerClassname() {
		return customizerClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setCustomizerClassname(String newCustomizerClassname) {
		String oldCustomizerClassname = customizerClassname;
		customizerClassname = newCustomizerClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CUSTOMIZER_CLASSNAME, oldCustomizerClassname, customizerClassname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public String getTreeViewClassname() {
		return treeViewClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setTreeViewClassname(String newTreeViewClassname) {
		String oldTreeViewClassname = treeViewClassname;
		treeViewClassname = newTreeViewClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME, oldTreeViewClassname, treeViewClassname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public String getGraphViewClassname() {
		return graphViewClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setGraphViewClassname(String newGraphViewClassname) {
		String oldGraphViewClassname = graphViewClassname;
		graphViewClassname = newGraphViewClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPH_VIEW_CLASSNAME, oldGraphViewClassname, graphViewClassname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public String getModelAdapterClassname() {
		return modelAdapterClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setModelAdapterClassname(String newModelAdapterClassname) {
		String oldModelAdapterClassname = modelAdapterClassname;
		modelAdapterClassname = newModelAdapterClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__MODEL_ADAPTER_CLASSNAME, oldModelAdapterClassname, modelAdapterClassname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public String getDefaultPalette() {
		return defaultPalette;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setDefaultPalette(String newDefaultPalette) {
		String oldDefaultPalette = defaultPalette;
		defaultPalette = newDefaultPalette;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DEFAULT_PALETTE, oldDefaultPalette, defaultPalette));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public String getLabelProviderClassname() {
		return labelProviderClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setLabelProviderClassname(String newLabelProviderClassname) {
		String oldLabelProviderClassname = labelProviderClassname;
		labelProviderClassname = newLabelProviderClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME, oldLabelProviderClassname, labelProviderClassname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public Graphic getGraphic() {
		return graphic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public NotificationChain basicSetGraphic(Graphic newGraphic, NotificationChain msgs) {
		Graphic oldGraphic = graphic;
		graphic = newGraphic;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC, oldGraphic, newGraphic);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setGraphic(Graphic newGraphic) {
		if (newGraphic != graphic) {
			NotificationChain msgs = null;
			if (graphic != null)
				msgs = ((InternalEObject)graphic).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC, null, msgs);
			if (newGraphic != null)
				msgs = ((InternalEObject)newGraphic).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC, null, msgs);
			msgs = basicSetGraphic(newGraphic, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC, newGraphic, newGraphic));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DETAILS:
					return ((InternalEList)getDetails()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
					return eBasicSetContainer(null, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CONTENTS:
					return ((InternalEList)getContents()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
					return basicSetDisplayNameString(null, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
					return basicSetDescriptionString(null, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC:
					return basicSetGraphic(null, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
					return ((InternalEObject)eContainer).eInverseRemove(this, EcorePackage.EMODEL_ELEMENT__EANNOTATIONS, EModelElement.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return ((InternalEObject)eContainer).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				return getEAnnotations();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__SOURCE:
				return getSource();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DETAILS:
				return getDetails();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CONTENTS:
				return getContents();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__REFERENCES:
				return getReferences();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HIDDEN:
				return isHidden() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				return getHelpContextIdsString();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__PREFERRED:
				return isPreferred() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				if (resolve) return getCategoryString();
				return basicGetCategoryString();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				return getFilterFlagStrings();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				return getDisplayNameString();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				return getDescriptionString();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
				return getKeyedValues();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CUSTOMIZER_CLASSNAME:
				return getCustomizerClassname();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME:
				return getTreeViewClassname();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPH_VIEW_CLASSNAME:
				return getGraphViewClassname();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__MODEL_ADAPTER_CLASSNAME:
				return getModelAdapterClassname();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DEFAULT_PALETTE:
				return getDefaultPalette();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				return getLabelProviderClassname();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC:
				return getGraphic();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__SOURCE:
				setSource((String)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DETAILS:
				getDetails().clear();
				getDetails().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HIDDEN:
				setHidden(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				getHelpContextIdsString().clear();
				getHelpContextIdsString().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__PREFERRED:
				setPreferred(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				setCategoryString((AbstractString)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				getFilterFlagStrings().clear();
				getFilterFlagStrings().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				setDisplayNameString((AbstractString)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				setDescriptionString((AbstractString)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CUSTOMIZER_CLASSNAME:
				setCustomizerClassname((String)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME:
				setTreeViewClassname((String)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPH_VIEW_CLASSNAME:
				setGraphViewClassname((String)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__MODEL_ADAPTER_CLASSNAME:
				setModelAdapterClassname((String)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DEFAULT_PALETTE:
				setDefaultPalette((String)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				setLabelProviderClassname((String)newValue);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC:
				setGraphic((Graphic)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DETAILS:
				getDetails().clear();
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)null);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CONTENTS:
				getContents().clear();
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__REFERENCES:
				getReferences().clear();
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HIDDEN:
				setHidden(HIDDEN_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				getHelpContextIdsString().clear();
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__PREFERRED:
				setPreferred(PREFERRED_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				setCategoryString((AbstractString)null);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				getFilterFlagStrings().clear();
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				setDisplayNameString((AbstractString)null);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				setDescriptionString((AbstractString)null);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CUSTOMIZER_CLASSNAME:
				setCustomizerClassname(CUSTOMIZER_CLASSNAME_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME:
				setTreeViewClassname(TREE_VIEW_CLASSNAME_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPH_VIEW_CLASSNAME:
				setGraphViewClassname(GRAPH_VIEW_CLASSNAME_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__MODEL_ADAPTER_CLASSNAME:
				setModelAdapterClassname(MODEL_ADAPTER_CLASSNAME_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DEFAULT_PALETTE:
				setDefaultPalette(DEFAULT_PALETTE_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				setLabelProviderClassname(LABEL_PROVIDER_CLASSNAME_EDEFAULT);
				return;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC:
				setGraphic((Graphic)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DETAILS:
				return details != null && !details.isEmpty();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement() != null;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CONTENTS:
				return contents != null && !contents.isEmpty();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__REFERENCES:
				return references != null && !references.isEmpty();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HIDDEN:
				return hidden != HIDDEN_EDEFAULT;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING:
				return helpContextIdsString != null && !helpContextIdsString.isEmpty();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__PREFERRED:
				return preferred != PREFERRED_EDEFAULT;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CATEGORY_STRING:
				return categoryString != null;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS:
				return filterFlagStrings != null && !filterFlagStrings.isEmpty();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING:
				return displayNameString != null;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING:
				return descriptionString != null;
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__CUSTOMIZER_CLASSNAME:
				return CUSTOMIZER_CLASSNAME_EDEFAULT == null ? customizerClassname != null : !CUSTOMIZER_CLASSNAME_EDEFAULT.equals(customizerClassname);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME:
				return TREE_VIEW_CLASSNAME_EDEFAULT == null ? treeViewClassname != null : !TREE_VIEW_CLASSNAME_EDEFAULT.equals(treeViewClassname);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPH_VIEW_CLASSNAME:
				return GRAPH_VIEW_CLASSNAME_EDEFAULT == null ? graphViewClassname != null : !GRAPH_VIEW_CLASSNAME_EDEFAULT.equals(graphViewClassname);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__MODEL_ADAPTER_CLASSNAME:
				return MODEL_ADAPTER_CLASSNAME_EDEFAULT == null ? modelAdapterClassname != null : !MODEL_ADAPTER_CLASSNAME_EDEFAULT.equals(modelAdapterClassname);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__DEFAULT_PALETTE:
				return DEFAULT_PALETTE_EDEFAULT == null ? defaultPalette != null : !DEFAULT_PALETTE_EDEFAULT.equals(defaultPalette);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME:
				return LABEL_PROVIDER_CLASSNAME_EDEFAULT == null ? labelProviderClassname != null : !LABEL_PROVIDER_CLASSNAME_EDEFAULT.equals(labelProviderClassname);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC:
				return graphic != null;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
		if (baseClass == KeyedValueHolder.class) {
			switch (derivedFeatureID) {
				case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES: return CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public int eDerivedStructuralFeatureID(int baseFeatureID, Class baseClass) {
		if (baseClass == KeyedValueHolder.class) {
			switch (baseFeatureID) {
				case CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES: return DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (customizerClassname: ");
		result.append(customizerClassname);
		result.append(", treeViewClassname: ");
		result.append(treeViewClassname);
		result.append(", graphViewClassname: ");
		result.append(graphViewClassname);
		result.append(", modelAdapterClassname: ");
		result.append(modelAdapterClassname);
		result.append(", defaultPalette: ");
		result.append(defaultPalette);
		result.append(", labelProviderClassname: ");
		result.append(labelProviderClassname);
		result.append(')');
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eObjectForURIFragmentSegment(java.lang.String)
	 */
	public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
		EObject eo = KeyedValueHolderHelper.eObjectForURIFragmentSegment(this, uriFragmentSegment);
		return eo == KeyedValueHolderHelper.NOT_KEYED_VALUES_FRAGMENT ? super.eObjectForURIFragmentSegment(uriFragmentSegment) : eo;
	}

} //ClassDescriptorDecoratorImpl
