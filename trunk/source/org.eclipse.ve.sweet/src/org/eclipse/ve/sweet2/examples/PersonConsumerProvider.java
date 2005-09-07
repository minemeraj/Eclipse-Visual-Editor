package org.eclipse.ve.sweet2.examples;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ve.sweet2.*;
import org.eclipse.ve.sweet2.ObjectDelegate.ChangeListener;


public class PersonConsumerProvider implements IElementContentProvider , IContentConsumer {
	
	private Viewer viewer;
	private ChangeListener listener;
	Person p;
	
	public Object getElement(Object input) {
		
		if(input instanceof Person){
			if(listener == null){
				listener = new ObjectDelegate.ChangeListener(){
					public void objectAdded(Object newObject) { }						
					public void objectRemove(Object oldObject) { }
					public void objectChanged(Object object, String propertyName) {
						if(object == p){
							viewer.refresh();
						}
					}
					
				};
				ObjectDelegate.addListener(Person.class,listener);				
			}
			p = (Person)input;			
			return p.getFirstName() + " " + p.getLastName();
		} else {
			return "";
		}
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	
		this.viewer = viewer;
	}

	public void setValue(Object aValue) {
		String stringValue = (String)aValue;
		int indexOfSpace = stringValue.indexOf(" ");
		String firstName = stringValue.substring(0,indexOfSpace);
		String lastName = stringValue.substring(indexOfSpace);
		p.setFirstName(firstName);
		p.setLastName(lastName);
		ObjectDelegate.refresh(p,null);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {		
	}

	public void ouputChanged(Object anObject) {	
		p = (Person)anObject;
	}

	public Class getType() {
		return String.class;
	}

	public Object getValue() {
		return getElement(p);
	}	

}
