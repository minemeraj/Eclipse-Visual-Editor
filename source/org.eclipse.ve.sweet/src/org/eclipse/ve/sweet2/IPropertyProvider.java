package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;
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
 *  $RCSfile: IPropertyProvider.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-01 14:38:32 $ 
 */

/**
 * 
 * @since 1.0.0
 */
public interface IPropertyProvider extends IContentProvider {

	public Object getValue();
	public void setValue(Object value);

}
