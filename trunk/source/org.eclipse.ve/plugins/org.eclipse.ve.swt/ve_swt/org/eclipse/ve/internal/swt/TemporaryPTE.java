/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TemporaryPTE.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-27 01:12:11 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject.EStore;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Internal;

import org.eclipse.jem.internal.instantiation.PTExpression;
import org.eclipse.jem.internal.instantiation.ParseVisitor;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
 
/**
 * 
 * This is a temporary hack, until we have and EMF reference support with JEM
 * deprecate it as a warning
 * @deprecated
 * @author Gili Mendel
 * @since 1.0.0
 */
public class TemporaryPTE implements InternalEObject,  PTExpression {

	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	
	IJavaObjectInstance parent = null ;
	String fFlags = "org.eclipse.swt.SWT.None" ; 
	
	EList fList = new BasicEList() ;
	
	public TemporaryPTE() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.instantiation.PTExpression#accept(org.eclipse.jem.internal.instantiation.ParseVisitor)
	 */
	public void accept(ParseVisitor visitor) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eClass()
	 */
	public EClass eClass() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eResource()
	 */
	public Resource eResource() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eContainer()
	 */
	public EObject eContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
	 */
	public EStructuralFeature eContainingFeature() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
	 */
	public EReference eContainmentFeature() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eContents()
	 */
	public EList eContents() {
		return fList ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eAllContents()
	 */
	public TreeIterator eAllContents() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eIsProxy()
	 */
	public boolean eIsProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
	 */
	public EList eCrossReferences() {
		// TODO Auto-generated method stub
		return fList ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public Object eGet(EStructuralFeature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
	 */
	public Object eGet(EStructuralFeature feature, boolean resolve) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public void eSet(EStructuralFeature feature, Object newValue) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public boolean eIsSet(EStructuralFeature feature) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void eUnset(EStructuralFeature feature) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
	 */
	public EList eAdapters() {
		return fList ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
	 */
	public boolean eDeliver() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
	 */
	public void eSetDeliver(boolean deliver) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
	 */
	public void eNotify(Notification notification) {
		// TODO Auto-generated method stub

	}
	
	public String getFlags() {
		return fFlags ;
	}
	
	public void setFlags(String flags) {
		fFlags = flags ;
	}

	/**
	 * @return Returns the parent.
	 */
	public IJavaObjectInstance getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(IJavaObjectInstance parent) {
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eBaseStructuralFeatureID(int, java.lang.Class)
	 */
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eBasicRemoveFromContainer(org.eclipse.emf.common.notify.NotificationChain)
	 */
	public NotificationChain eBasicRemoveFromContainer(NotificationChain notifications) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eBasicSetContainer(org.eclipse.emf.ecore.InternalEObject, int, org.eclipse.emf.common.notify.NotificationChain)
	 */
	public NotificationChain eBasicSetContainer(InternalEObject newContainer, int newContainerFeatureID, NotificationChain notifications) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eContainerFeatureID()
	 */
	public int eContainerFeatureID() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eDerivedStructuralFeatureID(int, java.lang.Class)
	 */
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class baseClass) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eInternalResource()
	 */
	public Internal eInternalResource() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eInverseAdd(org.eclipse.emf.ecore.InternalEObject, int, java.lang.Class, org.eclipse.emf.common.notify.NotificationChain)
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain notifications) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eInverseRemove(org.eclipse.emf.ecore.InternalEObject, int, java.lang.Class, org.eclipse.emf.common.notify.NotificationChain)
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain notifications) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eNotificationRequired()
	 */
	public boolean eNotificationRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eObjectForURIFragmentSegment(java.lang.String)
	 */
	public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eProxyURI()
	 */
	public URI eProxyURI() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eResolveProxy(org.eclipse.emf.ecore.InternalEObject)
	 */
	public EObject eResolveProxy(InternalEObject proxy) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eSetClass(org.eclipse.emf.ecore.EClass)
	 */
	public void eSetClass(EClass eClass) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eSetProxyURI(org.eclipse.emf.common.util.URI)
	 */
	public void eSetProxyURI(URI uri) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eSetResource(org.eclipse.emf.ecore.resource.Resource.Internal, org.eclipse.emf.common.notify.NotificationChain)
	 */
	public NotificationChain eSetResource(Internal resource, NotificationChain notifications) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eSetStore(org.eclipse.emf.ecore.InternalEObject.EStore)
	 */
	public void eSetStore(EStore store) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eSetting(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public Setting eSetting(EStructuralFeature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eStore()
	 */
	public EStore eStore() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eURIFragmentSegment(org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.emf.ecore.EObject)
	 */
	public String eURIFragmentSegment(EStructuralFeature eFeature, EObject eObject) {
		// TODO Auto-generated method stub
		return null;
	}

}
