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
 *  $RCSfile: AnnotationCreationFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-20 23:54:40 $ 
 */
package org.eclipse.ve.internal.cde.core;



import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.emf.IDomainedFactory;
import org.eclipse.ve.internal.cdm.*;

import org.eclipse.gef.requests.CreationFactory;

/**
 * This wrappers a CreationFactory. It will add into the new object created by the CreationFactory the annotations that are set within this factory.
 * 
 * @since 1.1.0
 */
public class AnnotationCreationFactory extends Object implements CreationFactory, IDomainedFactory {

	protected CreationFactory objectFactory;

	protected List keyedValues;

	protected AnnotationLinkagePolicy policy;

	/**
	 * Create with keyed values and object factory.
	 * @param keyedValues
	 * @param objectFactory
	 * 
	 * @since 1.1.0
	 */
	public AnnotationCreationFactory(List keyedValues, CreationFactory objectFactory) {
		this.keyedValues = keyedValues;
		this.objectFactory = objectFactory;
	}

	/**
	 * Create with only an object factory. This constructor will still create an
	 * annotation, but it will be empty.
	 * @param objectFactory
	 * 
	 * @since 1.1.0
	 */
	public AnnotationCreationFactory(CreationFactory objectFactory) {
		this(null, objectFactory);
	}

	/**
	 * Get the wrappered factory.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public CreationFactory getWrapperedFactory() {
		return objectFactory;
	}

	public Object getObjectType() {
		return objectFactory.getObjectType();
	}

	public Object getNewObject() {
		Object newObject = objectFactory.getNewObject();
		// Note: Below when adding the keyed value, since we don't know if there is already a key in there we have to
		// remove the old one first, if there is one. This is because add() doesn't check if the key is already there.
		Annotation annotation = policy.getAnnotation(newObject);
		if (annotation == null)
			annotation = AnnotationPolicy.createAnnotation(newObject);
		
		if (keyedValues != null) {
			EMap kvs = annotation.getKeyedValues();
			Iterator itr = keyedValues.iterator();
			while (itr.hasNext()) {
				EObject kv = EcoreUtil.copy((EObject) itr.next());
				int keyPos = kvs.indexOfKey(((BasicEMap.Entry) kv).getKey());
				if (keyPos != -1)
					kvs.set(keyPos, kv);
				else
					kvs.add(kv);
			}
		}
		
		policy.setModelOnAnnotation(newObject, annotation);


		return newObject;
	}

	/**
	 * @see IDomainedFactory#setEditDomain(EditDomain)
	 */
	public void setEditDomain(EditDomain domain) {
		policy = domain.getAnnotationLinkagePolicy();
		if (objectFactory instanceof IDomainedFactory)
			((IDomainedFactory) objectFactory).setEditDomain(domain);
	}

}