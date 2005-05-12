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
 *  $RCSfile: PasteJavaBeanObjectActionDelegate.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-12 15:28:46 $ 
 */

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;

import org.eclipse.ve.internal.java.core.PasteJavaBeanAction;

/**
 * ObjectActionDelegate for the PasteJavaBeanAction.
 */
public class PasteJavaBeanObjectActionDelegate implements IObjectActionDelegate {

	private PasteJavaBeanAction pasteCopyJavaBeanAction;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

		if (targetPart instanceof JavaVisualEditorPart){
			pasteCopyJavaBeanAction = ((JavaVisualEditorPart)targetPart).pasteBeanAction;
		} else if (targetPart instanceof BeansList){
			IPage beansListPage = ((BeansList)targetPart).getCurrentPage();			
			pasteCopyJavaBeanAction = ((JavaVisualEditorOutlinePage)beansListPage).getPasteAction();
		}
		action.setEnabled(pasteCopyJavaBeanAction != null);
	}


	public void run(IAction action) {

		pasteCopyJavaBeanAction.run();
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		
	}


}