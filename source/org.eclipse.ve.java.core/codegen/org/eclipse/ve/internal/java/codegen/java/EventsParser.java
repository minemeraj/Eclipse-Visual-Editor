/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventsParser.java,v $
 *  $Revision: 1.7 $  $Date: 2004-04-15 19:34:09 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.beaninfo.EventSetDecorator;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.java.codegen.java.rules.IEventProcessingRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 * @author Gili Mendel
 *  
 * This Parser would analyze the code for events that are associated with a given
 * BeanPart.  It is assumed that a BDM is already built.
 * 
 * It will use a special EventMethodVisitor and seed it with the relevent events to look for.
 */
public class EventsParser {
		
	IBeanDeclModel				fModel ;
	CompilationUnit				fastDom ;
	HashMap						faddListeners = new HashMap() ;
	MethodDeclaration 			domMethods[] ;
	JavaElementInfo				cuMethods[] ;
	
	
	public EventsParser(IBeanDeclModel m, CompilationUnit dom) {
		fModel = m;
		fastDom = dom ;
		domMethods = ((TypeDeclaration)fastDom.types().get(0)).getMethods();
		// TODO WARNING - CU maybe stale
		cuMethods = TypeVisitor.getCUMethods(domMethods, CodeGenUtil.getMethodsInfo(fModel.getCompilationUnit()), fModel);
	}
	
	private   List getAddSignitures(JavaClass h) {
		List l = (List) faddListeners.get(h) ;
		if (l == null) {
			EList events = null ;
			try {
			  events = h.getAllEvents() ;
			}
			catch (Throwable t) {
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(t) ;
			}		                
            l = new ArrayList() ;		
            if (events!= null && events.size()>0) {
		    for (Iterator iter = events.iterator(); iter.hasNext();) {
			   JavaEvent e = (JavaEvent) iter.next();
			   EventSetDecorator ed = org.eclipse.jem.internal.beaninfo.core.Utilities.getEventSetDecorator(e) ;
			   if (ed != null)
			      l.add(ed) ;
		    }
            }
		    faddListeners.put(h,l);
        }
        return l ;
	}
	
	private CodeMethodRef getMethodRef(MethodDeclaration md, JavaElementInfo im) throws JavaModelException {
		String handle = im.getHandle() ;
		CodeTypeRef tr = fModel.getTypeRef() ;
		Iterator itr = tr.getMethods() ;
		while (itr.hasNext()) {
		   CodeMethodRef m = (CodeMethodRef)itr.next() ;
		   if (handle.equals(m.getMethodHandle()))
		      return m ;
		}
		CodeMethodRef m = new CodeMethodRef(md, tr, handle, im.getSourceRange(), im.getContent());
		return m ;
	}
	
	protected void analyze(BeanPart b, JavaClass h) {
		List addSignitures = getAddSignitures(h) ;
		if (addSignitures==null || addSignitures.size()==0) return ;
		
		IEventProcessingRule rule = (IEventProcessingRule) CodeGenUtil.getEditorStyle(fModel).getRule(IEventProcessingRule.RULE_ID) ;
				
		int idx;
		for (idx = 0; idx < domMethods.length; idx++) {
			if (rule.parseForEvents(domMethods[idx], b)) {
				EventMethodVisitor v=null;
				if (b.isInitMethod(domMethods[idx])) 
				   v = new EventMethodVisitor(b, fModel, addSignitures, fastDom); 
				else {
				  try {
					MethodDeclaration md = (MethodDeclaration) domMethods[idx];
					CodeMethodRef mref = getMethodRef(md,cuMethods[idx]) ;
					v = new EventMethodVisitor(md, mref, b, fModel, addSignitures, fastDom);
				}
				catch (JavaModelException e) {}
				}
				if (v != null)
				   v.visit() ;
	        }
		}
	}
	
	public void addEvents(BeanPart b) {		
		
		CodeMethodRef m = b.getInitMethod() ;
		if (m != null) {
			String t = b.getType() ;
			JavaHelpers h = JavaRefFactory.eINSTANCE.reflectType(t,fModel.getCompositionModel().getModelResourceSet()) ;
			if (h instanceof JavaClass)			
			   analyze(b, (JavaClass)h) ;
		}
		
	}
}
