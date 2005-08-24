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
 *  $RCSfile: BorderLayoutSwitcher.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

import org.eclipse.ve.internal.java.vce.SubclassCompositionContainerPolicy;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IPointBeanProxy;
/**
 * This helper class is used when a container's layout is changed
 * to BorderLayout. Each of the constraints for the container's children 
 * needs to be set based on each child's order and position.
 */
public class BorderLayoutSwitcher extends LayoutSwitcher {
	BorderLayoutPolicyHelper helper;
	SubclassCompositionContainerPolicy freeFormContainerPolicy;

public BorderLayoutSwitcher(VisualContainerPolicy aPolicy) {
	super(aPolicy);
	helper = new BorderLayoutPolicyHelper(aPolicy);
}
/**
 * Create the change constraint commands for the bean's children
 * We need to create BorderComponent objects for the new constraints
 */
protected Command getChangeConstraintsCommand(List children) {
	// use the helper to help with constraint conversion
	List regions = new ArrayList(5);
	List internalTags = BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS;
	for (int i=0; i<5; i++) {
		regions.add(internalTags.get(i));
	}
	ArrayList remComps = new ArrayList();
	CompoundCommand cc = new CompoundCommand("Create constraints"); //$NON-NLS-1$
	// get the components
	Iterator comps = children.iterator();
	// In the first pass, loop thru the components and assign a region to the location the object is in
	List constraints = new ArrayList(5);
	List compsWithConstraints = new ArrayList(5);
	while (comps.hasNext()) {
		IJavaObjectInstance comp = (IJavaObjectInstance)comps.next();
		// get the location from the bean since flow does not have constraints
		IBeanProxy proxy = BeanProxyUtilities.getBeanProxy(comp);
		IPointBeanProxy pointProxy = BeanAwtUtilities.invoke_getLocation(proxy);
		Point loc = new Point(pointProxy.getX(),pointProxy.getY());
		// get the location and decide which region it should be placed in
		if (loc != null) {
			String str = helper.getCurrentConstraint(helper.convertLocation(loc));
			if (str != null && regions.contains(str)) {
				constraints.add(str);
				compsWithConstraints.add(comp);
				// Remote the region just used up
				regions.remove(str);
			} else
				// remComps is the people we can't allocate a constraint to yet
				remComps.add(comp);
		}
	}
	// If there are any regions still unoccupied and components targeted for removal,
	// assign the remaining regions until all regions are occupied or no more components
	// are left to be assigned.
	while (!remComps.isEmpty() && !regions.isEmpty()) {
		constraints.add(regions.get(0));
		compsWithConstraints.add(remComps.get(0));
		regions.remove(regions.get(0));
		remComps.remove(remComps.get(0));
	}
	// For all the components that have regions let the helper add them
	cc.add(helper.getChangeConstraintCommand(compsWithConstraints, constraints));
	
	// If there are components still remaining, remove them from the container - Border only
	// has five regions	
	// Then add them to the free form so the user can still see them
	if (!remComps.isEmpty()) {
		cc.add(removeComponents(remComps));
	}
	if (cc.size() > 0)
		return cc.unwrap();
	else
		return null;
}
/**
 * Create the cancel command to remove the remaining components from the container.
 */
private Command removeComponents(List comps) {
	if ( freeFormContainerPolicy == null ) {
		// Find the BeanSubclassComposition
		IJavaObjectInstance component = (IJavaObjectInstance)comps.get(0);
		EObject componentOwner = component.eContainer();
		while ( componentOwner != null ) {
			if ( componentOwner instanceof BeanSubclassComposition ) {
				freeFormContainerPolicy = new SubclassCompositionContainerPolicy(policy.getEditDomain());
				freeFormContainerPolicy.setContainer(componentOwner);
				break;
			}
			componentOwner = componentOwner.eContainer();
		}		
	}	

	//Anonymous....Adapter.getFirstReferencedBy(javainstance, VCEPackage.eInstance.getMethod_Return());			
	
	CompoundCommand cc = new CompoundCommand("Move the extra components from the BorderLayout container to the free form"); //$NON-NLS-1$
	cc.add(policy.getOrphanChildrenCommand(comps));
	// Having orphansed the components from the container, add them to the free form
	// Some of the components might not be returned by methods ( so they are therefore not referencable by the free form
	// so this will need more thought - JRW
	cc.add(freeFormContainerPolicy.getAddCommand(comps,null));

	return cc;
}

}
