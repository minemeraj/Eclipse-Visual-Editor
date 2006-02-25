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
 *  $RCSfile: TestRefreshSuper.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-25 23:32:17 $ 
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
import org.eclipse.ve.internal.java.core.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.core.TypeResolver.ResolvedType;
 

/**
 * This tests that changes in the super class hierarchy cause the 
 * types resolved through the hierarchy will clear to be resolved again.
 * @since 1.0.0
 */
public class TestRefreshSuper extends BaseResolveTestCase {

	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/pkg/base/BaseClass.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		resolver = new TypeResolver(cu.getImports(), cu.getType("BaseClass"));
	}
	
	public void testSuperInnerClass() throws CoreException, MalformedTreeException, BadLocationException {
		Resolved rt = resolveType("InnerSuper");
		assertEquals("pkg2.SuperClass$InnerSuper", rt.getName());
		
		// Now we will change the hierarchy of pkg2.SuperClass so that we cause a
		// change in the hierarchy. This should then require a refresh and we should
		// now get a different rt for InnerSuper. We are going to just add in a run method and
		// change it to implement Runnable. This shouldn't cause any followon tests to have a problem (HOPEFULLY?)
		ResolvedType srt = resolveType("pkg2.SuperClass");
		final IType st = srt.getIType();
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
		List interfaces = scType.superInterfaces();
		interfaces.add(ast.newName(new String[] {"java", "lang", "Runnable"}));
		MethodDeclaration run = ast.newMethodDeclaration();
		run.setName(ast.newSimpleName("run"));
		run.setModifiers(Modifier.PUBLIC);
		run.setReturnType(ast.newPrimitiveType(PrimitiveType.VOID));
		run.setBody(ast.newBlock());
		scType.bodyDeclarations().add(run);
		TextEdit te = cu.rewrite(doc, null);
		te.apply(doc, 0);
		buffer.setContents(doc.get());
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				st.getCompilationUnit().commitWorkingCopy(true, monitor);
			}
		}, st.getResource(), IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
		st.getCompilationUnit().discardWorkingCopy();		
		
		// Now see if we get a different rt.
		Resolved rt2 = resolveType("InnerSuper");
		assertEquals("pkg2.SuperClass$InnerSuper", rt2.getName());		
		assertNotSame(rt, rt2);
	}
}
