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
 *  $RCSfile: ExpressionParser.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-20 00:44:29 $ 
 */

import java.util.logging.Level;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * ExpresionParser handles all the needs of parsing an expression. 
 * Expressions according to the ExpressionParser are broken into
 * three parts:
 *  (1) Filler
 *  (2) Code
 *  (3) Comment
 * 
 * Filler:			Region from a NEWLINE to the first non-WHITESPACE of the Code area.
 * Code:			Region specified by the user as Code Area. Strictly this is from the first 
 * 					non-WHITESPACE after Filler to the character before the SEMICOLON. 
 * 					It does not include the SEMICOLON.
 * Comment:	Region from the start of a COMMENT_LINE, COMMENT_BLOCK, COMMENT_JAVADOC
 * 					to the end of them *excluding* NEWLINEs at the end.
 * 
 * Example:
 * >     <>public static final String copyright = "(c) Copyright IBM Corporation 2002."  <;    >//$NON-NLS-1$<
 */
public class ExpressionParser {
	
	String fSource ;
	
	int    fSourceOff ;   // not including a filler, or comments
	int    fSourceLen ;
	
	int    fExpOff = -1 ; // The works
	int    fExpLen = -1 ;
	
	int    fCommentsOff = -1 ; 
	int    fCommentsLen = -1 ;
	
