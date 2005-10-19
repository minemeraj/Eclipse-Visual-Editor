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

import org.eclipse.jface.binding.ChangeEvent;
import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.IChangeListener;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.UpdatableValue;

public class AggregateUpdatableValue extends UpdatableValue {

	private IUpdatableValue[] updatableValues;

	private String delimiter;

	private IChangeListener listener = new IChangeListener() {
		public void handleChange(IChangeEvent changeEvent) {
			fireChangeEvent(ChangeEvent.CHANGE, null, null);
		}
	};

	public AggregateUpdatableValue(IUpdatableValue[] updatableValues,
			String delimiter) {
		this.updatableValues = updatableValues;
		this.delimiter = delimiter;
		for (int i = 0; i < updatableValues.length; i++) {
			updatableValues[i].addChangeListener(listener);
		}
	}

	public void setValue(Object value) {
		StringTokenizer tokenizer = new StringTokenizer((String) value,
				delimiter);
		for (int i = 0; i < updatableValues.length; i++) {
			if (tokenizer.hasMoreElements()) {
				updatableValues[i].setValue(tokenizer.nextElement());
			} else {
				updatableValues[i].setValue(null);
			}
		}
	}

	public Object getValue() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < updatableValues.length; i++) {
			if (i > 0 & i < updatableValues.length) {
				result.append(delimiter);
			}
			result.append(updatableValues[i].getValue());
		}
		return result.toString();
	}

	public Class getValueType() {
		return String.class;
	}

	public void dispose() {
		for (int i = 0; i < updatableValues.length; i++) {
			updatableValues[i].removeChangeListener(listener);
		}
		super.dispose();
	}

}
