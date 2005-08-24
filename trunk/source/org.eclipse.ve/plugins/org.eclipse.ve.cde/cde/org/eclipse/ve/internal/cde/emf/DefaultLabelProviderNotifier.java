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
/*
 * $RCSfile: DefaultLabelProviderNotifier.java,v $ $Revision: 1.6 $ $Date: 2005-08-24 23:12:48 $
 */
package org.eclipse.ve.internal.cde.emf;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.*;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.CDMPackage;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.AnnotationPolicy.AnnotationListener;

/**
 * This can be used by EMF editparts to encapsulate handling a label provider, listening for changes in the label provider, listening for changes to
 * the EMF model object, and notify when the label needs to be updated.
 * <p>
 * When no longer needed, call with setModel(null, null, null) to remove all listening.
 * <p>
 * The notification will be through the <code>IDefaultLabelProviderListener</code> added to this notifier.
 * <p>
 * It will use the labelProvider.isLabelProperty(element, propertyName) to determine if change will cause a refresh of label. For changes to the 
 * model, the element will be the model. If changes are to the annotation, element will be the annotation. Also, if 
 * changes are to the annotation, and the feature is a keyedValues, then the propertyName will be the key of the value,
 * and not the KeyedValues feature itself.
 * 
 * @since 1.0
 * @see DefaultLabelProviderNotifier
 */
public class DefaultLabelProviderNotifier {

	public interface IDefaultLabelProviderListener {

		/**
		 * Notify to refresh the visuals.
		 * <p>
		 * NOTE: It is not guarenteed that this will be called from a UI thread. If this is necessary
		 * then the implementation will need to deal with this.
		 * 
		 * @param provider - the provider that needs to be refreshed.
		 */
		public void refreshLabel(ILabelProvider provider);
	}

	protected EObject model;

	protected ILabelProvider labelProvider;

	protected IDefaultLabelProviderListener listener;

	protected AnnotationListener annotationListener;

	/*
	 * Used by this class to listen for changes to the label provider itself. For instance it could signal that
	 * something about it has changed, and so we need a refresh. This one we use here is conservative and will
	 * refresh on any change in the label provider itself.
	 */
	protected ILabelProviderListener labelProviderListener = new ILabelProviderListener() {

		public void labelProviderChanged(LabelProviderChangedEvent event) {
			// We'll be conservative and refresh for any change.
			listener.refreshLabel(labelProvider);
		}
	};
	
	/*
	 * Used by this class to listen to the model. If there are any changes, the property name is sent to the label
	 * provider to determine if a refresh is required, and if so, the notification is sent out that a refresh is needed.
	 */
	protected Adapter modelListener = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (labelProvider == null)
				return;	// It could of gone away, but we are still listening.
			switch (msg.getEventType()) {
				case Notification.ADD:
				case Notification.ADD_MANY:
				case Notification.REMOVE:
				case Notification.REMOVE_MANY:
				case Notification.SET:
				case Notification.UNSET:
					Object sf = msg.getFeature();
					if (sf instanceof EStructuralFeature) {
						// Check to see if the label provider needs a refresh for this property.
						if (labelProvider.isLabelProperty(model, ((EStructuralFeature) sf).getName())) {
							listener.refreshLabel(labelProvider);
						}
					}
					break;
				default:
					break;
			}
		}
	};
	
	protected EditDomain domain;

	/**
	 * Constructor for DefaultLabelProviderNotifier.
	 */
	public DefaultLabelProviderNotifier() {
	}

	/**
	 * Return the label provider for ad-hoc requests.
	 */
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * Set a new provider. It should not be null.
	 */
	public void setLabelProvider(ILabelProvider provider) {
		if (model != null) {
			if (this.labelProvider != null)
				this.labelProvider.removeListener(labelProviderListener);
			this.labelProvider = provider;
			if (this.labelProvider != null) {
				this.labelProvider.addListener(labelProviderListener);

				if (annotationListener == null) {
					model.eAdapters().add(modelListener);	// We add/remove model and annotation listener at same time, so check on annotation listener is sufficient.
					annotationListener = new AnnotationListener(model, domain) {

						public void notifyAnnotation(int eventType, Annotation oldAnnotation, Annotation newAnnotation) {
							if (labelProvider == null)
								return;
							// Added or removed an annotation so refresh label.
							listener.refreshLabel(labelProvider);
						}

						public void notifyAnnotationChanges(Notification msg) {
							if (labelProvider == null)
								return;
							Object sf = msg.getFeature();
							if (sf instanceof EStructuralFeature) {
								boolean refresh = false;
								if (sf == CDMPackage.eINSTANCE.getKeyedValueHolder_KeyedValues()) {
									refresh = labelProvider.isLabelProperty(msg.getNotifier(), (String) ((BasicEMap.Entry) (msg.getEventType() == Notification.SET ? msg.getNewValue() : msg.getOldValue())).getKey());
								} else
									refresh = labelProvider.isLabelProperty(model, ((EStructuralFeature) sf).getName());
								// Check to see if the label provider needs a refresh for this property.
								if (refresh) {
									// label changed
									listener.refreshLabel(labelProvider);
								}
							}
						}
					};
				}
			}
		}
	}

	/**
	 * Set up and start listening. To stop listening, send in null for everything.
	 */
	public void setModel(EObject model, EditDomain domain, IDefaultLabelProviderListener listener, ILabelProvider provider) {
		if (this.model != null) {
			// release old
			if (this.labelProvider != null)
				this.labelProvider.removeListener(labelProviderListener);
			this.labelProvider = null;
			if (annotationListener != null)
				annotationListener.removeListening();
			this.model.eAdapters().remove(modelListener);
			annotationListener = null;
			this.model = null;
			this.domain = null;
		}

		if (model != null) {
			this.model = model;
			this.domain = domain;
			this.listener = listener;
			if (provider != null)
				setLabelProvider(provider);
		}
	}

}
