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
 *  $RCSfile: NullLayoutSwitcher.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;
/**
 * This helper class is used when a container's layout is changed
 * to a null Layout. Each of the constraints for the container's children 
 * constraints need to be assigned a rectangle.
 *
 * Creation date: (10/11/00 12:13:19 PM)
 * @author: Peter Walker
 */
public class NullLayoutSwitcher extends LayoutSwitcher {
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
		cb.append(getCancelConstraintsCommand(children));
		// Then get the current positioning of each of the children
		ArrayList constraints = new ArrayList(children.size());
		Iterator comps = children.iterator();
		while (comps.hasNext()) {
			IJavaObjectInstance comp = (IJavaObjectInstance) comps.next();
			IBeanProxy compProxy = BeanProxyUtilities.getBeanProxy(comp);
			
			IRectangleBeanProxy rectProxy = BeanAwtUtilities.invoke_getBounds(compProxy);
			Rectangle newRect = new Rectangle(rectProxy.getX(), rectProxy.getY(), rectProxy.getWidth(), rectProxy.getHeight());
			constraints.add(new NullLayoutPolicyHelper.NullConstraint(newRect, true, true));
		}
		cb.append(helper.getChangeConstraintCommand(children, constraints));	// Now let the helper change the constraints correctly.
		return cb.getCommand(); 
	}
	/*
	 * Get the commands to set the respective constraints of the children to null.
	 */
	public Command getCancelConstraintsCommand(List children) {
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(policy.getEditDomain());
		EReference sfConstraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		EReference sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		EReference sfComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
		
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
		Iterator childs = children.iterator();
		while (childs.hasNext()) {
			EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject) getContainerBean(), sfComponents, sfConstraintComponent, (EObject) childs.next());
			cb.applyAttributeSetting(constraintComponent, sfConstraintConstraint, null);
		}
		return cb.getCommand();
	}
}
