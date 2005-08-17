package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeListener;

public interface IObjectBinder extends IValue {

	void refresh(String propertyName);
	IValueProvider getPropertyProvider(String string);

}
