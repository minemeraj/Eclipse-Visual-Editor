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
 *  $RCSfile: IJTabbedPanePageListener.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:42:05 $ 
 */

import java.util.EventListener;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

/**
 * Listener for when a JTabbedPane page is selected.
 */
public interface IJTabbedPanePageListener extends EventListener {

	/*
	 * The page of a JTabbedPane has been selected
	 */
	public void pageSelected(IJavaObjectInstance page);
}
