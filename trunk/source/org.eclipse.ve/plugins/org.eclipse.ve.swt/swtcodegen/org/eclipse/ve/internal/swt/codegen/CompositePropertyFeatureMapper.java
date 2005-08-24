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
 *  $RCSfile: CompositePropertyFeatureMapper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.java.PropertyFeatureMapper;
 

/**
 * @author sri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CompositePropertyFeatureMapper extends PropertyFeatureMapper {
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper#getFeaturePriority(java.lang.String)
	 */
	public int getFeaturePriority(String methodType) {
		// If setBounds() or setSize() on composite, reduce its priority as re-lays out its children
		if(		methodType.equals(ISWTFeatureMapper.COMPOSITE_BOUNDS_NAME) ||
				methodType.equals(ISWTFeatureMapper.COMPOSITE_SIZE_NAME))
			return ISWTFeatureMapper.PRIORITY_COMPOSITE_BOUNDS; 
		return super.getFeaturePriority(methodType);
	}
}
