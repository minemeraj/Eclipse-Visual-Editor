package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.Viewer;

public abstract class AbstractPropertyProvider implements IPropertyProvider {

	public Object getSource() {
		return null;
	}
	public void setSource(Object aSource) {		
	}

	public boolean isForProperty(String propertyName) {
		return false;
	}
	
	public void dispose() {		
	}


}
