/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ISWTFeatureMapper.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;
 

/**
 * @author sri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ISWTFeatureMapper extends IJavaFeatureMapper {
	
	public static String  COMPOSITE_BOUNDS_FEATURE_NAME = "bounds"; //$NON-NLS-1$
	public static String  COMPOSITE_BOUNDS_NAME = "setBounds"; //$NON-NLS-1$
	public static String  COMPOSITE_SIZE_FEATURE_NAME = "size"; //$NON-NLS-1$
	public static String  COMPOSITE_SIZE_NAME = "setSize"; //$NON-NLS-1$

	// If setBounds() or setSize() on composite, reduce its priority as re-lays out its children
	final static int PRIORITY_COMPOSITE_BOUNDS = IJavaFeatureMapper.PRIORITY_ADD - 1000;
	
}
