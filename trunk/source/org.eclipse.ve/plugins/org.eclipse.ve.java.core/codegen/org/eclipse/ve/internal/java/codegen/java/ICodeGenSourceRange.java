package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: ICodeGenSourceRange.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
/**
 * @author
 */
public interface ICodeGenSourceRange {
    
   public int getOffset () ;
   public int getLength() ;
   public int getLineOffset() ;
   

}
