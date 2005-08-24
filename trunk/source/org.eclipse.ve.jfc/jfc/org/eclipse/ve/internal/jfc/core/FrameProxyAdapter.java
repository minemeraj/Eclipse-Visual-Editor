/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:38:10 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

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

	/**
	 * Handle the frame title. It is used when instantiating and no title set, or when apply and applying
	 * 
	 * @param frame
	 * @param title
	 * @param replaceOld
	 * @param wantOld	<code>true</code> if want the old value.
	 * @param expression
	 * 
	 * @return old value proxy if wantOld is <code>true</code> else return null.
	 * @since 1.1.0
	 */
	protected IProxy handleFrameTitle(IProxy frame, IProxy title, boolean replaceOld, boolean wantOld, IExpression expression) {
		return expression.createSimpleMethodInvoke(BeanAwtUtilities.getWindowApplyFrameTitleMethodProxy(expression),
				null, new IProxy[] {frame, title, expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(replaceOld)}, wantOld);
	}
	
	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		IProxy result = super.primInstantiateBeanProxy(expression);
		if (!getJavaObject().eIsSet(sfTitle)) {
			// Handle applying a default title, and get original value to be used later if title is explicitly set.
			setOriginalValue(sfTitle, handleFrameTitle(result, null, false, true, expression));
		}
		return result;
	}
	
	protected IProxy applyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue) throws NoSuchMethodException, NoSuchFieldException {
		if (propertyDecorator.getEModelElement() == sfTitle) {
			return handleFrameTitle(getProxy(), settingProxy, true, getOriginalValue, expression);
		} else
			return super.applyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
	}
	
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute, IExpression exp, ForExpression forExpression) {
		if (aBeanPropertyAttribute == sfTitle && isSettingInOriginalSettingsTable(aBeanPropertyAttribute)) {
			// This is title, it was not explicitly set, and we have an original settings, so return that instead of
			// default string we use as a title. (Note: we would not be here if explicitly set).
			return (IProxy) getOriginalSettingsTable().get(aBeanPropertyAttribute);
		}
		return super.getBeanPropertyProxyValue(aBeanPropertyAttribute, exp, forExpression);
	}
}
