/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.tests.codegen.java.templates;
/*
 *  $RCSfile: TemplateObjectFactoryTest.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-07 22:37:10 $ 
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.ve.internal.java.vce.templates.TemplateObjectFactory;
import org.eclipse.ve.internal.java.vce.templates.TemplateUtil;

/**
 * @author Gili Mendel
 *
 */
public class TemplateObjectFactoryTest extends TestCase {
	
	final String NL = System.getProperties().getProperty("line.separator");
		
	String generatedClassName = "TestClass" ;
	String skelatonName="skeleton.java" ;
	String signiture = "1,2,3 checking" ;
	String skelaton =	"package a1.b1.c1;"+NL+
						"/*******************************************************************************"+NL+
 						" * Copyright (c) 2001, 2003 IBM Corporation and others."+NL+
 						" * All rights reserved. This program and the accompanying materials"+NL+
 						" * are made available under the terms of the Eclipse Public License v1.0"+NL+
 						" * which accompanies this distribution, and is available at"+NL+
 						" * http://www.eclipse.org/legal/epl-v10.html"+NL+
 						" *"+NL+
 						" * Contributors:"+NL+
 						" *     IBM Corporation - initial API and implementation"+NL+
 						" *******************************************************************************/"+NL+NL+ 						
 						"public class SKELETON_CLASS"+NL+"{"+NL+
 						"public String additionalMethod2() { return null ;}"+NL+
 						"public static final String copyright = \"(c) Copyright IBM Corporation 2003.\" ;"+NL+NL+
 						"public String toString() {"+NL+
 						"return \"\" ;"+NL+"}"+NL+"}" ;						 

	String template = "<%@ jet skeleton=\""+skelatonName+"\" package=\"a.b.c\" class=\""+generatedClassName+
	                  "\" %>"+NL +
	                  "This output is generated for the class "+generatedClassName+NL+
	                  "This objects overrides the toString() method."+NL+
	                  signiture ;	

    String pathURL[] = new String [1] ;  // Location of the template/skelaton
    String classPath[] = null ; // Compile class path
   	File tf = null ;  // denotes the template file
	File sf = null ;  // denotes the skeleton file	
	                  
	/**
	 * Use the template defined above to generate a template/skelaton,
	 * and try to create an object out of it.  
	 * Invoke the toString(), and make sure it returns the "signiture"
	 */
	public void testJetTemplateToObject() {

	    String generateSource =null ;
	    Object  instance = null ;
		try {
			generateSource = TemplateObjectFactory.getClassSource(pathURL, tf.getName(), null, null);
	    	instance = TemplateObjectFactory.getClassInstance(classPath,pathURL,tf.getName(),this.getClass().getClassLoader(),null, null) ;
	    	assertTrue(instance!=null) ;
	    }
	    catch (Exception e) {
	    	fail(e.getMessage()) ;	    
	    }
	    System.out.println(generateSource) ;	    	    
	    System.out.println("Instance = "+instance+NL+"instance.toString(): ") ;
	    instance.toString() ;
	    
	    assertTrue(instance.toString().indexOf(signiture) >= 0) ;
		
	}
	
	
	public void DO_NOT_RUN_testJetTemplateTimeStampChange() {
		// TODO - temporarily disabled until bug 98670 is fixed.
		 Object instance=null, newInstance = null ;

		 try {	 	
			 instance = TemplateObjectFactory.getClassInstance(classPath,pathURL,tf.getName(),this.getClass().getClassLoader(),null, null) ;
             newInstance = TemplateObjectFactory.getClassInstance(classPath,pathURL,tf.getName(),this.getClass().getClassLoader(),null, null) ;		
		 }
		 catch (Exception e) {
		 	fail("First fail: " + e.getMessage()) ;
		 }
		 if (newInstance.getClass() != instance.getClass()) 
		    fail ("Should be the same class") ;

		 // Recreate the template, incrementing its time stamp ... will force 
		 // a recompile.
		 long preTime = tf.lastModified() ;
		 try {
			 Thread.sleep(1000) ;	// This is so that there will be at least one second between last modified and the next modified.
		} catch (InterruptedException e1) {}
		 try {
			FileWriter out = new FileWriter(tf);
			 out.write(template);
			 out.flush() ;			 
			 out.close();
			 File f ;
			 long startTime = System.currentTimeMillis();
			 do {
			 	if (System.currentTimeMillis()-startTime > 1000)
			 		fail("File system timestamp error");
			 	f = new java.io.File(tf.getAbsolutePath()) ;
			 } while (f.lastModified() <= preTime) ;
			 
		}
		catch (IOException e) {
			fail ("Second fail: " + e.getMessage()) ;
		}
		 try {
		     newInstance = TemplateObjectFactory.getClassInstance(classPath,pathURL,tf.getName(),this.getClass().getClassLoader(),null , null) ;		
		 }
		 catch (Exception e) {
		 	fail("Third fail: " + e.getMessage()) ;
		 }
		 if (newInstance.getClass() == instance.getClass()) 
		   fail ("New class should have been created") ;
		 

	}

	/**
	 * Constructor for TemplateObjectFactoryTest.
	 * @param name
	 */
	public TemplateObjectFactoryTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TemplateObjectFactoryTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// Generate the template and skelatons on a temporary directory
		pathURL = new String[1];
		try {
			tf = File.createTempFile("template", ".jet");
			System.out.println("templatePath: " + tf.getAbsolutePath());
			tf.deleteOnExit();
			FileWriter out = new FileWriter(tf);
			out.write(template);
			out.flush() ;
			out.close();
			pathURL[0] = tf.getParent();
			sf = new File(tf.getParent() + File.separator + skelatonName);
			sf.deleteOnExit();
			out = new FileWriter(sf);
			out.write(skelaton);
			out.flush() ;
			out.close();
		}
		catch (IOException e) {
			fail("Can not create template File:" + e.getMessage());
		}

		// Computer the codeGen compile classPath
		List list = TemplateUtil.getPluginAndPreReqJarPath("org.eclipse.ve.java.core");
		list.addAll(TemplateUtil.getPlatformJREPath());
		classPath = (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		// Clean up the template/skeletons
		tf.delete() ;
		sf.delete() ;
		
	}

}
