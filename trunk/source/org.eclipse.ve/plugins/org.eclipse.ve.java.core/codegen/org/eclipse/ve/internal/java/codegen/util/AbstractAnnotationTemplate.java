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
 *  $RCSfile: AbstractAnnotationTemplate.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
/**
 * @version 	1.0
 * @author
 */
public abstract class AbstractAnnotationTemplate {
    
	
	
final public static String ANNOTATION_SIG 	=	"@jve:" ; //$NON-NLS-1$
// No Support, yet for multi line def, so just stick it here
final public static String VISUAL_INFO_TYPE	=	"visual-info  decl-index=0" ; //$NON-NLS-1$



	
		
	String fName ;
	int    fIndent=2 ;
	String fSeperator = System.getProperty("line.separator") ; //$NON-NLS-1$
	String fAnnotationType = "none" ; //$NON-NLS-1$
	String fContent = ""; //$NON-NLS-1$


public AbstractAnnotationTemplate (String annotationType) {
    fAnnotationType = annotationType ;
}

	
public void setIndent(int i) { fIndent = i; } ;
public void setSeperator (String sep) { fSeperator = sep ; }
public void setContent (String c) { fContent = c ; }
public void setAnnotationType (String t) { fAnnotationType = t ; }





	
public String toString () {
		
    StringBuffer st = new StringBuffer() ;	
	
    for (int i=0; i<fIndent; i++)
	   st.append(ExpressionTemplate.SPACE) ;	
    st.append(ANNOTATION_SIG) ;
    st.append(fAnnotationType) ;
    st.append(ExpressionTemplate.SPACE) ;
    
    st.append(fContent) ;
    
    //st.append(fSeperator) ;
    
    return st.toString() ;
    
}







    
    
    

}
