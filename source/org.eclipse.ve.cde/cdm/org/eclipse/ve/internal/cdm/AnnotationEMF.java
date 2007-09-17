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
package org.eclipse.ve.internal.cdm;
/*
 *  $RCSfile: AnnotationEMF.java,v $
 *  $Revision: 1.8 $  $Date: 2007-09-17 14:17:13 $ 
 */


import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation EMF</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Annotation that can annotate an EMF object (RefObject).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.AnnotationEMF#getAnnotates <em>Annotates</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getAnnotationEMF()
 * @model
 * @generated
 */
public interface AnnotationEMF extends Annotation{


	/**
	 * If the parent annotation of an EObject changes, a special custom notification will go out on
	 * the EObject.
	 * 
	 * The Notification will have an eventType == PARENT_ANNOTATION_NOTIFICATION_TYPE, 
	 * and the postion == the simulated eventType (i.e. Notification.SET, UNSET, etc).
	 * 
	 * The old and new values will be the appropriate annotations.
	 */
	
	/**
	 * The adapter class for retrieving the parent annotation. It is stored on any EObject instance.
	 * If the adapter doesn't exist on the EObject, then the EObject doesn't have an annotation attached to it.
	 */
	public interface ParentAdapter extends Adapter {
		public static final Class PARENT_ANNOTATION_ADAPTER_KEY = ParentAdapter.class;	// The key for getting the parent annotation adapter.
		public static final int PARENT_ANNOTATION_NOTIFICATION_TYPE = -500;	// Notification type that is used for sending notification since this is not a real feature.		
		/**
		 * Answer the parent annotation, if any. null if not one.
		 */
		public Annotation getParentAnnotation();
	}
	
	/**
	 * Returns the value of the '<em><b>Annotates</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annotates</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annotates</em>' reference.
	 * @see #setAnnotates(EObject)
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getAnnotationEMF_Annotates()
	 * @model
	 * @generated
	 */
	EObject getAnnotates();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cdm.AnnotationEMF#getAnnotates <em>Annotates</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annotates</em>' reference.
	 * @see #getAnnotates()
	 * @generated
	 */
	void setAnnotates(EObject value);

}
