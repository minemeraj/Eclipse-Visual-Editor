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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: AnnotationDecoderAdapter.java,v $
 *  $Revision: 1.26 $  $Date: 2005-07-01 20:21:04 $ 
 */
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringSavePreferences;
import org.eclipse.jdt.internal.ui.text.correction.LinkedNamesAssistProposal;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorSite;

import org.eclipse.ve.internal.cdm.AnnotationEMF;
import org.eclipse.ve.internal.cdm.VisualInfo;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorPart;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;



/**
 * @version 	1.0
 * @author
 */
public class AnnotationDecoderAdapter implements ICodeGenAdapter {

	static class BPVarFinderVisitor extends ASTVisitor {
		String varName = null;
		String methodName = null;
		String[] methodParamTypes = null;
		boolean isInValidMethod = false;
		boolean isInValidField = false;
		boolean isSimpleType = false;
		SimpleName variableName = null;
		Stack isInValidMethodStack = null;
		
		public BPVarFinderVisitor(String varName, String methodName, String[] methodParamTypes){
			this.varName = varName;
			this.methodName = methodName;
			this.methodParamTypes = methodParamTypes;
			this.isInValidMethodStack = new Stack();
		}
		
		public boolean visit(FieldDeclaration node) {
			isInValidField = methodName==null || methodName.length()<1;
			isSimpleType = node.getType().isSimpleType();
			return super.visit(node);
		}
		public void endVisit(FieldDeclaration node) {
			isInValidField = false;
			super.endVisit(node);
		}
		public boolean visit(MethodDeclaration node) {
			isInValidMethodStack.push(new Boolean(isInValidMethod));
			if(methodName!=null && methodName.length()>0 && methodParamTypes!=null){
				if(methodName.equals(node.getName().getIdentifier())){
					List params = node.parameters();
					if(params.size()==methodParamTypes.length){
						boolean allParamsEqual = true;
						for (int pc = 0; pc < params.size(); pc++) {
							SingleVariableDeclaration svd = (SingleVariableDeclaration) params.get(pc);
							String type = getTypeFromAST(svd.getType());
							if(!type.equals(methodParamTypes[pc])){
								allParamsEqual = false;
								break;
							}
						}
						isInValidMethod = allParamsEqual;
					}
				}
			}
			return super.visit(node);
		}
		public void endVisit(MethodDeclaration node) {
			isInValidMethod = ((Boolean) isInValidMethodStack.pop()).booleanValue();
			super.endVisit(node);
		}
		public boolean visit(VariableDeclarationFragment node) {
			if(isSimpleType && (isInValidMethod || isInValidField) && variableName==null){
				// The type name is of interest
				if(node.getName().getIdentifier().equals(varName))
					variableName = node.getName();
			}
			return super.visit(node);
		}
		
		public boolean visit(VariableDeclarationStatement node) {
			if(isInValidMethod){
				isSimpleType = node.getType().isSimpleType();
			}
			return super.visit(node);
		}
		public void endVisit(VariableDeclarationStatement node) {
			isSimpleType = false;
			super.endVisit(node);
		}
		
		public SimpleName getVariableName(){
			return variableName;
		}

		public boolean visit(SingleVariableDeclaration node) {
			if(isSimpleType && (isInValidMethod || isInValidField) && variableName==null){
				// The type name is of interest
				if(node.getName().getIdentifier().equals(varName))
					variableName = node.getName();
			}
			return super.visit(node);
		}
	}
	
	static class MethodNameFinderVisitor extends ASTVisitor{
		SimpleName methodNameAST = null;
		String methodName = null;
		String[] methodParamTypes = null;

		public MethodNameFinderVisitor(String methodName, String[] methodParamTypes){
			this.methodName = methodName;
			this.methodParamTypes = methodParamTypes;
		}
		
		public boolean visit(MethodDeclaration node) {
			if(methodName!=null && methodName.length()>0 && methodParamTypes!=null){
				if(methodName.equals(node.getName().getIdentifier())){
					List params = node.parameters();
					if(params.size()==methodParamTypes.length){
						boolean allParamsEqual = true;
						for (int pc = 0; pc < params.size(); pc++) {
							SingleVariableDeclaration svd = (SingleVariableDeclaration) params.get(pc);
							String type = getTypeFromAST(svd.getType());
							if(!type.equals(methodParamTypes[pc])){
								allParamsEqual = false;
								break;
							}
						}
						if(allParamsEqual){
							methodNameAST = node.getName();
						}
					}
				}
			}
			return super.visit(node);
		}
		
		
		/**
		 * @return Returns the methodNameAST.
		 */
		public SimpleName getMethodName() {
			return methodNameAST;
		}
	}
	
