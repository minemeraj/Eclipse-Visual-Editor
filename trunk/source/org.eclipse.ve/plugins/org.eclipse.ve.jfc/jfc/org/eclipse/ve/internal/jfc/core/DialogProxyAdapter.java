/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on May 5, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: DialogProxyAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:48 $ 
 */

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * java.awt.Dialog Proxy Adapter
 */
public class DialogProxyAdapter extends FrameConstructorProxyAdapter {

	// Need these features often, but they depend upon the class we are in,
	// can't get them as statics because they would be different for each Eclipse project.
	protected EStructuralFeature sfDialogModal;

	/**
	 * @param domain
	 */
	public DialogProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	/*
	 * @see Adapter#setTarget(Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sfDialogModal = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_DIALOG_MODAL);
	}	
	
	/**
	 * @see BeanProxyAdapter#primInstantiateBeanProxy()
	 */
	protected void primInstantiateBeanProxy() {
		// KLUDGE This is a super duper kludge. Need a better way to do this. We can't let the Dialog default
		// to modal because when setVisible is done, this will lock up until the dialog is dismissed, which
		// we can't do because it is not within view of the user. So here we will make sure that the default
		// setting for modal is false.
		//

		super.primInstantiateBeanProxy();
		
		if (isBeanProxyInstantiated()) {
			IJavaInstance modal = BeanProxyUtilities.wrapperBeanProxy(
				getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(false),
				((EObject)target).eResource().getResourceSet(),
				null,
				true);
			super.applied(sfDialogModal, modal, 0);	// Force it not modal.
		}
	}

	/**
	 * @see BeanProxyAdapter#applied(EStructuralFeature, Object, int)
	 */
	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		if (sf != sfDialogModal)
			super.applied(sf, newValue, position);
	}
}
