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
 *  $RCSfile: ActionBarContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */



import org.eclipse.ui.*;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.*;

public class ActionBarContributor extends org.eclipse.gef.ui.actions.ActionBarContributor {
	public ActionBarContributor(){
	}

/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#createActions()
 */
protected void buildActions() {
	addRetargetAction(new UndoRetargetAction());
	addRetargetAction(new RedoRetargetAction());
	addRetargetAction(new DeleteRetargetAction());
}
/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
 */
protected void declareGlobalActionKeys() {
	addGlobalActionKey(IWorkbenchActionConstants.PRINT);
}

/**
 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(IToolBarManager)
 */
public void contributeToToolBar(IToolBarManager tbm) {
	tbm.add(getAction(IWorkbenchActionConstants.UNDO));
	tbm.add(getAction(IWorkbenchActionConstants.REDO));
	tbm.add(getAction(IWorkbenchActionConstants.DELETE));
// TODO Need to revisit how these actions work.
//	tbm.add(getAction(ZoomAction.ACTION_ID));
//	tbm.add(getAction(ZoomInAction.ACTION_ID));
//	tbm.add(getAction(ZoomOutAction.ACTION_ID));
//
//	tbm.add(getAction(ShowGridAction.ACTION_ID));
//	tbm.add(getAction(SnapToGridAction.ACTION_ID));
//	tbm.add(getAction(GridPropertiesAction.ACTION_ID));

//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.LEFT_ALIGN)));
//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.CENTER_ALIGN)));
//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.RIGHT_ALIGN)));
//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.TOP_ALIGN)));
//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.MIDDLE_ALIGN)));
//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.BOTTOM_ALIGN)));
//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.MATCH_WIDTH)));
//	tbm.add(getAction(AlignmentAction.getActionId(AlignmentCommandRequest.MATCH_HEIGHT)));
//	
//	tbm.add(getAction(ShowDistributeBoxAction.ACTION_ID));
//	tbm.add(getAction(DistributeAction.getActionId(DistributeCommandRequest.HORIZONTAL)));
//	tbm.add(getAction(DistributeAction.getActionId(DistributeCommandRequest.VERTICAL)));
}
}