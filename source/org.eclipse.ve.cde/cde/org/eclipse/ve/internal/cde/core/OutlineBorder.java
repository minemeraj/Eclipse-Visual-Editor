/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: OutlineBorder.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:34 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.SWT;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
/**
 * This border outlines the figure. It will draw the outline
 * on top of the figure, not outside the client area. This is so that
 * the client area won't be reduced by the border.
 */
public class OutlineBorder extends AbstractBorder {
	
	protected Color 
		foreground = ColorConstants.black, 
		background;
	
	private boolean borderDisabled;	// Is the border disabled? If it is then it won't draw.
	private boolean overrideAndDisable;	// A temporary override of the border disabled state. If true, then override and disable, if false no override. 
		
	private static final Insets insets = new Insets(0,0,0,0);
	protected int lineStyle = SWT.LINE_SOLID;

	public OutlineBorder() {
	}
	
	public OutlineBorder(Color foreground, Color background) {
		this.foreground = foreground;
		this.background = background;
	}
	
	public OutlineBorder(Color foreground, Color background, int lineStyle) {
		this(foreground, background);
		this.lineStyle = lineStyle;
	}

	
	public void paint(IFigure aFigure, Graphics g, Insets insets) {
		if (overrideAndDisable || borderDisabled)
			return;	// Disabled, don't pain.
		Rectangle r = getPaintRectangle(aFigure, insets);
		r.resize(-1,-1);	// Make room for the outline.
		g.setForegroundColor(foreground);
		if (lineStyle != SWT.LINE_SOLID) {
			// Non-solid lines need a background color to be set. If we have one use it, else compute it.
			if (background != null)
				g.setBackgroundColor(background);
			else {
				// If no background is set then make the background black
				// and set it to XOR true.  This means the line will dash over
				// the background.  The foreground will also XOR
				// so it only works well if the foreground is Black or Gray.  Colors
				// don't work well because they only paint true on black
				// areas
				g.setBackgroundColor(ColorConstants.black);
				g.setXORMode(true);
			}
		}
		g.setLineStyle(lineStyle);
		g.drawRectangle(r);
	}
	
	public void setColors(Color foreground, Color background) {
		this.foreground = foreground;
		this.background = background;
	}
	
	public void setLineStyle(int aStyle) {
		lineStyle = aStyle;
	}
	
	public Insets getInsets(IFigure aFigure) {
		return insets;
	}
	
	public boolean isOpaque() {
		return true;
	}

	/**
	 * Set the border to be disable so it doesn't paint.
	 * @param borderDisabled The borderDisabled to set.
	 * @since 1.0.0
	 */
	public void setBorderDisabled(boolean borderDisabled) {
		this.borderDisabled = borderDisabled;
	}

	/**
	 * Return whether the borderDisable flag is on.
	 * @return Returns the borderDisabled.
	 * 
	 * @since 1.0.0
	 */
	public boolean isBorderDisabled() {
		return borderDisabled;
	}

	/**
	 * Override the current border disable setting and force a disable if <code>true</code>
	 * or restore current border disable state if <code>false</code>
	 * @param overrideAndDisable <code>true</code> to force disable, <code>false</code> to revert to current disable state.
	 * @since 1.0.0
	 */
	public void setOverrideAndDisable(boolean overrideAndDisable) {
		this.overrideAndDisable = overrideAndDisable;
	}

	/**
	 * Answers if currently override the disable state and it is temporarily disabled.
	 * @return Returns the overrideAndDisable.
	 * 
	 * @since 1.0.0
	 */
	public boolean isOverrideAndDisable() {
		return overrideAndDisable;
	}
	
	
}
