package org.eclipse.ve.internal.jfc.vm;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ContainerManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:12 $ 
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
 * 
 * Note: It is static. No need to have one for each container. The container
 * will always be passed in.
 */
public class ContainerManager {

	/**
	 * Return the index of the specified component. -1 if not found.
	 */
	public static int componentIndex(Container container, Component beforeComponent, Component addingComponent) {
		Component[] components = container.getComponents();
		for (int i = 0, r = 0; i < components.length; i++, r++) {
			if (components[i] == beforeComponent)
				return r;
			if (components[i] == addingComponent)
				r--;	// Need to adjust because we found adding component first. When it gets added again, it will be removed from old pos, and if we didn't decr r it would then be one to many. 
		}
		
		return -1;
	}
	
	/**
	 * Add a component before the specified component. If the
	 * component is not found, then add at end.
	 */
	public static void addComponentBefore(Container container, Component addComponent, Component beforeComponent) {
		int pos = componentIndex(container, beforeComponent, addComponent);
		container.add(addComponent, pos);
	}
	
	/**
	 * Add a component before the specified component with the given constraint. If the
	 * component is not found, then add at end.
	 */
	public static void addComponentBefore(Container container, Component addComponent, Object constraint, Component beforeComponent) {
		int pos = componentIndex(container, beforeComponent, addComponent);
		container.add(addComponent, constraint, pos);
	}
	

}
