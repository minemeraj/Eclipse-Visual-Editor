package org.eclipse.ve.internal.java.codegen.editorpart;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RenameJavaBeanObjectActionDelegate.java,v $
 *  $Revision: 1.3 $  $Date: 2004-04-19 20:39:12 $ 
 */

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringMessages;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.codegen.java.BeanDecoderAdapter;
import org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter;
import org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * ObjectActionDelegate for the RenameJavaAction.
 */
public class RenameJavaBeanObjectActionDelegate implements IObjectActionDelegate {
	/**
	 * The Rename prompt dialog.
	 */
	private static class RenameDialog extends TitleAreaDialog {
		
		protected String initialName;
		protected EObject field;
		protected IInstanceVariableCreationRule rule;
		protected IType type;
		protected IBeanDeclModel bdm;
		protected String finalName;

		public RenameDialog(Shell parentShell, String initialName, EditPart ep) {
			super(parentShell);
			this.initialName = initialName;
			
		field = (EObject) ep.getModel();
		BeanDecoderAdapter beanDecoderAdapter =
			(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(
				field,
				ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		bdm = beanDecoderAdapter.getBeanPart().getModel();
		type = CodeGenUtil.getMainType(bdm.getWorkingCopyProvider().getWorkingCopy(true));
		EditDomain domain = EditDomain.getEditDomain(ep);
		rule = (IInstanceVariableCreationRule) domain.getRuleRegistry().getRule(IInstanceVariableCreationRule.RULE_ID);
			
		}

		/**
		 * @see org.eclipse.jface.window.Window#configureShell(Shell)
		 */
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(CodegenEditorPartMessages.getString("RenameJavaBeanObjectActionDelegate.Shell.Text")); //$NON-NLS-1$
		}

		/**
		 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea
		 */
		protected Control createDialogArea(Composite parent) {
			// top level composite
			Composite parentComposite = (Composite) super.createDialogArea(parent);

			// creates dialog area composite
			Composite contents = createComposite(parentComposite);
			applyDialogFont(parent);
			return contents;
		}

		protected Image titleImage;
		protected Text input;
		/**
		 * Creates and configures this dialog's main composite.
		 * 
		 * @param parentComposite parent's composite
		 * @return this dialog's main composite
		 */
		private Composite createComposite(Composite parentComposite) {
			// creates a composite with standard margins and spacing
			Composite contents = new Composite(parentComposite, SWT.NONE);

			GridLayout layout = new GridLayout();

			layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
			layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);

			contents.setLayout(layout);
			contents.setFont(parentComposite.getFont());
			contents.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			input = new Text(contents, SWT.LEFT | SWT.BORDER);
			input.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			input.setText(initialName);
			
			final Label label = new Label(contents, SWT.LEFT);
			final MessageFormat format = new MessageFormat(CodegenEditorPartMessages.getString("RenameJavaBeanObjectActionDelegate.FieldNaming.Desc")); //$NON-NLS-1$
			label.setText(format.format(new Object[] {initialName}));
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			input.addModifyListener(new ModifyListener() {
				/**
				 * @see org.eclipse.swt.events.ModifyListener#modifyText(ModifyEvent)
				 */
				public void modifyText(ModifyEvent e) {
					String text = input.getText();
					setErrorMessage(null);					
					if (text.equals(initialName)) {
						label.setText(format.format(new Object[] {initialName}));						
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					} else {
						IStatus validation = JavaConventions.validateFieldName(text);
						if (validation.isOK()) {
							finalName = rule.getValidInstanceVariableName(field, text, type, bdm);
							label.setText(format.format(new Object[] {finalName}));
							getButton(IDialogConstants.OK_ID).setEnabled(true);
						} else {
							setErrorMessage(validation.getMessage());
							getButton(IDialogConstants.OK_ID).setEnabled(false);							
						}
					}
				}
			});

			setTitle(RefactoringMessages.getString("RefactoringGroup.rename_field_title")); //$NON-NLS-1$
			titleImage = JavaPluginImages.DESC_WIZBAN_REFACTOR_FIELD.createImage(parentComposite.getDisplay());
			setTitleImage(titleImage);
			setMessage(CodegenEditorPartMessages.getString("RenameJavaBeanObjectActionDelegate.Message")); //$NON-NLS-1$
			
			input.setFocus();
			return contents;
		}
		
		public String getFinalName() {
			return finalName;
		}
		

		/**
		 * @see org.eclipse.jface.window.Window#close()
		 */
		public boolean close() {
			if (titleImage != null)
				titleImage.dispose();
				
			return super.close();
		}

		/**
		 * @see org.eclipse.jface.window.Window#create()
		 */
		public void create() {
			super.create();
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}

	}
	
	IWorkbenchPart targetPart;
	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		EObject model = (EObject) selectedEP.getModel();
		BeanDecoderAdapter bda  =
			(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(model, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);		
		IJavaElement field = (IJavaElement) bda.getAdapter(IJavaElement.class);
		if (field == null)
			return;	// It has disappeared between popup and selection. Slight possibility of a race condition can cause this.
		RenameDialog dialog = new RenameDialog(targetPart.getSite().getShell(), field.getElementName(), selectedEP);
		if (dialog.open() == Window.OK) {
			try {
				RenameSupport rename =
					RenameSupport.create(
						(IField) field,
						dialog.getFinalName(),
						RenameSupport.UPDATE_GETTER_METHOD
							| RenameSupport.UPDATE_REFERENCES
							| RenameSupport.UPDATE_SETTER_METHOD
							| RenameSupport.UPDATE_TEXTUAL_MATCHES);
				rename.perform(
					targetPart.getSite().getShell(),
					targetPart.getSite().getWorkbenchWindow());
				
			} catch (CoreException e) {
				JavaVEPlugin.log(e);
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
				JavaVEPlugin.log(e.getTargetException());
			}
		}

		
	}
	
	protected EditPart selectedEP;
	private static final Object STILL_FLUSHING_KEY = new Object();

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// We can be assured that we are only a java bean. That testing was already done in the <filter> section.
		// Test to see if action is enabled, if it is, that means we've already passed the enablesFor test.
		if (!action.isEnabled())
			return;
			
		selectedEP = null;
		if (!(selection instanceof IStructuredSelection))
			action.setEnabled(false);
		else {
			IStructuredSelection ss = (IStructuredSelection) selection;
			selectedEP = (EditPart) ss.getFirstElement();
			EObject model = (EObject) selectedEP.getModel();
			BeanDecoderAdapter bda  =
				(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(model, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
			action.setEnabled(bda != null && bda.getAdapter(IJavaElement.class) != null);
			
			// TODO KLUDGE,KLUDGE!! We can't allow another rename to occur until the previous one has flushed
			// through the system and reload has been done. To prevent that we need to get a flag out of the
			// edit domain and see if it is still there. If it is, then we can't be enabled because the reload
			// hasn't finished. This is a major kludge. Need a better way in the future. Need a way of turning off
			// all model changes while we have pending ones out there. Such as the hold changes flag.
			if (action.isEnabled()) {
				// Don't bother checking if not enabled.
				EditDomain ed = EditDomain.getEditDomain(selectedEP);
				synchronized (STILL_FLUSHING_KEY) {
					if (ed.getData(STILL_FLUSHING_KEY) != null)
						action.setEnabled(false);
				}
			}
		}
	}

}
