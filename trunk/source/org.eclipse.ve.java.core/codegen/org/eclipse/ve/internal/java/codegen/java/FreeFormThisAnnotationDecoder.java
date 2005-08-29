/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: FreeFormThisAnnotationDecoder.java,v $
 *  $Revision: 1.14 $  $Date: 2005-08-29 18:47:06 $ 
 */

import java.util.logging.Level;

import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FreeFormThisAnnotationDecoder extends FreeFormAnnoationDecoder {

	/**
	 * Constructor for FreeFormThisAnnotationDecoder.
	 * @param bean
	 */
	public FreeFormThisAnnotationDecoder(BeanPart bean) {
		super(bean);
	}

	/**
	 * Ties to get the area between the main class' decleration and the end of line.
	 * e.g.,  public myClass extends Foo {   // lskjfdlsjfd 
	 *                                    ^                  ^
	 */	
	public static ICodeGenSourceRange getDesignatedAnnotationRange (ICompilationUnit cu) {
		
/******  The following code used to put the annotation just after the extend,
 *       the problem is that the Java Model think it is part of the next method if
 *       there are not fields in it
 * 		
		    String st ;		    
            int start, len ;
			try {
				ISourceRange nameSR = CodeGenUtil.getMainType(cu).getNameRange() ;
				start = nameSR.getOffset() + nameSR.getLength() ;
				st = cu.getSource().substring(start);				
				
			} catch (JavaModelException e) {
				return null ;
			}
		    org.eclipse.ve.internal.cde.core.CDEHack.fixMe("Should consider syntax error") ;
		    // May not be on the same line
			int braceIndex = st.indexOf('{') ;
			if (braceIndex<0) return null ;
			
			len = 0 ;			
			st = st.substring(braceIndex+1) ;
			
			
			// Terminate it at the EOL
			int endIndex = st.indexOf('\r') ;
			if (endIndex<0)
			   endIndex = st.indexOf('\n') ;
		    if (endIndex<0) return null ;
		    len+=endIndex ;
		    
		    CodeGenSourceRange SR = new CodeGenSourceRange(start+braceIndex+1,len) ;
		    return SR ;
 ******
 *         For now, put it at the end of the class
 */	

		    String st ;		    
            int start, len ;
			try {
				IType t = CodeGenUtil.getMainType(cu) ;
				if(t==null)
					return null;
				ISourceRange range = t.getSourceRange() ;
				String typeSource = null;
				if(range.getOffset()+range.getLength()>cu.getSource().length())
					// TODO  Defect where length of type exceeds length of the CU ! 
					// This happens when there is no new line at the end of the type decl in the CU.
					typeSource = cu.getSource().substring(range.getOffset(), cu.getSource().length());
				else
					// Normal way
					typeSource = t.getSource();
				start = typeSource.lastIndexOf('}') ;  	// JCMMethod offset
				if (start<0) return null ;
				
				start += range.getOffset() ;  				// CU offset
				st = cu.getSource().substring(start);  
				
			} catch (JavaModelException e) {
				return null ;
			}
		    // TODO  Should consider syntax error
		    // May not be on the same line
			
			len = 0 ;			
			
			// Terminate it at the EOL
			int endIndex = st.indexOf('\r') ;
			if (endIndex<0)
			   endIndex = st.indexOf('\n') ;
			if (endIndex<0)
                endIndex=st.length() ;
            
		    len+=endIndex-1 ;
		    
		    CodeGenSourceRange SR = new CodeGenSourceRange(start+1,len) ;
		    return SR ;	    
		    	
	}
	public static String getDesignatedAnnotationString (ICompilationUnit cu) {
		ICodeGenSourceRange sr = getDesignatedAnnotationRange(cu) ;
		if (sr == null) return null ;
		try {			
			return cu.getSource().substring(sr.getOffset(),sr.getOffset()+sr.getLength()) ;
		} catch (JavaModelException e) {
			return null ;
		}
	}
	
	public boolean decode() throws CodeGenException {		
		ICompilationUnit cu = fBeanpart.getModel().getCompilationUnit() ;
		// Main Type decleration name offset
		String src = getDesignatedAnnotationString(cu);
		if (src==null) return false ;
		fBeanpart.setFieldDeclHandle(BeanPart.THIS_HANDLE) ;
	    return decode(src) ;		
	}


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IAnnotationDecoder#reflectMOFchange()
	 */
	public void reflectMOFchange() {

		try {
			String src = getDesignatedAnnotationString(fBeanpart.getModel().getCompilationUnit());
			ICodeGenSourceRange curSR = getDesignatedAnnotationRange(fBeanpart.getModel().getCompilationUnit());
			if (src == null || curSR == null) {
				JavaVEPlugin.log("FFThisAnnotationDecoder.reflectMOFchange(): Could not insert THIS FF annotation", Level.FINE); //$NON-NLS-1$
				return;
			}
			String newSrc = null;
			int len;

			String curAnnotation = FreeFormAnnotationTemplate.getCurrentAnnotation(src, fBeanpart.getModel().getScannerFactory());

			if (curAnnotation == null) {
				// Brand New Anotation
				newSrc = generate(null, null);
				if (newSrc == null || newSrc.length() == 0) {
					if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
						JavaVEPlugin.log(fBeanpart.getUniqueName() + " No FF annotation.", Level.WARNING); //$NON-NLS-1$
					return;
				}
				newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + newSrc;

				int commentStart = FreeFormAnnotationTemplate.getAnnotationStart(src);
				commentStart = FreeFormAnnotationTemplate.collectPrecedingSpaces(src, commentStart);
				if (commentStart < 0)
					len = 0;
				else
					len = commentStart + FreeFormAnnotationTemplate.ANNOTATION_START.length();

				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log(fBeanpart.getUniqueName() + " Creating FF annotation", Level.FINE); //$NON-NLS-1$
			} else {
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log(fBeanpart.getUniqueName() + " Updating FF annotation", Level.FINE); //$NON-NLS-1$
				newSrc = generate(null, null);
				if (newSrc != null && newSrc.length() > 0)
					newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + newSrc;
				int s = FreeFormAnnotationTemplate.getAnnotationStart(src);
				s = FreeFormAnnotationTemplate.collectPrecedingSpaces(src, s);
				int end = FreeFormAnnotationTemplate.getAnnotationEnd(src, s, fBeanpart.getModel().getScannerFactory());
				len = end + 1;
			}

			fBeanpart.getModel().getDocumentBuffer().replace(curSR.getOffset(), len, newSrc);
			// update offsets
			fBeanpart.getModel().driveExpressionChangedEvent(null, curSR.getOffset(), newSrc.length() - len);

			JavaVEPlugin.log(newSrc, Level.FINE);
		} catch (Exception e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
	}

}


