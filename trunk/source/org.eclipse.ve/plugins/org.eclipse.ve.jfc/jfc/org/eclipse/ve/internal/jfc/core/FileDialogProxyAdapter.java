package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: FileDialogProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */
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

	/**
	 * @see BeanProxyAdapter#primInstantiateBeanProxy()
	 */
	protected void primInstantiateBeanProxy() {
		super.primInstantiateBeanProxy();
		
		if (isBeanProxyInstantiated()) {
			super.applyVisibility(false, Boolean.FALSE);	// KLUDGE Must never become visible. FileDialogs cause problems if they do.
		}
	}

	/**
	 * @see ComponentProxyAdapter#applyVisibility(boolean, Boolean)
	 */
	public void applyVisibility(boolean apply, Boolean setToVisibility) {
		// KLUDGE There is also another kludge in that we can't let it be visible either. FileDialogs will always show up
		// in the right corner on Windows(TM) no matter what it was set to if it becomes visible.		
		super.applyVisibility(false, Boolean.FALSE);
	}

}