	protected static boolean isValidMetaComment(LineComment comment, String source){
		if(	comment==null || 
				source==null || 
				source.length()<1 || 
				comment.getStartPosition()<0 || 
				(comment.getStartPosition()+comment.getLength())>source.length())
			return false;
		String commentSource = source.substring(comment.getStartPosition(), comment.getStartPosition()+comment.getLength());
		String annotationSource = FreeFormAnnotationTemplate.getCurrentAnnotation(commentSource, new DefaultScannerFactory());
		if(annotationSource!=null && annotationSource.indexOf(FreeFormAnnotationTemplate.VISUAL_INFO_DECL)>-1)
			return true;
		return false;
	}
	
	/**
	 * Return whether the passed in declaration's annotation has the text 'decl-index=' inside it. If this 
	 * text is present, then the declaration should be of interest for VE. This method should have the same
	 * behavior as isDeclarationParseable(VariableDeclarationStatement)
	 * 
	 * @param field 
	 * @param bdm - BDM to provide the latest source
	 * @return
	 * @see #isDeclarationParseable(VariableDeclarationStatement)
	 * @since 1.0.0
	 */
	public static boolean isDeclarationParseable(FieldDeclaration field, IBeanDeclModel bdm) {
		try{
			TypeDeclaration td = bdm.getTypeRef().getTypeDecl(); // get the latest AST nodes
			ASTNode node = td;
			while(node!=null && !(node instanceof CompilationUnit))
				node = node.getParent();
			
			String astSource = (String) td.getProperty(JavaBeanModelBuilder.ASTNODE_SOURCE_PROPERTY);
			if(node!=null){
				CompilationUnit cuNode = (CompilationUnit) node;
				int fieldLineNumber = cuNode.lineNumber(field.getType().getStartPosition()); // we check for the type's location, cos previous comments effect the field's start position
				List comments = ((CompilationUnit)node).getCommentList();
				for (int cc = 0; cc < comments.size(); cc++) {
					if (comments.get(cc) instanceof LineComment){ 
						LineComment lineComment = (LineComment) comments.get(cc);
						int commentLine = cuNode.lineNumber(lineComment.getStartPosition());
						if(commentLine==fieldLineNumber){
							return isValidMetaComment(lineComment, astSource);
						}
						if(commentLine==fieldLineNumber+1){
							if(isValidMetaComment(lineComment, astSource)){
								// This line comment is in the next line - check to see that no 
								// other field declarations are present in that line.
								FieldDeclaration[] fields = ((TypeDeclaration)cuNode.types().get(0)).getFields();
								for (int fc = 0; fc < fields.length; fc++) {
									if(	!fields[fc].equals(field) &&
											cuNode.lineNumber(fields[fc].getType().getStartPosition())==fieldLineNumber+1)
										return false;
								}
								return true;
							}
						}
					}
				}
			}
		}catch(Throwable e){
			JavaVEPlugin.log(e, Level.FINE);
		}
		return false;
	}
	
