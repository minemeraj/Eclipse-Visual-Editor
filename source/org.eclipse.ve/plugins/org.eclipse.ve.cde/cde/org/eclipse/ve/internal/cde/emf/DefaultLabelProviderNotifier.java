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
 * $RCSfile: DefaultLabelProviderNotifier.java,v $ $Revision: 1.2 $ $Date: 2004-03-26 23:07:50 $
 */
package org.eclipse.ve.internal.cde.emf;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.AnnotationPolicy.AnnotationListener;

/**
 * This can be used by EMF editparts to encapsulate handling a label provider, listening for changes in the label provider, listening for changes to
 * the EMF model object, and notify when the label needs to be updated.
 * 
 * When no longer needed, call with setModel(null, null, null) to remove all listening.
 * 
 * @version 1.0
 * @author
 */
public class DefaultLabelProviderNotifier {

	public interface IDefaultLabelProviderListener {

		/**
		 * Notify to refresh the visuals.
		 * 
		 * @param provider -
		 *            The provider to use
		 */
		public void refreshLabel(ILabelProvider provider);
	}

	protected EObject model;

	protected ILabelProvider labelProvider;

	protected IDefaultLabelProviderListener listener;

	protected AnnotationListener annotationListener;

	protected ILabelProviderListener labelProviderListener;

	protected EditDomain domain;
	{
		labelProviderListener = new ILabelProviderListener() {

			public void labelProviderChanged(LabelProviderChangedEvent event) {
				// We'll be conservative and refresh for any change.
				listener.refreshLabel(labelProvider);
			}
		};
	}

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
					annotationListener = new AnnotationListener(model, domain) {

						public void notifyAnnotation(int eventType, Annotation oldAnnotation, Annotation newAnnotation) {
							CDEUtilities.displayExec(getDisplay(), new Runnable() {

								public void run() {
									if (model != null) {
										// Added or removed an annotation so refresh label.
										listener.refreshLabel(labelProvider);
									}
								}
							});
						}

						public void notifyAnnotationChanges(Notification msg) {
							if (labelProvider == null)
								return;
							Object sf = msg.getFeature();
							if (sf instanceof EStructuralFeature) {
								// It's supposed to be here, but it may be null
								if (DefaultLabelProviderNotifier.this.labelProvider.isLabelProperty(DefaultLabelProviderNotifier.this.model,
										((EStructuralFeature) sf).getName())) {
									CDEUtilities.displayExec(getDisplay(), new Runnable() {

										public void run() {
											if (model != null) {
												// label changed
												listener.refreshLabel(labelProvider);
											}
										}
									});
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

	private Display getDisplay() {
		return domain.getEditorPart().getSite().getWorkbenchWindow().getShell().getDisplay();
	}

}