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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagLayoutCursorHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:42:05 $ 
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PopUpHelper;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.widgets.Control;

public class GridBagLayoutCursorHelper extends PopUpHelper {

	public GridBagLayoutCursorHelper(Control c){
		super(c);
	}
	
	public void dispose(){
		getShell().dispose();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.PopUpHelper#hookShellListeners()
	 */
	public void showCursorFigure(IFigure cursorFigure, int x, int y){
		if(cursorFigure != null){
			getLightweightSystem().setContents(cursorFigure);
			Dimension cursorSize = cursorFigure.getPreferredSize();
			setShellBounds(x, y, cursorSize.width, cursorSize.height);
			show();
		}
	}
	protected void hookShellListeners() {
	}

}
