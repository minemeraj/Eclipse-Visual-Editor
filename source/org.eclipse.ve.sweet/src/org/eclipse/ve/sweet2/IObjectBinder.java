package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeListener;


public interface IObjectBinder extends IValue {

	IValueProvider getPropertyProvider(String propertyName);
	void addPropertyChangeListener(PropertyChangeListener l);
	void refresh(String propertyName);

}
