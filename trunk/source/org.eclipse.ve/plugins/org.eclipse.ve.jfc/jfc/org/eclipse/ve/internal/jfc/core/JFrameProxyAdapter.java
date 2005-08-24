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
 *  $RCSfile: JFrameProxyAdapter.java,v $
 *  $Revision: 1.15 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * Proxy adapter for javax.swing.JFrame. Need to setDefaultCloseOperation to WindowConstants.DO_NOTHING_ON_CLOSE so that the live window can't be
 * closed if the user tries to close it from the task bar or from the window itself.
 */
public class JFrameProxyAdapter extends FrameProxyAdapter {

	/**
	 * Constructor for JFrameProxyAdapter.
	 * 
	 * @param domain
	 */
	public JFrameProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * @see Adapter#setTarget(ComponentManagerFeedbackControllerNotifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			EStructuralFeature sfDefaultCloseOperation = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget,
					JFCConstants.SF_JFRAME_DEFAULTCLOSEPERATION);
			// KLUDGE This is so that the frame won't be closed by accident if someone selected the live frame from the toolbar and
			// tried to close it. If we ever get to where we can hide these from the toolbar then we can get rid of this. But
			// have to be careful of certain window managers which move things from off-screen to on screen, e.g. one that is on Linux does this.
			overrideProperty(sfDefaultCloseOperation, BeanAwtUtilities.getJFrame_DefaultOnClose_DoNothing(getBeanProxyDomain()
					.getProxyFactoryRegistry()), null);
		}
	}

}
