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
/*
 *  $RCSfile: GridLayoutEditPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:09 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.ui.IActionFilter;

import org.eclipse.ve.internal.cde.core.CustomizeLayoutPage;
import org.eclipse.ve.internal.cde.core.CustomizeLayoutWindowAction;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 

/**
 * 
 * @since 1.0.0
 */
public class GridLayoutEditPolicy extends FlowLayoutEditPolicy implements IActionFilter {
	
	public final static String LAYOUT_ID = "java.awt.GridLayout"; //$NON-NLS-1$
	
	public GridLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
	}
	
	public void activate() {
		super.activate();
		CustomizeLayoutWindowAction.addLayoutCustomizationPage(getHost().getViewer(), GridLayoutLayoutPage.class);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith(CustomizeLayoutPage.LAYOUT_POLICY_KEY) && value.equals(LAYOUT_ID))
			return true;
		
		return false;
	}
	
}
