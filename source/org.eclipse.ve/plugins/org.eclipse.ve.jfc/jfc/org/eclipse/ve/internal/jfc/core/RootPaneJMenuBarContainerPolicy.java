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
 *  $RCSfile: RootPaneJMenuBarContainerPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:18:06 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.ve.internal.java.core.JavaMultiFeatureContainerPolicy;

/**
 * @author pwalker
 *
 * Container policy for Swing containers that can have both a rootpane and JMenuBar
 * such as JFrame, JDialog, JApplet, and JInternalFrame.
 */
public class RootPaneJMenuBarContainerPolicy extends JavaMultiFeatureContainerPolicy {

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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.JavaMultiFeatureContainerPolicy#getContainmentSF(java.util.List, int)
	 */
	protected EStructuralFeature getContainmentSF(List children, int requestType) {
		// For RootPaneJMenuBar, we can't add/orphan/move if more than one child is selected because
		// that would involve more than one feature.
		if (children.isEmpty() || children.size() > 1)
			return null;
			
		return sfJMenuBar.getEType().isInstance(children.get(0)) ? sfJMenuBar : sfContentPane;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.JavaMultiFeatureContainerPolicy#getContainmentSF(java.lang.Object, int)
	 */
	protected EStructuralFeature getContainmentSF(Object child, int requestType) {
		return sfJMenuBar.getEType().isInstance(child) ? sfJMenuBar : sfContentPane;
	}

}
