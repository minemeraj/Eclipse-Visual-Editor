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
 *  $RCSfile: SWTWidgetChildDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2004-08-20 15:58:49 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.HashMap;

import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.java.AllocationFeatureMapper;
 

/**
 * @author sri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SWTWidgetChildDecoder extends SWTControlDecoder {
	
	static HashMap allocationFeatures ;   // Map a class name to feature
	
	static {
		allocationFeatures=new HashMap();
		allocationFeatures.put("org.eclipse.swt.widgets.TableColumn",TableDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.TabItem",TabFolderDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.CoolBar",CoolBarDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.MenuItem",MenuDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.ToolItem",ToolBarDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
	}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.swt.codegen.SWTControlDecoder#initialDecoderHelper()
 */
protected void initialDecoderHelper() {
	if (fFeatureMapper.getFeature(null).getName().equals(AllocationFeatureMapper.ALLOCATION_FEATURE)) {
		String typeName=((JavaClass)fbeanPart.getEObject().eClass()).getQualifiedName();
		fhelper =  new SWTConstructorDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this, (String)allocationFeatures.get(typeName));
	}
	else
		super.initialDecoderHelper();
}
}
