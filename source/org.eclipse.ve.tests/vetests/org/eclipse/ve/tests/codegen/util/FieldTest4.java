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
 *  $RCSfile: FieldTest4.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FieldTest4 extends FieldTest {

	private static String field = "\r\n  \r\nprivate javax.swing.JPanel jPanel7 = null;//  @jve:visual-info  decl-index=0 visual-constraint=\"122,51\"\r\n  \r\n ";
	private static int[] codeStartEnd = {field.indexOf("private javax"), field.lastIndexOf(';')-1};
	private static String[] fillerCodeComment = {"", "private javax.swing.JPanel jPanel7 = null","//  @jve:visual-info  decl-index=0 visual-constraint=\"122,51\""};

	/**
	 * Constructor for FieldTest1.
	 * @param field
	 * @param codeStartEnd
	 * @param fillerCodeComment
	 */
	public FieldTest4() {
		super(field, codeStartEnd, fillerCodeComment);
	}

	/**
	 * Constructor for FieldTest1.
	 * @param arg0
	 * @param field
	 * @param codeStartEnd
	 * @param fillerCodeComment
	 */
	public FieldTest4(String arg0){
		super(arg0, field, codeStartEnd, fillerCodeComment);
	}

}
