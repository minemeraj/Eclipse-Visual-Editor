/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.model;
/*
 *  $RCSfile: CodeExpressionRef.java,v $
 *  $Revision: 1.58 $  $Date: 2005-08-23 17:16:21 $ 
 */


import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
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
protected 	VEexpressionPriority	fPriority		= null;      
protected   CodeExpressionRef		fMasteredExpression = null;  // STATE_NO_SRC expressions may have a master expression
protected   List					freqImports = new ArrayList();  // Imports required
protected 	List fReferences = new ArrayList() ; // References to other BeanBarts
protected List fSameLineExpressions = new ArrayList(); // List of expressions in the same line - STATE_SHARED_LINE


/*********
   The following are various states the expression can be at
*********/ 
 

    //  Expression exists in the BDM model
public final static int                STATE_EXIST				= 0x0001 ;
    //  Source / Model are in sync
public final static int                STATE_IN_SYNC			= 0x0002 ;
    //  Valid source exists with no VE model (no decoder) e.g., bean foo = new bean() 
public final static int                STATE_NO_MODEL			= 0x0004 ;
//  Valid source exists with no VE model (no decoder) e.g., bean foo = new bean() 
public final static int                STATE_NO_SRC		     	= 0x0008 ;
//  Expression is being deleted
public final static int                STATE_DELETE     		= 0x0010 ;
    //  Referred to an implicit usage. 
public final static int                STATE_IMPLICIT	     	= 0x0020 ;
    //  In progress of updating the source
public final static int                STATE_UPDATING_SOURCE   	= 0x0040 ; 
    //  This expression relative position in the source can not be changed.
public final static int                STATE_SRC_LOC_FIXED     	= 0x0080 ;
    //  Expression in src. can not be parsed at this time, but is in the VE model
public final static int                STATE_EXP_IN_LIMBO      	= 0x0100 ;
   //   Expression is not persisted in source code.
public final static int                STATE_EXP_NOT_PERSISTED 	= 0x0200 ;
  // A Master expression, drives the existance of another expression. 
public final static int                STATE_MASTER            	= 0x0400 ;
// A Master expression, drives the existance of another expression. 
public final static int                STATE_MASTER_DELETED     = 0x0800 ;
// Typically set for an allocation statement that initialize the expression.
public final static int                STATE_INIT_EXPR         	= 0x1000 ;  
// Typically set when an expression is sharing the same line with other expressions
public final static int                STATE_SHARED_LINE         	= 0x2000 ;  
// Typically set when an expression is not inside a method, but needs to be an expression
public final static int                STATE_FIELD_EXP         	= 0x4000 ;

/*******/
	

/*******/
	

