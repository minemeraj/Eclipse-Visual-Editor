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
 *  $RCSfile: InstanceVariableTemplate.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
public class InstanceVariableTemplate {
	
	
final public static String INITIALIZOR =   "= null" ; //$NON-NLS-1$
final public static String MODIFIER    =   "private" ; //$NON-NLS-1$


	
	
	String fComments [] ;
	String fType ;
	String fName ;
	String    fIndent="\t" ;
	String fSeperator = System.getProperty("line.separator") ; //$NON-NLS-1$


public InstanceVariableTemplate (String name, String t, String[] comments) {
	fComments = comments;
	fName = name ;
	fType = t ;
}

	
public void setComments(String[] c) {  fComments = c; }	
public void setComment(String c) { fComments = new String[] {c}; }
public void setType(String t) { fType = t ; }
public void setName(String n) { fName = n ; }
public void setIndent(String s) { fIndent = s; } ;
public void setSeperator (String sep) { fSeperator = sep ; }
public String getSeperator () { return fSeperator ; }



	
public String toString () {
		
    StringBuffer st = new StringBuffer() ;	
	    
	st.append(fIndent) ;	
    st.append(MODIFIER+ExpressionTemplate.SPACE) ;
    st.append(fType+ExpressionTemplate.SPACE) ;
    st.append(fName+ExpressionTemplate.SPACE) ;   
    st.append(INITIALIZOR+ExpressionTemplate.SEMICOL) ;
    // TODO  Multi Line Comment
    if (fComments != null && fComments.length>0)
      if (fComments[0]!= null)
       st.append(ExpressionTemplate.SPACE+ExpressionTemplate.COMMENT+fComments[0]) ;
    st.append(fSeperator) ;
    
    return st.toString() ;
    
}

}


