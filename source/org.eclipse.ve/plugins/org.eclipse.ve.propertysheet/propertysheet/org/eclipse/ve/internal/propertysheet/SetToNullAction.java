package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SetToNullAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */



import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
/**
 * This action is used to set the active entries to null.
 */
public class SetToNullAction extends EToolsPropertySheetAction {
	protected static ImageDescriptor NULL_RESET_IMAGE;
		
	public SetToNullAction(EToolsPropertySheetPage page) {
		super(page);
		if ( NULL_RESET_IMAGE == null ) {
			try {
				NULL_RESET_IMAGE = ImageDescriptor.createFromURL(new URL(PSheetPlugin.getPlugin().getDescriptor().getInstallURL(), "images/nullreset16.gif"));	//$NON-NLS-1$
			} catch (MalformedURLException e) {
				NULL_RESET_IMAGE = ImageDescriptor.getMissingImageDescriptor();
			}		
		};
		setImageDescriptor(NULL_RESET_IMAGE);
		setText(PropertysheetMessages.getString(PropertysheetMessages.SET_NULLS_LABEL));
		setToolTipText(PropertysheetMessages.getString(PropertysheetMessages.SET_NULLS_TOOLTIP));		
	}		

	/**
	 * Call when selection has been changed. The EToolsProeprtySheetPage will do this for us.
	 */
	public void selectionChanged(ISelection selection) {
		boolean enable = false;
		IStructuredSelection selected = (IStructuredSelection) getSelection();
		if (!selected.isEmpty()) {
			IDescriptorPropertySheetEntry entry = (IDescriptorPropertySheetEntry) selected.getFirstElement();
			// Can't have nulls if the editor is not active or if the descriptor says no nulls.
			enable = !entry.areNullsInvalid();
		}
		setEnabled(enable);
	}

	/**
	 * @see Action
	 */
	public void run() {
		IStructuredSelection selected = (IStructuredSelection) getSelection();
		if (!selected.isEmpty()) {
			IDescriptorPropertySheetEntry entry = (IDescriptorPropertySheetEntry) selected.getFirstElement();
			entry.setToNull();
		}		
	}	
}