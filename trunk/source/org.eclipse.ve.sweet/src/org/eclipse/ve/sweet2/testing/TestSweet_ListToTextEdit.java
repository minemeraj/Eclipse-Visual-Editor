package org.eclipse.ve.sweet2.testing;


import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ve.sweet2.*;
import org.eclipse.ve.sweet2.examples.*;
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
		
		IStructuredContentProvider listContentProvider = new ListContentProvider();		
		ILabelProvider personLabelProvider = new PersonLabelProvider();
		
		listViewer.setContentProvider(listContentProvider);
		listViewer.setLabelProvider(personLabelProvider);
		listViewer.setInput(Person.getSampleData());

		Composite c = new Composite(shell,SWT.NONE);
		c.setLayout(new GridLayout(2,false));
		c.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label nameLabel = new Label(c,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final IObjectDelegate personBinder = ObjectDelegate.createObjectBinder(Person.class);
		
		TextEditor nameTextViewer = new TextEditor(c,SWT.BORDER);
		nameTextViewer.setContentProvider(new ObjectContentProvider("firstName"));
		nameTextViewer.setContentConsumer(new ObjectContentConsumer("firstName"));
		nameTextViewer.setInput(personBinder);
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(c,SWT.NONE);
		ageLabel.setText("Age: ");
		
		SpinnerEditor ageSpinnerEditor = new SpinnerEditor(c,SWT.BORDER);
		ageSpinnerEditor.setContentProvider(new ObjectContentProvider("age"));
		ageSpinnerEditor.setContentConsumer(new ObjectContentConsumer("age"));
		ageSpinnerEditor.setInput(personBinder);
		
		Label managerLabel = new Label(c,SWT.NONE);
		managerLabel.setText("Manager: ");		
		
		ListEditor managerListEditor = new ListEditor(c,SWT.BORDER);
		managerListEditor.setContentProvider(listContentProvider);
		managerListEditor.setLabelProvider(personLabelProvider);
		managerListEditor.setContentConsumer(new ObjectContentConsumer("manager"));		
		managerListEditor.setInput(Person.getSampleData());
		managerListEditor.setOutput(personBinder);
		
		managerListEditor.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(c,SWT.NONE);
		
		ComboEditor comboListEditor = new ComboEditor(c,SWT.BORDER);
		comboListEditor.setContentProvider(listContentProvider);
		comboListEditor.setLabelProvider(personLabelProvider);
		comboListEditor.setContentConsumer(new ObjectContentConsumer("manager"));
		comboListEditor.setInput(Person.getSampleData());
		comboListEditor.setOutput(personBinder);
		
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
