package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: XMLTextPage.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */



import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;

public class XMLTextPage extends Page {
	protected XMLTextViewer viewer;
	protected String fText = ""; //$NON-NLS-1$
	
public void createControl(Composite parent) {
	viewer = new XMLTextViewer(parent , SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
	viewer.setText(fText);
	fText = null;	// Get rid of it since now handled through viewer.
}		

public void setText(final String aString){
	if ( viewer != null ) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				viewer.setText(aString);
			}
		}); 
	} else {
		fText = aString;
	}
}

public Control getControl() {
	return viewer != null ? viewer.getControl() : null;
}

public void setFocus() {
	viewer.getControl().setFocus();
}
}