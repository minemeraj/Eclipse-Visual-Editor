package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: NoopCreationPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:42:05 $ 
 */

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFCreationTool;

public class NoopCreationPolicy implements EMFCreationTool.CreationPolicy {
	
public Command getCommand(Command aCommand, EditDomain domain, CreateRequest aRequest){
	return aCommand;
}
    /**
     * @see CreationPolicy#getDefaultSuperString(EClass)
     */
    public String getDefaultSuperString(EClass superClass) {
        return null ;
    }

}
