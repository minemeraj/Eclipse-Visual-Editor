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
package org.eclipse.ve.internal.java.codegen.util;
/*
 *  $RCSfile: CodeGenInfoMissing.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:46 $ 
 */

/**
 *  This exception is typically used to denote that not enough information
 *  is available to continue processing (typically it is a request to pospone
 *  executing until a first path is completed).  
 */
public class CodeGenInfoMissing extends CodeGenException {
/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -5111559429107726766L;

public CodeGenInfoMissing(String msg) {
	super(msg) ;
}

}
