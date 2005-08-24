/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventExpressionParser.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.HashMap;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import org.eclipse.ve.internal.java.codegen.java.CodeGenSourceRange;
import org.eclipse.ve.internal.java.codegen.java.ICodeGenSourceRange;
import org.eclipse.ve.internal.java.codegen.model.IScannerFactory;

/**
 * @author gmendel
 */
public class EventExpressionParser extends ExpressionParser {


     HashMap fCallBackOffset = new HashMap() ;

	/**
	 * @param sourceSnippet
	 * @param expOffset
	 * @param expLen
	 */
	public EventExpressionParser(String sourceSnippet, int expOffset, int expLen, IScannerFactory scannerFactory) {
		super(sourceSnippet, expOffset, expLen, scannerFactory);		
	}

	/**
	 * @param field
	 */
	public EventExpressionParser(IField field, IScannerFactory scannerFactory) {
		super(field, scannerFactory);		
	}
	
	/**
	 * Overide the regular expression for the skip, as an anonymouse event expression may
	 * have nested expressions with ';' in them.
	 * 
	 */
	protected int skipSemiColonifNeeded(int right) {				
			// The range from fSourceOff to fSourceOff+fSourceLength could
			// hold Comments in them - so strip the comments off.
			// TODO need to deal with this one.
    	
		int index = fSource.substring(right).indexOf(';') ;
		return index<0 ? right : right+index+1 ;    
	}
	
