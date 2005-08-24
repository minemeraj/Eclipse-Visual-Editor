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
 *  $RCSfile: IScannerFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:47 $ 
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
