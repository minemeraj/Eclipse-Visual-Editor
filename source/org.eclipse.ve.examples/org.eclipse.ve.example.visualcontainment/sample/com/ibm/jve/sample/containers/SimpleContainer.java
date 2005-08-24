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
package com.ibm.jve.sample.containers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.ibm.jve.sample.components.UIDropDownComboPart;
import com.ibm.jve.sample.components.UILabelTextPart;
import com.ibm.jve.sample.core.UIContainer;

/**
 * @author doconnor
 */
public class SimpleContainer extends UIContainer {
	private Composite parent = null;

	/**
	 * 
	 */
	public SimpleContainer() {
		super("My Container");
	}


	/* (non-Javadoc)
	 * @see com.ibm.jve.sample.core.UIContainer#create()
	 */
	protected void create() {
		parent = createBaseContainer(1, false);
		Composite c1 = createComposite(parent, 2);
		
		Label name = new Label(c1, SWT.NONE);
		name.setText("Complete Name:");
				
		UILabelTextPart firstName = new UILabelTextPart(c1, "First Name:");
		Label dummy = new Label(c1, SWT.NONE);		
		UILabelTextPart lastName = new UILabelTextPart(c1, "Last Name:");
		Label dummy2 = new Label(c1, SWT.NONE);
		UIDropDownComboPart prefix = new UIDropDownComboPart(c1, "Title:", new String[]{"Mr.", "Mrs.", "Ms.", "Miss.", "Dr."});
		

	}

}
