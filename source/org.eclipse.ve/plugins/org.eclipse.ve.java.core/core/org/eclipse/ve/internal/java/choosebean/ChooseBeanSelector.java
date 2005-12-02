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
 * $RCSfile: ChooseBeanSelector.java,v $ $Revision: 1.13 $ $Date: 2005-12-02 16:31:20 $
 */

import org.eclipse.gef.tools.CreationTool;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;

public class ChooseBeanSelector implements SelectionCreationToolEntry.ISelector {

	public ChooseBeanSelector() {
	}

	public Object[] getNewObjectAndType(CreationTool creationTool, org.eclipse.gef.EditDomain domain) {
		EditDomain ed = (EditDomain) domain;
		return ChooseBeanDialog.getChooseBeanResults(ed, null, null);
	}

}
