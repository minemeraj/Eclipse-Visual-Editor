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
 *  $RCSfile: DefaultScannerFactory.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.internal.core.util.PublicScanner;
 

/**
 * @see org.eclipse.ve.internal.java.codegen.model.IScannerFactory
 * @since 1.0.2
 */
public class DefaultScannerFactory implements IScannerFactory {
	IScanner fScanner = null;
	/* 
	 * Returns one scanner per BDM as the creation of a scanner is an expensive
	 * operation. Creating a scanner for every expression is inefficient.
	 * 
	 * @return IScanner per BDM
	 */
	public IScanner getScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean recordLineSeparator) {
		if(fScanner==null)
			fScanner = ToolFactory.createScanner(tokenizeComments, tokenizeWhiteSpace, false, recordLineSeparator); // assertMode is always false - hence changing it once is enough
		((PublicScanner)fScanner).tokenizeComments = tokenizeComments;
		((PublicScanner)fScanner).tokenizeWhiteSpace = tokenizeWhiteSpace;
		((PublicScanner)fScanner).recordLineSeparator = recordLineSeparator;
		// temporary fix for PublicScanner not cleaning up internals - this should be removed 
		// once JDT defect 84398 is fixed
		int lineEndsSize = ((PublicScanner)fScanner).lineEnds.length;
		for (int i = 0; i < lineEndsSize; i++) 
			((PublicScanner)fScanner).lineEnds[i]=0;
		return fScanner;
	}

}
