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
 *  $RCSfile: IJVEActionField.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.swt.events.MouseListener;
import org.eclipse.ui.texteditor.IStatusField;

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface IJVEActionField extends IStatusField {
	
	/**
	 *   This interface renders 
	 */
	void setPause(boolean flag);
	void setError(boolean flag);
	void setPauseListener(MouseListener listener) ;
	void unsetPauseListener(MouseListener listener) ;
	void setSrc2Model(boolean flag);	// NOTE: Must be called from UI Thread
	void setModel2Src(boolean flag) ;	// NOTE: Must be called from UI Thread
	void dispose() ;

}
