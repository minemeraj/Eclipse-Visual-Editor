package org.eclipse.ve.internal.java.codegen.java;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ISynchronizerListener.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:28:35 $ 
 */
/**
 * @version 	1.0
 * @author
 * 
 */
public interface ISynchronizerListener {
    
    // The synchronizer will call this method, when it has
    // processed this marker.
    void markerProcessed(String marker) ;

}