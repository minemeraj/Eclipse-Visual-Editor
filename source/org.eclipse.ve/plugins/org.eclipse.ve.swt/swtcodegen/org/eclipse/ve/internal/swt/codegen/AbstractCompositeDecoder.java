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
 *  $RCSfile: AbstractCompositeDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-23 12:45:11 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ObjectDecoder;
import org.eclipse.ve.internal.java.codegen.model.*;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public abstract class AbstractCompositeDecoder extends ObjectDecoder {

	/**
	 * @param expr
	 * @param model
	 * @param cm
	 * @param part
	 * 
	 * @since 1.0.0
	 */
	public AbstractCompositeDecoder(CodeExpressionRef expr, IBeanDeclModel model, IDiagramModelInstance cm, BeanPart part) {
		super(expr, model, cm, part);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public AbstractCompositeDecoder() {
		super();
		// TODO Auto-generated constructor stub
	}

}
