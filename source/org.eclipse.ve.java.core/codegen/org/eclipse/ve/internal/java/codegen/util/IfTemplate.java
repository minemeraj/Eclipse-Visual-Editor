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
 *  $RCSfile: IfTemplate.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:28:35 $ 
 */


public class IfTemplate {

public static final String IF =    "if" ; //$NON-NLS-1$

	
	int fIndent = 5;
	
	BlockTemplate ifPart  ;
	String        fExpression ;
	int           fBlockIndent = 5 ;

public void setIndent(int i) { fIndent = i; } ; 

public IfTemplate (BlockTemplate ifPart, String exp) {
	this.ifPart = ifPart ;
	fExpression = exp ;
}

protected void punchLine(StringBuffer sb,Object content) {
	for (int i=0; i<fIndent; i++)
	  sb.append(ExpressionTemplate.SPACE) ;
	sb.append(content.toString()) ;
	sb.append(System.getProperty("line.separator")) ; //$NON-NLS-1$
}	
	          

public String toString() {
	
	if (ifPart == null) return "" ; //$NON-NLS-1$
	
	StringBuffer sb = new StringBuffer () ;
	ifPart.setIndent(fIndent+fBlockIndent) ;
	punchLine(sb,IF+ExpressionTemplate.SPACE+ExpressionTemplate.LPAREN+fExpression+ExpressionTemplate.RPAREN+ExpressionTemplate.SPACE+BlockTemplate.LBRACE);
	sb.append(ifPart.toString()) ;	
	
	punchLine(sb,BlockTemplate.RBRACE) ;
	return sb.toString() ;
	
}



}