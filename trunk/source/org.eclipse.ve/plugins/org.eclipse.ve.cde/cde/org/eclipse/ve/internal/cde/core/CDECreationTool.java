package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: CDECreationTool.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.*;
import org.eclipse.gef.requests.*;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.graphics.Cursor;

/**
 * @author JoeWin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class CDECreationTool extends CreationTool {
	protected Cursor editPolicyCursor;	
	protected CDECreationTool(CreationFactory aFactory){
		super(aFactory);
	}
	protected CDECreationTool(){
	}
	
	protected Request createTargetRequest(){
		CreateRequest request = new CDECreateRequest();
		request.setFactory(getFactory());
		return request;
	}	

	protected void showTargetFeedback(){
		super.showTargetFeedback();
		Object cursor = ((CDECreateRequest)getTargetRequest()).get(Cursor.class);
		if ( cursor instanceof Cursor ){
			editPolicyCursor = (Cursor)cursor;
		}
	}
	protected Cursor getEditPolicyCursor(){
		if ( editPolicyCursor != null ) {
			return editPolicyCursor;
		} else {
			return getDefaultCursor();
		}
	}
	/**
	 * Determines (and returns) the appropriate cursur.
	 */
	protected Cursor calculateCursor(){
		Command command = getCurrentCommand();
		if (command == null || !command.canExecute()) {
			return getDisabledCursor(); 
		} else {
			return getEditPolicyCursor();
		}
	}
	protected void eraseTargetFeedback(){
		super.eraseTargetFeedback();
		Object cursor = ((CDECreateRequest)getTargetRequest()).get(Cursor.class);
		if ( cursor == null && editPolicyCursor != null ) {
			setCursor(getDefaultCursor());
			editPolicyCursor.dispose();			
			editPolicyCursor = null;		
		}
	}	
}
