package org.eclipse.ve.internal.java.vce;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LookAndFeelDialog.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-22 13:04:18 $ 
 */

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * @author JoeWin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LookAndFeelDialog extends Dialog {
	
	protected Text fLookAndFeelNameText, fLookAndFeelClassText;
	protected String fName, fClass;
	
public LookAndFeelDialog(Shell aShell){
	super(aShell);
}

public LookAndFeelDialog(Shell aShell,String aName, String aClass){
	this(aShell);
	fName = aName;
	fClass = aClass;
}

protected void configureShell(Shell newShell) {
	super.configureShell(newShell);
	newShell.setText(VCEMessages.LookAndFeelDialog_Shell_Text); 
}

protected void createButtonsForButtonBar(Composite parent) {
	
	super.createButtonsForButtonBar(parent);
	validate();
}
	
protected Control createDialogArea(Composite aComposite){
	
	Composite parent = (Composite)super.createDialogArea(aComposite);
	Composite c = new Composite(parent,SWT.NONE);
	c.setLayout(new GridLayout(2,false));
	
	Label lookAndFeelNameLabel = new Label(c,SWT.NONE);
	lookAndFeelNameLabel.setText(VCEMessages.LookAndFeelDialog_LookAndFeel_Name); 
	
	fLookAndFeelNameText = new Text(c,SWT.BORDER);
	GridData data = new GridData();
	GC gc = new GC(fLookAndFeelNameText);
	data.widthHint = convertWidthInCharsToPixels(gc.getFontMetrics(),20);
	fLookAndFeelNameText.setLayoutData(data);
	if ( fName != null ) fLookAndFeelNameText.setText(fName);
	
	Label lookAndFeelClassLabel = new Label(c,SWT.NONE);
	lookAndFeelClassLabel.setText(VCEMessages.LookAndFeelDialog_LookAndFeel_Class); 
	
	fLookAndFeelClassText = new Text(c,SWT.BORDER);
	GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
	data1.widthHint = convertWidthInCharsToPixels(gc.getFontMetrics(),60);
	fLookAndFeelClassText.setLayoutData(data1);
	if ( fClass != null ) fLookAndFeelClassText.setText(fClass);

	gc.dispose();
	
	// Add listeners to validate the name and class are not blank
	ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			validate();
		}
	};
	fLookAndFeelClassText.addModifyListener(listener);
	fLookAndFeelNameText.addModifyListener(listener);

	applyDialogFont(c);
	return c;

}
// To be valid the look and feel name and class must be entered and can't contain commas
// ( as we use this as our delimiter character when we store it in the plugin preference store )
// We could enhance the rules to validate the class name but this seems like overkill
protected void validate(){
	String nameText = fLookAndFeelNameText.getText();
	String classText = fLookAndFeelClassText.getText();
	if ( nameText == null || 
		nameText.trim().length() == 0 ||
		nameText.indexOf(',') != -1 ||
		nameText.indexOf(' ') != -1 ||		
		classText == null ||
		classText.trim().length() == 0 ||
		classText.indexOf(',') != -1 ||
		classText.indexOf(' ') != -1 ) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else {
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
}
protected void okPressed(){
	fName = fLookAndFeelNameText.getText();
	fClass = fLookAndFeelClassText.getText();
	super.okPressed();
}
}
