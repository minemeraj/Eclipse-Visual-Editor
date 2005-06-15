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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: BoundsPropertyDescriptor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-15 20:19:21 $ 
 */
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;

import org.eclipse.ve.internal.cde.core.XYLayoutUtility;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;
/**
 * Provide some specific overrides for Bounds property.
 */
public class BoundsPropertyDescriptor extends BeanPropertyDescriptorAdapter implements ICommandPropertyDescriptor {

	public boolean areNullsInvalid() {
		return true;
	}
	
	public Command setValue(IPropertySource source, Object setValue) {
		// Set the bounds and unset the location and size, if set.
		// Unset the location and size first before applying bounds because
		// they interfere with each other.
		IJavaObjectInstance comp = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(comp);
		RuledCommandBuilder cb = new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());		
		EStructuralFeature 
			sfControlSize = JavaInstantiation.getSFeature(comp, SWTConstants.SF_CONTROL_SIZE),
			sfControlLocation = JavaInstantiation.getSFeature(comp, SWTConstants.SF_CONTROL_LOCATION);			
		if (comp.eIsSet(sfControlSize))
			cb.cancelAttributeSetting(comp, sfControlSize);
		if (comp.eIsSet(sfControlLocation))
			cb.cancelAttributeSetting(comp, sfControlLocation);
		
		// If there are any "preferred" settings on the bounds, we need to use ApplyNullLayoutConstraintCommand instead to handle these.
		IBeanProxyHost sh = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) setValue);
		IBeanProxy rect = sh.instantiateBeanProxy();
		if (rect instanceof IRectangleBeanProxy) {
			IRectangleBeanProxy rectProxy = (IRectangleBeanProxy) rect;
			int x = rectProxy.getX();
			int y = rectProxy.getY();
			int width = rectProxy.getWidth();
			int height = rectProxy.getHeight();
			if (XYLayoutUtility.constraintContainsPreferredSettings(x, y, width, height, true, true)) {
				ApplyNullLayoutConstraintCommand apply = new ApplyNullLayoutConstraintCommand();
				apply.setTarget(comp);
				apply.setDomain(h.getBeanProxyDomain().getEditDomain());
				apply.setConstraint(new Rectangle(x, y, width, height), true, true);
				cb.append(apply);
				return cb.getCommand();
			}
		}
		
		cb.applyAttributeSetting(comp, (EStructuralFeature) getTarget(), setValue);
		return cb.getCommand();
	}
	
	public Command resetValue(IPropertySource source) {
		IJavaObjectInstance comp = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(comp);		
		RuledCommandBuilder cb =  new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());
		cb.cancelAttributeSetting(comp, (EReference) getTarget());
		return cb.getCommand();
	}

}
