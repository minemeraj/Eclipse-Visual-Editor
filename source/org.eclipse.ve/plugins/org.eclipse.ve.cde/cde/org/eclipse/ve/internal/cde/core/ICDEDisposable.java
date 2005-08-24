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
 *  $RCSfile: ICDEDisposable.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
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
