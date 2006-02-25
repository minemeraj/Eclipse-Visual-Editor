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
 *  $RCSfile: CodeGenExpFlattener.java,v $
 *  $Revision: 1.15 $  $Date: 2006-02-25 23:32:06 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.core.TypeResolver;
 



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
		 * @see org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener#visit(org.eclipse.jem.internal.instantiation.PTAnonymousClassDeclaration)
		 */
		public boolean visit(PTAnonymousClassDeclaration node) {
			if (fimportList != null) {
				List imports = node.getImports();
				for (Iterator importItr = imports.iterator(); importItr.hasNext();) {
					String aimport = (String) importItr.next();
					if (!fimportList.contains(aimport))
						fimportList.add(aimport);					
				}
			}
			return super.visit(node);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.instantiation.ParseVisitor#visit(org.eclipse.jem.internal.instantiation.PTTypeLiteral)
		 */
		public boolean visit(PTInstanceReference node) {
			IJavaInstance obj = node.getReference() ;
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
		    		else if (alloc.isParseTree()) {
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
				TypeResolver.FieldResolvedType ft = fmodel.getResolver().resolveWithPossibleField(qName);
				if (ft != null) {
					// Could be resolved
					if (ft.resolvedType.getIType() != null) {
						// Is not primitive
						String typeName = ft.resolvedType.getName();
						if (!fimportList.contains(typeName))
							fimportList.add(typeName);
						return ft.resolvedType.getIType().getElementName() + qName.substring(typeName.length());	// Just the short name and then everything after the name.
					}
				}
			}
			return qName;
		}
		
}
