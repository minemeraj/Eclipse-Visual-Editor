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
 *  $RCSfile: AnnotationCreationFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */



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
 * This factory creates the annotation for the object that will be created from
 * the factory passed to it.
 */
public class AnnotationCreationFactory extends Object implements CreationFactory, IDomainedFactory {
	protected CreationFactory objectFactory;
	protected List keyedValues;
	protected AnnotationLinkagePolicy policy;

public AnnotationCreationFactory(List keyedValues, CreationFactory objectFactory) {
	this.keyedValues = keyedValues;
	this.objectFactory = objectFactory;
}

public AnnotationCreationFactory(CreationFactory objectFactory) {
	this(null, objectFactory);
}

public Object getObjectType() {
	return objectFactory.getObjectType();
}

/**
 * getNewObject method comment. 
 */
public Object getNewObject() {
	Object newObject = objectFactory.getNewObject();
	// Note: Below when adding the keyed value, since we don't know if there is already a key in there we have to
	// remove the old one first, if there is one. This is because add() doesn't check if the key is already there.
	if (newObject instanceof EObject) {
		CDMFactory fact = CDMFactory.eINSTANCE;
		AnnotationEMF annotation = fact.createAnnotationEMF();
		if (keyedValues != null) {
			EMap kvs = annotation.getKeyedValues();
			Iterator itr = keyedValues.iterator();
			while (itr.hasNext()) {
				EObject kv = EcoreUtil.copy((EObject) itr.next());
				int keyPos = kvs.indexOfKey(((BasicEMap.Entry)kv).getKey());
				if (keyPos != -1)
					kvs.set(keyPos, kv);
				else
					kvs.add(kv);
			}
		}
		annotation.setAnnotates((EObject) newObject);
	} else {
		CDMFactory fact = CDMFactory.eINSTANCE;
		AnnotationGeneric annotation = fact.createAnnotationGeneric();
		if (keyedValues != null) {
			EMap kvs = annotation.getKeyedValues();
			Iterator itr = keyedValues.iterator();
			while (itr.hasNext()) {
				EObject kv = EcoreUtil.copy((EObject) itr.next());
				int keyPos = kvs.indexOfKey(((BasicEMap.Entry)kv).getKey());
				if (keyPos != -1)
					kvs.set(keyPos, kv);
				else
					kvs.add(kv);
			}
		}
		policy.setModelOnAnnotation(newObject, annotation);
	}

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