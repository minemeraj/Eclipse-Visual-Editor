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
 *  $RCSfile: ApplyNullLayoutConstraintCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
/**
 * Command to apply the Null Layout constraint(s). This is
 * a command because it is complicated because of the
 * possibility of storing in bounds, size, and/or location.
 */
public class ApplyNullLayoutConstraintCommand extends CommandWrapper {
	
	protected IJavaObjectInstance target;
	protected ResourceSet rset;
	protected Rectangle constraint;	// Could be a Rectangle, Point, or Dimension.
	protected byte changed = NO_CHANGE;
	protected EditDomain domain;
	protected final static byte
		NO_CHANGE = 0,
		MOVED = 0x1,
		RESIZED = 0x2;
	
	public ApplyNullLayoutConstraintCommand(String label) {
		super(label);
	}

	public ApplyNullLayoutConstraintCommand() {
	}


	public void setTarget(IJavaObjectInstance target) {
		this.target = target;
	}

	public void setDomain(EditDomain domain) {
		this.domain = domain;
		rset = EMFEditDomainHelper.getResourceSet(domain);
	}
			
	public void setConstraint(Rectangle constraint, boolean moved, boolean resized) {
		if (moved)
			changed |= MOVED;
		if (resized)
			changed |= RESIZED;
		this.constraint = constraint;
	}
	
	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return target != null && rset != null && constraint != null;
	}
	
	public void execute() {
		// It will determine what to set. If bounds are set, it will set that, otherwise it will try size/location. If neither
		// is set, then it will set a bounds.
		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		EReference sfComponentBounds = JavaInstantiation.getReference(target, JFCConstants.SF_COMPONENT_BOUNDS);					
		if (target.eIsSet(sfComponentBounds)) {
			// We have a bounds.
			IRectangleBeanProxy oldBounds = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaObjectInstance) target.eGet(sfComponentBounds), rset);
			int x, y, width, height;
			if ((changed & MOVED) != 0) {
				x = constraint.x;
				y = constraint.y;
			} else {
				x = oldBounds.getX();
				y = oldBounds.getY();
			}
			if ((changed & RESIZED) != 0) {
				width = constraint.width;
				height = constraint.height;
			} else {
				width = oldBounds.getWidth();
				height = oldBounds.getHeight();
			}
			IJavaInstance bounds = BeanUtilities.createJavaObject("java.awt.Rectangle", rset, //$NON-NLS-1$
				RectangleJavaClassCellEditor.getJavaInitializationString(x, y, width, height));
			cb.applyAttributeSetting(target, sfComponentBounds, bounds);
		} else {
			EReference sfComponentSize = JavaInstantiation.getReference(target, JFCConstants.SF_COMPONENT_SIZE);			
			if ((changed & RESIZED) != 0 && (changed & MOVED) == 0) {
				// We want size only. In that case, we will set the size. This is usually on the freeform.
				IJavaInstance size = BeanUtilities.createJavaObject(
					"java.awt.Dimension", //$NON-NLS-1$
					rset,
					DimensionJavaClassCellEditor.getJavaInitializationString(constraint.width, constraint.height));				
				cb.applyAttributeSetting(target, sfComponentSize, size);
			} else {
				EReference sfComponentLocation = JavaInstantiation.getReference(target, JFCConstants.SF_COMPONENT_LOCATION);
				if (target.eIsSet(sfComponentSize) || target.eIsSet(sfComponentLocation)) {
					// One of them is set, so we are not using bounds. If neither was set, we would drop to bounds.
					if ((changed & RESIZED) != 0) {
						IJavaInstance size = BeanUtilities.createJavaObject(
							"java.awt.Dimension", //$NON-NLS-1$
							rset,
							DimensionJavaClassCellEditor.getJavaInitializationString(constraint.width, constraint.height));
						cb.applyAttributeSetting(target, sfComponentSize, size);
					}
					
					if ((changed & MOVED) != 0) {
						IJavaInstance loc = BeanUtilities.createJavaObject(
							"java.awt.Point", //$NON-NLS-1$
							rset,
							PointJavaClassCellEditor.getJavaInitializationString(constraint.x, constraint.y));												
						cb.applyAttributeSetting(target, sfComponentLocation, loc);
					}
				} else {
					// If we got this far, then we are applying bounds because we had no bounds, size, or location set.
					IJavaInstance bounds = BeanUtilities.createJavaObject(
						"java.awt.Rectangle", //$NON-NLS-1$
						rset,
						RectangleJavaClassCellEditor.getJavaInitializationString(constraint));
					cb.applyAttributeSetting(target, sfComponentBounds, bounds);
				}
			}
		}
		command = cb.getCommand();
		command.execute();
	}
}