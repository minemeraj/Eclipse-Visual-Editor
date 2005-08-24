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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: IJTabbedPanePageListener.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
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
