/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: FreeFormAnnoationDecoder.java,v $
 *  $Revision: 1.25 $  $Date: 2005-07-10 15:00:59 $ 
 */
import java.awt.Point;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.IField;

import org.eclipse.ve.internal.cdm.CDMFactory;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.impl.KeyedConstraintImpl;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;
import org.eclipse.ve.internal.cdm.model.Rectangle;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @version 	1.0
 * @author
 */
public class FreeFormAnnoationDecoder extends AbstractAnnotationDecoder {

     FreeFormAnnotationTemplate fFFtemplate = null ;
     protected boolean visual = true;

    /**
     * Constructor for FreeFormAnnoationDecoder.
     */
    public FreeFormAnnoationDecoder(BeanPart bean) {
        super(CDMModelConstants.VISUAL_CONSTRAINT_KEY,bean);
    }
    
    FreeFormAnnotationTemplate getFFtemplate() {
        if (fFFtemplate!=null) return fFFtemplate ;
        
        fFFtemplate = new FreeFormAnnotationTemplate() ;          
        fFFtemplate.setSeperator(fBeanpart.getModel().getLineSeperator()) ;    
        
        return fFFtemplate ;
        
    }
    
    public boolean isVisualOnFreeform(){
		String comment = null;

		IField f = CodeGenUtil.getFieldByName(fBeanpart.getSimpleName(), fBeanpart.getModel().getCompilationUnit()) ;
		if (f != null){
	        ExpressionParser p = new ExpressionParser(f, fBeanpart.getModel());
	        comment = p.getComment();
		}else{
			// No field, check comment on init expression
			CodeExpressionRef exp = fBeanpart.getInitExpression();
			if(exp!=null && !exp.isStateSet(CodeExpressionRef.STATE_NO_SRC)){
				comment = exp.getCommentsContent();
			}
		}
		
        return AnnotationDecoderAdapter.isBeanVisible(comment);          
    }
    
    public String generate(EStructuralFeature sf, Object[] args) throws CodeGenException {
		Object constraint =  getAnnotationValue() ;
		if(constraint!=null){
			FreeFormAnnotationTemplate fft = getFFtemplate() ;
			if (constraint instanceof Rectangle) {
				Rectangle r = (Rectangle) constraint;
				fft.setPosition(new Point(r.x,r.y)) ;
			}else if (constraint instanceof Boolean) {
				// Free Form or not
				if (!((Boolean)constraint).booleanValue())
					fft.setPosition(null);
			}
			fContent = fft.toString() ;
        }else{
        	fContent = ""; //$NON-NLS-1$
        }
        return fContent ;
    }
        
    
    protected boolean decode(String src) {
         if (src== null)  {
             // No Free Form Information
         	 return noAnnotationInSource(false);
          }
    	  String curAnnotation = FreeFormAnnotationTemplate.getCurrentAnnotation(src, fBeanpart.getModel()) ;
          if (curAnnotation == null)  {
             // No Free Form Information
          	 return noAnnotationInSource(false);
          }
                    
          if (JavaVEPlugin.isLoggingLevel(Level.FINE))
          	JavaVEPlugin.log(fBeanpart.getUniqueName()+" Decoding FF annotation", Level.FINE) ;    //$NON-NLS-1$
          
          int[] args = FreeFormAnnotationTemplate.getAnnotationArgs(src,0) ;
          if (args == null) {
          	// no location found in the source
          	
          	// is it because there is no VISUAL_CONTENT_TYPE	[hide from FF]
          	// OR is it because there are no "x,y" mentioned 				[free-float on FF]
          	if(src.indexOf(FreeFormAnnotationTemplate.VISUAL_CONTENT_TYPE)>-1){
          		return noAnnotationInSource(true); // free-float on FF
          	}else{
          		return noAnnotationInSource(false); // hide from FF
          	}
          }
          
          try {
			Object curVal = getAnnotationValue();
			if (curVal instanceof Rectangle) {
				Rectangle rVal = (Rectangle) curVal;
				if (args[0] == rVal.x && args[1] == rVal.y)
					return true;
			}
		  } catch (CodeGenException e1) {}
          
	      KeyedConstraintImpl c = (KeyedConstraintImpl) CDMFactory.eINSTANCE.create(CDMPackage.eINSTANCE.getKeyedConstraint());
	      c.setValue(new Rectangle(args[0],args[1],-1,-1)) ;
	      try {
	      	setAnnotationValue (c) ;
	      }
	      catch (Exception e) {
			return false ;
		  }
	      return true ;    	    	
    }
    
    /*
     * @see IAnnotationDecoder#decode()
     */
    public boolean decode() throws CodeGenException {
      
        IField f = CodeGenUtil.getFieldByName(fBeanpart.getSimpleName(),
                                              fBeanpart.getModel().getCompilationUnit()) ;
        
        if (f == null) return false ;
        
        fBeanpart.setFieldDeclHandle(f.getHandleIdentifier()) ;
        try {
         
          ExpressionParser p = new ExpressionParser(f, fBeanpart.getModel());
          String src = p.getComment();
          return decode(src) ;                    
        }
        catch (Exception e) {
            JavaVEPlugin.log(e, Level.WARNING) ;
            return false ;
        }
    }
    
	public boolean restore() throws CodeGenException {
		return true;
	}
    
    
    public void reflectMOFchange() {
		
		IField f = CodeGenUtil.getField(fBeanpart.getFieldDeclHandle(), fBeanpart.getModel().getCompilationUnit());
		if (f == null)
			return;

		try {
			ExpressionParser p = new ExpressionParser(f, fBeanpart.getModel());
			int srcStart = p.getCodeOff();
			int srcLen = p.getCodeLen();
			String src = fBeanpart.getModel().getDocumentBuffer().getContents().substring(srcStart + srcLen + 1);
			String newSrc = null;
			int start, len;

			// We want to keep start withing the range of the def., so that the JModel will pick up the comment
			start = srcStart + srcLen + 1; // ;'s are not part of the <CodeOff, CodeOff+CodeLen>
			String curAnnotation = FreeFormAnnotationTemplate.getCurrentAnnotation(src, fBeanpart.getModel());
			if (curAnnotation == null) {
				// Brand New Anotation 
				newSrc = generate(null, null);
				if (newSrc == null || newSrc.length() == 0) {
					return;
				}
				newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + newSrc;
				// Just append the comment at the end of the line
				int end = FreeFormAnnotationTemplate.getEOL(src, 0);
				int pSpaces = FreeFormAnnotationTemplate.collectPrecedingSpaces(src, end);
				start = start + pSpaces;
				len = end - pSpaces;
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log(fBeanpart.getUniqueName() + " Creating FF annotation", Level.FINE); //$NON-NLS-1$
			} else {
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log(fBeanpart.getUniqueName() + " Updating FF annotation", Level.FINE); //$NON-NLS-1$
				int s = FreeFormAnnotationTemplate.getAnnotationStart(src);
				s = FreeFormAnnotationTemplate.collectPrecedingSpaces(src, s);
				int end = FreeFormAnnotationTemplate.getAnnotationEnd(src, s, fBeanpart.getModel());
				newSrc = generate(null, null);
				if (newSrc != null && newSrc.length() > 0)
					newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + newSrc;
				start = start + s;
				len = end - s + 1;
			}

			fBeanpart.getModel().getDocumentBuffer().replace(start, len, newSrc);
			// update offsets
			fBeanpart.getModel().driveExpressionChangedEvent(null, start, newSrc.length() - len);
			JavaVEPlugin.log(newSrc, Level.FINE);
		} catch (Exception e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
	}
}
