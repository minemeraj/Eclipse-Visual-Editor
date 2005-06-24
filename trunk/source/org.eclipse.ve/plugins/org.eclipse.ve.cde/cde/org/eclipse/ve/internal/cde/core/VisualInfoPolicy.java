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
 *  $RCSfile: VisualInfoPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-24 18:57:15 $ 
 */

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.commands.ApplyVisualInfoCommand;
import org.eclipse.ve.internal.cde.commands.CancelKeyedValueCommand;
import org.eclipse.ve.internal.cdm.*;
/**
 * A helper policy for handling the VisualInfo.
 * It takes care of finding it, updating it, getting
 * notifications back of changes to it.
 *
 * It is not instantiated, all of the methods are statics.
 *
 * There is a inner class, VisualInfoListener, which when created
 * will listen for annotation add/removes, visual info add/removes, and
 * any visual info changes. This can be used for listening to VisualInfo changes.
 */
public class VisualInfoPolicy {
	
	/**
	 * Return the VisualInfo for the specified EditPart.
	 */
	public static VisualInfo getVisualInfo(EditPart ep) {
		return getVisualInfo(ep.getModel(), ep.getRoot().getViewer());
	}
		
	/**
	 * Return the VisualInfo for the specified object for the
	 * specified viewer. This is usually used on ADD/CREATE because
	 * we don't yet have an editpart to find the viewer.
	 */
	public static VisualInfo getVisualInfo(Object model, EditPartViewer viewer) {
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		AnnotationLinkagePolicy al = dom.getAnnotationLinkagePolicy();
		
		// Get the Annotation for the editpart.
		Annotation annot = al.getAnnotation(model)		;
		if (annot == null)
			return null;	// There is no annotation to store the VisualInfo
			
		// Get the diagram for the viewer that the object is/will be in.
		Diagram diagram = dom.getDiagram(viewer);
		VisualInfo vi = annot.getVisualInfo(diagram);
		return vi;
	}
	
	/**
	 * Get all of the visual infos for the model object.
	 * @param model
	 * @param dom
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static List getAllVisualInfos(Object model, EditDomain dom) {
		AnnotationLinkagePolicy al = dom.getAnnotationLinkagePolicy();
		
		// Get the Annotation for the editpart.
		Annotation annot = al.getAnnotation(model)		;
		if (annot == null)
			return Collections.EMPTY_LIST;	// There is no annotation to store the VisualInfo
		return annot.getVisualInfos();
	}
	
	
	/**
	 * Create the appropriate command to apply the Visual Info setting.
	 * It will create the VisualInfo/Annotation if necessary.
	 *
	 * Note: This is important to be used if the annotation/visual info may not yet be available. It will make
	 * sure that they are created correctly. If the visual info already exists, then a straight ApplyKeyedValueCommand
	 * could be used, however this is simpler in that it can be applied without worrying if it exists or not.
	 */
	public static Command applyVisualInfoSetting(Object modelObject, Object newValue, EditDomain domain, Diagram diagram) {
		return new ApplyVisualInfoCommand(modelObject, newValue, domain, diagram);
	}
	
	/**
	 * Cancel a VisualInfo KeyValue.
	 * It will do more in that if that is the only keyedValue. This will remove it from the
	 * diagram, thereby saving storage.
	 */
	public static Command cancelVisualInfoSetting(VisualInfo vi, String key) {
		if (!vi.getKeyedValues().containsKey(key))
			return null;	// Not set, so can't cancel.
			
		if (vi.getKeyedValues().size() == 1)
			return AnnotationPolicy.cancelVisualInfo(vi);	// Cancel the entire visual info
		
		// There is more than one, so only cancel the one setting.
		CancelKeyedValueCommand cmd = new CancelKeyedValueCommand();
		cmd.setTarget(vi);
		cmd.setKey(key);
		return cmd;
	}
	
	/**
	 * To completely remove a VisualInfo.
	 *
	 * Note: It is important that this command be used to completely remove a VisualInfo. If it isn't
	 * used, it won't be correctly removed from the model.
	 */
	public static Command cancelVisualInfo(VisualInfo vi) {
		return AnnotationPolicy.cancelVisualInfo(vi);
	}
	
	
	/**
	 * A compound listener that listens for VisualInfo changes. It is abstract. Users should implement the two abstract methods
	 * to get notification of annotation/visualinfo (of the diagram of interest) being added/removed, or the annotation being modified.
	 *
	 * It notifies:
	 *   a) VisualInfos added/removed for the model and diagram of interest. This will be sent to notifyVisualInfo.
	 *   b) Any VisualInfo changes, including KeyValues. Notifier will be VisualInfo. This will be sent to notifyVisualInfoChanges.
	 */	
	public static abstract class VisualInfoListener extends AnnotationPolicy.AnnotationListener {
		protected Diagram diagram;
		protected VisualInfo visualInfo;
		protected Adapter visualInfoAdapter;
		
