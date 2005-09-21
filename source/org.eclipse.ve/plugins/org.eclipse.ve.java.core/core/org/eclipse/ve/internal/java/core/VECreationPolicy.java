/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VECreationPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-09-21 10:39:50 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.ve.internal.cde.core.CDECreationTool.CreationPolicy;
 

/**
 * This is the base class for VE Creation Policies. This should be used
 * only by those classes that need a different super() string in the
 * constructor.
 * <p>
 * This is only used by codegen.
 * TODO This needs to be re-thought out. It is a bit of kludge here.
 * 
 * @since 1.1.0
 */
public abstract class VECreationPolicy implements CreationPolicy {

	/**
	 * 
	 * 
	 * @since 1.1.0
	 */
	public VECreationPolicy() {
		super();
	}

	/**
	 * TODO This doesn't belong here. It should be create default ctor itself, instead of just the super. For example for Dialog it
	 * should by default create a ctor that takes a Frame, and use super(framepassedin) and
	 * not create a default ctor that does a super(new Frame).
	 * <p>
	 *  Optionally overides the default null constructor super string.
	 *  e.g., <code>super(new Arg1())</code>.
	 *  
	 * @param superClass
	 * @return <code>null</code> if no override is needed, String if an override exists
	 * 
	 * @since 1.0.0
	 */
	public abstract String getDefaultSuperString(EClass superClass);
	
}
