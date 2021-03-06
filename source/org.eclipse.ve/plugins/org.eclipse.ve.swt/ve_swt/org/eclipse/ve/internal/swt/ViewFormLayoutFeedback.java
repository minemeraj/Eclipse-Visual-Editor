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
 *  $RCSfile: ViewFormLayoutFeedback.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
 
/**
 * This class provides feedback for the ViewForm layout.  Open regions appear in a light color while
 * filled regions appear in a dark color.
 * 
 * @since 1.1
 */
public class ViewFormLayoutFeedback extends RectangleFigure {
	protected int fLineWidth = 1;
	protected int fLineStyle = SWT.LINE_DASHDOT;
	private java.lang.String[] filledRegions;
	
	private static final String TOP_LEFT_CONTROL = "Top Left"; //$NON-NLS-1$
	private static final String TOP_RIGHT_CONTROL = "Top Right"; //$NON-NLS-1$
	private static final String TOP_CENTER_CONTROL = "Top Center"; //$NON-NLS-1$
	private static final String CONTENT_CONTROL = "Content"; //$NON-NLS-1$
	
	public ViewFormLayoutFeedback() {
		super();
	}
	
	public void fillShape(Graphics g) {
		Rectangle r = getBounds().getCopy();
		// Fill in the already occupied regions
		if (getFilledRegions() != null ) {
			final Rectangle leftRect = new Rectangle( r.x, r.y, r.width / 3, r.height / 3);
			final Rectangle centerRect = new Rectangle( r.x + r.width / 3, r.y, r.width / 3, r.height / 3);
			final Rectangle rightRect = new Rectangle( r.x + 2 * r.width / 3, r.y, r.width / 3, r.height / 3);
			final Rectangle contentRect = new Rectangle( r.x, r.y + r.height / 3, r.width, 2 * r.height / 3);

			g.setBackgroundColor(ColorConstants.gray);
			g.setXORMode(true);
			for (int i = 0; i < getFilledRegions().length; i++) {
				if (getFilledRegions()[i].equals(ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.LEFT_INDEX)))
					g.fillRectangle(leftRect);
				if (getFilledRegions()[i].equals(ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.CENTER_INDEX)))
					g.fillRectangle(centerRect);
				if (getFilledRegions()[i].equals(ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.RIGHT_INDEX)))
					g.fillRectangle(rightRect);
				if (getFilledRegions()[i].equals(ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(ViewFormLayoutPolicyHelper.CONTENT_INDEX)))
					g.fillRectangle(contentRect);
			}
		}
		g.setForegroundColor(ColorConstants.green);
		
		// Draw outlines around each of the drop candidate areas
		g.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
		g.drawLine(r.x, r.y + r.height / 3, r.x + r.width - 1, r.y + r.height / 3);
		g.drawLine(r.x + r.width / 3 - 1, r.y, r.x + r.width / 3 - 1, r.y + r.height / 3);
		g.drawLine(r.x + 2 * r.width / 3 - 1, r.y, r.x + 2 * r.width / 3 - 1, r.y + r.height / 3);
	}
	
	public String getCurrentConstraint(Point p) {
		if (p == null)
			return null;
		Rectangle r = getBounds().getCopy();
		Rectangle leftRect = new Rectangle( r.x, r.y, r.width / 3, r.height / 3);
		Rectangle centerRect = new Rectangle(r.x + r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle rightRect = new Rectangle( r.x + 2 * r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle contentRect = new Rectangle( r.x, r.y + r.height / 3, r.width, 2 * r.height / 3);

		if (leftRect.contains(p))
			return TOP_LEFT_CONTROL;
		if (centerRect.contains(p))
			return TOP_CENTER_CONTROL;
		if (rightRect.contains(p))
			return TOP_RIGHT_CONTROL;
		if (contentRect.contains(p))
			return CONTENT_CONTROL;
		return null;
	}
	
	public Rectangle getCurrentRectangle(Point p) {
		if (p == null)
			return null;
		Rectangle r = getBounds().getCopy();
		Rectangle leftRect = new Rectangle( r.x, r.y, r.width / 3, r.height / 3);
		Rectangle centerRect = new Rectangle(r.x + r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle rightRect = new Rectangle( r.x + 2 * r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle contentRect = new Rectangle( r.x, r.y + r.height / 3, r.width, 2 * r.height / 3);
		
		if (leftRect.contains(p))
			return leftRect;
		if (centerRect.contains(p))
			return centerRect;
		if (rightRect.contains(p))
			return rightRect;
		if (contentRect.contains(p))
			return contentRect;
		return null;
	}
	
	public static String getDisplayConstraint(String constraint){
		int ndx = ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS.indexOf(constraint);
		return ndx > -1 ?
			(String) ViewFormLayoutPolicyHelper.DISPLAY_TAGS.get(ndx) : ""; //$NON-NLS-1$
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
}
