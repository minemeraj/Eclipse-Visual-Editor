package org.eclipse.ve.internal.java.codegen.util;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodeTemplateHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
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