/**
 * Sometimes an expression might not belong to a method at all - like field decalrations.
 * 
 * @param exp	Field declaration wrapped inside a Statement
 * @param source	Source in which exp resides
 * @param bp  BeanPart to which this expression belongs
 * 
 * @since 1.1.0.1
 */
	public CodeExpressionRef(Statement exp, String source, BeanPart bp) {
		super(-1, -1, null);

		int start, end;
		start = exp.getStartPosition() ;
		end = exp.getStartPosition() + exp.getLength();
		String fromSource = source.substring(start, end);
		end = ExpressionParser.indexOfLastSemiColon(fromSource, bp.getModel()) - 1 + start;

		ExpressionParser fCP = createExpressionParser(source, start , end - start + 1, bp.getModel());

		fMethod = null;
		fexpStmt = exp;
		fBean = bp;
		fBean.addRefExpression(this);
		setState(STATE_SRC_LOC_FIXED, true);
		setContent(fCP);
		setOffset(getContentParser().getFillerOff());
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
        end = ExpressionParser.indexOfLastSemiColon(fromSource, method.getTypeRef().getBeanModel())-1+start;
//    }
        
	ExpressionParser fCP = createExpressionParser(method.getContent(),
	                                      start-method.getOffset(),
	                                      end-start+1, method.getTypeRef().getBeanModel()) ;

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


public VEexpressionPriority getPriority(){
	if (isStateSet(STATE_DELETE) || isStateSet(STATE_NO_MODEL))
		return IJavaFeatureMapper.NOPriority;
	
	return primGetDecoder().determinePriority();
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
			if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
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
      if ( (!isStateSet(CodeExpressionRef.STATE_FIELD_EXP)) &&
    		(fBean.getInitMethod() == null || !fBean.getInitMethod().equals(fMethod))) {
      	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
      		JavaVEPlugin.log("CodeExpressionRef.decodeExpression(): Invalid init JCMMethod for"+fBean,Level.FINE) ; //$NON-NLS-1$      	
      	return false ;
      }
      
      if (isDuplicate()) {
      	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
      		JavaVEPlugin.log("CodeExpressionRef.decodeExpression(): Duplicate Expression"+fexpStmt, Level.FINE) ; //$NON-NLS-1$
      	return false ;
      }
    
      freqImports.clear();
      fReferences.clear();
      // TODO Need to be able and work with a IProgressMonitor"
      if(getExpressionDecoder()!=null) // this may not aloways have a decoder (snippet)
	      return getExpressionDecoder().decode() ; 
	else
		return false;
}

protected ExpressionParser createExpressionParser(String sourceSnippet, int expOffset, int expLen, IScannerFactory scannerFactory) {
   return new ExpressionParser(sourceSnippet,expOffset, expLen, scannerFactory) ;
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
    
      freqImports.clear();
      fReferences.clear();
      // TODO Need to be able and work with a IProgressMonitor
      String result = getExpressionDecoder().generate(sf,fArguments) ; 
      if (result == null) return result ;
      
      result = removeWhiteSpace(result);
      // The jdt parser does not include the ";" or comments as part of the expression.
      String e = ExpressionTemplate.getExpression(result) ;
      ExpressionParser p = createExpressionParser(BeanMethodTemplate.getInitExprFiller()+result,
                                                BeanMethodTemplate.getInitExprFiller().length(),
                                                e.length(), getExpDecoder().getBeanModel()) ;
      setContent(p) ;
      setOffset(-1) ;
      setFillerContent(BeanMethodTemplate.getInitExprFiller());
      try {
	     refreshAST();
	     getExpDecoder().setStatement(fexpStmt);
	     // At this time not all decoders construct PT for all expressions; e.g., the AddDecoderHelper
	     // does not need to construct a PT for elements that are not complex e.g., jPanel.add(jButton,null)
	     // the jButton has its own allocatin, and so does the jPanel, but not the add()
	     Object[] added = getDecoderReferences();
	     for (int i = 0; i < added.length; i++) {
			EObject o = (EObject)added[i];
			if (fBean.getModel().getABean(o) != null && 
				!getReferences().contains(o))
				getReferences().add(o);			
		}
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
		   boolean isField = isStateSet(STATE_FIELD_EXP);
		   clearState();
		   setState(STATE_DELETE, true) ;
		   setState(STATE_NO_SRC,isSrc);
		   setState(STATE_FIELD_EXP,isField);
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
		boolean isMaster = isStateSet(STATE_MASTER);
		clearState();
		setState(STATE_DELETE, true) ;
		setState(STATE_NO_SRC,isSrc);
		setState(STATE_MASTER, isMaster);
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
		
		
		int curState = primGetState();
	    ExpressionParser curParser = getContentParser();
	    Statement  curStatement = getExprStmt();
	    
		// extract new changes
		setContent(exp.getParser()) ;
        updateLimboState(exp) ;
        if (exp.getExprStmt() != null)
		   setExprStmt(exp.getExprStmt());
		
		// Update the VCE model
		if((!isStateSet(STATE_EXP_IN_LIMBO)) && (!isStateSet(STATE_NO_MODEL))) { //((fState&STATE_EXP_IN_LIMBO)==0 && (fState&STATE_NO_OP)==0){
			// Let the decoder refresh its JDOM Expression
			primGetDecoder().setExpression(this);

			final Boolean[] changed = { Boolean.FALSE };
			Adapter changedAdapter = new AdapterImpl() {
		  		public void notifyChanged(Notification msg){
		  			if (msg.getEventType()!= Notification.REMOVING_ADAPTER)
		  			    changed[0] = Boolean.TRUE;
		  	    }
		    };
			try {
			  fBean.getEObject().eAdapters().add(changedAdapter);
			  if (!primGetDecoder().decode()) {
			  	// Decoder failed to decode... potentially because of import resolution problems...
			  	// do not pick up the chnage... pick it up when the import is added
			  	int len = getLen();
			  	setContent(curParser);
			  	primSetState(curState);
			  	setExprStmt(curStatement);
			  	primGetDecoder().setExpression(this);
			  	// Set the document len... so that a top down could replace it correclty.
			  	setLen(len);
			  }
			}
			catch (Throwable t) {
				JavaVEPlugin.log(t) ;
			}
			finally {
				fBean.getEObject().eAdapters().remove(changedAdapter);				
			}
			if (changed[0].booleanValue())
			    // Model is in update mode already here
			    CodeGenUtil.snoozeAlarm(fBean.getEObject(),fBean.getModel().getCompositionModel().getModelResourceSet(), new HashMap()) ;
		}
	    setOffset(off) ;
	    	    
	    
	}catch(Exception e){
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	finally {
	   setState(STATE_UPDATING_SOURCE, false); // fState &= ~STATE_UPDATING_SOURCE ;				   	 
	}
}


public  void updateDocument(ExpressionParser newParser) {
	if (isStateSet(STATE_NO_SRC)) return ;	
	setState(STATE_UPDATING_SOURCE, true); //fState |= STATE_UPDATING_SOURCE ;
	int off = getOffset();
	int len = getLen();

	setContent(newParser) ;

	getMethod().refreshIMethod();
	int docOff = off + getMethod().getOffset();
	String newContent = getContent(); //(fState&~STATE_UPDATING_SOURCE) == STATE_NOT_EXISTANT ? "" : getContent()  //$NON-NLS-1$
	updateDocument(docOff,len,newContent);

	setOffset(off);
	setState(STATE_UPDATING_SOURCE, false); 
	setState(STATE_EXP_NOT_PERSISTED,false);	
}

/**
 * Updates the document with the latest content from refreshComposition().
 * Optionally set a new filler and comment for the expression. Only non-null
 * filler and comments are set. Pass in null to filler and comment to use the default.
 * 
 * @param updateSharedDoc
 * @param newFiller
 * @param newComment
 * 
 * @since 1.1
 */
protected  void updateDocument(boolean updateSharedDoc, String newFiller, String newComment) {
	if(isStateSet(STATE_IN_SYNC))  
		return ;
	
    boolean isFieldExp = isStateSet(STATE_FIELD_EXP);
    
	StringBuffer trace = new StringBuffer() ;
	trace.append("CodeExpressionRef.updateDocument():\n") ; //$NON-NLS-1$	
	setState(STATE_UPDATING_SOURCE, true); //fState |= STATE_UPDATING_SOURCE ;
	int off = getOffset() ;
	int len = getLen() ;
	String prevContent = getContent()==null ? "" : getContent() ;	  	   //$NON-NLS-1$
	trace.append(prevContent) ;	  
          
	if(newFiller!=null)
		try {
			setFillerContent(newFiller);
		} catch (CodeGenException e1) {
			JavaVEPlugin.log(e1, Level.WARNING);
		}
	if(newComment!=null)
		try {
			setCommentContent(newComment);
		} catch (CodeGenException e1) {
			JavaVEPlugin.log(e1, Level.WARNING);
		}
	

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
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log ("CodeExpressionRef.updateDocument() : No change - "+prevContent, Level.FINE) ; //$NON-NLS-1$
			setState(STATE_UPDATING_SOURCE, false); //fState &= ~STATE_UPDATING_SOURCE ;		
			return ;
		}
		
		// Field expressions are deleted by BPFactory overall - no need to change document as it is already removed
		boolean isFieldExpDeletion = isFieldExp && isStateSet(STATE_DELETE);
		if(!isFieldExpDeletion){
			int docOff = isFieldExp ? off : off+getMethod().getOffset() ;
			String newContent = ((!isAnyStateSet()) || isStateSet(STATE_DELETE)) ? "" : getContent(); //(fState&~STATE_UPDATING_SOURCE) == STATE_NOT_EXISTANT ? "" : getContent()  //$NON-NLS-1$
			trace.append("\t changed to: \n") ; //$NON-NLS-1$
			trace.append(newContent+"\n") ;	       //$NON-NLS-1$
			updateDocument(docOff,len,newContent) ;
		}
    }
	setOffset(off) ;
	setState(STATE_UPDATING_SOURCE, false); //fState &= ~STATE_UPDATING_SOURCE ;
	
	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
		JavaVEPlugin.log(trace.toString(), Level.FINE) ;

	if ((!isAnyStateSet()) || isStateSet(STATE_DELETE)) { //(fState == STATE_NOT_EXISTANT) {
		// Expression was deleted
		if (isStateSet(STATE_MASTER)) {
			CodeExpressionRef master = getMasteredExpression();
			master.setState(STATE_MASTER_DELETED,isStateSet(STATE_DELETE));
			master.setState(STATE_IN_SYNC,false);
			master.updateDocument(true, null, null);
		}
		dispose() ;
	}	
}
/**
 * 
 * @param docOff
 * @param len current content
 * @param newContent new content
 * 
 * @since 1.1.0
 */
