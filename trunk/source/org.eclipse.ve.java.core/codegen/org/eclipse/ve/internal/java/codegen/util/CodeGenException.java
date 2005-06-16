package org.eclipse.ve.internal.java.codegen.util;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodeGenException.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:06 $ 
 */

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class CodeGenException extends org.eclipse.core.runtime.CoreException {	 

/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -4111071752944891135L;
/**
 *  Make it easier to deal with IStatus
 */
public CodeGenException(String msg,Throwable e) {
	super(new Status(IStatus.ERROR, "org.eclipse.ve.java.core", 0, msg, e));		 //$NON-NLS-1$
}
public CodeGenException(String msg) {
	this(msg,null) ;
}
public CodeGenException(Throwable e) {
	this(e.getMessage(),e) ; //$NON-NLS-1$
}
}