package org.eclipse.ve.internal.java.codegen.model;
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
 *  $RCSfile: CodeExpressionRef.java,v $
 *  $Revision: 1.17 $  $Date: 2004-04-01 21:35:50 $ 
 */


import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class CodeExpressionRef extends AbstractCodeRef {


protected   Statement				fexpStmt			= null ;
protected   CodeMethodRef			fMethod			= null ;
protected   IExpressionDecoder		fDecoder		= null ;   
protected   BeanPart				fBean			= null ;
protected   Object[]				fArguments		= null ;   // Some expression involve other components
private     int						fInternalState	= 0 ;
protected   ExpressionParser		fContentParser	= null ; 
protected 	Object					fPriority		= null;      
protected   CodeExpressionRef		fShadowExp		= null;


/*********
   The following are various states the expression can be at
*********/ 
 

    //  Expression exists in the BDM model
public final static int                STATE_EXIST				= 0x001 ;
    //  Source / Model are in sync
public final static int                STATE_IN_SYNC			= 0x002 ;
    //  Valid source exists with no VE model (no decoder) e.g., bean foo = new bean() 
public final static int                STATE_NO_MODEL			= 0x004 ;
//  Valid source exists with no VE model (no decoder) e.g., bean foo = new bean() 
public final static int                STATE_NO_SRC		     	= 0x008 ;
//  Expression is being deleted
public final static int                STATE_DELETE     		= 0x010 ;
    //  Referred to an implicit usage. 
public final static int                STATE_IMPLICIT	     	= 0x020 ;
    //  In progress of updating the source
public final static int                STATE_UPDATING_SOURCE   	= 0x040 ; 
    //  This expression relative position in the source can not be changed.
public final static int                STATE_SRC_LOC_FIXED     	= 0x080 ;
    //  Expression in src. can not be parsed at this time, but is in the VE model
public final static int                STATE_EXP_IN_LIMBO      	= 0x100 ;
   //   Expression is not persisted in source code.
public final static int                STATE_EXP_NOT_PERSISTED 	= 0x200 ;
  // Typically set for a Delta BDM
public final static int                STATE_SHADOW            	= 0x400 ;
  // Typically set for an allocation statement that initialize the expression.
public final static int                STATE_INIT_EXPR         	= 0x800 ;  

/*******/
	

public CodeExpressionRef (Statement exp, CodeMethodRef method, BeanPart bean) {
    
    

    setOffset(-1) ;
    setBean(bean)	;
	
}
/**
 * exp/method may not have been generated from the same source
 * if not, offset will have to be provided to normalize these two
 * @param exp
 * @param method
 * 
 * @since 1.0.0
 */
public CodeExpressionRef (Statement exp, CodeMethodRef method, int delta) {	
	super(-1,-1,null) ;

    int start, end ;
//    if (exp instanceof VariableDeclarationExpression) {
//        // Local Declaration is built out of 3 expressions
//        start = ((VariableDeclarationExpression)exp).declarationSourceStart+delta ;
//        end = ((VariableDeclarationExpression)exp).declarationSourceEnd+delta ;
//    }
//    else {
        start = exp.getStartPosition()+delta ;
        end = exp.getStartPosition()+exp.getLength()+delta ;
        String fromSource = method.getContent().substring(start-method.getOffset(), end-method.getOffset());
        end = ExpressionParser.indexOfLastSemiColon(fromSource)-1+start;
//    }
        
	ExpressionParser fCP = createExpressionParser(method.getContent(),
	                                      start-method.getOffset(),
	                                      end-start+1) ;

    fMethod = method ;
    fexpStmt = exp ;
	setState(STATE_SRC_LOC_FIXED, true);   
	setContent(fCP) ;
	setOffset(getContentParser().getFillerOff()) ;
	// Expression's content will include its Filler and comments	                           
}

public CodeExpressionRef (Statement exp, CodeMethodRef method) {
	this(exp,method,0) ;
}

public CodeExpressionRef (CodeMethodRef method,BeanPart bean) {	
	super(-1,-1,null) ;	        
	fMethod = method ;
	fexpStmt = null ;
	setBean(bean) ;
}

public CodeMethodRef getMethod()  {
	return fMethod ;
}


public Object getPriority(){
	return fPriority;
}

public String getCodeContent() {
	if (fContentParser != null) 
	   return fContentParser.getCode() ;
	else
	   return null ;
}
 
public void setCodeContent(String code) throws CodeGenException {
	if (fContentParser == null) throw new CodeGenException ("No ExpressionParser") ;	 //$NON-NLS-1$
      fContentParser.replaceCode(code) ;
	setContent(fContentParser) ;
}

public void setCommentContent(String comment) throws CodeGenException {
	if (fContentParser == null) throw new CodeGenException ("No ExpressionParser") ;	 //$NON-NLS-1$
	fContentParser.replaceComments(comment) ;
	setContent(fContentParser) ;	
}

public void setFillerContent(String filler) throws CodeGenException {
	if (fContentParser == null) throw new CodeGenException ("No ExpressionParser") ;	 //$NON-NLS-1$
	fContentParser.replaceFiller(filler) ;
	setContent(fContentParser) ;	
}



public String getFillerContent() {
	if (fContentParser != null) 
	   return fContentParser.getFiller() ;
	else
	   return null ;	
}

public String getCommentsContent() {
	if (fContentParser != null) 
	   return fContentParser.getComment() ;
	else
	   return null ;	
	
}
/**
 * @param parser  which parsed the expression within the method
 */
public void setContent (ExpressionParser parser) {
	fContentParser = parser ;
	if (parser != null) {
	  super.setContent(fContentParser.getExpression()) ; 	  
	}
	else {
	  super.setContent(null) ;
	}
	// fMethod.updateExpressionOrder();
}

public ExpressionParser getParser () {
	return fContentParser ;
}

public void setContent (String content)  {
	CodeGenException e = new CodeGenException("Use an ExpressionParser to set the content") ; //$NON-NLS-1$
	JavaVEPlugin.log(e, Level.WARNING) ;
}




 /**
 *  Find a decoder for this expression
 *  Temporary until we properly set up the decoder factory
 */
protected IExpressionDecoder  getExpressionDecoder(){
	
	if (fDecoder != null) return fDecoder ;
	
	fDecoder = CodeGenUtil.getDecoderFactory(fBean.getModel()).getExpDecoder((IJavaInstance)fBean.getEObject()) ;
	// Factory new the object using the default constructor
    fDecoder.setBeanModel(fBean.getModel()) ;
    fDecoder.setBeanPart(fBean) ;
    fDecoder.setCompositionModel(fBean.getModel().getCompositionModel()) ;
    try {
		fDecoder.setExpression(this) ;
	}
	catch (CodeGenException e) {
		JavaVEPlugin.log(e) ;
	}
    
    return fDecoder ;
 
}


/**
 * @return true if there is already equivalent expression
 */
protected boolean isDuplicate() {
	boolean result = false ;
	
	// TODO Need a rule to descide this
	Iterator itr = fBean.getRefExpressions().iterator() ;
	while (itr.hasNext()) {
		CodeExpressionRef e = (CodeExpressionRef) itr.next();
		if (e == this) {
			return false ;
		}
		try {
		 if (e.isEquivalent(this)>=0) 
		     return true ;
		}
		catch (CodeGenException exp) {
			JavaVEPlugin.log("CodeExpressionRef.isDuplicate():  can not determine: "+this) ;	 //$NON-NLS-1$
		}
	}	
	return result ;
}

/**
 *  Decode this. expression 
 */
public  boolean  decodeExpression() throws CodeGenException {
    
      // If it is already in MOF, no need to create it again.
      if ((!isAnyStateSet()) || isStateSet(STATE_DELETE)) // ((fState&~STATE_SRC_LOC_FIXED) != STATE_NOT_EXISTANT) 
      	return true ;
      
      if(isStateSet(STATE_NO_MODEL))
      	return true;
      
      // Do not decode
      if (fBean.getInitMethod() == null || !fBean.getInitMethod().equals(fMethod)) {
      	JavaVEPlugin.log("CodeExpressionRef.decodeExpression(): Invalid init JCMMethod for"+fBean,Level.FINE) ; //$NON-NLS-1$      	
      	return false ;
      }
      
      if (isDuplicate()) {
      	JavaVEPlugin.log("CodeExpressionRef.decodeExpression(): Duplicate Expression"+fexpStmt, Level.FINE) ; //$NON-NLS-1$
      	return false ;
      }
    
      // TODO Need to be able and work with a IProgressMonitor"
      if(getExpressionDecoder()!=null) // this may not aloways have a decoder (snippet)
	      return getExpressionDecoder().decode() ; 
	else
		return false;
}

protected ExpressionParser createExpressionParser(String sourceSnippet, int expOffset, int expLen) {
   return new ExpressionParser(sourceSnippet,expOffset, expLen) ;
}

protected String removeWhiteSpace(String s) {
	int i = 0 ;
	for (;i<s.length();i++) {
		if (!Character.isWhitespace(s.charAt(i)))
			break ;
	}
	return s.substring(i) ;
}
 
/**
 *  Generate this. expression 
 */
public  String  generateSource(EStructuralFeature sf) throws CodeGenException {
    
      if (!isStateSet(STATE_EXIST)) //((fState|STATE_EXIST)==0) 
      	return null ;
    
      // TODO Need to be able and work with a IProgressMonitor
      String result = getExpressionDecoder().generate(sf,fArguments) ; 
      if (result == null) return result ;
      
      result = removeWhiteSpace(result);
      // The jdt parser does not include the ";" or comments as part of the expression.
      String e = ExpressionTemplate.getExpression(result) ;
      ExpressionParser p = createExpressionParser(BeanMethodTemplate.getInitExprFiller()+result,
                                                BeanMethodTemplate.getInitExprFiller().length(),
                                                e.length()) ;
      setContent(p) ;
      setOffset(-1) ;
      setFillerContent(BeanMethodTemplate.getInitExprFiller());
      try {
	     refreshAST();
	     getExpDecoder().setStatement(fexpStmt);
      }
      catch (Exception e1) {
//      	JavaVEPlugin.log(e1) ;
      }
      return result ;
}



/**
 *  setters/getters
 */
public void setBean(BeanPart bean) {
	fBean = bean ;
	fMethod.addExpressionRef(this) ;
	bean.addRefExpression(this) ;
}
public BeanPart getBean() {
	return fBean ;
}

public Statement getExprStmt() {
	return fexpStmt ;
}


public void setDecoder(IExpressionDecoder decoder) {
	if (fDecoder == decoder) return  ;
	
    if (fDecoder != null) fDecoder.dispose() ;
	fDecoder = decoder ;
}



public  IExpressionDecoder getExpDecoder() {
	if (fDecoder != null) return fDecoder ;
	if (!isAnyStateSet() || isStateSet(STATE_DELETE) ||
	    isStateSet(STATE_NO_MODEL))
    return null ;
// Temporary sanity check
if (this instanceof CodeEventRef)
   throw new RuntimeException("EventRef calling getExpDecoder") ; //$NON-NLS-1$
       
    return getExpressionDecoder() ;   
}

public  void refreshFromComposition() throws CodeGenException {
	
	if ((!isAnyStateSet()) ||  //((fState&~STATE_UPDATING_SOURCE) == STATE_NOT_EXISTANT) {
	    (isStateSet(STATE_DELETE))){
		   // Clear the expression's content
		   boolean isSrc = isStateSet(STATE_NO_SRC);
		   clearState();
		   setState(STATE_DELETE, true) ;
		   setState(STATE_NO_SRC,isSrc);
		   setContent((ExpressionParser) null) ;
		   return ;
	    }

	if(isStateSet(STATE_NO_MODEL))     //((fState & STATE_NO_OP) > 0)
	    return ;	
	
	if (fDecoder == null) throw new CodeGenException ("No Decoder") ; //$NON-NLS-1$
	
	if (fDecoder.isImplicit(fArguments))
	    return ;	
	if (fDecoder.isDeleted()) {
		boolean isSrc = isStateSet(STATE_NO_SRC);
		clearState();
		setState(STATE_DELETE, true) ;
		setState(STATE_NO_SRC,isSrc);
		setContent((ExpressionParser) null) ;
		return ;
	}
	String curContent = ExpressionTemplate.getExpression(fDecoder.reflectExpression(getCodeContent())) ;
	if (!curContent.equals(getCodeContent())) {	
	  setCodeContent(curContent) ;
	  setOffset(-1) ;		
	}	  
	setState(STATE_IN_SYNC, true);  // fState |= STATE_IN_SYNC|STATE_EXIST ;
	setState(STATE_EXIST, true);
}


// Show the expression, and the location in the document they point to
public String _debugExpressions() {    
    return getMethod()._debugExpressions() ;
}

/**
 * If the exp was parsed at all, than do not mark it in limbo in the model, so that
 * we can fix it from the model (PS)
 * 
 * Comment(Sri)06/14/2002: Commented ALL lines (except last line). Dont know
 *                         WHY a limbo state of ExpressionRef depends on its
 *                         JDOM Expression????
 */
public  void updateLimboState (CodeExpressionRef exp) {
	setState(STATE_EXP_IN_LIMBO, exp.isStateSet(STATE_EXP_IN_LIMBO));
}

/**
 * This has to update the following:
 *  o Update the MOF model if a decoder is present.
 *  o Update the local working copy to reflect the text change.
 */
public  void refreshFromJOM(CodeExpressionRef exp){
	try{
		setState(STATE_UPDATING_SOURCE, true); //fState |= STATE_UPDATING_SOURCE ;
		
		
		int off = getOffset() ;
		
		
		// extract new changes
		setContent(exp.getParser()) ;
        updateLimboState(exp) ;
        if (exp.getExprStmt() != null)
		   setExprStmt(exp.getExprStmt());
		
		// Update the VCE model
		if((!isStateSet(STATE_EXP_IN_LIMBO)) && (!isStateSet(STATE_NO_MODEL))) { //((fState&STATE_EXP_IN_LIMBO)==0 && (fState&STATE_NO_OP)==0){
			// Let the decoder refresh its JDOM Expression
			primGetDecoder().setExpression(this);
			// TODO This assumes that there is NO preveious value
			try {
			  primGetDecoder().decode();		
			}
			catch (Throwable t) {
				JavaVEPlugin.log(t) ;
			}
		}
	    setOffset(off) ;
	    
	    // Model is in update mode already here
	    CodeGenUtil.snoozeAlarm(fBean.getEObject(),fBean.getModel().getCompositionModel().getModelResourceSet()) ;
	    
	    
	}catch(Exception e){
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	finally {
	   setState(STATE_UPDATING_SOURCE, false); // fState &= ~STATE_UPDATING_SOURCE ;				   	 
	}
}


public  void updateDocument(ExpressionParser newParser) {
	if (isStateSet(STATE_NO_SRC)) return ;
	synchronized (fBean.getModel().getDocumentLock()) {
		setState(STATE_UPDATING_SOURCE, true); //fState |= STATE_UPDATING_SOURCE ;
		int off = getOffset();
		int len = getLen();

		setContent(newParser) ;

		getMethod().refreshIMethod();
		int docOff = off + getMethod().getOffset();
		String newContent = getContent(); //(fState&~STATE_UPDATING_SOURCE) == STATE_NOT_EXISTANT ? "" : getContent()  //$NON-NLS-1$
		fBean.getModel().getDocumentBuffer().replace(docOff, len, newContent);

		setOffset(off);
		setState(STATE_UPDATING_SOURCE, false); 
		setState(STATE_EXP_NOT_PERSISTED,false);
	}
}

public  void updateDocument(boolean updateSharedDoc) {
	if(isStateSet(STATE_IN_SYNC))  
		return ;
     
          
	StringBuffer trace = new StringBuffer() ;
	trace.append("CodeExpressionRef.updateDocument():\n") ; //$NON-NLS-1$
	synchronized (fBean.getModel().getDocumentLock()) {
		setState(STATE_UPDATING_SOURCE, true); //fState |= STATE_UPDATING_SOURCE ;
		int off = getOffset() ;
		int len = getLen() ;
		String prevContent = getContent()==null ? "" : getContent() ;	  	   //$NON-NLS-1$
		trace.append(prevContent) ;	  
		try {	  
			refreshFromComposition() ;
		}catch (CodeGenException e) {
			JavaVEPlugin.log(e, Level.WARNING) ;
			setState(STATE_UPDATING_SOURCE, false); //fState &= ~STATE_UPDATING_SOURCE ;		
			return ;
		}
        
        boolean isExpInLimbo = false;
        boolean prevContentFoundInCode = false;
        
        if (updateSharedDoc && isStateSet(STATE_EXP_IN_LIMBO) && !isStateSet(STATE_NO_SRC)) {
            // updating an expression in Limbo, just re-create a good one for now.
            isExpInLimbo = true;
            if(getMethod().getContent().indexOf(prevContent)==off)
            	prevContentFoundInCode = true;
            if(!prevContentFoundInCode){
	            setOffset(off) ;
	            insertContentToDocument() ;
            }
	   	    setState(STATE_EXP_IN_LIMBO,false) ;
        }
        
		if(((!isExpInLimbo) || (isExpInLimbo && prevContentFoundInCode))&&
			 !isStateSet(STATE_NO_SRC)){
    		// Do we need to update the document ?
    		if (getContent() != null && prevContent.equals(getContent()))  {
    			JavaVEPlugin.log ("CodeExpressionRef.updateDocument() : No change - "+prevContent, Level.FINE) ; //$NON-NLS-1$
    			setState(STATE_UPDATING_SOURCE, false); //fState &= ~STATE_UPDATING_SOURCE ;		
    			return ;
    		}
	
    		int docOff = off+getMethod().getOffset() ;
    		String newContent = ((!isAnyStateSet()) || isStateSet(STATE_DELETE)) ? "" : getContent(); //(fState&~STATE_UPDATING_SOURCE) == STATE_NOT_EXISTANT ? "" : getContent()  //$NON-NLS-1$
    		trace.append("\t changed to: \n") ; //$NON-NLS-1$
    		trace.append(newContent+"\n") ;	       //$NON-NLS-1$
    		updateDocument(docOff,len,newContent) ;
        }
		setOffset(off) ;
		setState(STATE_UPDATING_SOURCE, false); //fState &= ~STATE_UPDATING_SOURCE ;
	}
	JavaVEPlugin.log(trace.toString(), Level.FINE) ;

	if ((!isAnyStateSet()) || isStateSet(STATE_DELETE)) { //(fState == STATE_NOT_EXISTANT) {
		// Expression was deleted
		dispose() ;
	}	
}

protected void updateDocument(int docOff, int len, String newContent) {
	IBeanDeclModel model = fBean.getModel() ;
	
		try {
			model.aboutTochangeDoc();
			model.getDocumentBuffer().replace(docOff,len,newContent) ;				
			model.driveExpressionChangedEvent(getMethod(), docOff, newContent.length()-len) ;		
			setState(STATE_EXP_NOT_PERSISTED,false);
        } catch (Exception e) {
			JavaVEPlugin.log(e) ;
		}	
}

public  void insertContentToDocument() {
	if (isStateSet(STATE_NO_SRC)) return ;
	JavaVEPlugin.log("CodeExpressionRef: creating:\n"+getContent()+"\n", Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	synchronized (fBean.getModel().getDocumentLock()) {
		// mark a controlled update (Top-Down)		
		setState(STATE_UPDATING_SOURCE, true); 
		int docOff = getOffset()+getMethod().getOffset() ;
		updateDocument(docOff, 0, getContent()) ;
		setState(STATE_UPDATING_SOURCE, false); 
	}
}

/**
 * In the case of a generated expresion (from a template), we need a ref. AST node
 * for snippet analysis
 * 
 * @since 1.0.0
 */
public void refreshAST() {
		
		final Statement[] s = new Statement[] { null } ;    
        ASTNode ast = AST.parse(AST.K_STATEMENTS, getContent().toCharArray(), 0, getContent().length(), null);
        ASTVisitor visitor = new ASTVisitor() {
			public void endVisit(ExpressionStatement node) {
				s[0] = node;			
			}
			public void endVisit(VariableDeclarationStatement node) {
				s[0] = node;
			}
		};
		ast.accept(visitor);
		
        if (s[0] ==null)
        	JavaVEPlugin.log("CodeExpressionRef.refreshAST(): could notrefresh "+getContent(),Level.WARNING);
        else
            fexpStmt = s[0];	
}



public boolean isStateSet(int state){
	return ((primGetState() & state) == state);
}
/**
 * @return true if this exprepression is a shadow expression of the argument.
 * Shadow expression is an expression that is cloned during plan B
 */
public boolean isShadowExpOf(CodeExpressionRef shadow){
	if(fShadowExp==null || shadow==null)
		return false;
	return fShadowExp.equals(shadow);
}

public boolean isAnyStateSet(){
	return primGetState() != 0;
}

/**
 * Has to determine if this expression is similar
 * to the expression given. 'similar' means having
 * the same argument types.
 * @return boolean -1 no, 0 same with some changes, 1 identical
 */
public int isEquivalent(AbstractCodeRef code) throws CodeGenException  {
	 
	if(code instanceof CodeExpressionRef && code.getClass().isAssignableFrom(CodeExpressionRef.class)){        
		CodeExpressionRef exp1 = (CodeExpressionRef)code;
		if(isShadowExpOf(exp1) || exp1.isShadowExpOf(this))	
			return 1;
        if (exp1.equals(this)) return 1 ;
        
		if (getBean() == null && exp1.getBean() != null) return -1 ;
		else
		  if (getBean() != null && exp1.getBean() == null) return -1 ;
		
		boolean beanNameEquivalency = getBean().getSimpleName().equals(exp1.getBean().getSimpleName());

//TODO: Need to deal with Local Declerations		
		String expc1 = exp1.getMethodNameContent();
		String expc2 = getMethodNameContent();
		boolean expEquivalency = expc1.equals(expc2);		
		
		if(beanNameEquivalency && expEquivalency){
		    if (isStateSet(STATE_NO_MODEL))
		       if (exp1.isStateSet(STATE_NO_MODEL)) {
		       	if (getCodeContent().equals(exp1.getCodeContent())) return 1 ;
		       	else return 0 ;
		       }
		       else return -1 ;
		    else
		       if (exp1.isStateSet(STATE_NO_MODEL)) return -1 ;

            // Need to have decoders for equivalency		       
		    if (getBean().getEObject() == null &&
		        exp1.getBean().getEObject() == null) throw new CodeGenException ("Can not determine equivalency") ; //$NON-NLS-1$
		    
		    boolean thisSetProxy = false ;
		    boolean exp1SetProcy = false ;
		      
		    if (getBean().getEObject() == null)  {
		    	thisSetProxy = true ;
		        if (getBean().isProxy()) throw new CodeGenException ("Proxy with no Ref Object") ; //$NON-NLS-1$
		    	getBean().setProxy(exp1.getBean()) ;
		    }
		    else if (exp1.getBean().getEObject() == null) {
		    	exp1SetProcy = true ;
		    	if (exp1.getBean().isProxy()) throw new CodeGenException ("Proxy with no Ref Object") ; //$NON-NLS-1$
		        exp1.getBean().setProxy(getBean()) ;
		    }
		        
		    
			try {                        
				Object[] h = getExpressionDecoder().getArgsHandles(getExprStmt())  ;				
				Object[] hc = exp1.getExpressionDecoder().getArgsHandles(exp1.getExprStmt())  ;				
				
				if (thisSetProxy)
				   getBean().setProxy(null) ;
				if (exp1SetProcy)
				  exp1.getBean().setProxy(null) ;	
					
				
				if (h == null && hc != null ||
				    h != null && hc == null) return -1 ;
				if (h==null && hc == null) {					 
				  if(getCodeContent().equals(exp1.getCodeContent()))
				   return 1; // Identical
			      else
				   return 0; // Same expression, but needs update
				}
				if (h.length != hc.length) return -1 ;
				for (int i=0; i<h.length; i++)
				  if (!h[i].equals(hc[i])) return -1 ;
				  
				if(getCodeContent().equals(exp1.getCodeContent()))
				   return 1; // Identical
			    else
				   return 0; // Same expression, but needs update
								
			}	
			catch (Exception e) {
				// It is possible that a feature is not available, but entered in the code
				JavaVEPlugin.log("CodeExpressionRef.isEquivalent: could not compare :\n\t"+this+"\n\t"+exp1) ; //$NON-NLS-1$ //$NON-NLS-2$
                return -1 ;
				
			}
		}
	}

	return -1;
}

/**
 * ExpressionVisitor handles only MessageSend and Assignment.
 */
public String getMethodNameContent(){
	if(fexpStmt!=null && fexpStmt instanceof ExpressionStatement){
		Expression e = ((ExpressionStatement)fexpStmt).getExpression();
		if (e instanceof MethodInvocation) {
			MethodInvocation msg = (MethodInvocation) e;
			return msg.getName().getIdentifier();
		}
		if (e instanceof Assignment) {
			Assignment assgn = (Assignment) e;
			if (assgn.getRightHandSide() instanceof ClassInstanceCreation) {
				ClassInstanceCreation alloc = (ClassInstanceCreation) assgn.getRightHandSide();
				String allocS = alloc.toString();
				return allocS.substring(0,allocS.indexOf(alloc.getName().toString())).trim();
			}
		}
	}
	if (fContentParser != null)
	  return fContentParser.getSelectorContent() ;
	else
	  return null ;
}


/**
 * A call to despose will delete this expression from the VE model if needed,
 * and will clean up.  It is assume that the document have been updated already
 * at this point by the decoder.
 */
public void dispose() {
	// A dispose will be called after a delete.
  
	if (!isStateSet(STATE_DELETE) && primGetDecoder() != null) {
		primGetDecoder().dispose();		
	}
	fDecoder = null ;
	clearState();	
	setState(STATE_DELETE, true) ;
	setContent((ExpressionParser) null) ;
	setProprity(null) ;
	if (fMethod != null)
	   fMethod.removeExpressionRef(this) ;		
	fMethod = null ;
	if (fBean != null) {
	   fBean.removeRefExpression(this) ;
	   fBean.removeNoSrcExpresion(this);
	}
	fBean = null ;	
}
public void setProprity(Object priority){
	fPriority = priority;
}
private void primSetState(int flag) {
    fInternalState = flag ;
}
public void clearState(){
	primSetState(0);
}


public void setStateFrom(CodeExpressionRef e){
	primSetState(e.primGetState());
}
public void setState(int flag, boolean state) {
	if(state)
		primSetState(primGetState() | flag);
	else
		primSetState(primGetState() & (~flag));
}
public String toString(){
	String states = "{ States: "; //$NON-NLS-1$
	if(isStateSet(STATE_EXIST))
		states = states.concat("EXIST#"); //$NON-NLS-1$
	if(isStateSet(STATE_EXP_IN_LIMBO))
		states = states.concat("INLIMBO#"); //$NON-NLS-1$
	if(isStateSet(STATE_IMPLICIT))
		states = states.concat("IMPLICIT#"); //$NON-NLS-1$
	if(isStateSet(STATE_IN_SYNC))
		states = states.concat("INSYNC#"); //$NON-NLS-1$	
	if(isStateSet(STATE_INIT_EXPR))
		states = states.concat("INIT#"); //$NON-NLS-1$			
	if(isStateSet(STATE_SRC_LOC_FIXED))
		states = states.concat("SRCLOCFIXED#"); //$NON-NLS-1$
	if(isStateSet(STATE_UPDATING_SOURCE))
		states = states.concat("UPDATINGSRC#"); //$NON-NLS-1$
    if(isStateSet(STATE_EXP_NOT_PERSISTED))
        states = states.concat("NOTPERSISTED#"); //$NON-NLS-1$    
    if(isStateSet(STATE_SHADOW))
        states = states.concat("SHADOW#"); //$NON-NLS-1$
    if(isStateSet(STATE_DELETE))
    	states = states.concat("DELETE#"); //$NON-NLS-1$    
    if (isStateSet(STATE_NO_SRC))
   	    states = states.concat("STATE_NO_SRC#"); //$NON-NLS-1$
   	if (isStateSet(STATE_NO_MODEL))
   		states = states.concat("STATE_NO_MODEL#"); //$NON-NLS-1$    
	states = states.concat("}"+" Offset: "+Integer.toString(getOffset())); //$NON-NLS-1$ //$NON-NLS-2$
	return super.toString() + states;
}

public int primGetState() {
	return fInternalState ;
}

public void setArguments(Object[] args) {
	fArguments = args ;
}
public Object[] getArgs() {
	return fArguments ;
}

public ExpressionParser getContentParser() {
	return fContentParser ;
}

public ICodeGenSourceRange getTargetSourceRange() {
    if (fBean == null || fBean.getModel() == null || fBean.getModel().isStateSet(IBeanDeclModel.BDM_STATE_DOWN)) return null ;
    ISourceRange mSR = fBean.getModel().getWorkingCopyProvider().getSourceRange(fMethod.getMethodHandle()) ;   
    if (mSR == null) return null ;
    CodeGenSourceRange result = new CodeGenSourceRange (mSR.getOffset()+getOffset(),getLen()) ;
    result.setLineOffset(fBean.getModel().getWorkingCopyProvider().getLineNo(result.getOffset())) ;
    return result ;
}

public void setSF(EStructuralFeature sf) {
    getExpDecoder() ;
    if (fDecoder !=null)	
	   fDecoder.setSF(sf) ;
}
private void setShadowExp(CodeExpressionRef exp){
	fShadowExp = exp;
}
public EStructuralFeature getSF() {
	getExpDecoder() ;
	if (fDecoder!=null)
		return fDecoder.getSF() ;
    else
        return null ;
}

public CodeExpressionRef createShadow(CodeMethodRef mr, BeanPart bp) {
   
    CodeExpressionRef shadow = new CodeExpressionRef (mr,bp) ;
    
    
    shadow.setStateFrom(this) ;
    shadow.setState(STATE_SHADOW, true) ;
    
    shadow.setExprStmt(getExprStmt()) ;
    
    // Delay the creation of a decoder.
    if (!bp.isProxy())
       bp.setProxy(fBean);
       
    shadow.setContent(getContentParser()) ;
    shadow.setProprity(fPriority) ;
    shadow.primSetState(primGetState()) ;
    shadow.setState(STATE_SHADOW, true) ;
    
    if (shadow.getExprStmt() == null)
        shadow.setSF(getSF()) ;
    
    setShadowExp(shadow);
    
    return shadow ;    
}

protected IJVEDecoder primGetDecoder() {
   return getExpressionDecoder() ;
}

/**
 * @param statement
 */
public void setExprStmt(Statement statement) {
	fexpStmt = statement;
}

public void setNoSrcExpression() {
	setState(STATE_NO_SRC,true) ;
	if (fBean!=null) {
	   fBean.removeRefExpression(this);
	   fBean.addNoSrcExpresion(this);
	}
	if (getMethod()!=null) {
	    getMethod().removeExpressionRef(this) ;
	    fMethod=null;
	}
}

public Object[] getAddedInstances() {
	if (fDecoder != null)
		 return fDecoder.getAddedInstance();
	return null ;	
}

}