	/**
	 *  Will scan for anonymouse method name, and return the offset (line start + expr. filler)
	 * @param name
	 * @return offset from the beginning of ths expression.
	 */
	protected int getAnonymousMethodStart(String name) {
		// Note: using here the new AST parser.
		if (fCallBackOffset.get(name) != null)  
		   return ((Integer)fCallBackOffset.get(name)).intValue() ;
		org.eclipse.jdt.core.compiler.IScanner scanner = fScannerFactory.getScanner(false, false, true);
		scanner.setSource(getExpression().toCharArray());
		try {
			int token;
			int startingOffset = -1;
			// Search for a public/void token and look for a method's name		
			while ((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF) {
				switch (token) {
					case ITerminalSymbols.TokenNamepublic :
					case ITerminalSymbols.TokenNamevoid :
						if (startingOffset < 0)
							startingOffset = scanner.getCurrentTokenStartPosition();
						break;
					case ITerminalSymbols.TokenNameIdentifier :
						if (startingOffset > 0)
							if (name.equals(new String(scanner.getCurrentTokenSource()))) {
								// get the offset from the expression filler 
							    int off = scanner.getLineStart(scanner.getLineNumber(startingOffset)) ;
							    off = off + getFillerLen() ;
							    fCallBackOffset.put(name, new Integer(off)) ;
								return off;
							}
						startingOffset = -1;
						break;
				}
			}
		}
		catch (InvalidInputException ex) {}
		return -1;
	}
		
	
	/**
	 *   This method will parse and remove the body of the given method for a style 3 event.
	 *   CodeGenException is thrown if the method is not found.  It is assumed that the text is well formed
	 * 
	 *   A new EventExpressionParser is returned if changes are needed, null returned otherwise
	 * @param name
	 * @return
	 */
	public EventExpressionParser removeAnonymousMethodBody(String name) throws CodeGenException {
		int startingOffset = getAnonymousMethodStart(name) ;
		if (startingOffset < 0) throw new CodeGenException("JCMMethod not found") ; //$NON-NLS-1$

		// Note: using here the new AST parser.		
		org.eclipse.jdt.core.compiler.IScanner scanner = fScannerFactory.getScanner(false, false, true);
		scanner.setSource(getExpression().substring(startingOffset).toCharArray());
		int start = -1;
		int end = -1;
		try {
			int token;
			int count = 0 ;
			boolean done = false ;
			// Search for the first '{' and last '}' 		
			while ((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF && !done) {
				switch (token) {
					case ITerminalSymbols.TokenNameLBRACE:
					      if (start<0)
					         start = scanner.getCurrentTokenEndPosition()+1 ;
					      else
					         count++ ;
					      break ;
					case ITerminalSymbols.TokenNameRBRACE:
					      if (count==0) {
					         end = scanner.getCurrentTokenStartPosition() ;
					         // Stop iterating
					         done = true ;
					      }
					      else
					         count-- ;
						break;
				}
			}
		}
		catch (InvalidInputException ex) {}
		if (start<0 || end<0) throw new CodeGenException("mulformed method") ; //$NON-NLS-1$
		
		// check to see if we need to delete anyting
		if (start==end) return null ;
		
		// normalize indexes
		start+= startingOffset ;
		end+= startingOffset ;
		
		StringBuffer buff = new StringBuffer(getExpression()) ;
		buff.replace(start, end, "") ; //$NON-NLS-1$
		
		int off = fFillerLen ;
		int end1 = ExpressionParser.indexOfLastSemiColon(buff.toString(), fScannerFactory)-1;
		int len = end1-off+1;
		return new EventExpressionParser(buff.toString(),off,len, fScannerFactory) ;
	}
	
	
	protected int getAnonymousMethodEndOffset(int startingOffset) throws CodeGenException {

		if (startingOffset < 0)
			throw new CodeGenException("JCMMethod not found"); //$NON-NLS-1$

		// Note: using here the new AST parser.		
		org.eclipse.jdt.core.compiler.IScanner scanner = fScannerFactory.getScanner(false, false, true);
		scanner.setSource(getExpression().substring(startingOffset).toCharArray());
		int start = -1;
		int end = -1;
		try {
			int token;
			int count = 0;
			boolean done = false;
			// Search for the first '{' and last '}' 		
			while ((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF && !done) {
				switch (token) {
					case ITerminalSymbols.TokenNameSEMICOLON:
					      if (start < 0) {					      
					         end = scanner.getCurrentTokenStartPosition() + 1;
					         start = 1 ;
					         done = true ;
					      }	
					      break ;				
					case ITerminalSymbols.TokenNameLBRACE :
						if (start < 0)
							start = scanner.getCurrentTokenEndPosition() + 1;
						else
							count++;
						break;
					case ITerminalSymbols.TokenNameRBRACE :
						if (count == 0) {
							end = scanner.getCurrentTokenStartPosition() + 1;
							
							// If there is a semi-colon right after the ending brace,
							// associate it with this method, as when removing, it leaves
							// the semi-colon behind, making it look like a bug.
							if(scanner.getNextToken()==ITerminalSymbols.TokenNameSEMICOLON){
								end = scanner.getCurrentTokenStartPosition()+1;
							}
							
							// Stop iterating
							done = true;
						}
						else
							count--;
						break;
				}
			}
		}
		catch (InvalidInputException ex) {}
		if (start < 0 || end < 0)
			throw new CodeGenException("malformed method"); //$NON-NLS-1$

		// normalize index
		end += startingOffset;

		return end;

	}
	/**
	 *   This method will parse and remove the body of a given method for a style 3 event.
	 *   CodeGenException is thrown if the method is not found.  It is assumed that text is correctly formed.
	 * 
	 *   A new EventExpressionParser is returned if changes are needed, null returned otherwise
	 * @param name
	 * @return
	 */
	public EventExpressionParser removeAnonymousMethod(String name) throws CodeGenException {
		int startingOffset = getAnonymousMethodStart(name) ;
		int end = getAnonymousMethodEndOffset(startingOffset) ;
		
		StringBuffer buff = new StringBuffer(getExpression()) ;
		buff.replace(startingOffset, end, "") ; //$NON-NLS-1$
		
		int off = fFillerLen ;
		int end1 = ExpressionParser.indexOfLastSemiColon(buff.toString(), fScannerFactory)-1;
		int len = end1-off+1;
		return new EventExpressionParser(buff.toString(),off,len, fScannerFactory) ;
	}
	
	protected int getEventBodyStart() {
		org.eclipse.jdt.core.compiler.IScanner scanner = fScannerFactory.getScanner(false, false, true);
		scanner.setSource(getExpression().toCharArray());
		int startingOffset = -1;
		try {
			int token;			
			// Search for a public/void token and look for a method's name		
			while ((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF &&
			        startingOffset<0) {
				switch (token) {
					case ITerminalSymbols.TokenNameLBRACE :
						startingOffset = scanner.getCurrentTokenEndPosition()+1;
						break;
				}
			}
		}
		catch (InvalidInputException ex) {}
		return startingOffset ;
	}
	
	public EventExpressionParser addAnonymousMethod(String content) throws CodeGenException {
		int offset = getEventBodyStart() ;
		
		StringBuffer buff = new StringBuffer(getExpression()) ;
		buff.insert(offset,content) ;
		
		int off = fFillerLen ;
		int end1 = ExpressionParser.indexOfLastSemiColon(buff.toString(), fScannerFactory)-1;
		int len = end1-off+1;
		return new EventExpressionParser(buff.toString(),off,len, fScannerFactory) ;
	}
	
	public EventExpressionParser addPropertyBlock (String content) throws CodeGenException {
		int start = getEventBodyStart();
		org.eclipse.jdt.core.compiler.IScanner scanner = fScannerFactory.getScanner(false, false, true);
		scanner.setSource(getExpression().substring(start).toCharArray());
		int startingOffset = -1;
		try {
			int token;
			// Search for a public/void token and look for a method's name		
			while ((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF && startingOffset < 0) {
				switch (token) {
					case ITerminalSymbols.TokenNameLBRACE :
						startingOffset = scanner.getCurrentTokenEndPosition() + 1;
						break;
				}
			}
		}
		catch (InvalidInputException ex) {}

		StringBuffer buff = new StringBuffer(getExpression());
		buff.insert(start + startingOffset, content);
		
		int off = fFillerLen ;
		int end = ExpressionParser.indexOfLastSemiColon(buff.toString(), fScannerFactory)-1;
		int len = end-off+1;
		return new EventExpressionParser(buff.toString(), off, len, fScannerFactory);
		
	}
	
	
	protected int getPropertyStart(String property) {		
		org.eclipse.jdt.core.compiler.IScanner scanner = fScannerFactory.getScanner(false, false, true);
		scanner.setSource(getExpression().toCharArray());
		int ifOffset = -1;
		int startingOffset = - 1 ;
		int lpCount = -1 ;
		int count=-1 ;
		boolean twoArg = false ;
		try {
			int token;
			// Search for an in if with "property" 		
			while ((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF && startingOffset < 0) {
				switch (token) {					
					case ITerminalSymbols.TokenNameif :
					      ifOffset = scanner.getCurrentTokenStartPosition();
					      count=0 ;
						  break;
				    case ITerminalSymbols.TokenNameLPAREN:
				          lpCount++ ;
				          if (count>=0) count++ ;
				          break ;
					case ITerminalSymbols.TokenNameRPAREN:
					      lpCount-- ;
					      if (count>0) {
					      	count-- ;
					      	if (count==0) {
					      		ifOffset = -1 ;
					      		count = -1 ;
					      	}
					      }
					      break ;
					case ITerminalSymbols.TokenNamepublic:
					      lpCount-- ; // Cludge .. we know that we passed the first method
					      break ;
					case ITerminalSymbols.TokenNameLBRACE:
					      if (twoArg && lpCount<0) {
							startingOffset = scanner.getCurrentTokenStartPosition()+1;
					      }
					      break ;
				    case ITerminalSymbols.TokenNameStringLiteral:
				          // inside an if Statement, or part of a two argment property - add ("property", Listener)
				          if (ifOffset>=0 || lpCount==0) {
				          	if (new String(scanner.getCurrentTokenSource()).equals("\""+property+"\"")) //$NON-NLS-1$ //$NON-NLS-2$
				          	   if (ifOffset>0)
				          	       startingOffset = ifOffset ;
				          	   else
				          	       twoArg = true ;			          	   
				          }
				          break ;				    
				}
			}
		}
		catch (InvalidInputException ex) {}
		return startingOffset ;
	}
	/**
	 * It does expect an if Statement to be there
	 * 
	 * @param property
	 * @return
	 * @throws CodeGenException
	 */
	public EventExpressionParser removeAnonymousProperty (String property) throws CodeGenException {
		int start = getPropertyStart(property) ;
		int end = getAnonymousMethodEndOffset(start) ;
		
		if (start<0 || end <0) return null ;
		
		StringBuffer buff = new StringBuffer(getExpression()) ;
		
		int index = end ;
		while (index<buff.length()-1 && 
		       (buff.charAt(index)==' ' || buff.charAt(index)=='\r' || buff.charAt(index)=='\n')) {		       
		       index++ ;
		}
		if (index<buff.length()-1)
		   end = index ;
		buff.replace(start, end, "") ; //$NON-NLS-1$
		
		int off = fFillerLen ;
		int end1 = ExpressionParser.indexOfLastSemiColon(buff.toString(), fScannerFactory)-1;
		int len = end1-off+1;
		return new EventExpressionParser(buff.toString(),off,len, fScannerFactory) ;
	
	}
	
	/**
	 * 
	 * @param name
	 * @return Offset from the expression.
	 * @throws CodeGenException
	 */
	public ICodeGenSourceRange getAnonymousMethodHighlight(String name) throws CodeGenException {
		final int start = getAnonymousMethodStart(name);
		final int end = getAnonymousMethodEndOffset(start);

		CodeGenSourceRange sr = new CodeGenSourceRange(start, end - start + 1);
		return sr;
	}

	/**
		 * 
		 * @param name
		 * @return Offset from the expression.
		 * @throws CodeGenException
		 */
	public ICodeGenSourceRange getAnonymousMethodProperty(String property) throws CodeGenException {
		final int start = getPropertyStart(property);
		final int end = getAnonymousMethodEndOffset(start);

		CodeGenSourceRange sr = new CodeGenSourceRange(start, end - start + 1);
		return sr;
	}

}
