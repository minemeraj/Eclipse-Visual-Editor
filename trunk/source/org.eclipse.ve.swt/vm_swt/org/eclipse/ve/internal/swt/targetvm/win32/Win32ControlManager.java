/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: Win32ControlManager.java,v $
 *  $Revision: 1.2 $  $Date: 2004-07-30 15:20:00 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.win32;

import org.eclipse.ve.internal.swt.targetvm.ControlManager;
import org.eclipse.ve.internal.swt.targetvm.IImageCapture;
 

/**
 * 
 * @since 1.0.0
 */
public class Win32ControlManager extends ControlManager {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.targetvm.ControlManager#getImageCapturer()
	 */
	public IImageCapture getImageCapturer() {
		return new ImageCapture();
	}

}
