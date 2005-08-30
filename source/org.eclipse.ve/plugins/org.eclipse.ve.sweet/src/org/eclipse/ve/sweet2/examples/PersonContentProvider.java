package org.eclipse.ve.sweet2.examples;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ve.sweet2.IElementContentProvider;

public class PersonContentProvider implements IElementContentProvider {

	public Object getElement(Object input) {
		if(input instanceof Person){
			Person p = (Person)input;
			return p.getFirstName() + " " + p.getLastName();
		} else {
			return "";
		}
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {		
	}

}
