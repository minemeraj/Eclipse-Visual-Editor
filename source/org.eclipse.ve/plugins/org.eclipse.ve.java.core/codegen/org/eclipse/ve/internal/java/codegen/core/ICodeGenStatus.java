package org.eclipse.ve.internal.java.codegen.core;
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
 *  $RCSfile: ICodeGenStatus.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface ICodeGenStatus {
	
	public static int  JVE_CODEGEN_STATUS_SYNCHING				= 0x01 ;
	public static int  JVE_CODEGEN_STATUS_OUTOFSYNC 				= 0x02 ;
	public static int  JVE_CODEGEN_STATUS_UPDATING_SOURCE 		= 0x04 ;
	public static int  JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL 		= 0x08 ;
	public static int  JVE_CODEGEN_STATUS_PAUSE					= 0x10 ;
	public static int  JVE_CODEGEN_STATUS_PARSE_ERRROR			= 0x20 ;
	public static int  JVE_CODEGEN_STATUS_RELOAD_PENDING          = 0x40 ;
	public static int  JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS      = 0x80 ;
	
	// Completed update of JVE Model from source.
	public static int  JVE_CODEGEN_STATUS_UPDATE_JVE_MODEL_COMPLETE	= 0x100; 
	
	
	/**
	 * Enable/Disable a state
	 */
	public void setStatus(int flag, boolean state) ;
	/**
	 * Get the complete state 
	 */
	public int  getState() ;
	/**
	 * check for specific status
	 */
	public boolean isStatusSet(int state) ;
	/**
	 * This method will set the RELOAD_PENDING flag, as well
	 * as keep a counter for outstanding pending request
	 * @return if the reload counter > 0
	 */
	public boolean setReloadPending(boolean flag) ;
		

}
