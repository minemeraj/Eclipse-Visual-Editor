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
 *  $RCSfile: TreeDecoder.java,v $
 *  $Revision: 1.3 $  $Date: 2005-11-08 18:31:09 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper;

import org.eclipse.ve.internal.swt.SWTConstants;

 

/**
 * @author sri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeDecoder extends CompositeDecoder {
	protected final static String ADD_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_TREE_COLUMNS);
	protected final static String ADD_TREEITEMS_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_TREEITEMS_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_TREE_ITEMS);

	public TreeDecoder(){
		super();
		addStructuralFeatureAndWriteMethod(ADD_METHOD_SF_NAME, ADD_METHOD_PREFIX);
		addStructuralFeatureAndWriteMethod(ADD_TREEITEMS_METHOD_SF_NAME, ADD_TREEITEMS_METHOD_PREFIX);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.codegen.AbstractCompositeDecoder#getAppropriateFeatureMapper(java.lang.String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		 if (structuralFeature.equals(ADD_METHOD_SF_NAME) || structuralFeature.equals(ADD_TREEITEMS_METHOD_SF_NAME))
		    return new CompositeFeatureMapper();
		return super.getAppropriateFeatureMapper(structuralFeature);
	}
}
