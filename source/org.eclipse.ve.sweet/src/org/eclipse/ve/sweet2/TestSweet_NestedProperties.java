package org.eclipse.ve.sweet2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */

public class TestSweet_NestedProperties {
	
	private static Person p;
	private static IObjectBinder personBinder;
	private static Person manager;
	private static IObjectBinder managerBinder;

	public static void main(String[] args) {
		
		TextEditor.DEFAULT_COMMIT_POLICY = Editor.COMMIT_FOCUS;
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2,true));
		
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
		personGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));	
		
		addPersonDetailsTo(personGroup,personBinder,SWT.BORDER);
		
		Group personReadOnlyGroup = new Group(shell,SWT.NONE);
		personReadOnlyGroup.setText("Person");
		personReadOnlyGroup.setLayout(new GridLayout(2,false));
		personReadOnlyGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		addPersonDetailsTo(personReadOnlyGroup,personBinder,SWT.READ_ONLY);		
		
		// Manager's first name
		Label nameLabel = new Label(personGroup,SWT.NONE);
		nameLabel.setText("Manager's First Name: ");		
		TextEditor firstNameTextViewer = new TextEditor(personGroup,SWT.BORDER); 
		firstNameTextViewer.setContentProvider(personBinder.getPropertyProvider("manager.firstName"));
		firstNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		

		Label nameLabel_2 = new Label(personReadOnlyGroup,SWT.NONE);
		nameLabel_2.setText("Manager's First Name: ");		
		TextEditor firstNameTextViewer_2 = new TextEditor(personReadOnlyGroup,SWT.READ_ONLY); 
		firstNameTextViewer_2.setContentProvider(personBinder.getPropertyProvider("manager.firstName"));
		firstNameTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));				
		
		Group managerGroup = new Group(shell,SWT.NONE);
		managerGroup.setText("Manager");		
		managerGroup.setLayout(new GridLayout(2,false));
		managerGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		addPersonDetailsTo(managerGroup,managerBinder,SWT.BORDER);	
		
		Group managerReadOnlyGroup = new Group(shell,SWT.NONE);
		managerReadOnlyGroup.setText("Manager");
		managerReadOnlyGroup.setLayout(new GridLayout(2,false));
		managerReadOnlyGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		addPersonDetailsTo(managerReadOnlyGroup,managerBinder,SWT.READ_ONLY);		
		
		shell.setSize(600,300);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
	}
	
	public static void addPersonDetailsTo(Composite parent, IObjectBinder aPersonBinder, int styleBits){
		
		// First name
		Label nameLabel = new Label(parent,SWT.NONE);
		nameLabel.setText("First Name: ");		
		TextEditor firstNameTextViewer = new TextEditor(parent,styleBits); 
		firstNameTextViewer.setContentProvider(aPersonBinder.getPropertyProvider("firstName"));
		firstNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Last name		
		Label lastNameLabel = new Label(parent,SWT.NONE);
		lastNameLabel.setText("Last Name: ");		
		TextEditor lastNameTextViewer = new TextEditor(parent,styleBits);
		lastNameTextViewer.setContentProvider(aPersonBinder.getPropertyProvider("lastName"));
		lastNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		// age
		Label ageLabel = new Label(parent,SWT.NONE);
		ageLabel.setText("age: ");	
		SpinnerEditor ageSpinnerViewer = new SpinnerEditor(parent,styleBits); 
		ageSpinnerViewer.setContentProvider(aPersonBinder.getPropertyProvider("age"));
		
	}
}
