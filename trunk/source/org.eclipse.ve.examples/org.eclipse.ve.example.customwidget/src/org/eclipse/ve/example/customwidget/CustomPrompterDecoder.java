/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Feb 28, 2005 by Gili Mendel
 * 
 */
package org.eclipse.ve.example.customwidget;

import org.eclipse.ve.internal.swt.codegen.SWTControlDecoder;

/**
 * 
 * @since 1.1.0
 */
public class CustomPrompterDecoder extends SWTControlDecoder {

	protected void initialDecoderHelper() {
		// if it is the text property that this decoder is decoding, use 
		// our special helper
		if (fFeatureMapper.getFeature(null).getName().equals("text"))
			fhelper = new CustomPrompterDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
		else
			super.initialDecoderHelper();
	}
}