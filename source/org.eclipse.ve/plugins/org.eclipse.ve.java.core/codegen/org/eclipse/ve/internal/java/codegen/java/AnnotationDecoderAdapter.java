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
package org.eclipse.ve.internal.java.codegen.java;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.cdm.AnnotationEMF;
import org.eclipse.ve.internal.cdm.VisualInfo;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import com.ibm.icu.util.ULocale;



/**
 * @version 	1.0
 * @author
 */
public class AnnotationDecoderAdapter implements ICodeGenAdapter {

	private static HashMap cuToRenameRequestCollectorMap = new HashMap();
	public static RenameRequestCollector getRenamecogetRenameCollector(ICompilationUnit cu){
		if(cuToRenameRequestCollectorMap.containsKey(cu))
			return (RenameRequestCollector) cuToRenameRequestCollectorMap.get(cu);
		return null;
	}
	public static void setRenameRequestCollector(ICompilationUnit cu, RenameRequestCollector collector){
		cuToRenameRequestCollectorMap.put(cu, collector);
	}
	static class BeanPartNodesFinder extends ASTVisitor {
		
		String varName = null;
		int varFirstOffset = -1;
		int currentVarOffset = -1;
		IMethod iMethod = null;
		
		SimpleName variableName = null;
		SimpleName variableMethodName = null;
		
		public BeanPartNodesFinder(String varName, int varFirstOffset, IMethod iMethod){
			this.varName = varName;
			this.varFirstOffset = varFirstOffset;
			// we only rename get<varName> type method
			if(iMethod!=null){
				String methodName = iMethod.getElementName().toLowerCase(ULocale.getDefault().toLocale());
				String expectedName = "get"+varName.toLowerCase(ULocale.getDefault().toLocale()); //$NON-NLS-1$
				if(expectedName.equals(methodName)){
					this.iMethod = iMethod;
				}else{
					expectedName = "create"+varName.toLowerCase(ULocale.getDefault().toLocale()); //$NON-NLS-1$
					if(expectedName.equals(methodName)){
						this.iMethod = iMethod;
					}
				}
			}
		}
		
		public boolean visit(SimpleName node) {
			if(node.getIdentifier().equals(varName)) // The type name is of interest
				processVariableName(node);
			return super.visit(node);
		}

		private void processVariableName(SimpleName possibleVariableName) {
			if(possibleVariableName!=null && !(possibleVariableName.getStartPosition()<varFirstOffset)){ // Only after the first
				int possibleVariableNameStart = possibleVariableName.getStartPosition();
				if(currentVarOffset==-1 || (possibleVariableNameStart < currentVarOffset)){ // the closest to 'varFirstOffset'
					variableName = possibleVariableName;
					currentVarOffset = possibleVariableNameStart;
				}
			}
		}
		
		public SimpleName getVariableName(){
			return variableName;
		}
		
		public SimpleName getVariableMethodName(){
			return variableMethodName;
		}
		
		public boolean visit(MethodDeclaration node) {
			if(iMethod!=null && variableMethodName==null){
				if(node.getName().getIdentifier().equals(iMethod.getElementName())){
					try{
						String iMethodReturnTypeName = Signature.toString(iMethod.getReturnType());
						String astMethodReturnTypeName = null;
						Type astMethodReturnType = node.getReturnType();
						if(astMethodReturnType.isSimpleType())
							astMethodReturnTypeName = ((SimpleType)astMethodReturnType).getName().getFullyQualifiedName();
						else if(astMethodReturnType.isQualifiedType())
							astMethodReturnTypeName = ((QualifiedType)astMethodReturnType).getName().getFullyQualifiedName();
						else if(astMethodReturnType.isPrimitiveType())
							astMethodReturnTypeName = ((PrimitiveType)astMethodReturnType).getPrimitiveTypeCode().toString();
						if(iMethodReturnTypeName!=null && iMethodReturnTypeName.equals(astMethodReturnTypeName)){
							if(node.parameters().size()==iMethod.getParameterTypes().length){
								boolean allParamsEqual = true;
								if(node.parameters().size()>0){
									String[] iMethodParams = iMethod.getParameterTypes();
									for (int pc = 0; pc < iMethodParams.length; pc++) {
										SingleVariableDeclaration svd = (SingleVariableDeclaration) node.parameters().get(pc);
										String nodeParamType = CodeGenUtil.getTypeName(svd.getType());
										String iMethodParamType = Signature.toString(iMethodParams[pc]);
										if(	(nodeParamType==null && iMethodParamType!=null) ||
											(nodeParamType!=null && iMethodParamType==null)){
											allParamsEqual = false;
											break;
										}else if(nodeParamType!=null && iMethodParamType!=null){
											if(!nodeParamType.equals(iMethodParamType)){
												allParamsEqual = false;
												break;
											}
										}
									}
								}
								if(allParamsEqual)
									variableMethodName = node.getName();
							}
						}
					}catch (JavaModelException e) {
						JavaVEPlugin.log(e, Level.FINEST);
					}
				}
			}
			return super.visit(node);
		}
		
	}

