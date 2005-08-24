/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NoFFModelAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.core.IContainmentHandler;
 

/**
 * @author pwalker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NoFFModelAdapter extends ControlModelAdapter implements IContainmentHandler {

	/**
	 * @param aControl
	 */
	public NoFFModelAdapter(Object aControl) {
		super(aControl);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#isParentValid(java.lang.Object)
	 */
	public boolean isParentValid(Object parent) {
		// Parent is valid iff the parent is NOT the canvas
		return !(parent instanceof DiagramData);
	}

}
