/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.tests.codegen.java.rules;
/*
 *  $RCSfile: CodeGenRulesFactoryTest.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
import org.eclipse.ve.internal.java.vce.rules.IEditorStyle;
import org.eclipse.ve.internal.java.vce.rules.JVEStyleRegistry;

import junit.framework.TestCase;

import org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableRule;

/**
 * @author Gili Mendel
 *
 */
public class CodeGenRulesFactoryTest extends TestCase {
	
	IEditorStyle editorStyle = null ;

	                  
	/**
	 * Use the template defined above to generate a template/skelaton,
	 * and try to create an object out of it.  
	 * Invoke the toString(), and make sure it returns the "signiture"
	 */
	public void testStyleRuleCachingTest() {

	 Object rule = editorStyle.getRule(IInstanceVariableRule.RULE_ID) ;
	 if (!(rule instanceof IInstanceVariableRule))
	   fail ("Invalid Rule") ;
	   
	 // Make sure it is cached
	 assertEquals(rule,editorStyle.getRule(IInstanceVariableRule.RULE_ID)) ;
	 	
	}
	
	public void testStyleRuleTest() {

	 Object rule = editorStyle.getRule(IInstanceVariableRule.RULE_ID) ;
	 if (!(rule instanceof IInstanceVariableRule))
	   fail ("Invalid Rule") ;


// Need to change the style	   
//	 editorStyle.setStyle("jUnitStyle") ;
	 Object newRule = editorStyle.getRule(IInstanceVariableRule.RULE_ID) ;
	 if (!(newRule instanceof IInstanceVariableRule))
	   fail ("Invalid Rule") ;	 	 
	 assertTrue("Overided rule was not generated",newRule == rule) ;
	   
	}
	


	/**
	 * Constructor for TemplateObjectFactoryTest.
	 * @param name
	 */
	public CodeGenRulesFactoryTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(CodeGenRulesFactoryTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		editorStyle = JVEStyleRegistry.getJVEStyleRegistry().getStyle("jUnitStyle") ;

	
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		
	}

}
