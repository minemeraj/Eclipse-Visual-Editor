package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: ExpressionRefFactory.java,v $
 *  $Revision: 1.16 $  $Date: 2004-05-17 20:28:01 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;





public class ExpressionRefFactory {

	final static String CLASS_PREFIX = "class Foo {\n" ;  //$NON-NLS-1$
    BeanPart             fBeanPart ;
    EStructuralFeature fSF ;
    CodeExpressionRef    fExpr = null ;
    
   public  ExpressionRefFactory (BeanPart bean, EStructuralFeature sf) {
   	fBeanPart = bean ;
   	fSF = sf ;
   }
   

public void insertContentToDocument() throws CodeGenException {
    if (fExpr == null) throw new CodeGenException("Invalid Context") ;  //$NON-NLS-1$
    
    if (fExpr.getMethod().getMethodHandle() != null)
       fExpr.insertContentToDocument() ;
    else
       fExpr.setState(CodeExpressionRef.STATE_EXP_NOT_PERSISTED, true) ;
       
}   

/**
 * Generate a new initialization expression
 * @param constructorArgs arguments if any, or null
 * @return Init Expression
 * 
 * @since 1.0.0
 */
public CodeExpressionRef createInitExpression() {
	CodeMethodRef mr = fBeanPart.getInitMethod() ;   	
	CodeExpressionRef exp = new CodeExpressionRef(mr,fBeanPart) ;
	exp.clearState();
	exp.setState(CodeExpressionRef.STATE_EXIST, true);

	exp.setState(CodeExpressionRef.STATE_INIT_EXPR, true) ;
	exp.setState(CodeExpressionRef.STATE_IN_SYNC, true);
	exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true);
	    
	IJavaObjectInstance obj = (IJavaObjectInstance)fBeanPart.getEObject();
	InitExpressionGenerator gen = new InitExpressionGenerator(fBeanPart.getEObject(),fBeanPart.getModel());
	gen.setInitbeanName(fBeanPart.getSimpleName());
	gen.setInitbeanConstructionString(CodeGenUtil.getInitString(obj,fBeanPart.getModel()));
		   			
	String content = gen.generate();

	// Since inner variables also need to have an annotation now (VariableRule.ignoreVariable),
	// we need to generate on and add it to the init expression.
	try {
		String comment = fBeanPart.getFFDecoder().generate(null, null);
		if(comment!=null && comment.length()>0){
			comment = FreeFormAnnotationTemplate.ANNOTATION_PREFIX + comment;
			if(content.indexOf(';')>-1){
				content = content.substring(0, content.indexOf(';')+1) + comment + content.substring(content.indexOf(';')+1); 
			}else{
	    		content += comment;
			}
		}
	} catch (CodeGenException e) {
		JavaVEPlugin.log(e, Level.WARNING);
	}
	
	String classContent = classWrapper(content, true);
	Statement Stmt = getInitExpression(classContent);
	int contentStart = classContent.indexOf(content);
	int start = Stmt.getStartPosition() - contentStart;
	int end = ExpressionParser.indexOfLastSemiColon(classContent.substring(Stmt.getStartPosition(), Stmt.getStartPosition()+Stmt.getLength())) - 1 + Stmt.getStartPosition() - contentStart;
	int len = end - start + 1;
	
	ExpressionParser p = new ExpressionParser (content, start, len);
	
	exp.setContent(p) ;
	exp.setExprStmt(Stmt);
	exp.setOffset(p.getExpressionOff()) ;   	
	fExpr = exp ;
	return fExpr ;
	
}

///**
// * @deprecated temporary untill we deal with internal bean creation
// * @param mt
// * @return
// * 
// * @since 1.0.0
// */
//public CodeExpressionRef createInitExpression(BeanMethodTemplate mt) {
//	CodeMethodRef mr = fBeanPart.getInitMethod() ;   	
//	CodeExpressionRef exp = new CodeExpressionRef(mr,fBeanPart) ;
//	//exp.setState(exp.STATE_EXIST|exp.STATE_NO_OP|exp.STATE_IN_SYNC|exp.STATE_SRC_LOC_FIXED) ;
//	exp.clearState();
//	exp.setState(CodeExpressionRef.STATE_EXIST, true);
//	exp.setState(CodeExpressionRef.STATE_NO_MODEL, true);
//	exp.setState(CodeExpressionRef.STATE_INIT_EXPR, true) ;
//	exp.setState(CodeExpressionRef.STATE_IN_SYNC, true);
//	exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true);
//	ExpressionParser p = new ExpressionParser (mt.getPrefix(),mt.getInitExpressionOffset(),
//			mt.getInitExpression().length()) ;
//	
//	exp.setContent(p) ;
//	exp.setOffset(p.getExpressionOff()) ;   	
//	fexpStmt = exp ;
//	return fexpStmt ;
//}

