package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTabbedPaneNextObjectAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.jface.action.Action;

/**
 * This action will select the next page in a JTabbedPane.
 */
public class JTabbedPaneNextObjectAction extends Action {
	
	protected JTabbedPaneGraphicalEditPart editPart;
	
	public JTabbedPaneNextObjectAction(JTabbedPaneGraphicalEditPart editPart) {
		super(VisualMessages.getString("JTabbedPaneNextObjectAction.Text")); //$NON-NLS-1$
		this.editPart = editPart;
		// While here set enablement. It shouldn't change because this action should be recreated each time popped up.
		int cp = editPart.getCurrentPageIndex();
		setEnabled((cp+1) < editPart.getChildren().size());
	}
	
	public void run() {
		editPart.selectNextPage();
	}
}
