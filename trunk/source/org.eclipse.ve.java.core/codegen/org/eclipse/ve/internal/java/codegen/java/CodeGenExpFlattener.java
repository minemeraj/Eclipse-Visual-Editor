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
 *  $RCSfile: CodeGenExpFlattener.java,v $
 *  $Revision: 1.1 $  $Date: 2004-02-03 20:11:36 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.jem.internal.instantiation.PTInstanceReference;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class CodeGenExpFlattener extends NaiveExpressionFlattener {

		IBeanDeclModel fmodel ;
		
		
		public CodeGenExpFlattener (IBeanDeclModel model) {
			super() ;
			fmodel = model ;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.instantiation.ParseVisitor#visit(org.eclipse.jem.internal.instantiation.PTTypeLiteral)
		 */
		public boolean visit(PTInstanceReference node) {
			IJavaObjectInstance obj = node.getObject() ;
		    BeanPart bp = fmodel.getABean(obj);
		    if (bp!=null)
		    	  buffer.append(bp.getSimpleName()) ;
		    else {
		    	  buffer.append("new ");
		    	  buffer.append(((JavaHelpers)obj.eClass()).getName());
		    }
			return false;
		}
		


}