	int    fFillerOff = -1 ; // Prefixed  white space
	int    fFillerLen = -1 ;
	
	
	
/**
 *  @param sourceSnippet the source code body where the expression is part off
 *  @param expOffset  where the expression java source starts (    >setFoo() <;)
 *  @param expLen     the length of the source .
 */	
public ExpressionParser(String sourceSnippet, int expOffset, int expLen) {
		fSource = sourceSnippet ;
		fSourceOff = expOffset ;
		fSourceLen = expLen ;
}

/**
 * Expression sources now contain the comments inside of them.
 */
public ExpressionParser(IField field){
	try{
		fSource = field.getCompilationUnit().getSource();
		fSourceOff = field.getSourceRange().getOffset();
		fSourceLen = indexOfSemiColon(field.getSource());
	}catch(JavaModelException e){
		JavaVEPlugin.log(e, Level.WARNING);
	}
}

protected void clear() {
	fFillerOff = fFillerLen = fCommentsOff = fCommentsLen = fExpOff = fExpLen = -1 ;
}


public void replaceCode(String code) {
	StringBuffer st = new StringBuffer (fSource) ;
	st.replace(getCodeOff(),getCodeOff()+getCodeLen(),code) ;
	fSource=st.toString() ;
	fSourceLen = code.length() ;
	clear() ;
}

public void replaceComments(String comment) {
	StringBuffer st = new StringBuffer (fSource) ;
	st.replace(getCommentOff(),getCommentOff()+getCommentLen(),comment) ;
	fSource=st.toString() ;
	clear() ;
}

public void replaceFiller (String filler) {
	int delta = filler.length() - getFillerLen() ;
	StringBuffer st = new StringBuffer (fSource) ;
	st.replace(getFillerOff(),getFillerOff()+getFillerLen(),filler) ;	
	fSource = st.toString() ;
	fSourceOff+= delta ;
    clear() ;
}

/**
 * Code is the area defined by the parameters in the constructor.
 */
public String getCode() {
	return fSource.substring(fSourceOff,fSourceOff+fSourceLen) ;
}

public int getCodeOff() {
	return fSourceOff ;
}

public int getCodeLen() {
	return fSourceLen ;
}

protected int getLineStartOff() {
	int left = fSourceOff ;
	// Include the filler on the left (spaces, tabs
	while ((left-1)>=0) {
		char ch = fSource.charAt(left-1) ;
		if (Character.isWhitespace(ch) && ch != '\r' && ch != '\n') {
			left -- ;
			continue ;
		} 
		break ;
	}
	if (left == 0 || fSource.charAt(left-1) == '\r' || fSource.charAt(left-1) == '\n')
	  // First exp. on the line
 	  return left ;	
    else
      // The filler belongs to the previous expression
      return fSourceOff ;
}	


protected int advanceLineSeperator(int position) {
	int index = position ;
	while (index<fSource.length() && 
	   	   (fSource.charAt(index) == '\r' || fSource.charAt(index) == '\n'))
	   	    index ++ ;	
	return index ;
}

protected int reverseLineSeperator(int position) {
	int index = position-1 ;
	while (index>0&& 
	   	   (fSource.charAt(index) == '\r' || fSource.charAt(index) == '\n'))
	   	    index -- ;	
	return index+1 ;
}

/**
 *  @return String represending the second Identifier e.g., ivjFoo.setBar()
 *                                                                 ^    ^
 */
public String getSelectorContent() {
	String code = getCode() ;
	Scanner scanner = new Scanner() ;
	scanner.setSource(code.toCharArray()) ;
	scanner.recordLineSeparator = true ;
	scanner.tokenizeWhiteSpace = true ;
	scanner.tokenizeComments = true ;
	
	int token ;
	String prevIdentifier=null ;
	try {
	 token = scanner.getNextToken() ;
	 while (token != Scanner.TokenNameEOF &&
	        token != Scanner.TokenNameLPAREN &&
	        token != Scanner.TokenNamenew) {
	       
	       if (token == Scanner.TokenNameIdentifier)
	         if (prevIdentifier == null)
	            prevIdentifier = new String(scanner.getCurrentTokenSource()) ;
	         else
	            break ;
	       
	       token = scanner.getNextToken() ;
	 }
	}
	catch (InvalidInputException e) {
		return null;
	}
	
	if (token == Scanner.TokenNameIdentifier || token == Scanner.TokenNamenew)
	   return new String (scanner.getCurrentTokenSource()) ;
	else
	   if (prevIdentifier != null)
	      return prevIdentifier ;
	   else
	      return null ;
}

protected int skipSemiColonifNeeded(int right) {
    {
    	// The range from fSourceOff to fSourceOff+fSourceLength could
    	// hold Comments in them - so strip the comments off.
    	
    	String targetSrc = fSource.substring(fSourceOff,right);
		Scanner scanner = new Scanner() ;
		scanner.setSource(targetSrc.toCharArray()) ;
		scanner.recordLineSeparator = true ;
		scanner.tokenizeWhiteSpace = true ;
		scanner.tokenizeComments = true ;
		
		try{
			int token = scanner.getNextToken();
			for(int i=0;i<targetSrc.length();i++){
				if(token==Scanner.TokenNameEOF)
					break;
				if(token==Scanner.TokenNameSEMICOLON)
					return right ;
				token = scanner.getNextToken();
			}
		}
		catch(InvalidInputException e){
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e,Level.FINE) ;
		}		
    }
    int index = fSource.substring(right).indexOf(';') ;
    return index<0 ? right : right+index+1 ;
    
}

public static int indexOfSemiColon(String targetSrc) {
	Scanner scanner = new Scanner() ;
	scanner.setSource(targetSrc.toCharArray()) ;
	scanner.recordLineSeparator = false ;
	scanner.tokenizeWhiteSpace = true ;
	scanner.tokenizeComments = true ;
	
	try{
		int token = scanner.getNextToken();
		for(int i=0;i<targetSrc.length();i++){
			if(token==Scanner.TokenNameEOF)
				break;
			if(token==Scanner.TokenNameSEMICOLON)
				return scanner.currentPosition - 1; // current position goes to the position after getNextToken() 
			token = scanner.getNextToken();
		}
	}
	catch(InvalidInputException e){
		org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e,Level.FINE) ;
	}		
    return targetSrc.length();
}
	
