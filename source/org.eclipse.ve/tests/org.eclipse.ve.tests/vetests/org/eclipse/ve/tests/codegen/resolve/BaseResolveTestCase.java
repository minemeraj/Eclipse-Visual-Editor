/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BaseResolveTestCase.java,v $
 *  $Revision: 1.6 $  $Date: 2006-05-17 20:16:07 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import junit.framework.TestCase;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Name;

import org.eclipse.ve.internal.java.core.TypeResolver;
import org.eclipse.ve.internal.java.core.TypeResolver.ResolvedType;

import com.ibm.icu.util.StringTokenizer;
 

/**
 * 
 * @since 1.0.0
 */
public abstract class BaseResolveTestCase extends TestCase {

	protected TypeResolver resolver;
	protected AST ast;
	
	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public BaseResolveTestCase() {
		super();
	}

	/**
	 * @param name
	 * 
	 * @since 1.0.0
	 */
	public BaseResolveTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ast = AST.newAST(AST.JLS2);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		resolver.dispose();
		super.tearDown();
	}


	protected ResolvedType resolveType(String toresolve) throws JavaModelException {
		ResolvedType rt = resolver.resolveType(toresolve);
		assertNotNull(rt);
		return rt;
	}

	protected ResolvedType resolveType(Name toresolve) throws JavaModelException {
		ResolvedType rt = resolver.resolveType(toresolve);
		assertNotNull(rt);
		return rt;
	}

	protected Name createName(String name) {
		StringTokenizer st = new StringTokenizer(name, ".");
		String[] names = new String[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens()) {
			names[i++] = st.nextToken();
		}
		return ast.newName(names);
	}

}
