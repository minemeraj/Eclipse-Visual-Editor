/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 14, 2005
 */
package com.ibm.jve.sample.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.ibm.jve.sample.core.UIPartBase;

/**
 * @author doconnor
 */
public class UILabelTextPart extends UIPartBase {

	/**
	 * @param parent
	 * @param title
	 */
	public UILabelTextPart(Composite parent, String title) {
		super(parent, title);
		createContents();
	}

	/**
	 * 
	 */
	private void createContents() {
		initializeContainer(2);
		createLabel();
		Text text = new Text(getContainer(), SWT.BORDER);
		GridData textData = new GridData();
		textData.grabExcessHorizontalSpace = true;
		textData.horizontalAlignment = GridData.FILL;
		
		text.setLayoutData(textData);
		
		
		setControls(new Control[]{text});
	}

}
