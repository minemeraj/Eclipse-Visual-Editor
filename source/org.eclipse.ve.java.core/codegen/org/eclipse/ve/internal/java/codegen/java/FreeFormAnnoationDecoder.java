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
 *  $Revision: 1.8 $  $Date: 2004-05-17 20:28:01 $ 
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

import org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableCreationRule;
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
        boolean isModelled = false;
        if(	fBeanpart!=null && 
            fBeanpart.getEObject()!=null && 
    		fBeanpart.getEObject().eResource()!=null && 
    		fBeanpart.getEObject().eResource().getResourceSet()!=null){
            	isModelled = InstanceVariableCreationRule.isModelled(fBeanpart.getEObject().eClass(), fBeanpart.getEObject().eResource().getResourceSet());
            }else
            	isModelled = false;
        if(!isModelled || constraint!=null){
	        FreeFormAnnotationTemplate fft = getFFtemplate() ;
	        if (constraint != null) 
	        	fft.setPosition(new Point(constraint.x,constraint.y)) ;
	        else
	        	fft.setPosition(null);
	        
	        fft.setParseable(!isModelled);
	        
	        fContent = fft.toString() ;
        }else{
        	fContent = "";
        }
        return fContent ;
    }
        
    
    protected boolean decode(String src) {
         if (src== null)  {
             // No Free Form Information
         	 noAnnotationInSource();
             return true ;
          }
    	  String curAnnotation = FreeFormAnnotationTemplate.getCurrentAnnotation(src) ;
          if (curAnnotation == null)  {
             // No Free Form Information
          	 noAnnotationInSource();
             return true ;
          }
                    
          JavaVEPlugin.log(fBeanpart.getUniqueName()+" Decoding FF annotation", Level.FINE) ;    //$NON-NLS-1$
          
          int[] args = FreeFormAnnotationTemplate.getAnnotationArgs(src,0) ;
          if (args == null) {
          	// No Free Form Information
          	noAnnotationInSource();
          	return false ;
          }
          
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
            JavaVEPlugin.log(e, Level.WARNING) ;
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
              newSrc = generate(null,null) ;
              if (newSrc == null || newSrc.length() == 0) {
                JavaVEPlugin.log(fBeanpart.getUniqueName()+" No FF annotation.", Level.WARNING) ; //$NON-NLS-1$
                return ;
              }
              newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + newSrc; 
              // Just append the comment at the end of the line
              int end = FreeFormAnnotationTemplate.getEOL(src, 0);
              int pSpaces = FreeFormAnnotationTemplate.collectPrecedingSpaces(src, end);
              start = start + pSpaces;
              len = end - pSpaces;
              JavaVEPlugin.log(fBeanpart.getUniqueName()+" Creating FF annotation", Level.FINE) ;                          //$NON-NLS-1$
          }
          else {
              JavaVEPlugin.log(fBeanpart.getUniqueName()+" Updating FF annotation", Level.FINE) ;    //$NON-NLS-1$
              int s = FreeFormAnnotationTemplate.getAnnotationStart(src) ;
              s = FreeFormAnnotationTemplate.collectPrecedingSpaces(src, s);
              int end = FreeFormAnnotationTemplate.getAnnotationEnd(src,s) ;
              newSrc = generate(null,null) ;
              if(newSrc!=null && newSrc.length()>0)
              	newSrc = FreeFormAnnotationTemplate.getAnnotationPrefix() + newSrc; 
              start = start + s;
              len = end-s+1;
          }
              
          fBeanpart.getModel().getDocumentBuffer().replace(start,len,newSrc) ;
		  // update offsets
		  fBeanpart.getModel().driveExpressionChangedEvent(null, start, newSrc.length()-len) ;
		  JavaVEPlugin.log(newSrc, Level.FINE) ;
        }
        catch (Exception e) {
            JavaVEPlugin.log(e, Level.WARNING) ;
        }
     }
        
    }

}
