package org.eclipse.ve.internal.cde.palette;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ICDEToolEntry.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:01 $ 
 */

/**
 * @author pwalker
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface ICDEToolEntry {
	
	public boolean isDefaultEntry();
	public void setDefaultEntry(boolean defaultEntry);

}
