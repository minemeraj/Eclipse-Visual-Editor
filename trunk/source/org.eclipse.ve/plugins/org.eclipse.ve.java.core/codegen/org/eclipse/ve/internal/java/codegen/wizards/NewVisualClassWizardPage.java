package org.eclipse.ve.internal.java.codegen.wizards;
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
 *  $RCSfile: NewVisualClassWizardPage.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-02 15:57:22 $ 
 */

import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;

/**
 * @author JoeWin
 */
public class NewVisualClassWizardPage extends NewClassWizardPage {

	protected SelectionButtonDialogFieldGroup fSwingAWTGroup;
	String[] widgetSetNames = new String[] { 
		CodegenMessages.getString("swing") ,  //$NON-NLS-1$
		CodegenMessages.getString("awt" ) }; //$NON-NLS-1$

	protected SelectionButtonDialogFieldGroup fVisualTypesGroup;
	Button[] buttons;
	String[] buttonNames = new String[] { 
		CodegenMessages.getString("frame") ,  //$NON-NLS-1$
		CodegenMessages.getString("panel") , 	//$NON-NLS-1$
		CodegenMessages.getString("applet") , //$NON-NLS-1$
	};
	String[] swingButtonClasses = new String[] { "javax.swing.JFrame" , "javax.swing.JPanel" , "javax.swing.JApplet" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	String[] AWTButtonClasses = new String[] { "java.awt.Frame" , "java.awt.Panel" , "java.applet.Applet" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	Button swingButton;
	boolean isSettingSuperclass;
	
	protected void createSuperClassControls(Composite composite, int nColumns) {
		
		Composite labelGroup = createComposite(composite,1);
		((GridData)labelGroup.getLayoutData()).horizontalSpan = 4;
		createLabel(labelGroup,CodegenMessages.getString("which.visual.superclass")); //$NON-NLS-1$

		createLabel(composite,""); //$NON-NLS-1$
		Composite classesGroup = createComposite(composite,4);

		// Create the array of buttons
		buttons = new Button[4];
		for (int i = 0; i < buttonNames.length; i++) {
			buttons[i] = createRadioButton(classesGroup,buttonNames[i],swingButtonClasses[i],AWTButtonClasses[i],false);
		}
		// Add a fourth button for java.lang.Object
		buttons[3] = createRadioButton(classesGroup,CodegenMessages.getString("other") ,"java.lang.Object","java.lang.Object",true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

//		((GridData)classesGroup.getLayoutData()).horizontalSpan = 1;

		// Create a toggle button for whether the classes are Swing or not
		createLabel(composite,""); //$NON-NLS-1$
		Composite swingGroup = createComposite(composite, 1);
		swingButton = createToggleButton(swingGroup,widgetSetNames[0]);
		swingButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				// When the toggle button switches to Swing or AWT we should default in the correct superclass
				for (int i = 0; i < buttonNames.length; i++) {
					if ( buttons[i].getSelection() ) {
						if ( swingButton.getSelection() ) {
							setSuperClass(swingButtonClasses[i]);
							getSuperclassButtonDialogField().setEnabled(false);
							break;
						} else {
							setSuperClass(AWTButtonClasses[i]);							
							getSuperclassButtonDialogField().setEnabled(false);
							break;
						}
					}
				}
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
//		createLabel(composite,"");						 //$NON-NLS-1$
		
		super.createSuperClassControls(composite,nColumns);				
		
		// Set the frame to be the default		
		if(getSuperclassButtonDialogField().getText()!=null && 
			getSuperclassButtonDialogField().getText().length()>0){
				refreshButtons();
			}else{
				setSuperClass(swingButtonClasses[0]);		
				swingButton.setSelection(true);
				buttons[0].setSelection(true);		
				getSuperclassButtonDialogField().setEnabled(false);
			}
	}
	protected void setSuperClass(String name){
		isSettingSuperclass = true;
		super.setSuperClass(name,true);
		isSettingSuperclass = false;
	}
	
	public void refreshButtons(){
		String name = getSuperclassButtonDialogField().getText();
		for(int i=0;i<swingButtonClasses.length;i++){
			if(swingButtonClasses[i].equals(name)){
				if(!swingButton.getSelection())
					swingButton.setSelection(true);
				buttons[i].setSelection(true);
				getSuperclassButtonDialogField().setEnabled(false);
				return;
			}
		}
		for(int i=0;i<AWTButtonClasses.length;i++){
			if(AWTButtonClasses[i].equals(name)){
				if(swingButton.getSelection())
					swingButton.setSelection(false);
				buttons[i].setSelection(true);
				getSuperclassButtonDialogField().setEnabled(false);
				return;
			}
		}
		getSuperclassButtonDialogField().setEnabled(true);
		swingButton.setSelection(false);
		buttons[buttonNames.length].setSelection(true);
		setSuperClass(name);
	}
	
	protected StringButtonDialogField localSuperclassButtonDialogField;
	protected StringButtonDialogField getSuperclassButtonDialogField(){
		if ( localSuperclassButtonDialogField == null ) {
			// This field is private and inherited so we have to retrieve it using reflection
			try {
				Class newClassWizardPage = Class.forName("org.eclipse.jdt.ui.wizards.NewTypeWizardPage"); //$NON-NLS-1$			
				java.lang.reflect.Field field = newClassWizardPage.getDeclaredField("fSuperClassDialogField"); //$NON-NLS-1$
				field.setAccessible(true);
				localSuperclassButtonDialogField = (StringButtonDialogField)field.get(this);
			} catch ( Exception exc ) {
			}
		}
		return localSuperclassButtonDialogField;
	}
	
	// We need to know when the superclass text field has changed so that we can deselect the buttons
	// This will help the user to know that they are no longer subclassing a Frame or Panel or Applet
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);	
//		outer:if ( buttons != null && fieldName.equals("NewTypeWizardPage.superclass") && !isSettingSuperclass) { //$NON-NLS-1$
//			String superclassName = getSuperClass();
//			// See whether the superclass is actually one of the ones we can deal with and, if so, reverse select the radio button
//			for (int i = 0; i < swingButtonClasses.length; i++) {
//				if ( swingButtonClasses[i].equals(superclassName)) {
//					swingButton.setSelection(true);
//					buttons[i].setSelection(true);
//					break outer;
//				};
//				if ( AWTButtonClasses[i].equals(superclassName) ){
//					swingButton.setSelection(false);
//					buttons[i].setSelection(true);
//					break outer;
//				};
//			}
		// If we are here then the superclass was none of the ones we can handle, so all buttons should be deselected
//			for (int i = 0; i < buttons.length; i++) {
//				buttons[i].setSelection(false);
//			}
//		}
	}
	protected Composite createComposite(Composite aParent, int numColumns){
		Composite group = new Composite(aParent,SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		group.setLayout(gridLayout);
		GridData data = new GridData();
//		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		return group;
	}	
	protected Group createGroup(Composite aParent, String title, int numColumns){
		Group group = new Group(aParent,SWT.NONE);
		if ( title != null ) {
			group.setText(title);
		}
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		group.setLayout(gridLayout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		return group;
	}
	protected Button createRadioButton(Composite parent, String label,final String swingClass,final String awtClass, final boolean allowChange){
		Button button = new Button(parent, SWT.RADIO | SWT.LEFT);
		button.setText(label);
		GridData data = new GridData();
		button.setData(data);
		button.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				if ( swingButton.getSelection() ) {
					setSuperClass(swingClass);
				} else {
					setSuperClass(awtClass);					
				}
				if ( !allowChange ) {
					getSuperclassButtonDialogField().setEnabled(false);										
				}
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		return button;
	}	
	protected Button createToggleButton(Composite parent, String label){
		Button button = new Button(parent, SWT.CHECK | SWT.LEFT);
		button.setText(label);
		GridData data = new GridData();
		button.setLayoutData(data);
		return button;
	}		
	protected void createLabel(Composite parent,String text) {
		Label spacer = new Label(parent, SWT.NONE);
		if ( text != null ) {
			spacer.setText(text);
		}
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		spacer.setLayoutData(data);
	}		
}
