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
 *  $RCSfile: SizePropertyDescriptor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-11-15 18:53:27 $ 
 */
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.XYLayoutUtility;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.PointJavaClassCellEditor;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;
/**
 * Provide some specific overrides for Size property.
 */
public class SizePropertyDescriptor extends BeanPropertyDescriptorAdapter implements ICommandPropertyDescriptor {

	public boolean areNullsInvalid() {
		return true;
	}
	
	public Command setValue(IPropertySource source, Object setValue) {
		// Set the size and unset the bounds, if set.
		// Unset the bounds first before applying size because
		// they interfere with each other.
		IJavaObjectInstance comp = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(comp);
		RuledCommandBuilder cb = new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());	
		EStructuralFeature sfControlBounds = JavaInstantiation.getSFeature(comp, SWTConstants.SF_CONTROL_BOUNDS);
		if (comp.eIsSet(sfControlBounds)) {
			cb.cancelAttributeSetting(comp, sfControlBounds);
			EStructuralFeature sfControlLocation = JavaInstantiation.getSFeature(comp, SWTConstants.SF_CONTROL_LOCATION);
			if (!comp.eIsSet(sfControlLocation)) {
				// A little more difficult, need to get the location from the bounds
				// and SET Location with it so we don't loose that.
				IJavaInstance boundsObject = (IJavaInstance) comp.eGet(sfControlBounds);
				IRectangleBeanProxy bounds = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy(boundsObject);
				Object newLoc = BeanUtilities.createJavaObject(SWTConstants.POINT_CLASS_NAME,
					comp.eResource().getResourceSet(),
					PointJavaClassCellEditor.getJavaAllocation(bounds.getX(), bounds.getY(),SWTConstants.POINT_CLASS_NAME));
				cb.applyAttributeSetting(comp, sfControlLocation, newLoc);
			}
		}
			
		// If there are any "preferred" settings on the bounds, we need to use ApplyNullLayoutConstraintCommand instead to handle these.
		IJavaInstance setJavaInstanceValue = (IJavaInstance) setValue;
		IBeanProxyHost sh = BeanProxyUtilities.getBeanProxyHost(setJavaInstanceValue);
		IBeanProxy dim = sh.instantiateBeanProxy();
		if (dim instanceof IPointBeanProxy) {
			IPointBeanProxy pointProxy = (IPointBeanProxy) dim;
			int width = pointProxy.getX();
			int height = pointProxy.getY();
			if (XYLayoutUtility.constraintContainsPreferredSettings(0, 0, width, height, false, true)) {
				ApplyNullLayoutConstraintCommand apply = new ApplyNullLayoutConstraintCommand();
				apply.setTarget(comp);
				apply.setDomain(h.getBeanProxyDomain().getEditDomain());
				apply.setConstraint(new Rectangle(0, 0, width, height), false, true);
				cb.append(apply);
				return cb.getCommand();
			}
		}
		
		ParseTreeAllocation alloc = changeToParseTreeAllocation(setJavaInstanceValue);
		if (alloc != null)
			cb.applyAttributeSetting(setJavaInstanceValue, JavaInstantiation.getAllocationFeature(setJavaInstanceValue), alloc);
		cb.applyAttributeSetting(comp, (EStructuralFeature) getTarget(), setJavaInstanceValue);

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


