package org.eclipse.ve.sweet2.testing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.Editor;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.IContentConsumer;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.IObjectDelegate;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.ObjectContentConsumer;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.ObjectContentProvider;
import org.eclipse.ve.sweet.fieldviewer.jface.internal.providers.ObjectDelegate;
import org.eclipse.ve.sweet2.SpinnerEditor;
import org.eclipse.ve.sweet2.TextEditor;
import org.eclipse.ve.sweet2.examples.Person;

/**
 * This example builds on TestSweet_CommitPolicies
 * It uses the same person instance for all of the editors to show that having
 * three separate ObjectBinders with the same Person value maintains signalling
 */

public class TestSweet_CommitPolicies {
	
	private static Person p;

	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1,false));
		
		Text t = new Text(shell,SWT.READ_ONLY | SWT.WRAP);
		t.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		t.setText("This example uses the same person object for each group of update policies with three separate object binders");
		Font font = new Font(display,"Helvetica",10,SWT.NONE);
		t.setFont(font);
		p = new Person("John","Doe",35);		
		
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
		
		final IObjectDelegate personBinder = ObjectDelegate.createObjectBinder(Person.class);
		personBinder.setValue(p);
		
		// To show name have two text editors
		Label nameLabel = new Label(parent,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final TextEditor nameTextViewer = new TextEditor(parent,SWT.BORDER);
		nameTextViewer.setUpdatePolicy(updatePolicy);
		
		nameTextViewer.setContentProvider(new ObjectContentProvider("firstName"));
		nameTextViewer.setInput(personBinder);
		final IContentConsumer nameConsumer = new ObjectContentConsumer("firstName");	
		nameTextViewer.setContentConsumer(nameConsumer);
		nameTextViewer.setOutput(personBinder);
		
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final TextEditor nameTextViewer_2 = new TextEditor(parent,SWT.READ_ONLY);		
		nameTextViewer_2.setContentProvider(new ObjectContentProvider("firstName"));
		nameTextViewer_2.setInput(personBinder);
		
		nameTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// For age have a spinner viewer on the left and a text editor read only on the right
		Label ageLabel = new Label(parent,SWT.NONE);
		ageLabel.setText("age: ");	
		
		final SpinnerEditor ageSpinnerViewer = new SpinnerEditor(parent,SWT.BORDER);
		ageSpinnerViewer.setUpdatePolicy(updatePolicy);		
		ageSpinnerViewer.setContentProvider(new ObjectContentProvider("age"));
		final IContentConsumer ageConsumer = new ObjectContentConsumer("age");
		ageSpinnerViewer.setContentConsumer(ageConsumer);
		ageSpinnerViewer.setInput(personBinder);
		ageSpinnerViewer.setOutput(personBinder);
		
		final TextEditor ageTextViewer_2 = new TextEditor(parent,SWT.READ_ONLY);
		ageTextViewer_2.setContentProvider(new ObjectContentProvider("age"));
		ageTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ageTextViewer_2.setInput(personBinder);
		
		// Place another age viewer that is a text viewer to show it works with ints
//		Label spacer = new Label(parent,SWT.NONE);
//		final TextEditor ageTextViewer = new TextEditor(parent,SWT.BORDER);
//		ageTextViewer.setContentProvider(personBinder.getPropertyProvider("age"));
//		ageTextViewer.setUpdatePolicy(updatePolicy);		
//		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
					
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
					nameConsumer.setValue(newName);
					// Update the age
					Object newAge = new Integer(ageSpinnerViewer.getSpinner().getSelection());
					ageConsumer.setValue(newAge);					
				}				
			});
		}								
	}
}
