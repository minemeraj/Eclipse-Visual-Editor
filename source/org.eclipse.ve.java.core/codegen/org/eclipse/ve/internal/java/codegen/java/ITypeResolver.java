package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: ITypeResolver.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-06 21:43:09 $ 
 */
/**
 * @version 	1.0
 * @author
 */
public interface ITypeResolver {
	/**
	 * Get unqualified and resolve it
	 * @param unresolved  
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public String resolve(String unresolved);
	/**
	 * Get unqualified and resolve it, not including Field accessors.
	 * e.g., org.eclipse.swt.SWT.None will return org.eclipse.swt.SWT
	 * @param unresolved
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public String resolveType(String unresolved);
	public String resolveThis();
}
