package org.eclipse.ve.internal.swt;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LocationPropertyDescriptor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-07 23:16:13 $ 
 */

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;

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


