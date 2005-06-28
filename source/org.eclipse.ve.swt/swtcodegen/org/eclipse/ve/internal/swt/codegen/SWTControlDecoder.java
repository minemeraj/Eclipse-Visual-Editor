/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTControlDecoder.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-28 22:51:35 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.AllocationFeatureMapper;
import org.eclipse.ve.internal.java.codegen.java.ObjectDecoder;
import org.eclipse.ve.internal.java.codegen.model.*;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class SWTControlDecoder extends ObjectDecoder {
	public SWTControlDecoder (CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance cm, BeanPart part) {
		super (expr,model,cm,part) ;
	}

	public SWTControlDecoder() {
		super () ;
	}



	protected void initialDecoderHelper() {
		
		if (fFeatureMapper.getFeature(null).getName().equals(AllocationFeatureMapper.ALLOCATION_FEATURE))
			fhelper =  new SWTConstructorDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this, "controls");					 //$NON-NLS-1$
		else
			super.initialDecoderHelper() ;	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractExpressionDecoder#isPriorityCacheable()
	 */
	protected boolean isPriorityCacheable() {
		if (fhelper instanceof SWTConstructorDecoderHelper)
			return false ;
		else
		    return super.isPriorityCacheable();
	}
}
