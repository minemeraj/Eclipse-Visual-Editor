/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IEventSrcGenerator.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.ve.internal.jcm.Callback;

/**
 * @author Gili Mendel
 *
 */
public interface IEventSrcGenerator {

/**
 * Returns the line seperator.
 * @return String
 */
 public String getSeperator() ;

/**
 * Sets the line seperator.
 * @param seperator The seperator to set
 */
 public void setSeperator(String seperator) ;
 
 public String generateEvent() ;
 public String generateEventMethod(Callback[] callbacks) ;
 public void setEventArgName(String eventName) ;


/**
 * Returns the indent Filler
 * @return String
 */
public String getIndent() ;

/**
 * Sets the indent Filler
 * @param indent The indent to set
 */
public void setIndent(String indent) ;


}
