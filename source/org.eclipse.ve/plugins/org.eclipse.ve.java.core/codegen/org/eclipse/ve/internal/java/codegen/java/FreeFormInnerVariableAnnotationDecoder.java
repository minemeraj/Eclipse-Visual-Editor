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
 *  $RCSfile: FreeFormInnerVariableAnnotationDecoder.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.Iterator;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;
import org.eclipse.ve.internal.java.codegen.util.*;
 

/**
 * 
 * @since 1.0.0
 */
public class FreeFormInnerVariableAnnotationDecoder extends FreeFormAnnoationDecoder {

	/**
	 * @param bean
	 * 
	 * @since 1.0.0
	 */
	public FreeFormInnerVariableAnnotationDecoder(BeanPart bean) {
		super(bean);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IAnnotationDecoder#decode()
	 */
	public boolean decode() throws CodeGenException {
		if(fBeanpart==null)
			return false;
		for (Iterator expItr = fBeanpart.getRefExpressions().iterator(); expItr.hasNext();) {
			CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
			if(exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
				String comment = exp.getContentParser().getComment();
				return decode(comment);
			}
			break;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IAnnotationDecoder#reflectMOFchange()
	 */
	public void reflectMOFchange() {
		if(fBeanpart==null)
			return;
//		for (Iterator expItr = fBeanpart.getRefExpressions().iterator(); expItr.hasNext();) {
//			CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
//			if(exp.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
//				try {
//					String comment = exp.getContentParser().getComment();
//					String curAnnotation = FreeFormAnnotationTemplate.getCurrentAnnotation(comment) ;
//					int start = exp.getMethod().getOffset() + exp.getContentParser().getCommentOff();
//					int len = 0;
//					String newSource = null;
//					if(curAnnotation==null){
//						// A new one is needed
//						newSource = FreeFormAnnotationTemplate.getAnnotationPrefix() + generate(null,null) ;
//						if (newSource== null || newSource.length() == 0) {
//						 	JavaVEPlugin.log(fBeanpart.getUniqueName()+" No FF annotation.", Level.WARNING) ; //$NON-NLS-1$
//						 	return ;
//						 }
//						 len = 0;
//						 JavaVEPlugin.log(fBeanpart.getUniqueName()+" Creating FF annotation", Level.FINE) ; //$NON-NLS-1$
//					}else{
//						// Change the old one
//						int annotationStart = FreeFormAnnotationTemplate.getAnnotationStart(comment);
//						start = start + annotationStart;
//						len = FreeFormAnnotationTemplate.getAnnotationEnd(comment, annotationStart) - annotationStart +1;
//						newSource = FreeFormAnnotationTemplate.getAnnotationPrefix() + generate(null,null) ;
//					}
//					fBeanpart.getModel().getDocumentBuffer().replace(start,len,newSource) ;
//					// update offsets
//					fBeanpart.getModel().driveExpressionChangedEvent(null, start, newSource.length()-len) ;
//					JavaVEPlugin.log(newSource, Level.FINE) ;
//					
//					// Now update the expression's content parser
//					String methodContent = fBeanpart.getModel().getDocumentBuffer().getText(exp.getMethod().getOffset(), exp.getMethod().getLen());
//					ExpressionParser newParser = new ExpressionParser(methodContent, exp.getContentParser().getCodeOff(), exp.getContentParser().getCodeLen());
//					exp.setContent(newParser);
//				} catch (Throwable e1) {
//					JavaVEPlugin.log(e1, Level.WARNING);
//				}
//				break;
//			}
//		}
	}
}
