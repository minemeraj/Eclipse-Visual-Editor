package org.eclipse.ve.sweet2.examples;

import java.beans.PropertyChangeListener;

import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.IContentConsumer;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.ObjectDelegate;

public class PersonContentConsumer implements IContentConsumer {
	
	private Person fPerson;

	public Object getValue() {
		return null;
	}

	public void setValue(Object aValue) {
		String stringValue = (String)aValue;
		int indexOfSpace = stringValue.indexOf(" ");
		String firstName = stringValue.substring(0,indexOfSpace);
		String lastName = stringValue.substring(indexOfSpace);
		fPerson.setFirstName(firstName);
		fPerson.setLastName(lastName);
		ObjectDelegate.refresh(fPerson,null);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {		
	}

	public void ouputChanged(Object anObject) {	
		fPerson = (Person)anObject;
	}

	public Class getType() {
		return String.class;
	}

}
