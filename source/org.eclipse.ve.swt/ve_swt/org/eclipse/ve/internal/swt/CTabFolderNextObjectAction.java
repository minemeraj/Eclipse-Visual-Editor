/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabFolderNextObjectAction.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-22 16:24:10 $ 
 */
package org.eclipse.ve.internal.swt;
 
import org.eclipse.jface.action.Action;

/**
 * This action will select the next page in a CTabFolder.
 */
public class CTabFolderNextObjectAction extends Action {
	
	protected CTabFolderGraphicalEditPart editPart;
	
	public CTabFolderNextObjectAction(CTabFolderGraphicalEditPart editPart) {
		super(SWTMessages.TabFolderNextObjectAction_Text); 
		this.editPart = editPart;
		// While here set enablement. It shouldn't change because this action should be recreated each time popped up.
		int cp = editPart.getCurrentPageIndex();
		setEnabled((cp+1) < editPart.getChildren().size());
	}
	
	public void run() {
		editPart.selectNextPage();
	}
}