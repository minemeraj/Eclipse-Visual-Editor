package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.ve.sweet.test.Person;

/**
 * This example shows a list of people and when one is selected it is editable within text fields showing the selected person
 */

public class TestSweet5 {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
				
		shell.setLayout(new GridLayout(2,false));
		
		ListViewer listViewer = new ListViewer(shell,SWT.BORDER);
		GridData table_data = new GridData(GridData.FILL_HORIZONTAL);
		table_data.horizontalSpan = 2;
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
		listViewer.setLabelProvider(new ILabelProvider(){
			public Image getImage(Object element) {
				return null;
			}
			public String getText(Object element) {
				Person p = (Person)element;
				return p.getName() + "  -  " + p.getAge();
			}
			public void addListener(ILabelProviderListener listener) {				
			}
			public void dispose() {				
			}
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			public void removeListener(ILabelProviderListener listener) {				
			}
		});
		listViewer.setInput("Dummy");

		Label separator = new Label(shell,SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sep_data = new GridData(GridData.FILL_HORIZONTAL);
		sep_data.horizontalSpan = 2;
		separator.setLayoutData(sep_data);		
		
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		
		final IObjectBinder personBinder = ObjectBinder.createObjectBinder(Person.class);
		
		TextViewer nameTextViewer = new TextViewer(shell,SWT.BORDER);
		nameTextViewer.setContentProvider(personBinder.getPropertyProvider("name"));
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(shell,SWT.NONE);
		ageLabel.setText("Age: ");
		TextViewer ageTextViewer = new TextViewer(shell,SWT.BORDER);
		ageTextViewer.setContentProvider(personBinder.getPropertyProvider("age"));
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		listViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				personBinder.setSource(((IStructuredSelection)event.getSelection()).getFirstElement());
			}
		});
				
		shell.setSize(300,300);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
	}

} 
