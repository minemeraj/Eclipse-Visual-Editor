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
 *  $Revision: 1.4 $  $Date: 2004-01-23 21:04:08 $ 
 */

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.BasicCompilationUnit;
import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;




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
  
  CompilationUnitDeclaration 	fJDOM  = null ;  // JDOM
  IBeanDeclModel 				fModel = null ; 
  ICompilationUnit				fCU = null ;
  IWorkingCopyProvider          fWCP = null ;
  IDiagramModelInstance         fDiagram  = null ;
  EditDomain					fDomain = null ;
	
public JavaBeanModelBuilder(EditDomain d, String fileName, char[][] packageName) {
  	this.fFileName = fileName ;
  	this.fPackageName = packageName ;
  	this.fDomain = d ;

}

public void setDiagram(IDiagramModelInstance diag) {
    fDiagram = diag ;
}


/**
 *  
 */
public JavaBeanModelBuilder(EditDomain d, IWorkingCopyProvider wcp, String filePath, char[][] packageName) {
	this (d,filePath,packageName) ;
  	fCU = wcp.getWorkingCopy(false) ;
  	fWCP = wcp ;
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
protected void ParseJavaCode() throws CodeGenException
{
	// To get the JDOM of the file we need a compilation unit
	String encoding = (String) JavaCore.getOptions().get(CompilerOptions.OPTION_Encoding);
	if ("".equals(encoding)) encoding = null;  //$NON-NLS-1$
	BasicCompilationUnit sourceUnit = new BasicCompilationUnit( fFileContent , fPackageName, fFileName,  encoding) ;
	// Create a default reporter to handle any parser errors.
	ProblemReporter reporter =new ProblemReporter(
		DefaultErrorHandlingPolicies.exitAfterAllProblems(),
		new CompilerOptions(),
		new DefaultProblemFactory(Locale.getDefault())
	);
	CompilationResult result = new CompilationResult(sourceUnit ,1,1,20);
	fJDOM = getModelFromParser(reporter, result, sourceUnit);	


    // Need to refine here, right now pick up the first problem
    // Note that we do not pick up compile problems, like extending an object 
    // without the proper constructors
	if (result.hasErrors())  {
		IProblem[] problems = result.getProblems() ;
//		String forDebugging = new String (fFileContent)  ;
		for (int i=0; i< problems.length; i++) {
			if (problems[i].isError()) {
			  
			  throw new CodeGenSyntaxError ("JVE Parsing Error: "+problems[i].getMessage()+ //$NON-NLS-1$
			                              " - Line: "+Integer.toString(problems[i].getSourceLineNumber())) ;			   //$NON-NLS-1$
			}
		}
		
	}
} 
 
  
protected CompilationUnitDeclaration getModelFromParser(
	ProblemReporter reporter,
	CompilationResult result,
	BasicCompilationUnit cu){
	Parser aParser = new Parser(reporter,true);
	return aParser.parse(cu,result);	
}
	
  
/**
 *  Initialize a new Bean Decleration Model
 */  
protected void CreateBeanDeclModel() throws CodeGenException {
	fModel = createDefaultModel(fDomain);
    fModel.setJDOM(fJDOM) ;
    fModel.setWorkingCopyProvider(fWCP) ;
    if (fJDOM.types == null) throw new CodeGenException ("No Type to work on") ; //$NON-NLS-1$
    fModel.setTypeDecleration((TypeDeclaration)fJDOM.types[0]) ;    
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
	for (int i=0; i<fFileContent.length; i++) {
		if (fFileContent[i] == '\r' || fFileContent[i] == '\n' ) {
			if (fFileContent[i] == '\r')
			   if (i+1<fFileContent.length && fFileContent[i+1] == '\n')
			     fModel.setLineSeperator("\r\n") ; //$NON-NLS-1$
			   else
			     fModel.setLineSeperator("\r") ; //$NON-NLS-1$
			else
			     fModel.setLineSeperator("\n") ; //$NON-NLS-1$
			break ;
		}
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
				JavaVEPlugin.log("*Discarting a beanPart " + bean, MsgLogger.LOG_FINE); //$NON-NLS-1$
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
	Statement s = eRef.getExpression() ;
	if (s instanceof MessageSend) {
		MessageSend ms = (MessageSend) s ;
		if (ms.arguments != null && ms.arguments.length==1) {
			if (ms.arguments[0] instanceof SingleNameReference)
			   name = new String (((SingleNameReference)ms.arguments[0]).token) ;
		}
	}
	if (name != null) {
		// Look at the type for this variable
		if (fJDOM.types != null && fJDOM.types.length>0 && 
		    fJDOM.types[0].fields != null) {
		   	for (int i = 0; i < fJDOM.types[0].fields.length; i++) {
				FieldDeclaration f = fJDOM.types[0].fields[i] ;
				if (new String(f.name).equals(name)) {
				  result = f.type.toString() ;		
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

protected  void analyzeEvents() {
	
	Iterator itr = fModel.getBeans().iterator() ;
	// EventParser will cache event information, and will 
	// Scan methods for event expressions.
	EventsParser p = new EventsParser(fModel, fJDOM) ;
	while (itr.hasNext()) {
		BeanPart b = (BeanPart) itr.next();
		p.addEvents(b) ;
	}
	
	List sharedListeners = mineForSharedListeners() ;
	
	// Parse and Event handler (VCE style 2) if one exits
	if (fJDOM.types != null && fJDOM.types[0].memberTypes != null) {
	  for (int i=0; i<fJDOM.types[0].memberTypes.length; i++) {
		String name = new String(fJDOM.types[0].memberTypes[i].name) ;
		if (sharedListeners.contains(name)) {
			new EventHandlerVisitor(fJDOM.types[0].memberTypes[i],fModel,false).visit() ;
			break ;
		}
	  }
	}
	
}

/**
 *  Go for it
 */  
public IBeanDeclModel build () throws CodeGenException {

JavaVEPlugin.log ("JavaBeanModelBuilder.build() starting .... ", MsgLogger.LOG_FINE) ; //$NON-NLS-1$


    if (fFileName == null || fFileName.length() == 0) throw new CodeGenException("null Input Source") ;     //$NON-NLS-1$
    fFileContent = getFileContents() ;
    // Build a JDOM
    ParseJavaCode () ;    
	CreateBeanDeclModel() ;
	setLineSeperator() ;
	  
    // Some of the Visitors may not be able to resolve some construct on the first path,
    // Run them again, if they put themself on the re-try list.
    try {
		List  tryAgain = new ArrayList () ;
	    
	    if (fJDOM.types[0] == null) throw new CodeGenException(".. no type was parsed ...") ; //$NON-NLS-1$
	    // Start visiting our main type
	    visitType((TypeDeclaration)fJDOM.types[0],fModel,fFileContent,tryAgain) ;
	
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
       JavaVEPlugin.log ("JavaBeanModelBuilder.build(), Done.", MsgLogger.LOG_FINE) ; //$NON-NLS-1$
	}
   return fModel ;
   
}
    
protected void visitType(TypeDeclaration type, IBeanDeclModel model, char[] content, List tryAgain){
	new TypeVisitor(type,model,tryAgain,false).visit()  ;
}
	
}

