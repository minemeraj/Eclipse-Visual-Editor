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
import org.eclipse.ve.sweet.test.Person;

/**
 * This example builds on TestSweet3 but instead of each contentProvider listening to a specific Person
 * this is a proxy object that allows the person to be changed and also notifies updates
 * The example shows the person being changed by duplicating the GUI text entry
 * The example shows the three different commit values that are possible
 */

public class TestSweet4 {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		final Person p = new Person("John Doe",35);
		final IObjectBinder personBinder = ObjectBinder.createObjectBinder(p);
//		personBinder.setCommitPolicy(IObjectBinder.COMMIT_MODIFY);		
//		personBinder.setCommitPolicy(IObjectBinder.COMMIT_FOCUS);
		personBinder.setCommitPolicy(IObjectBinder.COMMIT_EXPLICIT);
		
		shell.setLayout(new GridLayout(2,false));
		
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final TextViewer nameTextViewer = new TextViewer(shell,SWT.BORDER);
		nameTextViewer.setContentProvider(personBinder.getPropertyProvider("name"));
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(shell,SWT.NONE);
		ageLabel.setText("Age: ");
		final TextViewer ageTextViewer = new TextViewer(shell,SWT.BORDER);
		ageTextViewer.setContentProvider(personBinder.getPropertyProvider("age"));
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// If using explicit commit then create a button for this
		if(personBinder.getCommitPolicy() == IObjectBinder.COMMIT_EXPLICIT){
			Button commitButton = new Button(shell,SWT.PUSH);
			commitButton.setText("Commit");
			Label separator = new Label(shell,SWT.NONE);
			commitButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					((IPropertyProvider)nameTextViewer.getContentProvider()).refreshDomain();
					((IPropertyProvider)ageTextViewer.getContentProvider()).refreshDomain();					
				}				
			});
		}
		
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
		
		// Have a copy of the age field to show that notification works between the two
		Label ageLabel_2 = new Label(shell,SWT.NONE);
		ageLabel_2.setText("Age: ");
		TextViewer ageTextViewer_2 = new TextViewer(shell,SWT.BORDER);
		ageTextViewer_2.setContentProvider(personBinder.getPropertyProvider("age"));
		ageTextViewer_2.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
	}

}
