/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: NullLayoutConstraintCommand.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:47 $ 
 */
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

import org.eclipse.emf.common.util.URI;
 
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ModelChangeController;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;

/**
 * An abstract command that provides common functionality for subclasses who specialize
 * according to different component models
 * This command deals with the possibility of storing in bounds, size, and/or location
 * Subclasses specialize for different toolkits such as AWT or SWT
 * <p>
 * If there are any preferred settings, then it will do two things:
 * <ol>
 * <li>It will apply the constraints as is except changing the preferred settings to be zero (so that they are valid)
 * <li>Access the current transaction and add a runnable to the end which will set the calculated preferred values
 * on the target.
 * </ol>
 * <p>
 * What this will do will apply the settings so that the commands stack has something to undo so that we get back
 * to a valid state on undo, but yet at end of transaction it will set it to the correct values. Undo will reset
 * them back to what it was before this command, while redo will set them again and put another command at end of
 * transaction. For this to work it is important that the runnable on the end transaction changes NO OTHER SETTINGS
 * than the main execute does. Otherwise undo will not undo everything correctly.
 * @since 1.1.0
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

	/*
	 * Command that will put a runnable at the end of the transaction. This is so that
	 * after the transaction has completed (in which we know all changes have been applied)
	 * we can now execute the final part to handle getting the preferred settings. This
	 * needs to be done after everything else so that we have a live fully-set object to
	 * work upon.
	 */
	private static final int NO_TYPE = -1, TYPE_BOUNDS = 0, TYPE_LOC = 1, TYPE_SIZE = 2, TYPE_LOC_AND_SIZE = 3;
	private class TransactionEndCommand extends Command {
		public int type;
		public TransactionEndCommand(int type) {
			this.type = type;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.gef.commands.Command#execute()
		 */
		public void execute() {
			ModelChangeController mc = (ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
			mc.execAtEndOfTransaction(new Runnable() {
				public void run() {
					// Need a ruled command builder so that preset/postset are done correctly.
					RuledCommandBuilder cb = new RuledCommandBuilder(domain);
					switch (type) {
						case TYPE_BOUNDS:
							int x,y,width,height;
							if (XYLayoutUtility.constraintContainsPreferredSettings(constraint, true, true)) {
								Rectangle b = getPreferredBounds(constraint);
								x = b.x;
								y = b.y;
								width = b.width;
								height = b.height;
							} else if (XYLayoutUtility.constraintContainsPreferredSettings(constraint, true, false)) {
								Point p = getPreferredLoc();
								x = p.x;
								y = p.y;
								width = constraint.width;
								height = constraint.height;
							} else {
								// If it got here it must be size only.
								Dimension s = getPreferredSize(constraint.width, constraint.height);
								x = constraint.x;
								y = constraint.y;
								width = s.width;
								height = s.height;
							}
							IJavaInstance bounds = createBoundsInstance(x,y,width,height);
							cb.applyAttributeSetting(target, JavaInstantiation.getReference(target, getSFBounds()), bounds);
							break;
						case TYPE_LOC:
							Point p = getPreferredLoc();
							x = p.x;
							y = p.y;
							IJavaInstance loc = createLocationInstance(x,y);
							cb.applyAttributeSetting(target, JavaInstantiation.getReference(target, getSFLocation()), loc);
							break;
						case TYPE_SIZE:
							Dimension s = getPreferredSize(constraint.width, constraint.height);
							width = s.width;
							height = s.height;
							IJavaInstance size = createSizeInstance(width,height); 
							cb.applyAttributeSetting(target, JavaInstantiation.getReference(target, getSFSize()), size);
							break;
						case TYPE_LOC_AND_SIZE:
							p = getPreferredLoc();
							x = p.x;
							y = p.y;
							loc = createLocationInstance(x,y);
							cb.applyAttributeSetting(target, JavaInstantiation.getReference(target, getSFLocation()), loc);
							s = getPreferredSize(constraint.width, constraint.height);
							width = s.width;
							height = s.height;
							size = createSizeInstance(width,height); 
							cb.applyAttributeSetting(target, JavaInstantiation.getReference(target, getSFSize()), size);
							break;
					}
					cb.getCommand().execute();
				}
			});
		}
	}
	public void execute() {
		// It will determine what to set. If bounds are set, it will set that, otherwise it will try size/location. If neither
		// is set, then it will set a bounds.
		int tranEndType = NO_TYPE;	// No tranend command needed.
		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		EReference sfComponentBounds = JavaInstantiation.getReference(target, getSFBounds());					
		if (target.eIsSet(sfComponentBounds)) {
			// We have a bounds.
			IRectangleBeanProxy oldBounds = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaObjectInstance) target.eGet(sfComponentBounds), rset);
			int x, y, width, height;
			if ((changed & MOVED) != 0) {
				if (constraint.x == XYLayoutUtility.PREFERRED_LOC && constraint.y == XYLayoutUtility.PREFERRED_LOC) {
					tranEndType = TYPE_BOUNDS;
					x = 0;
					y = 0;
				}
				x = constraint.x;
				y = constraint.y;
			} else {
				x = oldBounds.getX();
				y = oldBounds.getY();
			}
			if ((changed & RESIZED) != 0) {
				if (constraint.width == XYLayoutUtility.PREFERRED_SIZE) {
					tranEndType = TYPE_BOUNDS;
					width = 0;
				} else
					width = constraint.width;
				if (constraint.height == XYLayoutUtility.PREFERRED_SIZE) {
					tranEndType = TYPE_BOUNDS;
					height = 0;
				} else
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
				int width, height;
				if (constraint.width == XYLayoutUtility.PREFERRED_SIZE) {
					tranEndType = TYPE_SIZE;
					width = 0;
				} else
					width = constraint.width;
				if (constraint.height == XYLayoutUtility.PREFERRED_SIZE) {
					tranEndType = TYPE_SIZE;
					height = 0;
				} else
					height = constraint.height;
				IJavaInstance size = createSizeInstance(width,height); 
				cb.applyAttributeSetting(target, sfComponentSize, size);
			} else {
				EReference sfComponentLocation = JavaInstantiation.getReference(target, getSFLocation());
				if (target.eIsSet(sfComponentSize) || target.eIsSet(sfComponentLocation)) {
					// One of them is set, so we are not using bounds. If neither was set, we would drop to bounds.
					if ((changed & RESIZED) != 0) {
						int width, height;
						if (constraint.width == XYLayoutUtility.PREFERRED_SIZE) {
							tranEndType = TYPE_SIZE;
							width = 0;
						} else
							width = constraint.width;
						if (constraint.height == XYLayoutUtility.PREFERRED_SIZE) {
							tranEndType = TYPE_SIZE;
							height = 0;
						} else
							height = constraint.height;
						IJavaInstance size = createSizeInstance(width,height); 
						cb.applyAttributeSetting(target, sfComponentSize, size);
					}
					
					if ((changed & MOVED) != 0) {
						int x,y;
						if (constraint.x == XYLayoutUtility.PREFERRED_LOC && constraint.y == XYLayoutUtility.PREFERRED_LOC) {
							tranEndType = tranEndType != TYPE_SIZE ? TYPE_LOC : TYPE_LOC_AND_SIZE;
							x = 0;
							y = 0;
						}
						x = constraint.x;
						y = constraint.y;
						IJavaInstance loc = createLocationInstance(x,y);												
						cb.applyAttributeSetting(target, sfComponentLocation, loc);
					}
				} else {
					// If we got this far, then we are applying bounds because we had no bounds, size, or location set.
					int x, y, width, height;
					if (constraint.x == XYLayoutUtility.PREFERRED_LOC && constraint.y == XYLayoutUtility.PREFERRED_LOC) {
						tranEndType = TYPE_BOUNDS;
						x = 0;
						y = 0;
					} else {
						x = constraint.x;
						y = constraint.y;
					}
					if (constraint.width == XYLayoutUtility.PREFERRED_SIZE) {
						tranEndType = TYPE_BOUNDS;
						width = 0;
					} else
						width = constraint.width;
					if (constraint.height == XYLayoutUtility.PREFERRED_SIZE) {
						tranEndType = TYPE_BOUNDS;
						height = 0;
					} else
						height = constraint.height;
					IJavaInstance bounds = createBoundsInstance(x,y,width,height); 
					cb.applyAttributeSetting(target, sfComponentBounds, bounds);
				}
			}
		}
		if (tranEndType != NO_TYPE)
			cb.appendPost(new TransactionEndCommand(tranEndType));
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
	 * Get the preferred location of this component. The component must be a live component.
	 * If for some reason it cannot return this info, then return a point with (XYLayoutUtility.PREFERRED_LOC, XYLayoutUtility.PREFERRED_LOC).
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract Point getPreferredLoc();
	
	/**
	 * Get the preferred size of this component for the dimension that is set to XYLayoutUtility.PREFERRED_SIZE. The component must be a live component.
	 * If for some reason it cannot return this info, then return a dimenstion with (width,height) as sent in.
	 * 
	 * @param width if XYLayoutUtility.PREFERRED_SIZE then return preferred size for width, else return width sent in.
	 * @param height if XYLayoutUtility.PREFERRED_SIZE then return preferred size for height, else return height sent in.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract Dimension getPreferredSize(int width, int height);
	
	/**
	 * Get the preferred constraints for this constraint. Find the
	 * preferred location, and if either width or height is XYLayoutUtility.PREFERRED_SIZE, then return the preferred width and/or height
	 * in the return new constraint rectangle. Any non-preferred settings from the original constraint should be copied 
	 * over. This will be called if both loc and size had a preferred setting. 
	 * 
	 * @param constraint
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract Rectangle getPreferredBounds(Rectangle constraint);
	
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
