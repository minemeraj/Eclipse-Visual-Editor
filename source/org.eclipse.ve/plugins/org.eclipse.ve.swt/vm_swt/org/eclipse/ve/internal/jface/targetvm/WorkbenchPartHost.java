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
 *  $Revision: 1.2 $  $Date: 2005-10-05 15:25:09 $ 
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

	private static final int MIN_WIDTH = 300;

	private static final int MIN_HEIGHT = 175;

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
				return new Point(Math.max(preferredSize.x, ((Integer) this.getData(WIDTH)).intValue()), Math.max(preferredSize.y, ((Integer) this.getData(HEIGHT)).intValue()));
			}
		};
		setWorkbenchPartWorkingSize(workbenchPartArgument, MIN_WIDTH, MIN_HEIGHT);	// Set initial preferred side.
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
	
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	
	/**
	 * Set the preferred minimum size for the workbench part.
	 * @param workbenchPart the workbenchpart returned in the second argument of the {@link #createWorkbenchPart(Composite, IWorkbenchPart, String, String, boolean, int)} returned array.
	 * @param width the min width (it will accept no less than {@link #MIN_WIDTH}. If set to less than that it will use the MIN_WIDTH instead).
	 * @param height the min height (it will accept no less than {@link #MIN_HEIGHT}. If set to less than that it will use the MIN_HEIGHT instead).
	 * 
	 * @since 1.2.0
	 */
	public static void setWorkbenchPartWorkingSize(Composite workbenchPart, int width, int height) {
		if (!workbenchPart.isDisposed()) {
			workbenchPart.setData(WIDTH, new Integer(Math.max(width, MIN_WIDTH)));
			workbenchPart.setData(HEIGHT, new Integer(Math.max(height, MIN_HEIGHT)));
		}
	}

}
