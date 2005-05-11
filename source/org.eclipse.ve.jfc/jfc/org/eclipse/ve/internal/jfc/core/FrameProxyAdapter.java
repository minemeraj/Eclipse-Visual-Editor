/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FrameProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-11 19:01:39 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * Frame Proxy Adapter.
 * <p>
 * This is here so that if no title on a frame a default title is shown. This is because people get confused when they see an untitled frame on the
 * Windows(TM) taskbar. The better way is if we could figure out how to not show it on the taskbar, but that requires non-java code. Maybe in the
 * future we can figure that out.
 * 
 * @since 1.0.0
 */
public class FrameProxyAdapter extends WindowProxyAdapter {

	private EStructuralFeature sfTitle;

	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public FrameProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		sfTitle = JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(domain.getEditDomain()), JFCConstants.SF_FRAME_TITLE);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ContainerProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void applied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		// If title, and override for title set, and valid to set, we will remove the override.
		// Note: use false for honorOverrides because we know override is set and we want to ignore the override for the test.
		// We are doing this in applied because we need to get rid of the override right away. If we didn't, it wouldn't
		// get past the applied and actually be applied.
		if (feature == sfTitle && isOverridePropertySet(feature) && testApplyValidity(expression, testValidity, feature, value, false)) {
			// We are now applying a title and it was previously overridden. So we can remove the override property.
			removeOverrideProperty(feature, expression);
		}
		super.applied(feature, value, index, isTouch, expression, testValidity);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter2#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void canceled(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sfTitle && !isOverridePropertySet(feature)) {
			// We are canceling title and we are not currently overridding, so we need to override now to default.
			overrideTitle(expression);
		}
		super.canceled(feature, value, index, expression);
	}

	private void overrideTitle(IExpression expression) {
		overrideProperty(sfTitle, getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(
				JFCMessages.getString("FrameDefaultTitle")), expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		if (newTarget != null && !((EObject) newTarget).eIsSet(sfTitle)) {
			// No title has been set, so put in the default title.
			overrideTitle(null);
		}

		super.setTarget(newTarget);
	}

}