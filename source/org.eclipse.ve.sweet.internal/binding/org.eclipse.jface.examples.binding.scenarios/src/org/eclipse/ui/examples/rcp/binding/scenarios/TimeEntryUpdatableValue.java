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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import org.eclipse.jface.databinding.ChangeEvent;
import org.eclipse.jface.databinding.UpdatableValue;

public class TimeEntryUpdatableValue extends UpdatableValue {

	private TimeEntry timeEntry;

	public TimeEntryUpdatableValue(TimeEntry aTimeEntry) {
		timeEntry = aTimeEntry;
		timeEntry.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				fireChangeEvent(ChangeEvent.CHANGE, evt.getOldValue(), evt
						.getNewValue());
			}
		});
	}

	public void setValue(Object value) {
		Object oldValue = getValue();
		try {
			timeEntry.setTime((String) value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		fireChangeEvent(ChangeEvent.CHANGE, oldValue, getValue());
	}

	public Object getValue() {
		return timeEntry.getTime();
	}

	public Class getValueType() {
		return String.class;
	}

}
