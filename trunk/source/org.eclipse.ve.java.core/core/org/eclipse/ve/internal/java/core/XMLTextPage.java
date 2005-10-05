/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: XMLTextPage.java,v $
 *  $Revision: 1.6 $  $Date: 2005-10-05 15:57:16 $ 
 */



import java.io.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.Page;

public class XMLTextPage extends Page implements IPageBookViewPage {
//	protected XMLTextViewer viewer;
	protected Browser browser;
	protected String fText = ""; //$NON-NLS-1$
	

File tempFile = null;	
private void setBrowserText(String text) {
	// The SWT widget does not know how to render
	// XML from a buffer (setText())... it uses the file's extension to figure out
	// that it is an XML mime type.
	try {
		File old = tempFile;
		tempFile = File.createTempFile("VE_XMI",".xml");
		Writer output = new BufferedWriter( new FileWriter(tempFile) );
		output.write( text );
		output.close();
		browser.setUrl(tempFile.toURL().toExternalForm());
		if (old != null)
		   old.delete();
	} catch (IOException e) {
		JavaVEPlugin.log(e);
	}
    
	
	
}
public void createControl(Composite parent) {
//	viewer = new XMLTextViewer(parent , SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
	browser = new Browser(parent, SWT.NONE);	
	
//	viewer.setText(fText);
	setBrowserText(fText);
	fText = null;	// Get rid of it since now handled through viewer.
}		

public void setText(final String aString){
//	if ( viewer != null ) {
	if ( browser != null ) {	
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				//browser.setText(aString);
				setBrowserText(aString);
			}
		}); 
	} else {
		fText = aString;
	}
}

public String getSelectedText(){
	//return ((ITextSelection)viewer.getSelection()).getText();
	return null;
}

public Control getControl() {
	// return viewer != null ? viewer.getControl() : null;
	return browser;
}

public void setFocus() {
	// viewer.getControl().setFocus();
	browser.setFocus();
}
}
