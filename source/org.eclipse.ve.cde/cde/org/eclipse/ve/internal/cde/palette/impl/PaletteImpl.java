package org.eclipse.ve.internal.cde.palette.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PaletteImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;

import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.ICDEToolEntry;
import org.eclipse.ve.internal.cde.palette.Palette;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Palette</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.PaletteImpl#getPaletteLabel <em>Palette Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public abstract class PaletteImpl extends ContainerImpl implements Palette {

	

	/**
	 * The cached value of the '{@link #getPaletteLabel() <em>Palette Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaletteLabel()
	 * @generated
	 * @ordered
	 */
	protected AbstractString paletteLabel = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected PaletteImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getPalette();
	}

	/**
	 * Return the Control Group.
	 */
	public Group getControlGroup() {
		return getPaletteControlGroup();
	}

	/**
	 * Return the categories
	 */
	public List getCategories() {
		return getPaletteCategories();
	}

	/**
	 * getChildren for a standard palette is to merge the controlGroup with the children
	 * until. If something different is desired then subclass the palette.
	 */
	public List getChildren() {
		List result = new ArrayList(getPaletteCategories().size() + 1);
		Group cGroup = getControlGroup();
		if (cGroup != null)
			result.add(cGroup.getEntry()); // There may not be a control group.

		List c = getPaletteCategories();
		for (int i=0; i<c.size(); i++)
			result.add(((Category) c.get(i)).getEntry());
			
		return result;
	}

	protected Group getPaletteControlGroup() {
		throw new IllegalStateException("Must be implemented by subclass."); //$NON-NLS-1$
	}

	protected List getPaletteCategories() {
		throw new IllegalStateException("Must be implemented by subclass."); //$NON-NLS-1$
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getPaletteLabel() {
		return paletteLabel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPaletteLabel(AbstractString newPaletteLabel, NotificationChain msgs) {
		AbstractString oldPaletteLabel = paletteLabel;
		paletteLabel = newPaletteLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PalettePackage.PALETTE__PALETTE_LABEL, oldPaletteLabel, newPaletteLabel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPaletteLabel(AbstractString newPaletteLabel) {
		if (newPaletteLabel != paletteLabel) {
			NotificationChain msgs = null;
			if (paletteLabel != null)
				msgs = ((InternalEObject)paletteLabel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PalettePackage.PALETTE__PALETTE_LABEL, null, msgs);
			if (newPaletteLabel != null)
				msgs = ((InternalEObject)newPaletteLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PalettePackage.PALETTE__PALETTE_LABEL, null, msgs);
			msgs = basicSetPaletteLabel(newPaletteLabel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.PALETTE__PALETTE_LABEL, newPaletteLabel, newPaletteLabel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.PALETTE__PALETTE_LABEL:
					return basicSetPaletteLabel(null, msgs);
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
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.PALETTE__PALETTE_LABEL:
				return getPaletteLabel();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.PALETTE__PALETTE_LABEL:
				return paletteLabel != null;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.PALETTE__PALETTE_LABEL:
				setPaletteLabel((AbstractString)newValue);
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
			case PalettePackage.PALETTE__PALETTE_LABEL:
				setPaletteLabel((AbstractString)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * @see org.eclipse.ve.internal.cde.palette.impl.ContainerImpl#createPaletteContainer()
	 */
	protected PaletteContainer createPaletteContainer() {
		return new PaletteRoot();
	}

	/**
	 * @see org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry#getEntry()
	 */
	public PaletteEntry getEntry() {
		PaletteRoot root = (PaletteRoot) super.getEntry();
		root.setDefaultEntry(findDefaultToolEntry(root));
		return root;
	}
	
	protected ToolEntry findDefaultToolEntry(PaletteContainer r) {
		List c = r.getChildren();
		for (int i=0; i<c.size(); i++) {
			PaletteEntry e = (PaletteEntry) c.get(i);
			if (e instanceof PaletteContainer) {
				ToolEntry te = findDefaultToolEntry((PaletteContainer) e);
				if (te != null)
					return te;
			} else if (e instanceof ICDEToolEntry)
				if (((ICDEToolEntry) e).isDefaultEntry())
					return (ToolEntry) e;
		}
		return null;
	}

}
