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
 *  $RCSfile: TableColumnDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-16 20:29:43 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.java.AllocationFeatureMapper;
 

/**
 * @author sri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableColumnDecoder extends SWTControlDecoder {

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.swt.codegen.SWTControlDecoder#initialDecoderHelper()
 */
protected void initialDecoderHelper() {
	if (fFeatureMapper.getFeature(null).getName().equals(AllocationFeatureMapper.ALLOCATION_FEATURE))
		fhelper =  new SWTConstructorDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this, "tableColumns");
	else
		super.initialDecoderHelper();
}
}
