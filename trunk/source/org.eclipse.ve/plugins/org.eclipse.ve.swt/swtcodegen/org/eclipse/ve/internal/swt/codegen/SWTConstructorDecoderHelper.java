/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTConstructorDecoderHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-05 17:50:37 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class SWTConstructorDecoderHelper extends ConstructorDecoderHelper {
	
	BeanPart fParent = null ;

	/**
	 * @param bean
	 * @param exp
	 * @param fm
	 * @param owner
	 * 
	 * @since 1.0.0
	 */
	public SWTConstructorDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);		
	}
	/**
	 * If this widget is initialized in the same method as its parent, than
	 * we know that this widget will be instantiated, and we need to set up
	 * the parent/child relationship
	 * @return
	 * 
	 * @since 1.0.0
	 */
	protected boolean isControlFeatureNeeded() {
		// Does the constructor refer to a parent
		if (fReferences.size()>0) {
			// check to see if both parent/child are defined on with the same method
			// The assumption is that a call to the init method of teh child will create
			// the control feature			
			fParent = fbeanPart.getModel().getABean((IJavaObjectInstance)fReferences.get(0));
			if (fbeanPart.getInitMethod().equals(fParent.getInitMethod()))
				return true;
		}
		return false ;
	}
	
	/**
	 * Add this bean to the "controls" feature of the parent,
	 * and create the proper Expression for the BDM
	 * @since 1.0.0
	 */
	protected void createParentChildRelationship() {				
		
		EObject parent = fParent.getEObject();
				
		
		EStructuralFeature sf = parent.eClass().getEStructuralFeature("controls");
		((EList)parent.eGet(sf)).add(fbeanPart.getEObject()) ;
		
		// Create a pseudo expression for the parent (no add(Foo) in SWT)
		// This will drive the creation of a decoder with controls, and will
		// establish the parent/child relationship in the BDM
		ExpressionRefFactory eGen = new ExpressionRefFactory(fParent, sf);
		try {
			eGen.createFromJVEModel(new Object[] { fbeanPart.getEObject() });
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {		
		boolean result = super.decode();
		if (result) {
			if (isControlFeatureNeeded()) {
				createParentChildRelationship();								
			}
		}
		return (result);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getIndexPriority()
	 */
	protected int getIndexPriority() {
		// TODO Index is depandent on SWT parent/child relationships (who comes up first)
		return super.getIndexPriority();
	}
	

}
