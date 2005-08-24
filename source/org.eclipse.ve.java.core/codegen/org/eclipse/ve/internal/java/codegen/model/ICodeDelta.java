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
package org.eclipse.ve.internal.java.codegen.model;
/*
 *  $RCSfile: ICodeDelta.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:47 $ 
 */

import java.util.List;

/**
 * Contains change information for a reference method
 */
public interface ICodeDelta {

public static final int CODE_TYPE_EXPRESSION = 0x01;
public static final int CODE_TYPE_METHOD = 0x02;
public static final int CODE_TYPE_FIELD = 0x04;
public static final int CODE_TYPE_IMPORT = 0x08;
public static final int CODE_TYPE_TYPE = 0x10; 

public static final int ELEMENT_NO_CHANGE       =  0x01 ;
public static final int ELEMENT_DELETED         =  0x02; 
public static final int ELEMENT_ADDED           =  0x04; 
public static final int ELEMENT_CHANGED         =  0x08; 
public static final int ELEMENT_UPDATED_OFFSETS =  0x10;
public static final int ELEMENT_UNDETERMINED    =  0x20;

public CodeMethodRef getDeltaMethod () ;

public BeanPart  getDeltaField() ;

public int getElementStatus(AbstractCodeRef element) ;

public List getDeletedElements(AbstractCodeRef element) ;

public int getElementStatus(BeanPart element) ;



}
