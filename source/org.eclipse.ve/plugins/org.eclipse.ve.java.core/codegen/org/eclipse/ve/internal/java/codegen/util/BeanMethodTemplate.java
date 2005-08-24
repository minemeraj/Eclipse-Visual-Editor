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
 *  $RCSfile: BeanMethodTemplate.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:46 $ 
 */

import org.eclipse.core.runtime.Preferences;

import org.eclipse.ve.internal.java.vce.VCEPreferences;


/**
 *   Temporary thing -- need major cleanup
 * @deprecated  move to JavaJets instead
 */
public class BeanMethodTemplate {
    public static final String COMMENT_BEG =    "/**" ; //$NON-NLS-1$
    public static final String COMMENT_END =    " */" ; //$NON-NLS-1$
    public static final String COMMENT     =    " * " ;    	 //$NON-NLS-1$
    public static final String MODIFIER =       "private" ; //$NON-NLS-1$
    public static final String EQNULL =         " == null" ;     //$NON-NLS-1$
    public static final String RETURN =         "return" ;         //$NON-NLS-1$
    public static final String COMMENT_IMPLICIT =   ExpressionTemplate.COMMENT_GENERATED ;
    public static final String COMMENT_EXPLICIT =   ExpressionTemplate.COMMENT_GENERATED ;
    public static final String VCE_COMMENT =    "This method initializes " ; //$NON-NLS-1$
    public static final String VOID =    "void" ; //$NON-NLS-1$
    
    
    
  
  String     fBeanType ;
  String     fBeanName ;
  String     fMethodName ;
  String     fComments[] ;
  String     fBeanInitString ;			// Instance initialization expression - new()
  String     fBeanConstructorString  = null ;
  int        fBeanInitStringOffset=-1 ;  // Not including the filler
  boolean   fImplicitMethod = false ;
  String     fLineSeperator = System.getProperty("line.separator") ;   //$NON-NLS-1$
  boolean   fInLineMethod = false ;
  boolean   fisThisMethod = false ;     // this method does not require the if/then test
  String     vce_comment = null ;
  String     fModifier = MODIFIER ;
  boolean   fGenerateComments = false ;
  boolean   fGenerateTryCatch = false;
  
  
  

public BeanMethodTemplate (String btype, String bname, String mname, String[] comments) {
	fBeanType = btype ;
	fBeanName = bname ;
	fMethodName = mname ;
	fComments = comments ;
    fBeanInitString = null ;
    vce_comment = VCE_COMMENT + fBeanName ;
    Preferences store = VCEPreferences.getPlugin().getPluginPreferences();
    fGenerateComments=store.getBoolean(VCEPreferences.GENERATE_COMMENT);
    fGenerateTryCatch=store.getBoolean(VCEPreferences.GENERATE_TRY_CATCH_BLOCK);
}	

public void setInLineMethod(boolean flag) {  fInLineMethod = flag ; }
public void setThisMethod(boolean flag) {  
    fisThisMethod = flag ; 
    if (flag)
      setBeanInitString("") ; //$NON-NLS-1$
}

public void setSeperator (String sep) {	
	fLineSeperator = sep ;
}

public void setModifier (String m) {
    fModifier = m ;
}

/**
 * get the method offset for the init expression.
 */
public int getInitExpressionOffset() {
	return fBeanInitStringOffset ;
}

/**
 * Get the init expression only.
 */
public String getInitExpression() {
	String line = getInitString() ;
	return line.substring(0,line.indexOf(")")+1) ; //$NON-NLS-1$
}

public void setImplicit (boolean flag) {
   fImplicitMethod = flag ;
}

public void setComments(String [] comments) {
	fComments = comments ;
}

protected void punchLine(StringBuffer sb,Object content, int codeLevel) {
    if (content != null) {
	  sb.append(CodeTemplateHelper.getFillerForLevel(codeLevel));
	  sb.append(content.toString()) ;
    }
	sb.append(fLineSeperator) ;
}	

/**
 * Set the complete initialization string,
 * e.g., Foo x = new Foo() ; 
 */
public void setBeanInitString(String str) {
	fBeanInitString = str ;
}

/**
 * Use the "constructor" string to set up the init string.
 * e.g., "new Foo()"   
 */
public void setBeanConstructorString(String str) {
    fBeanConstructorString = str ;
}

public static String getInitExprFiller() {
	if(VCEPreferences.getPlugin().getPluginPreferences().getBoolean(VCEPreferences.GENERATE_TRY_CATCH_BLOCK))
		return CodeTemplateHelper.getFillerForLevel(CodeTemplateHelper.NORMAL_TRY_CONTENT_LEVEL) ;	
	else
		return CodeTemplateHelper.getFillerForLevel(CodeTemplateHelper.NORMAL_IF_CONTENT_LEVEL) ;
}

private  String getInitString() {
    String result ;    
	if (fBeanInitString == null) {
	    if (fInLineMethod) 
	       result = fBeanType + ExpressionTemplate.SPACE ;
	    else
	       result = "" ; //$NON-NLS-1$
		// Use default instance variable initialization string
		if (fImplicitMethod) {
		  result += fBeanName+" = arg;" ; //$NON-NLS-1$
          if (fGenerateComments)
            result+=COMMENT_IMPLICIT ;
        }
		else {
          if (fBeanConstructorString == null)
		      result += fBeanName+" = new "+fBeanType+"(); " ;    		   //$NON-NLS-1$ //$NON-NLS-2$
          else
              result += fBeanName+" = "+fBeanConstructorString+";" ; //$NON-NLS-1$ //$NON-NLS-2$
          if (fGenerateComments)
              result += COMMENT_EXPLICIT ;
        }
	}
	else
	  result = fBeanInitString ;
   return result ;	
}

public   String getPrefix() {
	StringBuffer sb = new StringBuffer(1000) ;
	  // JCMMethod Comment
      punchLine(sb,COMMENT_BEG,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      punchLine(sb,COMMENT+vce_comment,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      punchLine(sb,COMMENT,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      if (fComments != null) {
         for (int i=0; i<fComments.length; i++)
            punchLine(sb,COMMENT+fComments[i],CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
         punchLine(sb,COMMENT,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      }      
      if (!fBeanType.equals(VOID))
          punchLine(sb,COMMENT+"@"+RETURN+ExpressionTemplate.SPACE+fBeanType,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ; //$NON-NLS-1$
      punchLine(sb,COMMENT_END,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      
      // JCMMethod Decleration
      if (fImplicitMethod) {
        punchLine(sb,fModifier+ExpressionTemplate.SPACE+fBeanType+
                  ExpressionTemplate.SPACE+fMethodName+ExpressionTemplate.LPAREN+
                  fBeanType+ExpressionTemplate.SPACE+"arg"+ //$NON-NLS-1$
                  ExpressionTemplate.RPAREN+ExpressionTemplate.SPACE+BlockTemplate.LBRACE,
                  CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      	
      }
      else {
        punchLine(sb,fModifier+ExpressionTemplate.SPACE+fBeanType+
                  ExpressionTemplate.SPACE+fMethodName+ExpressionTemplate.LPAREN+
                  ExpressionTemplate.RPAREN+ExpressionTemplate.SPACE+BlockTemplate.LBRACE,
                  CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;
      }

	  int level;
      // If Statement   
      if (!fisThisMethod) {   
        // TODO
        punchLine(sb,IfTemplate.IF+ExpressionTemplate.LPAREN+fBeanName+
                EQNULL+ExpressionTemplate.RPAREN+ExpressionTemplate.SPACE+BlockTemplate.LBRACE,
                CodeTemplateHelper.NORMAL_METHOD_CONTENT_LEVEL) ;
        level = CodeTemplateHelper.NORMAL_IF_CONTENT_LEVEL;
      }else{
      	level = CodeTemplateHelper.NORMAL_METHOD_CONTENT_LEVEL;
      }
      // Try Statement
      if(fGenerateTryCatch)
	      punchLine(sb,TryTemplate.TRY+ExpressionTemplate.SPACE+BlockTemplate.LBRACE,level) ;                
      
      // new the bean - deafult constructor
      fBeanInitStringOffset=sb.length()+getInitExprFiller().length() ; 
      if(fGenerateTryCatch)    
	      punchLine(sb,getInitString(), CodeTemplateHelper.NORMAL_TRY_CONTENT_LEVEL) ;
	  else
	      punchLine(sb,getInitString(), CodeTemplateHelper.NORMAL_IF_CONTENT_LEVEL) ;
      
      return sb.toString() ;
      
}

public String getPostfix() {
   StringBuffer sb = new StringBuffer() ;	
	
   int level = 0;
	
   // TODO
   // catch
   if (fGenerateTryCatch){
	   if (fisThisMethod)
	     level = CodeTemplateHelper.NORMAL_METHOD_CONTENT_LEVEL ;
	   else
	     level=CodeTemplateHelper.NORMAL_IF_CONTENT_LEVEL ;
	   punchLine(sb,BlockTemplate.RBRACE,level) ;
	   punchLine(sb,TryTemplate.CATCH+" (java.lang.Throwable e) {",level) ; //$NON-NLS-1$
	   punchLine(sb,"//  Do Something",level+1) ; //$NON-NLS-1$
	   punchLine(sb,BlockTemplate.RBRACE,level) ;
   }
   
   // If
   if (!fisThisMethod) {
     punchLine(sb,BlockTemplate.RBRACE,CodeTemplateHelper.NORMAL_METHOD_CONTENT_LEVEL) ;
     punchLine(sb,RETURN+ExpressionTemplate.SPACE+fBeanName+ExpressionTemplate.SEMICOL, CodeTemplateHelper.NORMAL_METHOD_CONTENT_LEVEL) ;
   }
   punchLine(sb,BlockTemplate.RBRACE,CodeTemplateHelper.NORMAL_METHOD_DEF_LEVEL) ;  
   return sb.toString() ; 	   	
}
      
}	
	

