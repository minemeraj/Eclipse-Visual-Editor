package org.eclipse.ve.tests.codegen.java.rules;

/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TestRule.java,v $
 *  $Revision: 1.6 $  $Date: 2004-03-16 20:55:53 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;
import org.eclipse.ve.internal.java.codegen.java.rules.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class TestRule implements IInstanceVariableRule, IMethodVariableRule {

	public static final String copyright = "(c) Copyright IBM Corporation 2002."; //$NON-NLS-1$
	static List utilityClasses = null;
	static ResourceSet utilityRS = null;
	static HashMap internalsCache = null;

	protected List getUtilityClasses(IVEModelInstance di) {
		if (utilityRS != null && utilityRS.equals(di.getModelResourceSet()))
			return utilityClasses;

		utilityClasses = new ArrayList();
		utilityRS = di.getModelResourceSet();
		internalsCache = null;
		for (int i = 0; i < InstanceVariableCreationRule.internalTypes.length; i++) {
			EClassifier iClass = JavaRefFactory.eINSTANCE.reflectType(InstanceVariableCreationRule.internalTypes[i], di.getModelResourceSet());
			utilityClasses.add(iClass);
		}
		return utilityClasses;
	}

	protected String getVariablename(List fragments){
		String name = "";
		if(fragments!=null && fragments.size()>0 && (fragments.get(0) instanceof VariableDeclarationFragment)) {
				VariableDeclarationFragment vF = (VariableDeclarationFragment) fragments.get(0);
				name = vF.getName().toString();
		}
		return name;
	}
	
	protected boolean ignoreVariable(Type type, List fragments, ITypeResolver resolver, IVEModelInstance di) {

		//  Need to filter arrays, 
		//
		if (isUtilityVariable(type, resolver, di))
			return false;

		try {
			// VAJava legacy
			if (getVariablename(fragments).startsWith(IInstanceVariableCreationRule.DEFAULT_VAR_PREFIX)) { //$NON-NLS-1$
				if (getVariablename(fragments).startsWith("ivjConn")) //$NON-NLS-1$
					return true;
				else
					return false;

			} else {
				String type1 = resolveType(type, resolver);
				if (type1 == null)
					return true;

				ResourceSet rs = di.getModelResourceSet();
				EClassifier meta = JavaRefFactory.eINSTANCE.reflectType(type1, rs);

				String pre = InstanceVariableCreationRule.getPrefix(meta, rs);
				if (pre == null || pre.length() == 0)
					return false;
				return true;
			}
		} catch (Throwable t) {
			return true;
		}
	}

	protected String getType(Type type) {
		String t = null;
		if (type instanceof SimpleType)
			t = new String(((SimpleType) type).getName().toString());
		else if (type instanceof QualifiedType) {
			t = ((QualifiedType) type).getName().toString();
		} else
			return null;
		return t;
	}
	
	protected String resolveType(Type type, ITypeResolver resolver) {
		String theType = getType(type);
		if (theType == null)
			return null;
		return resolver.resolve(getType(type));
	}

	/**
	 * e.g., GridBagConstraint.  The InstanceVariableCreationRule maintains the list
	 *       of utility objects.
	 */
	protected boolean isUtilityVariable(Type type, ITypeResolver resolver, IVEModelInstance di) {
		// TODO Need to support arrays etc.

		// This will also clear the internalsCache
		List utilClasses = getUtilityClasses(di);

		// Try to bypass resolving, and isAssignableFrom
		if (internalsCache == null)
			internalsCache = new HashMap(200);
		Boolean internal = (Boolean) internalsCache.get(getType(type));
		if (internal != null)
			return internal.booleanValue();

		String t = resolveType(type, resolver);
		
		if (t == null)
			return false;
		try {
			EClassifier iClass = JavaRefFactory.eINSTANCE.reflectType(t, di.getModelResourceSet());
			for (Iterator iterator = utilClasses.iterator(); iterator.hasNext();) {
				JavaClass uc = (JavaClass) iterator.next();
				if (uc.isAssignableFrom(iClass)) {
					internalsCache.put(getType(type), Boolean.TRUE);
					return true;
				}
			}
		} catch (Exception e) {
			JavaVEPlugin.log("InstanceVariableRule.isUtility(): Could not resolve - " + t, Level.FINE); //$NON-NLS-1$
		}

		internalsCache.put(getType(type), Boolean.FALSE);
		return false;
	}
	
	public static void clearCache() {
		utilityRS = null;
		utilityClasses = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule#getDefaultInitializationMethod(org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.jdt.internal.compiler.ast.TypeDeclaration)
	 */
	public String getDefaultInitializationMethod(FieldDeclaration field, ITypeResolver resolver, TypeDeclaration typeDec) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule#ignoreVariable(org.eclipse.jdt.core.dom.FieldDeclaration, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance)
	 */
	public boolean ignoreVariable(FieldDeclaration field, ITypeResolver resolver, IVEModelInstance di) {
		return ignoreVariable(field.getType(), field.fragments(), resolver, di);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IMethodVariableRule#ignoreVariable(org.eclipse.jdt.core.dom.VariableDeclarationStatement, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance)
	 */
	public boolean ignoreVariable(VariableDeclarationStatement localField, ITypeResolver resolver, IVEModelInstance di) {
		return ignoreVariable(localField.getType(), localField.fragments(), resolver, di);
	}

}