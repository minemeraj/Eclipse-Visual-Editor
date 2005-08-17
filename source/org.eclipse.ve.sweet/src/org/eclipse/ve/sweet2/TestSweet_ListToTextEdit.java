package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.sweet.test.Person;

/**
 * This example shows a list of people and when one is selected it is editable within text fields showing the selected person
 */

public class TestSweet_ListToTextEdit {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
				
		shell.setLayout(new GridLayout(2,true));
		
		ListViewer listViewer = new ListViewer(shell,SWT.BORDER);
		GridData table_data = new GridData(GridData.FILL_HORIZONTAL);
		table_data.heightHint = 100;
		listViewer.getControl().setLayoutData(table_data);
		listViewer.setContentProvider(new IStructuredContentProvider(){
			public Object[] getElements(Object inputElement) {
				return new Person[]{
					new Person("John Doe",35),
					new Person("Jill Smith",25),
					new Person("Chris Cringle",75)
				};
			}
			public void dispose() {				
			}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});
		listViewer.setLabelProvider(new LabelProvider(){
			public String getText(Object element) {
				Person p = (Person)element;
				return p.getName() + "  -  " + p.getAge();
			}
		});
		listViewer.setInput("Dummy");

		Composite c = new Composite(shell,SWT.NONE);
		c.setLayout(new GridLayout(2,false));
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label nameLabel = new Label(c,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final IObjectBinder personBinder = ObjectBinder.createObjectBinder(Person.class);
		
		TextEditor nameTextViewer = new TextEditor(c,SWT.BORDER);
		nameTextViewer.setContentProvider(personBinder.getPropertyProvider("name"));
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(c,SWT.NONE);
		ageLabel.setText("Age: ");
		TextEditor ageTextViewer = new TextEditor(c,SWT.BORDER);
		ageTextViewer.setContentProvider(personBinder.getPropertyProvider("age"));
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		listViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				personBinder.setValue(((IStructuredSelection)event.getSelection()).getFirstElement());
			}
		});
				
		shell.setSize(500,200);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
	}

} 
