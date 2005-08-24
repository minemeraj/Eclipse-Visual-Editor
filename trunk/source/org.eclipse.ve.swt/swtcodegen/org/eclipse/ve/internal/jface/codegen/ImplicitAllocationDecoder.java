/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ImplicitAllocationDecoder.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.jface.codegen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter;
import org.eclipse.ve.internal.java.codegen.java.IExpressionDecoder;
import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
 

public class ImplicitAllocationDecoder implements IExpressionDecoder{

	protected SingleVariableDeclaration singleVariableDeclaration = null;
	protected CodeExpressionRef codeExpressionRef = null;
	protected IBeanDeclModel beanModel = null;
	protected IVEModelInstance compositonModel = null;
	protected BeanPart implicitChildBeanpart = null;
	protected BeanPart implicitParentBeanpart = null;
	protected String implicitSFName = null;
	protected EStructuralFeature implicitSF;
	
	public ImplicitAllocationDecoder(BeanPart implicitParentBeanpart, BeanPart implicitChildBeanpart, CodeExpressionRef exp, String implcitSFName) {
		this.implicitParentBeanpart = implicitParentBeanpart;
		this.implicitChildBeanpart = implicitChildBeanpart;
		this.implicitSFName = implcitSFName;
		this.codeExpressionRef = exp;
	}

	public String generate(EStructuralFeature sf, Object[] args) throws CodeGenException {
		return null;
	}

	public List getChildren(IJavaObjectInstance component) {
		return new ArrayList();
	}

	public boolean isImplicit(Object[] args) {
		return true;
	}

	public EStructuralFeature getSF() {
		if(implicitSF==null){
			if(implicitParentBeanpart!=null && implicitParentBeanpart.getEObject()!=null)
				setSF(implicitParentBeanpart.getEObject().eClass().getEStructuralFeature(implicitSFName));
		}
		return implicitSF;
	}

	public void setSF(EStructuralFeature sf) {
		this.implicitSF = sf;
	}

	public Object[] getArgsHandles(Statement expr) throws CodeGenException {
		return null;
	}

	public boolean isRelevantFeature(EStructuralFeature sf) {
		if(getSF()!=null)
			return getSF().equals(sf);
		return false;
	}

	public Object[] getAddedInstance() {
		EObject addedInstance = implicitChildBeanpart.getEObject();
		if(addedInstance!=null)
			return new Object[]{addedInstance, addedInstance.eClass()};
		else
			return new Object[]{addedInstance};
	}

	public boolean canReflectMOFChange() {
		return false;
	}

	public boolean decode() throws CodeGenException {
		if(implicitParentBeanpart!=null && implicitChildBeanpart!=null && implicitParentBeanpart.getEObject()!=null && implicitChildBeanpart.getEObject()!=null){
			boolean fromCache = getBeanModel().getCompositionModel().isFromCache();			
			if (fromCache) 
				getExprRef().getMethod().restore(); // Make sure our method is adapted					
			else {				    			
				boolean decoded = true;
				applyDelegateControlSF();
				applyImplicitAllocation();
				return decoded;
			}
		}
		return false;
	}

	protected void applyImplicitAllocation() {
		if(getSF()!=null && implicitChildBeanpart.getEObject() instanceof IJavaInstance){
			ImplicitAllocation implicitalloc = InstantiationFactory.eINSTANCE.createImplicitAllocation(implicitParentBeanpart.getEObject(), getSF());
			((IJavaInstance)implicitChildBeanpart.getEObject()).setAllocation(implicitalloc);
		}
	}

	protected void applyDelegateControlSF() {
		if(getSF()!=null){
			implicitParentBeanpart.getEObject().eSet(getSF(), implicitChildBeanpart.getEObject());
		}
	}

	/*
	 * Not used currently
	 *
	private void applyDefaultLayout() {
		EStructuralFeature layoutSF = implicitChildBeanpart.getEObject().eClass().getEStructuralFeature("layout");
		if(layoutSF!=null){
			String layoutClassName = "org.eclipse.swt.layout.FillLayout";
			IJavaInstance layoutInstance = BeanUtilities.createJavaObject(
					layoutClassName, 
					EMFEditDomainHelper.getResourceSet(getBeanModel().getDomain()),
					"new org.eclipse.swt.layout.FillLayout()");
			if(layoutInstance!=null){
				implicitChildBeanpart.getEObject().eSet(layoutSF, layoutInstance);
			}
		}
	}

	/*
	 * Not used currently
	 *
	private void applyDefaultSize() {
		EStructuralFeature sizeSF = implicitChildBeanpart.getEObject().eClass().getEStructuralFeature("size");
		if(sizeSF!=null){
			String pointClassName = "org.eclipse.swt.graphics.Point";
			IJavaInstance pointInstance = BeanUtilities.createJavaObject(
					pointClassName, 
					EMFEditDomainHelper.getResourceSet(getBeanModel().getDomain()), 
					"new org.eclipse.swt.graphics.Point(300,300)");
			if(pointInstance!=null){
				implicitChildBeanpart.getEObject().eSet(sizeSF, pointInstance);
			}
		}
	}*/

	public VEexpressionPriority determinePriority() {
		return new VEexpressionPriority(Integer.MIN_VALUE, null);
	}

	public void setExpression(CodeExpressionRef expr) throws CodeGenException {
		codeExpressionRef = expr;
	}

	public CodeExpressionRef getExprRef() {
		return codeExpressionRef;
	}

	public void setBeanModel(IBeanDeclModel model) {
		this.beanModel = model;
	}

	public IBeanDeclModel getBeanModel() {
		if(beanModel==null){
			if(implicitParentBeanpart.getModel()!=null)
				beanModel = implicitParentBeanpart.getModel();
			else
				if(implicitChildBeanpart.getModel()!=null)
					beanModel = implicitChildBeanpart.getModel();
		}
		return beanModel;
	}

	public void setCompositionModel(IVEModelInstance cm) {
		compositonModel = cm;
	}

	public IVEModelInstance getCompositionModel() {
		return compositonModel;
	}

	public void setBeanPart(BeanPart part) {
		implicitChildBeanpart = part;
	}

	public BeanPart getBeanPart() {
		return implicitChildBeanpart;
	}

	public boolean isDeleted() {
		return false;
	}

	public String getCurrentExpression() {
		if(getExprRef()!=null)
			return getExprRef().getContent();
		return null;
	}

	public String reflectExpression(String expSig) throws CodeGenException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Shouldnt delete from source as this would result in compile problems
	 */
	public void deleteFromSrc() {}

	public void dispose() {
		deleteFromComposition();
	}

	public void reflectMOFchange() {
		// TODO Auto-generated method stub
		
	}

	public void deleteFromComposition() {
		if(implicitParentBeanpart!=null && getSF()!=null){
			implicitParentBeanpart.getEObject().eUnset(getSF());
		}
	}

	public ICodeGenAdapter createCodeGenInstanceAdapter(BeanPart bp) {
		// TODO Auto-generated method stub
		return null;
	}

	public ICodeGenAdapter createThisCodeGenInstanceAdapter(BeanPart bp) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setStatement(Statement s) {
		// TODO Auto-generated method stub
		
	}
	
	public Object[] getReferencedInstances() {
		// TODO Auto-generated method stub
		return null;
	}
}