/**
 *  Create a CodeExpressionRef from the VCE model
 */   
public CodeExpressionRef createFromJVEModel(Object[] args) throws CodeGenException {
   	
      	if (fExpr != null)
			return fExpr;
		if (getExistingExpressionRef(args) != null)
			throw new CodeGenException("Expression already exists"); //$NON-NLS-1$

		CodeMethodRef mr = fBeanPart.getInitMethod();
		CodeExpressionRef exp = new CodeExpressionRef(mr, fBeanPart);
		exp.setArguments(args);
		//exp.setState(exp.STATE_EXIST) ;
		exp.clearState();
		exp.setState(CodeExpressionRef.STATE_EXIST, true);
		exp.generateSource(fSF);
		if ((!exp.isAnyStateSet()) || 
				exp.isStateSet(CodeExpressionRef.STATE_NO_SRC)) 
			return exp ;

		fExpr = exp;
		try {
			mr.updateExpressionOrder();
		} catch (Throwable e) {
			JavaVEPlugin.log(e, Level.WARNING);
			Iterator itr = mr.getExpressions();
			CodeExpressionRef prev = null;

			while (itr.hasNext()) {
				CodeExpressionRef x = (CodeExpressionRef) itr.next();

				if (x.equals(exp))
					break;
				prev = x;
			}
			if (prev == null)
				prev = (CodeExpressionRef) itr.next();
			exp.setOffset(prev.getOffset() + prev.getLen());

			// TODO Remove this try{}catch{} block : CDEHack.fixMe("remove this try") ;
		}
		return exp;
	}
   
      
   
   
/**
 *  Look for an existing Expression in the BDM
 */
public CodeExpressionRef getExistingExpressionRef(Object[] args) {
   	if (fExpr != null)  return fExpr ;
   		
	Iterator itr = fBeanPart.getRefExpressions().iterator() ;
	while (itr.hasNext()) {
		CodeExpressionRef exp = (CodeExpressionRef) itr.next() ;
		if (exp.getExpDecoder() != null &&
		    fSF.equals(exp.getExpDecoder().getSF()) &&
		    ((exp.getArgs() == null && args==null) ||
		      exp.getArgs() != null && exp.getArgs().equals(args))) {
		    	fExpr = exp ;
		    	break ;
		}
	}
	return fExpr ;
}


/**
 * Assume that src. is for a new method, with only a single init expression
 * @param src content of a newly created method
 * @return
 * 
 * @since 1.0.0
 */
private Statement getInitExpression(String src) {
		
	ASTParser parser = ASTParser.newParser(AST.LEVEL_2_0);
	parser.setSource(src.toCharArray());
	CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	Statement Stmt = null;	
//		//TODO: this is a poor man's parsing, need revisit    
    List stmts =  ((TypeDeclaration)cu.types().get(0)).getMethods()[0].getBody().statements();
    if (stmts.size()>0) {
    	Statement s = (Statement) stmts.get(0);
    	if (s instanceof VariableDeclarationStatement ||
    	    (s instanceof ExpressionStatement &&
    	     ((ExpressionStatement)s).getExpression() instanceof Assignment)) {    	     
    	       Stmt = s;
    	}
    	else if (s instanceof IfStatement) {
    		Statement ts = ((IfStatement)s).getThenStatement();
    		if (ts instanceof Block) {
    			Stmt = (Statement) ((Block)ts).statements().get(0);
    			if (Stmt instanceof TryStatement)
    			   Stmt = (Statement) ((TryStatement)Stmt).getBody().statements().get(0);
    		}
    		
    	}    	      
    }
	return (Statement)Stmt ;    
//		org.eclipse.jdt.internal.compiler.ast.Statement st[] = cudecl.types[0].methods[1].statements ;
//		if (st != null) {
//			if (st[0] instanceof org.eclipse.jdt.internal.compiler.ast.LocalDeclaration
//				|| st[0] instanceof org.eclipse.jdt.internal.compiler.ast.Assignment)
//				Stmt = cudecl.types[0].methods[1].statements[0];
//			else if (st[0] instanceof org.eclipse.jdt.internal.compiler.ast.IfStatement) {
//				org.eclipse.jdt.internal.compiler.ast.Statement as =
//					((org.eclipse.jdt.internal.compiler.ast.IfStatement) st[0]).thenStatement;
//				if (as instanceof org.eclipse.jdt.internal.compiler.ast.Block) {
//					Stmt = ((org.eclipse.jdt.internal.compiler.ast.Block) as).statements[0];
//					if (Stmt instanceof org.eclipse.jdt.internal.compiler.ast.TryStatement)
//						Stmt = ((org.eclipse.jdt.internal.compiler.ast.TryStatement)Stmt).tryBlock.statements[0];
//				}
//			}
//		}
}

