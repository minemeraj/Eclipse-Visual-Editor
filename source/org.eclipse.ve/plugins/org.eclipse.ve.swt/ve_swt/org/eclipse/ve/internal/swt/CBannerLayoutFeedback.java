/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CBannerLayoutFeedback.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
 
/**
 * This class provides feedback for the CBanners layout.  Open regions appear in a light color while
 * filled regions appear in a dark color.  If the CBanner is simple the left and right will be separated
 * with a vertical line, otherwise there will be a bezier separating them, as a CBanner has at runtime.
 * 
 * @since 1.1
 */
public class CBannerLayoutFeedback extends RectangleFigure {
	
	protected int fLineWidth = 1;
	protected int fLineStyle = SWT.LINE_DASHDOT;
	private java.lang.String[] filledRegions;
	
	private static final String LEFT_CONTROL = "Left"; //$NON-NLS-1$
	private static final String RIGHT_CONTROL = "Right"; //$NON-NLS-1$
	private static final String BOTTOM_CONTROL = "Bottom"; //$NON-NLS-1$
	
	public CBannerLayoutFeedback() {
		super();
	}
	public void fillShape(Graphics g) {
		Rectangle r = getBounds().getCopy();
		// Fill in the already occupied regions
		if (getFilledRegions() != null ) {
			final Rectangle leftRect = new Rectangle( r.x, r.y, r.width / 2, r.height / 2);
			final Rectangle rightRect = new Rectangle( r.x + r.width / 2, r.y, r.width / 2, r.height / 2);
			final Rectangle bottomRect = new Rectangle( r.x, r.y + r.height / 2, r.width, r.height / 2);

			g.setBackgroundColor(ColorConstants.gray);
			g.setXORMode(true);
			for (int i = 0; i < getFilledRegions().length; i++) {
				if (getFilledRegions()[i].equals(CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(CBannerLayoutPolicyHelper.LEFT_INDEX)))
					g.fillRectangle(leftRect);
				if (getFilledRegions()[i].equals(CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(CBannerLayoutPolicyHelper.RIGHT_INDEX)))
					g.fillRectangle(rightRect);
				if (getFilledRegions()[i].equals(CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(CBannerLayoutPolicyHelper.BOTTOM_INDEX)))
					g.fillRectangle(bottomRect);
			}
		}
		g.setForegroundColor(ColorConstants.green);

		boolean simple = CBannerLayoutEditPolicy.isSimple();
		int[] curve;
		
		int h = r.height / 2 - 1;
		int half_width = r.width / 2;
		int quarter_width = r.width / 4;
		int curve_width = r.width / 6;
		
		// Draw the left half of the horizontal line solid
		g.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
		g.drawLine(r.x, r.y + r.height / 2, r.x + half_width, r.y + r.height / 2);

		// Draw the curved line 
		if (simple) {
			g.drawLine(r.x + r.width / 2 - 1, r.y, r.x + r.width / 2 - 1, r.y + r.height / 2);
		} else {
			curve = bezier(r.x + half_width, r.y + h,
					r.x + half_width + curve_width, r.y + h - 1,
					r.x + r.width - quarter_width - curve_width, r.y + 1,
					r.x + r.width - curve_width, r.y, 
					curve_width);
			
			g.drawPolyline(curve);
		}
		
		// Draw the right half of the horizontal line dashed
		g.setLineStyle(SWT.LINE_DASH);
		g.drawLine(r.x + half_width, r.y + r.height / 2, r.x + r.width - 1, r.y + r.height / 2);
	}
	
	public String getCurrentConstraint(Point p) {
		if (p == null)
			return null;
		Rectangle r = getBounds().getCopy();
		Rectangle leftRect = new Rectangle(r.x, r.y, r.width / 2, r.height / 2);
		Rectangle rightRect = new Rectangle(r.x + r.width / 2, r.y, r.width / 2, r.height / 2);
		Rectangle bottomRect = new Rectangle(r.x, r.y + r.height / 2, r.width, r.height / 2);

		if (leftRect.contains(p))
			return LEFT_CONTROL;
		if (rightRect.contains(p))
			return RIGHT_CONTROL;
		if (bottomRect.contains(p))
			return BOTTOM_CONTROL;
		return null;
	}
	
	public Rectangle getCurrentRectangle(Point p) {
		if (p == null)
			return null;
		Rectangle r = getBounds().getCopy();
		Rectangle leftRect = new Rectangle(r.x, r.y, r.width / 2, r.height / 2);
		Rectangle rightRect = new Rectangle(r.x + r.width / 2, r.y, r.width / 2, r.height / 2);
		Rectangle bottomRect = new Rectangle(r.x, r.y + r.height / 2, r.width, r.height / 2);
		
		if (leftRect.contains(p))
			return leftRect;
		if (rightRect.contains(p))
			return rightRect;
		if (bottomRect.contains(p))
			return bottomRect;
		return null;
	}
	
	public static String getDisplayConstraint(String constraint){
		int ndx = CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS.indexOf(constraint);
		return ndx > -1 ?
			(String) CBannerLayoutPolicyHelper.DISPLAY_TAGS.get(ndx) : ""; //$NON-NLS-1$
	}
	protected java.lang.String[] getFilledRegions() {
		return filledRegions;
	}
	public int getLineWidth() {
		return fLineWidth;
	}
	public void setFilledRegions(java.lang.String[] newFilledRegions) {
		filledRegions = newFilledRegions;
		invalidate();
	}
	public void setLineStyle(int newLineStyle) {
		fLineStyle = newLineStyle;
	}
	public void setLineWidth(int newLineWidth) {
		fLineWidth = newLineWidth;
	}
	
	/**
	 * This code is used from the CBanner class to calculate the curve used in a non-simple
	 * CBanner to separate the left control from the right control.
	 * 
	 * @since 1.1
	 */
	private int[] bezier(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int count) {
		
		double a0 = x0;
		double a1 = 3*(x1 - x0);
		double a2 = 3*(x0 + x2 - 2*x1);
		double a3 = x3 - x0 + 3*x1 - 3*x2;
		double b0 = y0;
		double b1 = 3*(y1 - y0);
		double b2 = 3*(y0 + y2 - 2*y1);
		double b3 = y3 - y0 + 3*y1 - 3*y2;

		int[] polygon = new int[2*count + 2];
		for (int i = 0; i <= count; i++) {
			double t = (double)i / (double)count;
			polygon[2*i] = (int)(a0 + a1*t + a2*t*t + a3*t*t*t);
			polygon[2*i + 1] = (int)(b0 + b1*t + b2*t*t + b3*t*t*t);
		}
		return polygon;
	}
}
