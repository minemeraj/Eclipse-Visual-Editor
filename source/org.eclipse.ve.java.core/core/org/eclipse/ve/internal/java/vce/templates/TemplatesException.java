/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce.templates;
/*
 *  $RCSfile: TemplatesException.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.core.runtime.*;

public class TemplatesException extends CoreException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 4400402133644701609L;
	/**
	 *  Make it easier to deal with IStatus
	 */
	public TemplatesException(String msg, Throwable e) {
		super(new Status(IStatus.ERROR, "org.eclipse.ve.java.core", 0, msg, e)); //$NON-NLS-1$
	}
	public TemplatesException(String msg) {
		this(msg, null);
	}
	public TemplatesException(Throwable e) {
		this(e.getMessage(), e); //$NON-NLS-1$
	}
}
