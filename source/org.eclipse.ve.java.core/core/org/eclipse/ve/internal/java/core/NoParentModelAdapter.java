/*
 * Created on Jun 20, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NoParentModelAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.ve.internal.cde.core.IContainmentHandler;

/**
 * @author richkulp
 *
 * Simple class used by the Events stuff so that none of the events
 * can be made a child of anything through drag/drop. Currently they
 * can only be made through the Wizard which will handle correct parentage.
 */
public class NoParentModelAdapter implements IContainmentHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#isParentValid(java.lang.Object)
	 */
	public boolean isParentValid(Object parent) {
		return false;
	}

}
