/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ICallbackDecoder.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:09 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.ve.internal.jcm.Callback;


/**
 * @author Gili Mendel
 *
 */
public interface ICallbackDecoder extends IJVEDecoder {


   public void addChangeListener(IExpressionChangeListener l) ;
   public void removeChangeListener(IExpressionChangeListener l) ;
   public void deleteFromSrc() ;
   public void setCallBack(Callback c) ;
   public Callback getCallBack() ;
 

}
