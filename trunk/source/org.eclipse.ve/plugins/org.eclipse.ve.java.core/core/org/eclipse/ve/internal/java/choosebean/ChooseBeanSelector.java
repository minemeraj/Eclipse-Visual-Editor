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
package org.eclipse.ve.internal.java.choosebean;
/*
 *  $RCSfile: ChooseBeanSelector.java,v $
 *  $Revision: 1.9 $  $Date: 2005-02-15 23:23:55 $ 
 */

import java.util.logging.Level;

import org.eclipse.jface.window.Window;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class ChooseBeanSelector
	implements SelectionCreationToolEntry.ISelector {
 
protected SelectionCreationToolEntry creationToolEntry = null;

public ChooseBeanSelector(){}

public Object[] getNewObjectAndType(SelectionCreationToolEntry.SelectionCreationTool creationTool){
	creationToolEntry = creationTool.getSelectionToolEntry();
	EditDomain ed = (EditDomain)creationTool.getDomain() ;	
	ChooseBeanDialog dialog = new ChooseBeanDialog(
							ed.getEditorPart().getSite().getShell(), 
							ed,
							null,
							-1,
							false);
	if(dialog.open()==Window.OK){
		try{
			Object[] results = dialog.getResult();
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log(ChooseBeanMessages.getString("ToolSelector.SelectionLogMessage")+results[0], Level.FINE); //$NON-NLS-1$
			return results;
		}catch(Exception e){
			return null;
		}
	}else{
		return null;
	}
}

}
