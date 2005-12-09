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
 *  $RCSfile: DistributeAction.java,v $
 *  $Revision: 1.8 $  $Date: 2005-12-09 17:29:13 $ 
 */

import java.util.*;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * Action which provides support for distribution of multiple view objects 
 * within a surface area or within a specific bounding box.
 */
public class DistributeAction extends SelectionAction {
	protected int fDistributeType;
	protected boolean fDistributeInSurface;

	// The resource prefix to use for each alignment type
	// The distribute type id's come from the DistributeCommandRequest.
	// The labels below must match the same order as those id's.
	private final static String[] resPrefixLabels = { 
		CDEMessages.DistributeAction_horizontal_label, 
		CDEMessages.DistributeAction_vertical_label }; 
	private final static String[] resPrefixTooltips = { 
		CDEMessages.DistributeAction_horizontal_tooltip, 
		CDEMessages.DistributeAction_vertical_tooltip }; 
	private final static String[] resPrefixImages = { 
		"distributehorz_obj.gif", //$NON-NLS-1$
		"distributevert_obj.gif" }; //$NON-NLS-1$
	private final static String[] resPrefixIDs = { 
		"DistributeAction.horizontal", //$NON-NLS-1$
		"DistributeAction.vertical" }; //$NON-NLS-1$

	public DistributeAction(int distributeType) {
		super((IWorkbenchPart) null);	// Actual Workbench part will be assigned later.
		// Default to horizontal alignment if the align type is incorrect
		if (!(distributeType >= 0 && distributeType < resPrefixLabels.length))
			fDistributeType = DistributeCommandRequest.HORIZONTAL;
		else
			fDistributeType = distributeType;
		setText(resPrefixLabels[fDistributeType]); //$NON-NLS-1$
		setToolTipText(resPrefixTooltips[fDistributeType]); //$NON-NLS-1$

		String graphicName = resPrefixImages[fDistributeType]; //$NON-NLS-1$
		// The file structure of these is that they exist in the plugin directory with three folder names, e.g.
		// /icons/full/clc16/alignleft_obj.gif for the color one
		// and elc16 for enabled and dlc16 for disasbled
		setImageDescriptor(
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
		setHoverImageDescriptor(getImageDescriptor());
		setDisabledImageDescriptor(
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName)); //$NON-NLS-1$
		setEnabled(true);
		setId(getActionId(fDistributeType));
	}
	/**
	 * Static method that returns the action id based on the alignment type.
	 */
	public static String getActionId(int distributeType) {
		return (
			(distributeType >= 0 && distributeType <= resPrefixIDs.length)
				? resPrefixIDs[distributeType]
				: resPrefixIDs[DistributeCommandRequest.HORIZONTAL]);
	}

	/**
	 * Creates commands to distribute the selected object(s) horizontally or 
	 * vertically within the parent's surface or within a bounding box area. 
	 * @return CompoundCommand with the command(s).
	 */
	protected Command createDistributeCommand(List objects) {
		// Not valid if less then two selected or if this root is not graphical.
		if (objects.size() < 2)
			return UnexecutableCommand.INSTANCE;

		// Need to verify that the editparts are all GraphicalEditParts, if they aren't then we can't distribute.
		Iterator itr = objects.iterator();
		while (itr.hasNext())
			if (!(itr.next() instanceof GraphicalEditPart))
				return UnexecutableCommand.INSTANCE;

		Rectangle boundingBox = getBoundingBox(objects);
		if (boundingBox == null)
			return UnexecutableCommand.INSTANCE;

		DistributeCommandRequest distReq = new DistributeCommandRequest();
		Command cmd = null;
		switch (fDistributeType) {
			case (DistributeCommandRequest.HORIZONTAL) :
				cmd = distributeHorizontally(objects, boundingBox, distReq);
				break;
			case (DistributeCommandRequest.VERTICAL) :
				cmd = distributeVertically(objects, boundingBox, distReq);
				break;
		}
		return cmd;
	}

	/**
	 * Get the distribute bounding box from the contents editpart. If it doesn't have
	 * one, use the outermost editparts to determine the bounding box size.
	 */
	protected Rectangle getBoundingBox(List selObjs) {
		Rectangle boundingBox = null;

		DistributeController dc = DistributeController.getDistributeController((EditPart) selObjs.get(0));
		if (dc == null)
			return null;

		if (dc.isBoxActive())
			boundingBox = dc.getBoundaryBox();

		if (boundingBox == null) {
			// No bounding box from the distribute controller so just get parent figure bounds. They must all have the same parent.
			// set the flag to distribute within the overall surface.
			Iterator itr = selObjs.iterator();
			EditPart contentsEditPart = ((EditPart) itr.next()).getParent();
			while (itr.hasNext()) {
				EditPart ep = (EditPart) itr.next();
				if (ep.getParent() != contentsEditPart)
					return null; // Can't distribute
			}
			boundingBox = new Rectangle(((GraphicalEditPart) contentsEditPart).getFigure().getClientArea());
			fDistributeInSurface = true;
		} else
			fDistributeInSurface = false;

		return boundingBox;
	}

