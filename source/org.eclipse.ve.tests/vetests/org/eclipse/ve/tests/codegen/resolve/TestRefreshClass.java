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
 *  $RCSfile: TestRefreshClass.java,v $
 *  $Revision: 1.5 $  $Date: 2006-05-17 20:16:07 $ 
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

import org.eclipse.ve.internal.java.core.TypeResolver;
import org.eclipse.ve.internal.java.core.TypeResolver.ResolvedType;
 

/**
 * Test refreshing classes (ITypes).
 * @since 1.0.0
 */
public class TestRefreshClass extends BaseResolveTestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/test2/testClass/TestRefresh.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		
		resolver = new TypeResolver(cu.getImports(), cu.getType("TestRefresh"));
	}
	
	public void testRemoveCU() throws CoreException {
		ResolvedType rt = resolveType("TestClassToDelete");
		resolveType("TestClassToDelete.TestInnerClassGoesToo");	
		
		// Now delete the CU and see that the class is now unresolved.
		rt.getIType().getResource().delete(true, new NullProgressMonitor());
		
		// See if both outer class and inner class go away.
		assertNull(resolver.resolveType("TestClassToDelete"));
		assertNull(resolver.resolveType("TestClassToDelete.TestInnerClassGoesToo"));
	}
	
	public void testRenameType() throws CoreException, MalformedTreeException, BadLocationException {
		ResolvedType rt = resolveType("TestRenameClass");
		
		// Now we will change the name of TestRename.
		rt.getIType().getCompilationUnit().rename("NewName.java", true, new NullProgressMonitor());
		
		// It should now be missing.
		assertNull(resolver.resolveType("TestRenameClass"));
	}
	
	public void testRenameInnerType() throws MalformedTreeException, BadLocationException, CoreException {

		ResolvedType rt = resolveType("ExternalClass.InnerClassToRename");

		// Now rename the inner class.
		final IType st = rt.getIType();
		st.getCompilationUnit().becomeWorkingCopy(null, new NullProgressMonitor());
		IBuffer buffer = st.getCompilationUnit().getBuffer();				
		Document doc = new Document(buffer.getContents());
		ASTParser astParser = ASTParser.newParser(AST.JLS2);
		astParser.setSource(doc.get().toCharArray());
		CompilationUnit cu = (CompilationUnit) astParser.createAST(new NullProgressMonitor());
		cu.recordModifications();
		AST ast = cu.getAST();				
		List types = cu.types();
		TypeDeclaration scType = (TypeDeclaration) types.get(0);	// It should be the main type as the first one.
		TypeDeclaration[] inners = scType.getTypes();
		for (int i = 0; i < inners.length; i++) {
			if (inners[i].getName().getIdentifier().equals("InnerClassToRename")) {
				TypeDeclaration td = inners[i];
				td.setName(ast.newSimpleName("NewInnerClassName"));
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
		
		// It should now be gone.
		assertNull(resolver.resolveType("InnerClassToRename"));
		// We should now have the new one.
		resolveType("ExternalClass.NewInnerClassName");
	}
	
}
