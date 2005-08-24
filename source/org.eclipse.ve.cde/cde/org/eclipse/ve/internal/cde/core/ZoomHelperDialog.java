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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: ZoomHelperDialog.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:49 $ 
 */



import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
/**
 * Dialog that prompts the user for the zoom percentage.
 *
 * Returns an int (newValue) that is the percent (i.e. 100, 200, etc.) or
 * returns 0 if either no change in the value or the cancel button was pressed.
 */

public class ZoomHelperDialog extends Dialog {
	public static final int NO_CHANGE = 0;	// Zoom value indicating it hasn't changed.
	
	List zoomlist;
	static String[] ZoomPercents = { "20", "40", "60", "80", "100", "120", "140", "160", "180", "200" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	int oldValue;
	
	int newValue = NO_CHANGE;
		
	public ZoomHelperDialog(Shell parent, int zoomValue) {
		super(parent);
		oldValue = zoomValue;
	}

	/**
	 * Get the zoom value.
	 */
	public int getZoomValue() {
		return newValue;
	}	
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CDEMessages.ZoomHelperDialog_label); 
		newShell.setImage(newShell.getDisplay().getSystemImage(SWT.ICON_QUESTION));
	}

	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		
		Label label = new Label(area, SWT.SHADOW_IN);
		label.setText(CDEMessages.ZoomHelperDialog_PERCENT); 
		
		zoomlist= new List(area, SWT.BORDER);
		zoomlist.setLayoutData(new GridData(GridData.FILL_BOTH));
		zoomlist.setItems(ZoomPercents);
		String value = Integer.toString(oldValue);
		if (zoomlist.indexOf(value) != -1) {
			zoomlist.setSelection(zoomlist.indexOf(value));
		}
		zoomlist.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// This is to be treated as OKPressed to dismiss the dialog.
				okPressed();
			}
			
			public void widgetSelected(SelectionEvent e) {
			}
		});
		return area;
	}
	
	protected void okPressed() {
		// Override to also get the current selection to compute the percentage.
		if (zoomlist.getSelectionIndex() == -1) {
			newValue = NO_CHANGE; // nothing selected
		} else {
			String value = zoomlist.getItem(zoomlist.getSelectionIndex());
			newValue = Integer.parseInt(value);
			if (newValue == oldValue) {
				newValue = NO_CHANGE; // indicate no change
			}
		}
		super.okPressed();
	}
	
	/**
	 * We want it at the mouse location so that it doesn't require
	 * user to move the mouse all of the way to the center to do something.
	 */
	protected Point getInitialLocation(Point initialSize) {
		return Display.getCurrent().getCursorLocation();
	}
		
}
