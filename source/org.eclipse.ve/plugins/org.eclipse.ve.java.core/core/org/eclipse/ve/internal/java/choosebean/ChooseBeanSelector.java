package org.eclipse.ve.internal.java.choosebean;
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
 *  $RCSfile: ChooseBeanSelector.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class ChooseBeanSelector
	implements SelectionCreationToolEntry.ISelector {
 
protected SelectionCreationToolEntry creationToolEntry = null;

public ChooseBeanSelector(){
}

protected String getPluginName(){
	return ResourcesPlugin.getPlugin().getDescriptor().getLabel();
}
public Object[] getNewObjectAndType(SelectionCreationToolEntry.SelectionCreationTool creationTool){
	creationToolEntry = creationTool.getSelectionToolEntry();
	EditDomain ed = (EditDomain)creationTool.getDomain() ;	
	ChooseBeanDialog dialog = new ChooseBeanDialog(
							Display.getDefault().getActiveShell(), 
							ed,
							ChooseBeanDialog.CHOICE_DEFAULT,
							false);
	if(dialog.open()==Window.OK){
		try{
			Object[] results = dialog.getResult();
			JavaVEPlugin.log(ChooseBeanMessages.getString("ToolSelector.SelectionLogMessage")+results[0], MsgLogger.LOG_FINE); //$NON-NLS-1$
			return results;
		}catch(Exception e){
			return null;
		}
	}else{
		return null;
	}
}

}