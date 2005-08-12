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
 *  $RCSfile: ScrollPaneManager.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-12 21:36:29 $ 
 */
package org.eclipse.ve.internal.jfc.vm;

import java.awt.*;
 
public class ScrollPaneManager {

	/**
	 * See bug 69514 - Dropping AWT ScrollPane fails on Linux 
	 * Exceptions are thrown because the scroll pane doesn't have children. 
	 * To prevent this, we'll add a temporary child (a special Panel) when there isn't one.
	 * 
	 * @param scrollPane    ScrollPane to be processed
	 * 
	 * @since 1.1.0.1
	 */
	public static void makeItRight(ScrollPane scrollPane) {
		Component[] children = scrollPane.getComponents();
		// For empty scroll pane, add the special child.
		if (children.length == 0)
			scrollPane.add(new ScrollPanePanel());
	}

	private static class ScrollPanePanel extends Panel {

		private static final long serialVersionUID = 1L;
	}
}
