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
 *  $RCSfile: FFOnlyModelAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.ve.internal.cde.core.IContainmentHandler;
import org.eclipse.ve.internal.cdm.DiagramData;

public class FFOnlyModelAdapter extends ComponentModelAdapter implements IContainmentHandler {
	
	public FFOnlyModelAdapter(Object model) {
		super(model);
	}

	/**
	 * @see IContainmentHandler#isParentValid(Object)
	 */
	public boolean isParentValid(Object parent) {
		// return true only for parents that are the freeform surface
		return parent instanceof DiagramData;
	}
}
