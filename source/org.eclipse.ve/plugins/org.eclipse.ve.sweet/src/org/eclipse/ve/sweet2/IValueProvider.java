package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;

public interface IValueProvider extends IContentProvider {
	
	public interface ChangeListener{
		public void valueChanged(Object oldValue, Object newValue);
	}
	
	Object getValue();
	void setValue(Object value);
	boolean canSetValue();

}
