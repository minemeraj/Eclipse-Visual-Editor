
package org.eclipse.ve.example.customwidget;

import org.eclipse.ve.internal.swt.codegen.SWTControlDecoder;

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