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
 *  $RCSfile: IBeanValidator.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-29 18:20:23 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
 

/**
 * 
 * @since 1.0.0
 */
public interface IBeanValidator {
	
	public interface ErrorListener{
		void errorStatusChanged();
	}
	
	void reset();
	void validateAll();
	/** 
	 * 
	 * @return List of errors that are instances of IErrorHolder.ErrorType
	 * 
	 * @since 1.0.0
	 */
	List getErrors();
	void setJavaInstance(IJavaInstance javaInstance);
	void validate(EStructuralFeature sf);
	void addErrorListener(IBeanValidator.ErrorListener errorListener);
	void removeErrorListener(IBeanValidator.ErrorListener errorListener);
}
