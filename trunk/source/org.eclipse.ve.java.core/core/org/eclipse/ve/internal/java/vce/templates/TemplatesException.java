package org.eclipse.ve.internal.java.vce.templates;
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
 *  $RCSfile: TemplatesException.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:55 $ 
 */

import org.eclipse.core.runtime.*;

public class TemplatesException extends CoreException {

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
