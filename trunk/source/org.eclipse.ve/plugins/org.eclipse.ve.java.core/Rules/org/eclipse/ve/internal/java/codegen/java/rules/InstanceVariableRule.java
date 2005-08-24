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
package org.eclipse.ve.internal.java.codegen.java.rules;
/*
 *  $RCSfile: InstanceVariableRule.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import com.ibm.etools.java.JavaClass;
import com.ibm.etools.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

public class InstanceVariableRule implements IInstanceVariableRule, IMethodVariableRule {

	static List utilityClasses = null;
	static ResourceSet utilityRS = null;
	static HashMap internalsCache = null;

	String fInitMethod = null;
	boolean fInitMethodSet = false;

	protected List getUtilityClasses(IDiagramModelInstance di) {
		if (utilityRS != null && utilityRS.equals(di.getModelResourceSet()))
			return utilityClasses;

		utilityClasses = new ArrayList();
		utilityRS = di.getModelResourceSet();
		internalsCache = null;
		for (int i = 0; i < InstanceVariableCreationRule.internalTypes.length; i++) {
			EClassifier iClass;
			try {
				iClass = JavaClassImpl.reflect(InstanceVariableCreationRule.internalTypes[i], di.getModelResourceSet());
			} catch (Exception e) {
				continue;
			}
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
				// Ignore VCE connections
				if (field.name().startsWith("ivjConn")) //$NON-NLS-1$
					return true;
				else {
					String type = resolveType(field, resolver);
					if (type == null)
						return true;
					if (type.indexOf("$") >= 0) //$NON-NLS-1$
						type = type.substring(type.indexOf("$") + 1); //$NON-NLS-1$

					if (type.startsWith("Ivj"))
						return true; // ignore IvjEventHandler and such //$NON-NLS-1$
					return false;
				}
			} else {
				String type = resolveType(field, resolver);
				if (type == null)
					return true;
				String t = type.indexOf("$") >= 0 ? type.substring(type.indexOf("$") + 1) : type; //$NON-NLS-1$ //$NON-NLS-2$
				if (t.startsWith("Ivj"))
					return true; // ignore IvjEventHandler and such //$NON-NLS-1$

				ResourceSet rs = di.getModelResourceSet();
				EClassifier meta = (EClassifier) com.ibm.etools.java.impl.JavaClassImpl.reflect(type, rs);

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
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log("InstanceVariableRule.isUtility(): Could not resolve - " + t, org.eclipse.ve.internal.java.core.JavaLevel.FINE); //$NON-NLS-1$
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
	public String getDefaultInitializationMethod(AbstractVariableDeclaration f, ITypeResolver resolver, TypeDeclaration typeDec) {
		if (f.initialization instanceof AllocationExpression || f.initialization instanceof ArrayAllocationExpression) {
			String[] methods = VCEPrefContributor.getMethodsFromStore();
			AbstractMethodDeclaration mDeclarations[] = typeDec.methods;
			if (methods == null || methods.length == 0 || mDeclarations == null || mDeclarations.length == 0)
				return null;
			for (int i = 0; i < methods.length; i++) {
				for (int j = 0; j < mDeclarations.length; j++) {
					if (mDeclarations[j] instanceof MethodDeclaration) {
						MethodDeclaration declaration = (MethodDeclaration) mDeclarations[j];
						if (methods[i].equals(new String(declaration.selector))) {
							fInitMethod = methods[i];
							fInitMethodSet = true;
							return fInitMethod;
						}
					}
				}
			}

		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}
}
