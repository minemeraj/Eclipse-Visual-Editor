package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeListener;

public interface IObjectDelegate {

	void refresh(String propertyName);
	void setValue(Object aValue);
	Object getValue();
	void addPropertyChangeListener(PropertyChangeListener l);
	void removePropertyChangeListener(PropertyChangeListener l);
	Class getType();

}
