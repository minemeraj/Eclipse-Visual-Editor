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
	private static IObjectDelegate personBinder;
	private static Person manager;
	private static IObjectDelegate managerBinder;

	public static void main(String[] args) {
		
		TextEditor.DEFAULT_COMMIT_POLICY = Editor.COMMIT_FOCUS;
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2,true));
		
		p = new Person("John","Doe",35);
		personBinder = ObjectDelegate.createObjectBinder(Person.class);
		personBinder.setValue(p);
		
		manager= new Person("Cheese","Big",50);
		p.setManager(manager);
		managerBinder = ObjectDelegate.createObjectBinder(Person.class);
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
		firstNameTextViewer.setContentProvider(new ObjectContentProvider("manager.firstName"));
		firstNameTextViewer.setContentConsumer(new ObjectContentConsumer("manager.firstName"));
		firstNameTextViewer.setInput(personBinder);

		firstNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		

		Label nameLabel_2 = new Label(personReadOnlyGroup,SWT.NONE);
		nameLabel_2.setText("Manager's First Name: ");		
		TextEditor firstNameTextViewer_2 = new TextEditor(personReadOnlyGroup,SWT.READ_ONLY); 
		firstNameTextViewer_2.setContentProvider(new ObjectContentProvider("manager.firstName"));
		firstNameTextViewer_2.setContentConsumer(new ObjectContentConsumer("manager.firstName"));
		firstNameTextViewer_2.setInput(personBinder);
		
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
	
	public static void addPersonDetailsTo(Composite parent, IObjectDelegate aPersonBinder, int styleBits){
		
		// First name
		Label nameLabel = new Label(parent,SWT.NONE);
		nameLabel.setText("First Name: ");		
		TextEditor firstNameTextViewer = new TextEditor(parent,styleBits); 
		firstNameTextViewer.setContentProvider(new ObjectContentProvider("firstName"));
		firstNameTextViewer.setContentConsumer(new ObjectContentConsumer("firstName"));
		firstNameTextViewer.setInput(aPersonBinder);
		firstNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Last name		
		Label lastNameLabel = new Label(parent,SWT.NONE);
		lastNameLabel.setText("Last Name: ");		
		TextEditor lastNameTextViewer = new TextEditor(parent,styleBits);
		lastNameTextViewer.setContentProvider(new ObjectContentProvider("lastName"));
		lastNameTextViewer.setContentConsumer(new ObjectContentConsumer("lastName"));
		lastNameTextViewer.setInput(aPersonBinder);
		lastNameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		// age
		Label ageLabel = new Label(parent,SWT.NONE);
		ageLabel.setText("age: ");	
		SpinnerEditor ageSpinnerViewer = new SpinnerEditor(parent,styleBits); 
		ageSpinnerViewer.setContentProvider(new ObjectContentProvider("age"));
		ageSpinnerViewer.setContentConsumer(new ObjectContentConsumer("age"));		
		ageSpinnerViewer.setInput(aPersonBinder);
		
	}
}
