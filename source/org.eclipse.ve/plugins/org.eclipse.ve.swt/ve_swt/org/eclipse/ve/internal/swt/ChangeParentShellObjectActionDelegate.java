/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChangeParentShellObjectActionDelegate.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:52:54 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Change the parent of an org.eclipse.swt.widget.Shell such that the ctor's first argument is another referenced Shell on the free form
 * 
 * @since 1.0.0
 */
public class ChangeParentShellObjectActionDelegate implements IObjectActionDelegate {

	protected IStructuredSelection selection;
	protected EditPart thisEditPart;
	protected int selectedIndex = -1;
	protected int initialIndex = -1;
	protected IWorkbenchPart targetPart;

	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (!(selection instanceof IStructuredSelection) || selection.isEmpty())
			action.setEnabled(false);
		else {
			// Disable if no other additional objects on the canvas
			this.selection = (IStructuredSelection) selection;
			thisEditPart = (EditPart) this.selection.getFirstElement();
			boolean enabled = false;
			List freeformParts = thisEditPart.getParent().getChildren();
			if (freeformParts.size() > 1) {
				enabled = true;
			}
			action.setEnabled(enabled);
		}
	}

	/**
	 * Return a List of model objects whose type is Shell
	 */
	protected List getShells() {
		JavaHelpers shellClass = Utilities.getJavaType("org.eclipse.swt.widgets.Shell", ((IJavaObjectInstance) thisEditPart.getModel()).eResource() //$NON-NLS-1$
				.getResourceSet());
		List freeformParts = thisEditPart.getParent().getChildren();
		List shellList = new ArrayList(1);
		for (Iterator iter = freeformParts.iterator(); iter.hasNext();) {
			EditPart freeformEP = (EditPart) iter.next();
			if (freeformEP == thisEditPart)
				continue;
			Object model = freeformEP.getModel();
			if (model instanceof IJavaObjectInstance && shellClass.isAssignableFrom(((IJavaObjectInstance) model).eClass())) {
				shellList.add(model);
			}
		}
		return shellList;
	}

	/**
	 * Open the dialog listing the other shells. If selection changes, create and execute a command to change the parent.
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		List shellList = getShells();
		ChangeParentDialog dialog = new ChangeParentDialog(targetPart.getSite().getShell(), shellList);
		if (dialog.open() == Window.OK) {
			if (selectedIndex != -1 && selectedIndex != initialIndex)
				EditDomain.getEditDomain(thisEditPart).getCommandStack().execute(getChangeParentCommand(shellList));
		}
	}

	/*
	 * Create the command to change the parent of this Shell by changing the parse tree allocation. If the selectedIndex of the shell list is the
	 * first one <none>, the referenced Shell is to be removed. Otherwise, the first argument is to be the referenced Shell based on the selectedIndex
	 * from the shell list.
	 */
	protected Command getChangeParentCommand(final List shellList) {
		Command changeParentCommand = new CommandWrapper() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				IJavaObjectInstance thisModel = (IJavaObjectInstance) thisEditPart.getModel();
				ParseTreeAllocation allocation = (ParseTreeAllocation) thisModel.getAllocation();
				PTExpression exp = allocation.getExpression();
				List orgArgs = null;
				PTInstanceReference ir = null;
				PTClassInstanceCreation ic = null;
				if (exp instanceof PTClassInstanceCreation) {
					ic = (PTClassInstanceCreation) exp;
					orgArgs = ic.getArguments();
				}
				// set the arguments
				if (selectedIndex > 0) {
					ir = InstantiationFactory.eINSTANCE.createPTInstanceReference();
					ir.setObject((IJavaObjectInstance) shellList.get(selectedIndex - 1));
					if (orgArgs != null && !orgArgs.isEmpty()) {
						// First argument is another Shell reference... just replace it with the new reference,
						// old additional args stay in same place.
						if (orgArgs.get(0) instanceof PTInstanceReference)
							orgArgs.set(0, ir);
						else {
							// Create new reference and arg list. Preserve the old args.
							ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation();
							ic.setType(thisModel.getJavaType().getJavaName());
							ic.getArguments().add(ir);
							for (int i = 0; i < orgArgs.size(); i++)
								ic.getArguments().add(orgArgs.get(i));
						}
					} else {
						// First arg is reference with no additional arguments
						ic.getArguments().add(ir);
					}
				} else {
					// Set with no parent. Preserve addition args.
					ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation();
					ic.setType(thisModel.getJavaType().getJavaName());
					if (orgArgs != null && !orgArgs.isEmpty()) {
						for (int i = 1; i < orgArgs.size(); i++)
							ic.getArguments().add(orgArgs.get(i));
					}
				}
				// Create new allocation and set the model in order to trigger the notifications to force code generation
				JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
				RuledCommandBuilder cb = new RuledCommandBuilder(EditDomain.getEditDomain(thisEditPart));
				cb.applyAttributeSetting(thisModel, thisModel.eClass().getEStructuralFeature("allocation"), alloc); //$NON-NLS-1$
				command = cb.getCommand();
				command.execute();
			}
		};
		return changeParentCommand;
	}

	/**
	 * Dialog containing a list of the Shells for setting the parent of this Shell.
	 *  
	 */
	private class ChangeParentDialog extends Dialog {
		protected List shellList;
		protected Composite mainComposite = null;
		protected org.eclipse.swt.widgets.List selectionList = null;

		/**
		 * @param parentShell
		 * 
		 * @since 1.0.0
		 */
		protected ChangeParentDialog(Shell parentShell, List shellList) {
			super(parentShell);
			this.shellList = shellList;
		}

		/**
		 * @see org.eclipse.jface.window.Window#create() Override to initially disable the OK button
		 */
		public void create() {
			super.create();
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}

		protected Composite createComposite(Composite comp) {
			Composite mainComposite = new Composite(comp, SWT.NONE);
			mainComposite.setLayout(new GridLayout(2, false));

			Label label1 = new Label(mainComposite, SWT.NONE);
			label1.setText(SWTMessages.ChangeParentShellObjectActionDelegate_Desc_Label); 
			GridData gd = new GridData();
			gd.horizontalSpan = 2;
			label1.setLayoutData(gd);

			selectionList = new org.eclipse.swt.widgets.List(mainComposite, SWT.BORDER | SWT.SINGLE);
			gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			selectionList.setLayoutData(gd);
			selectionList.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					selectedIndex = selectionList.getSelectionIndex();
					getButton(IDialogConstants.OK_ID).setEnabled(!(selectedIndex == initialIndex));
				}
			});

			/*
			 * Initialize the List using the list of Shells available on the canvas. Get the parse tree allocation from this Shell and see if it
			 * already has a parent set as the first argument. If it does, select this in the list.
			 */
			initialIndex = -1;
			IJavaObjectInstance firstArg = null;
			EditDomain domain = EditDomain.getEditDomain(thisEditPart);
			IJavaObjectInstance thisModel = (IJavaObjectInstance) thisEditPart.getModel();
			ParseTreeAllocation allocation = (ParseTreeAllocation) thisModel.getAllocation();
			PTExpression exp = allocation.getExpression();
			if (exp instanceof PTClassInstanceCreation && !((PTClassInstanceCreation) exp).getArguments().isEmpty()) {
				Object argExp = ((PTClassInstanceCreation) exp).getArguments().get(0);
				if (argExp instanceof PTInstanceReference) {
					firstArg = ((PTInstanceReference) argExp).getObject();
				} else
					initialIndex = 0;
			} else {
				initialIndex = 0;
			}
			selectionList.add(SWTMessages.ChangeParentShellObjectActionDelegate_None); 
			for (int i = 0; i < shellList.size(); i++) {
				IJavaObjectInstance shellModel = (IJavaObjectInstance) shellList.get(i);
				AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
				Annotation ann = policy.getAnnotation(shellModel);
				if (ann != null)
					selectionList.add((String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY));
				if (shellModel == firstArg)
					initialIndex = i + 1;
			}
			// Set the selection list index based on what the initialIndex was set to when the list was intialized.
			selectionList.select(initialIndex);
			applyDialogFont(mainComposite);
			return mainComposite;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
		 */
		protected Control createDialogArea(Composite parent) {
			// top level composite
			Composite parentComposite = (Composite) super.createDialogArea(parent);
			// creates dialog area composite
			return createComposite(parentComposite);
		}

		/**
		 * @see org.eclipse.jface.window.Window#configureShell(Shell)
		 */
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(SWTMessages.ChangeParentShellObjectActionDelegate_Shell_title); 
		}
	}
}
