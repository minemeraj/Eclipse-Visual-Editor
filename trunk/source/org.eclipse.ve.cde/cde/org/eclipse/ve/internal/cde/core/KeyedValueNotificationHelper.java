package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: KeyedValueNotificationHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
/**
 * This class contains static helper methods to
 * help with KeyedValue notifications. Such as
 * making notifications of KeyedValue changes
 * look like standard property changes.
 */
public class KeyedValueNotificationHelper {
	
	/**
	 * Adapter that are interested in knowing when a specific keyedValue
	 * has been changed should call this method.
	 *
	 * Usage in an adapter is:
	 *   notifyChanged(Notification msg) {
	 *     Notification kvMsg = KeyedValueNofificationHelper.notifyChanged(msg, Object keyOfInterest))
	 *     if (kvMsg != null) {
	 *       ...process the kvMsg. eventType will be SET/UNSET, isTouch works, don't use isReset because these are considered unsettable,
	 *          old/new will be the old and new BasicEMap.Entry (i.e. KeyedValue)).
	 *     } else {
	 *       ...process as a normal msg.
	 *     }
	 */
	 public static Notification notifyChanged(Notification msg, Object keyOfInterest) {
	 	if (msg.getFeatureID(KeyedValueHolder.class) != CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES)
	 		return null;
	 		
	 	// Adds and removes will be treated as Set and Unset because we are treating these as single valued features.
		switch (msg.getEventType()) {
			case Notification.ADD_MANY:
				// There where several added at once, return for the one interest.
				Iterator itr = ((List) msg.getNewValue()).iterator();
				while (itr.hasNext()) {
					BasicEMap.Entry kv = (BasicEMap.Entry) itr.next();
					if (keyOfInterest.equals(kv.getKey()))
						return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET, CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, null, kv, Notification.NO_INDEX);
				}
				break;
				
			case Notification.ADD:	// It was added.
			case Notification.SET:	// It was replaced.
				BasicEMap.Entry kv = (BasicEMap.Entry) msg.getNewValue();
				if (keyOfInterest.equals(kv.getKey())) 
					return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET , CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, msg.getOldValue(), msg.getNewValue(), Notification.NO_INDEX);
				break;
				
			case Notification.REMOVE_MANY:
				// There where several removed at once, return for the one of interest.
				itr = ((List) msg.getOldValue()).iterator();
				while (itr.hasNext()) {
					kv = (BasicEMap.Entry) itr.next();
					if (keyOfInterest.equals(kv))
						return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.UNSET, CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, kv, null, Notification.NO_INDEX);
				}
				break;
				
			case Notification.REMOVE:
				kv = (BasicEMap.Entry) msg.getOldValue();
				if (keyOfInterest.equals(kv.getKey())) 
					return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.UNSET, CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, kv, null, Notification.NO_INDEX);
				break;
				
			case Notification.MOVE:
				break;	// Moving a keyedvalue from one position to another is not considered a change to notify about.
				
		}
		
		return null;
	 }
					
}