package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: ICDEDisposable.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

/**
 * Implementers can be disposed when not needed.
 * It is used by the EditDomain to know what data needs to be disposed when
 * the editdomain is disposed.
 * @version 	1.0
 * @author
 */
public interface ICDEDisposable {
	public void dispose();
}
