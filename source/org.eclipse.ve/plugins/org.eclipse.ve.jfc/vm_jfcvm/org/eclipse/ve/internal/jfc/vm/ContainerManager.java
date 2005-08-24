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
package org.eclipse.ve.internal.jfc.vm;
/*
 *  $RCSfile: ContainerManager.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.awt.Component;
import java.awt.Container;

/**
 * This is the manager for a java.awt.Container. It allows
 * adding components before a particular component. 
 * It's purpose is to hide indexes and instead
 * work on components themselves. This is because the index of 
 * the component back in JBCF may not necessarily be the index
 * within the live component. This is because of components that
 * couldn't be instantiated due to errors, or if the container was
 * a subclass, there could be components already in the container.
 * <p>
 * This is a static helper class because it needs no state to be saved.
 */
public class ContainerManager {

	/**
	 * Return the true index of the specified component. -1 if not found. This will include any components
	 * that came from the superclass of the container (such as when subclassing panel and adding components) then
	 * dropping that panel and adding more component.s
	 * 
	 * @param container
	 * @param beforeComponent
	 * @param addingComponent
	 * @return Return the index of the specified component. -1 if not found.
	 * 
	 * @since 1.1.0
	 */
	public static int componentIndex(Container container, Component beforeComponent, Component addingComponent) {
		if (beforeComponent != null) {
			Component[] components = container.getComponents();
			for (int i = 0, r = 0; i < components.length; i++, r++) {
				if (components[i] == beforeComponent)
					return r;
				if (components[i] == addingComponent)
					r--; // Need to adjust because we found adding component first. When it gets added again, it will be removed from old pos, and if we didn't decr r it would then be one to many. 
			}
		}
		
		return -1;
	}

	/**
	 * Change the component's constraint.  If useDefaultConstraint is true,
	 * the component will be added with a constraint of the components.getName(). If the component is not
	 * a child of this container, then it will not do the change constraint.
	 * @param container
	 * @param component
	 * @param constraint
	 * @param useDefaultConstraint <code>true</code> means use default constraint, ignore constraint parm, else use the constraint parm.
	 * 
	 * @since 1.1.0
	 */
	public static void changeConstraint(Container container, Component component, Object constraint, boolean useDefaultConstraint) {
		Component[] components = container.getComponents();
		int pos = -1;
		for (int i = 0; i < components.length; i++) {
			if (components[i] == component) {
				pos = i;
				break;
			}
		}
		if (pos != -1) {
			if (useDefaultConstraint)
				addComponent(container, component, useDefaultConstraint, pos);
			else
				container.add(component, constraint, pos);
		}
	}
	
	/**
	 * Add the component before the specified component (if found) else add to the end. If useDefaultConstraint is true,
	 * the component will be added with a constraint of the components.getName(). Else it will simply be added at position
	 * with no constraint.
	 * 
	 * @param container
	 * @param addComponent
	 * @param beforeComponent add before this component. If <code>null</code> add to the end.
	 * @param useDefaultConstraint
	 * 
	 * @since 1.1.0
	 */
	public static void addComponentBefore(Container container, Component addComponent, Component beforeComponent, boolean useDefaultConstraint) {
		if (addComponent.getParent() == container) {
			// Need to remove it from this container first because the resulting indexes could be messed up if
			// it wasn't removed first. The problem is particularly in JLayeredPane. It could try to use its own position
			// as the index, and when it is removed, the index would now be too large.
			container.remove(addComponent);
		}		
		int pos = componentIndex(container, beforeComponent, addComponent);
		addComponent(container, addComponent, useDefaultConstraint, pos);
	}
	
	/**
	 * Add the component with the given constraint before the specified component (if found) else add to the end.
	 * @param container
	 * @param addComponent
	 * @param constraint
	 * @param beforeComponent add before this component. If <code>null</code> add to the end.
	 * 
	 * @since 1.1.0
	 */
	public static void addComponentBefore(Container container, Component addComponent, Object constraint, Component beforeComponent) {
		if (addComponent.getParent() == container) {
			// Need to remove it from this container first because the resulting indexes could be messed up if
			// it wasn't removed first. The problem is particularly in JLayeredPane. It could try to use its own position
			// as the index, and when it is removed, the index would now be too large.
			container.remove(addComponent);
		}
		int pos = componentIndex(container, beforeComponent, addComponent);
		container.add(addComponent, constraint, pos);

	}
	
	/**
	 * Remove the component from the container.
	 * @param container
	 * @param component
	 * 
	 * @since 1.1.0
	 */
	public static void removeComponent(Container container, Component component) {
		container.remove(component);
	}
	
	/**
	 * Add a component with no constraint or default constraint at the given position.
	 * @param container
	 * @param addComponent
	 * @param useDefaultConstraint
	 * @param pos
	 * 
	 * @since 1.1.0
	 */
	protected static void addComponent(Container container, Component addComponent, boolean useDefaultConstraint, int pos) {
		if (!useDefaultConstraint)
			container.add(addComponent, pos);
		else
			container.add(addComponent, addComponent.getName(), pos);
	}

}
