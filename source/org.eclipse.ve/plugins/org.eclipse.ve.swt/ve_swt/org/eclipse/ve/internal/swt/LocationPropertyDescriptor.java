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
 *  $RCSfile: LocationPropertyDescriptor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:55 $ 
 */

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.XYLayoutUtility;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.DimensionJavaClassCellEditor;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;
/**
 * Provide some specific overrides for Location property.
 */
public class LocationPropertyDescriptor extends BeanPropertyDescriptorAdapter implements ICommandPropertyDescriptor {

	public boolean areNullsInvalid() {
		return true;
	}
	
	public Command setValue(IPropertySource source, Object setValue) {
		// Set the location and unset the bounds, if set.
		// Unset the bounds first before applying location because
		// they interfere with each other.
		IJavaObjectInstance comp = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(comp);
		RuledCommandBuilder cb = new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());	
		EStructuralFeature sfControlBounds = JavaInstantiation.getSFeature(comp, SWTConstants.SF_CONTROL_BOUNDS);		
		if (comp.eIsSet(sfControlBounds)) {
			cb.cancelAttributeSetting(comp, sfControlBounds);
			EStructuralFeature sfControlSize = JavaInstantiation.getSFeature(comp, SWTConstants.SF_CONTROL_SIZE);
			if (!comp.eIsSet(sfControlSize)) {
				// A little more difficult, need to get the size from the bounds
				// and SET Size with it so we don't loose that.
				IJavaInstance boundsObject = (IJavaInstance) comp.eGet(sfControlBounds);
				IRectangleBeanProxy bounds = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy(boundsObject);
				Object newSize = BeanUtilities.createJavaObject(SWTConstants.POINT_CLASS_NAME,
					comp.eResource().getResourceSet(),
					DimensionJavaClassCellEditor.getJavaInitializationString(bounds.getWidth(), bounds.getHeight(), SWTConstants.POINT_CLASS_NAME));
				cb.applyAttributeSetting(comp, sfControlSize, newSize);
			}
		}
			
		// If there are any "preferred" settings on the bounds, we need to use ApplyNullLauoutConstraintCommand instead to handle these.
		IBeanProxyHost sh = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) setValue);
		IBeanProxy point = sh.instantiateBeanProxy();
		if (point instanceof IPointBeanProxy) {
			IPointBeanProxy pointProxy = (IPointBeanProxy) point;
			int x = pointProxy.getX();
			int y = pointProxy.getY();
			if (XYLayoutUtility.constraintContainsPreferredSettings(x, y, 0, 0, true, false)) {
				ApplyNullLayoutConstraintCommand apply = new ApplyNullLayoutConstraintCommand();
				apply.setTarget(comp);
				apply.setDomain(h.getBeanProxyDomain().getEditDomain());
				apply.setConstraint(new Rectangle(x, y, 0, 0), true, false);
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


