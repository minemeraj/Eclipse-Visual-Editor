package org.eclipse.ve.internal.java.codegen.util;
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
 *  $RCSfile: FreeFormAnnotationTemplate.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
/**
 * @version 	1.0
 * @author
 */


public class FreeFormAnnotationTemplate extends AbstractAnnotationTemplate {
    
    
    public final static String VISUAL_CONTENT_TYPE = "visual-constraint" ;   //$NON-NLS-1$
    public final static String ANNOTATION_START = "//" ; //$NON-NLS-1$
    
    public final static String ANNOTATION_PREFIX = "  "+ANNOTATION_START ; //$NON-NLS-1$

    /**
     * Constructor for FreeFormAnnotationTemplate.
     * @param annotationType
     */
    public FreeFormAnnotationTemplate() {
        super(VISUAL_INFO_TYPE);
    }
    
    public FreeFormAnnotationTemplate(int x, int y) {
        super(VISUAL_INFO_TYPE);
        setPosition(x,y) ;
    }
    
    public void setPosition (int x, int y) {
        fContent = VISUAL_CONTENT_TYPE+ExpressionTemplate.EQL+
                   "\""+Integer.toString(x)+","+Integer.toString(y)+"\"" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    
    protected static int getSigOnSameLine(int s, String src, String sig) {
        int start = src.indexOf(sig,s) ;
        if (start<0) return -1 ;
        int nlIndex = src.indexOf('\n') ;
        if (nlIndex >= 0 && nlIndex<=start) return -1 ;
        return start ;
    }
    protected static int getSigOnSameLine(int s, String src, char sig) {
        int start = src.indexOf(sig,s) ;
        if (start<0) return -1 ;
        int nlIndex = src.indexOf('\n') ;
        if (nlIndex >= 0 && nlIndex<=start) return -1 ;
        
        return start ;
    }
    
    public static int getAnnotationStart (String src) {
       // TODO  Need to support multi instance var. per line
       return getSigOnSameLine(0,src,ANNOTATION_START) ;
    }
    
    
    public static int getAnnotationArgStart (String src, int start) {
        try {  
       start = src.indexOf(ANNOTATION_SIG,start) ;
       if (start<0) return -1 ;
       
       int end = src.indexOf(VISUAL_INFO_TYPE,start) ;
       if (end<0) return -1 ;
       
       end = src.indexOf(VISUAL_CONTENT_TYPE,end) ;
       if (end <0) return -1 ;
       
       end = src.indexOf('"',end) ;
       if (end <0) return -1 ;
              
       return end+1 ;
      }
      catch (Throwable t) {
          return -1 ;
      }
    }
    
    public static int[] getAnnotationArgs(String src, int start) {
        int s = getAnnotationArgStart(src,start) ;
        if (start < 0) return null ;
        int sep = getSigOnSameLine(s,src,',') ;
        if (sep < 0) return null ;
        int end = getAnnotationEnd(src,start) ;
        
        int[] result = new int[2] ;
        result[0] = Integer.parseInt(src.substring(s,sep)) ;
        result[1] = Integer.parseInt(src.substring(sep+1,end)) ;
        
        return result ;
        
    }
    
    public static int getAnnotationEnd (String src, int start) {
      try {  
       
       
       int end = getAnnotationArgStart(src,start) ;
       if (end<0) return -1 ;              
       
       end = getSigOnSameLine(end,src,'"') ;
       if (end<0) return -1 ;
       return end ;
       
      }
      catch (Throwable t) {
          return -1 ;
      }
    
    }
    
    public static String getCurrentAnnotation (String src) {
       
       // TODO  Works with a single annotation on a line
       
       
       int start = getAnnotationStart(src) ;
       if (start < 0) return null ;
       int end = getAnnotationEnd(src,start) ;
       if (end<0) return null ;
                    
       return src.substring(start,end) ;
                            
       
//    	Scanner scanner = new Scanner() ;
//    	scanner.setSourceBuffer(src.toCharArray()) ;
//    	scanner.recordLineSeparator = true ;
//    	scanner.tokenizeWhiteSpace = true ;
//    	scanner.tokenizeComments = true ;
//    	
//    	int token ;
//    	String result=null ;
//    	try {
//    	 token = scanner.getNextToken() ;
//    	 while (token != scanner.TokenNameCOMMENT_LINE && token != scanner.TokenNameEOF) {
//    	       token = scanner.getNextToken() ;
//    	 }
//    	}
//    	catch (InvalidInputException e) {}
//    	return result ;
    	
    }
    
    static public  String getAnnotationPrefix() {
        return ANNOTATION_PREFIX ;
    }

}
