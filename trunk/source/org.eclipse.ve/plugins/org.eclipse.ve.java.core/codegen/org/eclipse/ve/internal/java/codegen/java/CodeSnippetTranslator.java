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
 *  $RCSfile: CodeSnippetTranslator.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-20 00:44:29 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.compiler.IProblem;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.core.TransientErrorEvent;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class CodeSnippetTranslator {


// Used to build the internal Shaddow BDM
public static String CODE_SNIPPET_CLASSNAME="CodeSnippet_2"; //$NON-NLS-1$
protected int insertOffset = -1;
protected int insertLength = -1;

private List errorStrore = new ArrayList();

protected int[] importStarts, importEnds;
protected int[] fieldStarts, fieldEnds;
protected int[] methodStarts, methodEnds;

protected String entireCode = null;
protected String packageName = null;
protected String extendsClass = null;
protected String[] implementsInterfaces = null;
protected ISourceRange[] methods = null;
protected ISourceRange[] fields = null;
protected ISourceRange[] imports = null;
protected ICancelMonitor cancelMonitor = null;
protected ICompilationUnit referenceCU = null;
protected int changedIndex = -1;
protected boolean isChangedElementMethod = false;
protected String[] methodHandles = null;
protected String[] methodSkeletons = null;
protected String[] innerTypeHandles = null;
protected String[] innerTypeSkeletons = null;

protected List offendingLines = null;
IDiagramModelInstance fDiagram = null ;

/**
 * 
 */
public CodeSnippetTranslator(
			String entireCode, 
			String packageName, 
			String extendsClass, 
			String[] implementsInterfaces, 
			ISourceRange[] imports, 
			ISourceRange[] fields, 
			ISourceRange[] methods, 
			ICancelMonitor cancelMonitor, 
			ICompilationUnit referenceCU,      
			String[] methodHandles,
			String[] methodSkeletons,
			String[] innerTypeHandles,
			String[] innerTypeSkeletons,
			int changedIndex, 
			boolean changedType){
		this.entireCode = entireCode;
		this.packageName = packageName;
		this.extendsClass = extendsClass;
		this.implementsInterfaces = implementsInterfaces;
		this.imports = imports;
		this.fields = fields;
		this.methods = methods;
		this.cancelMonitor = cancelMonitor;
		this.referenceCU = referenceCU;
		this.changedIndex = changedIndex;
		this.isChangedElementMethod = changedType;
		this.methodHandles = methodHandles;
		this.methodSkeletons = methodSkeletons;
		this.innerTypeHandles = innerTypeHandles;
		this.innerTypeSkeletons = innerTypeSkeletons;
		
		if(this.cancelMonitor==null){
			this.cancelMonitor=new ICancelMonitor(){
				public boolean isCanceled(){
					return false;
				}
				public void setCompleted() {}
				public boolean isCompleted(boolean wantToWait) { return false ; }
				
			};
		}
}

public void setDiagram(IDiagramModelInstance d) {
    fDiagram = d ;
}

protected String getCodeSnippetClass(int whichMethod, boolean putOtherMethods){
	StringBuffer src = new StringBuffer();
	try{
		StringBuffer prefix = new StringBuffer();
		// import packageName
		if(packageName!=null){
			prefix.append("package "+packageName+" ;\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(imports!=null){
			importStarts=new int[imports.length];
			importEnds=new int[imports.length];
			for(int ic=0;ic<imports.length;ic++){
				importStarts[ic]=prefix.length();
				if(imports[ic]!=null)
					prefix.append(entireCode.substring(imports[ic].getOffset(), imports[ic].getOffset()+imports[ic].getLength())+"\n"); //$NON-NLS-1$
				importEnds[ic]=prefix.length(); // Remove ';'
			}
		}
		prefix.append("public class "+CODE_SNIPPET_CLASSNAME); //$NON-NLS-1$
		if(extendsClass!=null && extendsClass.length()>0)
			prefix.append(" extends "+extendsClass); //$NON-NLS-1$
		if(implementsInterfaces!=null && implementsInterfaces.length>0){
			prefix.append(" implements "); //$NON-NLS-1$
			for(int i=0;i<implementsInterfaces.length;i++){
				if(implementsInterfaces[i]!=null){
					prefix.append(implementsInterfaces[i]);
					if(i<implementsInterfaces.length-1)
						prefix.append(", "); //$NON-NLS-1$
				}
			}
		}
		prefix.append("{\n"); //$NON-NLS-1$
		if(innerTypeSkeletons!=null && innerTypeSkeletons.length>0){
			for (int innerC = 0; innerC < innerTypeSkeletons.length; innerC++) {
				prefix.append(innerTypeSkeletons[innerC]);
			}
		}
		prefix.append("public "+CODE_SNIPPET_CLASSNAME+"(){super();}\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		StringBuffer postfix=new StringBuffer("\n}"); //$NON-NLS-1$
		src.append(prefix);
		StringBuffer infix=new StringBuffer();
		if(fields!=null){
			fieldStarts=new int[fields.length];
			fieldEnds=new int[fields.length];
			for(int fc=0;fc<fields.length;fc++){
				fieldStarts[fc]=src.length()+infix.length();
				if(fields[fc]!=null)
					infix.append(entireCode.substring(fields[fc].getOffset(), fields[fc].getOffset()+fields[fc].getLength())+"\n"); //$NON-NLS-1$
				fieldEnds[fc]=src.length()+infix.length();
			}
		}
		if(methods!=null){
			for(int mc=0;mc<methods.length;mc++){
				if(mc==whichMethod){
					methodStarts=new int[1];
					methodEnds=new int[1];
					methodStarts[0]=src.length()+infix.length();
					if(methods[whichMethod]!=null)
						infix.append(entireCode.substring(methods[whichMethod].getOffset(), methods[whichMethod].getOffset()+methods[whichMethod].getLength())+"\n"); //$NON-NLS-1$
					methodEnds[0]=src.length()+infix.length();
				}else{
					if(putOtherMethods)
						if(methodSkeletons!=null && methodSkeletons[mc]!=null)
							infix.append(methodSkeletons[mc]);
				}
			}
		}
		src.append(infix);
		src.append(postfix);
	}catch(Exception e){
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	return src.toString();
}

/**
 * Supposed to handle changes in the declarations of fields - additions and
 * removals. Not that useful because the document is reloaded when such areas
 * are modified.
 */
protected ICodeDelta generateFieldsDelta(IBeanDeclModel oldModel, IBeanDeclModel newModel, List offenders, String methodHandle, String source) throws CodeGenException{
	return null;
}

/**
 * This method handles only the additions and deletions of methods (at the IType level).
 *  - If there is no method handle, then this method returns no delta.
 *  - If there is a method handle, then
 *     - If method is found in old one, and not in the new then it is deleted.
 * 	   - If method is found in new one, and not in the old then it is added.
 */
protected ICodeDelta generateTypesDelta(IBeanDeclModel oldModel, IBeanDeclModel newModel, List offenders, String methodHandle, String source) throws CodeGenException{
    if (methodHandle==null) return null ;
    
	CodeMethodRef oldMethod = (CodeMethodRef) oldModel.getMethodInitializingABean(methodHandle);
	if(oldMethod != null){
		// JCMMethod changed initializes a bean!
		if(newModel==null){
			// Parsing error - no model;
			return null;
		}else{
			if(newModel.getMethodInitializingABean(methodHandle) == null){
				// New model doesnt even have the method

				// NOTE:: If oldBDM has an initMethod and it is not  
				//        there in the newBDM,then it has been deleted.
					DefaultCodeDelta delta = new DefaultCodeDelta();
					IBeanDeclModel newBDM = CodeSnippetTranslatorHelper.createShadowBDM(oldModel, methodHandle);
					delta.addDeltaMethod(newBDM.getMethodInitializingABean(methodHandle));
					delta.setElementStatus(newBDM.getMethodInitializingABean(methodHandle), ICodeDelta.ELEMENT_DELETED);
					return delta;
			}
		}
	}
	else if (newModel!=null) {
	    if (newModel.getMethodInitializingABean(methodHandle) != null){
	        if(source!=null || source.length()>0){
					DefaultCodeDelta delta = new DefaultCodeDelta();
					//IBeanDeclModel newBDM = CodeSnippetTranslatorHelper.createShadowBDM(newModel, methodHandle);
					delta.addDeltaMethod(newModel.getMethodInitializingABean(methodHandle));
					delta.setElementStatus(newModel.getMethodInitializingABean(methodHandle), ICodeDelta.ELEMENT_ADDED);
					return delta;
			}else{
					return null; // parsing error.. let generateMethodsDelta() generate the delta
			}
	    }
	}
	return null;
}

/**
 * Generates a code delta, whose sole purpose is to update 
 * the offsets of the expressions in the main model. This is
 * necessary becuase the code is in a parse error, and we dont
 * want to halt the visual editing no matter the state of the
 * code.
 * 
 * Offsets of the expressions in the main model are updated, by
 * doing a very simple string match of the expressions in the new
 * modified source. Once a match is detected, the expressions is
 * added to the delta with the ICodeDelta.ELEMENT_UPDATED_OFFSETS
 * flag.
 */
protected ICodeDelta generateOffsetUpdatingDelta(IBeanDeclModel oldModel, String methodHandle, String source){
	DefaultCodeDelta delta = new DefaultCodeDelta();
	List ranges = CodeSnippetTranslatorHelper.removeAllComments(source);
	
	ranges.remove(0);
	CodeMethodRef method = oldModel.getMethodInitializingABean(methodHandle);
	if(method==null)
		return delta;
	IBeanDeclModel shadowModel = CodeSnippetTranslatorHelper.createShadowBDM(oldModel, methodHandle);
	if (shadowModel.getMethodInitializingABean(methodHandle) == null) return delta ;
	shadowModel.getMethodInitializingABean(methodHandle).setContent(source);
	Iterator newExpressions = shadowModel.getMethodInitializingABean(methodHandle).getExpressions();
	Iterator oldExpressions = oldModel.getMethodInitializingABean(methodHandle).getExpressions();
	delta.addDeltaMethod(shadowModel.getMethodInitializingABean(methodHandle));
	delta.setElementStatus(delta.getDeltaMethod(), ICodeDelta.ELEMENT_UNDETERMINED);
	while(newExpressions.hasNext() && oldExpressions.hasNext()){
		CodeExpressionRef newExpression = (CodeExpressionRef) newExpressions.next();
		CodeExpressionRef oldExpression = (CodeExpressionRef) oldExpressions.next();
		int index = -1;
		index = source.indexOf(oldExpression.getCodeContent(), index+1);
		for(int i=0;i<ranges.size() && index>-1;i++){
			ISourceRange range = (ISourceRange) ranges.get(i);
			if(((range.getOffset())<=index) && 
			   ((range.getOffset() + range.getLength())>index)){
			   	index = source.indexOf(oldExpression.getCodeContent(), index+1);
			}
		}
		if(index>-1){
			delta.setElementStatus(newExpression, ICodeDelta.ELEMENT_UPDATED_OFFSETS);
			newExpression.setContent(
				new ExpressionParser(source, index, oldExpression.getCodeContent().length()));
			newExpression.setOffset(index - newExpression.getFillerContent().length());
		}else{
			// We delete the old expression
            delta.setElementStatus(newExpression, ICodeDelta.ELEMENT_DELETED);
			newExpression.setState(CodeExpressionRef.STATE_EXP_IN_LIMBO, true); //newExpression.getState() | newExpression.STATE_EXP_IN_LIMBO);
		}
	}
	return delta;
}


protected void collectChangedExpressions(List oldExpressions, List newExpressions, Iterator oldI, Iterator newI, DefaultCodeDelta delta, List offenders) throws CodeGenException {

	while (oldI != null && oldI.hasNext())
		oldExpressions.add(oldI.next());
	while (newI != null && newI.hasNext())
		newExpressions.add(newI.next());
		
	for (int oldC = 0; oldC < oldExpressions.size() && !cancelMonitor.isCanceled(); oldC++) {
		CodeExpressionRef oe = (CodeExpressionRef) oldExpressions.get(oldC);
		int newC = 0;
		for (; newC < newExpressions.size() && !cancelMonitor.isCanceled(); newC++) {
			CodeExpressionRef ne = (CodeExpressionRef) newExpressions.get(newC);
			int index = isExpressionPresentInOffenders(ne, offenders);
			if (index > -1) {
				ne.setState(CodeExpressionRef.STATE_EXP_IN_LIMBO, true); //ne.getState()|ne.STATE_EXP_IN_LIMBO);
				TransientErrorEvent event = (TransientErrorEvent) offenders.get(index);
				event.setRefObject(oe.getBean().getEObject());
			}
			else {
				ne.setState(CodeExpressionRef.STATE_EXP_IN_LIMBO, false); //ne.getState()&(~ne.STATE_EXP_IN_LIMBO));
			}
			int res = oe.isEquivalent(ne);
			if (res == 0) {
				// Updated expressions
				delta.setElementStatus(ne, ICodeDelta.ELEMENT_CHANGED);
				oldExpressions.remove(oe);
				newExpressions.remove(ne);
				oldC--;
				newC--;
				break;
			}
			else {
				if (res == 1) {
					// No change absolutely
					if (ne.getOffset() == oe.getOffset())
						delta.setElementStatus(ne, ICodeDelta.ELEMENT_NO_CHANGE);
					else
						delta.setElementStatus(ne, ICodeDelta.ELEMENT_UPDATED_OFFSETS);
					oldExpressions.remove(oe);
					newExpressions.remove(ne);
					oldC--;
					newC--;
					break;
				}
			}
		}
		if (newC == newExpressions.size()) {
			// expression not found.. It is possible that it was not
			// defined in the context of this method (e.g., Style 2 VCE event expression
			if (oe.getMethod().getMethodHandle().equals(delta.getDeltaMethod().getMethodHandle())) 			
			   delta.setElementStatus(oe, ICodeDelta.ELEMENT_DELETED);
			else
			   delta.setElementStatus(oe, ICodeDelta.ELEMENT_NO_CHANGE);
		    oldExpressions.remove(oe);
			oldC--;
		}
	}
}

protected void processNewlyCollectedExpressions(IBeanDeclModel oldModel, List newExpressions, DefaultCodeDelta delta, List offenders) {

	for (int newC = 0; newC < newExpressions.size() && !cancelMonitor.isCanceled(); newC++) {
		int index = isExpressionPresentInOffenders((CodeExpressionRef) newExpressions.get(newC), offenders);
		if (index > -1) {
			TransientErrorEvent event = (TransientErrorEvent) offenders.get(index);
			BeanPart beanPart = oldModel.getABean(((CodeExpressionRef) newExpressions.get(newC)).getBean().getUniqueName());
			if (beanPart != null)
				event.setRefObject(beanPart.getEObject());
			delta.setElementStatus((CodeExpressionRef) newExpressions.get(newC), ICodeDelta.ELEMENT_UNDETERMINED);
			continue;
		}
		delta.setElementStatus((CodeExpressionRef) newExpressions.get(newC), ICodeDelta.ELEMENT_ADDED);
	}
}

protected void processPropertyExpressions(IBeanDeclModel oldModel, BeanPart oldBeanPart, BeanPart newBeanPart, DefaultCodeDelta delta, List offenders) throws CodeGenException {
	List oldExpressions = new ArrayList();
	List newExpressions = new ArrayList();
		
	// Process Property Expression ;		
	Iterator oldI = oldBeanPart==null?null:oldBeanPart.getRefExpressions().iterator();
	// Note that in the delta model, event expressions will be duplicated, as the decoders
	// had not changeds to fail decoding event expressions as property and remove them.
	Iterator newI = newBeanPart==null?null:newBeanPart.getRefExpressions().iterator();		
	// So that a EObject is available for equivalency
	newBeanPart.setProxy(oldBeanPart) ;	    
	collectChangedExpressions(oldExpressions, newExpressions, oldI, newI, delta, offenders) ;
	// So that a EObject is available for equivalency
	newBeanPart.setProxy(null) ;		
	processNewlyCollectedExpressions(oldModel, newExpressions, delta, offenders) ;	
	
}

protected void processEventExpressions(IBeanDeclModel oldModel, BeanPart oldBeanPart, BeanPart newBeanPart, DefaultCodeDelta delta, List offenders) throws CodeGenException {
	
	List oldExpressions = new ArrayList();
	List newExpressions = new ArrayList();
	
	// Now Process Event expressions
	Iterator oldI = oldBeanPart==null?null:oldBeanPart.getRefEventExpressions().iterator();
	Iterator newI = newBeanPart==null?null:newBeanPart.getRefEventExpressions().iterator();		
	// So that a EObject is available for equivalency
	newBeanPart.setProxy(oldBeanPart) ;	    
	collectChangedExpressions(oldExpressions, newExpressions, oldI, newI, delta, offenders) ;
	// So that a EObject is available for equivalency
	newBeanPart.setProxy(null) ;		
	processNewlyCollectedExpressions(oldModel, newExpressions, delta, offenders) ;	
}

protected ICodeDelta generateMethodsDelta(IBeanDeclModel oldModel, IBeanDeclModel newModel, List offenders, String methodHandle, String source) throws CodeGenException{
	if(newModel==null)
		return generateOffsetUpdatingDelta(oldModel, methodHandle, source);
	DefaultCodeDelta delta = new DefaultCodeDelta();
    
    // Keep track if any (internal) bean has been deleted.    
    ArrayList originalBeanList = new ArrayList() ;
    CodeMethodRef mr = oldModel.getMethodInitializingABean(methodHandle) ;
    if(mr==null)
    	return null;
    Iterator itr = oldModel.getBeans().iterator() ;
    while (itr.hasNext()) {
        BeanPart b = (BeanPart) itr.next();        
        if (mr.equals(b.getInitMethod()))
           originalBeanList.add(b) ;                      
    }
        
    
	for(int beanCount=0;beanCount<newModel.getBeans().size()&&!cancelMonitor.isCanceled();beanCount++){
		BeanPart newBeanPart = (BeanPart)newModel.getBeans().get(beanCount);
		BeanPart oldBeanPart = (BeanPart)oldModel.getABean(newBeanPart.getUniqueName());
        if (oldBeanPart!=null)
          originalBeanList.remove(oldBeanPart) ;  // Leave only those that have not been processed.
		if(oldBeanPart==null){
			// Old beanpart is null - could be because of 
			// (1) Its a new Local declaration (layout maybe)
			// (2) Original beanpart was not creatable (when the document was loaded)
			// So, we check if the beanpart has any expressions associated with it.
			if(newBeanPart.getInitMethod() == null){
				// Case (2) - Beanpart couldnt get created in the main BDM
				//            due to some MOF exception.
				continue;
			}else{
				// Case (1) - new bean part not present in old bdm - local declarations.
			}
		}
		CodeMethodRef newInitMethod = newBeanPart.getInitMethod();
		CodeMethodRef oldInitMethod = oldBeanPart==null?null:oldBeanPart.getInitMethod();
		if (newInitMethod == null)
		  if (oldInitMethod==null ||
		      !oldInitMethod.getMethodHandle().equals(methodHandle))
		      continue ;

		if(newInitMethod != null && oldInitMethod!=null && newInitMethod.getContent().equals(oldInitMethod.getContent()))
			continue; // No changes for this bean...  might be added due to inter method dependency.
		
		CodeMethodRef existingMR ;
		if (newInitMethod != null)
		  existingMR = newInitMethod ;
		else
		  existingMR = oldInitMethod ;	
		if(delta.getDeltaMethod()==null){
			delta.addDeltaMethod(existingMR);
			delta.setElementStatus(existingMR, ICodeDelta.ELEMENT_CHANGED);
		}
		
        processPropertyExpressions(oldModel, oldBeanPart, newBeanPart, delta, offenders) ;
        processEventExpressions(oldModel, oldBeanPart, newBeanPart, delta, offenders) ;	
		
	}
	if(delta!=null && delta.getDeltaMethod()!=null){
		Iterator iter = delta.getDeltaMethod().getExpressions();
		while (iter.hasNext()) {
			CodeExpressionRef exp = (CodeExpressionRef) iter.next();
			if(delta.getElementStatus(exp)<0){
				// uninteresting statements which 
				// havent been assigned a state
				delta.setElementStatus(exp, ICodeDelta.ELEMENT_NO_CHANGE);
			}
		}
	}
    // TODO fixme 
    if (originalBeanList.size() != 0) // At this time, let a reload occur
       throw new CodeGenException("Internal Beans were deleted; Reload - this is not an error") ; //$NON-NLS-1$
	return delta;
}

private int isExpressionPresentInOffenders(CodeExpressionRef exp, List offenders){
	String expCode = exp.getCodeContent();
	for(int i=0;i<offenders.size();i++){
		TransientErrorEvent evt = (TransientErrorEvent) offenders.get(i);
		String offCode = evt.getSource();
		if(offCode!=null && offCode.equals(expCode))
			return i;
	}
	return -1;
}

private IBeanDeclModel build(EditDomain d){
	// Try to compile the code snippet.
	try{
		if(isChangedElementMethod){
			// Change in a method. We know the index of the method which changed.
			offendingLines = CodeSnippetTranslatorHelper.determineOffendingParts(
									getCodeSnippetClass(changedIndex,true).toString(), 
									CODE_SNIPPET_CLASSNAME, 
									referenceCU.getJavaProject(), 
									changedIndex);
			if(offendingLines==null){
				// absolutely no problems...
                CodeSnippetModelBuilder mb = new CodeSnippetModelBuilder(d,
                                        getCodeSnippetClass(changedIndex,false).toString(), 
                                        new String[] {methodHandles[changedIndex]},
                                        importStarts, importEnds, 
                                        fieldStarts, fieldEnds, 
                                        methodStarts, methodEnds, referenceCU) ;
                mb.setDiagram(fDiagram) ;
				IBeanDeclModel newModel = mb.build() ;
				return newModel;
			}else{
				// some problems.. lets see if they are resolvable
				if(offendingLines.get(0) instanceof TransientErrorEvent){
					// Add the TransientErrorEvent problems to the error store.
					errorStrore.addAll(offendingLines);
					// resolvable..  remove that expression and return model
					CodeSnippetModelBuilder mb  = new CodeSnippetModelBuilder(d,
											getCodeSnippetClass(changedIndex,false).toString(), 
											new String[] {methodHandles[changedIndex]},
											// Build model WITH defective statement.. limbo state
											importStarts, importEnds, 
											fieldStarts, fieldEnds, 
											methodStarts, methodEnds, referenceCU) ;
                    mb.setDiagram(fDiagram) ;
                    IBeanDeclModel newModel = mb.build() ;
											
					return newModel;
				}else{
					// unresolvable.. just return no model (null)
					for(int i=0;i<offendingLines.size();i++){
						if(offendingLines.get(i) instanceof IProblem){
							IProblem problem = (IProblem) offendingLines.get(i);
							TransientErrorEvent event = 
								new TransientErrorEvent(
										TransientErrorEvent.TYPE_PARSER_ERROR, null, 
										problem.getSourceStart(), problem.getSourceEnd(), 
										problem.getMessage(), null, entireCode);
							errorStrore.add(event);
						}
					}
					return null;
				}
			}
		}else{
			// Field changes.. just build a totally new model and return
		}
	}catch(Exception e){
		JavaVEPlugin.log("*** Code Snippet in Error", Level.WARNING); //$NON-NLS-1$
		JavaVEPlugin.log(e, Level.WARNING);
		JavaVEPlugin.log("\n", Level.WARNING) ; //$NON-NLS-1$
	}
	return null;
}

private static String removeInternalReferences(String msg, String realTypeName){
	String newMsg = msg;
	if(newMsg.indexOf(CODE_SNIPPET_CLASSNAME)>-1){
		int from = newMsg.indexOf(CODE_SNIPPET_CLASSNAME);
		int to = from + CODE_SNIPPET_CLASSNAME.length();
		newMsg = newMsg.substring(0,from)+realTypeName+newMsg.substring(to,msg.length());
	}
	if(newMsg.indexOf(CodeSnippetTranslatorHelper.WRAPPER_CLASS_NAME)>-1){
		int from = newMsg.indexOf(CodeSnippetTranslatorHelper.WRAPPER_CLASS_NAME);
		int to = from + CodeSnippetTranslatorHelper.WRAPPER_CLASS_NAME.length();
		newMsg = newMsg.substring(0,from)+realTypeName+newMsg.substring(to,msg.length());
	}
	return newMsg;
}

/**
 * If the new model (deltaModel) is :
 *   (*) null - Means that it encountered parsing errors, and hence 
 *              totally ejected out.
 *   (*) not null - It was able to parse, maybe partially compile, but the 
 *                  variable offendingLines has the wrong lines.
 *
 * In the case that deltaModel is null, this is what is going to be done:
 *   (*) Use the parser, and remove comments of all types.
 *   (*) Use the old model to determine the offsets and lengths of the 
 *       expressions.
 *
 * @param mainModel	The old stable BDM which is used as a reference 
 *                      for the generation of CodeDelta
 * @param deltaModel 	The new model which is to update the reference model
 * @param source		The updated source code which has to be in the 
 *                      CodeDelta irrespective of the deltaModel
 */
public ICodeDelta generateCodeDelta(IBeanDeclModel mainModel, String methodHandle, String source) throws CodeGenException{
	resetErrorStore();
	IBeanDeclModel deltaModel = build(mainModel.getDomain());
	if(cancelMonitor!=null && cancelMonitor.isCanceled())
		return null;
	if(mainModel==null)
		return null; // No reference, hence no CodeDelta
	if(offendingLines==null)
		offendingLines = new ArrayList();
	ICodeDelta typeDelta = generateTypesDelta(mainModel, deltaModel, offendingLines, methodHandle, source);
	if(typeDelta!=null)
		return typeDelta;
	ICodeDelta fieldsDelta = generateFieldsDelta(mainModel, deltaModel, offendingLines, methodHandle, source);
	if(fieldsDelta!=null)
		return fieldsDelta;
	ICodeDelta methodsDelta = generateMethodsDelta(mainModel, deltaModel, offendingLines, methodHandle, source);
	return methodsDelta;
}

/**
 * A one-time returner of Errors in the previous
 * generateCodeDelta() call.
 */
public List getErrorsInCodeDelta(){
	List tempHolder = new ArrayList(errorStrore);
	Iterator itr = tempHolder.iterator();
	while (itr.hasNext()) {
		TransientErrorEvent evt = (TransientErrorEvent) itr.next();
		evt.setMessage(removeInternalReferences(evt.getMessage(),CodeGenUtil.getMainType(referenceCU).getElementName()));
	}
	resetErrorStore();
	return tempHolder;
}

private void resetErrorStore(){
	errorStrore = new ArrayList();
}

/**
 * Determines where in the first parameter, the second parameter
 * occurs. It ignores the presence of spaces in both Strings.
 * It returns two integers
 * 		Index where the second String starts
 *		Index where the second String stops
 *  
 * Ex: 
 *     main :   "ivjButton1.  setBackground   (new Color  (100, 2 , 1 )   )"
 *     find :   "new Color(   100,2   ,1)"
 *     
 *     returns: "ivjButton1.  setBackground   (new Color  (100, 2 , 1 )   )"
 *                                             ^                      ^
 *                                             |                      |
 */
public static int[] indexOfIgnoringSpace(String main, String find){
	if(main==null || main.length()<1 || find==null || find.length()<1)
		return new int[] {-1,-1};
	char[] mainTokens = main.toCharArray();
	StringTokenizer tokenizer = new StringTokenizer(find, " \t", false); //$NON-NLS-1$
	String findS = new String();
	while(tokenizer.hasMoreTokens())
		findS = findS.concat(tokenizer.nextToken());
	char[] findTokens = findS.toCharArray();
	int tokenFindStart = -1;
	int tokenFindEnd = -1;
	for(int mc=0;mc<mainTokens.length;mc++){
		if(Character.isWhitespace(mainTokens[mc]))
			continue;
		tokenFindStart = -1;
		tokenFindEnd = -1;
		if(mainTokens[mc]==findTokens[0]){
			tokenFindStart = mc;
			int fc=0;
			int nc=0;
			for(nc=mc; fc<findTokens.length && nc<mainTokens.length; nc++){
				if(Character.isWhitespace(mainTokens[nc]))
					continue;
				if(mainTokens[nc]==findTokens[fc]){
					fc++;
				}else{
					break;
				}
			}
			if(fc==findTokens.length){
				tokenFindEnd = nc-1;
				break;
			}
		}
	}
	if(tokenFindStart>-1 && tokenFindEnd>-1 && tokenFindStart<=tokenFindEnd){
		return new int[] {tokenFindStart, tokenFindEnd+1};
	}else{
		return new int[] {-1,-1};
	}
}

/**
 * Given a String, and an index to either a ')' or '(' brackets,
 * it returns the index of the corresponding bracket. It moves
 * left or right to find the necessary bracket.
 *
 * @param str  String in question
 * @param bracketIndex  Index of either ')' or '('
 * @return  Index of the corresponding bracket.
 */
public static int indexOfCorrespondingBracket(String str, int bracketIndex){
	char br = str.charAt(bracketIndex);
	char cbr = ' ';
	int direction=0;
	if(br==')'){
		cbr = '(';
		direction = -1;
	}else{
		if(br=='('){
			cbr = ')';
			direction = 1;
		}else{
			return -1;
		}
	}
	if(br==cbr || direction==0)
		return -1;
	char[] chars = str.toCharArray();
	int stackCount=0;
	for(int i=bracketIndex; i<chars.length && i>-1; i+=direction){
		if(chars[i]==br)
			stackCount++;
		if(chars[i]==cbr)
			stackCount--;
		if(stackCount==0)
			return i;
	}
	return -1;
}

}

