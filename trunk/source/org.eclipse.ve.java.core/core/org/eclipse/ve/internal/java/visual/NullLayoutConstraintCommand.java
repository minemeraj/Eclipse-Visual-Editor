package org.eclipse.ve.internal.java.visual;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.emf.common.util.URI;
 
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
/**
 * An abstract command that provides common functionality for subclasses who specialize
 * according to different component models
 * This command deals with the possibility of storing in bounds, size, and/or location
 * Subclasses specialize for different toolkits such as AWT or SWT
 */
public abstract class NullLayoutConstraintCommand extends CommandWrapper {
	
	protected IJavaObjectInstance target;
	protected ResourceSet rset;
	protected Rectangle constraint;	// Could be a Rectangle, Point, or Dimension.
	protected byte changed = NO_CHANGE;
	protected EditDomain domain;
	protected final static byte
		NO_CHANGE = 0,
		MOVED = 0x1,
		RESIZED = 0x2;
	
	public NullLayoutConstraintCommand(String label) {
		super(label);
	}

	public NullLayoutConstraintCommand() {
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
		EReference sfComponentBounds = JavaInstantiation.getReference(target, getSFBounds());					
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
			IJavaInstance bounds = createBoundsInstance(x,y,width,height);
			cb.applyAttributeSetting(target, sfComponentBounds, bounds);
		} else {
			EReference sfComponentSize = JavaInstantiation.getReference(target, getSFSize());			
			if ((changed & RESIZED) != 0 && (changed & MOVED) == 0) {
				// We want size only. In that case, we will set the size. This is usually on the freeform.
				IJavaInstance size = createSizeInstance(constraint.width,constraint.height); 
				cb.applyAttributeSetting(target, sfComponentSize, size);
			} else {
				EReference sfComponentLocation = JavaInstantiation.getReference(target, getSFLocation());
				if (target.eIsSet(sfComponentSize) || target.eIsSet(sfComponentLocation)) {
					// One of them is set, so we are not using bounds. If neither was set, we would drop to bounds.
					if ((changed & RESIZED) != 0) {
						IJavaInstance size = createSizeInstance(constraint.width,constraint.height); 
						cb.applyAttributeSetting(target, sfComponentSize, size);
					}
					
					if ((changed & MOVED) != 0) {
						IJavaInstance loc = createLocationInstance(constraint.x,constraint.y);												
						cb.applyAttributeSetting(target, sfComponentLocation, loc);
					}
				} else {
					// If we got this far, then we are applying bounds because we had no bounds, size, or location set.
					IJavaInstance bounds = createBoundsInstance(constraint.x,constraint.y,constraint.width,constraint.height); 
					cb.applyAttributeSetting(target, sfComponentBounds, bounds);
				}
			}
		}
		command = cb.getCommand();
		command.execute();
	}

	/**
	 * Return the location
	 */
	protected abstract IJavaInstance createLocationInstance(int x, int y);
	/**
	 * Return the bounds
	 */
	protected abstract IJavaInstance createBoundsInstance(int x, int y, int width, int height);
	/**
	 * Return the size for the width and height arguments
	 */
	protected abstract IJavaInstance createSizeInstance(int width, int height);
	/**
	 * Return the structural feature URI for bounds
	 */
	protected abstract URI getSFBounds();
	/**
	 * Return the structural feature URI for size
	 */	
	protected abstract URI getSFSize();
	/** 
	 * Return the structural feature URI for location 
	 */
	protected abstract URI getSFLocation();	
}