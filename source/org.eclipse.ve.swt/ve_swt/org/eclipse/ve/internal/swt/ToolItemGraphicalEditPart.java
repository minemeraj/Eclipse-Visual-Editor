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
 *  $RCSfile: ToolItemGraphicalEditPart.java,v $
 *  $Revision: 1.8 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;
 
/**
 * ToolItem Graphical Editpart.
 * 
 * @since 1.1.0
 */
public class ToolItemGraphicalEditPart extends ItemGraphicalEditPart {
	
	public ToolItemGraphicalEditPart(Object model) {
		super(model);
	}
	
	protected Rectangle getBounds() {
		try {
			IRectangleBeanProxy rectBeanProxy = ((ToolItemProxyAdapter) getBeanProxyAdapter()).getBounds();
			if (rectBeanProxy != null) { return new Rectangle(rectBeanProxy.getX(), rectBeanProxy.getY(), rectBeanProxy.getWidth(), rectBeanProxy
					.getHeight()); }
		} catch (ClassCastException e) {
			// ok, means it was undefined.
		}
		return new Rectangle(0, 0, 10, 10);
	}
}