protected void primParseExpression() {
		
	int left = getLineStartOff() ;
	
	primParseComment() ;
	
	int right = fCommentsOff+fCommentsLen ;
	if (right < 0)	// No Comments
	  right = fSourceOff+fSourceLen ;
	  
	right = skipSemiColonifNeeded(right) ;
	// right is now after the ; OR will be unaffected if ; is before right
	
	// Skip white space until EOL
	Scanner scanner = new Scanner() ;
	scanner.setSource(fSource.substring(right).toCharArray()) ;
	scanner.recordLineSeparator = true ;
	scanner.tokenizeWhiteSpace = true ;
	scanner.tokenizeComments = true ;
	
	try {
	 int token = scanner.getNextToken() ;	 
	 int prevToken = -1, prevStart = -1 ;
	 while (token == Scanner.TokenNameWHITESPACE) {
	     prevToken = token ;
	     prevStart = scanner.startPosition ;
	     token = scanner.getNextToken() ;
	 }
	     		   
	 // workaround: the Scanner will not record Line Seperators if it tokenizes white space
     int overide = -1 ;
	 if (prevToken == Scanner.TokenNameWHITESPACE) {
	 	int index ;
	 	for (index=prevStart; index<scanner.startPosition; index++) 
	 	   if (fSource.charAt(right+index) == '\r' || fSource.charAt(right+index) == '\n') {
	 	   	overide = right+index ;
	 	   	break ;
	 	   }
	 	if (overide>=0) 
	 	   overide = advanceLineSeperator(overide) ;
	 }	 
	 
	 if (scanner.lineEnds.length > 0 && scanner.getLineEnd(1)>0) { 
	 	// We advanced to the next line/s
	   right += scanner.getLineEnd(1) ;
	   right = advanceLineSeperator(right) ;
	 }
	 else if (token == Scanner.TokenNameEOF) {
	 	right += scanner.currentPosition-1 ;
	 }
	 else {
	 	// We have more than one expression on a given line, keep the white space
	 	if (overide > 0)
	 	   right = overide ;
	 	else
	 	   right += scanner.startPosition-1 ;
	 }
	 
	 
	 
	 fFillerOff=fExpOff=left ;
	 fFillerLen=fSourceOff-fFillerOff ;
	 fExpLen=right-left ;

    } 
    catch (InvalidInputException e) {
    	JavaVEPlugin.log(e, Level.WARNING) ;
    }
}

/**
 * @return String denoting the expression including the filler and comments
 * @param boolean includeEOL noting if to include the line seperator at the end of
 *                           the expression, if one exists.
 */

public String getExpression(boolean includeEOL) {
	if (fExpLen < 0 || fExpOff < 0)
	   primParseExpression() ;
	if (fExpLen < 0 || fExpOff < 0)
	   return null ;
	else {
	   int right = fExpOff+fExpLen ;
	   if (!includeEOL) 
	      right = reverseLineSeperator(right) ;
	   return fSource.substring(fExpOff,right) ;
	}
}

public int getExpressionOff() {
	if (fExpLen < 0 || fExpOff < 0)
	   primParseExpression() ;
	if (fExpLen < 0 || fExpOff < 0)
	   return -1 ;
	else 
	   return fExpOff ;
}


public int getExpressionLen(boolean includeEOL) {
	if (fExpLen < 0 || fExpOff < 0)
	   primParseExpression() ;
	if (fExpLen < 0 || fExpOff < 0)
	   return -1 ;
	else {
	   int right = fExpOff+fExpLen ;
	   if (!includeEOL) 
	      right = reverseLineSeperator(right) ;
	   return right-fExpOff ;
	}
}

public int getExpressionLen() {
	return getExpressionLen(true) ;
}


/**
 * @return String denoting the expression inculing the filler comments and EOL
 */
public String getExpression() {
	 return getExpression(true) ;
}	


