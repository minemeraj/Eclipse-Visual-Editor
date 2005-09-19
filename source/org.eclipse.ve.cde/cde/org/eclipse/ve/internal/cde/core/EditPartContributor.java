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
 *  $RCSfile: EditPartContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-09-19 20:37:48 $ 
 */
package org.eclipse.ve.internal.cde.core;
 

/**
 * Base EditPart contributor. The tree and graphical editpart contributors extend from this one.
 * @since 1.2.0
 */
public interface EditPartContributor {
	
	/**
	 * Tell the contributor to dispose. This will be called when the edit part has been deactivated.
	 * 
	 * 
	 * @since 1.2.0
	 */
	public void dispose();

}
