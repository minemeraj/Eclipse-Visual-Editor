package org.eclipse.ve.internal.swt;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TableContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-08 15:03:04 $ 
 */


import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
/**
 * Container Edit Policy for Tables/Columns.
 */
public class TableContainerPolicy extends CompositeContainerPolicy {
	
	public TableContainerPolicy(EditDomain domain) {
		super(domain);
		// Override the containment feature from CompositeContainer
		containmentSF = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), SWTConstants.SF_TABLE_COLUMNS);
	}

}
