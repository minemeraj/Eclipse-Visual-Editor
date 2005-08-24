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
 *  $RCSfile: AbstractRenameInstanceDialog.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.dialogs.PreferencesUtil;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;

import org.eclipse.ve.internal.java.vce.VCEPreferences;

/**
 * Abstract instance naming dialog
 * 
 * @since 1.1
 */
public abstract class AbstractRenameInstanceDialog extends TitleAreaDialog {

	protected String[] originalNames = null;
	protected String[] newNames = null;
	protected Text[] nameTexts = null;
	protected Label[] nameStatuses = null;
	protected EObject[] annotates = null;
	protected EditDomain domain = null;
	protected ILabelProvider labelProvider = null;
	protected boolean forceChange = false;
	protected boolean enableDontShowOption = false;

	protected Image titleImage = null;
	protected Image errorImage = null;
	
	public AbstractRenameInstanceDialog(Shell parentShell, String[] names, EObject[] annotates, EditDomain domain, boolean forceChange, boolean enableDontShowOption) {
		super(parentShell);
		this.originalNames = names;
		this.annotates = annotates;
		this.domain = domain;
		this.forceChange = forceChange;
		this.enableDontShowOption = enableDontShowOption;
		this.nameTexts = new Text[names.length];
		this.nameStatuses = new Label[names.length];
		this.newNames = new String[names.length];
		for (int i = 0; i < originalNames.length; i++) {
			newNames[i] = originalNames[i];
		}
	}

	/**
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea
	 */
	protected Control createDialogArea(Composite parent) {
		// top level composite
		Composite parentComposite = (Composite) super.createDialogArea(parent);

		// creates dialog area composite
		Composite contents = createComposite(parentComposite);
		applyDialogFont(parent);
		return contents;
	}


