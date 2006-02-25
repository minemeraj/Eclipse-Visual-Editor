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
 *  $RCSfile: TestImportChangesRemoveFromExisting.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-25 23:32:17 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.ve.internal.java.core.TypeResolver;
import org.eclipse.ve.internal.java.core.TypeResolver.ResolvedType;
 

/**
 * Test add an import to one that has imports..
 * @since 1.0.0
 */
public class TestImportChangesRemoveFromExisting extends BaseResolveTestCase {
	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/test2/testClass/TestImportsChangesRemove.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		
		resolver = new TypeResolver(cu.getImports(), cu.getType("TestImportsChangesRemove"));
	}
	
	public void testImportRemove() throws CoreException, MalformedTreeException, BadLocationException {
		// This tests add of import to an existing container. 
		// Verify there is an import container.
		assertTrue(resolver.resolveMain().getIType().getCompilationUnit().getImportContainer().exists());
		
		// Verify we can resolve it.
		resolveType("FullImport0");
		
		// Also make sure that java.lang stuff doesn't get cleared.
		ResolvedType langString = resolveType("String");

		// Now remove an import
		final IType st = resolver.resolveMain().getIType();
		st.getCompilationUnit().becomeWorkingCopy(null, new NullProgressMonitor());
		IBuffer buffer = st.getCompilationUnit().getBuffer();				
		Document doc = new Document(buffer.getContents());
		ASTParser astParser = ASTParser.newParser(AST.JLS2);
		astParser.setSource(doc.get().toCharArray());
		CompilationUnit cu = (CompilationUnit) astParser.createAST(new NullProgressMonitor());
		cu.recordModifications();
		List imports = cu.imports();
		for (Iterator itr = imports.iterator(); itr.hasNext();) {
			ImportDeclaration id = (ImportDeclaration) itr.next();
			if (id.isOnDemand() && id.getName().getFullyQualifiedName().equals("pkg1.sub1")) {
				itr.remove();
				break;
			}
		}
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
		
		// It should now be un-resolvable.
		assertNull(resolver.resolveType("FullImport0"));
		
		// It should be the same javalang.
		assertSame(langString, resolveType("String"));
	}
}
