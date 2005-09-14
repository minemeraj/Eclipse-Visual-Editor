package org.eclipse.ve.sweet.timeexample.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Display;

public class WorkRow extends Composite {

	private Text startTime = null;
	private Text endtime = null;
	private Text description = null;

	public WorkRow(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		startTime = new Text(this, SWT.NONE);
		startTime.setBounds(new org.eclipse.swt.graphics.Rectangle(9,11,25,25));
		endtime = new Text(this, SWT.NONE);
		endtime.setBounds(new org.eclipse.swt.graphics.Rectangle(42,11,28,25));
		description = new Text(this, SWT.NONE);
		description.setBounds(new org.eclipse.swt.graphics.Rectangle(77,11,76,25));
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		setSize(new org.eclipse.swt.graphics.Point(165,51));
	}

	public Text getDescription() {
		return description;
	}

	public Text getEndTime() {
		return endtime;
	}

	public Text getStartTime() {
		return startTime;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
