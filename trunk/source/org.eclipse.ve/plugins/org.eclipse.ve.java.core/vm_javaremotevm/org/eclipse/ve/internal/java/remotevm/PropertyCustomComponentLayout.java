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
package org.eclipse.ve.internal.java.remotevm;
/*
 *  $RCSfile: PropertyCustomComponentLayout.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.awt.*;

/**
 * A layout manager especially for the BeanPropertyEditorFrame's.
 * What it does is handle one component (which will be the custom editor component)
 * and will return preferred size taking into account the actual set size of the 
 * component. This is necessary because VaJava allowed components that didn't
 * respond with a preferred size greater than zero, but instead had set the
 * size of the component explicitly within themselves. This is not good, they
 * should of used preferred size layouts to adjust for things, but in the beginning
 * everyone was using null layouts. So we need to handle it too.
 * 
 * <package> protected because no one else should use it.
 * @author richkulp
 *
 */
class PropertyCustomComponentLayout implements LayoutManager {

	private static final Dimension USE_CALCULATED = new Dimension();

	private Dimension savedSize;

	/**
	 * @see java.awt.LayoutManager#addLayoutComponent(String, Component)
	 */
	public void addLayoutComponent(String name, Component comp) {
	}

	/**
	 * @see java.awt.LayoutManager#removeLayoutComponent(Component)
	 */
	public void removeLayoutComponent(Component comp) {
		savedSize = null;
	}

	/**
	 * @see java.awt.LayoutManager#preferredLayoutSize(Container)
	 */
	public Dimension preferredLayoutSize(Container parent) {
		// The algorithim is that the size is queried and if
		// both dimensions are non-zero, then that will be used
		// as the preferred size, otherwise it will go on and
		// query the preferred size and use that.
		// It will save this original size so that if preferred
		// size is requested again (such as another pack is done),
		// then we will use that size instead of asking for the
		// current size. This is because it may of been resized 
		// due to someone changing the frame size.

		Dimension preferred = null;
		if (parent.getComponentCount() == 0)
			preferred = new Dimension(); // No component yet
		else if (savedSize != USE_CALCULATED) {
			if (savedSize != null)
				preferred = new Dimension(savedSize);
			else {
				Component component = parent.getComponent(0);
				Dimension size = component.getSize();
				if (size.width != 0 && size.height != 0)
					preferred = new Dimension(savedSize = size);
				else {
					savedSize = USE_CALCULATED;
					preferred = component.getPreferredSize();
				}
			}
		} else
			preferred = parent.getComponent(0).getPreferredSize();

		Insets insets = parent.getInsets();
		preferred.width += insets.left + insets.right;
		preferred.height += insets.top + insets.bottom;
		return preferred;
	}

	/**
	 * @see java.awt.LayoutManager#minimumLayoutSize(Container)
	 */
	public Dimension minimumLayoutSize(Container parent) {
		// The algorithim is that the size is queried and if
		// both dimensions are non-zero, then that will be used
		// as the minimum size, otherwise it will go on and
		// query the minimum size and use that.
		// It will save this original size so that if minimum
		// size is requested again (such as another pack is done),
		// then we will use that size instead of asking for the
		// current size. This is because it may of been resized 
		// due to someone changing the frame size.

		Dimension minimum = null;
		if (parent.getComponentCount() == 0)
			minimum = new Dimension(); // No component yet
		else if (savedSize != USE_CALCULATED) {
			if (savedSize != null)
				minimum = new Dimension(savedSize);
			else {
				Component component = parent.getComponent(0);
				Dimension size = component.getSize();
				if (size.width != 0 && size.height != 0)
					minimum = new Dimension(savedSize = size);
				else {
					savedSize = USE_CALCULATED;
					minimum = component.getMinimumSize();
				}
			}
		} else
			minimum = parent.getComponent(0).getMinimumSize();

		Insets insets = parent.getInsets();
		minimum.width += insets.left + insets.right;
		minimum.height += insets.top + insets.bottom;
		return minimum;
	}

	/**
	 * @see java.awt.LayoutManager#layoutContainer(Container)
	 */
	public void layoutContainer(Container parent) {
		if (parent.getComponentCount() != 0) {
			// It will size the component to be the same size as the container.
			Insets insets = parent.getInsets();
			Dimension size = parent.getSize();

			int y = insets.top;
			int height = size.height - insets.bottom;
			int x = insets.left;
			int width = size.width - insets.right;

			parent.getComponent(0).setBounds(x, y, width, height);
		}
	}

}
