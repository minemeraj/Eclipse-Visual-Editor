/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
 *  $RCSfile: OpenActionGroup.java,v $
 *  $Revision: 1.6 $  $Date: 2005-09-14 23:30:25 $ 
 */

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.OpenAction;
import org.eclipse.jdt.ui.actions.OpenTypeHierarchyAction;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.OpenWithMenu;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

/**
 * This ActionGroup is for handling the Open actions on the
 * context menus of the jve editor and outline. 
 */
public class OpenActionGroup extends ActionGroup {
	private static class OurOpenAction extends OpenAction {
		private ISelection selection;
		public OurOpenAction(IWorkbenchSite site) {
			super(site);
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.jdt.ui.actions.SelectionDispatchAction#getSelection()
		 */
		public ISelection getSelection() {
			return selection;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.ui.actions.SelectionDispatchAction#selectionChanged(org.eclipse.jface.viewers.ISelection)
		 */
		public void update(ISelection selection) {
			this.selection = (selection instanceof IStructuredSelection) ? selection : StructuredSelection.EMPTY;
			super.update(this.selection);
		}

	}
	
	private static class OurOpenTypeHierarchyAction extends OpenTypeHierarchyAction {
		private ISelection selection;
		public OurOpenTypeHierarchyAction(IWorkbenchSite site) {
			super(site);
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.jdt.ui.actions.SelectionDispatchAction#getSelection()
		 */
		public ISelection getSelection() {
			return selection;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.ui.actions.SelectionDispatchAction#selectionChanged(org.eclipse.jface.viewers.ISelection)
		 */
		public void update(ISelection selection) {
			this.selection = (selection instanceof IStructuredSelection) ? selection : StructuredSelection.EMPTY;
			super.update(this.selection);
		}

	}	
	
	protected OurOpenAction openAction;
	protected OurOpenTypeHierarchyAction openTypeHierarchyAction;
	protected IWorkbenchSite site;
	
	public OpenActionGroup(IWorkbenchPart part) {
		site = part.getSite();
		openAction = new OurOpenAction(site);
		openTypeHierarchyAction = new OurOpenTypeHierarchyAction(site);
	}
	
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		ISelection selection= fixUpSelection(getContext().getSelection());		
		openAction.update(selection);
		openTypeHierarchyAction.update(selection);
		appendToGroup(menu, openAction);	
		addOpenWithMenu(menu, selection);
		appendToGroup(menu, openTypeHierarchyAction);			
	}
	
	private void appendToGroup(IMenuManager menu, IAction action) {
		if (action.isEnabled())
			menu.appendToGroup(IContextMenuConstants.GROUP_OPEN, action);
	}

	private void addOpenWithMenu(IMenuManager menu, ISelection selection) {
		if (selection.isEmpty() || !(selection instanceof IStructuredSelection))
			return;
		IStructuredSelection ss= (IStructuredSelection)selection;
		if (ss.size() != 1)
			return;

		Object o= ss.getFirstElement();
		if (!(o instanceof IAdaptable))
			return;

		IAdaptable element= (IAdaptable)o;
		Object resource= element.getAdapter(IResource.class);
		if (!(resource instanceof IFile))
			return; 

		// Create a menu flyout.
		IMenuManager submenu= new MenuManager(CodegenEditorPartMessages.OpenWithAction_label); 
		submenu.add(new OpenWithMenu(site.getPage(), (IFile) resource));

		// Add the submenu.
		menu.appendToGroup(IContextMenuConstants.GROUP_OPEN, submenu);
	}

	/*
	 * Fix up selection so that the editparts are converted to IJavaElements if possible.
	 * The actions we are using from JDT don't understand how to take the EditPart and get something useful out of it.
	 */
	protected ISelection fixUpSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() != 1)
				return StructuredSelection.EMPTY;	// Just return empty since you can't handle multi-select on open. Faster this way.
			Object e = ss.getFirstElement();
			if (e instanceof EditPart) {
				e = ((EditPart) e).getModel();
				if (e instanceof IJavaObjectInstance) {
					if (e instanceof IJavaObjectInstance) {
						JavaClass eo = (JavaClass) ((IJavaObjectInstance) e).eClass();
						e = eo.getReflectionType();
					}
				}
			} 
			if (!(e instanceof IJavaElement) && e instanceof IAdaptable) {
				e = (IJavaElement) ((IAdaptable) e).getAdapter(IJavaElement.class); 
			}
			return (e instanceof IJavaElement) ? new StructuredSelection(e) : StructuredSelection.EMPTY;	// Just return empty since if not adaptable and not an IJavaElement. Faster this way.
		}
		return selection;
	}
}
