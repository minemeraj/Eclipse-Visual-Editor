
package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeListener;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public interface PropertyEditor {
	public Control createControl(Composite parent, int style);
	public void setValue(Object value);
	public void setJavaObjectInstanceValue(IJavaObjectInstance value);
	public Object getValue();
	public String getText();
	public String getJavaInitializationString();
	public void addPropertyChangeListener(PropertyChangeListener event);
	public void removePropertyChangeListener(PropertyChangeListener event);
}
