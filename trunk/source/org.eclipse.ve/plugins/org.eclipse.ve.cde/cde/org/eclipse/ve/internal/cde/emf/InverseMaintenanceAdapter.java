package org.eclipse.ve.internal.cde.emf;
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
 *  $RCSfile: InverseMaintenanceAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import java.lang.ref.WeakReference;
import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.CDMPackage;

/**
 * This is used to maintain anonymous shared inverse relationships. For instance,
 * maintain back-pointers to all EObjects (and the appropriate feature) that
 * point to this EObject. This is a self-propagating adapter, starting with
 * top EObject it will propagate itself down to referenced EObjects. It will not
 * propagate to AnnotationEMF objects (since that object maintains its own back-pointer).
 * It will propagate itself to owned EObjects, but won't insert itself into the back list
 * since that is not a shared relationship.
 * 
 * Add ResourceInverseMaintenanceAdapter to a Resource when you want all EObjects in the
 * resource to have an inverse maintained on them. If you add it to an EObject only, then
 * it will be propagated down to containment references only. An adapter will be added to
 * shared references following the algorithm below, but such adapters won't propagate down
 * to its references until it gets added to a containment reference of an EObject that has been
 * propagated (or until that EObject gets added to one that has and so on up the chain).
 * After adding it to an EObject you will need to call propagate() method, otherwise it won't 
 * know to propagate down. This is not necessary when adding ResourceInverseMaintenanceAdapter. 
 * It will cause a propagation to occur immediately. 
 *  
 * NOTE: Propagation will not be allowed until after the object has been contained within a resource.
 * This allows the cross doc control to work correctly. So do not call propagate until the object is in
 * a resource.
 *  
 * 
 * Propagation and cross reference works in this manner (source is the inverse adapter/eobject that 
 * the setting is being made to, target is the inverse adapter/eobject that is the setting). This
 * algorithm runs through the inverse adapter of the source. If the source doesn't have an inverse adapter
 * then nothing happens since it is the source that detects the setting occurring, not the target:
 * 
 * (A) ref is a shared reference, source has propagated and shouldReference is true:
 *     (if source has not yet propagated then nothing happens to target because
 *      this will be handled at the time the source is propagated):
 *   1) If target has an inverse adapter, the back ref will simply be added to it.
 *   2) If target does not have an inverse adapter and shouldAddAdapter is true:
 *      a) source and target are in different resources and allowCrossDoc is false then inverse adapter not created
 *      b) source and target are in different resources and allowCrossDoc is true, then inverse adapter is created
 *         and a back ref added.
 *      c) target is not in a resource, then inverse adapter is created and a back ref added. 
 *         (This is because we don't know at this point whether ir will be in a different resource or not, and we don't
 *          want to loose this reference when it is finally in a resource).
 *      d) source and target are in same resource, then inverse adapter is created and a back ref added. 
 * 
 * (B) ref is a containment reference (which means target is contained by source), source has propagated:
 *     (if source has not yet propagated then nothing happens to target because
 *      this will be handled at the time the source is propagated):
 *   1) If target has an inverse adapter and not yet propagated, it will be propagated.
 *   2) If target does not have an inverse adapter and shouldPropagate is true, one will be created and it will be propagated.
 * 
 * (C) Propagate means adapter will mark itself as propagated and then run through all of its references and
 *     process them as in (A) and (B) appropriately.
 * 
 * The above algorithm may cause some unexpected actions. Propagation is only done when added to a source
 * that has propagated, or when that source is propagated. So there may be an inverse adapter on an object (target) that
 * has not propagated to it settings. So it will show only back refs to those sources that have propagated. It would
 * stick out like a leaf even though it may have settings. None of its settings will have back refs to it. You also can
 * still get a cross doc reference if the target was not contained by a resource at the time it was pointed to by 
 * a source that had been propagated. So it is best not to point to something until that something has been contained if
 * the source has been propagated and contained.  
 */
public class InverseMaintenanceAdapter extends AdapterImpl {
	public static final Class ADAPTER_KEY = InverseMaintenanceAdapter.class;

