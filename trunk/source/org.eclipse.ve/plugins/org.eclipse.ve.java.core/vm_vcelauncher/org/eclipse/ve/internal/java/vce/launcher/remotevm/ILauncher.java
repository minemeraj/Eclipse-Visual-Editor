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
 *  $RCSfile: ILauncher.java,v $
 *  $Revision: 1.1 $  $Date: 2004-05-18 13:56:08 $ 
 */
package org.eclipse.ve.internal.java.vce.launcher.remotevm;
 

/**
 * 
 * @since 1.0.0
 */
public interface ILauncher {

	public boolean supportsLaunching(Class clazz, Object bean);
	public void launch(Class clazz, Object bean, String[] args);
}
