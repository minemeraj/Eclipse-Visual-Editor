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
 *  $RCSfile: JavaBeanModelBuilder.java,v $
 *  $Revision: 1.12 $  $Date: 2004-04-15 19:34:09 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.ISynchronizable;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;




/**
 * This class is used to build a main BDM. Will creat use the Local compilation
 * unit, and will set up the BDM appropriatly.   
 * 
 * Do not use this builder to build a BDM if a main BDM already exist.  
 * Use the JavaBeanShadowModelBuilder  instead.
 */

public class JavaBeanModelBuilder {
	
  
  String     fFileName = null ;     //  Java Source 
  char[]    fFileContent = null ;
  char[][]  fPackageName = null ;
  
 
  CompilationUnit				fastCU = null; 
  IBeanDeclModel 				fModel = null ; 
  ICompilationUnit				fCU = null ;
  IWorkingCopyProvider          fWCP = null ;
  IVEModelInstance         		fDiagram  = null ;
  EditDomain					fDomain = null ;
  JavaSourceSynchronizer		fSync = null;
	
public JavaBeanModelBuilder(EditDomain d, String fileName, char[][] packageName) {
  	this.fFileName = fileName ;
  	this.fPackageName = packageName ;
  	this.fDomain = d ;

}

public void setDiagram(IVEModelInstance diag) {
    fDiagram = diag ;
}


/**
 *  
 */
public JavaBeanModelBuilder(EditDomain d, JavaSourceSynchronizer sync, IWorkingCopyProvider wcp, String filePath, char[][] packageName) {
	this (d,filePath,packageName) ;
  	fCU = wcp.getWorkingCopy(false) ;
  	fWCP = wcp ;
  	fSync=sync;
}


/**
 *  Read the source file
 */
protected char[] getFileContents() throws CodeGenException {

  char[] result = null ; 
  
  try {  
    if (fCU != null) { // even if the CU is not parsable, get it.
  	    result = fCU.getSource().toCharArray() ;  	  	
    }
    else 
      throw new CodeGenException ("Could not get a valid source text") ; //$NON-NLS-1$
  }
  catch (JavaModelException e) {} 
  
  return result ;
}

/**
 *  Get the JDOM for the input source file
 */
protected CompilationUnit ParseJavaCode() throws CodeGenException
{
		
	// TODO: we need to investigate the bindings option vs. resolve
	//       Also, from this level we have no control on the parser options.
	//       we may consider to turn off some errors ... as we are not interested in
	//       to compile all of this, but rather bits of pieaces of this
	
	try {
		CompilationUnit result;
		if (fCU!=null) {
			if (fCU.isConsistent()) {
				ASTParser parser = ASTParser.newParser(AST.LEVEL_2_0);
				parser.setSource(fCU);
				result = (CompilationUnit) parser.createAST(null);
			}
			else {
				// AST will only be returned if need to reconcile
			   result = fCU.reconcile(true,false,null,null);	
			}
		}
		else {
		  ASTParser parser = ASTParser.newParser(AST.LEVEL_2_0);
		
			fFileContent = getFileContents();
			parser.setSource(fFileContent);		
		    result = (CompilationUnit) parser.createAST(null);
		}
		   		
		
		IProblem[]	problems = result.getProblems();
		if (problems!=null) {
			for (int i = 0; i < problems.length; i++) {
				if (problems[i].isError()) {			
					throw new CodeGenSyntaxError ("JVE Parsing Error: "+problems[i].getMessage()+ //$NON-NLS-1$
							" - Line: "+Integer.toString(problems[i].getSourceLineNumber())) ;			   //$NON-NLS-1$
				}
			}
		}
		return result ;
	} catch (CodeGenSyntaxError e) {
		throw e;	// Pass it on, don't log it twice.
	} catch (Exception e) {
		throw new CodeGenSyntaxError ("JVE Parsing Error: "+e.getMessage());
	}
} 
 
	
  
/**
 *  Initialize a new Bean Decleration Model
 */  
protected void CreateBeanDeclModel() throws CodeGenException {
	fModel = createDefaultModel(fDomain);   
    fModel.setWorkingCopyProvider(fWCP) ;
    if (fastCU.types().size() == 0) throw new CodeGenException ("No Type to work on") ; //$NON-NLS-1$
    fModel.setTypeDecleration((TypeDeclaration)fastCU.types().get(0)) ;    
    fModel.setCompositionModel(fDiagram) ;
}  

protected IBeanDeclModel createDefaultModel(EditDomain d){
	BeanDeclModel m = new BeanDeclModel() ;
	m.setDomain(d) ;

    CodeGenUtil.getDecoderFactory(m).setResourceSet(fDiagram.getModelResourceSet()) ;
    CodeGenUtil.getMethodTextFactory(m).setResourceSet(fDiagram.getModelResourceSet()) ;
	return m ;
}

/**
 *
 */
void  setLineSeperator() {
	fModel.setLineSeperator(System.getProperty("line.separator")) ; //$NON-NLS-1$
	// For Shadow BDM no need to set up a seperator -- no fCU
	if (fCU==null) return ;
	try {		
		IBuffer buff = fCU.getBuffer();
		int len = buff.getLength();
		for (int i=0; i<len; i++) {
			char c = buff.getChar(i);
			if (c == '\r' || c == '\n' ) {
				if (c == '\r')
				   if (i+1<len && buff.getChar(i+1) == '\n')
				     fModel.setLineSeperator("\r\n") ; //$NON-NLS-1$
				   else
				     fModel.setLineSeperator("\r") ; //$NON-NLS-1$
				else
				     fModel.setLineSeperator("\n") ; //$NON-NLS-1$
				break ;
			}
		}
	} catch (JavaModelException e) {
		JavaVEPlugin.log(e);
	}
}


/**
 * The BDM reflecting the parsed code may contain BeanParts that have no 
 * associated expression/s, or no method with an initialization expression.
 * 
 * This method will remove these instances from the model
 */
protected void cleanModel () {

	Iterator itr = fModel.getBeans().iterator() ;
	ArrayList err = new ArrayList() ;
	
	while (itr.hasNext()) {
		BeanPart bean = (BeanPart) itr.next();
		boolean removeFlag = false;

		if (!bean.getSimpleName().equals(BeanPart.THIS_NAME)) {
			if (BeanPartFactory.getInstanceInitializationExpr(bean) == null && !bean.isInstanceInstantiation()){
//			    &&  (bean.getReturnedMethod() == null || bean.getRefExpressions().isEmpty())) {
				JavaVEPlugin.log("*Discarting a beanPart " + bean, Level.FINE); //$NON-NLS-1$
				removeFlag = true;
			}
		}

		if (removeFlag == true) {
			err.add(bean);
			// Children will not be connected to the VCE model
			Iterator bItr = bean.getChildren();
			if (bItr != null)
				while (bItr.hasNext())
					err.add(bItr.next());
		}
	}
	for (int i = 0; i < err.size(); i++) {
		((BeanPart) err.get(i)).dispose();
	}
}

private String getSharedHandlerName(CodeEventRef eRef) {
	String result = null ;
	String name = null ;
	Expression s = ((ExpressionStatement)eRef.getExprStmt()).getExpression() ;
	if (s instanceof MethodInvocation) {
		MethodInvocation ms = (MethodInvocation) s ;
		if (ms.arguments().size()==1) {
			if (ms.arguments().get(0) instanceof SimpleName)
			   name = ((SimpleName)ms.arguments().get(0)).getIdentifier();
		}
	}
	if (name != null) {
		// Look at the type for this variable
		if (fastCU != null && fastCU.types().size()>0 && 
			((TypeDeclaration)fastCU.types().get(0)).getFields() != null) {
            FieldDeclaration[] fields = ((TypeDeclaration)fastCU.types().get(0)).getFields();
		   	for (int i = 0; i < fields.length; i++) {
				//TODO: support multi variable per decleration.
				VariableDeclaration f = (VariableDeclaration)fields[i].fragments().get(0) ;
				if (f.getName().getIdentifier().equals(name)) {
				  result = fields[i].getType().toString();		
				  break ;
				}
			}
		}
		
	}
	return result ;
}
protected  List mineForSharedListeners() {
	List l = new ArrayList() ;
	for (Iterator itr=fModel.getAllMethods(); itr.hasNext();) {
		CodeMethodRef m = (CodeMethodRef) itr.next() ;
		for (Iterator exp=m.getEventExpressions(); exp.hasNext();){
			CodeEventRef eRef = (CodeEventRef) exp.next() ;
			String instance = getSharedHandlerName(eRef) ;
			if (!l.contains(instance))
			   l.add(instance);
		}
	}
	
	return l ;
}

protected List getInnerTypes() {
   List l = new ArrayList();
   if (fastCU.types().size()>0) {
    List body = ((TypeDeclaration)fastCU.types().get(0)).bodyDeclarations();
    for (int i = 0; i < body.size(); i++) {
       if (body.get(i) instanceof TypeDeclaration)
             l.add(body.get(i));	
   }
  }  
  return l ;
}

protected  void analyzeEvents() {
	
	Iterator itr = fModel.getBeans().iterator() ;
	// EventParser will cache event information, and will 
	// Scan methods for event expressions.
	EventsParser p = new EventsParser(fModel, fastCU) ;
	while (itr.hasNext()) {
		BeanPart b = (BeanPart) itr.next();
		p.addEvents(b) ;
	}
	
	List sharedListeners = mineForSharedListeners() ;
	
	// Parse an Event handler (VCE style 2) if one exits
    List innerTypes = getInnerTypes();	
	  for (int i=0; i<innerTypes.size(); i++) {
		String name = ((TypeDeclaration)innerTypes.get(0)).getName().getIdentifier();
		if (sharedListeners.contains(name)) {
			new EventHandlerVisitor((TypeDeclaration)innerTypes.get(0),fModel,false).visit() ;
			break ;
		}
	  }
	
	
}

/**
 *  Go for it
 */  
public IBeanDeclModel build () throws CodeGenException {

JavaVEPlugin.log ("JavaBeanModelBuilder.build() starting .... ", Level.FINE) ; //$NON-NLS-1$


    // Build a AST DOM
    // We do not want the document to change while we take a snippet of it.
    JavaElementInfo[] jdtMethods=null;
    if (fSync!=null) {
      Object lock = (fWCP.getDocument() instanceof ISynchronizable) ?
      		        ((ISynchronizable)fWCP.getDocument()).getLockObject() :
      		        fWCP.getDocument();
      synchronized(lock) {      	
        fSync.clearOutstandingWork();
        fastCU = ParseJavaCode () ;        
        if (fCU!=null) 
        	jdtMethods=CodeGenUtil.getMethodsInfo(fCU);
        else
        	jdtMethods=null ;
      }
    }
    else 
    	fastCU = ParseJavaCode () ;
	CreateBeanDeclModel() ;
	setLineSeperator() ;
	  
    // Some of the Visitors may not be able to resolve some construct on the first path,
    // Run them again, if they put themself on the re-try list.
    try {
		List  tryAgain = new ArrayList () ;
	    
	    // Start visiting our main type
	    visitType((TypeDeclaration)fastCU.types().get(0), fModel, jdtMethods, tryAgain) ;
	
	    // Let the non resolved visitor a chance to run again.    
	    for (int i=0; i<tryAgain.size(); i++) {
	    	ISourceVisitor visitor = (ISourceVisitor) tryAgain.get(i) ;
	    	visitor.setNoRetry() ;
	    	visitor.visit() ;
	    }
	    
	    analyzeEvents() ;
	    
	    // Update the parent
	    Iterator itr = fModel.getBeans().iterator() ;
		while (itr.hasNext()) {
	       BeanPart bean = (BeanPart) itr.next() ;
	       bean.setModel(fModel) ;
		}
		
		
		cleanModel() ;
		
	} catch(Exception e) {
	    org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
	}
	finally {        
       JavaVEPlugin.log ("JavaBeanModelBuilder.build(), Done.", Level.FINE) ; //$NON-NLS-1$
	}
   return fModel ;
   
}
    
 protected void visitType(TypeDeclaration type, IBeanDeclModel model,  JavaElementInfo[] mthds, List tryAgain){
	TypeVisitor v = new TypeVisitor(type,model, tryAgain,false) ;
	v.setJDTMethods(mthds);
	v.visit()  ;
}
	
}

