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
package org.eclipse.ve.example.customwidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;
public class LabelDialogContent extends Composite {

	private Text text = null;

	private Button hello = null;

	private Button goodbye = null;

	private String string;    //  @jve:decl-index=0

	private Label label = null;
	private Button custom = null;
	public LabelDialogContent(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		GridLayout gridLayout3 = new GridLayout();
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		GridData gridData1 = new GridData();
		text = new Text(this, SWT.BORDER);
		custom = new Button(this, SWT.RADIO);
		text.setText(getString());
		hello = new Button(this, SWT.RADIO);
		goodbye = new Button(this, SWT.RADIO);
		label = new Label(this, SWT.NONE);
		this.setLayout(gridLayout3);
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalSpan = 2;
		text.setLayoutData(gridData1);
		hello.setText("Hello");
		hello.setLayoutData(gridData4);
		goodbye.setText("Goodbye");
		goodbye.setLayoutData(gridData5);
		label.setLayoutData(gridData2);
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.horizontalSpan = 2;
		gridLayout3.numColumns = 2;
		gridLayout3.makeColumnsEqualWidth = true;
		gridData4.horizontalSpan = 2;
		gridData5.horizontalSpan = 2;
		custom.setText("Custom");
		setSize(new org.eclipse.swt.graphics.Point(285,145));
		text.addKeyListener(new org.eclipse.swt.events.KeyAdapter() { 
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) { 
				Control [] children = custom.getParent().getChildren();
				for (int i=0; i<children.length; i++) {
					Control child = children [i];
					if (child != custom &&
						child instanceof Button && (child.getStyle () & SWT.RADIO) != 0) { 
						((Button)child).setSelection (false);
					}
				}
				custom.setSelection(true);
			}
		});
		text.addModifyListener(new org.eclipse.swt.events.ModifyListener() { 
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {    
				string = text.getText();
			}
		});
		hello.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {   
				if (hello.getSelection())
				   text.setText("Hello");
			}
		});
		goodbye.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (goodbye.getSelection())
				   text.setText("Goodbye");
			}
		});
	}

	/**
	 * @return Returns the string.
	 */
	public String getString() {
		if (string==null)
			string = new String();		
		return string;
	}

	/**
	 * @param string
	 *            The string to set.
	 */
	public void setString(String string) {
		this.string = string ;
		if (text!=null && !text.isDisposed())
		    text.setText(string);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
