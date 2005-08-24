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
package org.eclipse.ve.internal.java.codegen.util;

import java.awt.Point;

import org.eclipse.jdt.core.compiler.*;

import org.eclipse.ve.internal.java.codegen.model.IScannerFactory;

/*
 *  $RCSfile: FreeFormAnnotationTemplate.java,v $
 *  $Revision: 1.13 $  $Date: 2005-08-24 23:30:46 $ 
 */
/**
 * @version 	1.0
 * @author
 */


public class FreeFormAnnotationTemplate extends AbstractAnnotationTemplate {
    
    
    public final static String VISUAL_CONTENT_TYPE = "visual-constraint" ;   //$NON-NLS-1$
    public final static String ANNOTATION_START = "//" ; //$NON-NLS-1$
    
    public final static String ANNOTATION_PREFIX = "  "+ANNOTATION_START ; //$NON-NLS-1$
    
    String positionString = ""; //$NON-NLS-1$
    String parseString = ""; //$NON-NLS-1$
    
    /**
     * Constructor for FreeFormAnnotationTemplate.
     * @param annotationType
     */
    public FreeFormAnnotationTemplate() {
        super(VISUAL_INFO_TYPE);
    }
    
    public FreeFormAnnotationTemplate(int x, int y) {
        super(VISUAL_INFO_TYPE);
        setPosition(new Point(x,y)) ;
    }
    
    public void setPosition (Point point) {
    	if(point==null)
    		positionString = VISUAL_CONTENT_TYPE+ExpressionTemplate.EQL+"\"\""; //$NON-NLS-1$
    	else
    		positionString = VISUAL_CONTENT_TYPE+ExpressionTemplate.EQL+
                   "\""+Integer.toString(point.x)+","+Integer.toString(point.y)+"\"" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
       
       // consider the case where decl-index=x is removed, but the visual-constraint 
       // is still present and valid (ex. JButton delcaration)
//       int end = src.indexOf(VISUAL_INFO_TYPE,start) ;
//       if (end<0) return -1 ;
       
       int end = src.indexOf(VISUAL_CONTENT_TYPE,start) ;
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
    protected static int getCommentLineEnd(String src, int start, IScannerFactory scannerFactory){
    	int endOfCommentLine = -1;
        try {
        	int jveAnnotationStart = getSigOnSameLine(start, src, ANNOTATION_SIG); 
			if(jveAnnotationStart>-1){ // If JVE annotation
				IScanner scanner = scannerFactory.getScanner(true, true, true);
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
    public static int getAnnotationEnd (String src, int start, IScannerFactory scannerFactory) {
    	try {
    		int endOfCommentLine = getCommentLineEnd(src, start, scannerFactory);
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
    public static String getCurrentAnnotation (String src, IScannerFactory scannerFactory) {
       
       // TODO  Works with a single annotation on a line
       
       
       int start = getAnnotationStart(src) ;
       if (start < 0) return null ;
       int end = getAnnotationEnd(src,start, scannerFactory) ;
       if (end<0) end = src.lastIndexOf(ANNOTATION_SEPERATOR); 
       if (end<0)
       	   return null ;
                    
       return src.substring(start,end+1) ;
    }
    
    static public  String getAnnotationPrefix() {
        return ANNOTATION_PREFIX ;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractAnnotationTemplate#determineContent()
	 */
	protected String determineContent() {
		String content = parseString;
		if(content!=null && content.length()>0 && positionString!=null && positionString.length()>0)
			content = content + "," ; //$NON-NLS-1$
		content = content + positionString;
		return content;
	}
}
