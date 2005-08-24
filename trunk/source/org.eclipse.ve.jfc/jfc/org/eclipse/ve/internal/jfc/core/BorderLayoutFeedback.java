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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: BorderLayoutFeedback.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class BorderLayoutFeedback extends RectangleFigure {
	protected int fLineWidth = 1;
	protected int fLineStyle = SWT.LINE_DASHDOT;
	private java.lang.String[] filledRegions;
public BorderLayoutFeedback() {
	super();
}
public void fillShape(Graphics g) {
	Rectangle r = getBounds().getCopy();
	// Fill in the already occupied regions
	if (getFilledRegions() != null ) {
		final Rectangle nRect = new Rectangle( r.x, r.y, r.width, r.height/3);
		final Rectangle wRect = new Rectangle( r.x, r.y + r.height / 3, r.width/3, r.height/3);
		final Rectangle cRect = new Rectangle( r.x + r.width / 3, r.y + r.height / 3, r.width/3, r.height/3);
		final Rectangle eRect = new Rectangle( r.x + 2 * r.width / 3, r.y + r.height / 3, r.width/3, r.height/3);
		final Rectangle sRect = new Rectangle( r.x, r.y + 2 * r.height / 3, r.width, r.height/3);
		g.setBackgroundColor(ColorConstants.gray);
		g.setXORMode(true);
		for (int i = 0; i < getFilledRegions().length; i++) {
			if (getFilledRegions()[i].equals(BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.NORTH_INDEX)))
				g.fillRectangle(nRect);
			if (getFilledRegions()[i].equals(BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.SOUTH_INDEX)))
				g.fillRectangle(sRect);
			if (getFilledRegions()[i].equals(BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.EAST_INDEX)))
				g.fillRectangle(eRect);
			if (getFilledRegions()[i].equals(BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.WEST_INDEX)))
				g.fillRectangle(wRect);
			if (getFilledRegions()[i].equals(BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.CENTER_INDEX)))
				g.fillRectangle(cRect);
		}
	}
	g.setForegroundColor(ColorConstants.green);
	// Draw outlines around each of the drop candidate areas
	g.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
	g.drawLine(r.x, r.y + r.height / 3, r.x + r.width - 1, r.y + r.height / 3);
	g.drawLine(r.x, r.y + 2 * r.height / 3, r.x + r.width - 1, r.y + 2 * r.height / 3);
	g.drawLine(r.x + r.width / 3, r.y + r.height / 3, r.x + r.width / 3, r.y + 2 * r.height / 3);
	g.drawLine(r.x + 2 * r.width / 3, r.y + r.height / 3, r.x + 2 * r.width / 3, r.y + 2 * r.height / 3);
	
}
public String getCurrentConstraint(Point p) {
	if (p == null)
		return null;
	Rectangle r = getBounds().getCopy();
	Rectangle nRect = new Rectangle(r.x, r.y, r.width, r.height / 3);
	Rectangle wRect = new Rectangle(r.x, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle cRect = new Rectangle(r.x + r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle sRect = new Rectangle(r.x, r.y + 2 * r.height / 3, r.width, r.height / 3);
	Rectangle eRect = new Rectangle(r.x + 2 * r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	if (nRect.contains(p))
		return (String) BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.NORTH_INDEX);
	if (wRect.contains(p))
		return (String) BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.WEST_INDEX);
	if (cRect.contains(p))
		return (String) BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.CENTER_INDEX);
	if (eRect.contains(p))
		return (String) BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.EAST_INDEX);
	if (sRect.contains(p))
		return (String) BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.get(BorderLayoutPolicyHelper.SOUTH_INDEX);
	return null;
}
public Rectangle getCurrentRectangle(Point p) {
	if (p == null)
		return null;
	Rectangle r = getBounds().getCopy();
	Rectangle nRect = new Rectangle(r.x, r.y, r.width, r.height / 3);
	Rectangle wRect = new Rectangle(r.x, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle cRect = new Rectangle(r.x + r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle sRect = new Rectangle(r.x, r.y + 2 * r.height / 3, r.width, r.height / 3);
	Rectangle eRect = new Rectangle(r.x + 2 * r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	if (nRect.contains(p))
		return nRect;
	if (wRect.contains(p))
		return wRect;
	if (cRect.contains(p))
		return cRect;
	if (eRect.contains(p))
		return eRect;
	if (sRect.contains(p))
		return sRect;
	return null;
}
public static String getDisplayConstraint(String constraint){
	int ndx = BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.indexOf(constraint);
	return ndx > -1 ?
		(String) BorderLayoutPolicyHelper.DISPLAY_TAGS.get(ndx) : ""; //$NON-NLS-1$
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
