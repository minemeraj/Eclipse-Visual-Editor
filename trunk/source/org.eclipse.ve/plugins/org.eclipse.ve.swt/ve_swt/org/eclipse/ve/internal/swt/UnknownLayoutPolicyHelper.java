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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: UnknownLayoutPolicyHelper.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:56 $ 
 */

import java.util.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Layout Policy Helper for unknown layouts. We can't handle
 * constraint. So no default constraint.
 *
 * Note: the constraint type is unknown, so the constraint should
 * either be a null or it should be a java object instance. Though
 * how it could be anything but a null is questionable.
 */
public class UnknownLayoutPolicyHelper extends LayoutPolicyHelper {
	
	public UnknownLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
	}
	
	public UnknownLayoutPolicyHelper() {
	}
	
	/**
	 * Don't know what kind of constraint is valid, so we return none.
	 */
	public List getDefaultConstraint(List children) {
		return Collections.nCopies(children.size(), null);
	}
	
	protected IJavaObjectInstance convertConstraint(Object constraint) {
		return (IJavaObjectInstance) constraint;	// No constraint to convert, if there is one it should already be a java object instance or null
	}

	protected void cancelConstraints(CommandBuilder commandBuilder, List children) {
		// TODO Auto-generated method stub		
	}
	
}
