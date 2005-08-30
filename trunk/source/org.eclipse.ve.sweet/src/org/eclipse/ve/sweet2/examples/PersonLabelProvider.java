package org.eclipse.ve.sweet2.examples;

import org.eclipse.jface.viewers.LabelProvider;

public class PersonLabelProvider extends LabelProvider{

	public String getText(Object element) {
		Person p = (Person)element;
		return p.getFirstName() + " " + p.getLastName() + " -  " + p.getAge();
	}	

}
