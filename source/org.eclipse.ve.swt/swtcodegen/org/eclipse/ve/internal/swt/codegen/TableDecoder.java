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
 *  $RCSfile: TableDecoder.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:56 $ 
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
public class TableDecoder extends CompositeDecoder {
	protected final static String ADD_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_TABLE_COLUMNS);
	protected final static String ADD_TABLEITEMS_METHOD_PREFIX = "create"; //$NON-NLS-1$
	protected final static String ADD_TABLEITEMS_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_TABLE_ITEMS);

	public TableDecoder(){
		super();
		addStructuralFeatureAndWriteMethod(ADD_METHOD_SF_NAME, ADD_METHOD_PREFIX);
		addStructuralFeatureAndWriteMethod(ADD_TABLEITEMS_METHOD_SF_NAME, ADD_TABLEITEMS_METHOD_PREFIX);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.codegen.AbstractCompositeDecoder#getAppropriateFeatureMapper(java.lang.String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		 if (structuralFeature.equals(ADD_METHOD_SF_NAME) || structuralFeature.equals(ADD_TABLEITEMS_METHOD_SF_NAME))
		    return new CompositeFeatureMapper();
		return super.getAppropriateFeatureMapper(structuralFeature);
	}
}
