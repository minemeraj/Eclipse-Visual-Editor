/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: FlowLayoutPolicyHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:48 $ 
 */

import java.util.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * FlowLayout policy helper.
 *
 * Note: There is no constraint, so constraint should always
 * be a null;
 */
public class FlowLayoutPolicyHelper extends LayoutPolicyHelper {
	
	public FlowLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
	}
	
	public FlowLayoutPolicyHelper() {
	}
	
	/**
	 * Doesn't have any constraint, so we return none.
	 */
	public List getDefaultConstraint(List children) {
		return Collections.nCopies(children.size(), null);
	}
	
	protected IJavaObjectInstance convertConstraint(Object constraint) {
		return null;	// No constraint to convert
	}

}
