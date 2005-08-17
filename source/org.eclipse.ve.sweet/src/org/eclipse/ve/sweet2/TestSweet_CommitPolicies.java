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
 * This example has three person objects each with a single binder
 * Each binder is used to create a set of editors - one input capable and one read only
 * the refreshPolicy is set differently on each set of editors to show that the commit policy works OK 
 */

public class TestSweet_CommitPolicies {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1,false));
		
		Text t = new Text(shell,SWT.READ_ONLY | SWT.WRAP);
		t.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		t.setText("This example uses different people objects and shows the update policy working for different states and editor types");
				
		Group modifyGroup = new Group(shell,SWT.NONE);
		modifyGroup.setLayout(new GridLayout(3,false));
		modifyGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		modifyGroup.setText("Modify");		
		createPersonFields(Editor.COMMIT_MODIFY,modifyGroup);
		
		Group focusGroup = new Group(shell,SWT.NONE);
		focusGroup.setLayout(new GridLayout(3,false));
		focusGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		focusGroup.setText("Focus");				
		createPersonFields(Editor.COMMIT_FOCUS,focusGroup);
		
		Group explicitGroup = new Group(shell,SWT.NONE);
		explicitGroup.setLayout(new GridLayout(3,false));
		explicitGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		explicitGroup.setText("Explicit");				
		createPersonFields(Editor.COMMIT_EXPLICIT,explicitGroup);
		
		shell.setSize(500,400);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
	}
	
	public static void createPersonFields(int updatePolicy, Composite parent){
		
		final Person p = new Person("John","Doe",35);
		final IObjectBinder personBinder = ObjectBinder.createObjectBinder(Person.class);
		personBinder.setValue(p);
		
		// NAME
		Label nameLabel = new Label(parent,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final TextEditor nameTextViewer = new TextEditor(parent,SWT.BORDER);
		nameTextViewer.setUpdatePolicy(updatePolicy);
		final IValueProvider nameBinder = personBinder.getPropertyProvider("firstName");		
		nameTextViewer.setContentProvider(nameBinder);
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final TextEditor nameTextViewer_2 = new TextEditor(parent,SWT.READ_ONLY);		
		nameTextViewer_2.setContentProvider(personBinder.getPropertyProvider("firstName"));
		nameTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// AGE
		Label ageLabel = new Label(parent,SWT.NONE);
		ageLabel.setText("age: ");	
		
		final SpinnerEditor ageSpinnerViewer = new SpinnerEditor(parent,SWT.BORDER);
		final IValueProvider ageBinder = personBinder.getPropertyProvider("age");
		ageSpinnerViewer.setContentProvider(ageBinder);
		ageSpinnerViewer.setUpdatePolicy(updatePolicy);		
		
		final TextEditor ageTextViewer_2 = new TextEditor(parent,SWT.READ_ONLY);
		ageTextViewer_2.setContentProvider(personBinder.getPropertyProvider("age"));
		ageTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Place another age viewer that is a text viewer to show it works with ints
		Label spacer = new Label(parent,SWT.NONE);
		final TextEditor ageTextViewer = new TextEditor(parent,SWT.BORDER);
		ageTextViewer.setContentProvider(personBinder.getPropertyProvider("age"));
		ageTextViewer.setUpdatePolicy(updatePolicy);		
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
				
		// If using explicit commit then create a button for this
		
		if(updatePolicy == Editor.COMMIT_EXPLICIT){
			Button commitButton = new Button(parent,SWT.PUSH);
			GridData data = new GridData();
			data.horizontalSpan = 2;
			commitButton.setLayoutData(data);
			commitButton.setText("Commit");
			Label separator = new Label(parent,SWT.NONE);
			commitButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					// Update the name
					Object newName = nameTextViewer.getText().getText();
					nameBinder.setValue(newName);
					// Update the age
					Object newAge = new Integer(ageSpinnerViewer.getSpinner().getSelection());
					ageBinder.setValue(newAge);					
				}				
			});
		}								
	}
}
