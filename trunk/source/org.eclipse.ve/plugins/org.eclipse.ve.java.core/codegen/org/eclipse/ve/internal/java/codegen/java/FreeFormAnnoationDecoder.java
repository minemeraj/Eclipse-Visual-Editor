package org.eclipse.ve.internal.java.codegen.java;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FreeFormAnnoationDecoder.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-21 00:00:24 $ 
 */
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.IField;

import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.cdm.CDMFactory;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.impl.KeyedConstraintImpl;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;
import org.eclipse.ve.internal.cdm.model.Rectangle;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @version 	1.0
 * @author
 */
public class FreeFormAnnoationDecoder extends AbstractAnnotationDecoder {

     FreeFormAnnotationTemplate fFFtemplate = null ;        

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
    
    public String generate(EStructuralFeature sf, Object[] args) throws CodeGenException {
        Rectangle constraint = (Rectangle) getAnnotationValue() ;
        if (constraint == null) {
            fContent = "" ;             //$NON-NLS-1$
        }
        else {
          FreeFormAnnotationTemplate fft = getFFtemplate() ;
          fft.setPosition(constraint.x,constraint.y) ;
          fContent = fft.toString() ;
        }
        return fContent ;
    }
    
    protected boolean decode(String src) {
         if (src== null)  {
             // No Free Form Information
             return true ;
          }
    	  String curAnnotation = FreeFormAnnotationTemplate.getCurrentAnnotation(src) ;
          if (curAnnotation == null)  {
             // No Free Form Information
             return true ;
          }
                    
          JavaVEPlugin.log(fBeanpart.getUniqueName()+" Decoding FF annotation", MsgLogger.LOG_FINE) ;    //$NON-NLS-1$
          
          int[] args = FreeFormAnnotationTemplate.getAnnotationArgs(src,0) ;
          if (args == null) return false ;
          
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

      synchronized (fBeanpart.getModel().getDocumentLock()) {
        IField f = CodeGenUtil.getFieldByName(fBeanpart.getSimpleName(),
                                              fBeanpart.getModel().getCompilationUnit()) ;
        
        if (f == null) return false ;
        
        fBeanpart.setFieldDeclHandle(f.getHandleIdentifier()) ;
        try {
         
          ExpressionParser p = new ExpressionParser(f);
          String src = p.getComment();
          return decode(src) ;                    
        }
        catch (Exception e) {
            JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
            return false ;
        }
     }     
    }
    
    
    public void reflectMOFchange() {
               
     synchronized (fBeanpart.getModel().getDocumentLock()) {
        IField f = CodeGenUtil.getField(fBeanpart.getFieldDeclHandle(),
                                        fBeanpart.getModel().getCompilationUnit()) ;
        if (f==null) return ;                                        
        
        try {
          ExpressionParser p = new ExpressionParser(f);
          int    srcStart = p.getCodeOff();
          int    srcLen   = p.getCodeLen();
          String src = fBeanpart.getModel().getDocumentBuffer().getContents().substring(srcStart+srcLen+1) ;
          String newSrc = null ;
          int start, len ;
          
          // We want to keep start withing the range of the def., so that the JModel will pick up the comment
          start = srcStart+srcLen+1 ; // ;'s are not part of the <CodeOff, CodeOff+CodeLen>
          String curAnnotation = FreeFormAnnotationTemplate.getCurrentAnnotation(src) ;
          if (curAnnotation == null) {
            // Brand New Anotation 
              newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + generate(null,null) ;
              if (newSrc == null || newSrc.length() == 0) {
                JavaVEPlugin.log(fBeanpart.getUniqueName()+" No FF annotation.", MsgLogger.LOG_WARNING) ; //$NON-NLS-1$
                return ;
              }
              
              
              int commentStart = FreeFormAnnotationTemplate.getAnnotationStart(src) ;
              if (commentStart <0)
                 len = 0 ;                    
              else
                 len = commentStart + FreeFormAnnotationTemplate.ANNOTATION_START.length() ;                 
              
              JavaVEPlugin.log(fBeanpart.getUniqueName()+" Creating FF annotation", MsgLogger.LOG_FINE) ;                          //$NON-NLS-1$
          }
          else {
              JavaVEPlugin.log(fBeanpart.getUniqueName()+" Updating FF annotation", MsgLogger.LOG_FINE) ;    //$NON-NLS-1$
              int s = FreeFormAnnotationTemplate.getAnnotationStart(src) ;
              int end = FreeFormAnnotationTemplate.getAnnotationEnd(src,s) ;
              if(getAnnotationValue()==null){
	              	int realEnd = src.indexOf('\n', end);
	              	if(realEnd<0)
	              		realEnd = src.indexOf('\r', end);
	              	if(realEnd<0)
	              		realEnd = src.length();
	              	int realStart = src.indexOf(FreeFormAnnotationTemplate.ANNOTATION_SIG, s);
	              	String nonAnnotationComment = new String();
	              	if(realStart > -1 && realStart>s+FreeFormAnnotationTemplate.ANNOTATION_START.length())
	              		nonAnnotationComment = nonAnnotationComment+src.substring(s+FreeFormAnnotationTemplate.ANNOTATION_START.length(), realStart);
	              	if(realEnd > end)
	              		nonAnnotationComment = nonAnnotationComment + src.substring(end+1, realEnd);
	              	boolean anyThingInteresting = false;
	              	for(int i=0;nonAnnotationComment!=null && i<nonAnnotationComment.length();i++)
	              		if(!Character.isWhitespace(nonAnnotationComment.charAt(i)))
	              			anyThingInteresting = true;
	              	if(anyThingInteresting){
	              		newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + nonAnnotationComment;
	              	}else{
	              		newSrc = new String();
	              	}
              }else{
				newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + generate(null,null) ;
              }
              len = end+1 ;                
          }
              

          fBeanpart.getModel().getDocumentBuffer().replace(start,len,newSrc) ;
		  // update offsets
		  fBeanpart.getModel().driveExpressionChangedEvent(null, start, newSrc.length()-len) ;
		  JavaVEPlugin.log(newSrc, MsgLogger.LOG_FINE) ;
        }
        catch (Exception e) {
            JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
        }
     }
        
    }

}
