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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ComponentModelAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IConstraintHandler;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
/**
 * Model adapter for awt.Component.
 *
 * For ComponentProxyAdapter: This is actually subclassed and
 * used there to provide the non-create IConstraintHandler functions.
 */
public class ComponentModelAdapter implements IConstraintHandler {
	
	protected IJavaObjectInstance component;
	protected EStructuralFeature sfComponentLocation, sfComponentSize, sfComponentBounds;
	
	
	public ComponentModelAdapter(Object component) {
		this.component = (IJavaObjectInstance) component;
		sfComponentLocation = JavaInstantiation.getSFeature(this.component, JFCConstants.SF_COMPONENT_LOCATION);
		sfComponentSize = JavaInstantiation.getSFeature(this.component, JFCConstants.SF_COMPONENT_SIZE);
		sfComponentBounds = JavaInstantiation.getSFeature(this.component, JFCConstants.SF_COMPONENT_BOUNDS);
	}

	// IConstraintHandler interface. It allows components to be resizable on the freeform surface.	
	
	/**
	 * isResizeable
	 *
	 * Return true if the resize handles should be applied to the figure.
	 * This means that the user can initiate resize requests through direct interaction.
	 *
	 * For create: Return true if it can be sized, either through the VisualInfo or it persists size itself.
	 */
	public boolean isResizeable() {
		return true;
	}
	
	/**
	 * contributeOrphanChildCommand
	 *
	 * This will be called on orphan child requests only.
	 * Return null if nothing to contribute. 
	 */
	public Command contributeOrphanChildCommand() {
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(component);
		RuledCommandBuilder cb = new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());
		if (component.eIsSet(sfComponentBounds))
			cb.cancelAttributeSetting(component, sfComponentBounds);
		if (component.eIsSet(sfComponentSize))
			cb.cancelAttributeSetting(component, sfComponentSize);
		if (component.eIsSet(sfComponentLocation))
			cb.cancelAttributeSetting(component, sfComponentLocation);
			
		return cb.getCommand();
	}
	
	/**
	 * contributeFigureSize
	 *
	 * The entire constraint in figure coordinates is passed in. The implementation
	 * can then modify the size. This is used just before being applied to figure. 
	 * It can be used to modify the size to handle zooming. Only changes to the size
	 * portion are honored. Changes to the position are ignored.
	 *
	 * For create: It won't be called.
	 */
	public void contributeFigureSize(org.eclipse.draw2d.geometry.Rectangle figureConstraint) {
		// This isn't called on create.
	}
	
	/**
	 * contributeSizeCommand
	 *
	 * This will be called on resize requests only. The size in model coordinates will be passed in.
	 * This will be the size calculated from the resize request.
	 * If the component wishes to contribute a size change request, it should return the
	 * command. If not, it should return null. If it returns null, then the entire constraint
	 * will be stored in the visual info. If it doesn't return null, then the size portion
	 * of the constraint in the visual info will be set to (-1, -1).
	 *
	 * If size is not persisted in the component itself, it should return null.
	 *
	 * For create: If it persists size in the model, it should return a command, else return null.
	 */
	public Command contributeSizeCommand(int width, int height, EditDomain domain) {
		// We want size to be set in the size/bounds of the object.
		ApplyNullLayoutConstraintCommand cmd = new ApplyNullLayoutConstraintCommand();
		cmd.setTarget(component);
		cmd.setDomain(domain);
		cmd.setConstraint(new org.eclipse.draw2d.geometry.Rectangle(0, 0, width, height), false, true);
		return cmd;
	}
	
	/**
	 * contributeModelSize
	 *
	 * The entire constraint in model coordinates is passed in. The implementation
	 * can then modify the size. It should get the size out of its persistent storage,
	 * which is usually a property on the component. This is used to refresh the constraint,
	 * either the first time it is displayed, or if the zoom has changed. It allows the
	 * component to return the size to use.  Only changes to the size
	 * portion are honored. Changes to the position are ignored.
	 *
	 * If size is not persisted in the component itself, it should do nothing.
	 *
	 * For create: This isn't called.
	 */
	public void contributeModelSize(org.eclipse.ve.internal.cdm.model.Rectangle modelConstraint) {
		// This isn't called on create.
	}
	
	/**
	 * addConstraintHandlerListener
	 *
	 * Add a constraint handler listener. If size is not persisted in the component then
	 * this method should do nothing with the listener.
	 *
	 * For create: This isn't called.
	 */
	public void addConstraintHandlerListener(IConstraintHandlerListener listener) {
		// This isn't called on create.
	}

	
	/**
	 * removeConstraintHandlerListener
	 *
	 * remove a constraint handler listener. If size is not persisted in the component then
	 * this method should do nothing with the listener.
	 *
	 * For create: This isn't called.
	 */
	public void removeConstraintHandlerListener(IConstraintHandlerListener listener) {
		// This isn't called on create.
	}
}
