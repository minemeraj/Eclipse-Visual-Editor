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
 *  $RCSfile: CodeGenSyntaxError.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:47 $ 
 */

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CodeGenSyntaxError extends CodeGenException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -6271589477796727094L;
	/**
	 * Constructor for CodeGenSyntaxError.
	 * @param msg
	 * @param e
	 */
	public CodeGenSyntaxError(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Constructor for CodeGenSyntaxError.
	 * @param msg
	 */
	public CodeGenSyntaxError(String msg) {
		super(msg);
	}

	/**
	 * Constructor for CodeGenSyntaxError.
	 * @param e
	 */
	public CodeGenSyntaxError(Throwable e) {
		super(e);
	}
	public String toString() {
		return "Syntax Error: "+getMessage() ; //$NON-NLS-1$
	}

}
