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
 *  $RCSfile: FreeFormAWTContainer.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.awt.*;

/**
 * This panel excepts to only have one component in it. It will use normal preferred size unless the container has been told to use the component's
 * actual size instead.
 * 
 * This allows for the component to either change with the preferred size of the child, or the child's actual size.
 */
public class FreeFormAWTContainer extends java.awt.Panel {

	
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 3914392199590971966L;

	public static class FreeFormLayoutManager implements LayoutManager2 { 

		public Boolean useComponentSize;	// Until this is set, this layout will not layout child.
		
		private Component comp;

		public void addLayoutComponent(Component comp, Object constraints) {
			this.comp = comp;
		}

		public Dimension maximumLayoutSize(Container target) {
			return comp != null ? comp.getMaximumSize() : new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		}

		public float getLayoutAlignmentX(Container target) {
			return comp != null ? comp.getAlignmentX() : CENTER_ALIGNMENT;
		}

		public float getLayoutAlignmentY(Container target) {
			return comp != null ? comp.getAlignmentY() : CENTER_ALIGNMENT;
		}

		public void invalidateLayout(Container target) {
		}

		public void addLayoutComponent(String name, Component comp) {
			addLayoutComponent(comp, name);
		}

		public void removeLayoutComponent(Component comp) {
			this.comp = null;
			useComponentSize = null;
		}

		public Dimension preferredLayoutSize(Container parent) {
			if (comp != null && useComponentSize != null) {
				if (useComponentSize.booleanValue())
					return comp.getSize();

				// Kludge. For an awt container that has no layout manager, the preferred size is
				// its current size. This isn't what we really want. We want the preferred size
				// to be the smallest size that fits all of the components.
				Dimension ps = (Dimension) comp.getPreferredSize().clone();
				if (comp instanceof Container) {
					Container container = (Container) comp;
					if (container.getLayout() == null) {
						for (int i = 0; i < container.getComponentCount(); i++) {
							Component c = container.getComponent(i);
							ps.width = Math.max(ps.width, c.getX() + c.getWidth());
							ps.height = Math.max(ps.height, c.getY() + c.getHeight());
						}
					}
					// Need to adjust width, height to at least 10,10 so we can select and see container
					ps.width = Math.max(ps.width, 10);
					ps.height = Math.max(ps.height, 10);
				}
				return ps;
			}
			return new Dimension(10, 10);
		}

		public Dimension minimumLayoutSize(Container parent) {
			return comp != null ? comp.getMinimumSize() : new Dimension(10,10);
		}

		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				if (comp != null && useComponentSize != null) {
					comp.setBounds(0, 0, parent.getWidth(), parent.getHeight());
				}
			}
		}
	}
	
	public FreeFormAWTContainer() {
		super(new FreeFormLayoutManager());
	}

	/**
	 * Set whether to use the component size or preferred size.
	 */
	public void setUseComponentSize(boolean useSize) {
		((FreeFormLayoutManager) getLayout()).useComponentSize = Boolean.valueOf(useSize);
	}
}
