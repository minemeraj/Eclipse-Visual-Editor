/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FillLayoutEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:51:49 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.ui.IActionFilter;

import org.eclipse.ve.internal.cde.core.CustomizeLayoutPage;
import org.eclipse.ve.internal.cde.core.CustomizeLayoutWindowAction;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 

/**
 * 
 * @since 1.0.0
 */
public class FillLayoutEditPolicy extends RowLayoutEditPolicy implements IActionFilter {
	
	// unique ID of this layout edit policy
	public static final String LAYOUT_ID = "org.eclipse.swt.layout.FillLayout";

	/**
	 * @param containerPolicy
	 * 
	 * @since 1.0.0
	 */
	public FillLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith(CustomizeLayoutPage.LAYOUT_POLICY_KEY) && value.equals(LAYOUT_ID)) //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		CustomizeLayoutWindowAction.addLayoutCustomizationPage(getHost().getViewer(), FillLayoutLayoutPage.class);
	}
}
