/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java.vm;

import java.awt.Canvas;
import java.awt.Graphics;

public class UsesJPanelForCustomizer_EXPLICIT_TRUE extends Canvas implements UsesJPanelForCustomizer {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 4762099041643087970L;
	String fTitle;

	
	public String getTitle() {
		return fTitle;
	}
	
	public void setTitle(String title) {
		fTitle = title;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(fTitle != null){
			g.drawString(fTitle,0,getHeight()/2);
		}
	}

}
