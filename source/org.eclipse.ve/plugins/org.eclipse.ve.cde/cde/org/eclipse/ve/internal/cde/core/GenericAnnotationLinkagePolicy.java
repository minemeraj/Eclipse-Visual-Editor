package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: GenericAnnotationLinkagePolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 19:27:20 $ 
 */

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cdm.*;
/**
 * This is the base of any Generic Model annotation linkage helper.
 * This is used for any models that are not EMF based.
 *
 * Since it not EMF based, we need a notification mechanism so that
 * anyone listening on a model object will know if annotation has been
 * added or removed from it. This is necessary because annotations can be
 * added at any time (for instance if an object is not annotated, but is moved
 * on the freeform, we now need an annotation, and so it will be added). This
 * notification mechanism is provided here.
 *
 * The listener mechanism is not provided for EMF based models because EMF already
 * has its own notification mechanism. The mechanism is based upon ID. The listener listens
 * on ID having an annotation or not. This is because the model object can be anything,
 * it may not even be a object that has identity. There could be several instances all
 * referring to the same ID and represent the same "model object".
 *
 * Note: However, there still is a concept of a model object. The editparts require it
 * and for our generic editpolicies we use the model object. So subclasses need to
 * able to decode a model object into an ID.
 */
public abstract class GenericAnnotationLinkagePolicy extends AnnotationLinkagePolicy {
	
	/**
	 * Interface for AnnotationLinkageListener
	 */
	public interface AnnotationLinkageListener extends EventListener {
		// The linkage has changed. The event gives the info.
		public void annotationLinkageChanged(AnnotationLinkageEvent event);
	}
	
	/**
	 * The event for linkage listeners.
	 * It is the base class for the events, and it is the event for REMOVING_LISTENER.
	 * Note: The "source" in the event is the String ID.
	 */
	public static class AnnotationLinkageEvent extends EventObject {
		/**
		 * Comment for <code>serialVersionUID</code>
		 * 
		 * @since 1.1.0
		 */
		private static final long serialVersionUID = 7151443223479264022L;

		public static final int 
			SET = 0,	// Annotation set, old value/new value will be set.
			UNSET = 1,	// Annotation unset, old value will be old value
			TOUCH = 2,	// Annotation touched, old/new will be the same, the current value.
			SET_ID = 3,	// The ID has been changed, same annotation and listeners.
			REMOVING_LISTENER = 4;
			
		protected int type;
		
		public AnnotationLinkageEvent(Object id, int type) {
			super(id);
			this.type = type;
		}
		
		public int getType() {
			return type;
		}
	}
	
	/**
	 * The event for annotation object changes.
	 * It is for event SET, UNSET, TOUCH.
	 */
	public static class AnnotationLinkageChangeEvent extends AnnotationLinkageEvent {

		/**
		 * Comment for <code>serialVersionUID</code>
		 * 
		 * @since 1.1.0
		 */
		private static final long serialVersionUID = -149455833875107330L;
		protected Annotation oldAnnotation, newAnnotation;
		
		public AnnotationLinkageChangeEvent(String id, int type, Annotation oldAnnotation, Annotation newAnnotation) {
			super(id, type);
			this.oldAnnotation = oldAnnotation;
			this.newAnnotation = newAnnotation;
		}
		
		
		public Annotation getOldAnnotation() {
			return oldAnnotation;
		}
		
		public Annotation getNewAnnotation() {
			return newAnnotation;
		}
	}
	
	/**
	 * The event of SET_ID changed. This is when the ID of the model object has been changed.
	 * It means it's the same model object and same annotation, but it has a new ID.
	 */
	public static class AnnotationIDChangeEvent extends AnnotationLinkageEvent {
		
		/**
		 * Comment for <code>serialVersionUID</code>
		 * 
		 * @since 1.1.0
		 */
		private static final long serialVersionUID = -1730398443677274046L;
		protected String oldID;
		
		public AnnotationIDChangeEvent(String id, String oldID) {
			super(id, SET_ID);
			
			this.oldID = oldID;
		}
		
		public String getOldID() {
			return oldID;
		}
	}
	
	// Much of this is private and some final methods because the handling of the hashmap and its entries
	// are very sensitive and we must make sure that it is not touched. Otherwise there could be 
	// leakages of data not being GCed.
	
	// ID (key) to AnnotationMapEntry (value) map.
	// Used to cache lookups from ID to annotation.
	// The AnnotationMapEntry has a WeakReference to the Annotation, 
	// so that when the annotation is gone and garbage collected, 
	// then the entry can go away if there are no listeners.
	// There is a ReferenceQueue that listens for the annotation weakRefs being
	// GCed so that the entry can then be removed, or notification sent of
	// the annotation is gone.
	// The value will be AnnotationMapEntry.
	private HashMap modelToAnnotationMap;	
	
