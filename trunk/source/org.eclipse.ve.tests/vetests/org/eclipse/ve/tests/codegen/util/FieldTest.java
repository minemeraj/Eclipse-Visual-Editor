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
package org.eclipse.ve.tests.codegen.util;
/*
 *  $RCSfile: FieldTest.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:54:15 $ 
 */
import org.eclipse.ve.internal.java.codegen.model.DefaultScannerFactory;
import org.eclipse.ve.internal.java.codegen.util.ExpressionParser;

import junit.framework.TestCase;

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class FieldTest extends TestCase {

	String field = null;
	int[] codeStartLength = null;
	String[] fillerCodeComment = null;
	ExpressionParser parser = null;

	/**
	 * Constructor for FieldTest1.
	 */
	public FieldTest(String field, int[] codeStartEnd, String[] fillerCodeComment) {
		super();
		init(field, codeStartEnd, fillerCodeComment);
	}

	/**
	 * Constructor for FieldTest1.
	 * @param arg0
	 */
	public FieldTest(String arg0, String field, int[] codeStartEnd, String[] fillerCodeComment) {
		super(arg0);
		init(field, codeStartEnd, fillerCodeComment);
	}
	
	private void init(String field, int[] codeStartEnd, String[] fillerCodeComment){
		this.field = field;
		this.codeStartLength = new int[codeStartEnd.length];
		codeStartLength[0]=codeStartEnd[0];
		codeStartLength[1]=codeStartEnd[1]-codeStartLength[0]+1;
		this.fillerCodeComment = fillerCodeComment;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		parser = new ExpressionParser(field, codeStartLength[0], codeStartLength[1], new DefaultScannerFactory());
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		parser = null;
	}
	
	public void testExpressionParserFiller(){
		String filler = parser.getFiller();
		assertEquals("Filler", fillerCodeComment[0], filler);
	}
	
	public void testExpressionParserCode(){
		String code = parser.getCode();
		assertEquals("Code", fillerCodeComment[1], code);
	}
	
	public void testExpressionParserComment(){
		String comment = parser.getComment();
		assertEquals("Comment", fillerCodeComment[2], comment);
	}

}
