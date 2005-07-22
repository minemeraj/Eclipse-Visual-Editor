/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodeGenExpFlattener.java,v $
 *  $Revision: 1.9 $  $Date: 2005-07-22 21:17:32 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
 



/**
 * This class will convert a PTree into a string, while resolving instance references,
 * and removing qualified names, while adding import requirements.
 * 
 * @author Gili Mendel
 * @since 1.0.0
 */
public class CodeGenExpFlattener extends NaiveExpressionFlattener {

		IBeanDeclModel fmodel ;
		List 		   fimportList;
		List		   frefList; 
		
		List		   visitedList = new ArrayList();
		
		
		public CodeGenExpFlattener (IBeanDeclModel model) {
			this(model, null, null);
		}
		/**
		 * Providing an import list, will not use qualified name resolutions,
		 * but will ad a required import list.
		 * @param model
		 * @param importList
		 */
		public CodeGenExpFlattener (IBeanDeclModel model, List importList, List refList) {
			super() ;
			fmodel = model ;
			fimportList = importList;
			frefList = refList;			
		} 

		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.instantiation.ParseVisitor#visit(org.eclipse.jem.internal.instantiation.PTTypeLiteral)
		 */
		public boolean visit(PTInstanceReference node) {
			IJavaObjectInstance obj = node.getObject() ;
			if (frefList!= null && !frefList.contains(obj))
				frefList.add(obj);
		    BeanPart bp = fmodel.getABean(obj);
		    if (bp!=null){
		    	  if(bp.getReturnedMethod()!=null){
		    		  // have a return method - use it instead of the 
		    		  // field which could benot initialized at runtime
		    		  getStringBuffer().append(bp.getReturnedMethod().getMethodName()+"()") ;//$NON-NLS-1$
		    	  }else{
		    		  getStringBuffer().append(bp.getSimpleName());
		    	  }
		    }
		    else {
		    	if (obj == null || obj.eContainer()==null) {
		    		// do nothing here ... bad object to begin with
		    	}
		    	else
		    	  if (obj.isSetAllocation() && !visitedList.contains(obj)) {
		    		JavaAllocation alloc = obj.getAllocation();
		    		if (alloc instanceof InitStringAllocation)
		    			getStringBuffer().append(((InitStringAllocation) alloc).getInitString());
		    		else if ((alloc instanceof ParseTreeAllocation)) {
		    			visitedList.add(obj);
		    			((ParseTreeAllocation) alloc).getExpression().accept(this);
		    		}
		    	  } else {
		    		getStringBuffer().append("new "); //$NON-NLS-1$
		    		getStringBuffer().append(handleQualifiedName((obj.getJavaType()).getJavaName())); 
		    		getStringBuffer().append("()"); //$NON-NLS-1$
		    	  }
		    }
			return false;
		}
		
		protected String handleQualifiedName(String qName) {									
			if (fimportList!=null) {
				// Use short names instead of full quallified names
				JavaHelpers clazz = JavaRefFactory.eINSTANCE.reflectType(qName, fmodel.getCompositionModel().getModelRoot());
				// mark a dependency on an import
				if (!fimportList.contains(clazz.getJavaName()))
					fimportList.add(clazz.getJavaName());
				return clazz.getName();
			}
			else
				return qName;
		}
		
}