	/**
	 * Creates and configures this dialog's main composite.
	 * 
	 * @param parentComposite
	 *            parent's composite
	 * @return this dialog's main composite
	 */
	protected Composite createComposite(Composite parentComposite) {
		// creates a composite with standard margins and spacing
		Composite mainContainer = new Composite(parentComposite, SWT.NONE);
		mainContainer.setLayout(new GridLayout(2, false));
		mainContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite beanNamesContainer = new Composite(mainContainer, SWT.NONE);
		beanNamesContainer.setLayout(new GridLayout(4, false));
		beanNamesContainer.setFont(parentComposite.getFont());
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan=2;
		beanNamesContainer.setLayoutData(data);

		for (int nameCount = 0; nameCount < newNames.length; nameCount++) {
			Image image = JavaVEPlugin.getJavaBeanImage();
			nameStatuses[nameCount] = new Label(beanNamesContainer, SWT.NONE);
			nameStatuses[nameCount].setImage(image);
			
			String name = newNames[nameCount];
			if(annotates[nameCount]!=null){
				labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(domain).getLabelProvider(annotates[nameCount].eClass());
				if(labelProvider!=null){
					name = labelProvider.getText(annotates[nameCount]);
					image = labelProvider.getImage(annotates[nameCount]);
				}
			}
			Label imageLabel = new Label(beanNamesContainer, SWT.NONE);
			imageLabel.setImage(image);
			
			Label nameLabel = new Label(beanNamesContainer, SWT.NONE);
			nameLabel.setText(name);
			
			nameTexts[nameCount] = new Text(beanNamesContainer, SWT.BORDER);
			nameTexts[nameCount].setText(newNames[nameCount]);
			nameTexts[nameCount].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			nameTexts[nameCount].addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					updateStatus();
				}
			});
		}

		if(enableDontShowOption){
			
			// Preferences link
			Link preferencesLink = new Link(mainContainer, SWT.NONE);
			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalAlignment=SWT.BEGINNING;
			preferencesLink.setLayoutData(data);
			preferencesLink.setText(MessageFormat.format("<a>{0}</a>", new Object[]{JavaMessages.AbstractRenameInstanceDialog_Preferences})); 
			preferencesLink.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					Link link = (Link) e.getSource();
					String prefID = "org.eclipse.ve.internal.java.vce.VCEPreferencePage"; //$NON-NLS-1$
					PreferencesUtil.createPreferenceDialogOn(link.getShell(), prefID, new String[]{prefID}, null).open();
				}
			});
			
			// dont ask again checkbox
			Button checkbox = new Button(mainContainer, SWT.CHECK);
			checkbox.setText(JavaMessages.AbstractRenameInstanceDialog_Checkbox_DontAsk); 
			checkbox.setLayoutData(new GridData());
			checkbox.setSelection(!VCEPreferences.askForRename());
			checkbox.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					Button cb = (Button) e.getSource();
					JavaVEPlugin.getPlugin().getPluginPreferences().setValue(VCEPreferences.RENAME_INSTANCE_ASK_KEY, !cb.getSelection());
				}
			});
			
		}
		
		titleImage = JavaPluginImages.DESC_WIZBAN_REFACTOR_TYPE.createImage(parentComposite.getDisplay());
		errorImage = JavaPluginImages.DESC_OBJS_REFACTORING_ERROR.createImage(parentComposite.getDisplay());
		setTitleImage(titleImage);

		return beanNamesContainer;
	}

	protected abstract String getValidInstanceVariableName(EObject instance, String name, List currentNamesInDialog);
	
	protected void updateStatus() {
		setErrorMessage(null);
		boolean allNamesValid = true;
		boolean anyChanged = false;
		String errorMessage = null;
		setErrorMessage(null);
		
		for (int nameCount = 0; nameCount < newNames.length; nameCount++) {
			newNames[nameCount] = nameTexts[nameCount].getText();
			IStatus validation = JavaConventions.validateFieldName(newNames[nameCount]);
			if (!originalNames[nameCount].equals(newNames[nameCount])) {
				anyChanged = true;
				if(validation.isOK()){
					List currentNames = new ArrayList(newNames.length);
					for (int count = 0; count < newNames.length; count++) 
						if(count!=nameCount)
							currentNames.add(nameTexts[count].getText());
					String newNameSuggestion = getValidInstanceVariableName(annotates[nameCount], newNames[nameCount], currentNames);
					if(!newNames[nameCount].equals(newNameSuggestion)){
						// new name suggested
						errorMessage = MessageFormat.format(JavaMessages.AbstractRenameInstanceDialog_ErrorMsg_NotUnique, new Object[]{newNames[nameCount], newNameSuggestion});  
						allNamesValid = false;
						nameStatuses[nameCount].setImage(errorImage);
						nameStatuses[nameCount].setToolTipText(errorMessage);
					}else{
						//valid name
						nameStatuses[nameCount].setImage(JavaVEPlugin.getJavaBeanImage());
						nameStatuses[nameCount].setToolTipText(null);
					}
				} else {
					// invalid java identifier
					errorMessage = validation.getMessage();
					allNamesValid = false;
					nameStatuses[nameCount].setImage(errorImage);
					nameStatuses[nameCount].setToolTipText(errorMessage);
				}
			}else{
				// no change
				nameStatuses[nameCount].setImage(JavaVEPlugin.getJavaBeanImage());
				nameStatuses[nameCount].setToolTipText(null);
			}
		}
		
		if(!allNamesValid){
			setErrorMessage(errorMessage);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}else{
			if(forceChange && !anyChanged)
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			else
				getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
	}

	public String[] getFinalNames() {
		return newNames;
	}

	/**
	 * @see org.eclipse.jface.window.Window#close()
	 */
	public boolean close() {
		if (titleImage != null)
			titleImage.dispose();
		if(errorImage!=null)
			errorImage.dispose();
		return super.close();
	}

	/**
	 * @see org.eclipse.jface.window.Window#create()
	 */
	public void create() {
		super.create();
		if(forceChange)
			getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

}
