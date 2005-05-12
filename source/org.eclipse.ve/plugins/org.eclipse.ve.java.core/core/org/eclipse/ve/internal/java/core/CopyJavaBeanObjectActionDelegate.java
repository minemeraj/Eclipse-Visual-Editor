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
package org.eclipse.ve.internal.java.core;

/*
 *  $RCSfile: CopyJavaBeanObjectActionDelegate.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-12 11:39:56 $ 
 */

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ve.internal.java.codegen.editorpart.BeansList;
import org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorOutlinePage;
import org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditorPart;

/**
 * ObjectActionDelegate for the CopyJavaBeanAction.
 */
public class CopyJavaBeanObjectActionDelegate implements IObjectActionDelegate {

	private CopyJavaBeanAction currentCopyJavaBeanAction;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

		IAction copyJavaBeanAction = null;
		if (targetPart instanceof JavaVisualEditorPart){
			currentCopyJavaBeanAction = ((JavaVisualEditorPart)targetPart).copyBeanAction;
		} else if (targetPart instanceof BeansList){
			IPage beansListPage = ((BeansList)targetPart).getCurrentPage();			
			currentCopyJavaBeanAction = ((JavaVisualEditorOutlinePage)beansListPage).jve.copyBeanAction;

		}
		action.setEnabled(currentCopyJavaBeanAction != null);
	}


	public void run(IAction action) {

		currentCopyJavaBeanAction.run();
		
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}


}