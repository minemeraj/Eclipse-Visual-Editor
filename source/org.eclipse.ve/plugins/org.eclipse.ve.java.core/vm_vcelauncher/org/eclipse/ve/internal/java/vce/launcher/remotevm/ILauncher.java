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
 *  $RCSfile: ILauncher.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:29:42 $ 
 */
package org.eclipse.ve.internal.java.vce.launcher.remotevm;
 

/**
 * 
 * @since 1.0.0
 */
public interface ILauncher {

	public boolean supportsLaunching(Class clazz);
	public void launch(Class clazz, String[] args);
}
