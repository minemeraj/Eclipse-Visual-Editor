/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.tests.codegen.java.rules;

/*
 *  $RCSfile: TestRule.java,v $
 *  $Revision: 1.16 $  $Date: 2006-05-17 20:16:07 $ 
 */

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.rules.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.core.TypeResolver;
import org.eclipse.ve.internal.java.core.TypeResolver.Resolved;

public class TestRule implements IInstanceVariableRule, IMethodVariableRule {

	public static final String copyright = "(c) Copyright IBM Corporation 2002."; //$NON-NLS-1$
	static ResourceSet utilityRS = null;
	static HashMap internalsCache = null;
	private static HashMap modelledBeansCache;

	/**
	 * e.g., GridBagConstraint.  The InstanceVariableCreationRule maintains the list
	 *       of utility objects.
	 */
	protected boolean isModelled(Type tp, TypeResolver resolver, IVEModelInstance di) {

		// Try to bypass resolving, and isAssignableFrom
		if (modelledBeansCache == null)
			modelledBeansCache = new HashMap(200);
		Boolean internal = (Boolean) modelledBeansCache.get(resolveType(tp, resolver));
		if (internal != null)
			return internal.booleanValue();

		String t = resolveType(tp, resolver);
		
		if (t == null)
			return false;
		try {
			EClassifier iClass = JavaRefFactory.eINSTANCE.reflectType(t, di.getModelResourceSet());
			boolean result = InstanceVariableCreationRule.isModelled(iClass, iClass.eResource().getResourceSet());
			modelledBeansCache.put(resolveType(tp,resolver), new Boolean(result));
			return result;
		} catch (Exception e) {
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("InstanceVariableRule.isUtility(): Could not resolve - " + t, Level.FINE); //$NON-NLS-1$
		}

		modelledBeansCache.put(resolveType(tp,resolver), Boolean.FALSE);
		return false;
	}

	protected String getVariablename(List fragments){
		String name = "";
		if(fragments!=null && fragments.size()>0 && (fragments.get(0) instanceof VariableDeclarationFragment)) {
				VariableDeclarationFragment vF = (VariableDeclarationFragment) fragments.get(0);
				name = vF.getName().toString();
		}
		return name;
	}
	
	protected boolean ignoreVariable(VariableDeclaration decl, Type tp, TypeResolver resolver, IVEModelInstance di) {
		try {
			String name = decl.getName().getIdentifier();
			if (name.startsWith("ivj")) {
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
				}
				return false;
			} 
			
			String type = resolveType(tp, resolver);
			if (type == null)
				return true;
			String t = type.indexOf("$") >= 0 ? type.substring(type.indexOf("$") + 1) : type; //$NON-NLS-1$ //$NON-NLS-2$
			if (t.startsWith("Ivj"))
				return true; // ignore IvjEventHandler and such //$NON-NLS-1$

			return true;
		} catch (Throwable t) {
			return true;
		}
	}
	
	protected String resolveType(Type type, TypeResolver resolver) {
		Resolved resolved = resolver.resolveType(type);
		return resolved != null ? resolved.getName() : null;
	}
	
	public static void clearCache() {
		utilityRS = null;
		modelledBeansCache = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule#getDefaultInitializationMethod(org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.jdt.internal.compiler.ast.TypeDeclaration)
	 */
	public String getDefaultInitializationMethod(FieldDeclaration field, TypeResolver resolver, TypeDeclaration typeDec) {
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
	public boolean ignoreVariable(FieldDeclaration field, TypeResolver resolver, IVEModelInstance di) {
		if (isModelled(field.getType(), resolver, di))
			return false;
		return ignoreVariable((VariableDeclaration)field.fragments().get(0), field.getType(), resolver, di);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IMethodVariableRule#ignoreVariable(org.eclipse.jdt.core.dom.VariableDeclarationStatement, org.eclipse.ve.internal.java.codegen.java.ITypeResolver, org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance)
	 */
	public boolean ignoreVariable(VariableDeclarationStatement localField, TypeResolver resolver, IVEModelInstance di) {
		if (isModelled(localField.getType(), resolver, di))
			return false;
		return ignoreVariable((VariableDeclaration)localField.fragments().get(0), localField.getType(), resolver, di);
	}

}
