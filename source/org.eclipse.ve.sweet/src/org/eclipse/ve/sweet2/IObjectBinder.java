package org.eclipse.ve.sweet2;


public interface IObjectBinder {

	IValueProvider getPropertyProvider(String propertyName);
	void setSource(Object aSource);

}
