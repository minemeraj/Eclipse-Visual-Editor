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
 *  $RCSfile: IEventDecoder.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:09 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;


import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.AbstractEventInvocation;
import org.eclipse.ve.internal.jcm.Callback;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public interface IEventDecoder extends IJVEDecoder {

  
   	// Generate the source code from the JVE model
   public String generate(AbstractEventInvocation ei, Object[] args) throws CodeGenException ;
   public AbstractEventInvocation getEventInvocation() ;
   public void setEventInvocation(AbstractEventInvocation eventInvocation) ;
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