	// The weak reference to the annotation. We need the id
	// so that when processing the queue we know what entry
	// this was from, since the annotation will be gone.	
	private static class AnnotationWeakReference extends WeakReference {
		public String id;
		
		public AnnotationWeakReference(String id, Annotation annotation, ReferenceQueue queue) {
			super(annotation, queue);
			this.id = id;
		}
	}
	
	// The entry in the map. It contains the ref to the annotation and any listeners.
	private static class AnnotationMapEntry {
		public AnnotationWeakReference annotation;
		public AnnotationLinkageListener[] listeners;
		public Object data;	// Auxiliary data for use by subclasses
		
		public AnnotationMapEntry() {
		}
		
		public AnnotationMapEntry(String id, Annotation annotation, ReferenceQueue queue) {
			if (annotation != null)
				this.annotation = new AnnotationWeakReference(id, annotation, queue);
		}

		public void setAnnotation(String id, Annotation annotation, ReferenceQueue queue) {
			this.annotation = new AnnotationWeakReference(id, annotation, queue);
		}
			
		public Annotation getAnnotation() {
			return annotation != null ? (Annotation) annotation.get() : null;
		}
	}	
	
	// Queue for listening to GC of annotations.
	private ReferenceQueue queue = new ReferenceQueue();

	// Process any queued up annotation GCs.
	private void processQueue() {
		if (modelToAnnotationMap == null || inDispose)
			return;
		Object wk;
		while ((wk = queue.poll()) != null) {
			removeAnnotationFromMap(((AnnotationWeakReference) wk).id);
		}
	}

	// Get the map, create it if it doesn't exist.	
	private HashMap getModelToAnnotationMap() {
		processQueue();
		if (modelToAnnotationMap == null)
			modelToAnnotationMap = new HashMap();
		return modelToAnnotationMap;
	}
	
	// Get the map entry for the given id.
	private AnnotationMapEntry getAnnotationEntryFromMap(String id) {
		return (AnnotationMapEntry) getModelToAnnotationMap().get(id);
	}
	
	protected final Annotation getAnnotationFromGeneric(Object model) {
		String id = getIDFromModel(model);
		if (id == null)
			return null;	// The ID doesn't exist in the model.
			
		AnnotationMapEntry e = getAnnotationEntryFromMap(id);
		return e != null ? e.getAnnotation() : null;
	}
	
	// The annotation has had an id set.
	private void setAnnotationFromMap(String id, Annotation annotation) {
		if (id == null)
			return;
		AnnotationMapEntry e = getAnnotationEntryFromMap(id);
		AnnotationLinkageEvent evt = null;
		if (e == null) {
			addToMap(id, new AnnotationMapEntry(id, annotation, queue));
			// Nobody is listening, so no event is needed to be created.
		} else {
			Annotation old = e.getAnnotation();
			e.setAnnotation(id, annotation, queue);
			if (e.listeners != null && e.listeners.length > 0)
				evt = new AnnotationLinkageChangeEvent(id, old != annotation ? AnnotationLinkageEvent.SET : AnnotationLinkageEvent.TOUCH, old, annotation);
		}
		if (evt != null)
			fireAnnotationLinkage(e, evt);
	}
	
	private void removeAnnotationFromMap(String id) {
		if (id == null)
			return;
		AnnotationMapEntry e = getAnnotationEntryFromMap(id);
		if (e != null) {
			if (e.listeners != null && e.listeners.length > 0) {
				// Somebody is listening
				Annotation old = e.getAnnotation();	// If this is from a GC, then annotation will be null.
				e.annotation = null;
				fireAnnotationLinkage(e, new AnnotationLinkageChangeEvent(id, AnnotationLinkageEvent.UNSET, old, null));
			} else {
				// Nobody is listening, so just remove the entry.
				removeFromMap(id);
			}
		}
	}
	
	private void fireAnnotationLinkage(AnnotationMapEntry entry, AnnotationLinkageEvent event) {
		AnnotationLinkageListener[] listeners = entry.listeners;
		if (listeners != null) 
			for (int i=0; i<listeners.length; i++)
				listeners[i].annotationLinkageChanged(event);
	}
	
