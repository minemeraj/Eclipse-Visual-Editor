package org.eclipse.jface.binding.internal.swt;

import org.eclipse.jface.binding.UpdatableValue;
import org.eclipse.swt.widgets.Control;

public class ControlUpdatableValue extends UpdatableValue {

	private final Control control;

	public ControlUpdatableValue(Control control, String attribute) {
		this.control = control;
		if (!attribute.equals("enabled")) {
			throw new IllegalArgumentException();
		}
	}

	public void setValue(Object value) {
		control.setEnabled(((Boolean) value).booleanValue());
	}

	public Object getValue() {
		return new Boolean(control.getEnabled());
	}

	public Class getValueType() {
		return Boolean.class;
	}

}
