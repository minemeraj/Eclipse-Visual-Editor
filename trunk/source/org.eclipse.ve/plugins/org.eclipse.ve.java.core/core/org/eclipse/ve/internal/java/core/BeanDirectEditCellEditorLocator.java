package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanDirectEditCellEditorLocator.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:37 $ 
 */

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

public class BeanDirectEditCellEditorLocator implements CellEditorLocator {
	IFigure figure;
	
	public BeanDirectEditCellEditorLocator(IFigure f) {
		figure = f;
	}
	
	public void relocate(CellEditor cellEditor){
		Text text = (Text)cellEditor.getControl();
		Point sel = text.getSelection();
		Point pref = text.computeSize(-1, -1);
		Rectangle rect = figure.getBounds().getCopy();
		figure.translateToAbsolute(rect);
		text.setBounds(rect.x, rect.y, pref.x+1, pref.y+1);	
		text.setBackground(ColorConstants.white);
		text.setSelection(0);
		text.setSelection(sel);
	}
}