protected void primParseComment() {
 
	Scanner scanner = new Scanner() ;
	int scanOff = fSourceOff+fSourceLen ;
	String scannerString = fSource.substring(scanOff);
	scanner.setSource(scannerString.toCharArray()) ;
	scanner.recordLineSeparator = true ;
	scanner.tokenizeComments = true ;
	
	try {
	 int token = scanner.getNextToken() ;	 
	 while (token == Scanner.TokenNameSEMICOLON ||	
	        token == Scanner.TokenNameWHITESPACE) 
	     token = scanner.getNextToken() ;
	 
     if (
         ((token == Scanner.TokenNameCOMMENT_LINE) &&
          ((scanner.getLineEnd(1)==0) || 
           (scanner.getLineEnd(1)>0 && scanner.currentPosition == scanner.getLineEnd(1)+1) ||
           (scanner.getLineEnd(2)>0 && scanner.currentPosition == scanner.getLineEnd(2)+1))
         )  ||
         ((token == Scanner.TokenNameCOMMENT_BLOCK || token == Scanner.TokenNameCOMMENT_JAVADOC) &&
     	  (scanner.lineEnds.length == 0 || scanner.getLineEnd(1)<=0 ||     	
     	     scanner.startPosition < scanner.getLineEnd(1)) 
     	 )) {
     	     // Comment starts on our line
     	     int endOfComment = scanner.currentPosition;
     	     
     	     // COMMENT_LINE tokens have the \r\n in them - 
     	     // this shouldnt be in the comment - it should be part of the expression.
     	     if(token == Scanner.TokenNameCOMMENT_LINE && scanner.getLineEnd(1)>0 && 
     	        ((scanner.currentPosition == scanner.getLineEnd(1)+1) ||
     	         (scanner.currentPosition == scanner.getLineEnd(2)+1))){
     	     	for(int f=0;f<2;f++)
	     	     	if(scannerString.charAt(endOfComment-1)=='\r' || scannerString.charAt(endOfComment-1)=='\n') 
	     	     		endOfComment--;
     	     }
	 	     fCommentsOff = scanOff+scanner.commentStarts[0] ;
       	     fCommentsLen = scanOff+endOfComment-fCommentsOff ;
	 }	      
    } 
    catch (InvalidInputException e) {
    	JavaVEPlugin.log(e, Level.WARNING) ;
    }	 
}
	
/**
 * Comments are any of COMMENT_LINE, COMMENT_BLOCK, COMMENT_JAVADOC 
 * in the same line as the expression. They do NOT include the LINE_SEPARATOR at
 * the end of the expression. Thats for the expression to take care of.
 * 
 * @return String denoting the comment portion at the end of the expression
 */	
public String getComment() {

  if (fCommentsOff<0) 
     primParseComment() ;
  
  if (fCommentsOff>0) {
  	return fSource.substring(fCommentsOff,fCommentsOff+fCommentsLen) ;
  }
  else return null ;
}	

public int getCommentOff() {
	if (fCommentsOff<0) 
        primParseComment() ;
  
    if (fCommentsOff>0) 
       return fCommentsOff ;
    else
       return -1 ;
}

public int getCommentLen() {
	if (fCommentsLen<0) 
        primParseComment() ;
  
    if (fCommentsLen>0) 
       return fCommentsLen ;
    else
       return -1 ;
}


/**
 * @return String denoting the white space preceeding the expression
 */	
public String getFiller() {
   if (fFillerOff < 0)  {
      fFillerOff = getLineStartOff() ;
      fFillerLen=fSourceOff-fFillerOff ;
   }
   if (fFillerOff < 0 || fFillerLen < 0)
	   return null ;
	else if (fFillerLen == 0) return "" ; //$NON-NLS-1$
	else
	   return fSource.substring(fFillerOff,fFillerOff+fFillerLen) ;         
}

public int getFillerOff() {
   if (fFillerOff < 0)  {
      fFillerOff = getLineStartOff() ;
      fFillerLen=fSourceOff-fFillerOff ;
   }
   if (fFillerOff < 0 || fFillerLen < 0)
	   return -1 ;
	else
	   return fFillerOff ;
}
public int getFillerLen() {
   if (fFillerLen < 0)  {
      fFillerOff = getLineStartOff() ;
      fFillerLen=fSourceOff-fFillerOff ;
   }
   if (fFillerOff < 0 || fFillerLen < 0)
	   return -1 ;
	else
	   return fFillerLen ;
}

public String toString() {
	// Debugger may get nested here,
	if (fExpOff>0 && fExpLen>0)
	  return getExpression() ;
	else
	  return super.toString() ;
}


	
}


	

