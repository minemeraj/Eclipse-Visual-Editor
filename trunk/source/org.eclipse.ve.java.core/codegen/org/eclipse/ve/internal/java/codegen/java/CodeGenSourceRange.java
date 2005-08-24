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
 *  $RCSfile: CodeGenSourceRange.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:44 $ 
 */

import org.eclipse.jdt.core.ISourceRange;

/**
 * @version 	1.0
 * @author
 */
public class CodeGenSourceRange implements ICodeGenSourceRange {
    
    int fOffset,
        fLen,
        fLine ;
    
   public CodeGenSourceRange (ISourceRange sr) {
       if (sr != null) {
        fOffset = sr.getOffset() ;
        fLen = sr.getLength() ;
       }
       else {
        fOffset = fLen = -1 ;
       }
       fLine = -1 ;
   }
   
   public CodeGenSourceRange (int off, int len) {
       fOffset = off ;
       fLen = len ;
       fLine = -1 ;
   }
   public int getOffset () {
       return fOffset ;
   }
   
   public int getLineOffset() {
       return fLine ;
   }
   public void setLineOffset(int line) {
       fLine = line ;
   }
   public int getLength() {
       return fLen ;
   }
   
   public String toString() {
       return "(off:"+fOffset+", line:"+fLine+", len:"+fLen+")" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
   }
   
   public void setOffset(int off) {
       fOffset = off ;
   }
   
   public void setLen(int len) {
       fLen = len ;
   }
   

}
