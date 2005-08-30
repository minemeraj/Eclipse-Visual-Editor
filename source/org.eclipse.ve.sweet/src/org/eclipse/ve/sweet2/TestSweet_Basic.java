package org.eclipse.ve.sweet2;

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
import org.eclipse.ve.sweet2.examples.Person;

/**
 * This example builds on TestSweet_CommitPolicies
 * It uses the same person instance for all of the editors to show that having
 * three separate ObjectBinders with the same Person value maintains signalling
 */

public class TestSweet_Basic {
	
	private static Person p;

	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2,false));
		
		TextEditor.DEFAULT_COMMIT_POLICY = Editor.COMMIT_MODIFY;
		
		Text t = new Text(shell,SWT.READ_ONLY | SWT.WRAP);
		GridData data = new GridData(GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		t.setLayoutData(data);
		t.setText("This example shows two TextEditors and SpinnerEditors, each bound to the firstName and age property of the same Person");
		Font font = new Font(display,"Helvetica",10,SWT.NONE);
		t.setFont(font);
		
		p = new Person("John","Doe",35);		
		final IObjectDelegate personBinder = ObjectDelegate.createObjectBinder(Person.class);
		personBinder.setValue(p);		
		
		// NAME
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		
		TextEditor nameTextEditor = new TextEditor(shell,SWT.BORDER);
		nameTextEditor.setContentProvider(new ObjectContentProvider("firstName"));
		nameTextEditor.setContentConsumer(new ObjectContentConsumer("firstName"));
		nameTextEditor.setInput(personBinder);
		nameTextEditor.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		// AGE
		Label ageLabel = new Label(shell,SWT.NONE);
		ageLabel.setText("Age: ");
		
		SpinnerEditor ageSpinnerEditor = new SpinnerEditor(shell,SWT.BORDER);
		ageSpinnerEditor.setContentProvider(new ObjectContentProvider("age"));
		ageSpinnerEditor.setContentConsumer(new ObjectContentConsumer("age"));
		ageSpinnerEditor.setInput(personBinder);
				
		
		// NAME
		Label nameLabel_2 = new Label(shell,SWT.NONE);
		nameLabel_2.setText("Name: ");		
		
		final TextEditor nameTextEditor_2 = new TextEditor(shell,SWT.BORDER);
		nameTextEditor_2.setContentProvider(new ObjectContentProvider("firstName"));
		nameTextEditor_2.setContentConsumer(new ObjectContentConsumer("firstName"));
		nameTextEditor_2.setInput(personBinder);		
		
		nameTextEditor_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
//		 AGE
		Label ageLabel_2 = new Label(shell,SWT.NONE);
		ageLabel_2.setText("Age: ");
		
		SpinnerEditor ageSpinnerEditor_2 = new SpinnerEditor(shell,SWT.BORDER);
		ageSpinnerEditor_2.setContentProvider(new ObjectContentProvider("age"));
		ageSpinnerEditor_2.setContentConsumer(new ObjectContentConsumer("age"));
		ageSpinnerEditor_2.setInput(personBinder);	
		ageSpinnerEditor_2.setOutput(personBinder);
		
		shell.setSize(500,400);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
	}
			
}