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
 *  $RCSfile: WindowManager.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-11 19:01:39 $ 
 */
package org.eclipse.ve.internal.jfc.vm;

import java.awt.Window;
 

/**
 * Manager for awt.Windows. This extends ComponentManager because it needs state to
 * be saved. So it can't be a static helper class.
 * @since 1.1.0
 */
public class WindowManager extends ComponentManager {
	
	/**
	 * Pack window on any change flag.
	 */
	protected boolean packOnChange;
	
	/**
	 * Set the pack on change flag. 
	 * @param packOnChange <code>true</code> if on any change (invalidate) the Window should be packed. This is used when no explicit size has
	 * been set and it should float to the packed size.
	 * 
	 * @since 1.1.0
	 */
	public void setPackOnChange(boolean packOnChange) {
		this.packOnChange = packOnChange;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.vm.ComponentManager#invalidated()
	 */
	protected void invalidated() {
		super.invalidated();
		if (packOnChange)
			((Window) getComponent()).pack();
	}

	
}
