/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JTabbedPaneNextObjectAction.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.jface.action.Action;

/**
 * This action will select the next page in a JTabbedPane.
 */
public class JTabbedPaneNextObjectAction extends Action {
	
	protected JTabbedPaneGraphicalEditPart editPart;
	
	public JTabbedPaneNextObjectAction(JTabbedPaneGraphicalEditPart editPart) {
		super(JFCMessages.JTabbedPaneNextObjectAction_Text); 
		this.editPart = editPart;
		// While here set enablement. It shouldn't change because this action should be recreated each time popped up.
		int cp = editPart.getCurrentPageIndex();
		setEnabled((cp+1) < editPart.getChildren().size());
	}
	
	public void run() {
		editPart.selectNextPage();
	}
}
