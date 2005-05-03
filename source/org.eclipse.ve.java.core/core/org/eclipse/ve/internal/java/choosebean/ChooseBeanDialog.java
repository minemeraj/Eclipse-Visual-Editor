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
 *  $RCSfile: ChooseBeanDialog.java,v $
 *  $Revision: 1.27 $  $Date: 2005-05-03 21:08:35 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.vce.rules.JVEStyleRegistry;
 
/**
 * ChooseBeanDioalog - for selecting existing beans.
 * 
 * @since 1.1
 */
public class ChooseBeanDialog extends TypeSelectionDialog2 implements ISelectionStatusValidator {

	// Constructor  input
	private ResourceSet resourceSet;
	private IJavaProject project;
	private IPackageFragment pkg;
	private IChooseBeanContributor[] contributors = null;
	private int selectedContributor = -1;
	private boolean disableOthers = false;
	private EditDomain editDomain = null ;
	private IJavaSearchScope javaSearchScope = null;

	// UI components
	Button[] contributorStyleButtons = null;
	String beanName = null; // The below text gets disposed when OK is pressed - keep the text in string
	Text beanNameText = null;
	
	// Status maintainers
	
	protected ChooseBeanDialog(Shell parent, boolean multi, IJavaSearchScope scope, int elementKinds) {
		super(parent, multi, PlatformUI.getWorkbench().getProgressService(), scope, elementKinds);
		setTitle(ChooseBeanMessages.getString("MainDialog.title")); //$NON-NLS-1$
		setMessage(ChooseBeanMessages.getString("MainDialog.message")); //$NON-NLS-1$
		setStatusLineAboveButtons(true);
		setValidator(this);
		this.javaSearchScope = scope;
	}
	
	/**
	 * @param shell
	 * @param ed
	 * @param contributors
	 * @param choice
	 * @param disableOthers
	 * 
	 * @since 1.1
	 */
	public ChooseBeanDialog(Shell shell, EditDomain ed, IChooseBeanContributor[] contributors, int choice, boolean disableOthers){
		this(shell, ((FileEditorInput)ed.getEditorPart().getEditorInput()).getFile(),
	            JavaEditDomainHelper.getResourceSet(ed), 
	            contributors, choice, disableOthers);
	    editDomain = ed;
	}

	/**
	 * @param shell
	 * @param file
	 * @param resourceSet
	 * @param contributors
	 * @param choice
	 * @param disableOthers
	 * 
	 * @since 1.1
	 */
	protected ChooseBeanDialog(Shell shell, IFile file, ResourceSet resourceSet, IChooseBeanContributor[] contributors, int choice, boolean disableOthers){
		this(shell, (IPackageFragment) JavaCore.create(file).getParent(), contributors, choice, disableOthers);
		this.resourceSet = resourceSet;
	}

	/**
	 * @param shell
	 * @param packageFragment
	 * @param contributors    If null, list of contributors is determined for passed in project
	 * @param choice
	 * @param disableOthers
	 * 
	 * @since 1.1
	 */
	public ChooseBeanDialog(Shell shell, IPackageFragment packageFragment, IChooseBeanContributor[] contributors, int choice, boolean disableOthers){
		this(shell, 
				false,
				SearchEngine.createJavaSearchScope(new IJavaElement[]{packageFragment.getJavaProject()}),
				IJavaSearchConstants.CLASS
				);
		
		this.selectedContributor = choice;
		this.pkg = packageFragment;
		this.project = packageFragment.getJavaProject();
		this.disableOthers = disableOthers;
		this.contributors = contributors != null ? contributors : ChooseBeanDialogUtilities.determineContributors(project);
		if(contributors==null || contributors.length < 1)
			selectedContributor = -1;
		else if(!isValidContributor())
			selectedContributor = 0;
	}
	
