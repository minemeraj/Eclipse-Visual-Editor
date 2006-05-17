/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.decorators.impl;
/*
 *  $RCSfile: ClassDescriptorDecoratorImpl.java,v $
 *  $Revision: 1.14 $  $Date: 2006-05-17 20:13:52 $ 
 */

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.jface.viewers.ILabelProvider;

import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
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
		return DecoratorsPackage.Literals.CLASS_DESCRIPTOR_DECORATOR;
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

  public void setTreeViewClassname(String newTreeViewClassname) {
  	hasRetrievedTreeEditPartClass = false;
  	treeEditPartClass = null;
  	treeEditPartConstructor = null;
  	setTreeViewClassnameGen(newTreeViewClassname);
  }
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setTreeViewClassnameGen(String newTreeViewClassname) {
		String oldTreeViewClassname = treeViewClassname;
		treeViewClassname = newTreeViewClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME, oldTreeViewClassname, treeViewClassname));
	}

  public void setGraphViewClassname(String newGraphViewClassname) {
  	hasRetrievedGraphicalEditPartClass = false;
  	graphicalEditPartClass = null;
  	graphicalEditPartConstructor = null;
  	setGraphViewClassnameGen(newGraphViewClassname);
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
  public void setGraphViewClassnameGen(String newGraphViewClassname) {
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

  public void setLabelProviderClassname(String newLabelProviderClassname) {
  	hasRetrievedLabelProviderClass = false;
  	labelProviderClass = null;
  	setLabelProviderClassnameGen(newLabelProviderClassname);
  }
  
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
  public void setLabelProviderClassnameGen(String newLabelProviderClassname) {
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
				return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__GRAPHIC:
				return basicSetGraphic(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
				if (coreType) return getKeyedValues();
				else return getKeyedValues().map();
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
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__KEYED_VALUES:
				((EStructuralFeature.Setting)getKeyedValues()).set(newValue);
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
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
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
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean eIsSetGen(int featureID) {
		switch (featureID) {
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
		return super.eIsSet(featureID);
	}

	/*
	 * Called by overrides to eIsSet to test if source is set. This is because for the 
	 * FeatureDecorator and subclasses, setting source to the classname is considered
	 * to be not set since that is the new default for each class level. By doing this
	 * when serializing it won't waste space and time adding a copy of the source string
	 * to the serialized output and then creating a NEW copy on each decorator loaded
	 * from an XMI file. 
	 * 
	 * @return <code>true</code> if source is not null and not equal to class name.
	 * 
	 * @since 1.1.0
	 */
	public boolean eIsSet(int featureId) {
		// See the FeatureDescriptorDecoratorImpl.eIsSet(EStructuralFeature) for why need 
		// to do it this way.
		switch (featureId) {
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR__SOURCE:
				return source != null && !eClass().getInstanceClassName().equals(source);
			default:
				return eIsSetGen(featureId);
		}
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

	private Constructor graphicalEditPartConstructor;	// Cached only if there is a constructor that takes an argument
	private Class graphicalEditPartClass;  // Cache of the graphical edit part class
	private boolean hasRetrievedGraphicalEditPartClass;
	/** 
	 * Return graphical edit part for the argument, caching the EditPart class and constructor used
	 */
	public GraphicalEditPart createGraphicalEditPart(Object object) {

		try {		
			if (!hasRetrievedGraphicalEditPartClass) {
				if (getGraphViewClassname() != null) {
					graphicalEditPartClass = CDEPlugin.getClassFromString(getGraphViewClassname());
					try {
						graphicalEditPartConstructor = graphicalEditPartClass.getConstructor(new Class[] { Object.class});
					} catch (NoSuchMethodException exc) {
						// It's possible there is no argument with a constructor so just continue
					}
				} 
				hasRetrievedGraphicalEditPartClass= true;
			} 		

			if(graphicalEditPartClass != null){
				if(graphicalEditPartConstructor == null){
					return (GraphicalEditPart)CDEPlugin.setInitializationData(graphicalEditPartClass.newInstance(), getGraphViewClassname(), null);
				} else {
					return (GraphicalEditPart) CDEPlugin.setInitializationData(graphicalEditPartConstructor.newInstance(new Object[] { object }), getGraphViewClassname(), null);
				}
			} 
		} catch (Exception e){
			String message =
				java.text.MessageFormat.format(
						CDEMessages.Object_noinstantiate_EXC_, 
							new Object[] { getGraphViewClassname() });
							Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
							CDEPlugin.getPlugin().getLog().log(s);
		}
		return null;		
	}
	
	private Constructor treeEditPartConstructor;	// Cached only if there is a constructor that takes an argument
	private Class treeEditPartClass;  // Cache of the tree edit part class
	private boolean hasRetrievedTreeEditPartClass;
	
	/** 
	 * Return Tree edit part for the argument, caching the EditPart class and constructor used
	 */
	public TreeEditPart createTreeEditPart(Object object) {

		try {		
			if (!hasRetrievedTreeEditPartClass) {
				if (getTreeViewClassname() != null) {
					treeEditPartClass = CDEPlugin.getClassFromString(getTreeViewClassname());
					if (treeEditPartClass != null) {
						try {
							treeEditPartConstructor = treeEditPartClass.getConstructor(new Class[] { Object.class});
						} catch (NoSuchMethodException exc) {
							// It's possible there is no argument with a constructor so just continue
						}
					}
				}
				hasRetrievedTreeEditPartClass = true;
			} 		

			if(treeEditPartClass != null){
				if(treeEditPartConstructor == null){
					return (TreeEditPart)CDEPlugin.setInitializationData(treeEditPartClass.newInstance(), getTreeViewClassname(), null);
				} else {
					return (TreeEditPart)CDEPlugin.setInitializationData(treeEditPartConstructor.newInstance(new Object[] { object }), getTreeViewClassname(), null);
				}
			} 
		} catch (Exception e){
			String message =
				java.text.MessageFormat.format(
						CDEMessages.Object_noinstantiate_EXC_, 
							new Object[] { getTreeViewClassname() });
							Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
							CDEPlugin.getPlugin().getLog().log(s);
		}
		return null;		
	}

	private Class labelProviderClass;
	private boolean hasRetrievedLabelProviderClass;
	
	/**
	 * Return LabelProvider using a cache'd instance of the label provider class
	 */
	public ILabelProvider getLabelProvider() {

		try {
			if (!hasRetrievedLabelProviderClass) {
				if (getLabelProviderClassname() != null) {
					labelProviderClass = CDEPlugin.getClassFromString(getLabelProviderClassname());
				}
				hasRetrievedLabelProviderClass = true;
			}
			if (labelProviderClass != null) {
				ILabelProvider result = (ILabelProvider) labelProviderClass.newInstance();
				// Set the initData
				CDEPlugin.setInitializationData(result, getLabelProviderClassname(), null);
				return result;
			}
		} catch (Exception exc) {
			String msg = MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { labelProviderClass}); 
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, exc));
		}
		return null;
	}	

} //ClassDescriptorDecoratorImpl
