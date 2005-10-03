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
/*
 *  $RCSfile: IEventDecoder.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-03 19:20:56 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;


import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public interface IEventDecoder extends IJVEDecoder {

  
   	// Generate the source code from the JVE model
   public String generate(AbstractEventInvocation ei, Object[] args) throws CodeGenException ;
   public AbstractEventInvocation getEventInvocation() ;
   public boolean setEventInvocation(AbstractEventInvocation eventInvocation) ;
   public void   setFiller(String filler) ;
   public String getFiller() ;
   public void   addCallBack(Callback c) ;
   public void   removeCallBack(Callback c) ;
   public void   addPropertyEvent(PropertyEvent c) ;
   public void   removePropertyEvent(PropertyEvent c) ;
   public ICodeGenSourceRange getCallBackSourceRange(Callback c) ;
   public ICodeGenSourceRange getPropertyEventSourceRange(PropertyEvent pe) ;
   public IEventDecoderHelper createDecoderHelper(Statement exp);
   
      

}
