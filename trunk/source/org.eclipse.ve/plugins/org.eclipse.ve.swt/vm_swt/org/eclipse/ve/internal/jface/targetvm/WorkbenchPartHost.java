/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: WorkbenchPartHost.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.jface.targetvm;

import java.lang.reflect.Method;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * WorkbenchPart Host helper. Used to create the host for a workbench part and the workbench part itself.
 * 
 * @since 1.1.0
 */
public class WorkbenchPartHost {

	public static final int MIN_X = 300;

	public static final int MIN_Y = 175;

	/**
	 * Create a workbench part, return the workbenchpart host, and the parent composite that is used to be the parent sent to createPartControl.
	 * <p>
	 * <b>Note:</b> This must be called on the UI thread.
	 * 
	 * @param hostParent
	 * @param aWorkbenchPart
	 * @param aTitle
	 * @param iconLocation
	 *            absolute location of icon in local file system or <code>null</code> to use default depending on type of workbench part (view vs.
	 *            editor)
	 * @param traditionalTabs
	 * @param tabPosition
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static Composite[] createWorkbenchPart(Composite hostParent, IWorkbenchPart aWorkbenchPart, String aTitle, String iconLocation,
			boolean traditionalTabs, int tabPosition) {
		
		CTabFolder folder = new CTabFolder(hostParent, SWT.BORDER);
		folder.setUnselectedCloseVisible(false);
		folder.setMaximizeVisible(true);
		folder.setMinimizeVisible(true);

		// Having simple and traditional tabs is not available on all target platforms so use reflection
		// folder.setSimple(fTraditionalTabs);
		try {
			Method setSimpleMethod = folder.getClass().getMethod("setSimple", new Class[] { Boolean.TYPE});
			setSimpleMethod.invoke(folder, new Object[] { Boolean.valueOf(traditionalTabs)});
		} catch (Exception e) {
		}
		folder.setTabPosition(tabPosition);

		// Editor parts don't need the viewform, but viewparts will (this is where their toolbar goes).
		ViewForm viewForm = new ViewForm(folder, SWT.NONE);
		viewForm.marginHeight = 0;
		viewForm.marginWidth = 0;
		viewForm.verticalSpacing = 0;
		viewForm.setBorderVisible(false);

		Composite workbenchPartArgument = new Composite(viewForm, SWT.NONE) {

			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point preferredSize = super.computeSize(wHint, hHint, changed);
				return new Point(Math.max(preferredSize.x, MIN_X), Math.max(preferredSize.y, MIN_Y));
			}
		};
		workbenchPartArgument.setLayout(new FillLayout());
		viewForm.setContent(workbenchPartArgument);

		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText(aTitle);
		folder.setSelection(item);
		// Load the icon.
		Image image = null;
		if (iconLocation == null) {
			if (aWorkbenchPart instanceof IEditorPart)
				image = ImageDescriptor.createFromFile(WorkbenchPartHost.class, "rcp_editor.gif").createImage();
			else
				image = ImageDescriptor.createFromFile(WorkbenchPartHost.class, "rcp_app.gif").createImage();
		} else
			image = ImageDescriptor.createFromFile(null, iconLocation).createImage();

		item.setImage(image);
		item.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				Item eItem = (Item) e.getSource();
				Image eImage = eItem.getImage();
				if (eImage != null && !eImage.isDisposed()) {
					eImage.dispose();
				}
			}
		});

		item.setControl(viewForm);

		aWorkbenchPart.createPartControl(workbenchPartArgument);
		return new Composite[] { folder, workbenchPartArgument};

	}

}
