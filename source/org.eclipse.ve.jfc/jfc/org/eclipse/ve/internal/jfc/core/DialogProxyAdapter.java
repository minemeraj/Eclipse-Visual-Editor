/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
 *  $RCSfile: DialogProxyAdapter.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * java.awt.Dialog Proxy Adapter
 */
public class DialogProxyAdapter extends FrameConstructorProxyAdapter {

	/**
	 * @param domain
	 */
	public DialogProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	/*
	 * @see Adapter#setTarget(ComponentManagerFeedbackControllerNotifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			EStructuralFeature sfDialogModal = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_DIALOG_MODAL);
			// KLUDGE This is a super duper kludge. Need a better way to do this. We can't let the Dialog default
			// to modal because when setVisible is done, this will lock up until the dialog is dismissed, which
			// we can't do because it is not within view of the user. So here we will make sure that the default
			// setting for modal is false.
			//			
			overrideProperty(sfDialogModal, getBeanProxyFactory().createBeanProxyWith(false), null);
		}
	}		
}
