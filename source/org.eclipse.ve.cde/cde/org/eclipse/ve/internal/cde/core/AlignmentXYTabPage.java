package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: AlignmentXYTabPage.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IEditorPart;

/**
 * This is the dialog that appears when the user selects Alignment
 */
public class AlignmentXYTabPage extends AlignmentTabPage {

	private AlignmentAction fAlignLeftAction = new AlignmentAction(AlignmentCommandRequest.LEFT_ALIGN);
	private AlignmentAction fAlignRightAction = new AlignmentAction(AlignmentCommandRequest.RIGHT_ALIGN);
	private AlignmentAction fAlignCenterAction = new AlignmentAction(AlignmentCommandRequest.CENTER_ALIGN);
	private AlignmentAction fAlignTopAction = new AlignmentAction(AlignmentCommandRequest.TOP_ALIGN);
	private AlignmentAction fAlignMiddleAction = new AlignmentAction(AlignmentCommandRequest.MIDDLE_ALIGN);
	private AlignmentAction fAlignBottomAction = new AlignmentAction(AlignmentCommandRequest.BOTTOM_ALIGN);
	private AlignmentAction fMatchWidthAction = new AlignmentAction(AlignmentCommandRequest.MATCH_WIDTH);
	private AlignmentAction fMatchHeightAction = new AlignmentAction(AlignmentCommandRequest.MATCH_HEIGHT);
	private ShowDistributeBoxAction fShowDistributeBoxAction = new ShowDistributeBoxAction();
	private DistributeAction fDistributeHorizontalAction = new DistributeAction(DistributeCommandRequest.HORIZONTAL);
	private DistributeAction fDistributeVerticalAction = new DistributeAction(DistributeCommandRequest.VERTICAL);
	
	
	protected void handleSelectionProviderInitialization(ISelectionProvider selectionProvider) {
		fAlignLeftAction.setSelectionProvider(selectionProvider);
		fAlignCenterAction.setSelectionProvider(selectionProvider);
		fAlignRightAction.setSelectionProvider(selectionProvider);
		fAlignTopAction.setSelectionProvider(selectionProvider);
		fAlignMiddleAction.setSelectionProvider(selectionProvider);
		fAlignBottomAction.setSelectionProvider(selectionProvider);
		fMatchWidthAction.setSelectionProvider(selectionProvider);
		fMatchHeightAction.setSelectionProvider(selectionProvider);
		fDistributeHorizontalAction.setSelectionProvider(selectionProvider);
		fDistributeVerticalAction.setSelectionProvider(selectionProvider);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.AlignmentTabPage#getImage()
	 */
	public Image getImage() {
		return null;	// No image for the tab
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.AlignmentTabPage#getText()
	 */
	public String getText() {
		return CDEMessages.getString("XYAlignmentTab.label"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.AlignmentTabPage#getToolTipText()
	 */
	public String getToolTipText() {
		return CDEMessages.getString("XYAlignmentTab.tooltip"); //$NON-NLS-1$
	}	

	public Control getControl(Composite parent) {
		Composite group = new Composite(parent, SWT.NONE);
		group.setLayout(new GridLayout(1, true));

		ToolBar toolBar1 = new ToolBar(group, SWT.FLAT);

		ToolBarManager mgr1 = new ToolBarManager(toolBar1);
		mgr1.add(fAlignLeftAction);
		mgr1.add(fAlignTopAction);
		mgr1.add(fMatchWidthAction);
		mgr1.add(fShowDistributeBoxAction);
		mgr1.update(true);

		ToolBar toolBar2 = new ToolBar(group, SWT.FLAT);
		ToolBarManager mgr2 = new ToolBarManager(toolBar2);
		mgr2.add(fAlignCenterAction);
		mgr2.add(fAlignMiddleAction);
		mgr2.add(fMatchHeightAction);
		mgr2.add(fDistributeHorizontalAction);
		mgr2.update(true);

		ToolBar toolBar3 = new ToolBar(group, SWT.FLAT);
		ToolBarManager mgr3 = new ToolBarManager(toolBar3);
		mgr3.add(fAlignRightAction);
		mgr3.add(fAlignBottomAction);
		mgr3.add(new NullAction());
		mgr3.add(fDistributeVerticalAction);
		mgr3.update(true);

		return group;

	}

	protected void handleSelectionChanged(ISelection oldSelection) {
		fAlignLeftAction.update();
		fAlignCenterAction.update();
		fAlignRightAction.update();
		fAlignTopAction.update();
		fAlignMiddleAction.update();
		fAlignBottomAction.update();
		fMatchWidthAction.update();
		fMatchHeightAction.update();
		fDistributeHorizontalAction.update();
		fDistributeVerticalAction.update();
	}

	protected void handleEditorPartChanged(IEditorPart oldEditorPart) {
		IEditorPart newEditorPart = getEditorPart();
		
		fAlignLeftAction.setWorkbenchPart(newEditorPart);
		fAlignCenterAction.setWorkbenchPart(newEditorPart);
		fAlignRightAction.setWorkbenchPart(newEditorPart);
		fAlignTopAction.setWorkbenchPart(newEditorPart);
		fAlignMiddleAction.setWorkbenchPart(newEditorPart);
		fAlignBottomAction.setWorkbenchPart(newEditorPart);
		fMatchWidthAction.setWorkbenchPart(newEditorPart);
		fMatchHeightAction.setWorkbenchPart(newEditorPart);
		fDistributeHorizontalAction.setWorkbenchPart(newEditorPart);
		fDistributeVerticalAction.setWorkbenchPart(newEditorPart);
		fShowDistributeBoxAction.setWorkbenchPart(newEditorPart);
	}
}
