package org.eclipse.jface.binding;

public abstract class ConditionalUpdatableValue extends UpdatableValue {

	private final IUpdatableValue innerUpdatableValue;

	IChangeListener changeListener = new IChangeListener() {
		public void handleChange(IChangeEvent changeEvent) {
			fireChangeEvent(IChangeEvent.CHANGE, null, null);
		}
	};

	public ConditionalUpdatableValue(IUpdatableValue innerUpdatableValue) {
		this.innerUpdatableValue = innerUpdatableValue;
		innerUpdatableValue.addChangeListener(changeListener);
	}

	public void setValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public Object getValue() {
		Object currentValue = innerUpdatableValue.getValue();
		return new Boolean(compute(currentValue));
	}

	abstract protected boolean compute(Object currentValue);

	public Class getValueType() {
		return Boolean.class;
	}
	
	public void dispose() {
		super.dispose();
		innerUpdatableValue.removeChangeListener(changeListener);
	}

}
