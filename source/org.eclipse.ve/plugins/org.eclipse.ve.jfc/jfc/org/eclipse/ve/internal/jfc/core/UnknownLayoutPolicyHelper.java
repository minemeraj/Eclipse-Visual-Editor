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
 *  $RCSfile: UnknownLayoutPolicyHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
/**
 * Layout Policy Helper for unknown layouts. We can't handle
 * constraint. So no default constraint.
 *
 * Note: the constraint type is unknown, so the constraint should
 * either be a null or it should be a java object instance. Though
 * how it could be anything but a null is questionable.
 */
public class UnknownLayoutPolicyHelper extends LayoutPolicyHelper {
	
	public UnknownLayoutPolicyHelper(ContainerPolicy ep) {
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
	
}