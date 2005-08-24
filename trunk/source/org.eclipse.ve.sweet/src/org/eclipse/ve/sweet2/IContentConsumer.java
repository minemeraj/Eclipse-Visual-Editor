package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeListener;

public interface IContentConsumer {
	
	public Object getValue();
	public void setValue(Object aValue);
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);	
	public Class getType();

}
