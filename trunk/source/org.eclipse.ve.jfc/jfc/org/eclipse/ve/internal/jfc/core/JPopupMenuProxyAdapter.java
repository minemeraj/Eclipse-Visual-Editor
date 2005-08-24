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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JPopupMenuProxyAdapter.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.ve.internal.java.core.CompositionProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * Proxy adapter for JPopupMenu.
 * <p> The correct structural feature is
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#addToFreeForm(org.eclipse.ve.internal.java.core.CompositionProxyAdapter)
	 */
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter) {
		// JPopupMenu must always be on freeform, but there is no visual for it. So we nned to prevent the normal ComponentProxy freeform stuff.
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#removeFromFreeForm()
	 */
	public void removeFromFreeForm() {
		// JPopupMenu must always be on freeform, but there is no visual for it. So we need to prevent the normal ComponentProxy freeform stuff.
	}

}
