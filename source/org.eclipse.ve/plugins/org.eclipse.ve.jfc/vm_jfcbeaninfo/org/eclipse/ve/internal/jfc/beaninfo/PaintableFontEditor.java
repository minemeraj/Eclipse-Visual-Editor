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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: PaintableFontEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.awt.*;
/**
 * This property editor is paintable and paints a preview of the chosen font
 */
public class PaintableFontEditor extends FontEditor {
public boolean isPaintable() {
	return true;
}
/**
 * Paint a character in the rectangle in the chosen font
 */
public void paintValue(Graphics gfx, Rectangle box) {
	gfx.setColor( Color.white );
	gfx.fillRect(box.x+1, box.y+1, box.height-2, box.height-2);
	gfx.setColor( Color.black );
	Font font = (Font) getValue();
	gfx.setFont(font);
	FontMetrics fm = gfx.getFontMetrics();
	int xStart = box.x + (box.height/2)-(fm.charWidth('A')/2);
	int yStart = box.y + box.height - 1;
	gfx.drawString(VisualBeanInfoMessages.getString("PaitableFontEditor.SampleString"), xStart, yStart); //$NON-NLS-1$
	gfx.drawRect(box.x+1, box.y+1, box.height-2, box.height-2);
}
}
