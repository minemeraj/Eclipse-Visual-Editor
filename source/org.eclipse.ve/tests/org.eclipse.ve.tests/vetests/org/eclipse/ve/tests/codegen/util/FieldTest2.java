package org.eclipse.ve.tests.codegen.util;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FieldTest2.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:38:46 $ 
 */

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FieldTest2 extends FieldTest {

	private static String field = "  \r\n   private javax.swing.JPanel jPanel1 = null  ;  \r\n   ";
	private static int[] codeStartEnd = {field.indexOf("private javax"), field.lastIndexOf(';')-1};
	private static String[] fillerCodeComment = {"   ", "private javax.swing.JPanel jPanel1 = null  ",null};

	/**
	 * Constructor for FieldTest1.
	 * @param field
	 * @param codeStartEnd
	 * @param fillerCodeComment
	 */
	public FieldTest2() {
		super(field, codeStartEnd, fillerCodeComment);
	}

	/**
	 * Constructor for FieldTest1.
	 * @param arg0
	 * @param field
	 * @param codeStartEnd
	 * @param fillerCodeComment
	 */
	public FieldTest2(String arg0){
		super(arg0, field, codeStartEnd, fillerCodeComment);
	}

}