		public VisualInfoListener(Object model, Diagram diagram, EditDomain domain) {
			super(model, domain);
			this.diagram = diagram;
			if (annotation != null)
				setupVisualInfoAdapter(annotation.getVisualInfo(diagram));
		}
		
		public abstract void notifyVisualInfo(int eventType, VisualInfo oldVI, VisualInfo newVI);
		public abstract void notifyVisualInfoChanges(Notification msg);		
		
		/**
		 * Remove all listening.
		 */
		public void removeListening() {
			super.removeListening();			
			if (diagram != null) {
				removeVisualInfoAdapter();
				diagram = null;
			}
		}
		
		/**
		 * This can be overridden if desired by subclasses, but super must be called.
		 * @see org.eclipse.ve.internal.cde.core.AnnotationPolicy.AnnotationListener#notifyAnnotation(int, Annotation, Annotation)
		 */
		public void notifyAnnotation(int eventType, Annotation oldAnnotation, Annotation newAnnotation) {
			if (eventType == Notification.SET) {
				if (oldAnnotation != newAnnotation)
					setupVisualInfoAdapter(newAnnotation != null ? newAnnotation.getVisualInfo(diagram) : null);
			} else
				removeVisualInfoAdapter();
		}
		
		/**
		 * This can be overridden if desired by subclasses, but super must be called.
		 * @see org.eclipse.ve.internal.cde.core.AnnotationPolicy.AnnotationListener#notifyAnnotationChanges(Notification)
		 */
		public void notifyAnnotationChanges(Notification msg) {
			if (msg.getFeatureID(Annotation.class) == CDMPackage.ANNOTATION__VISUAL_INFOS) {
				switch (msg.getEventType()) {
					case Notification.ADD:
					case Notification.SET:
						VisualInfo vi = (VisualInfo) msg.getNewValue();
						if (vi.getDiagram() == diagram) {
							if (!msg.isTouch())
								setupVisualInfoAdapter(vi);
							notifyVisualInfo(Notification.SET, (VisualInfo) msg.getOldValue(), vi);
						}
						break;
					case Notification.ADD_MANY:
						Iterator itr = ((List) msg.getNewValue()).iterator();
						while (itr.hasNext()) {
							vi = (VisualInfo) itr.next();
							if (vi.getDiagram() == diagram) {
								setupVisualInfoAdapter(vi);
								notifyVisualInfo(Notification.SET, null, vi);
								break;
							}
						}
						break;
					case Notification.REMOVE:
						vi = (VisualInfo) msg.getOldValue();
						if (vi.getDiagram() == diagram) {
							removeVisualInfoAdapter();
							notifyVisualInfo(Notification.UNSET, vi, null);
						}
						break;
					case Notification.REMOVE_MANY:
						itr = ((List) msg.getOldValue()).iterator();
						while (itr.hasNext()) {
							vi = (VisualInfo) itr.next();
							if (vi.getDiagram() == diagram) {
								removeVisualInfoAdapter();
								notifyVisualInfo(Notification.UNSET, vi, null);
								break;
							}
						}
						break;
				}
			}			
		}		
						
		private void setupVisualInfoAdapter(VisualInfo visualInfo) {
			removeVisualInfoAdapter();
			
			this.visualInfo = visualInfo;
			if (visualInfo != null) {
				visualInfoAdapter = new AdapterImpl() {
					public void notifyChanged(Notification msg) {
						if (msg.getEventType() == Notification.REMOVING_ADAPTER)
							removeVisualInfoAdapter();
						else
							notifyVisualInfoChanges(msg);
					}

				};
				visualInfo.eAdapters().add(visualInfoAdapter);
			}
		}
		
		private void removeVisualInfoAdapter() {
			if (visualInfoAdapter != null) {
				Adapter a = visualInfoAdapter;
				visualInfoAdapter = null;	// So we don't get a loop on remove.
				visualInfo.eAdapters().remove(a);
				visualInfo = null;
			}
		}		
	}	
		
}