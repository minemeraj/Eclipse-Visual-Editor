/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabFolderPreviousObjectAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;
 
import org.eclipse.jface.action.Action;

/**
 * This action will select the next page in a CTabFolder.
 */
public class CTabFolderPreviousObjectAction extends Action {
	
	protected CTabFolderGraphicalEditPart editPart;
	
	public CTabFolderPreviousObjectAction(CTabFolderGraphicalEditPart editPart) {
		super(SWTMessages.TabFolderPreviousObjectAction_Text); 
		this.editPart = editPart;
		// While here set enablement. It shouldn't change because this action should be recreated each time popped up.
		int cp = editPart.getCurrentPageIndex();
		setEnabled((cp-1) >= 0);
	}
	
	public void run() {
		editPart.selectPreviousPage();
	}
}
