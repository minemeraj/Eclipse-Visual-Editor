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
 *  $Revision: 1.4 $  $Date: 2004-06-19 20:17:36 $ 
 */
package org.eclipse.ve.internal.propertysheet;

import java.net.URL;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * This action is used to set the active entries to null.
 */
public class SetToNullAction extends EToolsPropertySheetAction {

	protected static ImageDescriptor NULL_RESET_IMAGE, NULL_RESET_DISABLE_IMAGE;

	public SetToNullAction(EToolsPropertySheetPage page) {
		super(page);
		if (NULL_RESET_IMAGE == null) {
			URL url = Platform.find(PSheetPlugin.getPlugin().getBundle(), new Path("images/full/elcl16/nullreset16.gif")); //$NON-NLS-1$
			NULL_RESET_IMAGE = url != null ? ImageDescriptor.createFromURL(url) : ImageDescriptor.getMissingImageDescriptor();
		}
// TODO Need a disabled image		
//		if (NULL_RESET_DISABLE_IMAGE == null) {
//			URL url = Platform.find(PSheetPlugin.getPlugin().getBundle(), new Path("images/full/dlcl16/nullreset16.gif")); //$NON-NLS-1$
//			NULL_RESET_DISABLE_IMAGE = url != null ? ImageDescriptor.createFromURL(url) : ImageDescriptor.getMissingImageDescriptor();
//		}		

		setImageDescriptor(NULL_RESET_IMAGE);
//		setDisabledImageDescriptor(setImageDescriptor(NULL_RESET_IMAGE););
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

	public void run() {
		IStructuredSelection selected = (IStructuredSelection) getSelection();
		if (!selected.isEmpty()) {
			IDescriptorPropertySheetEntry entry = (IDescriptorPropertySheetEntry) selected.getFirstElement();
			entry.setToNull();
		}
	}
}