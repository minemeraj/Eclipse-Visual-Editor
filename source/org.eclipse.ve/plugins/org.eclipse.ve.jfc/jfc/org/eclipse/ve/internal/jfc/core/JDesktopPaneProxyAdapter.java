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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JDesktopPaneProxyAdapter.java,v $
 *  $Revision: 1.7 $  $Date: 2005-04-25 16:09:11 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.core.*;

public class JDesktopPaneProxyAdapter extends JLayeredPaneProxyAdapter {
	protected JavaClass fJInternalFrameClass;

	public JDesktopPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());		
		fJInternalFrameClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("javax.swing.JInternalFrame", rset); //$NON-NLS-1$
	}
	/**
     * Activtates the selected JInternalFrame for this desktop pane. 
     * This is public and is used by the edit parts to select a frame 
     * so it can be brought to the front in the desktop pane.
	 */
	public void activateFrame(IJavaObjectInstance internalFrameModel) {
		if (!fJInternalFrameClass.isInstance(internalFrameModel)) return;
		
		// To activate the frame we need to first get the DesktopManager
		// from the DesktopPane, then have the DTM activate the frame.
		IBeanProxy dtMgrBeanProxy = getBeanProxy().getTypeProxy().getMethodProxy("getDesktopManager").invokeCatchThrowableExceptions(getBeanProxy());
		if (dtMgrBeanProxy != null) {
			IBeanProxy frameBeanProxy = BeanProxyUtilities.getBeanProxy(internalFrameModel);
            dtMgrBeanProxy.getTypeProxy().getMethodProxy("activateFrame", "javax.swing.JInternalFrame").invokeCatchThrowableExceptions(dtMgrBeanProxy, frameBeanProxy);
		}
		revalidateBeanProxy();
	}
	/**
	 * @see BeanProxyAdapter#applied(EStructuralFeature, Object, int)
	 */
	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		if (sf == sfContainerComponents) {
			// We are adding a new guy. Need to make it always visible.
			// Get the value of the constraint attribute of the component, the component in this case is a ConstraintsComponent.
			if (((EObject) newValue).eIsSet(sfConstraintComponent)) {
				IJavaInstance constraintAttributeValue = (IJavaInstance) ((EObject) newValue).eGet(sfConstraintComponent);
				ComponentProxyAdapter componentProxyHost = (ComponentProxyAdapter) BeanProxyUtilities.getBeanProxyHost(constraintAttributeValue);
				componentProxyHost.applyVisibility(false, Boolean.TRUE);
			}
		}		
		super.applied(sf, newValue, position);				
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		super.releaseBeanProxy();
	}

}
