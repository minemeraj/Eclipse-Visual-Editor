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
 *  $RCSfile: TestBlastReplace.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:59:07 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import java.io.*;
import java.util.List;
import java.util.ListIterator;

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
 * This will test a blast replacement of the class.
 * 
 * @since 1.0.0
 */
public class TestBlastReplace extends BaseResolveTestCase {

	private static final String ORIG_CLASS = "package test2.testClass; import pkg.base.*; public class TestBlast {}";
	private static final String BLAST_CLASS = "package test2.testClass; import pkg1.sub1.sub2.*; public class TestBlast {}";
	
	protected void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/test2/testClass/TestBlast.java"));
		file.create(getInputStream(ORIG_CLASS), true, new NullProgressMonitor());
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		
		resolver = new TypeResolver(cu.getImports(), cu.getType("TestBlast"));
	}
	
	private InputStream getInputStream(String fromString) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream(fromString.length()*2);
		OutputStreamWriter osw = new OutputStreamWriter(bo);
		osw.write(fromString);
		osw.close();
		bo.close();
		return new ByteArrayInputStream(bo.toByteArray());
	}
	
	public void testBlast() throws MalformedTreeException, BadLocationException, JavaModelException, CoreException, IOException {

		// This tests add of import to an existing container. 
		// Verify there is an import container.
		assertTrue(resolver.resolveMain().getIType().getCompilationUnit().getImportContainer().exists());
		
		// Make sure we resolve
		resolveType("BaseClass");
		
		// Make sure we don't resolve
		assertNull(resolver.resolveType("FullImport0"));
		
		// Also make sure that java.lang stuff doesn't get cleared.
		ResolvedType langString = resolveType("String");

		// Now change an import
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
		for (ListIterator itr = imports.listIterator(); itr.hasNext();) {
			ImportDeclaration id = (ImportDeclaration) itr.next();
			if (id.isOnDemand() && id.getName().getFullyQualifiedName().equals("pkg.base")) {
				id.setName(ast.newName(new String[] {"pkg1", "sub1"}));
				id.setOnDemand(true);
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
		
		// This should now not resolve.
		assertNull(resolver.resolveType("BaseClass"));
		
		// It should now be resolvable.
		resolveType("FullImport0");
		
		// It should be the same javalang.
		assertSame(langString, resolveType("String"));
		
		// Make sure not resolvable before blast.
		assertNull(resolver.resolveType("StarSub2"));
		
		// Now blast in the blast copy.
		((IFile)st.getUnderlyingResource()).setContents(getInputStream(BLAST_CLASS), true, false, new NullProgressMonitor());
		
		// It should now be un-resolvable.
		assertNull(resolver.resolveType("FullImport0"));
		
		// It should be now resolvable
		resolveType("StarSub2");
	}
	
	
	protected void tearDown() throws Exception {
		super.tearDown();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/test2/testClass/TestBlast.java"));
		file.delete(true, new NullProgressMonitor());
	}
}
