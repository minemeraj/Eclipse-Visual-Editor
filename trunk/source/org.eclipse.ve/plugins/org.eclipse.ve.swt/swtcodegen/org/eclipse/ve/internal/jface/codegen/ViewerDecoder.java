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
 *  Created Oct 5, 2005 by Gili Mendel
 * 
 *  $RCSfile: ViewerDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-06 19:57:25 $ 
 */
 
package org.eclipse.ve.internal.jface.codegen;

import org.eclipse.ve.internal.java.codegen.java.AllocationFeatureMapper;
import org.eclipse.ve.internal.java.codegen.java.ObjectDecoder;
 
/**
 * This decoder deal with the fact that Viewers may have
 * a visual given as part of thier constructor
 * 
 * @since 1.2.0
 */
public class ViewerDecoder extends ObjectDecoder {

	protected void initialDecoderHelper() {
		
		if (fFeatureMapper.getFeature(null).getName().equals(AllocationFeatureMapper.ALLOCATION_FEATURE)) {
			fhelper =  new ViewerConstructorDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
		}
		else		
		   super.initialDecoderHelper();
	}

}
