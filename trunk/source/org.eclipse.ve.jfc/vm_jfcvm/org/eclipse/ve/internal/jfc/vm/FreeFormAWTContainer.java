package org.eclipse.ve.internal.jfc.vm;
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
 *  $RCSfile: FreeFormAWTContainer.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:35 $ 
 */

import java.awt.*;
/**
 * This panel excepts to only have one component in it.
 * It will use normal preferred size unless the container
 * has been told to use the component's actual size instead.
 *
 * This allows for the component to either change
 * with the preferred size of the child, or the child's
 * actual size.
 */
public class FreeFormAWTContainer extends java.awt.Panel {
	boolean useComponentSize = false;
	
public FreeFormAWTContainer() {
	super(new BorderLayout());	// Use border so that the one child will fill the panel.
}

/**
 * Set whether to use the component size or preferred size.
 */
public void setUseComponentSize(boolean useSize) {
	useComponentSize = useSize;
}


public Dimension getPreferredSize() {
	if (getComponentCount() > 0) {
		// Sanity check only that we have a component. Shouldn't occur.
		Component comp0 = getComponent(0);		
		if (useComponentSize)
			return comp0.getSize();
			
		// Kludge. For an awt container that has no layout manager, the preferred size is
		// its current size. This isn't what we really want. We want the preferred size
		// to be the smallest size that fits all of the components.
		if (comp0 instanceof Container) {
			Container container = (Container) comp0;
			Dimension ps = (Dimension)container.getPreferredSize().clone();
			if (container.getLayout() == null) {			
				for (int i=0; i<container.getComponentCount(); i++) {
					Component c = container.getComponent(i);
					ps.width = Math.max(ps.width, c.getX() + c.getWidth());
					ps.height = Math.max(ps.height, c.getY() + c.getHeight());
				}
			}
			// Need to adjust width, height to at least 10,10 so we can select and see container
			ps.width = Math.max(ps.width, 10);
			ps.height = Math.max(ps.height, 10);
			return ps;
		}
	}
	return super.getPreferredSize();
}
		
}
