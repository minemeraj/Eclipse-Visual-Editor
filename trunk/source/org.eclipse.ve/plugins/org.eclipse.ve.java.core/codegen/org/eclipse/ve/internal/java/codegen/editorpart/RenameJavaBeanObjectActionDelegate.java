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
 *  $RCSfile: RenameJavaBeanObjectActionDelegate.java,v $
 *  $Revision: 1.14 $  $Date: 2005-08-24 23:30:47 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.codegen.java.BeanDecoderAdapter;
import org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter;
import org.eclipse.ve.internal.java.codegen.java.rules.IInstanceVariableCreationRule;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.AbstractRenameInstanceDialog;

import org.eclipse.ve.internal.propertysheet.ISourcedPropertyDescriptor;
import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

/**
 * ObjectActionDelegate for the RenameJavaAction.
 */
public class RenameJavaBeanObjectActionDelegate implements IObjectActionDelegate {

	protected static class RenameDialog extends AbstractRenameInstanceDialog {
		protected IBeanDeclModel bdm = null;
		protected IType type = null;
		IInstanceVariableCreationRule rule = null;
		
		public RenameDialog(Shell parentShell, String[] names, EObject[] annotates, EditDomain domain, boolean forceChange, boolean enableDontShowOption) {
			super(parentShell, names, annotates, domain, forceChange, enableDontShowOption);
			this.rule = (IInstanceVariableCreationRule) domain.getRuleRegistry().getRule(IInstanceVariableCreationRule.RULE_ID);
			if(annotates!=null && annotates.length>0){
				BeanDecoderAdapter beanDecoderAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(annotates[0],
						ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
				this.bdm = beanDecoderAdapter.getBeanPart().getModel();
				this.type = CodeGenUtil.getMainType(bdm.getWorkingCopyProvider().getWorkingCopy(true));
			}
		}

		protected String getValidInstanceVariableName(EObject instance, String name, List currentNames) {
			String suggestedName =  rule.getValidInstanceVariableName(instance, name, type, bdm);
			if(currentNames.contains(suggestedName)){
				String newSuggested = suggestedName;
				int count=1;
				while(currentNames.contains(newSuggested) && count<1000){ // small safety check
					newSuggested = suggestedName+(count++);
					newSuggested = rule.getValidInstanceVariableName(instance, newSuggested, type, bdm);
				}
				suggestedName = newSuggested;
			}
			return suggestedName;
		}

		/**
		 * @see org.eclipse.jface.window.Window#configureShell(Shell)
		 */
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(CodegenEditorPartMessages.RenameJavaBeanObjectActionDelegate_Shell_Text); 
		}

		protected Control createDialogArea(Composite parent) {
			setTitle(CodegenEditorPartMessages.RenameJavaBeanObjectActionDelegate_FieldNaming_Title); 
			setMessage(CodegenEditorPartMessages.RenameJavaBeanObjectActionDelegate_Message); 
			return super.createDialogArea(parent);
		}
		
	}

	protected List selectedEPs;
	protected List selectedEOs;
	protected IWorkbenchPart targetPart;

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
		EditDomain domain = null;
		
		List names = new ArrayList();
		List nameDescs = new ArrayList();
		List modelPSs = new ArrayList();
		
		for (int count = 0; count < selectedEPs.size(); count++) {
			boolean valid = true;
			EditPart selectedEP = (EditPart) selectedEPs.get(count);
			IPropertySource modelPS = (IPropertySource) selectedEP.getAdapter(IPropertySource.class);
			IPropertyDescriptor nameDesc = null;
			if (modelPS == null){
				valid = false; // Not valid for some reason.
			}else{
				IPropertyDescriptor[] descrs = modelPS.getPropertyDescriptors();
				for (int i = 0; i < descrs.length; i++) {
					if (descrs[i].getId() == NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY) {
						nameDesc = descrs[i];
						break;
					}
				}
				if (!(nameDesc instanceof ICommandPropertyDescriptor && nameDesc instanceof ISourcedPropertyDescriptor)){
					valid = false; // Not a valid descriptor, or no descriptor, so no name.
				}
			}
			if(!valid){
				selectedEPs.remove(count);
				selectedEOs.remove(count);
				count--;
			}else{
				names.add(((ISourcedPropertyDescriptor) nameDesc).getValue(modelPS));
				nameDescs.add(nameDesc);
				modelPSs.add(modelPS);
				if(domain==null && selectedEP.getViewer().getEditDomain()!=null)
					domain = (EditDomain) selectedEP.getViewer().getEditDomain();
			}
		}
		
		if(names.size()<1) 
			return; // nothing worthy to rename
		
		RenameDialog dialog = new RenameDialog(targetPart.getSite().getShell(), 
																	(String[]) names.toArray(new String[names.size()]),
																	(EObject[]) selectedEOs.toArray(new EObject[names.size()]), 
																	domain, 
																	true, false);
		if (dialog.open() == Window.OK) {
			// Now need to apply it.
			final CommandBuilder builder = new CommandBuilder();
			String[] finalNames = dialog.getFinalNames();
			for (int count = 0; count < names.size(); count++) {
				builder.append(((ICommandPropertyDescriptor) nameDescs.get(count)).setValue((IPropertySource)modelPSs.get(count), finalNames[count]));
			}
			domain.getCommandStack().execute(builder.getCommand());
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// We can be assured that we are only a java bean. That testing was already done in the <filter> section.
		// Test to see if action is enabled, if it is, that means we've already passed the enablesFor test.
		if (!action.isEnabled())
			return;

		selectedEPs = null;
		if (!(selection instanceof IStructuredSelection))
			action.setEnabled(false);
		else {
			IStructuredSelection ss = (IStructuredSelection) selection;
			List selectedList = new ArrayList(ss.toList());
			for (int count = 0; count < selectedList.size(); count++) {
				if(!(selectedList.get(count) instanceof EditPart)){
					selectedList.remove(count);
					count--;
				}
			}
			
			selectedEOs = new ArrayList(selectedList.size());
			selectedEPs = new ArrayList(selectedList.size());
			for (int count = 0; count < selectedList.size(); count++) {
				selectedEPs.add(selectedList.get(count));
				selectedEOs.add(((EditPart) selectedEPs.get(count)).getModel());
			}
			
			// TODO: Needed?
			// KLUDGE,KLUDGE!! We can't allow another rename to occur until the previous one has flushed
			// through the system and reload has been done. To prevent that we need to get a flag out of the
			// edit domain and see if it is still there. If it is, then we can't be enabled because the reload
			// hasn't finished. This is a major kludge. Need a better way in the future. Need a way of turning off
			// all model changes while we have pending ones out there. Such as the hold changes flag.
//			if (action.isEnabled()) {
//				// Don't bother checking if not enabled.
//				EditDomain ed = EditDomain.getEditDomain(selectedEP);
//				synchronized (STILL_FLUSHING_KEY) {
//					if (ed.getData(STILL_FLUSHING_KEY) != null)
//						action.setEnabled(false);
//				}
//			}
		}
	}

}
