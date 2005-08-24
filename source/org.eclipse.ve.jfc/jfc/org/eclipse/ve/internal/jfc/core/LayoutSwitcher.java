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
 *  $RCSfile: LayoutSwitcher.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
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
	public Command getCommand(EStructuralFeature sf, IJavaObjectInstance newManager) {
		IJavaObjectInstance containerBean = getContainerBean();
		
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain(), "change layout"); //$NON-NLS-1$
	
		cb.applyAttributeSetting(containerBean, sf, newManager);	// Change the layout manager.
		
		List constraintComponents = (List) containerBean.eGet(JavaInstantiation.getSFeature(containerBean, JFCConstants.SF_CONTAINER_COMPONENTS));
		ArrayList children = new ArrayList(constraintComponents.size());
		Iterator itr = constraintComponents.iterator();
		EStructuralFeature sfConstraintComponent = JavaInstantiation.getSFeature(containerBean, JFCConstants.SF_CONSTRAINT_COMPONENT);
		while(itr.hasNext()) {
			children.add(((EObject) itr.next()).eGet(sfConstraintComponent));
		}
		
		// Now do an orphan of the constraints. Bit of kludge, it really only orphans the non-constraint constraints, e.g. bounds.
		// First orphan all of the children so that the old constraints are removed.		
		if (!children.isEmpty()) {
			// Get the layout policy helper class from the layout policy factory and
			// set it in the container helper policy for the current layout, so that we can switch out.
			IBeanProxy containerProxy = BeanProxyUtilities.getBeanProxy(containerBean);			
			IBeanProxy layoutManagerProxy = BeanAwtUtilities.invoke_getLayout(containerProxy);
			ILayoutPolicyFactory lpFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerProxy, policy.getEditDomain());
			ILayoutPolicyHelper lpHelper = lpFactory.getLayoutPolicyHelper(policy);			
			cb.append(lpHelper.getOrphanConstraintsCommand(children));
		}
	
		
		// Now get the change constraint commands themselves to add the children at the right constraints.
		if (!children.isEmpty())
			cb.append(getChangeConstraintsCommand(children));
			
		return cb.getCommand();
	}
	
	/**
	 * Get the cancel command to orphan the children and then add them back at the 
	 * new calculated constraints.
	 */
	public Command getCancelCommand(EStructuralFeature sf, IBeanProxy defaultManager) {
		IJavaObjectInstance containerBean = getContainerBean();
		
		if (!containerBean.eIsSet(sf))
			return null;	// Not set, nothing to cancel.
		
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain(), "cancel layout"); //$NON-NLS-1$
		
		cb.cancelAttributeSetting(containerBean, sf);	// Cancel the layout.
		
		List constraintComponents = (List) containerBean.eGet(JavaInstantiation.getSFeature(containerBean, JFCConstants.SF_CONTAINER_COMPONENTS));
		ArrayList children = new ArrayList(constraintComponents.size());
		Iterator itr = constraintComponents.iterator();
		EStructuralFeature sfConstraintComponent = JavaInstantiation.getSFeature(containerBean, JFCConstants.SF_CONSTRAINT_COMPONENT);
		while(itr.hasNext()) {
			children.add(((EObject) itr.next()).eGet(sfConstraintComponent));
		}
		
		// Now do an orphan of the constraints. Bit of kludge, it really only orphans the non-constraint constraints, e.g. bounds.
		// First orphan all of the children so that the old constraints are removed.		
		if (!children.isEmpty()) {
			// Get the layout policy helper class from the layout policy factory and
			// set it in the container helper policy for the current layout, so that we can switch out.
			IBeanProxy containerProxy = BeanProxyUtilities.getBeanProxy(containerBean);	
			IBeanProxy layoutManagerProxy = BeanAwtUtilities.invoke_getLayout(containerProxy);
			ILayoutPolicyFactory lpFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerProxy, policy.getEditDomain());
			ILayoutPolicyHelper lpHelper = lpFactory.getLayoutPolicyHelper(policy);			
			cb.append(lpHelper.getOrphanConstraintsCommand(children));
		}

		
		
		// Now get the change constraint commands themselves to add the children at the right constraints.
		if (!children.isEmpty())
			cb.append(getChangeConstraintsCommand(children));
			
		return cb.getCommand();
	}	
	
	/**
	 * The container bean.
	 */
	protected IJavaObjectInstance getContainerBean() {
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
