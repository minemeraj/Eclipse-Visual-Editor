package org.eclipse.ve.internal.cde.emf;
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
 *  $RCSfile: IDomainedFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:58 $ 
 */

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * If a CreationTool.Factory implements this method, then
 * it needs to know the EditDomain to work. The CDECreationTool
 * will know to set the domain when it is activated onto its factory,
 * if the factory implements this interface.
 * @version 	1.0
 * @author
 */
public interface IDomainedFactory {
	
	public void setEditDomain(EditDomain domain);

}
