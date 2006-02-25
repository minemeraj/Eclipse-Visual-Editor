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
 *  $RCSfile: TestComplexNames.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-25 23:32:17 $ 
 */
package org.eclipse.ve.tests.codegen.resolve;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.java.core.TypeResolver;
import org.eclipse.ve.internal.java.core.TypeResolver.FieldResolvedType;
import org.eclipse.ve.internal.java.core.TypeResolver.ResolvedType;
 

/**
 * Test complex names, like" a.b.c.D"
 * @since 1.0.0
 */
public class TestComplexNames extends BaseResolveTestCase {

	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public TestComplexNames() {
		super();
	}

	/**
	 * @param name
	 * 
	 * @since 1.0.0
	 */
	public TestComplexNames(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(ResolveSuite.RESOLVE_TESTDATA_PROJECT+"/src/pkg/base/BaseClass.java"));
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		resolver = new TypeResolver(cu.getImports(), cu.getType("BaseClass"));
	}

	public void testResolveFullyQualified() throws JavaModelException {
		// This tests that it resolves a fully-qualified reference, with a package. No import or inherited simple names used.
		assertEquals("pkg2.Interface", resolveType("pkg2.Interface").getName());
	}

	public void testResolveFullyQualifiedInner2() throws JavaModelException {
		// This tests that it resolves a fully-qualified reference, with a package. No import or inherited simple names used.
		assertEquals("pkg2.Interface$InterfaceInner0$Inner0", resolveType("pkg2.Interface.InterfaceInner0.Inner0").getName());
	}
	
	
	public void testResolveReturnsSame() throws JavaModelException {
		// This is to test the cache that if returns the same resolved complex type.
		ResolvedType rt = resolveType("pkg2.Interface");
		assertSame(rt, resolveType("pkg2.Interface"));
	}
	
	public void testResolveFindsInnerOfPreviousMatch() throws JavaModelException {
		// This test that it finds an inner class of a previously matched resolve.
		resolveType("pkg2.Interface");
		assertEquals("pkg2.Interface$InterfaceInner0", resolveType("pkg2.Interface.InterfaceInner0").getName());
	}
	
	public void testResolveFailsFakeInnerOfPreviousMatch() throws JavaModelException {
		// This test that it deteects that an inner class of a previously matched won't resolve.
		resolveType("pkg2.Interface");
		assertNull(resolver.resolveType("pkg2.Interface.FakeInterfaceInner0"));
	}
	
	public void testResolveFailsFakeInner() throws JavaModelException {
		// This doesn't previously match, but it is with a found one.
		assertNull(resolver.resolveType("pkg2.Interface.FakeInterfaceInner0"));
	}
	
	public void testResolveFailsFake() throws JavaModelException {
		// This tests complete fake.
		assertNull(resolver.resolveType("pkg2.Interface0.FakeInterfaceInner0"));
	}
	
	public void testResolveFullyQualifiedName() throws JavaModelException {
		// This tests that it resolves a fully-qualified reference as Name, with a package. No import or inherited simple names used.
		// No need to any further ast Name tests because they all break down into the same as the string tests. This just makes
		// sure it is decomposed correctly.
		assertEquals("pkg2.Interface", resolveType(createName("pkg2.Interface")).getName());
	}	
	
	public void testComplexFromSimple() throws JavaModelException {
		// This tests where first qualifier is a simple name that can be resolved. Then the rest will be found from that.
		assertEquals("pkg1.sub1.FullImport0$Inner0", resolveType("FullImport0.Inner0").getName());
	}
	
	public void testResolveField() throws JavaModelException {
		FieldResolvedType ft = resolver.resolveWithPossibleField("pkg2.Interface.f.g");
		assertNotNull(ft);
		assertNotNull(ft.resolvedType);
		assertEquals("pkg2.Interface", ft.resolvedType.getName());
		assertNotNull(ft.fieldAccessors);
		assertEquals(2, ft.fieldAccessors.length);
		assertEquals("f", ft.fieldAccessors[0]);
		assertEquals("g", ft.fieldAccessors[1]);
	}
	
	public void testResolveFieldWithNoField() throws JavaModelException {
		FieldResolvedType ft = resolver.resolveWithPossibleField("pkg2.Interface");
		assertNotNull(ft);
		assertNotNull(ft.resolvedType);
		assertEquals("pkg2.Interface", ft.resolvedType.getName());
		assertNotNull(ft.fieldAccessors);
		assertEquals(0, ft.fieldAccessors.length);
	}
	
	public void testResolveFullMultiPartPackage() {
		// This tests a multi-fragment package resolve with no import.
		assertNotNull(resolver.resolveType("pkg1.sub1.sub2.StarSub2"));
	}
	
	public void testResolveFullMultiPartPackageReretrieve() {
		// This tests a multi-fragment package resolve with no import and that we get the same one back.
		ResolvedType resolveType = resolver.resolveType("pkg1.sub1.sub2.StarSub2");
		assertNotNull(resolveType);
		assertSame(resolveType, resolver.resolveType("pkg1.sub1.sub2.StarSub2"));
	}

}
