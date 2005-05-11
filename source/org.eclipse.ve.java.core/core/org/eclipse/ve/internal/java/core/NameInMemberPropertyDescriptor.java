/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NameInMemberPropertyDescriptor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-05-11 22:41:32 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.AnnotationEMF;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.MemberContainer;

import org.eclipse.ve.internal.java.vce.VCEPreferences;
 

/**
 * This is a composition name property descriptor that makes the name unique for the MemberContainer
 * it is in, and not unique within the whole composition, like the superclass does.
 * 
 * @since 1.0.0
 */
public class NameInMemberPropertyDescriptor extends NameInCompositionPropertyDescriptor {

	/**
	 * Dialog class to name beans when they are being added from the UI (Palette, ChooseBean, etc.)
	 * 
	 * @since 1.1
	 */
	public class NameChangeDialog extends AbstractRenameInstanceDialog{
		public NameChangeDialog(Shell parentShell, String[] names, EObject[] annotates, EditDomain domain, boolean forceChange, boolean enableDontShowOption) {
			super(parentShell, names, annotates, domain, forceChange, enableDontShowOption);
		}

		protected String getValidInstanceVariableName(EObject instance, String name, java.util.List currentNames) {
			return getUniqueNameInComposition(domain, name, new HashSet(currentNames));
		}

		protected Control createDialogArea(Composite parent) {
			setTitle(JavaMessages.getString("NameInMemberPropertyDescriptor.NameChangeDialog.Dialog.Title")); //$NON-NLS-1$
			setMessage(JavaMessages.getString("NameInMemberPropertyDescriptor.NameChangeDialog.Dialog.Message")); //$NON-NLS-1$
			return super.createDialogArea(parent);
		}

		protected void configureShell(Shell newShell) {
			newShell.setText(JavaMessages.getString("NameInMemberPropertyDescriptor.NameChangeDialog.Shell.Title")); //$NON-NLS-1$
			super.configureShell(newShell);
		}
	}
	/**
	 * Member based name validator. It will validate name within the member container
	 * of the source.
	 * 
	 * @since 1.0.0
	 */
	public static class MemberBasedNameValidator extends NameValidator {

		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor.NameValidator#getSuggestedName(java.lang.String)
		 */
		protected String getSuggestedName(String name) {
			Object source = pos[0].getEditableValue();
			if (!(source instanceof EObject))
				return super.getSuggestedName(name);	// Don't know what it is, do unique in composition.
			EObject container = ((EObject) source).eContainer();
			if (!(container instanceof MemberContainer))
				return super.getSuggestedName(name);	// Don't know what it is, do unique in composition.
			return getUniqueNameInMember(domain, (MemberContainer) container, name); 
		}
	}
	
//	public static class NameChangeDialog extends Dialog{
//		
//		protected String[] names = null;
//		protected EObject[] annotates = null;
//		protected EditDomain domain = null;
//		protected ILabelProvider labelProvider = null;
//		
//		public NameChangeDialog(Shell parentShell, String[] names, EObject[] annotates, EditDomain domain) {
//			super(parentShell);
//			this.names = names;
//			this.annotates = annotates;
//			this.domain = domain;
//			setShellStyle(getShellStyle()|SWT.RESIZE);
//		}
//		
//		protected Control createDialogArea(Composite parent) {
//			Composite top = new Composite((Composite) super.createDialogArea(parent), SWT.NONE);
//			top.setLayout(new GridLayout(3, false));
//			for (int nameCount = 0; nameCount < names.length; nameCount++) {
//				Image image = JavaVEPlugin.getJavaBeanImage();
//				String name = names[nameCount];
//				if(annotates[nameCount]!=null){
//					labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(domain).getLabelProvider(annotates[nameCount].eClass());
//					if(labelProvider!=null){
//						name = labelProvider.getText(annotates[nameCount]);
//						image = labelProvider.getImage(annotates[nameCount]);
//					}
//				}
//				Label imageLabel = new Label(top, SWT.NONE);
//				imageLabel.setImage(image);
//				
//				Label nameLabel = new Label(top, SWT.NONE);
//				nameLabel.setText(name);
//				
//				Text text = new Text(top, SWT.BORDER);
//				text.setText(names[nameCount]);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//			}
//			
//			Button checkbox = new Button(top, SWT.CHECK);
//			checkbox.setText("Dont ask again");
//			GridData data = new GridData(GridData.FILL_HORIZONTAL);
//			data.horizontalSpan=3;
//			data.widthHint = 300;
//			data.horizontalAlignment=SWT.END;
//			checkbox.setLayoutData(data);
//
//			return top;
//		}
//		
//		public String[] getCurrentNames(){
//			return names;
//		}
//	    protected void createButtonsForButtonBar(Composite parent) {
//	        // create OK button only
//	        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
//	                true);
//	    }
//	}
	
	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public NameInMemberPropertyDescriptor() {
		super();
	}
	
