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
 *  $RCSfile: NullLayoutSwitcher.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 
/**
 * 
 * @since 1.0.0
 */
public class NullLayoutSwitcher extends LayoutSwitcher {
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutSwitcher#getChangeConstraintsCommand(java.util.List)
	 */
	protected NullLayoutPolicyHelper helper;
	
	/**
	 * NullLayoutConstraintConverter constructor comment.
	 */
	public NullLayoutSwitcher(VisualContainerPolicy cp) {
		super(cp);
		helper = new NullLayoutPolicyHelper(cp);
	}
	
	/**
	 * Returns a compound command containing the child constraint commands to position the
	 * children within a null Layout container.
	 * If there were previous constraints, remove them first.
	 * Fot the children we're going to create Rectangle constraints and 
	 * in order to do this we must get the position coordinates from the live bean
	 * and create setting commands with a Bean rectangle constraint.
	 */
	protected Command getChangeConstraintsCommand(List children) {
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
		// First get rid of any previous constraints 
		cb.append(helper.getOrphanConstraintsCommand(children));
		// Then get the current positioning of each of the children
		ArrayList constraints = new ArrayList(children.size());
		Iterator comps = children.iterator();
		while (comps.hasNext()) {
			IJavaObjectInstance comp = (IJavaObjectInstance) comps.next();
			IBeanProxy compProxy = BeanProxyUtilities.getBeanProxy(comp);
			
			IRectangleBeanProxy rectProxy = BeanSWTUtilities.invoke_getBounds(compProxy);
			Rectangle newRect = new Rectangle(rectProxy.getX(), rectProxy.getY(), rectProxy.getWidth(), rectProxy.getHeight());
			constraints.add(new NullLayoutPolicyHelper.NullConstraint(newRect, true, true));
		}
		cb.append(helper.getChangeConstraintCommand(children, constraints));	// Now let the helper change the constraints correctly.
		return cb.getCommand(); 
	}
}
