/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.util;
/*
 *  $RCSfile: CodeTemplateHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:47 $ 
 */

public class CodeTemplateHelper {

public static final String DEFAULT_FILLER = "\t"; //$NON-NLS-1$

public static String getFillerForLevel(int level){
	if(level<=0)
		return ""; //$NON-NLS-1$
	String filler = new String();
	for(int i=0;i<level;i++)
		filler = filler.concat(DEFAULT_FILLER);
	return filler;
}

public static int NORMAL_METHOD_DEF_LEVEL = 1;
public static int NORMAL_METHOD_CONTENT_LEVEL = 2;
public static int NORMAL_IF_DEF_LEVEL = NORMAL_METHOD_CONTENT_LEVEL;
public static int NORMAL_IF_CONTENT_LEVEL = 3;
public static int NORMAL_TRY_DEF_LEVEL = NORMAL_IF_CONTENT_LEVEL;
public static int NORMAL_TRY_CONTENT_LEVEL = 4;

public static int getCharLength(String src){
	char[] chars = src.toCharArray();
	int length = 0;
	for(int i=0;i<chars.length;i++){
		if(chars[i]=='\t')
			length+=4;
		else
			length++;
	}
	return length;
}

}
