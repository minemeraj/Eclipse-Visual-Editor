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
 *  $RCSfile: IImageCapture.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:54:57 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
 

/**
 * 
 * @since 1.0.0
 */
public interface IImageCapture {
	public Image getImage(Control control, boolean includeChildren);
}
