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
 *  $RCSfile: AbstractFeatureMapper.java,v $
 *  $Revision: 1.5 $  $Date: 2004-04-28 14:21:33 $ 
 */
import java.util.logging.Level;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public abstract class AbstractFeatureMapper implements IJavaFeatureMapper {

	protected EStructuralFeature fSF = null;
	protected String fSFname = null;
	protected PropertyDecorator fPD = null;
	protected IJavaInstance fRefObj = null;
	protected String fMethodName = null;
	protected String fReadMethodName = null;
	protected boolean fisMethod = true;

	/**
	 *  Implementors will provide a specific logic.
	 *  Note: that implementors are designed for a particular set of features.
	 *        One has to load the propper mapper
	 */
	public abstract EStructuralFeature getFeature(Statement expr);

	public boolean isFieldFeature() {
		return !fisMethod;
	}

	protected boolean isFieldAccess(EStructuralFeature sf) {

		BeanFeatureDecorator bdec = (BeanFeatureDecorator) Utilities.getDecorator(sf, BeanFeatureDecorator.class);
		return (bdec != null && "org.eclipse.ve.java.core/org.eclipse.ve.internal.java.core.BeanFieldProxyFeatureMediator".equals(bdec.getBeanProxyMediatorName())); //$NON-NLS-1$
	}

	public void setFeature(EStructuralFeature sf) {
		fSF = sf;
		if (sf != null) {
			try {
				fSFname = ((EStructuralFeature) fSF).getName();
				fPD = Utilities.getPropertyDecorator((EModelElement) sf);
			} catch (Throwable e) {
				JavaVEPlugin.log("Utilities.getPropertyDecorator on:" + sf, Level.WARNING); //$NON-NLS-1$
				JavaVEPlugin.log(e, Level.WARNING);
			}
			if (fPD == null && isFieldAccess(sf)) {
				fisMethod = false;
				fMethodName = sf.getName();
			}
		}
	}
	public String getFeatureName() {
		return fSFname;
	}

	public void setRefObject(IJavaInstance obj) {
		fRefObj = obj;
	}
	public IJavaInstance getRefObject() {
		return fRefObj;
	}

	public String getMethodName() {
		if (fMethodName == null) {
			if (fPD != null && fPD.getWriteMethod() != null) {
				fMethodName = fPD.getWriteMethod().getName();
			}
		}
		return fMethodName;
	}

	public String getReadMethodName() {
		if (fReadMethodName == null) {
			if (fPD != null && fPD.getReadMethod() != null) {
				fReadMethodName = fPD.getReadMethod().getName();
			}
		}
		return fReadMethodName;
	}

	protected String getMethodName(Statement exp) {
		if (fMethodName == null)
			fMethodName = getWriteMethod(exp);
		return fMethodName;
	}

	public PropertyDecorator getDecorator() {
		return fPD;
	}

	/**
	 * The default, overide if needed
	 */
	public String getIndexMethodName() {
		return getMethodName();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper#getFeaturePriority(String)
	 */
	public int getFeaturePriority(String methodType) {
		return IJavaFeatureMapper.PRIORITY_DEFAULT;
	}
	
	/**
	 *  Extract the end resulted write method or public field access.
	 * 
	 * Only ExpressionStatement, VariableDeclarationStatement is supported
	 * (moved from CodeGenUtil)
	 */	
	static public String getWriteMethod(Statement expr)	{
		
		Expression e = null;
		if (expr instanceof ExpressionStatement)
			e = ((ExpressionStatement) expr).getExpression();
		else if (expr instanceof VariableDeclarationStatement) {
			Type tr = ((VariableDeclarationStatement) expr).getType();
			if (tr instanceof SimpleType) {
				//TODO: need tor resolve a Simple name
				return "new" + ((SimpleType) tr).getName().toString();
				//return "new" + CodeGenUtil.resolve(((SimpleType)tr).getName(), ???)
			}
		}

		if (e != null) {

			if (expr == null)
				return null;
			// TODO Must deal with non Literal also
			if (e instanceof MethodInvocation)
				return ((MethodInvocation) e).getName().getIdentifier();
			else if (e instanceof Assignment) {
				// public field access, e.g., gridBagConstraints.gridx = 0 ;
				if (((Assignment) e).getLeftHandSide() instanceof QualifiedName) {
					return ((QualifiedName) ((Assignment) e).getLeftHandSide()).getName().getIdentifier();
				} else if (((Assignment) e).getLeftHandSide() instanceof SimpleName) {
					return ((SimpleName) ((Assignment) e).getLeftHandSide()).getIdentifier();
				}
			}
			//		else if (expr instanceof LocalDeclaration) {
			//			TypeReference tr = ((LocalDeclaration)expr).type ;
			//			if (tr instanceof QualifiedTypeReference)
			//				return "new " + tokensToString(((QualifiedTypeReference)tr).tokens) ;
			// //$NON-NLS-1$
			//			else {
			//				// TODO Need to resolve
			//				return "new" ; //$NON-NLS-1$
			//			}
			//		}
		}
		return null;
	}

}