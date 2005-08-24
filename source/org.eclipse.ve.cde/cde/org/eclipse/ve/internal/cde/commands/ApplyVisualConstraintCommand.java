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
package org.eclipse.ve.internal.cde.commands;
/*
 *  $RCSfile: ApplyVisualConstraintCommand.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.impl.KeyedConstraintImpl;
import org.eclipse.ve.internal.cdm.model.*;

import org.eclipse.ve.internal.cde.core.XYLayoutUtility;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Apply either the new constraint ,the new size, or new location to the Visual constraint keyed value.
 * The three are mutually exclusive, the last set in the command will be what's applied.
 * Creation date: (06/05/00 3:04:51 PM)
 * @author: Administrator
 */
public class ApplyVisualConstraintCommand extends CommandWrapper {
	protected Object fNewConstraint;
	protected KeyedValueHolder fTarget;
	protected static final Rectangle SDEFAULT_RECT = XYLayoutUtility.modifyPreferredCDMRectangle(new Rectangle(), true, true, true); 
	/**
	 * ApplyVisualLocationCommand constructor comment.
	 * @param desc java.lang.String
	 */
	public ApplyVisualConstraintCommand(String desc) {
		super(desc);
	}
	public void setTarget(KeyedValueHolder target) {
		fTarget = target;
	}
	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return fTarget != null && fNewConstraint != null;
	}

	public void execute() {
		if (command != null)
			super.execute();

		KeyedConstraintImpl c = (KeyedConstraintImpl) CDMFactory.eINSTANCE.create(CDMPackage.eINSTANCE.getKeyedConstraint());
		c.setKey(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
		if (fNewConstraint instanceof Rectangle) {
			c.setValue(fNewConstraint);
		} else if (fNewConstraint instanceof Point) {
			// Setting size instead of entire constraint.
			Object oldV = fTarget.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
			// to be on the safe side, in case old is not a KeyedConstraint, or doesn't exist.
			Rectangle old = oldV instanceof Rectangle ? (Rectangle) oldV : SDEFAULT_RECT;
			// OldV must exist, can't set size if no old v of type constraint.
			c.setValue(new Rectangle(((Point) fNewConstraint).x, ((Point) fNewConstraint).y, old.width, old.height));
		} else if (fNewConstraint instanceof Dimension) {
			// Setting size instead of entire constraint.
			Object oldV = fTarget.getKeyedValues().get(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
			// to be on the safe side, in case old is not a KeyedConstraint, or doesn't exist.
			Rectangle old = oldV instanceof Rectangle ? (Rectangle) oldV : SDEFAULT_RECT;
			// OldV must exist, can't set size if no old v of type constraint.
			c.setValue(new Rectangle(old.x, old.y, ((Dimension) fNewConstraint).width, ((Dimension) fNewConstraint).height));
		}

		CommandBuilder cbld = new CommandBuilder(getLabel());
		cbld.applyAttributeSetting(fTarget, c);
		command = cbld.getCommand();
		command.execute();
	}

	public void setConstraint(Object constraint) {
		if (constraint instanceof Rectangle || constraint instanceof Point || constraint instanceof Dimension)
			fNewConstraint = constraint;
	}

	/**
	 * Set the constraint to apply.
	 * Creation date: (06/05/00 3:06:38 PM)
	 */
	public void setRectangle(Rectangle newConstraint) {
		fNewConstraint = newConstraint;
	}
	/**
	 * Set the point to apply.
	 * Creation date: (06/05/00 3:06:38 PM)
	 */
	public void setLocation(Point newPoint) {
		fNewConstraint = newPoint;
	}
	/**
	 * Set the size to apply.
	 * Creation date: (06/05/00 3:06:38 PM)
	 */
	public void setSize(Dimension newSize) {
		fNewConstraint = newSize;
	}

}
