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
 *  $RCSfile: ToolBarDecoder.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-20 16:39:41 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.java.HardCodedFeatureMapper;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;

import org.eclipse.ve.internal.swt.SWTConstants;
 

/**
 * 
 * @since 1.0.0
 */
public class ToolBarDecoder extends CompositeDecoder{
	protected final static String ADD_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_TOOLBAR_ITEMS);

	public ToolBarDecoder(){
		super();
		addStructuralFeatureAndWriteMethod(ADD_METHOD_SF_NAME, ADD_METHOD_PREFIX);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.codegen.AbstractCompositeDecoder#getAppropriateFeatureMapper(java.lang.String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		 if (structuralFeature.equals(ADD_METHOD_SF_NAME))
		    return new HardCodedFeatureMapper(ADD_METHOD_SF_NAME, "new");//$NON-NLS-1$
		return super.getAppropriateFeatureMapper(structuralFeature);
	}
	
}
