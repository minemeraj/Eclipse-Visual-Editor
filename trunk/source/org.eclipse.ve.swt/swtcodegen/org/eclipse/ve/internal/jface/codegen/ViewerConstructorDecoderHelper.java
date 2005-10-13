/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Oct 5, 2005 by Gili Mendel
 * 
 *  $RCSfile: ViewerConstructorDecoderHelper.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-13 20:31:02 $ 
 */
 
package org.eclipse.ve.internal.jface.codegen;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
 

public class ViewerConstructorDecoderHelper extends ConstructorDecoderHelper {
	
	EStructuralFeature implicitFeature = null;
	
	public ViewerConstructorDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner); 
	}
	
    protected EStructuralFeature getImplicitFeature() {
    	if (implicitFeature!=null) return implicitFeature;
    	
    	implicitFeature = getRequiredImplicitFeature((IJavaObjectInstance) fbeanPart.getEObject());
    	return implicitFeature;
    }

	protected void setImplicitFeature() throws CodeGenException {
		EStructuralFeature sf = getImplicitFeature();
		EObject implicit = getImplicitChild();
		BeanPart implicitBean = fbeanPart.getModel().getABean(implicit);
		EObject parent = fbeanPart.getEObject();
		
		// Check to see if there is already an existing tree
		EObject currentImplicit = (EObject) parent.eGet(sf);
		if(currentImplicit!=null && currentImplicit!=implicit){
			// There is a different implcit child already existing - 
			// check if its an implicit
			if(currentImplicit.eContainingFeature() == JCMPackage.eINSTANCE.getMemberContainer_Implicits()){
				// dipose the implicit bean
				BeanPart currentImplicitBP = fbeanPart.getModel().getABean(currentImplicit);
				if(currentImplicitBP!=null)
					currentImplicitBP.dispose();
			}
		}
		
		parent.eSet(sf, implicit);		
		ExpressionRefFactory eGen = new ExpressionRefFactory(fbeanPart, sf);		
		// create a NOSRC expression in the BDM
		eGen.createFromJVEModelWithNoSrc(new Object[] { implicit } );

		CodeExpressionRef init = implicitBean.getInitExpression();
		// Add reference to the visual parent, If the tv is not referenced, it will be deactivated.
		init.getReferences().add(fbeanPart.getEObject());			
	}
	
	protected void unSetImplicitFeature() {
		EStructuralFeature sf = getImplicitFeature();
		EObject implicit = getImplicitChild();
		BeanPart implicitBean = fbeanPart.getModel().getABean(implicit);
		
		BeanPartFactory.unSetBeanPartAsImplicit(implicitBean, fbeanPart, sf);
		CodeExpressionRef init = implicitBean.getInitExpression();
		// Add reference to the visual parent, If the tv is not referenced, it will be deactivated.
		init.getReferences().remove(fbeanPart.getEObject());
				
	}
	
	protected void restoreImplicitFeature() throws CodeGenException {
		EStructuralFeature sf = getImplicitFeature();
		EObject implicit = (EObject) fbeanPart.getEObject().eGet(sf);
		
		ExpressionRefFactory eGen = new ExpressionRefFactory(fbeanPart, sf);		
		// prime the proper helpers
		eGen.createFromJVEModelWithNoSrc(new Object[] { implicit } );
	}
	
	
	protected EObject getImplicitChild() {
		IJavaObjectInstance obj = (IJavaObjectInstance)fbeanPart.getEObject();
		EObject child = null;
		if (obj.getAllocation() instanceof ParseTreeAllocation) {
			ParseTreeAllocation alloc = (ParseTreeAllocation)obj.getAllocation();
			if (alloc.getExpression() instanceof PTClassInstanceCreation) {
				List args = ((PTClassInstanceCreation)alloc.getExpression()).getArguments();
				if ((args.size()==1) &&  args.get(0) instanceof PTInstanceReference) {
				    child = ((PTInstanceReference)args.get(0)).getReference();
				}
			}
		}
		return child;
	}
	
	protected boolean isExplicitConstructor() {
		// Only in the form of: new Viewer(widget)  
		// where widget is of the same type of the required implicit feature		
		IJavaObjectInstance child = (IJavaObjectInstance)getImplicitChild();
		if (child!=null) {
			JavaClass argClass = (JavaClass)child.getJavaType();
			EStructuralFeature implicitFeature = getImplicitFeature();
			return implicitFeature.getEType() == argClass;
		}
		return false;
	}
	
	protected void createImplicitInstancesIfNeeded() throws CodeGenException  {		
		if (isExplicitConstructor()) {
			setImplicitFeature();
		}
		else
		   super.createImplicitInstancesIfNeeded();
	}
	
	protected void removeImplicitInstancesIfNeeded() throws CodeGenException {
		if (isExplicitConstructor()) {
			unSetImplicitFeature();
		}
		else
		   super.removeImplicitInstancesIfNeeded();		
	}
	
	protected void restoreImplicitInstancesIfNeeded() {
		if (isExplicitConstructor()) {
			try {
				restoreImplicitFeature();
			} catch (CodeGenException e) {
				
			}
		}
		else
		   super.restoreImplicitInstancesIfNeeded();				
	}


}
