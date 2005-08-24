/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.example.customwidget;

import org.eclipse.ve.internal.swt.codegen.SWTControlDecoder;

public class CustomPrompterDecoder extends SWTControlDecoder {

	protected void initialDecoderHelper() {
		// if it is the text property that this decoder is decoding, use a custom helper
		if (fFeatureMapper.getFeature(null).getName().equals("text")) {
			fhelper = new CustomPrompterDecoderHelper(fbeanPart, fExpr, fFeatureMapper, this);
		} else {
			super.initialDecoderHelper();
		}
	}
}
