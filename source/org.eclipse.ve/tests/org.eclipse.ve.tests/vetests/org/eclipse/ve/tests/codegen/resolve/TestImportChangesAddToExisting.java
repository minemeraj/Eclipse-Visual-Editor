/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TestImportChangesAddToExisting.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
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
 * Test add an import to one that has imports..
 * @since 1.0.0
 */
public class TestImportChangesAddToExisting extends BaseResolveTestCase {
	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/test2/testClass/TestImportsChangesAddToExisting.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		
		resolver = new TypeResolver(cu.getImports(), cu.getType("TestImportsChangesAddToExisting"));
	}
	
	public void testImportAdd() throws CoreException, MalformedTreeException, BadLocationException {
		// This tests add of import to an existing container. 
		// Verify there is an import container.
		assertTrue(resolver.resolveMain().getIType().getCompilationUnit().getImportContainer().exists());
		
		// Make sure we don't resolve thru some other way.
		assertNull(resolver.resolveType("FullImport0"));
		
		// Also make sure that java.lang stuff doesn't get cleared.
		ResolvedType langString = resolveType("String");

		// Now add an import
		final IType st = resolver.resolveMain().getIType();
		st.getCompilationUnit().becomeWorkingCopy(null, new NullProgressMonitor());
		IBuffer buffer = st.getCompilationUnit().getBuffer();				
		Document doc = new Document(buffer.getContents());
		ASTParser astParser = ASTParser.newParser(AST.JLS2);
		astParser.setSource(doc.get().toCharArray());
		CompilationUnit cu = (CompilationUnit) astParser.createAST(new NullProgressMonitor());
		cu.recordModifications();
		List imports = cu.imports();
		AST ast = cu.getAST();
		ImportDeclaration id = ast.newImportDeclaration();
		id.setName(ast.newName(new String[] {"pkg1", "sub1"}));
		id.setOnDemand(true);
		imports.add(id);
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
		
		// It should now be resolvable.
		resolveType("FullImport0");
		
		// It should be the same javalang.
		assertSame(langString, resolveType("String"));
	}
}
