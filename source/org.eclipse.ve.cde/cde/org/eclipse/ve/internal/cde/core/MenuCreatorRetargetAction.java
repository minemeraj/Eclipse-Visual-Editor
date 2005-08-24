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
 *  $RCSfile: MenuCreatorRetargetAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.ui.actions.LabelRetargetAction;
/**
 * This is a RetargetAction which is a drop-down too. 
 */
public class MenuCreatorRetargetAction extends LabelRetargetAction {
	
	// Because the inherited handler is private we must re-declare it
	protected IAction localHandler;
	
	public MenuCreatorRetargetAction(String id, String text){
		super(id,text);
	}
	protected void setActionHandler(IAction newHandler) {
		localHandler = newHandler;
		super.setActionHandler(newHandler);
	}
	
	public int getStyle(){
		return AS_DROP_DOWN_MENU;	// This is because style can only be set in Action ctor, which we don't have access to thru the RetargetAction
	}
	
	public IMenuCreator getMenuCreator() {
		if (localHandler != null) {
			return localHandler.getMenuCreator();
		} else {
			return super.getMenuCreator();
		}		
	}	
}
