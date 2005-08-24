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
 * $RCSfile: KeyedValueNotificationHelper.java,v $ $Revision: 1.5 $ $Date: 2005-08-24 23:12:50 $
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;

/**
 * This class contains static helper methods to help with KeyedValue notifications. Such as making notifications of KeyedValue changes look like
 * standard property changes.
 * 
 * @since 1.0.0
 */
public class KeyedValueNotificationHelper {

	/**
	 * Adapter that are interested in knowing when a specific keyedValue has been changed should call this method.
	 * 
	 * Usage in an adapter is: notifyChanged(Notification msg) { Notification kvMsg = KeyedValueNofificationHelper.notifyChanged(msg, Object
	 * keyOfInterest)) if (kvMsg != null) { ...process the kvMsg. eventType will be SET/UNSET, isTouch works, don't use isReset because these are
	 * considered unsettable, old/new will be the old and new BasicEMap.Entry (i.e. KeyedValue)). } else { ...process as a normal msg. }
	 * 
	 * @param msg
	 * @param keyOfInterest
	 * @return notification if key of interest, or <code>null</code> if not keyed values or not key of interest. The old/new values will be of type
	 *         BasicEMap.Entry (i.e. the keyed value), key and value. The feature will be KeyedValues feature (i.e. unchanged).
	 * 
	 * @see BasicEMap.Entry
	 * @since 1.0.0
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
						return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET,
								CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, null, kv, Notification.NO_INDEX);
				}
				break;

			case Notification.ADD:
			// It was added.
			case Notification.SET:
				// It was replaced.
				BasicEMap.Entry kv = (BasicEMap.Entry) msg.getNewValue();
				if (keyOfInterest.equals(kv.getKey()))
					return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET, CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES,
							msg.getOldValue(), msg.getNewValue(), Notification.NO_INDEX);
				break;

			case Notification.REMOVE_MANY:
				// There where several removed at once, return for the one of interest.
				itr = ((List) msg.getOldValue()).iterator();
				while (itr.hasNext()) {
					kv = (BasicEMap.Entry) itr.next();
					if (keyOfInterest.equals(kv))
						return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.UNSET,
								CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, kv, null, Notification.NO_INDEX);
				}
				break;

			case Notification.REMOVE:
				kv = (BasicEMap.Entry) msg.getOldValue();
				if (keyOfInterest.equals(kv.getKey()))
					return new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.UNSET,
							CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, kv, null, Notification.NO_INDEX);
				break;

			case Notification.MOVE:
				break; // Moving a keyedvalue from one position to another is not considered a change to notify about.

		}

		return null;
	}

	/**
	 * This will return Notifications for all KeyedValued types. This is for when just interested in any KeyedValues, this will return a notification
	 * for each type.
	 * <p>
	 * An ADD_MANY/REMOVE_MANY will be turned into individual notifications. This is because you can't have multiple values with the same key (or
	 * semantically you can't, if code tries to do it, it can be done, but it isn't right).
	 * 
	 * @param msg
	 * @return notifications array, or <code>null</code> if not keyed values. The old/new values will be of type BasicEMap.Entry (i.e. the keyed
	 *         value), key and value.
	 * 
	 * @see BasicEMap.Entry
	 * 
	 * @since 1.0.0
	 */
	public static Notification[] notifyChanged(Notification msg) {
		if (msg.getFeatureID(KeyedValueHolder.class) != CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES)
			return null;

		List notifications = new ArrayList(2);
		// Adds and removes will be treated as Set and Unset because we are treating these as single valued features.
		switch (msg.getEventType()) {
			case Notification.ADD_MANY:
				// There where several added at once, return for the one interest.
				Iterator itr = ((List) msg.getNewValue()).iterator();
				while (itr.hasNext()) {
					BasicEMap.Entry kv = (BasicEMap.Entry) itr.next();
					notifications.add(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET,
							CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, null, kv, Notification.NO_INDEX));
				}
				break;

			case Notification.ADD:
			// It was added.
			case Notification.SET:
				// It was replaced.
				BasicEMap.Entry kv = (BasicEMap.Entry) msg.getNewValue();
				notifications.add(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET,
						CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, msg.getOldValue(), msg.getNewValue(), Notification.NO_INDEX));
				break;

			case Notification.REMOVE_MANY:
				// There where several removed at once, return for the one of interest.
				itr = ((List) msg.getOldValue()).iterator();
				while (itr.hasNext()) {
					kv = (BasicEMap.Entry) itr.next();
					notifications.add(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.UNSET,
							CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, kv, null, Notification.NO_INDEX));
				}
				break;

			case Notification.REMOVE:
				kv = (BasicEMap.Entry) msg.getOldValue();
				notifications.add(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.UNSET,
						CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES, kv, null, Notification.NO_INDEX));
				break;

			case Notification.MOVE:
				break; // Moving a keyedvalue from one position to another is not considered a change to notify about.

		}

		if (notifications.isEmpty())
			return null;
		return (Notification[]) notifications.toArray(new Notification[notifications.size()]);

	}

}
