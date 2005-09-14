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
 *  $Revision: 1.5 $  $Date: 2005-09-14 21:22:55 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
 

/**
 * @see org.eclipse.ve.internal.java.codegen.model.IScannerFactory
 * @since 1.0.2
 */
public class DefaultScannerFactory implements IScannerFactory {
	
	/*
	 * Contains a map of the scanner options to a scanner instance.
	 */
	Map optionsToScannerMap = new HashMap();
	
	/* 
	 * Returns one scanner per BDM as the creation of a scanner is an expensive
	 * operation. Creating a scanner for every expression is inefficient.
	 * 
	 * @return IScanner per BDM
	 */
	public IScanner getScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean recordLineSeparator) {
		String optionsKey = getOptionsKey(tokenizeComments, tokenizeWhiteSpace, recordLineSeparator);
		IScanner scanner = (IScanner) optionsToScannerMap.get(optionsKey);
		if(scanner==null){
			scanner = ToolFactory.createScanner(tokenizeComments, tokenizeWhiteSpace, false, recordLineSeparator); // assertMode is always false - hence changing it once is enough
			optionsToScannerMap.put(optionsKey, scanner);
		}
		return scanner;
	}
	
	private String getOptionsKey(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean recordLineSeparator){
		return 	new Boolean(tokenizeComments).toString() + 
				new Boolean(tokenizeWhiteSpace).toString() + 
				new Boolean(recordLineSeparator).toString();
	}
}
