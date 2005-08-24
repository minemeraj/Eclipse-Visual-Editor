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
/*
 *  $RCSfile: TryTemplate.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */


public class TryTemplate  {
	

public static final String TRY =    "try" ; //$NON-NLS-1$
public static final String CATCH =    "catch" ; //$NON-NLS-1$


	
	BlockTemplate tryPart=null,
	              catchPart=null ;
	int           fIndent = 5 ;
	int           fBlockIndent = 5 ;
	String        fException ;

public TryTemplate(BlockTemplate tryP, BlockTemplate catchP,String excep, int i) {
	tryPart = tryP ;
	catchPart = catchP ;
	fIndent = i ;
	fException = excep ;
}
	
public void setIndent(int i) { fIndent = i; } ; 

protected void punchLine(StringBuffer sb,Object content) {
	for (int i=0; i<fIndent; i++)
	  sb.append(ExpressionTemplate.SPACE) ;
	sb.append(content.toString()) ;
	sb.append(System.getProperty("line.separator")) ; //$NON-NLS-1$
}	
	          

public String toString() {
	
	if (tryPart == null) return "" ; //$NON-NLS-1$
	
	StringBuffer sb = new StringBuffer () ;
	tryPart.setIndent(fIndent+fBlockIndent) ;
	punchLine(sb,TRY+ExpressionTemplate.SPACE+BlockTemplate.LBRACE);
	sb.append(tryPart.toString()) ;
	
	catchPart.setIndent(fIndent+fBlockIndent) ;
	punchLine(sb,CATCH+ExpressionTemplate.LPAREN+fException+ExpressionTemplate.RPAREN+ExpressionTemplate.SPACE+BlockTemplate.LBRACE) ;
	sb.append(catchPart.toString()) ;
	
	punchLine(sb,BlockTemplate.RBRACE) ;
	return sb.toString() ;
	
}


}
