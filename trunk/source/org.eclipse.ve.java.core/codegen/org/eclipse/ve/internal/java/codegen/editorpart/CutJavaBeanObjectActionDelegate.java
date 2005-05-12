/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.editorpart;

/*
 *  $RCSfile: CutJavaBeanObjectActionDelegate.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-12 15:28:46 $ 
 */

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;

import org.eclipse.ve.internal.java.core.CutJavaBeanAction;

/**
 * ObjectActionDelegate for the CutJavaBeanAction.
 */
public class CutJavaBeanObjectActionDelegate implements IObjectActionDelegate {

	private CutJavaBeanAction cutCopyJavaBeanAction;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

		if (targetPart instanceof JavaVisualEditorPart){
			cutCopyJavaBeanAction = ((JavaVisualEditorPart)targetPart).cutBeanAction;
		} else if (targetPart instanceof BeansList){
			IPage beansListPage = ((BeansList)targetPart).getCurrentPage();			
			cutCopyJavaBeanAction = ((JavaVisualEditorOutlinePage)beansListPage).jve.cutBeanAction;

		}
		action.setEnabled(cutCopyJavaBeanAction != null);
	}

	public void run(IAction action) {

		cutCopyJavaBeanAction.run();
		
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}


}