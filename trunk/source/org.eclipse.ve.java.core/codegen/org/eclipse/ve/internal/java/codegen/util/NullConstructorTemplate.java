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
 *  $RCSfile: NullConstructorTemplate.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */
/**
 * @version 	1.0
 * @author
 */
public class NullConstructorTemplate extends BeanMethodTemplate {


    public static final String MODIFIER =        "public" ; //$NON-NLS-1$
    public static final String SUPER = 			"super();" ; //$NON-NLS-1$
    public static        String DEFAULT_INIT_EXP_FILLER = null ; ; //$NON-NLS-1$
    
    String  fsuperInitString = SUPER ;
    /**
     * Constructor for NullConstructorTemplate.
     * @param btype
     * @param bname
     * @param mname
     * @param comments
     */
    public NullConstructorTemplate(
        String btype,
        String bname,
        String mname,
        String[] comments) {
        super(btype, bname, mname, comments);
    }
    
    public void setSuperInitString(String sig) {
        fsuperInitString = sig ;
    }
    
public   String getPrefix() {
	StringBuffer sb = new StringBuffer(1000) ;
	  // JCMMethod Comment
      punchLine(sb,COMMENT_BEG,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      if (fComments == null) {     
        punchLine(sb,COMMENT+VCE_COMMENT,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;             
      } else {		 
         for (int i=0; i<fComments.length; i++)
            punchLine(sb,COMMENT+fComments[i],CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;         
      } 
	  punchLine(sb,COMMENT,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;     
      punchLine(sb,COMMENT_END,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      
      // JCMMethod Decleration

        punchLine(sb,MODIFIER+
                  ExpressionTemplate.SPACE+fMethodName+ExpressionTemplate.LPAREN+
                  ExpressionTemplate.RPAREN+ExpressionTemplate.SPACE+BlockTemplate.LBRACE,
                  CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;

      // Super
      
      punchLine(sb,fsuperInitString,CodeTemplateHelper.NORMAL_METHOD_CONTENT_LEVEL) ;
      return sb.toString() ;
      
}    

public static String getInitExprFiller() {
	if (DEFAULT_INIT_EXP_FILLER == null) {
        DEFAULT_INIT_EXP_FILLER = 	CodeTemplateHelper.DEFAULT_FILLER+
                                    CodeTemplateHelper.getFillerForLevel(CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
    }
    return DEFAULT_INIT_EXP_FILLER ;
        
}

public String getPostfix() {
   StringBuffer sb = new StringBuffer() ;		
	
   // TODO 
   // catch
   punchLine(sb,null,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
   punchLine(sb,BlockTemplate.RBRACE,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;  
   return sb.toString() ; 	   	
}

}
