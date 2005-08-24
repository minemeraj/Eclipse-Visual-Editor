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
package org.eclipse.ve.internal.java.codegen.core;
/*
 *  $RCSfile: IJVEStatusChangeListener.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.util.EventListener;

/**
 * Interface used for those interested in changes to the code gen status.
 * Implementers can change the views or images shown to the user based on the current status.
 * For example, the graph viewer can show an image or figure indicating there
 * is a reload in progress.
 * 
 * The status constants used come from ICodeGenStatus. Some examples are:
 * 		JVE_CODEGEN_STATUS_SYNCHING
 * 		JVE_CODEGEN_STATUS_OUTOFSYNC
 * 		JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS
 */
public interface IJVEStatusChangeListener extends EventListener {
	/**
	 * Notify the listeners the status has changed. 
	 */
	public void statusChanged(int oldStatus,  int newStatus);

}
