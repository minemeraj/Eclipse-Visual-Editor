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
 *  $RCSfile: IEventTemplate.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:46 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

/**
 * @author Gili Mendel
 *
 */
public interface IEventTemplate {

  public String generateEvent(AbstractEventSrcGenerator.EventInfo info) ;
  public IEventTemplate createNLTemplate(String nl);

}
