/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FieldNameValidator.java,v $
 *  $Revision: 1.11 $  $Date: 2006-05-17 20:14:53 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.util.CharacterUtil;
import org.eclipse.jem.util.CharacterUtil.StringIterator;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeMethodRef;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.propertysheet.ISourced;
import org.eclipse.ve.internal.propertysheet.ISourcedPropertyDescriptor;
 
/**
 * 
 * @since 1.0.0
 */
public class FieldNameValidator implements ICellEditorValidator, ISourced {
	static class VarNameVisitor extends ASTVisitor{
		private boolean nameFound = false;
		private String name = null;
		public VarNameVisitor(String name){
			this.name = name;
		}
		public boolean visit(VariableDeclarationFragment node) {
			if(node.getName().getIdentifier().equals(name))
				nameFound = true;
			return super.visit(node);
		}
		public boolean nameFound(){
			return nameFound;
		}
	}
	private Object[] sources = null;
	private IPropertySource[] propertySources = null;
	private IPropertyDescriptor[] propertyDescriptors = null;
	
	protected BeanPart getBeanPart(EObject obj){
		Object beanAdapter = EcoreUtil.getExistingAdapter(obj, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		if(beanAdapter!=null && beanAdapter instanceof BeanDecoderAdapter){
			if(((BeanDecoderAdapter)beanAdapter).getBeanPart()!=null)
				return ((BeanDecoderAdapter)beanAdapter).getBeanPart();
		}
		Object annotationAdapter = EcoreUtil.getExistingAdapter(obj, ICodeGenAdapter.JVE_CODEGEN_ANNOTATION_ADAPTER);
		if(annotationAdapter!=null && annotationAdapter instanceof AnnotationDecoderAdapter){
			IAnnotationDecoder decoder = ((AnnotationDecoderAdapter)annotationAdapter).getDecoder();
			if(decoder!=null && decoder.getBeanPart()!=null)
				return decoder.getBeanPart();
		}
		return null;
	}
	
	private static final Set KEYWORDS = new HashSet(20);
	static {
		// This is the list of keywords as of Java 5.0. 
		KEYWORDS.add("abstract"); //$NON-NLS-1$
		KEYWORDS.add("assert"); //$NON-NLS-1$
		KEYWORDS.add("byte"); //$NON-NLS-1$
		KEYWORDS.add("boolean"); //$NON-NLS-1$
		KEYWORDS.add("break"); //$NON-NLS-1$
		KEYWORDS.add("case"); //$NON-NLS-1$
		KEYWORDS.add("char"); //$NON-NLS-1$
		KEYWORDS.add("catch"); //$NON-NLS-1$
		KEYWORDS.add("class"); //$NON-NLS-1$
		KEYWORDS.add("continue"); //$NON-NLS-1$
		KEYWORDS.add("do"); //$NON-NLS-1$
		KEYWORDS.add("double"); //$NON-NLS-1$
		KEYWORDS.add("default"); //$NON-NLS-1$
		KEYWORDS.add("else"); //$NON-NLS-1$
		KEYWORDS.add("extends"); //$NON-NLS-1$
		KEYWORDS.add("for"); //$NON-NLS-1$
		KEYWORDS.add("final"); //$NON-NLS-1$
		KEYWORDS.add("float"); //$NON-NLS-1$
		KEYWORDS.add("false"); //$NON-NLS-1$
		KEYWORDS.add("finally"); //$NON-NLS-1$
		KEYWORDS.add("if"); //$NON-NLS-1$
		KEYWORDS.add("int"); //$NON-NLS-1$
		KEYWORDS.add("import"); //$NON-NLS-1$
		KEYWORDS.add("interface"); //$NON-NLS-1$
		KEYWORDS.add("implements"); //$NON-NLS-1$
		KEYWORDS.add("instanceof"); //$NON-NLS-1$
		KEYWORDS.add("long"); //$NON-NLS-1$
		KEYWORDS.add("new"); //$NON-NLS-1$
		KEYWORDS.add("null"); //$NON-NLS-1$
		KEYWORDS.add("native"); //$NON-NLS-1$
		KEYWORDS.add("public"); //$NON-NLS-1$
		KEYWORDS.add("package"); //$NON-NLS-1$
		KEYWORDS.add("private"); //$NON-NLS-1$
		KEYWORDS.add("protected"); //$NON-NLS-1$
		KEYWORDS.add("return"); //$NON-NLS-1$
		KEYWORDS.add("short"); //$NON-NLS-1$
		KEYWORDS.add("super"); //$NON-NLS-1$
		KEYWORDS.add("static"); //$NON-NLS-1$
		KEYWORDS.add("switch"); //$NON-NLS-1$
		KEYWORDS.add("strictfp"); //$NON-NLS-1$
		KEYWORDS.add("synchronized"); //$NON-NLS-1$
		KEYWORDS.add("try"); //$NON-NLS-1$
		KEYWORDS.add("this"); //$NON-NLS-1$
		KEYWORDS.add("true"); //$NON-NLS-1$
		KEYWORDS.add("throw"); //$NON-NLS-1$
		KEYWORDS.add("throws"); //$NON-NLS-1$
		KEYWORDS.add("transient"); //$NON-NLS-1$
		KEYWORDS.add("void"); //$NON-NLS-1$
		KEYWORDS.add("volatile"); //$NON-NLS-1$
		KEYWORDS.add("while"); //$NON-NLS-1$
	}
	
	/**
	 * Determines if a given name is a valid java variable name or not. 
	 * if invalid a reason is given, and <code>null</code> is returned.
	 *  
	 * @param name
	 * @return  <code>null</code> if valid, else reason
	 * 
	 * @since 1.2.0
	 */
	public static String isValidName(String name){
		if(name==null || name.length()<1)
			return CodegenEditorPartMessages.FieldNameValidator_InvalidVariableName_INFO_; 
		if (KEYWORDS.contains(name))
			return CodegenEditorPartMessages.FieldNameValidator_InvalidVariableName_INFO_;
		StringIterator charIter = new StringIterator(name);
		if(!CharacterUtil.isJavaIdentifierStart(charIter.next()))
			return CodegenEditorPartMessages.FieldNameValidator_InvalidVariableName_INFO_; 			
		while (charIter.hasNext()) {
			if(!CharacterUtil.isJavaIdentifierPart(charIter.next()))
				return CodegenEditorPartMessages.FieldNameValidator_InvalidVariableName_INFO_; 
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if (value instanceof String) {
			String name = (String) value;
			if(name.equals(getCurrentName()))
				return null;
			String isValidName = isValidName(name);
			if(isValidName!=null)
				return isValidName;
			if(sources!=null && sources.length>0 && sources[0] instanceof EObject){
				EObject instance = (EObject) sources[0];
				BeanPart bp = getBeanPart(instance);
				if(bp!=null && bp.getModel().getCompilationUnit()!=null){
					ICompilationUnit cu = bp.getModel().getCompilationUnit();
					if(bp.getDecleration().isInstanceVar()){
						if(isDuplicateField(cu, name))
							return CodegenEditorPartMessages.FieldNameValidator_VariableNameExists_INFO_; 
					}else{
						if(isDuplicateField(cu, name) || isDuplicateLocalVar(cu, bp, name))
							return CodegenEditorPartMessages.FieldNameValidator_VariableNameExists_INFO_; 
					}
				}
			}
		}
		return null;
	}
	protected boolean isDuplicateField(ICompilationUnit cu, String name){
		IType type = CodeGenUtil.getMainType(cu);
		if(type!=null){
			try {
				IField[] fields = type.getFields();
				for (int fc = 0; fc < fields.length; fc++) {
					if(fields[fc].getElementName().equals(name))
						return true;
				}
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e, Level.FINER);
			}
		}
		return false;
	}
	protected boolean isDuplicateLocalVar(ICompilationUnit cu, BeanPart bp, String name){
		IType mainType = CodeGenUtil.getMainType(cu);
		if(bp.getInitMethod()!=null && mainType!=null){
			CodeMethodRef initMethod = bp.getInitMethod();
			IMethod iInitmethod = CodeGenUtil.getMethod(mainType, initMethod.getMethodHandle());
			if(iInitmethod!=null){
				try {
					ASTParser parser = ASTParser.newParser(AST.JLS2);
					String wrapperSource = "class WRAPPER_CLASS{\r\n"+iInitmethod.getSource()+"\r\n}"; //$NON-NLS-1$ //$NON-NLS-2$
					parser.setSource(wrapperSource.toCharArray());
					ASTNode wrapperNode= parser.createAST(null);
					VarNameVisitor varNameVisitor = new VarNameVisitor(name);
					wrapperNode.accept(varNameVisitor);
					return varNameVisitor.nameFound();
				} catch (JavaModelException e) {
					JavaVEPlugin.log(e, Level.FINER);
				}
			}
		}
		return false;
	}
	private String getCurrentName() {
		// Some may use special ways of getting the name in composition for the current source, so we will
		// use the property source and the property descriptor to get the name.
		// There will only be one, can't handle more than one source, it doesn't make sense because since
		// they need to be unique, we can't set more than one to the same name.
		if (propertyDescriptors[0] instanceof ISourcedPropertyDescriptor)
			return (String) ((ISourcedPropertyDescriptor) propertyDescriptors[0]).getValue(propertySources[0]);
		else
			return (String) propertySources[0].getPropertyValue(propertyDescriptors[0]);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.ISourced#setSources(java.lang.Object[], org.eclipse.ui.views.properties.IPropertySource[], org.eclipse.ui.views.properties.IPropertyDescriptor[])
	 */
	public void setSources(Object[] sources, IPropertySource[] propertySources,
			IPropertyDescriptor[] descriptors) {
		this.sources = sources;
		this.propertySources = propertySources;
		this.propertyDescriptors = descriptors;
	}
}
