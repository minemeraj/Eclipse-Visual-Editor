/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: GridSpanHandle.java,v $
 *  $Revision: 1.4 $  $Date: 2005-07-12 22:42:52 $ 
 */

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.swt.graphics.Color;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.IVisualComponentListener;
import org.eclipse.ve.internal.cde.core.VisualComponentAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class GridSpanHandle extends ResizeHandle {
	public static int HANDLE_SIZE = DEFAULT_HANDLE_SIZE;
	private IVisualComponentListener fGridComponentListener = new VisualComponentAdapter () {
		public void componentValidated() {
			revalidate();
		};
	};

	public GridSpanHandle(GraphicalEditPart owner, int direction, GridLayoutEditPolicy layoutEditPolicy) {
		super(owner, direction);
		setLocator(new GridSpanHandleLocator(owner, direction, layoutEditPolicy));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.handles.SquareHandle#getBorderColor()
	 */
	protected Color getBorderColor() {
		return ColorConstants.darkGray;
	}
/* (non-Javadoc)
	 * @see org.eclipse.gef.handles.SquareHandle#getFillColor()
	 */
	protected Color getFillColor() {
		return ColorConstants.lightGreen;
	}
	public void addNotify() {
		super.addNotify();
		ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)getOwner().getModel());
		if (beanProxy != null)
			beanProxy.addComponentListener(fGridComponentListener);

	}
	public void removeNotify() {
		ControlProxyAdapter beanProxy = (ControlProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)getOwner().getModel());
		if (beanProxy != null)
			beanProxy.removeComponentListener(fGridComponentListener);
		super.removeNotify();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds();
		r.shrink(1,1);
		try {
			g.setBackgroundColor(getFillColor());
			g.fillRectangle(r.x, r.y, r.width, r.height);
			g.setForegroundColor(getBorderColor());
			g.drawRectangle(r.x, r.y, r.width, r.height);
		} finally {
			//We don't really own rect 'r', so fix it.
			r.expand(1,1);
		}
	}

}
