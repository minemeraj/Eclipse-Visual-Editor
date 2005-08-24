/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java.beaninfo;

import java.awt.*;
import javax.swing.*;

public class SwatchIcon implements Icon {
	Color color = Color.black;
public SwatchIcon(Color aColor) {
	setColor(aColor);
}
public Color getColor() {
	return color;
}
public int getIconHeight() {
	return 12;
}
public int getIconWidth() {
	return 12;
}
public void paintIcon(Component c, Graphics g, int x, int y) {
	
	int offset = ((g.getFontMetrics().getHeight() - getIconHeight()) / 2) + 1;
	
	g.setColor(Color.black);
	g.drawRect(2,offset,getIconWidth(),getIconHeight());
	g.setColor(getColor());
	g.fillRect(3,offset+1,getIconWidth()-1,getIconHeight()-1);
}
public void setColor(Color aColor) {
	color = aColor;
}
}


