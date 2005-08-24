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
 *  $RCSfile: FixedContentExpressionParser.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.jface.codegen;

import org.eclipse.ve.internal.java.codegen.model.IScannerFactory;
import org.eclipse.ve.internal.java.codegen.util.ExpressionParser;
 

public class FixedContentExpressionParser extends ExpressionParser {

	public FixedContentExpressionParser(String sourceSnippet, int expOffset, int expLen, IScannerFactory scannerFactory) {
		super(sourceSnippet, expOffset, expLen, scannerFactory);
	}
	
	public String getSelectorContent() {
		return getCode();
	}

	protected void primParseComment() {
		fCommentsOff = fSourceOff+fSourceLen;
		fCommentsLen = 0;
	}

	protected void primParseExpression() {
		fFillerOff = fSourceOff;
		fFillerLen = 0;
		fExpLen = fSourceLen;
	}
}
