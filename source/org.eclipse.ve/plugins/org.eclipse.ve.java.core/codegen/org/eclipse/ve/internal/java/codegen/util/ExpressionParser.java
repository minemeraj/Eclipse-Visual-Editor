package org.eclipse.ve.internal.java.codegen.util;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ExpressionParser.java,v $
 *  $Revision: 1.7 $  $Date: 2005-03-30 17:34:23 $ 
 */

import java.util.logging.Level;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.*;

import org.eclipse.ve.internal.java.codegen.model.IScannerFactory;
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
	
	protected String fSource ;
	
	protected int    fSourceOff ;   // not including a filler, or comments
	protected int    fSourceLen ;
	
	protected int    fExpOff = -1 ; // The works
	protected int    fExpLen = -1 ;
	
	protected int    fCommentsOff = -1 ; 
	protected int    fCommentsLen = -1 ;
	
	protected int    fFillerOff = -1 ; // Prefixed  white space
	protected int    fFillerLen = -1 ;
	
	protected IScannerFactory fScannerFactory = null; // Factory to provide scanner
	
/**
 *  @param sourceSnippet the source code body where the expression is part off
 *  @param expOffset  where the expression java source starts (    >setFoo() <;)
 *  @param expLen     the length of the source .
 */	
public ExpressionParser(String sourceSnippet, int expOffset, int expLen, IScannerFactory scannerFactory) {
		fSource = sourceSnippet ;
		fSourceOff = expOffset ;
		fSourceLen = expLen ;
		fScannerFactory = scannerFactory;
}

/**
 * Expression sources now contain the comments inside of them.
 */
