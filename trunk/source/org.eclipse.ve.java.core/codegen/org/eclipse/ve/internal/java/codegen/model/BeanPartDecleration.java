/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Mar 23, 2005 by Gili Mendel
 * 
 * It is possible that multiple bean instances share a single decleration
 * e.g., a Label is declared once, but the instance/local variable is reused with a different
 * constructor.  This implies that multiple instance will share a single instance/local var. name,
 * 
 * BeanParts with the same name/Scope will reUse a single BeanPartDecleration 
 * 
 *  $RCSfile: BeanPartDecleration.java,v $
 *  $Revision: 1.7 $  $Date: 2005-07-12 18:41:11 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
 

/**
 * 
 * @since 1.1.0
 */
public class BeanPartDecleration {
		
	
	ASTNode fieldDecl = null ;
	String  fieldDeclHandle = null ;   // IField handle
	String  declerationHandle = null;  // BeanPartDeclerationHandle provide a unique decl. name
	String 	name = null ;  // Decleration simple Name
	String 	type = null;
	boolean typeResolved = false;

	CodeMethodRef declaredMethod = null;  // if null it is an instance variable
	boolean  inModel = false;             // A declaration will only be inserted to the BDM when the scope is resolved
	
	IBeanDeclModel	model = null ;    
		
	// This deleration relates to the following instances
	List    beanParts = new ArrayList();
	
	public BeanPartDecleration (String name) {
		this.name = name;		
	}
	public BeanPartDecleration (String name, String type) {
		this (name);
		setType(type);
	}
	
	public BeanPartDecleration (FieldDeclaration fd) {
		this (((VariableDeclaration)fd.fragments().get(0)).getName().getIdentifier());
		setFieldDecl(fd);	
		setType(fd.getType());
		setDeclaringMethod(null);
	}
	
	public BeanPartDecleration (String name, CodeMethodRef method) {
		this(name);
		setDeclaringMethod(method);
	}
	
	public BeanPartDecleration (VariableDeclarationStatement vd) {
		this (((VariableDeclaration)vd.fragments().get(0)).getName().getIdentifier());
		setFieldDecl(vd);
		setType(vd.getType());		
	}
	
	public ASTNode getFieldDecl() {
		return fieldDecl;
	}
	public void setFieldDecl(ASTNode fieldDecl) {
		this.fieldDecl = fieldDecl;
	}
	public String getType() {
		return type;
	}
	
	protected void resolveType(Name ast) {
	    if (model != null && type !=null && !typeResolved) { 
	    	if (type.equals("")) //$NON-NLS-1$
	    		typeResolved=true;
	    	else {
		    	if(model.getCompilationUnit()!=null){
		    	  Resolved rt;
		    	  if (ast==null)
		    	  	rt =getModel().getResolver().resolveType(type);
		    	  else
		    	  	rt = getModel().getResolver().resolveType(ast);
				  if(rt!=null) {
				  	type = rt.getName();
				  	typeResolved = true;
				  }
		    	}
	    	}
	    }
	}
	
	public void setType(String t) {
		if (this.type==null) {
		  this.type = t;
		  resolveType(null);
		}
	}
	public void setType(Name t){
		if (type==null) {
		  type = t.getFullyQualifiedName();
		  resolveType(t);
		}
	}
	
	public void setType (Type t) {
		if (t instanceof SimpleType)
			setType(((SimpleType)t).getName());
		else if (t instanceof PrimitiveType)
			setType(((PrimitiveType)t).getPrimitiveTypeCode().toString());	
	}
	
	public String getName() {
		return name;
	}
	public void addBeanPart(BeanPart bp) {
		if (!bp.getSimpleName().equals(name))
			throw new IllegalArgumentException();
		if (!beanParts.contains(bp))
			beanParts.add(bp);			
	}
	
	public void refreshDeclerationSource() {		
		for (int i = 0; i < beanParts.size(); i++) {
			CodeExpressionRef e = ((BeanPart)beanParts.get(i)).getInitExpression();
			if (e != null) {
				// May need to add, or remove decleration
				e.getExpressionDecoder().reflectMOFchange();
			}
		}
	}
	
	public void removeBeanPart (BeanPart bp) {		
		beanParts.remove(bp);
		// If this is the last declared bean, remove this from the BDM
		if (beanParts.size()==0) {
			setModel(null);
		}
	}
	public BeanPart[] getBeanParts() {
		return (BeanPart[]) beanParts.toArray(new BeanPart[beanParts.size()]);
	}	
	
	/**
	 * Returns the index of the passed in bean part with reference
	 * to similarly named bean parts in the same method. The determination
	 * of the index is based on the offsets of the init expressions. If no
	 * init expression is there for a bean, then that bean is placed last.
	 * 
	 * Ex:
	 * 		GridLayout layout = new GridLayout();
	 * 		...
	 * 		layout = new GridLayout(GridLayout.FILL_BOTH);
	 * 		...
	 * 
	 * @param bp
	 * @return  index of the passed in bean with reference to other 
	 * 			beans sharing the same declaration
	 * 
	 * @since 1.0.2
	 */
	public int getBeanPartIndex(BeanPart bp){
		CodeExpressionRef bpInitExp = bp.getInitExpression();
		int bpInitExpOffset = bpInitExp==null?Integer.MAX_VALUE:bpInitExp.getOffset();
		BeanPart[] bps = getBeanParts();
		int bpIndex = 0;
		for (int i = 0; i < bps.length; i++) {
			if(bp==bps[i])
				continue;
			CodeExpressionRef bpsInitExp = bps[i].getInitExpression();
			int bpsInitExpOffset = bpsInitExp==null?Integer.MAX_VALUE:bpsInitExp.getOffset();
			if(bpsInitExpOffset<bpInitExpOffset)
				bpIndex++;
		}
		return bpIndex;
	}
	
