package org.eclipse.ve.sweet.timeexample.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WorkdayRow extends Composite {

	private Label label = null;
	private Text date = null;
	private Label label1 = null;
	private Text description = null;
	
	public WorkdayRow(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		label = new Label(this, SWT.NONE);
		label.setText("Date:");
		date = new Text(this, SWT.BORDER);
		date.setLayoutData(gridData);
		label1 = new Label(this, SWT.NONE);
		label1.setText("Description:");
		description = new Text(this, SWT.BORDER);
		description.setLayoutData(gridData1);
		this.setLayout(gridLayout);
		setSize(new org.eclipse.swt.graphics.Point(465,74));
	}

	public Text getDate() {
		return date;
	}

	public Text getDescription() {
		return description;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
