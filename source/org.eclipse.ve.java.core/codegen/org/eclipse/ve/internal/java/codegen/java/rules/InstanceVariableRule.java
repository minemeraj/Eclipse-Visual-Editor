package org.eclipse.ve.internal.java.codegen.java.rules;
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
 *  $RCSfile: InstanceVariableRule.java,v $
 *  $Revision: 1.5 $  $Date: 2004-03-05 23:18:38 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;


import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

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
				iClass = JavaRefFactory.eINSTANCE.reflectType(InstanceVariableCreationRule.internalTypes[i], di.getModelResourceSet());
			} catch (Exception e) {
				continue;
			}
			utilityClasses.add(iClass);
		}
		return utilityClasses;	
	}
	public boolean ignoreVariable(FieldDeclaration field, ITypeResolver resolver, IDiagramModelInstance di) {
		//TODO:  Need to filter arrays, 
		//
		if (isUtilityVariable(field.getType(), resolver, di))
			return false;
		//TODO: support multi declerations
		return ignoreVariable((VariableDeclaration)field.fragments().get(0), field.getType(), resolver, di);
	}
	
	public boolean ignoreVariable(VariableDeclarationStatement stmt, ITypeResolver resolver, IDiagramModelInstance di) {
		//TODO:  Need to filter arrays, 
		//
		if (isUtilityVariable(stmt.getType(), resolver, di))
			return false;
		//TODO: support multi declerations
		return ignoreVariable((VariableDeclaration)stmt.fragments().get(0), stmt.getType(), resolver, di);
	}
	
	
	protected boolean ignoreVariable(VariableDeclaration decl, Type tp, ITypeResolver resolver, IDiagramModelInstance di) {

		
		try {
						
			String name = decl.getName().getIdentifier();
			if (name.startsWith(IInstanceVariableCreationRule.DEFAULT_VAR_PREFIX)) { //$NON-NLS-1$
				// Ignore VCE connections
				if (name.startsWith("ivjConn")) //$NON-NLS-1$
					return true;
				else {
					String type = resolveType(tp, resolver);
					if (type == null)
						return true;
					if (type.indexOf("$") >= 0) //$NON-NLS-1$
						type = type.substring(type.indexOf("$") + 1); //$NON-NLS-1$

					if (type.startsWith("Ivj"))
						return true; // ignore IvjEventHandler and such //$NON-NLS-1$
					return false;
				}
			} else {
				String type = resolveType(tp, resolver);
				if (type == null)
					return true;
				String t = type.indexOf("$") >= 0 ? type.substring(type.indexOf("$") + 1) : type; //$NON-NLS-1$ //$NON-NLS-2$
				if (t.startsWith("Ivj"))
					return true; // ignore IvjEventHandler and such //$NON-NLS-1$

				ResourceSet rs = di.getModelResourceSet();
				EClassifier meta = JavaRefFactory.eINSTANCE.reflectType(type, rs);

				String pre = InstanceVariableCreationRule.getPrefix(meta, rs);
				if (pre == null || pre.length() == 0)
					return false;
				return true;
			}
		} catch (Throwable t) {
			return true;
		}
	}

	
	protected String resolveType(Type t, ITypeResolver resolver) {		
		if (t instanceof SimpleType) {
			return CodeGenUtil.resolve(((SimpleType)t).getName(), resolver) ;
		} else
			return null;
	}

	/**
	 * e.g., GridBagConstraint.  The InstanceVariableCreationRule maintains the list
	 *       of utility objects.
	 */
	protected boolean isUtilityVariable(Type tp, ITypeResolver resolver, IDiagramModelInstance di) {

		// This will also clear the internalsCache
		List utilClasses = getUtilityClasses(di);

		// Try to bypass resolving, and isAssignableFrom
		if (internalsCache == null)
			internalsCache = new HashMap(200);
		Boolean internal = (Boolean) internalsCache.get(resolveType(tp, resolver));
		if (internal != null)
			return internal.booleanValue();

		String t = resolveType(tp, resolver);
		
		if (t == null)
			return false;
		try {
			EClassifier iClass = JavaRefFactory.eINSTANCE.reflectType(t, di.getModelResourceSet());
			for (Iterator iterator = utilClasses.iterator(); iterator.hasNext();) {
				JavaClass uc = (JavaClass) iterator.next();
				if (uc.isAssignableFrom(iClass)) {
					internalsCache.put(resolveType(tp,resolver), Boolean.TRUE);
					return true;
				}
			}
		} catch (Exception e) {
			JavaVEPlugin.log("InstanceVariableRule.isUtility(): Could not resolve - " + t, Level.FINE); //$NON-NLS-1$
		}

		internalsCache.put(resolveType(tp,resolver), Boolean.FALSE);
		return false;
	}
	public static void clearCache() {
		utilityRS = null;
		utilityClasses = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule#getDefaultInitializationMethod(org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.jdt.internal.compiler.ast.TypeDeclaration)
	 */
	public String getDefaultInitializationMethod(FieldDeclaration f, ITypeResolver resolver, TypeDeclaration typeDec) {
		//TODO: deal with multi delerations
		Expression init = ((VariableDeclaration) f.fragments().get(0)).getInitializer();
		if (init != null) {
			if (init instanceof ClassInstanceCreation || init instanceof ArrayCreation) {
				String[] methods = VCEPrefContributor.getMethodsFromStore();
				MethodDeclaration mDeclarations[] = typeDec.getMethods();
				if (methods == null || methods.length == 0 || mDeclarations == null || mDeclarations.length == 0)
					return null;
				for (int i = 0; i < methods.length; i++) {
					for (int j = 0; j < mDeclarations.length; j++) {
						if (mDeclarations[j] instanceof MethodDeclaration) {
							MethodDeclaration declaration = (MethodDeclaration) mDeclarations[j];
							if (methods[i].equals(declaration.getName().getIdentifier())) {
								fInitMethod = methods[i];
								fInitMethodSet = true;
								return fInitMethod;
							}
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