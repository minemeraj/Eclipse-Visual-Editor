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
 *  $Revision: 1.21 $  $Date: 2006-02-25 23:32:07 $ 
 */

import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.core.TypeResolver;
import org.eclipse.ve.internal.java.core.TypeResolver.Resolved;

public class InstanceVariableRule implements IInstanceVariableRule, IMethodVariableRule {

	String fInitMethod = null;
	boolean fInitMethodSet = false;

	public boolean ignoreVariable(FieldDeclaration field, TypeResolver resolver, IVEModelInstance di) {
		if(field.getType().isArrayType())
			return true;
		if (field.getType().isPrimitiveType())
			return true;
		if (resolveType(field.getType(), resolver) == null)
			return true;	// If not resolved, ignore it.
		//TODO: support multi declerations
		return ignoreVariable((VariableDeclaration)field.fragments().get(0), field.getType(), resolver, di);
	}
	
	public boolean ignoreVariable(VariableDeclarationStatement stmt, TypeResolver resolver, IVEModelInstance di) {
		if(stmt.getType().isArrayType())
			return true;
		if (stmt.getType().isPrimitiveType())
			return true;
		if (resolveType(stmt.getType(), resolver) == null)
			return true;	// If not resolved, ignore it.
		
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
		
		return false;
	}
	
	protected String resolveType(Type t, TypeResolver resolver) {
		Resolved resolved = resolver.resolveType(t);
		return resolved != null ? resolved.getName() : null;
	}

	public static void clearCache() {
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
