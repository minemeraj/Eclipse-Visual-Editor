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
/*
 *  $RCSfile: AbstractFeatureMapper.java,v $
 *  $Revision: 1.14 $  $Date: 2005-08-24 23:30:45 $ 
 */
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

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

	public void setFeature(EStructuralFeature sf) {
		fSF = sf;
		if (sf != null) {
			try {
				fSFname = fSF.getName();
				fPD = Utilities.getPropertyDecorator(sf);
			} catch (Throwable e) {
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
					JavaVEPlugin.log("Utilities.getPropertyDecorator on:" + sf, Level.WARNING); //$NON-NLS-1$
					JavaVEPlugin.log(e, Level.WARNING);
				}
			}
			if (fPD != null && fPD.getField() != null) {
				fisMethod = false;
				fMethodName = fPD.getField().getName();
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
				return "new" + ((SimpleType) tr).getName().toString(); //$NON-NLS-1$
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
				} else  if (((Assignment) e).getRightHandSide() instanceof ClassInstanceCreation) {
					return "new" ; //$NON-NLS-1$
				}
				else if (((Assignment) e).getLeftHandSide() instanceof SimpleName) {
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
