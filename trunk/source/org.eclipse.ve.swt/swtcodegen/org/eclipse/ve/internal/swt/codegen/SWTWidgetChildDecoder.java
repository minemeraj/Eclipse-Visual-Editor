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
 *  $RCSfile: SWTWidgetChildDecoder.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-04 15:41:48 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.HashMap;

import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.java.AllocationFeatureMapper;
 

/**
 * This decoder will deal with a SWT parent/child relationships represented by a constructor but
 * using a feature the is different from the default (Composite's) 'control'.
 * e.g., a TabbedFolder will have an 'item' as a child.
 * 
 * This is a typical case in a TabItem, TableColumn type of widgets
 */
public class SWTWidgetChildDecoder extends SWTControlDecoder {
	
	static HashMap allocationFeatures ;   // Map a class name to feature
	
	static {
		allocationFeatures=new HashMap();
		allocationFeatures.put("org.eclipse.swt.widgets.TableColumn",TableDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.TableItem",TableDecoder.ADD_TABLEITEMS_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.TreeColumn",TreeDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.TreeItem",TreeDecoder.ADD_TREEITEMS_METHOD_SF_NAME); //$NON-NLS-1$		
		allocationFeatures.put("org.eclipse.swt.widgets.TabItem",TabFolderDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.custom.CTabItem",CTabFolderDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.CoolItem",CoolBarDecoder.ADD_METHOD_SF_NAME); //$NON-NLS-1$
		allocationFeatures.put("org.eclipse.swt.widgets.MenuItem",SWTWidgetDecoder.ADD_MENU_ITEMS_METHOD_SF_NAME); //$NON-NLS-1$
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