	protected static boolean isValidMetaComment(LineComment comment, String source, IScannerFactory scannerFactory){
		if(	comment==null || 
				source==null || 
				source.length()<1 || 
				comment.getStartPosition()<0 || 
				(comment.getStartPosition()+comment.getLength())>source.length())
			return false;
		String commentSource = source.substring(comment.getStartPosition(), comment.getStartPosition()+comment.getLength());
		String annotationSource = FreeFormAnnotationTemplate.getCurrentAnnotation(commentSource, scannerFactory);
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
				//	we need to look at end of field instead of starting as field could span multiple lines! (99637)
				// Need to determine the offset of the ';' because it decides ultimately the ending of the declaration.
				int fieldStartOffset = field.getStartPosition();
				int fieldEndOffset = field.getStartPosition() + field.getLength();
				int lastSourceCharOffset = fieldEndOffset;
				if(field.getType()!=null)
					lastSourceCharOffset = field.getType().getStartPosition()+field.getType().getLength();
				if(field.fragments().size()>0){
					// Find the ending of the last fragment
					VariableDeclarationFragment vdf = (VariableDeclarationFragment) field.fragments().get(field.fragments().size()-1);
					lastSourceCharOffset = vdf.getStartPosition() + vdf.getLength();
				}
				int semiColonLocation = astSource.indexOf(';', lastSourceCharOffset);
				if(semiColonLocation<fieldStartOffset || semiColonLocation>fieldEndOffset)
					semiColonLocation = lastSourceCharOffset;
				
				int fieldLineNumber = cuNode.lineNumber(semiColonLocation); // we check for the type's location, cos previous comments effect the field's start position
				List comments = ((CompilationUnit)node).getCommentList();
				for (int cc = 0; cc < comments.size(); cc++) {
					if (comments.get(cc) instanceof LineComment){ 
						LineComment lineComment = (LineComment) comments.get(cc);
						int commentLine = cuNode.lineNumber(lineComment.getStartPosition());
						if(commentLine==fieldLineNumber){
							return isValidMetaComment(lineComment, astSource, bdm.getScannerFactory());
						}
						if(commentLine==fieldLineNumber+1){
							if(isValidMetaComment(lineComment, astSource, bdm.getScannerFactory())){
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
				// we need to look at end of decl instead of starting as decl could span multiple lines! (99637)
				int fieldLineNumber = cuNode.lineNumber(varDecl.getStartPosition()+varDecl.getLength());  
				List comments = ((CompilationUnit)node).getCommentList();
				for (int cc = 0; cc < comments.size(); cc++) {
					if (comments.get(cc) instanceof LineComment){ 
						LineComment lineComment = (LineComment) comments.get(cc);
						int commentLine = cuNode.lineNumber(lineComment.getStartPosition());
						if(commentLine==fieldLineNumber){
							return isValidMetaComment(lineComment, astSource, bdm.getScannerFactory());
						}
						if(commentLine==fieldLineNumber+1){
							if(isValidMetaComment(lineComment, astSource, bdm.getScannerFactory())){
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
			return false;
		index = commentSource.indexOf(FreeFormAnnotationTemplate.VISUAL_CONTENT_TYPE, index) ;
		return index>-1;
	}
  
  protected IAnnotationDecoder fDecoder=null ;

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

/**
 * Returns a method corresponding to the bean. The return method 
 * is given if it is present, else the init method is returned.
 * 
 * @param cu
 * @param bp
 * @return
 * 
 * @since 1.1.0.1
 */
protected IMethod getBeanMethod(ICompilationUnit cu, BeanPart bp){
	IMethod method = null;
	if(bp.getReturnedMethod()!=null){
		IType mainType = CodeGenUtil.getMainType(cu);
		if(mainType!=null){
			method = CodeGenUtil.getMethod(mainType, bp.getReturnedMethod().getMethodHandle());
		}
	}
	if(method==null && bp.getInitMethod()!=null){
		IType mainType = CodeGenUtil.getMainType(cu);
		if(method==null){
			method = CodeGenUtil.getMethod(mainType, bp.getInitMethod().getMethodHandle());
		}
	}
	return method;
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

protected void performLocalRename(final ICompilationUnit compilationUnit, BeanPart nameChangedBP, final String newFieldName, final IMethod method){
	// Perform all local renames in one step
	RenameRequestCollector collector = getRenamecogetRenameCollector(compilationUnit);
	if(collector==null){
		collector = new RenameRequestCollector(nameChangedBP.getModel(), compilationUnit); 
		setRenameRequestCollector(compilationUnit, collector); // the collector removes itself from the map after it has run.
		// If a dialog opens, the main shell loses focus and property is re-applied,
		// hence do this process async()
		// WARNING - NO BDM members should be in the async, as it will be stale
		nameChangedBP.getModel().getDomain().getEditorPart().getEditorSite().getShell().getDisplay().asyncExec(collector);
	}
	
	int initExpOffset = -1;
	if(nameChangedBP.getInitExpression()!=null && nameChangedBP.getInitMethod()!=null){
		initExpOffset = nameChangedBP.getInitExpression().getOffset() + nameChangedBP.getInitMethod().getOffset();
	}
	final int afterOffset = initExpOffset;
	final String oldVarName = nameChangedBP.getSimpleName();
	
	collector.addRequest(oldVarName, afterOffset, newFieldName, method);
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
		IMethod method = getBeanMethod(cu, nameChangedBP);
		performLocalRename(cu, nameChangedBP, newFieldName, method);
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
