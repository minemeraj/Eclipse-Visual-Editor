package org.eclipse.ve.sweet.timeexample.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class WorkHeader extends Composite {

	private Label label = null;
	private Label label1 = null;
	private Label label2 = null;

	public WorkHeader(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		label = new Label(this, SWT.NONE);
		label.setBounds(new org.eclipse.swt.graphics.Rectangle(8,14,70,17));
		label.setText("Start Time");
		label1 = new Label(this, SWT.NONE);
		label1.setBounds(new org.eclipse.swt.graphics.Rectangle(90,14,72,17));
		label1.setText("End Time");
		label2 = new Label(this, SWT.NONE);
		label2.setBounds(new org.eclipse.swt.graphics.Rectangle(175,12,78,17));
		label2.setText("Description");
		setSize(new org.eclipse.swt.graphics.Point(300,55));
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
