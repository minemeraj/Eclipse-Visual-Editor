package org.eclipse.ve.tests.codegen.util;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FieldTest8.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:59:09 $ 
 */

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FieldTest8 extends FieldTest {

	private static String field = "\r\n  \r\nprivate javax.swing.JPanel jPanel19 = null    ;    /**  @jve:visual-info  decl-index=0 visual-constraint=\"122,51\"\r\n  hello */\r\n  \r\n ";
	private static int[] codeStartEnd = {field.indexOf("private javax"), field.lastIndexOf(';')-1};
	private static String[] fillerCodeComment = {"", "private javax.swing.JPanel jPanel19 = null    ","/**  @jve:visual-info  decl-index=0 visual-constraint=\"122,51\"\r\n  hello */"};

	/**
	 * Constructor for FieldTest1.
	 * @param field
	 * @param codeStartEnd
	 * @param fillerCodeComment
	 */
	public FieldTest8() {
		super(field, codeStartEnd, fillerCodeComment);
	}

	/**
	 * Constructor for FieldTest1.
	 * @param arg0
	 * @param field
	 * @param codeStartEnd
	 * @param fillerCodeComment
	 */
	public FieldTest8(String arg0){
		super(arg0, field, codeStartEnd, fillerCodeComment);
	}

}