	/**
	 * Creates commands to distribute the selected object(s) across and within
	 * a bounding box or surface area. 
	 * @return CompoundCommand with the command(s).
	 */
	protected Command distributeHorizontally(List inSelObjs, Rectangle boundingBox, DistributeCommandRequest distReq) {
		// need to sort the elements based on their x position within the freeform.
		List selObjs = new ArrayList(inSelObjs); // We will be sorting, so we don't want to change incoming list.
		Collections.sort(selObjs, new Comparator() {
			public int compare(Object o1, Object o2) {
				// Return a positive integer if the x + width value of the
				// first object is greater than the x + width value of the 
				// second object, negative integer if less than, and 
				// zero if they are equal.
				Rectangle r = ((GraphicalEditPart) o1).getFigure().getBounds();
				int x1 = r.x + r.width;
				r = ((GraphicalEditPart) o2).getFigure().getBounds();
				int x2 = r.x + r.width;
				return x1 - x2;
			}
		});
		int summWidths = 0;
		CompoundCommand compoundCmd = new CompoundCommand();
		// Add up the widths of all the objects.
		for (int i = 0; i < selObjs.size(); i++) {
			Rectangle bounds = ((GraphicalEditPart) selObjs.get(i)).getFigure().getBounds();
			summWidths += bounds.width;
		}
		int gap = 0;
		int nextXPos = 0; // starting x position
		if (fDistributeInSurface) {
			gap = (boundingBox.width - summWidths) / (selObjs.size() + 1);
			nextXPos = boundingBox.x + gap;
		} else {
			gap = (boundingBox.width - summWidths) / (selObjs.size() - 1);
			nextXPos = boundingBox.x; // starting x position
		}
		// Modify the x position of each object based on the gap between them.
		// We don't need to change the first or last since they are the 
		// objects at the edges of the boundary box.
		for (int i = 0; i < selObjs.size(); i++) {
			GraphicalEditPart currObj = (GraphicalEditPart) selObjs.get(i);
			Rectangle currRect = currObj.getFigure().getBounds();
			Rectangle newRect = new Rectangle(nextXPos, currRect.y, currRect.width, currRect.height);
			distReq.setBounds(newRect);
			Command cmd = currObj.getCommand(distReq);
			if (cmd == null)
				return UnexecutableCommand.INSTANCE; // Can't distribute because request wasn't processed.
			compoundCmd.append(cmd);
			nextXPos = nextXPos + gap + currRect.width;
		}
		return compoundCmd;
	}
	/**
	 * Creates commands to distribute the selected object(s) vertically and within
	 * a bounding box or surface area. 
	 * @return CompoundCommand with the command(s).
	 */
	protected Command distributeVertically(List inSelObjs, Rectangle boundingBox, DistributeCommandRequest distReq) {
		// need to sort the elements based on their y position within the freeform.
		List selObjs = new ArrayList(inSelObjs); // We will be sorting, so we don't want to change incoming list.	
		Collections.sort(selObjs, new Comparator() {
			public int compare(Object o1, Object o2) {
				// Return a positive integer if the y + height value of the
				// first object is greater than the y + height value of the 
				// second object, negative integer if less than, and 
				// zero if they are equal.
				Rectangle r = ((GraphicalEditPart) o1).getFigure().getBounds();
				int y1 = r.y + r.height;
				r = ((GraphicalEditPart) o2).getFigure().getBounds();
				int y2 = r.y + r.height;
				return y1 - y2;
			}
		});
		int summHeights = 0;
		CompoundCommand compoundCmd = new CompoundCommand();
		// Add up the heights of all the objects.
		for (int i = 0; i < selObjs.size(); i++) {
			Rectangle bounds = ((GraphicalEditPart) selObjs.get(i)).getFigure().getBounds();
			summHeights += bounds.height;
		}
		int gap = 0;
		int nextYPos = 0; // starting y position
		if (fDistributeInSurface) {
			gap = (boundingBox.height - summHeights) / (selObjs.size() + 1);
			nextYPos = boundingBox.y + gap;
		} else {
			gap = (boundingBox.height - summHeights) / (selObjs.size() - 1);
			nextYPos = boundingBox.y;
		}
		// Modify the y position of each object based on the gap between them
		for (int i = 0; i < selObjs.size(); i++) {
			GraphicalEditPart currObj = (GraphicalEditPart) selObjs.get(i);
			Rectangle currRect = currObj.getFigure().getBounds();
			Rectangle newRect = new Rectangle(currRect.x, nextYPos, currRect.width, currRect.height);
			distReq.setBounds(newRect);
			Command cmd = currObj.getCommand(distReq);
			if (cmd == null)
				return UnexecutableCommand.INSTANCE; // Can't distribute because request wasn't processed.
			compoundCmd.append(cmd);
			nextYPos = nextYPos + gap + currRect.height;
		}
		return compoundCmd;
	}

	/**
	 * @see EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		Command cmd = createDistributeCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}

	/**
	 * Creates and executes the aalignment command on the selected objects.
	 */
	public void run() {
		execute(createDistributeCommand(getSelectedObjects()));
	}
	
	public void setWorkbenchPart(IWorkbenchPart part) {
		// To work around GEF's refusal to do the right thing and leave it public.
		super.setWorkbenchPart(part);
	}	
}