protected void updateDocument(int docOff, int len, String newContent) {
	IBeanDeclModel model = fBean.getModel() ;
	
		try {
			model.getDocumentBuffer().replace(docOff,len,newContent) ;				
			model.driveExpressionChangedEvent(getMethod(), docOff, newContent.length()-len) ;
			handleImportStatements(fBean.getModel().getCompilationUnit(), fBean.getModel(), freqImports);
			freqImports.clear();
			setState(STATE_EXP_NOT_PERSISTED,false);
        } catch (Exception e) {
			JavaVEPlugin.log(e) ;
		}	
}

/**
 * Expression decoders may generated code that will require
 * a set of import statements.  If they do not exist, add them
 */
public static void handleImportStatements(ICompilationUnit cu, IBeanDeclModel model, List imports) {
	if (imports != null && imports.size()>0) {		
		try {
			String packageName=null;
			IPackageDeclaration[] pkg = cu.getPackageDeclarations();
			if (pkg!=null && pkg.length>0)
				packageName = pkg[0].getElementName();

			for (int i = 0; i < imports.size(); i++) {
//				cu.reconcile(false,false,null,null);
			   String reqName = (String)imports.get(i);
			   int lastIndex = reqName.lastIndexOf('.');
			   if (reqName.indexOf('.')<0)
			   	   continue; // Default package ... no need
			   else if (reqName.substring(0,lastIndex).equals(packageName))
			   	   continue; // same package
			   
			   int preLen = cu.getSource().length();
			   IImportDeclaration[] cuImports = cu.getImports();
			   boolean found=false;			   
			   for (int j = 0; j < cuImports.length; j++) {
						String iname = cuImports[j].getElementName();
						// check to see if this is a a.b.c.* type of an import
						boolean star = false ;
						StringTokenizer tk = new StringTokenizer(iname,"."); //$NON-NLS-1$
						String lastSeg = iname ;
						while (tk.hasMoreElements())
							lastSeg=tk.nextToken();
						if (lastSeg == "*") { //$NON-NLS-1$
							star = true;
							iname = iname.substring(0,iname.length()-lastSeg.length());
						}
						if ((star && reqName.startsWith(iname)) ||
						    (!star && reqName.equals(iname))) {
						      found=true;
						      break;
						}
			   }
			   if (!found) {
			   	  int offset = cu.createImport(reqName, null, null).getSourceRange().getOffset();
			   	  int delta = cu.getSource().length()-preLen;
			   	  if (model != null)
				      model.driveExpressionChangedEvent(null, offset, delta) ;
			   }
			}

		} catch (JavaModelException e) {
			JavaVEPlugin.log(e);
		}
	}
	
}