	/**
	 * Return whether the passed in declaration's annotation has the text 'decl-index=' inside it. If this 
	 * text is present, then the declaration should be of interest for VE. This method should have the same
	 * behavior as isDeclarationParseable(VariableDeclarationStatement)
	 * 
	 * @param field 
	 * @param bdm - BDM to provide the latest source
	 * @return
	 * @see #isDeclarationParseable(FieldDeclaration)
	 * @since 1.0.0
	 */
	public static boolean isDeclarationParseable(VariableDeclarationStatement varDecl, IBeanDeclModel bdm) {
		try{
			TypeDeclaration td = bdm.getTypeRef().getTypeDecl(); // get the latest AST nodes
			ASTNode node = td;
			while(node!=null && !(node instanceof CompilationUnit))
				node = node.getParent();
			
			String astSource = (String) td.getProperty(JavaBeanModelBuilder.ASTNODE_SOURCE_PROPERTY);
			if(node!=null){
				CompilationUnit cuNode = (CompilationUnit) node;
				int fieldLineNumber = cuNode.lineNumber(varDecl.getStartPosition());
				List comments = ((CompilationUnit)node).getCommentList();
				for (int cc = 0; cc < comments.size(); cc++) {
					if (comments.get(cc) instanceof LineComment){ 
						LineComment lineComment = (LineComment) comments.get(cc);
						int commentLine = cuNode.lineNumber(lineComment.getStartPosition());
						if(commentLine==fieldLineNumber){
							return isValidMetaComment(lineComment, astSource);
						}
						if(commentLine==fieldLineNumber+1){
							if(isValidMetaComment(lineComment, astSource)){
								// This line comment is in the next line - check to see that no 
								// other field declarations are present in that line.
								if (varDecl.getParent() instanceof Block) {
									Block block = (Block) varDecl.getParent();
									for(int sc=0;sc<block.statements().size();sc++){
										Statement stmt = (Statement) block.statements().get(sc);
										if(	!stmt.equals(varDecl) &&
												cuNode.lineNumber(stmt.getStartPosition())==fieldLineNumber+1)
											return false;
									}
									return true;
								}
								return false;
							}
						}
					}
				}
			}
		}catch(Throwable e){
			JavaVEPlugin.log(e, Level.FINE);
		}
		return false;
	}
	
	public static boolean isBeanVisible(String commentSource){
		if(commentSource==null)
			return false;
		int index = commentSource.indexOf(FreeFormAnnotationTemplate.ANNOTATION_SIG, 0) ;
		if(index<0)
			return true;
		index = commentSource.indexOf(FreeFormAnnotationTemplate.VISUAL_CONTENT_TYPE, index) ;
		return index>-1;
	}
  
