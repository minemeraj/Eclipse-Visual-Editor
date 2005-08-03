package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.ve.sweet.test.Person;

/**
 * This example builds on TestSweet2 but uses a generic PropertyProvider instead of a customer IContentProvider for the viewer
 */

public class TestSweet3 {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		final Person p = new Person("John Doe",35);
		
		shell.setLayout(new GridLayout(2,false));
		
		Label nameLabel = new Label(shell,SWT.NONE);
		nameLabel.setText("Name: ");
		TextViewer nameTextViewer = new TextViewer(shell,SWT.BORDER);
		nameTextViewer.setContentProvider(PropertyProvider.getPropertyProvider(p,"name"));
		nameTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label ageLabel = new Label(shell,SWT.NONE);
		ageLabel.setText("Age: ");
		TextViewer ageTextViewer = new TextViewer(shell,SWT.BORDER);
		ageTextViewer.setContentProvider(PropertyProvider.getPropertyProvider(p,"age"));
		ageTextViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label separator = new Label(shell,SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		separator.setLayoutData(data);
		
		shell.setSize(300,200);
		shell.open();
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		
	}

}
