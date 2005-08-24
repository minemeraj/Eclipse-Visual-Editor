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

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/*
 *  $RCSfile: AbstractCodeRef.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:47 $ 
 */


public abstract class AbstractCodeRef {

private     int            fsrcOffset,
                           fsrcLen ;
private     String         fContent ;										
										
public AbstractCodeRef(int Off,int Len, String Content) {
	fsrcOffset = Off ;
	fsrcLen = Len ;
	fContent = Content ;
}										

public AbstractCodeRef() {
  fsrcLen = fsrcOffset = -1 ;
  fContent = null ;	
}

/**
 *
 */
public int getOffset() {
	return fsrcOffset ;
}
public void setOffset (int off) {
	fsrcOffset = off ;
}
public int getLen() {
	return fsrcLen ;
} 

public String getContent() {
	return fContent ;
}

public void setContent(String content) {
	if (content == null) 
		fsrcLen = -1 ;
      else
            fsrcLen = content.length() ;                  
	fContent = content ;      
}

protected void setLen(int len) {
	fsrcLen = len;
}

public String toString() {
	return super.toString() + ":" + fContent ; //$NON-NLS-1$
}

public abstract int isEquivalent(AbstractCodeRef codeRef) throws CodeGenException ;

}