protected String classWrapper(String content, boolean generateMethod) {
	StringBuffer sb = new StringBuffer();
	sb.append(CLASS_PREFIX) ;
	if (generateMethod)		
		sb.append("void method() {\n");//$NON-NLS-1$
	sb.append(content) ;
	if (generateMethod)
		sb.append("}"); //$NON-NLS-1$
	sb.append("\n}") ; //$NON-NLS-1$
	return sb.toString();	
}

protected Statement parseMethodForInitExpr (String method) {
	return getInitExpression(classWrapper(method,false)) ;
}

/**
 * 
 * this method will parse a method to get its init expression (new Foo())
 * The assumption is that the init metod was just created with a single 
 * expression, the init expression
 * 
 * @since 1.0.0
 */
public CodeExpressionRef parseInitExpression() {
	
	CodeMethodRef mr = fBeanPart.getInitMethod() ;
	Statement s = parseMethodForInitExpr(mr.getContent());
	CodeExpressionRef exp = new CodeExpressionRef(s,mr,mr.getOffset()-CLASS_PREFIX.length()) ;
	//exp.setState(exp.STATE_EXIST|exp.STATE_NO_OP|exp.STATE_IN_SYNC|exp.STATE_SRC_LOC_FIXED) ;
	exp.clearState();
	exp.setState(CodeExpressionRef.STATE_EXIST, true);	
	exp.setState(CodeExpressionRef.STATE_INIT_EXPR, true) ;
	exp.setState(CodeExpressionRef.STATE_IN_SYNC, true);
	exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true);
	
	// add it into the BDM
	exp.setBean(fBeanPart) ;
	fExpr = exp ;
	
	return fExpr ;		
}



/**
 *  Create a new Expression from an existing shadow one (e.g., delta expression)
 *  @arg exp is the shadow expression
 *  @arg method the method the new expression is to reside in
 *  @arg b is the bean part the expression is acting on
 */
public CodeExpressionRef createFromSource(CodeExpressionRef exp, CodeMethodRef method) {
	fExpr = new CodeExpressionRef (method,fBeanPart) ;	
   	fExpr.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true); //fexpStmt.getState() | fexpStmt.STATE_SRC_LOC_FIXED) ;
   	fExpr.setExprStmt(exp.getExprStmt()) ;
   	fExpr.setContent(exp.getContentParser()) ;
   	fExpr.setOffset(exp.getOffset()) ;
   	return fExpr ;
}

public static CodeExpressionRef createShadowExpression(String content, int SrcOffset, int ExpOffset, int len, CodeMethodRef method, BeanPart b) {
	CodeExpressionRef exp = new CodeExpressionRef (method,b) ;	
	//exp.setState(exp.STATE_EXIST) ;
	exp.clearState();
	exp.setState(CodeExpressionRef.STATE_EXIST, true);
	exp.setContent(new ExpressionParser(content,SrcOffset,len)) ;
	exp.setOffset(ExpOffset) ;
	return exp ;
}   