  protected IAnnotationDecoder fDecoder=null ;
  static RenameRequestCollector renameCollector;

public AnnotationDecoderAdapter(IAnnotationDecoder decoder) {
	super() ;
	fDecoder = decoder ;
}

/**
 * isAdaptorForType.
 */
public boolean isAdapterForType(Object type) {
	return JVE_CODE_GEN_TYPE.equals(type)||
	        JVE_CODEGEN_ANNOTATION_ADAPTER.equals(type);
}

protected IField getField(ICompilationUnit cu, BeanPart bp){
	IField field = null;
	if(bp.getDecleration().isInstanceVar() && cu.findPrimaryType()!=null){
		field = cu.findPrimaryType().getField(bp.getSimpleName());
	}
	return field;
}

protected IMethod getReturnMethod(ICompilationUnit cu, BeanPart bp){
	IMethod returnMethod = null;
	if(bp.getDecleration().isInstanceVar() && bp.getReturnedMethod()!=null && cu.findPrimaryType()!=null){
		try {
			TypeResolver resolver = bp.getModel().getResolver();
			IMethod[] methods = cu.findPrimaryType().getMethods();
			for (int mc = 0; mc < methods.length; mc++) {
				String returnType = Signature.toString(methods[mc].getReturnType());
				if(	returnType!=null &&
						!returnType.equals("void") && //$NON-NLS-1$
						bp.getReturnedMethod().getMethodName().equals(methods[mc].getElementName())) {
					Resolved r = resolver.resolveType(returnType);
					if (r != null && bp.getType().equals(r.getName())){
						returnMethod = methods[mc];
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			JavaVEPlugin.log(e, Level.FINE);
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.FINE);
		}
	}
	return returnMethod;
}

public static String getNameFromAST(Name name){
	if(name.isQualifiedName()){
		QualifiedName qn = (QualifiedName) name;
		return getNameFromAST(qn.getQualifier()) + qn.getName().getIdentifier();
	}else if(name.isSimpleName()){
		return ((SimpleName)name).getIdentifier();
	}
	return new String();
}

public static String getTypeFromAST(Type type){
	String t = new String();
	if(type.isArrayType()){
		ArrayType at = (ArrayType) type;
		t = getTypeFromAST(at.getComponentType()) + "[]"; //$NON-NLS-1$
	}else if(type.isPrimitiveType()){
		t = ((PrimitiveType)type).getPrimitiveTypeCode().toString();
	}else if(type.isQualifiedType()){
		QualifiedType qt = (QualifiedType) type;
		t = getTypeFromAST(qt.getQualifier()) + qt.getName().getIdentifier() ;
	}else if(type.isSimpleType()){
		t = getNameFromAST(((SimpleType)type).getName());
	}
	return t;
}

protected Object[] getInitMethodNameAndParamNames(ICompilationUnit cu, BeanPart bp){
	String mName = null;
	String[] pTypes= null;
	CodeMethodRef initMethod = bp.getInitMethod();
	if(initMethod!=null){
		IType type = CodeGenUtil.getMainType(cu);
		if(type!=null){
			// First try to get the IMethod, next try to get the AST method declaration
			IMethod initMethodJDT = (initMethod.getMethodHandle()!=null) ? CodeGenUtil.getMethod(type, initMethod.getMethodHandle()):null;
			if(initMethodJDT!=null){
				mName = initMethodJDT.getElementName();
				String[] pTs = initMethodJDT.getParameterTypes();
				pTypes = new String[pTs.length];
				for (int pc = 0; pc < pTs.length; pc++) {
					pTypes[pc] = Signature.toString(pTs[pc]);
				}
			}else if(initMethod.getDeclMethod()!=null){
				MethodDeclaration mDecl = initMethod.getDeclMethod();
				mName = mDecl.getName().getIdentifier();
				List ps = mDecl.parameters();
				pTypes = new String[ps.size()];
				for (int pc=0;pc<mDecl.parameters().size();pc++) {
					SingleVariableDeclaration param = (SingleVariableDeclaration) ps.get(pc);
					Type paramType = param.getType();
					pTypes[pc] = getTypeFromAST(paramType);
				}
			}
		}
	}
	return new Object[]{mName, pTypes};
}

protected void performLocalRename(final ICompilationUnit cu, BeanPart nameChangedBP, final String newFieldName, final IMethod bpRetMethod){
	// Perform all local renames in one step
	if(renameCollector==null){
		renameCollector = new RenameRequestCollector(nameChangedBP.getModel());
		// If a dialog opens, the main shell loses focus and property is re-applied,
		// hence do this process async()
		// WARNING - NO BDM members should be in the async, as it will be stale
		nameChangedBP.getModel().getDomain().getEditorPart().getEditorSite().getShell().getDisplay().asyncExec(renameCollector);
	}

	final String oldVarName = nameChangedBP.getSimpleName();
	Object[] ret;
	if(nameChangedBP.getDecleration().isInstanceVar()){
		ret = new Object[]{null, null};
	}else{
		ret = getInitMethodNameAndParamNames(cu, nameChangedBP);
	}
	final String methodName =(String)ret[0];
	final String[] paramNames = (String[])ret[1];
	final CompilationUnitEditor cuEditor = (CompilationUnitEditor) nameChangedBP.getModel().getDomain().getEditorPart();
	final ITextViewer viewer = ((JavaVisualEditorPart)nameChangedBP.getModel().getDomain().getEditorPart()).getViewer();
	renameCollector.addRequest(
		new Runnable() {
			public void run() {
			  try {
				ASTParser parser = ASTParser.newParser(AST.JLS2);
				parser.setSource(cu);
				CompilationUnit cuNode = (CompilationUnit) parser.createAST(null);
				BPVarFinderVisitor visitor = new BPVarFinderVisitor(oldVarName, methodName, paramNames);
				cuNode.accept(visitor);
				if(visitor.getVariableName()!=null){
					LinkedNamesAssistProposal prop = new LinkedNamesAssistProposal("Renaming variable", cu, visitor.getVariableName(), newFieldName);
					char triggerChar = (newFieldName!=null && newFieldName.length()>0) ? newFieldName.charAt(newFieldName.length()-1) : 0;
					int triggerPosition = visitor.getVariableName().getStartPosition()+newFieldName.length()-1;
					prop.apply(viewer, triggerChar, 0, triggerPosition);
					cuEditor.aboutToBeReconciled();
					if(bpRetMethod!=null){
						String methodName = bpRetMethod.getElementName().toLowerCase(Locale.getDefault());
						String varName = visitor.getVariableName().getIdentifier();
						String varNameLower = varName.toLowerCase(Locale.getDefault());
						if(	methodName.startsWith("get") && //$NON-NLS-1$
								methodName.indexOf(varNameLower)==3 &&
								methodName.length()==varNameLower.length()+3){
									//	Excatly of the form get<VarName>()
									String newMethodName = "get" + newFieldName.toUpperCase(Locale.getDefault()).charAt(0) + newFieldName.substring(1); //$NON-NLS-1$
									parser = ASTParser.newParser(AST.JLS2);
									parser.setSource(cu);
									cuNode = (CompilationUnit) parser.createAST(null);
									String getterMethodName = bpRetMethod.getElementName();
									String[] getterParamSigs = bpRetMethod.getParameterTypes();
									String[] getterParamTypes = new String[getterParamSigs.length];
									for (int pc = 0; pc < getterParamSigs.length; pc++) {
										getterParamTypes[pc] = Signature.toString(getterParamSigs[pc]);
									}
									MethodNameFinderVisitor mVisitor = new MethodNameFinderVisitor(getterMethodName, getterParamTypes);
									cuNode.accept(mVisitor);
									if(mVisitor.getMethodName()!=null){
										prop = new LinkedNamesAssistProposal("Renaming method", cu, mVisitor.getMethodName(), newMethodName);
										triggerChar = newMethodName.charAt(newMethodName.length()-1) ;
										triggerPosition = mVisitor.getMethodName().getStartPosition()+newMethodName.length()-1;
										prop.apply(viewer, triggerChar, 0, triggerPosition);
										cuEditor.aboutToBeReconciled();
									}
						}
					}
				}
			  } catch (RuntimeException e) {
					JavaVEPlugin.log(e);
			  }
			}
		 }
		
	);
}

protected void performGlobalRename(BeanPart nameChangedBP, final IField bpField, final String newFieldName){
	final IEditorSite es = nameChangedBP.getModel().getDomain().getEditorPart().getEditorSite();
	// If a dialog opens, the main shell loses focus and property is re-applied,
	// hence do this process async()
	// WARNING - NO BDM members should be in the async, as it will be stale
	nameChangedBP.getModel().getDomain().getEditorPart().getEditorSite().getShell().getDisplay().asyncExec(
		new Runnable() {
			public void run() {
				boolean previousRefactoringSaveEditors = RefactoringSavePreferences.getSaveAllEditors();
				RefactoringSavePreferences.setSaveAllEditors(true);
				try {
					RenameSupport rename =
							RenameSupport.create(
								bpField,
								newFieldName,
								RenameSupport.UPDATE_GETTER_METHOD
									| RenameSupport.UPDATE_TEXTUAL_MATCHES
									| RenameSupport.UPDATE_REFERENCES
									| RenameSupport.UPDATE_SETTER_METHOD);
					rename.perform(es.getShell(), es.getWorkbenchWindow());
				} catch (CoreException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (InterruptedException e) {
				} catch (InvocationTargetException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}finally{
					RefactoringSavePreferences.setSaveAllEditors(previousRefactoringSaveEditors);
				}
			}
		}
	);
}

protected void renameField(Notification msg){
	Map.Entry entry = (Map.Entry) msg.getNewValue();
	final String newFieldName = (String) entry.getValue();
	BeanPart nameChangedBP = fDecoder.getBeanPart();
	if(nameChangedBP==null){
		EObject nameChangedbean = ((AnnotationEMF) msg.getNotifier()).getAnnotates();
		nameChangedBP = fDecoder.getBeanModel().getABean(nameChangedbean);
	}
	if(nameChangedBP!=null){
		ICompilationUnit cu = fDecoder.getBeanModel().getCompilationUnit();
		
		final IField bpField = getField(cu, nameChangedBP);
		IMethod bpRetMethod = getReturnMethod(cu, nameChangedBP);
		boolean needLocalRename = true;
		try {
			boolean isFieldPrivate = bpField!=null && Flags.isPrivate(bpField.getFlags());
			boolean isMethodPrivate = bpRetMethod!=null && Flags.isPrivate(bpRetMethod.getFlags());
			needLocalRename = (bpField==null || isFieldPrivate) && (bpRetMethod==null || isMethodPrivate);
		} catch (JavaModelException e1) {
			JavaVEPlugin.log(e1, Level.FINE);
		}
		
		if(needLocalRename)
			performLocalRename(cu, nameChangedBP, newFieldName, bpRetMethod);
		else
			performGlobalRename(nameChangedBP, bpField, newFieldName);
	}
}

/**
 * applied: A setting has been applied to the mof object,
 * notify the decoder
 */ 
public void notifyChanged(Notification msg){

  try {
      
      if (fDecoder.getBeanModel() == null  || fDecoder.getBeanPart().getModel() == null ||
          fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL) ||
          fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET)) return  ;
      
      if (msg.getFeature() == null) return ;
      String action=null ;
	switch ( msg.getEventType() ) {		     
		case Notification.SET : 
			 if (!CDEUtilities.isUnset(msg)) {
				     action = msg.isTouch() ? "TOUCH" : "SET" ;		      //$NON-NLS-1$	//$NON-NLS-2$
				     break ;
			 }	// else flow into unset.
		case Notification.UNSET : 
		     action = "UNSET" ; //$NON-NLS-1$
		     break ;		     
		case Notification.ADD :
		     action = "ADD" ; //$NON-NLS-1$
		     break ;
		case Notification.REMOVE :
		     action = "REMOVE" ; //$NON-NLS-1$
		     break ;
		case Notification.REMOVING_ADAPTER:
		     action = "REMOVING_ADAPTER" ;		      //$NON-NLS-1$
		     break;
	}
	
	if (JavaVEPlugin.isLoggingLevel(Level.FINE)) {
		JavaVEPlugin.log(this+" action= "+action, Level.FINE) ; //$NON-NLS-1$
		JavaVEPlugin.log("SourceRange ="+getJavaSourceRange(), Level.FINE) ;	 //$NON-NLS-1$
	}
	
	  // Note: this doesn't handle add_many/remove_many, but currently we don't do that.
      switch (msg.getEventType()) {

        case Notification.UNSET:
        case Notification.REMOVE:
             if (msg.getOldValue() instanceof VisualInfo) {
                  ((Notifier)msg.getOldValue()).eAdapters().remove(this) ;
             }
             fDecoder.reflectMOFchange() ;	             
			 break;
			 
        case Notification.SET:     
        case Notification.ADD:
             if (msg.getOldValue() instanceof VisualInfo) {
                  ((Notifier)msg.getOldValue()).eAdapters().remove(this) ;
             }        
              if (msg.getNewValue() instanceof VisualInfo) {
                  ((EObject)msg.getNewValue()).eAdapters().add(this) ;
                  return ;
              }
             if (msg.getNewValue() instanceof Map.Entry) {
				Map.Entry entry = (Map.Entry) msg.getNewValue();
				if(	entry.getKey() instanceof String  && 
						entry.getValue() instanceof String && 
						((String)entry.getKey()).equals(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY) &&
						msg.getNotifier() instanceof AnnotationEMF)
				renameField(msg);
			}
        case Notification.MOVE:  
        if (msg.getNewValue() instanceof BasicEMap.Entry) {
        	// No point to notify the decocder if not a constraint key 
        	if (!((BasicEMap.Entry)msg.getNewValue()).getKey().equals(CDMModelConstants.VISUAL_CONSTRAINT_KEY))
        		return ;
        }
	    fDecoder.reflectMOFchange() ;		     
	    break ;      	
      }
  }
  catch (Throwable t) {
      JavaVEPlugin.log(t, Level.WARNING) ;
  }
}


public IAnnotationDecoder getDecoder() {
	return fDecoder ;
}

/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	// Insert code to print the receiver here.
	// This implementation forwards the message to super.
	// You may replace or supplement this.
	return "\tAnnotationDecoderAdapter:"+fDecoder; //$NON-NLS-1$
}

public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException {
    //return fDecoder.getExprRef().getTargetSourceRange() ;
    return null ;
}

/**
 * @see ICodeGenAdapter#getBDMSourceRange()
 */
public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException {
    return null;
}

/**
 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getSelectionSourceRange()
 */
public ICodeGenSourceRange getHighlightSourceRange()
	throws CodeGenException {
	return getJavaSourceRange();
}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return null;
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(Notifier)
	 */
	public void setTarget(Notifier newTarget) {
	}

}
