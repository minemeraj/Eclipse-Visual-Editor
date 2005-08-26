package org.eclipse.ve.sweet2;

import java.util.List;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
/**
 * This example shows a list of people and when one is selected it is editable within text fields showing the selected person
 */

public class TestSweet_ListToTextEdit {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
				
		shell.setLayout(new GridLayout(2,true));
		
		ListViewer listViewer = new ListViewer(shell,SWT.BORDER);
		GridData table_data = new GridData(GridData.FILL_BOTH);
		table_data.heightHint = 150;
		listViewer.getControl().setLayoutData(table_data);
		
		IStructuredContentProvider listContentProvider = new IStructuredContentProvider(){
			public Object[] getElements(Object inputElement) {
				return ((List)inputElement).toArray();
			}
			public void dispose() {				
			}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		};		
		ILabelProvider personLabelProvider = new LabelProvider(){
			public String getText(Object element) {
				Person p = (Person)element;
				return p.getFirstName() + " " + p.getLastName() + " -  " + p.getAge();
			}
		};
		
		listViewer.setContentProvider(listContentProvider);
		listViewer.setLabelProvider(personLabelProvider);
		listViewer.setInput(Person.getSampleData());

		Composite c = new Composite(shell,SWT.NONE);
		c.setLayout(new GridLayout(2,false));
		c.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label nameLabel = new Label(c,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final IObjectBinder personBinder = ObjectBinder.createObjectBinder(Person.class);
		
		TextEditor nameTextViewer = new TextEditor(c,SWT.BORDER);
		nameTextViewer.setContentProvider(new PropertyContentProvider("firstName"));
		nameTextViewer.setContentConsumer(new PropertyContentConsumer("firstName"));
		nameTextViewer.setInput(personBinder);
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(c,SWT.NONE);
		ageLabel.setText("Age: ");
		
		SpinnerEditor ageSpinnerEditor = new SpinnerEditor(c,SWT.BORDER);
		ageSpinnerEditor.setContentProvider(new PropertyContentProvider("age"));
		ageSpinnerEditor.setContentConsumer(new PropertyContentConsumer("age"));
		ageSpinnerEditor.setInput(personBinder);
		
		Label managerLabel = new Label(c,SWT.NONE);
		managerLabel.setText("Manager: ");		
		
		ListEditor managerListEditor = new ListEditor(c,SWT.BORDER);
		managerListEditor.setContentProvider(listContentProvider);
		managerListEditor.setLabelProvider(personLabelProvider);
		managerListEditor.setContentConsumer(personBinder.getContentConsumer("manager"));
		managerListEditor.setInput(Person.getSampleData());
		
		managerListEditor.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
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
