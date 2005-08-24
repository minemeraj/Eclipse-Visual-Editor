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
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.jve.sample;

import org.eclipse.swt.widgets.Composite;

import com.ibm.jve.sample.containers.SimpleContainer;

/**
 * @author doconnor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyTestComposite extends Composite {

	/**
	 * @param parent
	 * @param style
	 */
	public MyTestComposite(Composite parent, int style) {
		super(parent, style);		
		initialize();
		SimpleContainer sc = new SimpleContainer();
		sc.createContents(this);
	}

	private void initialize() {
		setSize(new org.eclipse.swt.graphics.Point(300,200));
	}
}