	/**
	 * Change an ID from one to another. This means the same object now has a new ID.
	 * This means that the annotation will be the same, just have a new ID, and anyone
	 * listening on the ID will now be listening to the new ID instead.
	 * The new ID must not exist. The IllegalArgumentException will be thrown
	 * if the new ID already exists. If the old one is not being listened to and doesn't
	 * have an annotation, nothing is done because nobody cares. If the old one doesn't
	 * exist then nothing is done because there is no entry to change to. If the new id is
	 * null, then an IllegalArgumentException is thrown because we don't know what to do with it.
	 */
	public void changeID(String fromID, String toID) {
		AnnotationMapEntry e = getAnnotationEntryFromMap(fromID);
		if (e == null)
			return;	// There is none registered to change.
			
		if (toID == null)
			throw new IllegalArgumentException("ID must not be null."); //$NON-NLS-1$
			
		if (getAnnotationEntryFromMap(toID) != null)
			throw new IllegalArgumentException("ID already is registered:"+toID); //$NON-NLS-1$
			
		// Change the id in the annotation if any, but don't have the annotation notify. It is still
		// really pointing to the same "model object".
			
		AnnotationGeneric ann = (AnnotationGeneric) e.getAnnotation();
		if (ann != null) {
			boolean was = ann.eDeliver();
			ann.eSetDeliver(false);
			try {
				ann.setAnnotatesID(toID);
			} finally {
				ann.eSetDeliver(was);
			}
		}
		
		// Now update the map. Need to remove the old and put it back under the new.
		getModelToAnnotationMap().remove(fromID);	// NOTE: This doesn't signal unlinkingFromID
		if (e.annotation != null) {
			// There is a weak reference, need to change the id in it.
			e.annotation.id = toID;
		}
		// Now put the update entry back out, NOTE: This doesn't signal linkingToID.
		getModelToAnnotationMap().put(toID, e);
		
		// Now let the listeners know it has changed.
		fireAnnotationLinkage(e, new AnnotationIDChangeEvent(fromID, toID));
	}
	
	private void addToMap(String id, AnnotationMapEntry e) {
		getModelToAnnotationMap().put(id, e);
		e.data = linkingToID(id);
	}
	
	private void removeFromMap(String id) {
		AnnotationMapEntry e = (AnnotationMapEntry) getModelToAnnotationMap().remove(id);
		if (e != null)
			unlinkingFromID(id, e.data);
	}
	
	protected boolean inDispose = false;
	public void dispose() {
		inDispose = true;
		// Need to release all listeners and IDs so that this can go away.
		Iterator entries = getModelToAnnotationMap().entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			// Signal removing listeners.
			String id = (String) entry.getKey();
			AnnotationMapEntry e = (AnnotationMapEntry) entry.getValue();
			if (e.listeners != null && e.listeners.length > 0) {
				for (int i=0; i<e.listeners.length; i++) {
					try {
						e.listeners[i].annotationLinkageChanged(new AnnotationLinkageEvent(id, AnnotationLinkageEvent.REMOVING_LISTENER));
					} catch (Exception exp) {
						// Don't want anything to stop it, want all to get a chance.
					}
				}
			}
			
			try {
				// Signal unlinking id
				unlinkingFromID(id, e.data);
			} catch (Exception exp) {
				// Don't want anything to stop it, want all to get a chance.
			}

			// Now unlink from Annotation.
			Annotation ann = e.getAnnotation();
			if (ann != null) {
				AnnotationAdapter aa = (AnnotationAdapter) EcoreUtil.getExistingAdapter(ann, AnnotationAdapter.class);
				if (aa != null)
					ann.eAdapters().remove(aa);
			}
		}
		
		getModelToAnnotationMap().clear();
		
