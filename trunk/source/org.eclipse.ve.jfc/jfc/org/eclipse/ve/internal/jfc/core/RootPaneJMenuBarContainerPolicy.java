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
 *  $RCSfile: RootPaneJMenuBarContainerPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BaseJavaContainerPolicy;

/**
 * @author pwalker
 *
 * Container policy for Swing containers that can have both a rootpane and JMenuBar
 * such as JFrame, JDialog, JApplet, and JInternalFrame.
 */
public class RootPaneJMenuBarContainerPolicy extends BaseJavaContainerPolicy {

	private EStructuralFeature sfJMenuBar, sfContentPane;
	
	/**
	 * Constructor for RootPaneJMenuBarContainerPolicy.
	 * @param containmentSF
	 * @param domain
	 */
	public RootPaneJMenuBarContainerPolicy(EditDomain domain) {
		super(domain);
	}


	/**
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#setContainer(Object)
	 */
	public void setContainer(Object container) {
		super.setContainer(container);
		if (container != null) {
			JavaClass modelType = (JavaClass) ((EObject) container).eClass();
			sfJMenuBar = modelType.getEStructuralFeature("JMenuBar"); //$NON-NLS-1$
			sfContentPane = modelType.getEStructuralFeature("contentPane"); //$NON-NLS-1$			
		}
	}
	
	protected EStructuralFeature getContainmentSF(List children, Object positionBeforeChild, int requestType) {
		// For RootPaneJMenuBar, we can't add/orphan/move if more than one child is selected because
		// that could involve more than one feature.
		if (children.isEmpty() || children.size() > 1)
			return null;
			
		return sfJMenuBar.getEType().isInstance(children.get(0)) ? sfJMenuBar : sfContentPane;
	}

	protected EStructuralFeature getContainmentSF(Object child, Object positionBeforeChild, int requestType) {
		return sfJMenuBar.getEType().isInstance(child) ? sfJMenuBar : sfContentPane;
	}

}
