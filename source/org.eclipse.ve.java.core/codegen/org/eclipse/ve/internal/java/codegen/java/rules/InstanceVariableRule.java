/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.17 $  $Date: 2005-05-11 22:41:32 $ 
 */

import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.AnnotationDecoderAdapter;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class InstanceVariableRule implements IInstanceVariableRule, IMethodVariableRule {

	static HashMap modelledBeansCache = new HashMap(200);

	String fInitMethod = null;
	boolean fInitMethodSet = false;

	public boolean ignoreVariable(FieldDeclaration field, TypeResolver resolver, IVEModelInstance di) {
		//TODO:  Need to filter arrays, 
		//
		if (resolveType(field.getType(), resolver) == null)
			return true;	// If not resolved, ignore it.

		if (isModelled(field.getType(), resolver, di))
			return false;
		if (AnnotationDecoderAdapter.isDeclarationParseable(field))
			return false;
		//TODO: support multi declerations
		return ignoreVariable((VariableDeclaration)field.fragments().get(0), field.getType(), resolver, di);
	}
	
	public boolean ignoreVariable(VariableDeclarationStatement stmt, TypeResolver resolver, IVEModelInstance di) {
		//TODO:  Need to filter arrays, 
		//
		if (resolveType(stmt.getType(), resolver) == null)
			return true;	// If not resolved, ignore it.
		
		if (isModelled(stmt.getType(), resolver, di))
			return false;
		if (AnnotationDecoderAdapter.isDeclarationParseable(stmt))
			return false;
		//TODO: support multi declerations
		return ignoreVariable((VariableDeclaration)stmt.fragments().get(0), stmt.getType(), resolver, di);
	}
	
	protected boolean ignoreVariable(VariableDeclaration decl, Type tp, TypeResolver resolver, IVEModelInstance di) {
		String name = decl.getName().getIdentifier();
		if (name.startsWith("ivj")) { //$NON-NLS-1$
			// Ignore VCE connections
			if (name.startsWith("ivjConn")) //$NON-NLS-1$
				return true;
			else {
				String type = resolveType(tp, resolver);
				if (type == null)
					return true;
				// Find the index of the first inner class (i.e. x.y.z.Q$Ivj...) If this is actually an inner of
				// an inner class, it is the first inner class that counts. We know the pattern from VAJ has Ivj...
				// as an immediate inner class of the main class. These are event handlers and should be ignored.
				int firstInner = type.indexOf('$', type.lastIndexOf('.')+1)+1;
				return type.startsWith("Ivj", firstInner);	// ignore IvjEventHandler and such //$NON-NLS-1$
			}
		} 
		
		return true;
	}
	
	protected String resolveType(Type t, TypeResolver resolver) {
		Resolved resolved = resolver.resolveType(t);
		return resolved != null ? resolved.getName() : null;
	}

	/**
	 * e.g., GridBagConstraint.  The InstanceVariableCreationRule maintains the list
	 *       of utility objects.
	 */
	protected boolean isModelled(Type tp, TypeResolver resolver, IVEModelInstance di) {

		String resolvedType = resolveType(tp, resolver);
		if (resolvedType == null)
			return false;
		
		// Try to bypass resolving, and isAssignableFrom
		if (modelledBeansCache == null)
			modelledBeansCache = new HashMap(200);
		
		Boolean internal = (Boolean) modelledBeansCache.get(resolvedType);
		if (internal != null)
			return internal.booleanValue();
		
		try {
			EClassifier iClass = JavaRefFactory.eINSTANCE.reflectType(resolvedType, di.getModelResourceSet());
			boolean result = InstanceVariableCreationRule.isModelled(iClass, iClass.eResource().getResourceSet());
			modelledBeansCache.put(resolvedType, result ? Boolean.TRUE : Boolean.FALSE);
			return result;
		} catch (Exception e) {
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("InstanceVariableRule.isUtility(): Could not resolve - " + resolvedType, Level.FINE); //$NON-NLS-1$
		}

		modelledBeansCache.put(resolvedType, Boolean.FALSE);
		return false;
	}
	public static void clearCache() {
		modelledBeansCache.clear();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule#getDefaultInitializationMethod(org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.jdt.internal.compiler.ast.TypeDeclaration)
	 */
	public String getDefaultInitializationMethod(FieldDeclaration f, TypeResolver resolver, TypeDeclaration typeDec) {
		//TODO: deal with multi delerations
		Expression init = ((VariableDeclaration) f.fragments().get(0)).getInitializer();
		if (init != null) {
			if (init instanceof ClassInstanceCreation || init instanceof ArrayCreation) {
				String[] methods = VCEPrefContributor.getMethodsFromStore();
				MethodDeclaration mDeclarations[] = typeDec.getMethods();
				if (methods.length == 0 || mDeclarations == null || mDeclarations.length == 0)
					return null;
				for (int i = 0; i < methods.length; i++) {
					for (int j = 0; j < mDeclarations.length; j++) {
						MethodDeclaration declaration = mDeclarations[j];
						if (methods[i].equals(declaration.getName().getIdentifier())) {
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
