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
package org.eclipse.ve.internal.swt;
import org.eclipse.ui.IActionFilter;

import org.eclipse.ve.internal.cde.core.CustomizeLayoutPage;
import org.eclipse.ve.internal.cde.core.CustomizeLayoutWindowAction;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * Layout input policy for handling a SWT FormLayout 
 * It'a based off the default FlowLayout for SWT and isHorizontal() 
 * returns true if the 'type' of the FormLayout is also horizontal.
 */
public class FormLayoutEditPolicy extends DefaultLayoutEditPolicy implements IActionFilter {
	
	// unique ID of this layout edit policy
	public static final String LAYOUT_ID = "org.eclipse.swt.layout.FormLayout"; //$NON-NLS-1$

	/**
	 * Create with the container policy for handling DiagramFigures.
	 */
	public FormLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
	}
	/**
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		CustomizeLayoutWindowAction.addLayoutCustomizationPage(getHost().getViewer(), FormLayoutLayoutPage.class);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith(CustomizeLayoutPage.LAYOUT_POLICY_KEY) && value.equals(LAYOUT_ID)) //$NON-NLS-1$
		return true;
		return false;
	}
}
