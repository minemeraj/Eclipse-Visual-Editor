package org.eclipse.ve.internal.java.codegen.util;
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
 *  $RCSfile: CodeGenInfoMissing.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

/**
 *  This exception is typically used to denote that not enough information
 *  is available to continue processing (typically it is a request to pospone
 *  executing until a first path is completed).  
 */
public class CodeGenInfoMissing extends CodeGenException {
public CodeGenInfoMissing(String msg) {
	super(msg) ;
}

}