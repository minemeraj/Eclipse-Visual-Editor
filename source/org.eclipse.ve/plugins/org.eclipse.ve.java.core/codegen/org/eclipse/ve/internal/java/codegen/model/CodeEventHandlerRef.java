/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodeEventHandlerRef.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:28:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import org.eclipse.jdt.core.dom.TypeDeclaration;


/**
 * @author Gili Mendel
 *
 */
public class CodeEventHandlerRef extends CodeTypeRef {
	public CodeEventHandlerRef(TypeDeclaration declType, IBeanDeclModel model) {
		super(declType, model);
	}
}
