/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IScannerFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2005-01-20 22:05:24 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import org.eclipse.jdt.core.compiler.IScanner;
 

/**
 * Factory for dealing with IScanners. Creating of IScanner is an expensive task, hence 
 * having one factory provides a single scanner for multiple usages. A default implementation
 * is provided as DefaultScannerFactory.
 * 
 * @see org.eclipse.ve.internal.java.codegen.model.DefaultScannerFactory
 * @since 1.0.2
 */
public interface IScannerFactory {
	public IScanner getScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean recordLineSeparator);
}
