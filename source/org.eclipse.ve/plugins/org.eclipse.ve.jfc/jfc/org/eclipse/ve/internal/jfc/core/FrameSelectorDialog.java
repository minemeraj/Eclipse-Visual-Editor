/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FrameSelectorDialog.java,v $
 *  $Revision: 1.4 $  $Date: 2006-05-17 20:14:58 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

/**
 * Frame Selector dialog.
 * @since 1.2.0
 */
class FrameSelectorDialog extends TitleAreaDialog {

	private final EditDomain domain;
	private FrameSelectorDialogArea dialogArea;
	private List frames;

	/**
	 * @param parentShell
	 * 
	 * @since 1.2.0
	 */
	public FrameSelectorDialog(Shell parentShell, EditDomain domain) {
		super(parentShell);
		this.domain = domain;
	}

	protected Control createDialogArea(Composite parent) {
		Composite newParent = (Composite) super.createDialogArea(parent);
		dialogArea = new FrameSelectorDialogArea(newParent, SWT.NONE, domain);
		dialogArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		if (frames != null)
			dialogArea.setFrames(frames);
		setTitle(JFCMessages.FrameSelectorDialog_Title);
		setMessage(JFCMessages.FrameSelectorDialog_Message);
		final Image titleImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/wizban/javabean_wiz.gif"); // internal cleanup - copied from 'org.eclipse.jdt.ui\icons\full\wizban\typerefact_wiz.gif' //$NON-NLS-1$
		setTitleImage(titleImage);
		newParent.addDisposeListener(new DisposeListener() {
		
			public void widgetDisposed(DisposeEvent e) {
				titleImage.dispose();
			}
		
		});
		return newParent;
	}
	
	public void setInput(List frames) {
		this.frames = frames;
		if (dialogArea != null)
			dialogArea.setFrames(frames);
	}
	
	public Object getSelectedFrame() {
		return dialogArea.getSelectedFrame();
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(JFCMessages.FrameSelectorDialog_Window_Title);
	}
}
