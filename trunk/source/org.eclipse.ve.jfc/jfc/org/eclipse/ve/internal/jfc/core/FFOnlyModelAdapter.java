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
 *  $Revision: 1.4 $  $Date: 2005-09-14 23:30:22 $ 
 */

import java.util.List;

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.core.IContainmentHandler;

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

	public ChildFeature getChildToAdd(Object parent, List features) {
		// TODO Auto-generated method stub
		return null;
	}
}
