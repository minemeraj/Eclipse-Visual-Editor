package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.ve.sweet.test.Person;

public class TestSweet2 {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		final Person p = new Person("John Doe",35);
		
		shell.setLayout(new GridLayout(2,false));
		
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		TextViewer nameTextViewer = new TextViewer(shell,SWT.BORDER);
		nameTextViewer.setContentProvider(new IContentProvider(){
			public void dispose() { }
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { 
				((TextViewer)viewer).getText().setText(((Person)newInput).getName());
			}
		});
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(shell,SWT.NONE);
		ageLabel.setText("Age: ");
		TextViewer ageTextViewer = new TextViewer(shell,SWT.BORDER);
		ageTextViewer.setContentProvider(new IContentProvider(){
			public void dispose() { }
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { 
				((TextViewer)viewer).getText().setText(String.valueOf(((Person)newInput).getAge()));
			}
		});
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		nameTextViewer.setInput(p);
		ageTextViewer.setInput(p);		
		
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
	}

}
