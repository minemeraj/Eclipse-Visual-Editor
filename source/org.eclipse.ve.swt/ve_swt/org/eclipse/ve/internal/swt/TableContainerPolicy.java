/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: TableContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:51:47 $ 
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
