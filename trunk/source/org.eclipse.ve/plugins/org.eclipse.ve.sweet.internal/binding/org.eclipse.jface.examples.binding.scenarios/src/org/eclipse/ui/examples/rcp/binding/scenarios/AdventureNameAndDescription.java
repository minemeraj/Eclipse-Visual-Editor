/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.util.StringTokenizer;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.UpdatableValue;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;

public class AdventureNameAndDescription extends UpdatableValue {

	private Adventure adventure;

	private AdapterImpl adapter;

	public AdventureNameAndDescription(Adventure anAdventure) {
		adventure = anAdventure;
		adapter = new AdapterImpl() {
			public void notifyChanged(Notification notification) {
				if (!notification.isTouch()) {
					// We are only interested in "description" and "name" events
					// being fired
					if (notification.getFeature() == AdventurePackage.eINSTANCE
							.getAdventure_Description()
							|| notification.getFeature() == AdventurePackage.eINSTANCE
									.getAdventure_Name()) {
						AdventureNameAndDescription.this.fireChangeEvent(
								IChangeEvent.CHANGE, null, null);
					}
				}
				super.notifyChanged(notification);
			}
		};
		adventure.eAdapters().add(adapter);
	}

	public void setValue(Object value) {
		StringTokenizer tokenizer = new StringTokenizer((String) value, ":");
		adventure.setDescription(((String) tokenizer.nextElement()).trim());
		if (tokenizer.countTokens() == 1) {
			adventure.setName(((String) tokenizer.nextElement()).trim());
		} else {
			adventure.setName("");
		}
	}

	public Object getValue() {
		return adventure.getDescription() + " : " + adventure.getName();
	}

	public Class getValueType() {
		return String.class;
	}

	public void dispose() {
		adventure.eAdapters().remove(adapter);
	}

}
