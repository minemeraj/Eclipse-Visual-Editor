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
 *  $RCSfile: FreeFormSwingContainer.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:35 $ 
 */
 
import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JPanel;
/**
 * This panel excepts to only have one component in it.
 * It will use normal preferred size unless the container
 * has been told to use the component's actual size instead.
 *
 * This allows for the component to either change
 * with the preferred size of the child, or the child's
 * actual size.
 */
public class FreeFormSwingContainer extends JPanel {
	boolean useComponentSize = false;
	
public FreeFormSwingContainer() {
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
		// We have a component, a sanity check, shouldn't occur but be safe.		
		JComponent comp0 = (JComponent) getComponent(0);		
		if (useComponentSize)
			return comp0.getSize();
			
		if (!comp0.isPreferredSizeSet()) {	
			// Kludge. For most jcomponents that have no layout manager, the preferred size is
			// some real small size (JComponents typically are (1x1)).
			// We don't want this if there are any children, so if there are any children we
			// will get there size/position to calculate the preferred size to make them visible.
			// However, some return a valid size (e.g. JMenuItem) but still don't have a layoutmanager,
			// so in those cases we will (if preferred size not explicitly set) use the max of that size
			// and any children. Hopefully we want run into any that have children but really shouldn't
			// change the preferred size.
			Dimension ps = (Dimension)comp0.getPreferredSize().clone();
			// Check for Container, it should be a JComponent, so it should be a container, but to be safe, test it.		
			if (comp0.getLayout() == null) {
				for (int i=0; i<comp0.getComponentCount(); i++) {
					Component c = comp0.getComponent(i);
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
