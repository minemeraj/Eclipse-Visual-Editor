/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridLayoutComponentComposite.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-16 23:03:39 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.GridLayout;


public class GridLayoutComponentComposite extends Composite {

	protected Spinner horizontalSpanSpinner, verticalSpanSpinner;
	protected int horizontalSpanValue = 1, verticalSpanValue = 1;

	public GridLayoutComponentComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		this.setLayout(gridLayout2);
		setSize(new Point(400, 250));
	}

}