	// Pointer to backrefs. Key==Back_Feature, Value=List(Weak(Back_EObjects)) or Weak(Back_EObject) if only one.
	// Using WeakReference for back objects so that if the back object is not held onto by anybody, the reference
	// is really gone and we shouldn't hold onto the object itself.
	protected HashMap backRefs;
	
	protected boolean allowCrossDoc;	// Are we allowing cross-document traversal.
	private boolean propagated;	// Have we propagated?

	/**
	 * Constructor for InverseMaintenanceAdapter.
	 * This ctor doesn't allow cross-doc references.
	 */
	public InverseMaintenanceAdapter() {
		this(false);
	}
	
	/**
	 * This adapter, and adapters it propagates down, will not cross over
	 * into another document if true. This means the adapters will not
	 * add itself (if one is not there) to a reference to an object in
	 * another document.
	 * 
	 * @param allowCrossDoc If true, then allows cross doc references.
	 */
	public InverseMaintenanceAdapter(boolean allowCrossDoc) {
		super();
		this.allowCrossDoc = allowCrossDoc;
	}

	/**
	 * Static helper to get InverseMaintenanceAdapter and
	 * return the list of back objects referenced by a feature for the object passed in.
	 * 
	 * @param object The object to find inverse against.
	 * @param feature The feature looked for.
	 * @return List of objects referencing this object through the feature.
	 */
	public static EObject[] getReferencedBy(Notifier object, EReference feature) {
		InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(object, InverseMaintenanceAdapter.ADAPTER_KEY);
		return ai != null ? ai.getReferencedBy(feature) : EMPTY_EOBJECTS;
	}

	/**
	 * Static helper to get InverseMaintenanceAdapter and
	 * to get just the first back reference for a feature for the object passed in.
	 * 
	 * @param object The object to find inverse against.
	 * @param feature The feature looked for.
	 * @return First object referencing this object through the feature. null is none are.
	 */
	public static EObject getFirstReferencedBy(Notifier object, EReference feature) {
		InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(object, InverseMaintenanceAdapter.ADAPTER_KEY);
		return ai != null ? ai.getFirstReferencedBy(feature) : null;
	}
	
	/**
	 * Static helper to get intermediate reference. There is often an
	 * intermediate object that we want to get. For example, source-(ref1)->intermediate-(ref2)->target.
	 * We want to get the intermediate object that points to target using ref2, but in addition, we want
	 * the intermediate object that is pointed to by source using ref1 (which may be a containment feature).
	 * This is because there could be more than one intermediate object that refers to target through ref2. 
	 * This narrows it down to the one of interest.
	 * 
	 * @param source The source object.
	 * @param ref1 The reference from source to intermediate (this ref may be a containment ref).
	 * @param ref2 The reference from intermediate to target (this ref must be a shared ref).
	 * @param target The object to back track from.
	 * 
	 * @return The intermediate object. null if no intermediate is found.
	 */
	public static EObject getIntermediateReference(EObject source, EReference ref1, EReference ref2, Notifier target) {
		Object ref1s = null;
		// If ref1 is containment, it is quicker to check eContainmentFeature for the searched for then to do a contains against
		// all of the ref1 settings from source.
		if (!ref1.isContainment()) 
			ref1s = source.eGet(ref1);
		EObject[] backRefs = getReferencedBy(target, ref2);
		for (int i = 0; i < backRefs.length; i++) {
			EObject i1 = backRefs[i];
			if (ref1.isContainment()) {
				if (i1.eContainmentFeature() == ref1)
					return i1;
			} else if (ref1s instanceof List) {
				if (((List) ref1s).contains(i1))
					return i1;
			} else if (i1 == ref1s)
				return i1;
		}
		
		return null;
	}
	
