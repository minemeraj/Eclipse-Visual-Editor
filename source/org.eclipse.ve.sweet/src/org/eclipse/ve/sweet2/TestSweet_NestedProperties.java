package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.ve.sweet.testcase.Person;

/**
 * This example builds on TestSweet3 but instead of each contentProvider listening to a specific Person
 * this is a proxy object that allows the person to be changed and also notifies updates
 * The example shows the person being changed by duplicating the GUI text entry
 * The example shows the three different commit values that are possible
 */

public class TestSweet_NestedProperties {
	
	private static Person p;
	private static IObjectBinder personBinder;
	private static Person manager;
	private static IObjectBinder managerBinder;

	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1,false));
		
		p = new Person("John","Doe",35);
		personBinder = ObjectBinder.createObjectBinder(Person.class);
		personBinder.setValue(p);
		
		manager= new Person("Cheese","Big",50);
		p.setManager(manager);
		managerBinder = ObjectBinder.createObjectBinder(Person.class);
		managerBinder.setValue(manager);
		
		Group personGroup = new Group(shell,SWT.NONE);
		personGroup.setText("Person");
		personGroup.setLayout(new GridLayout(2,false));
		personGroup.setLayoutData(new GridData(GridData.FILL_BOTH));		
		
		addPersonDetailsTo(personGroup,personBinder);
		
		// Manager's first name
		Label nameLabel = new Label(personGroup,SWT.NONE);
		nameLabel.setText("Manager's First Name: ");		
		TextEditor firstNameTextViewer = new TextEditor(personGroup,SWT.BORDER); 
		firstNameTextViewer.setContentProvider(personBinder.getPropertyProvider("manager.firstName"));
		firstNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		Group managerGroup = new Group(shell,SWT.NONE);
		managerGroup.setText("Manager");		
		managerGroup.setLayout(new GridLayout(2,false));
		managerGroup.setLayoutData(new GridData(GridData.FILL_BOTH));				
		
		addPersonDetailsTo(managerGroup,managerBinder);		
		
		shell.setSize(400,300);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
	}
	
	public static void addPersonDetailsTo(Composite parent, IObjectBinder aPersonBinder){
		
		// First name
		Label nameLabel = new Label(parent,SWT.NONE);
		nameLabel.setText("First Name: ");		
		TextEditor firstNameTextViewer = new TextEditor(parent,SWT.BORDER); 
		firstNameTextViewer.setContentProvider(aPersonBinder.getPropertyProvider("firstName"));
		firstNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Last name		
		Label lastNameLabel = new Label(parent,SWT.NONE);
		lastNameLabel.setText("Last Name: ");		
		TextEditor lastNameTextViewer = new TextEditor(parent,SWT.BORDER);
		lastNameTextViewer.setContentProvider(aPersonBinder.getPropertyProvider("lastName"));
		lastNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		// age
		Label ageLabel = new Label(parent,SWT.NONE);
		ageLabel.setText("age: ");	
		SpinnerEditor ageSpinnerViewer = new SpinnerEditor(parent,SWT.BORDER); 
		ageSpinnerViewer.setContentProvider(aPersonBinder.getPropertyProvider("age"));
		
	}
}