protected static CodeGenSourceRange computeSR(MethodDeclaration m, ASTNode node, IMethod im) throws JavaModelException{
	       String src = im.getCompilationUnit().getSource();
	       int realMethodStart = m.getStartPosition();
	       try{
	       		String methodSource = im.getSource();
	       		realMethodStart = src.indexOf(methodSource);
	       }catch(JavaModelException e){
	       	 realMethodStart = m.getStartPosition();
	       }
	       int start = node.getStartPosition()  ;
           // Get the filler
           int filler=0 ;
           for (int i=start-1; i>=0 && src.charAt(i) != '\n'; i--)  {
               if (src.charAt(i) == '\t') filler+=4 ;
               else filler++ ;
           }
           // Advance to the next line
           String content = src.substring(start,start+node.getLength()) ;
           start+=content.indexOf('\n')+1 ;
           start -= realMethodStart;
           return new CodeGenSourceRange(start,filler) ;
}

/**
 * @return a source range, where offset is the exp. offset from the method,
 *          length is the Filler's length.
 */
public static ICodeGenSourceRange getOffsetForFirstExpression (IMethod m) {
    
    
    CodeGenSourceRange sr=null ; 
    try {
        // No need to resolve anyting, just parse this thing
    	ASTParser parser = ASTParser.newParser(AST.LEVEL_2_0);
    	parser.setSource(m.getCompilationUnit());
    	CompilationUnit jDom = (CompilationUnit) parser.createAST(null);
		Message[] errors = jDom.getMessages() ;
		if (errors != null && errors.length > 0) {
		    // TODO  Handle the case when errors are present in AST - should we give off,len pair ?
		}
	    if (jDom.types().size() > 0) {
	        TypeDeclaration t = (TypeDeclaration) jDom.types().get(0) ;
	        MethodDeclaration[] methods = t.getMethods() ;
	        MethodDeclaration method = null ;
	        if (methods != null && methods.length>0) {
        	    for (int i = 0; i < methods.length; i++) {
        	        String name = methods[i].getName().getIdentifier() ;
        	        String rType = methods[i].getReturnType().toString() ;
        	        if (methods[i].getReturnType().isPrimitiveType()) {
        	            rType = ((PrimitiveType)methods[i].getReturnType()).getPrimitiveTypeCode().toString() ;
        	        }
                    int args = methods[i].parameters() == null ? 0 : methods[i].parameters().size() ;      	        
        	        if (name == null || rType == null) continue ;
        	        if (name.equals(m.getElementName()) &&
        	            rType.equals(Signature.toString(m.getReturnType())) &&
        	            m.getNumberOfParameters() == args) {            
        	            method = methods[i] ;
        	            break ;
        	        } 
        	    }      
	            
	        }
	        ASTNode node = null ;
	        if (method != null) {
	            if (method.getBody() != null) {
	                if (method.getBody().statements() != null &&
	                    method.getBody().statements().size()>0 ) {
	                       Statement s = (Statement) method.getBody().statements().get(0) ;
	                       if (s instanceof TryStatement) {
	                           if (((TryStatement)s).getBody() != null) {
	                               node = ((TryStatement)s).getBody() ;
	                           }
	                           else {
	                               // No try body
	                               node = s ;
	                           }
	                       }
	                       else {
	                           // Statements but not a try
	                           node = s ;
	                       }
	                }
	                else {
	                    // No Statements - insert into the body.
	                    int start = method.getBody().getStartPosition();
	                    String src = m.getCompilationUnit().getSource();
	                    int realStart = src.indexOf('\n',start);
	                    if(realStart>=(method.getBody().getStartPosition()+method.getBody().getLength())){
	                    	realStart = start;
	                    }else{
	                    	if(realStart==-1){
	                    		realStart = start;
	                    	}
	                    }
	                    realStart++;
	                    
	                    // AST and JDT have a difference of opinion of where the method starts.
	                    // AST thinks that the last comment before the method belongs to the method,
	                    // while JDT thinks ALL comments before a method belong to the method.
	                    // SO the difference should be taken into account...
	                    int differenceInASTandJDT = m.getSourceRange().getOffset()-method.getStartPosition();
	                    
	                    sr = new CodeGenSourceRange(
	                    		realStart - method.getStartPosition() - differenceInASTandJDT, 
	                    		CodeTemplateHelper.getCharLength(CodeTemplateHelper.getFillerForLevel(CodeTemplateHelper.NORMAL_METHOD_CONTENT_LEVEL)));
	                    node = null;
	                }
	            }
	            if (node != null)
	               sr = computeSR(method,node,m) ;
	        }
	    }	    
	} catch(Throwable t) {}
	return sr ;
    

}
   
   

}