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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: CursorHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:12:49 $ 
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PopUpHelper;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.widgets.Control;

public class CursorHelper extends PopUpHelper {

	public CursorHelper(Control c){
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
