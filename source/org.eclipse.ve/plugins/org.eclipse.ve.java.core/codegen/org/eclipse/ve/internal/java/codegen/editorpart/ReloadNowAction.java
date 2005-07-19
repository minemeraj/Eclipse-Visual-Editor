/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ReloadNowAction.java,v $
 *  $Revision: 1.5 $  $Date: 2005-07-19 22:58:38 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.jface.action.Action;
import org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorReloadActionController.IReloadCallback; 

/**
 * Do a reload when run action. This will not cycle through pause/reload, it will just reload when run.
 * This is used by the Status field for JVE status field double-click.
 * <p>
 * It will use the ReloadAction's IReloadCallback to perform the reload.
 * 
 * @since 1.0.0
 */
public class ReloadNowAction extends Action {
	/**
	 * Action ID for Reload action.
	 */
	public static final String RELOADNOW_ACTION_ID = "org.eclipse.ve.java.core.ReloadNow"; //$NON-NLS-1$

	protected IReloadCallback reloadCallback;
	
	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public ReloadNowAction(IReloadCallback reloadCallback) {
		super();
		setId(RELOADNOW_ACTION_ID);
		this.reloadCallback = reloadCallback;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		reloadCallback.reload(true);
	}

}
