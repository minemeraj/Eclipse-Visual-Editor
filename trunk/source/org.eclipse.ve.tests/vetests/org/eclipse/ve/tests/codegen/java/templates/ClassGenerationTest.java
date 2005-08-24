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
 *  $RCSfile: ClassGenerationTest.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:54:16 $ 
 */
package org.eclipse.ve.tests.codegen.java.templates;


import junit.framework.TestCase;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

import org.eclipse.jem.tests.JavaProjectUtil;

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.DefaultClassGenerator;
 

/**
 * The idea is to generate a .java target file from a .javajet template
 * This .javajet will be translated to a .java template, then compiled to a .class template
 * that will generate the target .java file.
 * 
 * a .javajet template will be a resource in your plugin.  During development, if your plugin
 * has the JavaJet nature, a build process will generate .java and .class versions of your .javajet template.  
 * The .class version is used to generate the template's content ... .java target. 
 *   e.g.,               org.eclipse.ve.java.core/codegen/org/eclipse/ve/internal/java/codegen/jjet/util
 *         is not in CVS... it is generated on the fly by a JavaJet builder using templates from 
 *                       org.eclipse.ve.java.core/templates/org/eclipse/ve/internal/java/codegen/jjet/util
 * 
 * In run time, if the org.eclipse.ve.java.core/codegen/org/eclipse/ve/internal/java/codegen/jjet/util is not available
 * or if the time stamp of the .javajet is newer from the .jar or .class version of the .javajet CodeGen will
 * regenerate the .java and .class versions of the template using the JDT.  Dynamically generated .class templates will
 * be stored in the .medadata area of the org.eclipse.ve.java.core plugin under the JavaJet directory
 * 
 * In this test, we did not set the IDE JavaJet builder to pre build any .java or .class versions of the template
 * We did put the template that is used in this test as a resource under the template directory of the plugin and
 * rely on CodeGen to dynamically generate the .java and .class versions of the template.  See org.eclipse.ve.java.core
 * on how to set a JavaJet nature 
 *   
 * 
 * @since 1.0.0
 */
public class ClassGenerationTest extends TestCase {
	// Target
	final static String className = "HelloClass";
	final static String classPkg = "a.b.c";
	final static String ProjectName = "JavaProject";
	// Source
	final static String pluginID = "org.eclipse.ve.tests"; // plugin where the template is located
	final static String templateDir = "templates/org/eclipse/ve" ;
	final static String templateFileName = className+"Template";
	
	IJavaProject   JavaProject = null;

	
	protected ICompilationUnit getCU() {		
		try {
			IPath p = new Path (classPkg.replace('.','/')+"."+className+".java");
			IJavaElement e = JavaProject.findElement(p);
			if (e instanceof ICompilationUnit)
				return (ICompilationUnit)e;
		} catch (JavaModelException e) {
			return null;
		}
		return null;
	}
	
	public void testJavaClassCreation() {
		
		DefaultClassGenerator generator = new DefaultClassGenerator(
				className, classPkg,
				pluginID, templateDir,
				templateFileName);
	
		generator.setProject(JavaProject);
		try {
			generator.generateClass(null);
		} catch (CodeGenException e) {
			fail (e.getMessage()) ;
		}
		ICompilationUnit cu = getCU();
		if (cu==null)
			fail();
		try {
			System.out.println(cu.getSource());
		} catch (JavaModelException e1) {}
				
	}
	
	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// Create a JavaProject to put our class in
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject prg;
		IPath path = new Path(ProjectName);
		try {
		    prg = JavaProjectUtil.createEmptyJavaProject(workspace,path,null);
		}
		catch (CoreException e) {
			// Already there
			prg = workspace.getRoot().getProject(path.lastSegment());
		}
		JavaProject = JavaCore.create(prg);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		ICompilationUnit cu = getCU();
		if (cu != null)
		   cu.delete(true,null);
	}

}
