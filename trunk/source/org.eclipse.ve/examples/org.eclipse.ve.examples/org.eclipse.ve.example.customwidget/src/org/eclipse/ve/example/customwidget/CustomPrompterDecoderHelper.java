
package org.eclipse.ve.example.customwidget;

import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;


public class CustomPrompterDecoderHelper extends SimpleAttributeDecoderHelper {

	public CustomPrompterDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#generate(java.lang.Object[])
	 */
	public String generate(Object[] noArgs) throws CodeGenException {		
		String result = super.generate(noArgs);
		// Add a comment at the end of the expression 
		int idx = result.lastIndexOf(';') + 1;
		return result.substring(0, idx) + " // Prompter Text Property " + result.substring(idx, result.length());
	}
}