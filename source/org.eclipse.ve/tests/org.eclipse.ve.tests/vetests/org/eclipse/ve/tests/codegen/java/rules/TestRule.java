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
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:38:46 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;
import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;
import org.eclipse.ve.internal.java.codegen.java.rules.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.java.impl.JavaClassImpl;

public class TestRule implements IInstanceVariableRule, IMethodVariableRule {

	public static final String copyright = "(c) Copyright IBM Corporation 2002."; //$NON-NLS-1$
	static List utilityClasses = null;
	static ResourceSet utilityRS = null;
	static HashMap internalsCache = null;

	protected List getUtilityClasses(IDiagramModelInstance di) {
		if (utilityRS != null && utilityRS.equals(di.getModelResourceSet()))
			return utilityClasses;

		utilityClasses = new ArrayList();
		utilityRS = di.getModelResourceSet();
		internalsCache = null;
		for (int i = 0; i < InstanceVariableCreationRule.internalTypes.length; i++) {
			EClassifier iClass = JavaClassImpl.reflect(InstanceVariableCreationRule.internalTypes[i], di.getModelResourceSet());
			utilityClasses.add(iClass);
		}
		return utilityClasses;
	}

	public boolean ignoreVariable(AbstractVariableDeclaration field, ITypeResolver resolver, IDiagramModelInstance di) {

		//  Need to filter arrays, 
		//
		if (isUtilityVariable(field, resolver, di))
			return false;

		try {
			// VAJava legacy
			if (field.name().startsWith(IInstanceVariableCreationRule.DEFAULT_VAR_PREFIX)) { //$NON-NLS-1$
				if (field.name().startsWith("ivjConn")) //$NON-NLS-1$
					return true;
				else
					return false;

			} else {
				String type = resolveType(field, resolver);
				if (type == null)
					return true;

				ResourceSet rs = di.getModelResourceSet();
				EClassifier meta = JavaClassImpl.reflect(type, rs);

				String pre = InstanceVariableCreationRule.getPrefix(meta, rs);
				if (pre == null || pre.length() == 0)
					return false;
				return true;
			}
		} catch (Throwable t) {
			return true;
		}
	}

	protected String getType(AbstractVariableDeclaration field) {
		String t = null;
		if (field.type instanceof SingleTypeReference)
			t = new String(((SingleTypeReference) field.type).token);
		else if (field.type instanceof QualifiedTypeReference) {
			t = CodeGenUtil.tokensToString(((QualifiedTypeReference) field.type).tokens);
		} else
			return null;
		return t;
	}
	protected String resolveType(AbstractVariableDeclaration field, ITypeResolver resolver) {
		String theType = getType(field);
		if (theType == null)
			return null;
		return resolver.resolve(getType(field));
	}

	/**
	 * e.g., GridBagConstraint.  The InstanceVariableCreationRule maintains the list
	 *       of utility objects.
	 */
	protected boolean isUtilityVariable(AbstractVariableDeclaration field, ITypeResolver resolver, IDiagramModelInstance di) {
		// TODO Need to support arrays etc.

		// This will also clear the internalsCache
		List utilClasses = getUtilityClasses(di);

		// Try to bypass resolving, and isAssignableFrom
		if (internalsCache == null)
			internalsCache = new HashMap(200);
		Boolean internal = (Boolean) internalsCache.get(getType(field));
		if (internal != null)
			return internal.booleanValue();

		String t = resolveType(field, resolver);
		;
		if (t == null)
			return false;
		try {
			EClassifier iClass = JavaClassImpl.reflect(t, di.getModelResourceSet());
			for (Iterator iterator = utilClasses.iterator(); iterator.hasNext();) {
				JavaClass uc = (JavaClass) iterator.next();
				if (uc.isAssignableFrom(iClass)) {
					internalsCache.put(getType(field), Boolean.TRUE);
					return true;
				}
			}
		} catch (Exception e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log("InstanceVariableRule.isUtility(): Could not resolve - " + t, org.eclipse.jem.internal.core.MsgLogger.LOG_FINE); //$NON-NLS-1$
		}

		internalsCache.put(getType(field), Boolean.FALSE);
		return false;
	}
	public static void clearCache() {
		utilityRS = null;
		utilityClasses = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule#getDefaultInitializationMethod(org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.jdt.internal.compiler.ast.TypeDeclaration)
	 */
	public String getDefaultInitializationMethod(AbstractVariableDeclaration field, ITypeResolver resolver, TypeDeclaration typeDec) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
	}

}