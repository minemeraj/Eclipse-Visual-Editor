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
 *  $RCSfile: SWTWidgetDecoder.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.java.*;

import org.eclipse.ve.internal.swt.SWTConstants;
 

/**
 * 
 * This decoder deals with the fact that a Widget may not behave like a control.
 * A constructor does not have the typical semantics of parent/child relationhip
 * a control has with its parent.  A constructor here is just a constructor.
 * 
 * Here parent/child relationships are dealt with a different feature  
 * e.g., setMenuBar(menu);
 */
public class SWTWidgetDecoder extends ObjectDecoder {
	
	// Menu's items
	protected final static String ADD_MENU_ITEMS_METHOD_PREFIX = "setItems"; //$NON-NLS-1$
	protected final static String ADD_MENU_ITEMS_METHOD_SF_NAME = URItoFeature(SWTConstants.SF_MENU_ITEMS);
	
	
	
	



	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.codegen.AbstractCompositeDecoder#getAppropriateFeatureMapper(java.lang.String)
	 */
	protected IJavaFeatureMapper getAppropriateFeatureMapper(String structuralFeature) {
		 if (structuralFeature.equals(ADD_MENU_ITEMS_METHOD_SF_NAME))
		    return new HardCodedFeatureMapper(ADD_MENU_ITEMS_METHOD_SF_NAME, "new");//$NON-NLS-1$
		return null;
	}

	protected IExpressionDecoderHelper getAppropriateDecoderHelper(String structuralFeature) {	
		return null;
	}

	protected boolean isInternalPriorityCacheable() {		
		if ((fhelper instanceof CompositeAddDecoderHelper) ||
			fFeatureMapper.getFeature(null).getName().equals(ADD_MENU_ITEMS_METHOD_SF_NAME)) 
				return false ;
		else
			return super.isPriorityCacheable();
	}
	
	protected void initialDecoderHelper() {
		if (fFeatureMapper.getFeature(null).getName().equals(ADD_MENU_ITEMS_METHOD_SF_NAME)) {
			// Items is the parent/child  like relationship, using the items feature		
			fhelper =  new CompositeAddDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
		}
		else
			super.initialDecoderHelper();
	}	

}
