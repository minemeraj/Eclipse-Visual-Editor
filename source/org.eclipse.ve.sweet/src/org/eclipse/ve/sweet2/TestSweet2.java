package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.ve.sweet.test.Person;

/**
 * This example demonstrates a simple TextViewer with a custom content provider for age and name
 */

public class TestSweet2 {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		final Person p = new Person("John Doe",35);
		
		shell.setLayout(new GridLayout(2,false));
		
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		final TextViewer nameTextViewer = new TextViewer(shell,SWT.BORDER);
		nameTextViewer.setContentProvider(new AbstractPropertyProvider(){
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { 
				((TextViewer)viewer).getText().setText(((Person)newInput).getName());
			}
			public Object getValue() {
				return p.getName();			}
			public void setValue(Object value) {
				p.setName((String) value);
			}
			public void refreshUI() {
				nameTextViewer.getText().setText(p.getName());
			}
			public void refreshDomain() {
				p.setName(nameTextViewer.getText().getText());
			}
			public boolean isForProperty(String propertyName) {
				return "name".equals(propertyName);
			}
		});
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(shell,SWT.NONE);
		ageLabel.setText("Age: ");
		final TextViewer ageTextViewer = new TextViewer(shell,SWT.BORDER);
		ageTextViewer.setContentProvider(new AbstractPropertyProvider(){
			public void dispose() { }
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { 
				((TextViewer)viewer).getText().setText(String.valueOf(((Person)newInput).getAge()));
			}
			public Object getValue() {
				return new Integer(p.getAge());
			}
			public void setValue(Object value) {
				p.setAge( ((Integer)value).intValue());
			}
			public void refreshUI() {
				ageTextViewer.getText().setText(String.valueOf(p.getAge()));
			}
			public void refreshDomain() {
				p.setAge(new Integer(ageTextViewer.getText().getText()).intValue());
			}
			public boolean isForProperty(String propertyName) {
				return "age".equals("propertyName");
			}
		});
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		nameTextViewer.setInput(p);
		ageTextViewer.setInput(p);		
		
		shell.setSize(300,200);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
	}

}
