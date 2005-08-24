/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.example.customwidget.prompter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;


public class MyCustomPrompter extends Composite {

	private Button button = null;
	private Text text = null;
	private Label label = null;
	private int type = DOTS;
	
	public final static int DOTS = 0;
	public final static int MORE = 1;
	public final static int OPEN = 2;
	
	public MyCustomPrompter(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
	private void initialize() {
		GridData gridData21 = new org.eclipse.swt.layout.GridData();
		GridData gridData3 = new GridData();
		GridData gridData2 = new GridData();
		GridLayout gridLayout1 = new GridLayout();
		label = new Label(this, SWT.NONE);
		text = new Text(this, SWT.BORDER);
		button = new Button(this, SWT.NONE);
		this.setLayout(gridLayout1);
		gridLayout1.numColumns = 3;
		button.setText("...");
		button.setLayoutData(gridData2);
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		text.setLayoutData(gridData3);
		label.setText("Label");
		label.setLayoutData(gridData21);
		gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		setSize(new org.eclipse.swt.graphics.Point(300,69));
	}
	
    public void addButtonSelectionListener(ButtonSelectionListener listener) {
    	
    }    
    public void removeButtonSelectionListener(ButtonSelectionListener listener) {

    	
    }
    public void setType (int type) {
    	switch (type) {
    	   case DOTS: button.setText("...");
    	   	break;
    	   case MORE: button.setText("More");
    	   	break;
    	   case OPEN: button.setText("Open");
    	   	break;
    	   default:
    	    throw new IllegalArgumentException("Value " + type + " must be one of 0, 1 or 2");
    	}
    }
    public int getType()  {
    	return type;
    }
    public void setText (String text) {
    	this.text.setText(text);
    }
    public String getText() {
    	return this.text.getText();
    }
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
