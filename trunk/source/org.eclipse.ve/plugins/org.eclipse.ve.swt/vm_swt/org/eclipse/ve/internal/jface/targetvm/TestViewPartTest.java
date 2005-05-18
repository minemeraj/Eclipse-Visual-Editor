/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TestViewPartTest.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-18 18:23:13 $ 
 */
package org.eclipse.ve.internal.jface.targetvm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class TestViewPartTest extends ViewPart {

	public void createPartControl(Composite parent) {

		Composite c = new Composite(parent,SWT.BORDER);
		c.setLayoutData(new GridData(GridData.FILL_BOTH));
		c.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_CYAN));
		new Button(c,SWT.NONE).setText(JFaceTargetVMMessages.getString("TestViewPartTest.Button.Push")); //$NON-NLS-1$
		
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
