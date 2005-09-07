package org.eclipse.ve.sweet2;

public class ObjectSelectionService extends ObjectContentConsumer implements ISelectionService {

	public ObjectSelectionService(Object aSource, String aPropertyName) {
		super(aPropertyName);
		ouputChanged(aSource);
	}

	public Object getSource() {
		return fBinders[0];
	}
}
