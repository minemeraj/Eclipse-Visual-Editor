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
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: VisualContainerPolicy.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:47 $ 
 */
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BaseJavaContainerPolicy;

/**
 * A Java container policy for handling visuals with constraints.
 * <p>
 * It is abstract.
 * @since 1.1.0
 */
public abstract class VisualContainerPolicy extends BaseJavaContainerPolicy {

	/**
	 * Construct the policy.
	 * @param feature
	 * @param domain
	 * 
	 * @since 1.1.0.1
	 */
	public VisualContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(feature,domain);
	}
	
	/**
	 * Called to do a create with a given constraint.
	 * @param constraintComponent
	 * @param childComponent
	 * @param position
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public abstract Command getCreateCommand(Object constraintComponent, Object childComponent, Object position);

	/**
	 * Called to do an add with a given constraint.
	 * @param componentConstraints
	 * @param childrenComponents
	 * @param position
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public abstract Command getAddCommand(List componentConstraints, List childrenComponents, Object position);
	
}
