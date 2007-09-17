/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.choosebean;

/*
 * $RCSfile: ChooseBeanSelector.java,v $ $Revision: 1.14 $ $Date: 2007-09-17 14:21:53 $
 */

import org.eclipse.gef.tools.CreationTool;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;

public class ChooseBeanSelector implements SelectionCreationToolEntry.ISelector {

	public ChooseBeanSelector() {
	}

	public Object[] getNewObjectAndType(CreationTool creationTool, org.eclipse.gef.EditDomain domain) {
		EditDomain ed = (EditDomain) domain;
		//TODO: 33 port. contributed by Erik Hecht. List all types in the dialog by default
		return ChooseBeanDialog.getChooseBeanResults(ed, null, "**");
	}

}
