package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: SizePropertyDescriptor.java,v $
 *  $Revision: 1.2 $  $Date: 2003-12-03 10:18:02 $ 
 */
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
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
		EStructuralFeature sfComponentBounds = JavaInstantiation.getSFeature(comp, JFCConstants.SF_COMPONENT_BOUNDS);
		if (comp.eIsSet(sfComponentBounds)) {
			cb.cancelAttributeSetting(comp, sfComponentBounds);
			EStructuralFeature sfComponentLocation = JavaInstantiation.getSFeature(comp, JFCConstants.SF_COMPONENT_LOCATION);
			if (!comp.eIsSet(sfComponentLocation)) {
				// A little more difficult, need to get the location from the bounds
				// and SET Location with it so we don't loose that.
				IJavaInstance boundsObject = (IJavaInstance) comp.eGet(sfComponentBounds);
				IRectangleBeanProxy bounds = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy(boundsObject);
				Object newLoc = BeanUtilities.createJavaObject("java.awt.Point", //$NON-NLS-1$
					comp.eResource().getResourceSet(),
					PointJavaClassCellEditor.getJavaInitializationString(bounds.getX(), bounds.getY(),JFCConstants.POINT_CLASS_NAME));
				cb.applyAttributeSetting(comp, sfComponentLocation, newLoc);
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


