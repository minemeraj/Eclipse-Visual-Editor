package org.eclipse.ve.sweet2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
		
		Text t = new Text(shell,SWT.READ_ONLY | SWT.WRAP);
		GridData data = new GridData(GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		t.setLayoutData(data);
		t.setText("This example uses the same person object for each group of update policies with three separate object binders");
		
		p = new Person("John","Doe",35);		
		final IObjectBinder personBinder = ObjectBinder.createObjectBinder(Person.class);
		personBinder.setValue(p);		
		
		// NAME
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final TextEditor nameTextViewer = new TextEditor(shell,SWT.BORDER);
		nameTextViewer.setContentProvider(new PropertyContentProvider("firstName"));
		nameTextViewer.setContentConsumer(personBinder.getContentConsumer("firstName"));
		nameTextViewer.setInput(personBinder);
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		// NAME
		Label nameLabel_2 = new Label(shell,SWT.NONE);
		nameLabel_2.setText("Name: ");		
		
		final TextEditor nameTextViewer_2 = new TextEditor(shell,SWT.BORDER);
		nameTextViewer_2.setContentProvider(new PropertyContentProvider("firstName"));
		nameTextViewer.setContentConsumer(personBinder.getContentConsumer("firstName"));
		nameTextViewer_2.setInput(personBinder);		
		
		nameTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		shell.setSize(500,400);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
	}
			
}