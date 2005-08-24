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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: ICodeGenSourceRange.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:45 $ 
 */
/**
 * @author
 */
public interface ICodeGenSourceRange {
    
   public int getOffset () ;
   public int getLength() ;
   public int getLineOffset() ;
   

}
