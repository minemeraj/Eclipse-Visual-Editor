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
 *  $Revision: 1.2 $  $Date: 2004-02-03 23:18:16 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
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
		    	  getStringBuffer().append(bp.getSimpleName()) ;
		    else {
		    	if (obj.isSetAllocation()) {
		    		JavaAllocation alloc = obj.getAllocation();
		    		if (alloc instanceof InitStringAllocation)
		    			getStringBuffer().append(((InitStringAllocation) alloc).getInitString());
		    		else if (alloc instanceof ParseTreeAllocation)
		    			((ParseTreeAllocation) alloc).getExpression().accept(this);
		    	} else {
		    		getStringBuffer().append("new ");
		    		getStringBuffer().append(((JavaHelpers)obj.eClass()).getJavaName());
		    		getStringBuffer().append("()");
		    	}
		    }
			return false;
		}
		


}
