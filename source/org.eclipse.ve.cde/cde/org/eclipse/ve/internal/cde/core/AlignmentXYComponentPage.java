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
 *  $RCSfile: AlignmentXYComponentPage.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:12:49 $ 
 */

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.*;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;

/**
 * This is the dialog that appears when the user selects Alignment
 */
public class AlignmentXYComponentPage extends CustomizeLayoutPage {

	private AlignmentAction fAlignLeftAction = new AlignmentAction(AlignmentCommandRequest.LEFT_ALIGN);
	private AlignmentAction fAlignRightAction = new AlignmentAction(AlignmentCommandRequest.RIGHT_ALIGN);
	private AlignmentAction fAlignCenterAction = new AlignmentAction(AlignmentCommandRequest.CENTER_ALIGN);
	private AlignmentAction fAlignTopAction = new AlignmentAction(AlignmentCommandRequest.TOP_ALIGN);
	private AlignmentAction fAlignMiddleAction = new AlignmentAction(AlignmentCommandRequest.MIDDLE_ALIGN);
	private AlignmentAction fAlignBottomAction = new AlignmentAction(AlignmentCommandRequest.BOTTOM_ALIGN);
	private AlignmentAction fMatchWidthAction = new AlignmentAction(AlignmentCommandRequest.MATCH_WIDTH);
	private AlignmentAction fMatchHeightAction = new AlignmentAction(AlignmentCommandRequest.MATCH_HEIGHT);
	
	private RestorePreferredSizeAction fRestorePreferredSizeAction = new RestorePreferredSizeAction();
	
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
		fRestorePreferredSizeAction.setSelectionProvider(selectionProvider);
		fDistributeHorizontalAction.setSelectionProvider(selectionProvider);
		fDistributeVerticalAction.setSelectionProvider(selectionProvider);
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
		mgr3.add(fRestorePreferredSizeAction);
		mgr3.add(fDistributeVerticalAction);
		mgr3.update(true);

		return group;

	}

	protected boolean handleSelectionChanged(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;
			boolean enableAll = true;
			if (editparts.get(0) instanceof EditPart && ((EditPart) editparts.get(0)).getParent() != null) {
				firstParent = ((EditPart) editparts.get(0)).getParent();
				// Check the parent to ensure its layout policy is an XYLayout
				if (isValidParent(firstParent)) {
					EditPart ep = (EditPart) editparts.get(0);
					/*
					 * Need to iterate through the selection list and ensure each selection is:
					 * - an EditPart
					 * - they share the same parent
					 * - it's parent has a XYLayout as it's layout manager
					 */
					for (int i = 1; i < editparts.size(); i++) {
						if (editparts.get(i) instanceof EditPart) {
							ep = (EditPart) editparts.get(i);
							// Check to see if we have the same parent
							if (ep.getParent() == null || ep.getParent() != firstParent) {
								enableAll = false;
								break;
							}
						} else {
							enableAll = false;
							break;
						}
					}
					// If the parent is the same, enable all the actions.
					if (enableAll) {
						fAlignLeftAction.update();
						fAlignCenterAction.update();
						fAlignRightAction.update();
						fAlignTopAction.update();
						fAlignMiddleAction.update();
						fAlignBottomAction.update();
						fMatchWidthAction.update();
						fMatchHeightAction.update();
						fRestorePreferredSizeAction.update();
						fDistributeHorizontalAction.update();
						fDistributeVerticalAction.update();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * Return true if the parent's layout policy is a XYLayout.
	 * If parent is a tree editpart (selected from the Beans viewer, we need to get its
	 * corresponding graphical editpart from the Graph viewer in order to check its layout policy.
	 */
	public boolean isValidParent(EditPart parent) {
		if (parent instanceof TreeEditPart) {
			EditDomain ed = EditDomain.getEditDomain(parent);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				// Get the graphical editpart using the model that is common between the two viewers
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(parent.getModel());
				if (ep != null)
					parent = ep;
			}
		}
		IActionFilter af = (IActionFilter) ((IAdaptable) parent).getAdapter(IActionFilter.class);
		if (af != null && af.testAttribute(parent, LAYOUT_FILTER_KEY, XYLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$
			return true;
		}
		return false;
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
		fRestorePreferredSizeAction.setWorkbenchPart(newEditorPart);
		fDistributeHorizontalAction.setWorkbenchPart(newEditorPart);
		fDistributeVerticalAction.setWorkbenchPart(newEditorPart);
		fShowDistributeBoxAction.setWorkbenchPart(newEditorPart);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getLabelForSelection(org.eclipse.jface.viewers.ISelection)
	 */
	protected String getLabelForSelection(ISelection newSelection) {
		if (newSelection instanceof IStructuredSelection) {
			if (((IStructuredSelection)newSelection).size() > 1) {
				return CDEMessages.AlignmentXYComponentPage_multipleSelection; 
			}
		}
		return null;
	}
}