	/**
	 * @param displayNameToUse
	 * 
	 * @since 1.0.0
	 */
	public NameInMemberPropertyDescriptor(String displayNameToUse) {
		super(displayNameToUse);
	}
	
	/**
	 * @param displayNameToUse
	 * @param additionalValidator
	 * 
	 * @since 1.0.0
	 */
	public NameInMemberPropertyDescriptor(String displayNameToUse, ICellEditorValidator additionalValidator) {
		super(displayNameToUse, additionalValidator);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor#getNameValidator()
	 */
	protected NameValidator getNameValidator() {
		return new MemberBasedNameValidator();
	}

	/**
	 * Get a unique name in member using the given base name.
	 * @param domain
	 * @param container the membercontainer to look into.
	 * @param name base name to start with
	 * @return unique name in member container based upon starting name
	 * 
	 * @since 1.0.0
	 */
	public static String getUniqueNameInMember(EditDomain domain, MemberContainer container, String name) {
		return getUniqueNameInMember(domain, container, name, null);
	}
	
	/**
	 * Get a unique name in composition using the given base name. It will also
	 * look in the Set of other names if the set is not null. This allows for checking
	 * for an add of group at once, so that they also don't duplicate themselves.
	 * 
	 * @param domain
	 * @param container the membercontainer to look into.
	 * @param name base name to start with
	 * @param otherNames set of other names (names not yet in member, but will be, so don't duplicate) <code>null</code> if no other names.
	 * @return unique name in member container based upon starting name
	 * 
	 * @since 1.0.0
	 */
	public static String getUniqueNameInMember(EditDomain domain, MemberContainer container, String name, Set otherNames) {
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		String baseName = null;
		if (name != null)
			baseName = name;
		else
			baseName = CDEMessages.getString("PropertyDescriptor.NameInComposition.Default"); // Use a default. //$NON-NLS-1$
		String componentName = baseName;
		int incr = 0;
		main : while (true) {
			if (otherNames != null && otherNames.contains(componentName)) {
				componentName = baseName + ++incr;
				continue;
			}
			Iterator itr = container.getMembers().iterator();
			while (itr.hasNext()) {
				Annotation an = policy.getAnnotation(itr.next());
				if (an != null) {
					BasicEMap.Entry ks = getMapEntry(an, NAME_IN_COMPOSITION_KEY);
					if (ks != null && componentName.equals(ks.getValue())) {
						componentName = baseName + ++incr;
						continue main;
					}
				}
			}
			break;
		}

		return componentName;
	}

	/**
	 * 
	 * @since 1.1
	 */
	public String[] getUniqueNamesInComposition(EditDomain domain, String[] names, Annotation[] annotations) {
		String[] uniques = super.getUniqueNamesInComposition(domain, names, annotations);
		if(JavaVEPlugin.getPlugin().getPluginPreferences().getBoolean(VCEPreferences.RENAME_INSTANCE_ASK_KEY)){
			EObject[] annotates = new EObject[annotations.length];
			for (int i = 0; i < annotations.length; i++) {
				if (annotations[i] instanceof AnnotationEMF) {
					AnnotationEMF annotationEMF = (AnnotationEMF) annotations[i];
					annotates[i] = annotationEMF.getAnnotates();
				}else{
					annotates[i] = null;
				}
			}
			NameChangeDialog dialog = new NameChangeDialog(domain.getEditorPart().getSite().getShell(), uniques, annotates, domain, false, true);
			if(dialog.open()==Dialog.OK){
				uniques = dialog.getFinalNames();
			}
		}
		return uniques;
	}

}
