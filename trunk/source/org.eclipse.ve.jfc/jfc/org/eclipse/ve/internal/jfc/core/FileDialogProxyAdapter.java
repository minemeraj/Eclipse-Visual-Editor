package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: FileDialogProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-11 19:01:38 $ 
 */
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * Proxy adapter for FileDialog.
 * This is needed to prevent the actual instanciation of the FileDialog in the remote VM.
 * Otherwise a modal file dialog starts up prompting us for a file.
 * For now it is just a big kludge.
 */
public class FileDialogProxyAdapter extends DialogProxyAdapter {

	/**
	 * Constructor for FileDialogProxyAdapter.
	 * @param domain
	 */
	public FileDialogProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	
	public void overrideVisibility(boolean visibility, IExpression expression) {
		// KLUDGE It must never become visible. FileDialogs cause problems if they do.
		super.overrideVisibility(false, expression);
	}
}