package org.eclipse.ve.internal.java.codegen.editorpart;
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
 *  $RCSfile: IJVEStatus.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-26 23:08:01 $ 
 */

import org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus;
import org.eclipse.ve.internal.java.codegen.core.IJVEStatusChangeListener;

/**
 * @version 	1.0
 * @deprecated NEED to get rid of this
 * @author
 */
public interface IJVEStatus extends ICodeGenStatus {
    
    public final static int  NORMAL_MSG   = 0 ;
    public final static int  ERROR_MSG    = 1 ;
    public final static int  WARNING_MSG  = 2 ;
    
	public final static String STATUS_CATEGORY_SYNC_STATUS = "JVE_SyncStatus" ;   //$NON-NLS-1$
	public final static String STATUS_CATEGORY_SYNC_ACTION = "JVE_SyncAction" ;   //$NON-NLS-1$
	
	public final static String STATUS_MSG_SYNC_STATUS_INSYNC = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_INSYNC") ;  //$NON-NLS-1$
	public final static String STATUS_MSG_SYNC_STATUS_OUTOFSYNC = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_NOT_IN_SYNC") ;  //$NON-NLS-1$
	public final static String STATUS_MSG_SYNC_STATUS_SYNCING = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_SYNCHRONIZING") ;  //$NON-NLS-1$
	public final static String STATUS_MSG_SYNC_STATUS_RELOAD = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_RELOAD") ;  //$NON-NLS-1$
	public final static String STATUS_MSG_SYNC_STATUS_PAUSE = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_PAUSE") ;  //$NON-NLS-1$
	public final static String STATUS_MSG_SYNC_STATUS_PARSE_ERROR = CodegenEditorPartMessages.getString("JVE_STATUS_MSG_ERROR") ; //$NON-NLS-1$
	
    
    public  void showMsg(String msg,int kind) ;
    public  void addStatusListener(IJVEStatusChangeListener sl) ;
    public  void removeStatusListener(IJVEStatusChangeListener sl) ;
    

}
