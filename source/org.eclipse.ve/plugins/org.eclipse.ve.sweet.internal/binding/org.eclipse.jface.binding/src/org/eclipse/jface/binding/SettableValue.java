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
package org.eclipse.jface.binding;

public class SettableValue extends UpdatableValue {

	private final Class type;

	private Object value;

	public SettableValue(Class type) {
		this.type = type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setValueAndNotify(Object newValue) {
		Object oldValue = value;
		this.value = newValue;
		fireChangeEvent(IChangeEvent.CHANGE, oldValue, newValue);
	}

	public Object getValue() {
		return value;
	}

	public Class getValueType() {
		return type;
	}

}
