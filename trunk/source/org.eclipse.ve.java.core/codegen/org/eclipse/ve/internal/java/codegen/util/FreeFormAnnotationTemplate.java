package org.eclipse.ve.internal.java.codegen.util;

import java.util.logging.Level;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

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
 *  $Revision: 1.2 $  $Date: 2004-05-14 19:53:17 $ 
 */
/**
 * @version 	1.0
 * @author
 */


public class FreeFormAnnotationTemplate extends AbstractAnnotationTemplate {
    
    
    public final static String VISUAL_CONTENT_TYPE = "visual-constraint" ;   //$NON-NLS-1$
    public final static String ANNOTATION_START = "//" ; //$NON-NLS-1$
    
    public final static String ANNOTATION_PREFIX = "  "+ANNOTATION_START ; //$NON-NLS-1$
    final public static String VISUAL_PARSE = "parse"; //$NON-NLS-1$
    
    String positionString = "";
    String parseString = "";
    
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
    	if(x==Integer.MIN_VALUE && y==Integer.MIN_VALUE)
    		positionString = "";
    	else
    		positionString = VISUAL_CONTENT_TYPE+ExpressionTemplate.EQL+
                   "\""+Integer.toString(x)+","+Integer.toString(y)+"\"" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    public void setParseable(boolean parse){
    	if(parse)
    		parseString = VISUAL_PARSE;
    	else
    		parseString = "";
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
    
    /**
     * Returns the index of the comment which contains the @jve: inside it.
     * Does not return any other comment found in the source. Returns the 
     * exact start of the //
     * 
     * @param src
     * @return
     */
    public static int getAnnotationStart (String src) {
    	// TODO Need to support multi instance var. per line
    	int commentStart = getSigOnSameLine(0,src,ANNOTATION_START) ;
    	if(commentStart<0)
    		return -1;
    	int annotationStart = getSigOnSameLine(commentStart, src, ANNOTATION_SIG);
    	if(annotationStart<0)
    		return -1;
    	int lastCommentStart = src.lastIndexOf(ANNOTATION_START, annotationStart);
    	return lastCommentStart;
    }
    
    /**
     * Collects all preceding spaces and tabs and returns the index
     * @param src
     * @param from
     * @return
     * 
     * @since 1.0.0
     */
    public static int collectPrecedingSpaces(String src, int from){
		if(from>0){
    		// take in any preceeding white spaces - else annotation slowly keeps moving right.
    		char oneCharLeft = src.charAt(from-1);
        	while(oneCharLeft==' ' || oneCharLeft=='\t'){
        		if(--from > 0)
        			oneCharLeft = src.charAt(from - 1);
        		else
        			break;
        	}
    	}
		return from;
    }
    
    /**
     * Returns the EOL from the offset given. If no EOL was found, the offset is returned back 
     * 
     * @param src
     * @param offset
     * @return
     * 
     * @since 1.0.0
     */
    public static int getEOL(String src, int offset){
    	if(src==null || offset < 0 || offset>src.length())
    		return offset;
    	int eol1 = src.indexOf('\r', offset);
    	int eol2 = src.indexOf('\n', offset);
    	if(eol1==-1 && eol2==-1)
    		return offset;
    	if(eol1==-1)
    		return eol2;
    	if(eol2==-1)
    		return eol1;
    	return Math.min(eol1, eol2);
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
        if (s < 0) return null; // If no args start was found, return null
        if (start < 0) return null ;
        int sep = getSigOnSameLine(s,src,',') ;
        if (sep < 0) return null ;
        int end = src.indexOf('"', sep);
        
        int[] result = new int[2] ;
        result[0] = Integer.parseInt(src.substring(s,sep)) ;
        result[1] = Integer.parseInt(src.substring(sep+1,end)) ;
        
        return result ;
        
    }
    
    /**
     * Returns the end of the passed in // ... comment. It will end where either
     * a new comment is found, the passed in source ends, or where a line break
     * occurs. This will work only if @jve: is found in the comment start 
     *  
     * @param src
     * @param start
     * @return
     * 
     * @since 1.0.0
     */
    protected static int getCommentLineEnd(String src, int start){
    	int endOfCommentLine = -1;
        try {
        	int jveAnnotationStart = getSigOnSameLine(start, src, ANNOTATION_SIG); 
			if(jveAnnotationStart>-1){ // If JVE annotation
				IScanner scanner = ToolFactory.createScanner(true, true, false, true);
				scanner.setSource(src.substring(jveAnnotationStart).toCharArray());
				int token = scanner.getNextToken();
				boolean endFound = false;
				while(!endFound && 
						token!=ITerminalSymbols.TokenNameEOF && 
						token!=ITerminalSymbols.TokenNameCOMMENT_LINE &&
						token!=ITerminalSymbols.TokenNameCOMMENT_BLOCK &&
						token!=ITerminalSymbols.TokenNameCOMMENT_JAVADOC){
					if(token==ITerminalSymbols.TokenNameWHITESPACE){
						// check to see if newline
						char[] whitespaceChars = scanner.getCurrentTokenSource();
						for (int wc = 0; wc < whitespaceChars.length; wc++) {
							if(whitespaceChars[wc]=='\r' || whitespaceChars[wc]=='\n'){
								endFound = true;
								break;
							}
						}
					}
					if(!endFound)
						token = scanner.getNextToken();
				}
				if(token==ITerminalSymbols.TokenNameWHITESPACE){
					// If whitespace, search for the first \r\n
					char[] whitespaceChars = scanner.getCurrentTokenSource();
					for (int wc = 0; wc < whitespaceChars.length; wc++) {
						if(whitespaceChars[wc]=='\r' || whitespaceChars[wc]=='\n'){
							endOfCommentLine = wc + scanner.getCurrentTokenStartPosition() + jveAnnotationStart-1; // Use -1 to give the EXACT position
							break;
						}
					}
				}else{
					endOfCommentLine = scanner.getCurrentTokenStartPosition()+jveAnnotationStart-1; // Use -1 to give the EXACT position
				}
			}
		} catch (InvalidInputException e) {
			endOfCommentLine = -1;
		}
        return endOfCommentLine;
    }
    
    /**
     * Returns the EXACT index of the end. This does not return in the normal
     * end+1 fashion which things like substring(begin, end) expect. The end is 
     * either the end of the line, or the beginning of a new comment.
     *  
     * @param src
     * @param start
     * @return the EXACT index of the end of the annotation
     */
    public static int getAnnotationEnd (String src, int start) {
    	try {
    		int endOfCommentLine = getCommentLineEnd(src, start);
    		return endOfCommentLine ;
    	}catch (Throwable t) {
    		return -1 ;
    	}
    }
    
    /**
     * Returns the comment which has the // @jve: ... in it. Its range is 
     * what is returned by getAnnotationStart(String) and getAnnotationEnd(String, int)
     * It does not return other comments if present. 
     * @see  getAnnotationStart(String)
     * @see getAnnotationEnd(String, int)
     * @param src
     * @return
     */
    public static String getCurrentAnnotation (String src) {
       
       // TODO  Works with a single annotation on a line
       
       
       int start = getAnnotationStart(src) ;
       if (start < 0) return null ;
       int end = getAnnotationEnd(src,start) ;
       if (end<0) return null ;
                    
       return src.substring(start,end+1) ;
                            
       
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

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractAnnotationTemplate#determineContent()
	 */
	protected String determineContent() {
		return parseString + ExpressionTemplate.SPACE + positionString;
	}
}