public  void insertContentToDocument() {
	if (isStateSet(STATE_NO_SRC) || isStateSet(STATE_DELETE)) return ;
	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
		JavaVEPlugin.log("CodeExpressionRef: creating:\n"+getContent()+"\n", Level.FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	try {
		// mark a controlled update (Top-Down)		
		setState(STATE_UPDATING_SOURCE, true);
		String txt = getContent();
		int docOff = getOffset()+getMethod().getOffset() ;
//		if (isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
//			// add a new line -- mostly the case for local declerations
//			Iterator itr = getMethod().getExpressions();
//			if (itr.next()==this) {
//				// first in line			
//				txt = txt + fBean.getModel().getLineSeperator();				
//			}
//			else {
//				CodeExpressionRef e=null;
//				while (itr.hasNext())
//						e = (CodeExpressionRef)itr.next();
//				if (e==this) {
//					// last in line
//					txt = fBean.getModel().getLineSeperator() + txt;
//					setOffset(getOffset()+fBean.getModel().getLineSeperator().length());
//				}
//			}
//		}
		updateDocument(docOff, 0, txt) ;
		if (isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
			 try {
				setState(STATE_UPDATING_SOURCE, false);
				fBean.forceExpressionOrdering();
				fBean.generateFeatures();
			} catch (CodeGenException e) {
				JavaVEPlugin.log(e);
			}
			  
		}
	}
	finally {
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
		ASTParser parser = ASTParser.newParser(AST.JLS2);
		parser.setSource(getContent().toCharArray());
		parser.setSourceRange(0,getContent().length());
		parser.setKind(ASTParser.K_STATEMENTS) ;		
//        ASTNode ast = AST.parse(AST.K_STATEMENTS, getContent().toCharArray(), 0, getContent().length(), null);
        ASTNode ast = parser.createAST(null);
        ASTVisitor visitor = new ASTVisitor() {
			public void endVisit(ExpressionStatement node) {
				s[0] = node;					
			}
			public void endVisit(VariableDeclarationStatement node) {
				s[0] = node;
			}
		};
		ast.accept(visitor);
		
        if (s[0] ==null) {
        	if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
        		JavaVEPlugin.log("CodeExpressionRef.refreshAST(): could notrefresh "+getContent(),Level.WARNING); //$NON-NLS-1$
        } else
            fexpStmt = s[0];	
}



public boolean isStateSet(int state){
	return ((primGetState() & state) == state);
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

        if (exp1.equals(this)) return 1 ;
        
		if (getBean() == null && exp1.getBean() != null) return -1 ;
		else
		  if (getBean() != null && exp1.getBean() == null) return -1 ;
		
		boolean beanEquivalency = getBean().isEquivalent(exp1.getBean());
		
		//TODO: Need to deal with Local Declerations		
		boolean expEquivalency;
		if(isStateSet(CodeExpressionRef.STATE_INIT_EXPR) && exp1.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)){
			expEquivalency = true;
		}else{
			String expc1 = exp1.getMethodNameContent();
			String expc2 = getMethodNameContent();
			expEquivalency = expc1.equals(expc2);
		}
		
		if(beanEquivalency && expEquivalency){
		    if (isStateSet(STATE_NO_MODEL) || exp1.isStateSet(STATE_NO_MODEL)) {
				// One of the expressions is specifically not in model - check
				// if either has an eobject. If not just return based on content
				if (getBean().getEObject() == null && exp1.getBean().getEObject() == null) {
					if (getCodeContent().equals(exp1.getCodeContent()))
						return 1;
					else
						return 0;
				}
			}

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
				if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
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
				String content = allocS.substring(0,allocS.indexOf(alloc.getName().toString())).trim();
				if(assgn.getLeftHandSide() instanceof QualifiedName){
					QualifiedName qn = (QualifiedName) assgn.getLeftHandSide();
					if(qn.getName()!=null)
						content = qn.getName().toString()+"."+content; //$NON-NLS-1$
				}
				return content;
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
  
	if (!isStateSet(STATE_DELETE) && primGetDecoder() != null && !isStateSet(STATE_NO_MODEL)) {
		primGetDecoder().dispose();		
	}
	fDecoder = null ;
	clearState();	
	setState(STATE_DELETE, true) ;
	setContent((ExpressionParser) null) ;
	if (fMethod != null)
	   fMethod.removeExpressionRef(this) ;		
	fMethod = null ;
	if (fBean != null) {
	   fBean.removeRefExpression(this) ;
	   fBean.removeNoSrcExpresion(this);
	}
	fBean = null ;	

	// Remove this expression from the same line expressions of others
	if(fSameLineExpressions.size()>1){
		for (Iterator expItr = fSameLineExpressions.iterator(); expItr.hasNext();) {
			CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
			if(exp==this)
				continue;
			List list = exp.getSameLineExpressions();
			if(list!=null && list.contains(this))
				list.remove(this);
		}
	}
	setSameLineExpressions(null);
}

/**
 * 
 * @param priority
 * @todo Generated comment
 * @deprecated  priorities will be calculated by the Expression itself.
 */
public void setProprity(VEexpressionPriority priority){
	//fPriority = priority;
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
    if(isStateSet(STATE_MASTER))
        states = states.concat("MASTER#"); //$NON-NLS-1$
    if(isStateSet(STATE_MASTER_DELETED))
        states = states.concat("MASTER_DELETED#"); //$NON-NLS-1$
    if(isStateSet(STATE_DELETE))
    	states = states.concat("DELETE#"); //$NON-NLS-1$    
    if (isStateSet(STATE_NO_SRC))
   	    states = states.concat("STATE_NO_SRC#"); //$NON-NLS-1$
   	if (isStateSet(STATE_NO_MODEL))
   		states = states.concat("STATE_NO_MODEL#"); //$NON-NLS-1$    
   	if (isStateSet(STATE_SHARED_LINE))
   		states = states.concat("STATE_SHARED_LINE#"); //$NON-NLS-1$
   	if (isStateSet(STATE_FIELD_EXP))
   		states = states.concat("STATE_FIELD_EXP#"); //$NON-NLS-1$  
	states = states.concat("}"+" Offset: "+getOffset()+" upTo:"+(getOffset()+getLen())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	if (isStateSet(STATE_NO_SRC) && fMasteredExpression != null)
		return fMasteredExpression.getContent() + states;
	else
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
    CodeGenSourceRange result = null;
    if(isStateSet(CodeExpressionRef.STATE_FIELD_EXP)){
	    result = new CodeGenSourceRange (getOffset(),getLen()) ;
	    result.setLineOffset(fBean.getModel().getWorkingCopyProvider().getLineNo(result.getOffset())) ;
    }else{
	    ISourceRange mSR = fBean.getModel().getWorkingCopyProvider().getSourceRange(fMethod.getMethodHandle()) ;   
	    if (mSR == null) return null ;
	    result = new CodeGenSourceRange (mSR.getOffset()+getOffset(),getLen()) ;
	    result.setLineOffset(fBean.getModel().getWorkingCopyProvider().getLineNo(result.getOffset())) ;
    }
    return result ;
}

public void setSF(EStructuralFeature sf) {
    getExpDecoder() ;
    if (fDecoder !=null)	
	   fDecoder.setSF(sf) ;
}

public EStructuralFeature getSF() {
	getExpDecoder() ;
	if (fDecoder!=null)
		return fDecoder.getSF() ;
    else
        return null ;
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

public void setNoSrcExpression(boolean noSource) {
	setState(STATE_NO_SRC,noSource) ;
	if (fBean!=null) {
	  if (noSource) {
	   fBean.removeRefExpression(this);
	   fBean.addNoSrcExpresion(this);
	  }
	  else {
	  	fBean.removeNoSrcExpresion(this);
	  	fBean.addRefExpression(this);
	  }
	}
	if (getMethod()!=null) {
	  if (noSource)
	    getMethod().removeExpressionRef(this) ;
	  else
	  	getMethod().addExpressionRef(this);
	}
}

public Object[] getDecoderReferences() {
	if (fDecoder != null)
		 return fDecoder.getReferencedInstances();
	return null ;	
}

public Object[] getAddedInstances() {
	if (fDecoder != null)
		 return fDecoder.getAddedInstance();
	return null ;	
}

/**
 * @return Returns the fMasteredExpression.
 * @todo Generated comment
 */
public CodeExpressionRef getMasteredExpression() {
	return fMasteredExpression;
}
/**
 * @param masterExpression The fMasteredExpression to set.
 * @todo Generated comment
 */
public void setMasteredExpression(CodeExpressionRef masterExpression) {
	fMasteredExpression = masterExpression;
	if (masterExpression==null)
		setState(STATE_MASTER,false) ;
	else
		setState(STATE_MASTER,true);
}
	/** (non-Javadoc)
	 * 
	 */
	public int getOffset() {
		if (fMasteredExpression != null && isStateSet(STATE_NO_SRC)) {
			// use our master offset.  This is important for
			// figuring out expression priorities, and z orders
			return fMasteredExpression.getOffset();
		}
		else 
			return super.getOffset();
	}
/**
 * @return Returns the freqImports.
 */
public List getReqImports() {
	return freqImports;
}
public List getReferences() {
	return fReferences;
}
public void setReferences(List references) {
	fReferences = references;
}

public static void resetExpressionStates(CodeExpressionRef exp, int states) {
	exp.setState(STATE_EXIST, (states & STATE_EXIST) > 0);
	exp.setState(STATE_IN_SYNC, (states & STATE_IN_SYNC) > 0);
	exp.setState(STATE_NO_MODEL, (states & STATE_NO_MODEL) > 0);
	exp.setState(STATE_NO_SRC, (states & STATE_NO_SRC) > 0);
	exp.setState(STATE_DELETE, (states & STATE_DELETE) > 0);
	exp.setState(STATE_IMPLICIT, (states & STATE_IMPLICIT) > 0);
	exp.setState(STATE_UPDATING_SOURCE, (states & STATE_UPDATING_SOURCE) > 0);
	exp.setState(STATE_SRC_LOC_FIXED, (states & STATE_SRC_LOC_FIXED) > 0);
	exp.setState(STATE_EXP_IN_LIMBO, (states & STATE_EXP_IN_LIMBO) > 0);
	exp.setState(STATE_EXP_NOT_PERSISTED, (states & STATE_EXP_NOT_PERSISTED) > 0);
	exp.setState(STATE_MASTER, (states & STATE_MASTER) > 0);
	exp.setState(STATE_MASTER_DELETED, (states & STATE_MASTER_DELETED) > 0);
	exp.setState(STATE_INIT_EXPR, (states & STATE_INIT_EXPR) > 0);
}

/**
 * Sets the list of expressions in the same line as this.
 * The passed in list generally includes this expression also 
 * as returned by ExpressionParser.getExpressionsOnSameLine().
 * This information should be used in conjunction with STATE_SHARED_LINE.
 * The contents of the passed in list are copied to an internal list.
 * 
 * @param sameLineExpressions
 * @see ExpressionParser#getExpressionsOnSameLine(CodeExpressionRef, CodeMethodRef)
 * 
 * @since 1.1
 */
public void setSameLineExpressions(List sameLineExpressions){
	fSameLineExpressions.clear();
	if(sameLineExpressions!=null)
		fSameLineExpressions.addAll(sameLineExpressions);
}

public List getSameLineExpressions(){
	return fSameLineExpressions;
}

/**
 * This API has been created specifically for breaking up expressions in 
 * the same line into multiple expressions. At the same time all the contents
 * of the expressions will be refreshed from emf model. This will only happen
 * when this expression has the STATE_SHARED_LINE state set.
 * 
 * @param updateSharedDoc
 * 
 * @since 1.1
 */
public void updateSharedLineExpressions(boolean updateSharedDoc){
	if(isStateSet(STATE_SHARED_LINE)){
		boolean clearThisSameLineExpressions = false;
		// we are about to update a shared line - regenerate all of them with the correct fillers and comments
		List sameLineExpressions = new ArrayList(fSameLineExpressions);
		String defaultFiller = ((CodeExpressionRef)sameLineExpressions.get(0)).getFillerContent();
		String NL = fBean.getModel().getLineSeperator();
		Iterator expItr = sameLineExpressions.iterator();
		while (expItr.hasNext()) {
			String correctFiller = defaultFiller;
			String correctEnd = NL;
			CodeExpressionRef exp = (CodeExpressionRef) expItr.next();
			if(sameLineExpressions.indexOf(exp)==0) // first expression
				correctFiller = null; // first expression's filler doesnt change
			else
				correctFiller = NL + correctFiller; // the way expressionparser works to find start is by NLs
			
			if(sameLineExpressions.indexOf(exp)!=sameLineExpressions.size()-1) // update newlines for all non-last expressions
				correctEnd = exp.getCommentsContent()==null ? correctEnd : exp.getCommentsContent()+correctEnd;
			else
				correctEnd = null; // last expression's comment doesnt change
			exp.setState(CodeExpressionRef.STATE_SHARED_LINE, false);
			if(exp!=this){
				exp.setSameLineExpressions(null);
				exp.setState(CodeExpressionRef.STATE_IN_SYNC, false);
				exp.updateDocument(updateSharedDoc, correctFiller, correctEnd);
				exp.setState(CodeExpressionRef.STATE_IN_SYNC, true);
			}else{
				boolean isInSync = isStateSet(STATE_IN_SYNC);
				if(isInSync)
					setState(STATE_IN_SYNC, false);
				updateDocument(updateSharedDoc, correctFiller, correctEnd);
				if(isInSync)
					setState(STATE_IN_SYNC, true);
				clearThisSameLineExpressions = true;
			}
		}
		if(clearThisSameLineExpressions)
			setSameLineExpressions(null);
	}
}

public void updateDocument(boolean updateSharedDoc) {
	if(isStateSet(STATE_SHARED_LINE)){
		updateSharedLineExpressions(updateSharedDoc);
	}else{
		updateDocument(updateSharedDoc, null, null); // the regular path
	}
}

}

