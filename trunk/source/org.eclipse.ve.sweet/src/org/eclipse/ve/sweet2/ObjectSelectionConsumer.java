package org.eclipse.ve.sweet2;

public class ObjectSelectionConsumer extends ObjectContentConsumer implements ISelectionConsumer {

	public ObjectSelectionConsumer(Object aSource, String aPropertyName) {
		super(aPropertyName);
		ouputChanged(aSource);
	}

	public ObjectSelectionConsumer(IObjectDelegate selectedPerson) {
		super(selectedPerson,null);
	}

	public Object getSource() {
		return fBinders[0];
	}
}
