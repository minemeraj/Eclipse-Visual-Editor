/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TestImportChangesRemoveContainer.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:59:07 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.ve.internal.java.codegen.util.TypeResolver;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.ResolvedType;
 

/**
 * Test remove an import container.
 * @since 1.0.0
 */
public class TestImportChangesRemoveContainer extends BaseResolveTestCase {
	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/test2/testClass/TestImportsChangesRemoveAll.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		
		resolver = new TypeResolver(cu.getImports(), cu.getType("TestImportsChangesRemoveAll"));
	}
	
	public void testImportRemoveAll() throws CoreException, MalformedTreeException, BadLocationException {
		// This tests add of entire import container. 
		// Verify there is an import container.
		assertTrue(resolver.resolveMain().getIType().getCompilationUnit().getImportContainer().exists());
		
		// Make sure we resolve.
		resolveType("BaseClass");
		
		// Also make sure that java.lang stuff doesn't get cleared.
		ResolvedType langString = resolveType("String");

		// Now add an import (which adds an import container too) that should make BaseClass now resolvable.
		final IType st = resolver.resolveMain().getIType();
		st.getCompilationUnit().becomeWorkingCopy(null, new NullProgressMonitor());
		IBuffer buffer = st.getCompilationUnit().getBuffer();				
		Document doc = new Document(buffer.getContents());
		ASTParser astParser = ASTParser.newParser(AST.JLS2);
		astParser.setSource(doc.get().toCharArray());
		CompilationUnit cu = (CompilationUnit) astParser.createAST(new NullProgressMonitor());
		cu.recordModifications();
		List imports = cu.imports();
		imports.clear();
		TextEdit te = cu.rewrite(doc, null);
		te.apply(doc, 0);
		buffer.setContents(doc.get());
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				st.getCompilationUnit().reconcile(ICompilationUnit.NO_AST, false, null, monitor);
				st.getCompilationUnit().commitWorkingCopy(true, monitor);
			}
		}, st.getResource(), IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
		st.getCompilationUnit().discardWorkingCopy();		
		
		// It should now be unresolvable.
		assertNull(resolver.resolveType("BaseClass"));
		
		// It should be the same javalang.
		assertSame(langString, resolveType("String"));
	}
}