		// Now remove DiagramData adapters.
		// We go from the end because the list is modified by the removeAdapter
		// so this way we won't miss any.
		for (int i = ddAdapter.targets.size()-1; i >= 0; i--) {
			DiagramData dd = (DiagramData) ddAdapter.targets.get(i);
			dd.eAdapters().remove(ddAdapter);
		}
		inDispose = false;
	}
			
	
	/**
	 * Add a listener for the id for any changes to annotation linkage
	 */
	public void addAnnotationLinkageListener(String id, AnnotationLinkageListener listener) {
		AnnotationMapEntry e = getAnnotationEntryFromMap(id);
		if (e == null) {
			// Need to create a holder to hold the listeners.
			e = new AnnotationMapEntry();
			addToMap(id, e);
		}
		if (e.listeners == null) {
			e.listeners = new AnnotationLinkageListener[] {listener};
		} else {
			AnnotationLinkageListener[] listeners = e.listeners;			
			e.listeners = new AnnotationLinkageListener[e.listeners.length+1];
			System.arraycopy(listeners, 0, e.listeners, 0, listeners.length);
			e.listeners[e.listeners.length-1] = listener;
		}
	}
	
	
	/**
	 * Remove a listener for the id for any changes to annotation linkage.
	 */
	public void removeAnnotationLinkageListener(String id, AnnotationLinkageListener listener) {
		if (inDispose)
			return;
		AnnotationMapEntry e = getAnnotationEntryFromMap(id);
		if (e != null) {
			if (e.listeners != null && e.listeners.length > 0) {
				int loc = -1;
				for (int i=0; i<e.listeners.length; i++) {
					if (e.listeners[i] == listener) {
						loc = i;
						break;
					}
				}
				if (loc != -1) {
					if (e.listeners.length == 1 && e.getAnnotation() == null) {
						removeFromMap(id);	// No listeners after removing this one and no annotation, get rid of the entry.
					} else {
						// We have either an annotatin, or there are more listeners, so compress the list.
						AnnotationLinkageListener[] listeners = e.listeners;
						e.listeners = new AnnotationLinkageListener[listeners.length-1];
						System.arraycopy(listeners, 0, e.listeners, 0, loc);
						if (loc < e.listeners.length-1)
							System.arraycopy(listeners, loc+1, e.listeners, loc, e.listeners.length-loc);
					}
					listener.annotationLinkageChanged(new AnnotationLinkageEvent(id, AnnotationLinkageEvent.REMOVING_LISTENER));
				}
			}
		}
	}

	// This adapter handles the changes to ID and notifies and registers as appropriate.		
	private class AnnotationAdapter extends AdapterImpl {
						
		public boolean isAdapterForType(Object type) {
			return type == AnnotationAdapter.class;
		}
		
		public void notifyChanged(Notification msg) {
			if (inDispose)
				return;
			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				// Adapter being removed, remove the association if any.
				removeAnnotationFromMap(((AnnotationGeneric) getTarget()).getAnnotatesID());
			} else if (msg.getFeature() == sSF_AnnotatesID) {
				switch (msg.getEventType()) {
					case Notification.SET:
						if (!msg.isReset()) {
							if (!msg.isTouch()) {
								// The id has changed, remove the old reference and add in the new one.
								removeAnnotationFromMap((String) msg.getOldValue());
								setAnnotationFromMap((String) msg.getNewValue(), (Annotation) getTarget());
							} else
								setAnnotationFromMap((String) msg.getNewValue(), (Annotation) getTarget());	// It was touched							
							break;
						}	// else flow into unset.
					case Notification.UNSET:
						// The id has been removed. Remove the old reference
						removeAnnotationFromMap((String) msg.getOldValue());
						break;
				}
			}
		}			
		
		public void setTarget(Notifier target) {
			super.setTarget(target);
			if (target != null)
				setAnnotationFromMap(((AnnotationGeneric) target).getAnnotatesID(), (Annotation) target);
		}
	}

	private static final EObject
		sSF_AnnotatesID,
		sSF_Annotations;
	static {
		sSF_AnnotatesID = CDMPackage.eINSTANCE.getAnnotationGeneric_AnnotatesID();
		sSF_Annotations = CDMPackage.eINSTANCE.getDiagramData_Annotations();
	}
	
	/**
	 * Initialize the annotation
	 */
	protected void initializeAnnotationGeneric(AnnotationGeneric annotation) {
		// Make sure we have an adapter. If not, then add it in.
		AnnotationAdapter aa = (AnnotationAdapter) EcoreUtil.getExistingAdapter(annotation, AnnotationAdapter.class);
		if (aa == null) {
			// We need to create the adapter
			aa = new AnnotationAdapter();
			annotation.eAdapters().add(aa);			
		}
	}
		
	/**
	 * Set the model on the generic annotation.
	 */
	protected final void setModelOnAnnotationGeneric(Object model, AnnotationGeneric annotation) {
		setAnnotationID(annotation, getIDFromModel(model));
	}
	
	/**
	 * Set the id on the annotation.
	 */
	public final void setAnnotationID(AnnotationGeneric annotation, String id) {
		if (id != null)
			annotation.setAnnotatesID(id);
		else
			annotation.eUnset(CDMPackage.eINSTANCE.getAnnotationEMF_Annotates());
	}
	
	/**
	 * Return the id for the model object.
	 * Null may be passed in for model, the implementer should return null in that case.
	 */
	public abstract String getIDFromModel(Object model);

	/**
	 * Called whenever a linkage is made to an ID. It gives the subclass a chance to do something, such
	 * as add a listener on the object represented by the ID. Then if whatever causes the object's ID
	 * to change, the listener will see this and call the changeID method.
	 *
	 * The returned object will be placed in the auxiliary data of the map entry.
	 *
	 * Note: It will not be called when changeID is called.
	 */
	protected abstract Object linkingToID(String id);
	
	/**
	 * Called whenever a linkage is removed from an ID. It gives the subclass a chance to remove any
	 * listeners it may of added in linkageToID above. The data is the auxiliary data from the map.
	 */
	protected abstract void unlinkingFromID(String id, Object data);
	
	/**
	 * If possible, return the immediate contained children for this model object.
	 * A contained child is one that is owned by this model object, and
	 * if the model object is deleted, all of the contained children will
	 * also be gone. Don't add in children of these children (i.e. don't recurse). 
	 * If your model doesn't have this concept, or it cannot
	 * be computed, return Collections.EMPTY_LIST. This method is used
	 * so that Annotations of children can be found to be added or removed.
	 */
	public abstract List getContainedChildren(Object model);
	

	// DiagramData adapter listening for annotations changes. With these
	// changes it activates/deactivates the listeners for the annotation.
	private class DiagramDataAdapter extends AdapterImpl {
		public ArrayList targets = new ArrayList();
		
		public Notifier getTarget() {
			return null;	// Doesn't matter, we only have one instance of the adapter, it is shared.
		}
		
		public void setTarget(Notifier newTarget) {
			if (newTarget != null)
				targets.add(newTarget);
		}
		
		public boolean isAdapterForType(Object type) {
			return (DiagramDataAdapter.class == type);
		}
		
		public void notifyChanged(Notification msg) {
			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				targets.remove(msg.getNotifier());
			}
			
			if (inDispose)
				return;			
			
			if (msg.getFeature() == sSF_Annotations) {
				switch (msg.getEventType()) {
					case Notification.ADD:
					case Notification.SET:
						if (msg.getOldValue() instanceof AnnotationGeneric) {
							// This one is leaving the diagram data. Remove the AnnotationAdapter.
							Adapter a = EcoreUtil.getExistingAdapter(((AnnotationGeneric) msg.getOldValue()), AnnotationAdapter.class);
							if (a != null)
								((AnnotationGeneric) msg.getOldValue()).eAdapters().remove(a);
						}
						if (msg.getNewValue() instanceof AnnotationGeneric) {
							// Need to initialize it in the system.
							initializeAnnotationGeneric((AnnotationGeneric) msg.getNewValue());
						}
						break;
					case Notification.ADD_MANY:
						Iterator itr = ((List) msg.getNewValue()).iterator();
						while (itr.hasNext()) {
							Object n = itr.next();
							if (n instanceof AnnotationGeneric) {
								// Need to initialize it in the system.
								initializeAnnotationGeneric((AnnotationGeneric) n);
							}
						}
						break;
					case Notification.REMOVE:
						if (msg.getOldValue() instanceof AnnotationGeneric) {
							// This one is leaving the diagram data. Remove the AnnotationAdapter.
							Adapter a = EcoreUtil.getExistingAdapter((AnnotationGeneric) msg.getOldValue(), AnnotationAdapter.class);
							if (a != null)
								((AnnotationGeneric) msg.getOldValue()).eAdapters().remove(a);
						}
						break;
					case Notification.REMOVE_MANY:
						itr = ((List) msg.getNewValue()).iterator();
						while (itr.hasNext()) {
							Object o = itr.next();
							if (o instanceof AnnotationGeneric) {
								// This one is leaving the diagram data. Remove the AnnotationAdapter.
								Adapter a = EcoreUtil.getExistingAdapter((AnnotationGeneric) o, AnnotationAdapter.class);
								if (a != null)
									((AnnotationGeneric) o).eAdapters().remove(a);
							}
						}
						break;
				}
			}
		}
	}
	
	private DiagramDataAdapter ddAdapter = new DiagramDataAdapter();
		
	/**
	 * Override to put an adapter in DiagramData so that it listens
	 * for changes to annotations feature and activates or deactivates
	 * AnnotationGeneric's that go through it.
	 *
	 * It will be removed when we dispose and when DiagramData's resource
	 * is unloaded.
	 */
	public void initializeLinkages(DiagramData data) {
		super.initializeLinkages(data);
		
		if (EcoreUtil.getExistingAdapter(data, DiagramDataAdapter.class) == null) {
			data.eAdapters().add(ddAdapter);
		}
	}
				
}