/*******************************************************************************
 * Copyright (c)  2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: CDECreationTool.java,v $ $Revision: 1.2 $ $Date: 2004-04-01 21:25:25 $
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.graphics.Cursor;

/**
 * This is the base Creation Tool that all CDE editors should use. It does: <bl>
 * <li>Uses CDECreateRequest so that CDE tools can add info to the request for later policies to use.
 * <li>Will use the Cursor out of the CDECreateRequest so that edit policies can return their specific cursor to use.
 * <li>Uses the CDEUtilities.calculateCursor to return the appropriate cursor for the state of the domain.
 * <li>Dispose of the cursor from the CDECreateRequest when no longer needed. </bl>
 * 
 * @author JoeWin
 */
public abstract class CDECreationTool extends CreationTool {

	protected Cursor editPolicyCursor;

	protected CDECreationTool(CreationFactory aFactory) {
		super(aFactory);
	}

	protected CDECreationTool() {
	}

	protected Request createTargetRequest() {
		CreateRequest request = new CDECreateRequest();
		request.setFactory(getFactory());
		return request;
	}

	protected void showTargetFeedback() {
		super.showTargetFeedback();
		Object cursor = ((CDECreateRequest) getTargetRequest()).get(Cursor.class);
		if (cursor instanceof Cursor) {
			editPolicyCursor = (Cursor) cursor;
		}
	}

	protected Cursor getEditPolicyCursor() {
		if (editPolicyCursor != null) {
			return editPolicyCursor;
		} else {
			return getDefaultCursor();
		}
	}


	protected Cursor calculateCursor() {
		Cursor result = CDEUtilities.calculateCursor((EditDomain) getDomain());
		if (result != null)
			return result;
		Command command = getCurrentCommand();
		if (command == null || !command.canExecute()) {
			return getDisabledCursor();
		} else {
			return getEditPolicyCursor();
		}
	}

	protected void eraseTargetFeedback() {
		super.eraseTargetFeedback();
		Object cursor = ((CDECreateRequest) getTargetRequest()).get(Cursor.class);
		if (cursor == null && editPolicyCursor != null) {
			setCursor(getDefaultCursor());
			editPolicyCursor.dispose();
			editPolicyCursor = null;
		}
	}
}