	/**
	 * Static helper to return the list of anonymous features that the given source refers to this target by.
	 * 
	 * @param source The source that refers to this object.
	 * @param target The target to look for the backrefs against.
	 * 
	 * @return The list of features that refer to this target from the source object.
	 */
	public static EReference[] getReferencesFrom(EObject source, Notifier target) {
		InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(target, InverseMaintenanceAdapter.ADAPTER_KEY);
		if (ai != null) {
			if (ai.backRefs != null && !ai.backRefs.isEmpty()) {
				ArrayList sourceRefs = new ArrayList();
				Iterator itr = ai.backRefs.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry entry = (Map.Entry) itr.next();
					if (entry.getValue() instanceof List) {
						List l = (List) entry.getValue();
						for (int i=0; i<l.size(); i++) {
							WeakReference wr = (WeakReference) l.get(i);
							if (wr != null && wr.get() == source) {
								sourceRefs.add(entry.getKey());
								break;
							}
						}
					} else if (entry.getValue() != null && ((WeakReference) entry.getValue()).get() == source)
						sourceRefs.add(entry.getKey());
				}
				if (!sourceRefs.isEmpty())
					return (EReference[]) sourceRefs.toArray(new EReference[sourceRefs.size()]);
			}
		} 
		
		return EMPTY_FEATURES;
	}

	private static final EObject[] EMPTY_EOBJECTS = new EObject[0];
	
	/**
	 * Return the list of back objects referenced by a feature.
	 * 
	 * @param feature The feature looked for.
	 * @return List of objects referencing this object through the feature.
	 */
	public EObject[] getReferencedBy(EReference feature) {
		if (backRefs == null)
			return EMPTY_EOBJECTS;
		else {
			Object refs = backRefs.get(feature);
			if (refs == null)
				return EMPTY_EOBJECTS;
			else if (refs instanceof WeakReference)
				return ((WeakReference) refs).get() != null ? new EObject[] { (EObject) ((WeakReference) refs).get() } : EMPTY_EOBJECTS;
			else if (((List) refs).isEmpty())
				return EMPTY_EOBJECTS;
			else {
				List l = (List) refs;
				ArrayList tList = new ArrayList(l.size());
				Iterator itr = l.iterator();
				while (itr.hasNext()) {
					WeakReference wr = (WeakReference) itr.next();
					if (wr.get() != null)
						tList.add(wr.get());
					else
						itr.remove();	// Get it out of the list
				}
				return !tList.isEmpty() ? (EObject[]) tList.toArray(new EObject[tList.size()]) : EMPTY_EOBJECTS;
			}
		}
	}
	
	/**
	 * A simple helper to get just the first back reference for a feature.
	 * 
	 * @param feature The feature looked for.
	 * @return First object referencing this object through the feature. null is none are.
	 */
	public EObject getFirstReferencedBy(EReference feature) {
		EObject[] eos = getReferencedBy(feature);
		return eos.length > 0 ? eos[0] : null;
	}
	
	private static final EReference[] EMPTY_FEATURES = new EReference[0];
	/**
	 * Return the set features.
	 * 
	 * @return The features that are referencing this object.
	 */
	public EReference[] getFeatures() {
		if (backRefs == null || backRefs.isEmpty())
			return EMPTY_FEATURES;
		else
			return (EReference[]) backRefs.keySet().toArray(new EReference[backRefs.size()]);
	}
		
	/**
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(Object)
	 */
	public boolean isAdapterForType(Object type) {
		return ADAPTER_KEY == type;
	}

	protected static final EClass ANNOTATIONCLASS = CDMPackage.eINSTANCE.getAnnotationEMF();
	
	/**
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
	 */
	public void notifyChanged(Notification notification) {
		if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
			// Going away. Clear out backRefs.
			backRefs = null;
			setPropagated(false);
			return;
		}
		// Don't bother if not propagated, or not a stnd event type, feature not an EReference, or feature type is not set or is an Annotation.
		if (!isPropagated() || notification.getEventType() < 0 || notification.getEventType() > Notification.EVENT_TYPE_COUNT ||
			!(notification.getFeature() instanceof EReference) || ((EReference) notification.getFeature()).getEReferenceType() == null || ((EReference) notification.getFeature()).getEReferenceType().isSuperTypeOf(ANNOTATIONCLASS))
			return;
		switch (notification.getEventType()) {
			case Notification.SET:
				if (!notification.isTouch()) {
					handleRemoveRef(notification.getFeature(), notification.getOldValue());
					if (!notification.isReset() || ((EReference) notification.getFeature()).isUnsettable())
						handleAddRef(notification.getFeature(), notification.getNewValue());
				}
				break;
			case Notification.UNSET:
				handleRemoveRef(notification.getFeature(), notification.getOldValue());			
				break;
			case Notification.ADD:
				handleAddRef(notification.getFeature(), notification.getNewValue());			
				break;
			case Notification.ADD_MANY:
				Object feature = notification.getFeature();
				Iterator itr = ((List) notification.getNewValue()).iterator();
				while (itr.hasNext())
					handleAddRef(feature, itr.next());
				break;
			case Notification.REMOVE:
				handleRemoveRef(notification.getFeature(), notification.getOldValue());			
				break;			
			case Notification.REMOVE_MANY:
				feature = notification.getFeature();
				itr = ((List) notification.getOldValue()).iterator();
				while (itr.hasNext())
					handleRemoveRef(feature, itr.next());
				break;			
			case Notification.RESOLVE:
				// Special case, we had a proxy and it is no longer a proxy, so should we add it.
				handleAddRef(notification.getFeature(), notification.getNewValue());
				break;
		}
	}
	
	protected void handleAddRef(Object feature, Object newValue) {
		// EAttributes don't need this because they do not reference EObjects.
		if (newValue instanceof Notifier) {
			EReference ref = (EReference) feature;
			Notifier target = (Notifier) newValue;
			// Any references to annotation should be ignored.
			InverseMaintenanceAdapter inverseAdapter = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(target, ADAPTER_KEY);											
			if (ref.isContainment()) {
				if (inverseAdapter == null && shouldPropagate(ref, target)) {
					inverseAdapter = addAdapter(target);	// add new adapter.
					if (inverseAdapter != null)	// It got added.
						inverseAdapter.primPropagate();	// Propagate (prim because we know it is not propagated and is in same resource).
				} else if (inverseAdapter != null && !inverseAdapter.isPropagated())
					inverseAdapter.primPropagate();	// Propagate (prim because we know it is not propagated and is in same resource). 
			} else if (shouldReference(ref, newValue)) {
				if (inverseAdapter == null)
					inverseAdapter = addAdapter(target);
				if (inverseAdapter != null)
					inverseAdapter.addBackRef(ref, getTarget());	// Could be propagated or already existed.
			}
		}
	}

	/*
	 * shouldPropagate: See if this feature and value requires us to propagate the
	 * adapter onto it if this is a containment relationship. Default is
	 * to allow propagation for all but annotations and proxies. Subclasses should override subShouldPropagate.
	 */
	protected final boolean shouldPropagate(EReference ref, Object newValue) {
		return (!(newValue instanceof EObject) || !((EObject) newValue).eIsProxy()) && subShouldPropagate(ref, newValue);
	}
	
	protected boolean subShouldPropagate(EReference ref, Object newValue) {
		return true;
	}
	
	/*
	 * Add to new value.
	 */
	protected final InverseMaintenanceAdapter addAdapter(Notifier newValue) {
		if (!allowCrossDoc && newValue instanceof EObject) {
			// If new value is in a resource, then if they are different resources don't do the propagate.
			// However if new value side is not in a resource, we can't tell, so we will allow in that case.
			// We know we (target) is in a resource because this method only called when propagated (and propagated
			// is only done when in a resource).
			Resource newRes = ((EObject) newValue).eResource();
			if (newRes != null && ((EObject) getTarget()).eResource() != newRes)
				return null;	// Don't allow it to cross.
		}
			
		InverseMaintenanceAdapter inverseAdapter = createInverseAdapter();
		newValue.eAdapters().add(inverseAdapter);
		return inverseAdapter;
	}
	
	/*
	 * Override to create a different type of InverseMaintenanceAdapter
	 */
	protected InverseMaintenanceAdapter createInverseAdapter() {
		return new InverseMaintenanceAdapter(allowCrossDoc);
	}
	
	/*
	 * Should this reference be set in the the inverse adapter. Default is to allow
	 * setting inverse for all but annotations and proxies, or references where the
	 * opposite end is a containment. Subclasses should override subShouldReference.
	 */
	protected final boolean shouldReference(EReference ref, Object newValue) {
		return (ref.getEOpposite() == null || !ref.getEOpposite().isContainment()) && (!(newValue instanceof EObject) || !((EObject) newValue).eIsProxy()) && subShouldReference(ref, newValue);		
	}
	
	protected boolean subShouldReference(EReference ref, Object newValue) {	
		return true;
	}
	
	protected void handleRemoveRef(Object feature, Object oldValue) {
		if (oldValue instanceof Notifier) {
			InverseMaintenanceAdapter inverseAdapter = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter((Notifier) oldValue, ADAPTER_KEY);
			if (inverseAdapter != null)
				inverseAdapter.removeBackRef((EReference) feature, getTarget());
		}
	}

	/*
	 * Called by Parent adapter to remove itself from the back pointer list. 
	 */
	protected void removeBackRef(EReference feature, Notifier backObject) {
		if (backRefs != null) {
			Object refs = backRefs.get(feature);
			if (refs instanceof WeakReference) { 
				if (((WeakReference) refs).get() == backObject)
					backRefs.remove(feature);	// Since it was the only object for the feature.
			} else if (refs instanceof List) {
				List l = (List) refs;
				int s = l.size();
				for (int i = 0; i < s; i++) {
					WeakReference wr = (WeakReference) l.get(i);
					if (wr.get() == backObject) {
						l.remove(i);
						break;
					}
				}
			}
		}			
	}
	
	/*
	 * Called by Parent adapter to add itself to the back pointer list. 
	 */
	protected void addBackRef(EReference feature, Notifier backObject) {
		if (backRefs == null)
			backRefs = new HashMap();
			
		Object refs = backRefs.get(feature);
		if (refs == null)
			backRefs.put(feature, new WeakReference(backObject));
		else if (refs instanceof List)
			((List) refs).add(new WeakReference(backObject));
		else {
			ArrayList list = new ArrayList(2);
			if (((WeakReference) refs).get() != null)
				list.add(refs);	// Still valid
			list.add(new WeakReference(backObject));
			backRefs.put(feature, list);
		}			
	}	

	public final void setPropagated(boolean propagated) {
		this.propagated = propagated;
	}

	public final boolean isPropagated() {
		return propagated;
	}
	
	/**
	 * Propagate to settings if not already propagated and not contained in a resource.
	 */
	public final void propagate() {
		if (isPropagated())
			return;
		if (getTarget() instanceof EObject && ((EObject) getTarget()).eResource() != null)
			primPropagate();
	}
	
	/*
	 * Propagate to settings irregardless of contain in resource.
	 * It is protected and shouldn't be called by any subclass but this class
	 * itself only. It is protected to allow overrides to the method. Overrides
	 * should call super.primPropagate() first.
	 */
	protected void primPropagate() {
		if (isPropagated())
			return;

		setPropagated(true);
		
		// Need to setup all of the backpointers.
		EObject obj = (EObject) getTarget();
		List allRefs = obj.eClass().getEAllReferences();
		Iterator itr = allRefs.iterator();
		while (itr.hasNext()) {
			EReference ref = (EReference) itr.next();
			if (ref.getEReferenceType().isSuperTypeOf(ANNOTATIONCLASS))
				continue;
			if (obj.eIsSet(ref)) {
				if (ref.isMany()) {
					Iterator bitr =
						((InternalEList) obj.eGet(ref)).basicIterator();
					while (bitr.hasNext()) {
						handleAddRef(ref, bitr.next());
					}
				} else
					handleAddRef(ref, obj.eGet(ref, false));
			}
		}		
	}
	
}
