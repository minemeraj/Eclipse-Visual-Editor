package org.eclipse.ve.internal.java.codegen.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ITransientErrorEvent.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import org.eclipse.emf.ecore.EObject;

/**
 * @version 	1.0
 * @author
 */
public interface ITransientErrorEvent {
	
	public final static String TYPE_PARSER_ERROR = "typeParserFatal"; //$NON-NLS-1$
	public final static String TYPE_COMPILER_ERROR = "typeCompilerError"; //$NON-NLS-1$
	public final static String TYPE_INFO = "typeInformation"; //$NON-NLS-1$
	public final static String TYPE_CORRECTED_BEAN = "typeCorrectedBean"; //$NON-NLS-1$
	public final static String TYPE_CORRECTED_FEATURE = "typeCorrectedFeature"; //$NON-NLS-1$
	
	public String getErrorType();
	
	public EObject getRefObject();
	
	public String getMessage();
	
}