	public boolean isInstanceVar() {
		return declaredMethod==null;
	}
	/**
	 * 
	 * @param method if null, it implies an Instance Variable
	 * 
	 * @since 1.1.0
	 */
	public void setDeclaringMethod(CodeMethodRef method) {
		this.declaredMethod = method;
		declerationHandle = createDeclerationHandle(method,name);
	}
	
	public String getFieldDeclHandle() {
		return fieldDeclHandle;
	}
	public void setFieldDeclHandle(String fieldDeclHandle) {
		this.fieldDeclHandle = fieldDeclHandle;
	}
	public IBeanDeclModel getModel() {
		return model;
	}
	
	/**
	 * This is where the Decleration will be resolved to a single common one
	 * A call to this method will set the model, and set the BeanPart with the a 
	 * common decleration.
	 * 
	 * @param model
	 * 
	 * @since 1.1.0
	 */
	public void setModel(IBeanDeclModel model) {
		if (this.model == model) 
			return ;
        IBeanDeclModel currentModel = this.model;
		this.model = model;
		if (model == null)	{
			if (currentModel!=null)
			   currentModel.removeBeanDecleration(this);
		}
		else {
			resolveType(null);
			if (!isInModel())
			 model.addBeanDecleration(this);
		}
	}
	
	public boolean isInModel() {
		return model!=null && model.getModelDecleration(this) == this;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		if (!isInModel())
			b.append("*Shadow* "); //$NON-NLS-1$
		if (isInstanceVar())
			b.append("InstanceVar: "); //$NON-NLS-1$
		else {
			b.append(">"); //$NON-NLS-1$
			b.append(declaredMethod.getMethodName());
			b.append("()<: "); //$NON-NLS-1$
		}
		b.append(beanParts.size()+" BeanParts "); //$NON-NLS-1$
		b.append("["+name+" ("+type+")]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return b.toString();
	}
	public String getDeclerationHandle() {
		if (declerationHandle==null) 
			throw new IllegalAccessError();
		return declerationHandle;
	}
	
	private static String createDeclerationHandle(String methodHandle, String simpleName){
		if (methodHandle!=null)
		     return methodHandle+"^"+simpleName; //$NON-NLS-1$
		else
			 return simpleName; //$NON-NLS-1$
	}

	public static String createDeclerationHandle(IMethod method, String simpleName){
		return createDeclerationHandle(method==null? null:method.getHandleIdentifier(), simpleName);
	}

	public static String createDeclerationHandle(CodeMethodRef method, String simpleName){
		return createDeclerationHandle(method==null? null:method.getMethodHandle(),simpleName);
	}
	
	public String getUniqueHandle (BeanPart bp) {
		return getDeclerationHandle();
	}
	
	/**
	 * 
	 * @return The instance of bean that relates to this offset within a method.
	 * 
	 * @since 1.1.0
	 */
	public BeanPart getBeanPart(CodeMethodRef m, int off) {
		CodeExpressionRef exp = null;
		for (int i = 0; i < beanParts.size(); i++) {
			BeanPart b = (BeanPart) beanParts.get(i);
			CodeExpressionRef e = b.getInitExpression();
			// Shaddow BDM will have a different CodeMethodRef
			if (e!=null && e.getMethod().getMethodName().equals(m.getMethodName())) {
				if (e.getOffset()<=off) {
					if (exp==null)
						exp = e;
					else if (e.getOffset()>exp.getOffset())
						    exp = e;
				}
			}
		}
		BeanPart bp = exp==null?null:exp.getBean();
		if(bp!=null && !bp.isActive()){
			bp.activate();
		}
		return bp;
	}
	/**
	 * 
	 * @return the first BeanPart that does not have an init expression yet.
	 * 
	 * @since 1.1.0
	 */
	public BeanPart getBeanPartWithNoInitExpression() {
		for (int i = 0; i < beanParts.size(); i++) {
			BeanPart b = (BeanPart) beanParts.get(i);
			CodeExpressionRef e = b.getInitExpression();
			if (e==null)
				return b;
		}
		return null;
	}
	
	public BeanPart createAnotherBeanPartInstance(CodeMethodRef initMethod) {
		int index = beanParts.size();
		BeanPart bp = new BeanPart(this);
		bp.setUniqueIndex(index);
		bp.addInitMethod(initMethod);
		model.addBean(bp);
		return bp;		
	}
	
	/**
	 * 
	 * @return true if this decleration is reused by multiple instances
	 *         false if this instance decleration is not shared
	 * 
	 * @since 1.1.0
	 */
	public boolean isSingleDecleration() {
		return beanParts.size()<=1;
	}
}