public ExpressionParser(IField field, IScannerFactory scannerFactory){
	try{
		fSource = field.getCompilationUnit().getSource();
		fSourceOff = field.getSourceRange().getOffset();
		fSourceLen = indexOfSemiColon(field.getSource(), scannerFactory);
		fScannerFactory = scannerFactory;
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
	//Scanner scanner = new Scanner() ;
	//scanner.setSource(code.toCharArray()) ;
	//scanner.recordLineSeparator = true ;
	//scanner.tokenizeWhiteSpace = true ;
	//scanner.tokenizeComments = true ;
	
	IScanner scanner = fScannerFactory.getScanner(true, true, true);
	scanner.setSource(code.toCharArray());
	int token ;
	String prevIdentifier=null ;
	try {
	 token = scanner.getNextToken() ;
	 while (token != ITerminalSymbols.TokenNameEOF &&
	        token != ITerminalSymbols.TokenNameLPAREN &&
	        token != ITerminalSymbols.TokenNamenew) {
	       
	       if (token == ITerminalSymbols.TokenNameIdentifier)
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
	
	if (token == ITerminalSymbols.TokenNameIdentifier || token == ITerminalSymbols.TokenNamenew)
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
		//Scanner scanner = new Scanner() ;
		//scanner.setSource(targetSrc.toCharArray()) ;
		//scanner.recordLineSeparator = true ;
		//scanner.tokenizeWhiteSpace = true ;
		//scanner.tokenizeComments = true ;
		IScanner scanner = fScannerFactory.getScanner(true, true, true);
		scanner.setSource(targetSrc.toCharArray());
		
		try{
			int token = scanner.getNextToken();
			for(int i=0;i<targetSrc.length();i++){
				if(token==ITerminalSymbols.TokenNameEOF)
					break;
				if(token==ITerminalSymbols.TokenNameSEMICOLON)
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

public static int indexOfSemiColon(String targetSrc, IScannerFactory scannerFactory) {
	//Scanner scanner = new Scanner() ;
	//scanner.setSource(targetSrc.toCharArray()) ;
	//scanner.recordLineSeparator = false ;
	//scanner.tokenizeWhiteSpace = true ;
	//scanner.tokenizeComments = true ;
	IScanner scanner = scannerFactory.getScanner(true, true, false);
	scanner.setSource(targetSrc.toCharArray());
	
	try{
		int token = scanner.getNextToken();
		for(int i=0;i<targetSrc.length();i++){
			if(token==ITerminalSymbols.TokenNameEOF)
				break;
			if(token==ITerminalSymbols.TokenNameSEMICOLON)
				return scanner.getCurrentTokenStartPosition();
				//return scanner.currentPosition - 1; // current position goes to the position after getNextToken() 
			token = scanner.getNextToken();
		}
	}
	catch(InvalidInputException e){
		org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e,Level.FINE) ;
	}		
    return targetSrc.length();
}

public static int indexOfLastSemiColon(String targetSrc, IScannerFactory scannerFactory) {
	//Scanner scanner = new Scanner() ;
	//scanner.setSource(targetSrc.toCharArray()) ;
	//scanner.recordLineSeparator = false ;
	//scanner.tokenizeWhiteSpace = true ;
	//scanner.tokenizeComments = true ;
	IScanner scanner = scannerFactory.getScanner(true, true, false);
	scanner.setSource(targetSrc.toCharArray());

	int semicolonIndex = -1;
	try{
		int token = scanner.getNextToken();
		for(int i=0;i<targetSrc.length();i++){
			if(token==ITerminalSymbols.TokenNameEOF)
				break;
			if(token==ITerminalSymbols.TokenNameSEMICOLON)
				//semicolonIndex = scanner.currentPosition - 1; // current position goes to the position after getNextToken()
				semicolonIndex = scanner.getCurrentTokenStartPosition();
			token = scanner.getNextToken();
		}
	}
	catch(InvalidInputException e){
		org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e,Level.FINE) ;
	}
	if(semicolonIndex>-1 && semicolonIndex<targetSrc.length())
		return semicolonIndex;
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
	//Scanner scanner = new Scanner() ;
	//scanner.setSource(fSource.substring(right).toCharArray()) ;
	//scanner.recordLineSeparator = true ;
	//scanner.tokenizeWhiteSpace = true ;
	//scanner.tokenizeComments = true ;
	IScanner scanner = fScannerFactory.getScanner(true, true, true);
	scanner.setSource(fSource.substring(right).toCharArray());
	
	try {
	 int token = scanner.getNextToken() ;	 
	 int prevToken = -1, prevStart = -1 ;
	 while (token == ITerminalSymbols.TokenNameWHITESPACE) {
	     prevToken = token ;
	     //prevStart = scanner.startPosition ;
	     prevStart = scanner.getCurrentTokenStartPosition();
	     token = scanner.getNextToken() ;
	 }
	     		   
	 // workaround: the Scanner will not record Line Seperators if it tokenizes white space
     int overide = -1 ;
	 if (prevToken == ITerminalSymbols.TokenNameWHITESPACE) {
	 	int index ;
	 	for (index=prevStart; index<scanner.getCurrentTokenStartPosition(); index++) 
	 	   if (fSource.charAt(right+index) == '\r' || fSource.charAt(right+index) == '\n') {
	 	   	overide = right+index ;
	 	   	break ;
	 	   }
	 	if (overide>=0) 
	 	   overide = advanceLineSeperator(overide) ;
	 }	 
	 
	 if (scanner.getLineEnds().length > 0 && scanner.getLineEnd(1)>0) { 
	 	// We advanced to the next line/s
	   right += scanner.getLineEnd(1) ;
	   right = advanceLineSeperator(right) ;
	 }
	 else if (token == ITerminalSymbols.TokenNameEOF) {
	 	right += scanner.getCurrentTokenEndPosition() ;
	 }
	 else {
	 	// We have more than one expression on a given line, keep the white space
	 	if (overide > 0)
	 	   right = overide ;
	 	else
	 	   right += scanner.getCurrentTokenStartPosition()-1 ;
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
 
	int scanOff = fSourceOff+fSourceLen ;
	String scannerString = fSource.substring(scanOff);
	//Scanner scanner = new Scanner() ;
	//scanner.setSource(scannerString.toCharArray()) ;
	//scanner.recordLineSeparator = true ;
	//scanner.tokenizeComments = true ;
	IScanner scanner = fScannerFactory.getScanner(true, false, true);
	scanner.setSource(scannerString.toCharArray());
	
	try {
	 int token = scanner.getNextToken() ;	 
	 while (token == ITerminalSymbols.TokenNameSEMICOLON ||	
	        token == ITerminalSymbols.TokenNameWHITESPACE) 
	     token = scanner.getNextToken() ;
	 
     if (
         ((token == ITerminalSymbols.TokenNameCOMMENT_LINE) &&
          ((scanner.getLineEnd(1)==0) || 
           (scanner.getLineEnd(1)>0 && (scanner.getCurrentTokenEndPosition()+1) == scanner.getLineEnd(1)+1) ||
           (scanner.getLineEnd(2)>0 && (scanner.getCurrentTokenEndPosition()+1) == scanner.getLineEnd(2)+1))
         )  ||
         ((token == ITerminalSymbols.TokenNameCOMMENT_BLOCK || token == ITerminalSymbols.TokenNameCOMMENT_JAVADOC) &&
     	  (scanner.getLineEnds().length == 0 || scanner.getLineEnd(1)<=0 ||     	
     	     scanner.getCurrentTokenStartPosition() < scanner.getLineEnd(1)) 
     	 )) {
     		int firstCommenStartPosition = scanner.getCurrentTokenStartPosition();
     	     // Comment starts on our line
     	     int endOfComment = scanner.getCurrentTokenEndPosition()+1;
     	     
     	     // COMMENT_LINE tokens have the \r\n in them - 
     	     // this shouldnt be in the comment - it should be part of the expression.
     	     if(token == ITerminalSymbols.TokenNameCOMMENT_LINE && scanner.getLineEnd(1)>0 && 
     	        (((scanner.getCurrentTokenEndPosition()+1) == scanner.getLineEnd(1)+1) ||
     	         ((scanner.getCurrentTokenEndPosition()+1) == scanner.getLineEnd(2)+1))){
     	     	for(int f=0;f<2;f++)
	     	     	if(scannerString.charAt(endOfComment-1)=='\r' || scannerString.charAt(endOfComment-1)=='\n') 
	     	     		endOfComment--;
     	     }
	 	     fCommentsOff = scanOff+firstCommenStartPosition ;
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


	
