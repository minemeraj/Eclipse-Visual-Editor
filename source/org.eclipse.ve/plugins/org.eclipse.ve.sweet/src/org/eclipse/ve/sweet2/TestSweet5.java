package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.ve.sweet.test.Person;

/**
 * This example builds on TestSweet3 but instead of each contentProvider listening to a specific Person
 * this is a proxy object that allows the person to be changed and also notifies updates
 * The example shows the person being changed by duplicating the GUI text entry
 * The example TestSweet5 illustrates this using a list of people
 */

public class TestSweet5 {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		final Person p = new Person("John Doe",35);
		IObjectBinder personBinder = ObjectBinder.createObjectBinder(p);
		
		shell.setLayout(new GridLayout(2,false));
		
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		
		TextViewer nameTextViewer = new TextViewer(shell,SWT.BORDER);
		nameTextViewer.setContentProvider(personBinder.getPropertyProvider("name"));
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(shell,SWT.NONE);
		ageLabel.setText("Age: ");
		TextViewer ageTextViewer = new TextViewer(shell,SWT.BORDER);
		ageTextViewer.setContentProvider(personBinder.getPropertyProvider("age"));
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label separator = new Label(shell,SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		separator.setLayoutData(data);
		
		// Have a copy of the name field to show that notification works between the two
		Label nameLabel_2 = new Label(shell,SWT.NONE);
		nameLabel_2.setText("Name: ");		
		TextViewer nameTextViewer_2 = new TextViewer(shell,SWT.BORDER);
		nameTextViewer_2.setContentProvider(personBinder.getPropertyProvider("name"));
		nameTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
	}

}
