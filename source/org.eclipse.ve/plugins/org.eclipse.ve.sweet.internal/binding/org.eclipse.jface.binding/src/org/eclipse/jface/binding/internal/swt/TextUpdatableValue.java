/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jface.binding.internal.swt;

import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.UpdatableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TextUpdatableValue extends UpdatableValue {

	private final Text text;

	public int listenerType;

	public static final int DEFAULT_UPDATE_POLICY = SWT.Modify;

	private boolean updating = false;

	public TextUpdatableValue(Text text) {
		this(text, DEFAULT_UPDATE_POLICY);
	}

	public TextUpdatableValue(Text text, int listenerType) {
		this.text = text;
		this.listenerType = listenerType;
		text.addListener(listenerType, new Listener() {
			public void handleEvent(Event event) {
				if (!updating) {
					fireChangeEvent(IChangeEvent.CHANGE, null, null);
				}
			}
		});
	}

	public void setValue(Object value) {
		try {
			updating = true;
			text.setText(value == null ? "" : value.toString());
		} finally {
			updating = false;
		}
	}

	public Object getValue() {
		return text.getText();
	}

	public Class getValueType() {
		return String.class;
	}

}
