package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JPopupMenuProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

/**
 * @author pwalker
 *
 * Proxy adapter for JPopupMenu. The correct structural feature is
 * retrieved based on "items" SF for the specific type. see getSFItems().
 *
 */
public class JPopupMenuProxyAdapter extends JMenuProxyAdapter {

	/**
	 * Constructor for JPopupMenuProxyAdapter.
	 * @param domain
	 */
	public JPopupMenuProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/**
	 * Windows need to be positioned off screen and also made visible
	 * even when the MOF model says they are mot
	 */
	public IBeanProxy instantiateOnFreeForm(IBeanProxy aFreeFormDialogHost) {

		// Need to make sure location/visibility are good before we instantiate so that it won't flash on the screen.
		applyLocation(false, BeanAwtUtilities.getOffScreenLocation());
		applyVisibility(false, Boolean.TRUE);
	
		if (!isBeanProxyInstantiated() && getErrorStatus() != ERROR_SEVERE)
			instantiateBeanProxy();	// If not already instantiated, and not errors, try again. If already instantiated w/severe, don't waste time

		if (getErrorStatus() == ERROR_SEVERE)
			return null; // It is bad, don't go on.

		// Having done this the frame on the target VM will now possibly be visible
		// We should attempt to restore focus to the IDE
		if (Display.getCurrent().getActiveShell() != null)
			Display.getCurrent().getActiveShell().setFocus();

		return super.instantiateBeanProxy();

	}
	/**
	 * revalidate - hide and show the popup menu so it will resize correctly to show its components
	 */
	public void revalidateBeanProxy() {
//		BeanAwtUtilities.invoke_jpopup_revalidate(getBeanProxy());
		super.revalidateBeanProxy();
	}
}
