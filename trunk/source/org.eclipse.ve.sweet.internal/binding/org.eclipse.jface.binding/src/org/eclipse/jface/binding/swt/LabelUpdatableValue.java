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

package org.eclipse.jface.binding.swt;

import org.eclipse.jface.binding.UpdatableValue;
import org.eclipse.swt.widgets.Label;

public class LabelUpdatableValue extends UpdatableValue {

	private final Label label;

	public LabelUpdatableValue(Label label) {
		this.label = label;
	}

	public void setValue(Object value) {
		label.setText(value == null ? "" : value.toString());
	}

	public Object getValue() {
		return label.getText();
	}

	public Class getValueType() {
		return String.class;
	}
}
