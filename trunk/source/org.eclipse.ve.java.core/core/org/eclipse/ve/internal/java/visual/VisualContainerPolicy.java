/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:23:54 $ 
 */
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.JavaContainerPolicy;
 
public abstract class VisualContainerPolicy extends JavaContainerPolicy {

	public VisualContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(feature,domain);
	}
	public abstract Command getCreateCommand(Object constraintComponent, Object childComponent, Object position);

	public abstract Command getAddCommand(List componentConstraints, List childrenComponents, Object position);
	
}
