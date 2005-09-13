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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: LayoutSwitcher.java,v $
 *  $Revision: 1.9 $  $Date: 2005-09-13 18:55:38 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.*;
/**
 * Base LayoutSwitcher class.
 */
public abstract class LayoutSwitcher implements ILayoutSwitcher {
	protected VisualContainerPolicy policy;
	
	public LayoutSwitcher(VisualContainerPolicy policy) {
		this.policy = policy;
	}
	
	/**
	 * Get the command to orphan the children and then add them back at the 
	 * new calculated constraints.
	 */
	public Command getCommand(EStructuralFeature sf, IJavaInstance newManager) {
		IJavaObjectInstance compositeBean = getCompositeBean();
		
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain(), "change layout"); //$NON-NLS-1$
		
		List components = (List) compositeBean.eGet(JavaInstantiation.getSFeature(compositeBean, SWTConstants.SF_COMPOSITE_CONTROLS));		
		if (!components.isEmpty()) {
			// Get the layout policy helper class from the layout policy factory and
			// set it in the container helper policy for the current layout, so that we can switch out.
			IBeanProxy containerProxy = BeanProxyUtilities.getBeanProxy(compositeBean);			
			ILayoutPolicyFactory lpFactory = BeanSWTUtilities.getLayoutPolicyFactory(containerProxy, policy.getEditDomain());
			ILayoutPolicyHelper lpHelper = lpFactory.getLayoutPolicyHelper(policy);			
			cb.append(lpHelper.getOrphanConstraintsCommand(components));
		}
		// Set the new layout into the composite 
		cb.applyAttributeSetting(compositeBean, sf, newManager);
		
		// Now get the change constraint commands themselves to add the children at the right constraints.
		if (!components.isEmpty())
			cb.append(getChangeConstraintsCommand(components));
			
		return cb.getCommand();
	}
	
	/**
	 * Get the cancel command to orphan the children and then add them back at the 
	 * new calculated constraints.
	 */
	public Command getCancelCommand(EStructuralFeature sf, IBeanProxy defaultManager) {
		IJavaObjectInstance containerBean = getCompositeBean();
		
		if (!containerBean.eIsSet(sf))
			return null;	// Not set, nothing to cancel.
		
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain(), "cancel layout"); //$NON-NLS-1$
		
		List controls = (List) containerBean.eGet(JavaInstantiation.getSFeature(containerBean, SWTConstants.SF_COMPOSITE_CONTROLS));
		
		if (!controls.isEmpty()) {
			// Get the layout policy helper class from the layout policy factory and
			// set it in the container helper policy for the current layout, so that we can switch out.
			ILayoutPolicyFactory lpFactory = BeanSWTUtilities.getLayoutPolicyFactoryFromLayout(defaultManager, policy.getEditDomain());
			ILayoutPolicyHelper lpHelper = lpFactory.getLayoutPolicyHelper(policy);			
			cb.append(lpHelper.getOrphanConstraintsCommand(controls));
		}

		cb.cancelAttributeSetting(containerBean, sf);
		
		// Now get the change constraint commands themselves to add the children at the right constraints.
		if (!controls.isEmpty())
			cb.append(getChangeConstraintsCommand(controls));
			
		return cb.getCommand();
	}	
	
	/**
	 * The container bean.
	 */
	protected IJavaObjectInstance getCompositeBean() {
		return (IJavaObjectInstance) policy.getContainer();
	}
	
	/*
	 * Get the command to the right constraints for the list of children.
	 * This allows the switcher to determine the order to add them back and whether
	 * they all can be added back. 
	 * The children are the actual components, not the constraintComponents. The constraint components
	 * should be reused if possible.
	 * 
	 * This command should handle the pre/postSet rules for the constraints being added.
	 */
	protected abstract Command getChangeConstraintsCommand(List children);
}
