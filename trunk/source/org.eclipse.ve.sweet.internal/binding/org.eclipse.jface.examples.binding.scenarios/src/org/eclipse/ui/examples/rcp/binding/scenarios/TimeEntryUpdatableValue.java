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
/*
 *  $RCSfile: TimeEntryUpdatableValue.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-18 17:38:36 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.UpdatableValue;

public class TimeEntryUpdatableValue extends UpdatableValue {

	private TimeEntry timeEntry;

	public TimeEntryUpdatableValue(TimeEntry aTimeEntry) {
		timeEntry = aTimeEntry;
		timeEntry.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				fireChangeEvent(IChangeEvent.CHANGE, evt.getOldValue(), evt
						.getNewValue());
			}
		});
	}

	public void setValue(Object value) {
		try {
			timeEntry.setTime((String) value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Object getValue() {
		return timeEntry.getTime();
	}

	public Class getValueType() {
		return String.class;
	}

}
