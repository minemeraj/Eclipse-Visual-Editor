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
 *  $RCSfile: TypeVisitor.java,v $
 *  $Revision: 1.5 $  $Date: 2004-04-15 19:34:09 $ 
 */

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule;
import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

public class TypeVisitor extends SourceVisitor {
	

	CodeTypeRef fType = null ;	
	String[] methodHandles = null;
	
	boolean forceJDOMUsage = false;
	char[]	content = null;  // is set when JDOM is not set
	Map  fInstanceDeclaredBeans = new HashMap() ;
	JavaElementInfo[]	JDTMethods = null;
	
public TypeVisitor (TypeDeclaration node, IBeanDeclModel model, char [] content, String[] methodHandles, List reTryList, boolean forceJDOMUsage) {
	this(node,model,reTryList,forceJDOMUsage) ;		
	this.methodHandles = methodHandles;
	this.content = content;
}

public TypeVisitor (TypeDeclaration node, IBeanDeclModel model,List reTryList, boolean forceJDOMUsage) {
	super(node,model,reTryList) ;	
	this.forceJDOMUsage = forceJDOMUsage;
	fType = new CodeTypeRef (node,model) ;
	model.setTypeRef(fType) ;
}

public TypeVisitor (CodeTypeRef tr, TypeDeclaration node, IBeanDeclModel model,List reTryList, boolean forceJDOMUsage) {
	super(node,model,reTryList) ;	
	this.forceJDOMUsage = forceJDOMUsage;
	fType = tr ;
}

/**
 *  Build a JCMMethod array which corresponds to the parsed methods array
 *  @return  Array of IMethods
 */
public static  JavaElementInfo[]  getCUMethods(MethodDeclaration aMethods[], JavaElementInfo[] elements, IBeanDeclModel model) {

	// Assumes order is the same
	ArrayList   methods = new ArrayList() ;
	if (elements == null || elements.length == 0) return null ;
	// Pad the array with these 
	for (int i=0; i<aMethods.length; i++) {
		String Name = new String (aMethods[i].getName().getIdentifier()) ;
		int j,Prev = 0 ;
		// Deal with duplicate names
		int dupCount = 0 ;
		for (i=0; i<methods.size(); i++) {
		    if (methods.get(i)!= null && ((JavaElementInfo)methods.get(i)).getName().equals(Name))
		      dupCount++ ;
		}
		for (j=Prev; j<elements.length; j++) {
			if (Name.equals(elements[j].getName())) {
			    dupCount-- ;
			    if (dupCount<0)
			      break ;
			}
		}
		if (j>=elements.length) {
			// JCMMethod declared implicitly (e.g., empty constructor) ;
			methods.add(null) ;
		}
		else {
		    methods.add(elements[j]) ;				
		    Prev=j+1 ;
		}
	}
	
	
	return (JavaElementInfo[]) methods.toArray(new JavaElementInfo[elements.length]) ;	
}


protected void createThisIfNecessary() {
    
        
    IThisReferenceRule thisRule = (IThisReferenceRule) CodeGenUtil.getEditorStyle(fModel).getRule(IThisReferenceRule.RULE_ID) ;
    
    String superName = null ;
    if (fType.getTypeDecl().getSuperclass() != null)
      superName = CodeGenUtil.resolve(fType.getTypeDecl().getSuperclass(),fModel) ;
    ResourceSet rs = fModel.getCompositionModel().getModelResourceSet() ;
    // The rule uses MOF reflection to introspect attributes : this works when the file is saved at this point.
    // So, try the super first    
    if ((superName!= null && (thisRule.useInheritance(superName,rs)) || thisRule.useInheritance(fType.getName(),rs))) {
        BeanPartFactory bpg = new BeanPartFactory(fType.getBeanModel(),null) ;
        // No Init method yet.
	   	bpg.createThisBeanPartIfNeeded(null) ;	 
    }
}




public void addFieldToMap(BeanPart bp, String method) {
	List l = (List) fInstanceDeclaredBeans.get(method) ;
	if (l == null) {
		l = new ArrayList() ;
		fInstanceDeclaredBeans.put(method,l) ;
	}
	l.add(bp) ;
}

protected void visitAMethod(MethodDeclaration method, IBeanDeclModel model,List reTryList,CodeTypeRef typeRef, String methodHandle, ISourceRange range, String content) {
	String mName = method.getName().getIdentifier();
	MethodVisitor v = new MethodVisitor(method,model,reTryList,typeRef,methodHandle,	range,content) ;
	// Check to see if the rule gave us an init method up front
	if (fInstanceDeclaredBeans.get(mName) != null) {
		Iterator itr = ((List)fInstanceDeclaredBeans.get(mName)).iterator() ;
		while (itr.hasNext())
			v.setInitMethodFor((BeanPart)itr.next()) ;
	}
	v.visit() ;
}

public void visit()  {
	// First look a instance variables 
	FieldDeclaration[] fields = fType.getTypeDecl().getFields();
	IInstanceVariableRule instVarRule = (IInstanceVariableRule) CodeGenUtil.getEditorStyle(fModel).getRule(IInstanceVariableRule.RULE_ID) ;
	if (fields != null) 
		for (int i=0 ; i<fields.length; i++) {
			// Should we skip this field ??
			if (instVarRule!=null && instVarRule.ignoreVariable(fields[i],fModel,fModel.getCompositionModel())) continue ;
			BeanPart bp = new BeanPart(fields[i]) ;
			fModel.addBean(bp) ;
			String overidInitMethod = instVarRule.getDefaultInitializationMethod(fields[i], fModel, (TypeDeclaration)fVisitedNode) ;
			if (overidInitMethod!=null) {			
			  addFieldToMap(bp, overidInitMethod) ;
			  bp.setInstanceInstantiation(true) ;
			}
		}
  
     
	createThisIfNecessary() ;

	// Assume method's order is the same
	MethodDeclaration[] methods = fType.getTypeDecl().getMethods();
	if (forceJDOMUsage) {
		// No compilation unit... depend on jdom solely
		if(methods==null || methods.length==0)
			return;
		try{
			int methodHandleUseCount = 0;
			for(int i=0; i<methods.length; i++){
				if(	(methods[i]!=null) &&
					(methods[i] instanceof MethodDeclaration) &&
					(!((MethodDeclaration)methods[i]).isConstructor())){
					String thisMethodHandle = ""; //$NON-NLS-1$
					if (methods[i] instanceof MethodDeclaration) {
						thisMethodHandle = methodHandles[methodHandleUseCount];
						methodHandleUseCount++;						
					}
// TODO Need to move the updateMethodOffset to this visitor, so that the logic is encapsulated
   					
					// Please see CodeSnippetModelBuilder.updateMethodOffsets() to see how
					// the declaration Source starts, and the declaration source ends are 
					// being modified. This is due to inconsistency between JDOM and JDT.
					visitAMethod(methods[i], fModel, fReTryLater, fType, thisMethodHandle, // null,
								 getSourceRange(methods[i].getStartPosition(),methods[i].getStartPosition()+methods[i].getLength()), 
								 String.copyValueOf(content,methods[i].getStartPosition(),methods[i].getLength()));
				}
			}
		}catch(Exception e){
			JavaVEPlugin.log(e, Level.WARNING) ;
		}
	}else{
		JavaElementInfo cuMethods[] = getCUMethods(methods, JDTMethods, fModel);
		if(cuMethods==null || cuMethods.length<1)
			return;
		// Compilation unit methods and jdom methods should match.
		if (cuMethods.length != methods.length) 
			throw new RuntimeException("methods length error") ; //$NON-NLS-1$
		int i=0 ;
		try {
			for (; i < methods.length ; i++){
				// Visit each method with the correct visitor
				if ( cuMethods[i] != null && 
					methods[i] instanceof MethodDeclaration ) {
					visitAMethod(methods[i],fModel,fReTryLater,fType,((JavaElementInfo)cuMethods[i]).getHandle(),
								cuMethods[i].getSourceRange(),
								cuMethods[i].getContent()) ;
				}
			}
		}catch (Exception e) {
			JavaVEPlugin.log ("TypeVisitor.visit() could not visit"+String.valueOf(methods[i].getName().getIdentifier())+" : "+e.getMessage(), Level.WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}

protected ISourceRange getSourceRange(int start, int end){
	final int offset = start;
	final int length = end-start+1;
	return new ISourceRange(){
		public int getOffset(){return offset;}
		public int getLength(){return length;}
	};
}

/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	
	return super.toString();
}

public void setJDTMethods(JavaElementInfo[] methods) {
	JDTMethods = methods;
}

}