	protected Control createDialogArea(Composite parent) {
		// Type selection area
		Composite area = (Composite) super.createDialogArea(parent);
		
		// Styles group
		if(contributors!=null && contributors.length>0){
			Composite stylesComposite = new Composite(area, SWT.NONE);
			GridLayout stylesCompositeLayout = new GridLayout(contributors.length+1, false);
			stylesComposite.setLayout(stylesCompositeLayout);
			stylesComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			Label image = new Label(stylesComposite, SWT.NONE);
			image.setImage(JavaVEPlugin.getJavaBeanImage());
			
			Label sectionLabel = new Label(stylesComposite, SWT.NONE);
			GridData sectionLabelData = new GridData(GridData.FILL_HORIZONTAL);
			sectionLabelData.horizontalSpan=contributors.length;
			sectionLabel.setLayoutData(sectionLabelData);
			sectionLabel.setText("Styles: ");
			
			Label spacer = new Label(stylesComposite, SWT.NONE);
			spacer.setText(""); // to remove NO READ warning
			
			contributorStyleButtons = new Button[contributors.length];
			for (int i = 0; i < contributors.length; i++) {
				contributorStyleButtons[i] = new Button(stylesComposite, SWT.RADIO);
				contributorStyleButtons[i].setText(contributors[i].getName());
				contributorStyleButtons[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}
		}
		
		// Variable name section
		Composite beanNameComposite = new Composite(area, SWT.NONE);
		beanNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		beanNameComposite.setLayout(new GridLayout(2, false));
		Label image = new Label(beanNameComposite, SWT.NONE);
		image.setImage(JavaPlugin.getDefault().getImageRegistry().get(JavaPluginImages.IMG_FIELD_PUBLIC));
		Label beanNameLabel = new Label(beanNameComposite, SWT.NONE);
		beanNameLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		beanNameLabel.setText("Bean name");
		Label spacer = new Label(beanNameComposite, SWT.NONE);
		spacer.setText(""); // to remove NO READ warning
		beanNameText = new Text(beanNameComposite, SWT.BORDER|SWT.BORDER);
		beanNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// finished
        applyDialogFont(area);
		return area;
	}
	
	public Object[] getResult() {
		Object[] results = super.getResult();
		if(resourceSet!=null){
			Object[] newResults = new Object[results.length*2];
			for(int i=0;i<results.length;i++){
				if (results[i] instanceof IType) {
					IType type = (IType) results[i];
					String realFQN = type.getFullyQualifiedName('$');
					// If there is a prototype factory use this to create the default instance
					PrototypeFactory prototypeFactory = null;
					EClass eClass = Utilities.getJavaClass(realFQN, resourceSet);					
					try {
						
						ClassDescriptorDecorator decorator =
							(ClassDescriptorDecorator) ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(
									eClass,
									ClassDescriptorDecorator.class,
									PrototypeFactory.PROTOTYPE_FACTORY_KEY);
						if(decorator != null){
							String prototypeFactoryName = (String)decorator.getKeyedValues().get(PrototypeFactory.PROTOTYPE_FACTORY_KEY);
							prototypeFactory = (PrototypeFactory) CDEPlugin.createInstance(null,prototypeFactoryName);
						}
					} catch (Exception e) {
						JavaVEPlugin.getPlugin().getLogger().log(Level.WARNING,e);
					}
					EObject eObject = null;
					if(prototypeFactory == null){
						eObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
					} else {
						eObject = prototypeFactory.createPrototype(eClass);
					}
					ChooseBeanDialogUtilities.setBeanName(eObject, beanName, editDomain);
					newResults[(i*2)] = eObject;
					newResults[(i*2)+1] = eClass;
				}
			}
			return newResults;
		}else{
			return results;
		}
	}
	
	protected boolean isValidContributor(){
		return selectedContributor > -1 && selectedContributor < contributors.length ;
	}

	public IStatus validate(Object[] selection) {
		IStatus validate = null;
		if(selection!=null && selection.length>0){
			validate = ChooseBeanDialogUtilities.getClassStatus(selection[0], pkg.getElementName(), resourceSet, javaSearchScope);
			if(validate.getSeverity()==IStatus.OK){
				if (selection[0] instanceof TypeInfo) {
					TypeInfo ti = (TypeInfo) selection[0];
					String name = ChooseBeanDialogUtilities.getFieldProposal(ti.getFullyQualifiedName(), editDomain, resourceSet);
					beanNameText.setText(name==null?new String():name);
				}
			}
		}else{
			beanNameText.setText(new String());
		}
		beanName = beanNameText.getText();
		return validate;
	}

}
