/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: SizePropertyDescriptor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-27 15:35:50 $ 
 */
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;

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
				Object newLoc = BeanUtilities.createJavaObject("org.eclipse.swt.graphics.Point", //$NON-NLS-1$
					comp.eResource().getResourceSet(),
					PointJavaClassCellEditor.getJavaInitializationString(bounds.getX(), bounds.getY(),SWTConstants.POINT_CLASS_NAME));
				cb.applyAttributeSetting(comp, sfControlLocation, newLoc);
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


