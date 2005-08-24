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
 *  $RCSfile: JDesktopPaneProxyAdapter.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IExpression;
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
		if (isBeanProxyInstantiated()) {
			if (!fJInternalFrameClass.isInstance(internalFrameModel))
				return;

			// To activate the frame we need to first get the DesktopManager
			// from the DesktopPane, then have the DTM activate the frame.
			IBeanProxy dtMgrBeanProxy = getBeanProxy().getTypeProxy().getMethodProxy("getDesktopManager").invokeCatchThrowableExceptions( //$NON-NLS-1$
					getBeanProxy());
			if (dtMgrBeanProxy != null) {
				IBeanProxy frameBeanProxy = BeanProxyUtilities.getBeanProxy(internalFrameModel);
				dtMgrBeanProxy.getTypeProxy().getMethodProxy("activateFrame", "javax.swing.JInternalFrame").invokeCatchThrowableExceptions( //$NON-NLS-1$ //$NON-NLS-2$
						dtMgrBeanProxy, frameBeanProxy);
			}
			revalidateBeanProxy();
		}
	}
	
	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {		
		if (feature == sfContainerComponents) {
			if (((EObject) value).eIsSet(sfConstraintComponent)) {
				IJavaInstance constraintAttributeValue = (IJavaInstance) ((EObject) value).eGet(sfConstraintComponent);
				ComponentProxyAdapter componentProxyHost = (ComponentProxyAdapter) getSettingBeanProxyHost(constraintAttributeValue);
				componentProxyHost.overrideVisibility(true, expression);
			}
		}				
		super.applySetting(feature, value, index, expression);
	}
	
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {				
		super.cancelSetting(feature, oldValue, index, expression);
		if (feature == sfContainerComponents) {
			// We are removing a guy. Need to remove the visibility override.
			// Get the value of the constraintComponent attribute of the component, the component in this case is a ConstraintsComponent.
			if (((EObject) oldValue).eIsSet(sfConstraintComponent)) {
				IJavaInstance constraintAttributeValue = (IJavaInstance) ((EObject) oldValue).eGet(sfConstraintComponent);
				((ComponentProxyAdapter) getSettingBeanProxyHost(constraintAttributeValue)).removeVisibilityOverride(expression);
			}
		}		
	}
}
