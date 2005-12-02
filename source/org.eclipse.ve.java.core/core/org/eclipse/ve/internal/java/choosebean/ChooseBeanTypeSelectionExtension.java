/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChooseBeanTypeSelectionExtension.java,v $
 *  $Revision: 1.2 $  $Date: 2005-12-02 20:22:22 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.util.*;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.dialogs.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.vce.VCEPreferences;
 
public class ChooseBeanTypeSelectionExtension extends TypeSelectionExtension{
	
	public class ChooseBeanDynamicProvider implements ITypeInfoFilterExtension, ITypeInfoImageProvider{
		private HashMap selectedContributorToFilterMap = new HashMap();
		private HashMap selectedContributorToImageMap = new HashMap();
		
		private ITypeInfoFilterExtension currentContributorFilter = null;
		private ImageDescriptor currentContributorImage = null;
		
		public boolean select(ITypeInfoRequestor typeInfoRequestor) {
			if(currentContributorFilter==null){
				if(selectedContributor!=null){
					if(!selectedContributorToFilterMap.containsKey(selectedContributor)){
						ITypeInfoFilterExtension filter =selectedContributor.getFilter(packageFragment, new NullProgressMonitor());
						selectedContributorToFilterMap.put(selectedContributor, filter);
					}
					currentContributorFilter = (ITypeInfoFilterExtension) selectedContributorToFilterMap.get(selectedContributor);
				}
			}
			if(currentContributorFilter!=null)
				return currentContributorFilter.select(typeInfoRequestor);
			return true;
		}
		
		public ImageDescriptor getImageDescriptor(ITypeInfoRequestor typeInfoRequestor) {
			if(currentContributorImage==null){
				if(selectedContributor!=null){
					if(!selectedContributorToImageMap.containsKey(selectedContributor)){
						ImageDescriptor imageDescriptor = selectedContributor.getImage();
						selectedContributorToImageMap.put(selectedContributor, imageDescriptor);
					}
					currentContributorImage = (ImageDescriptor) selectedContributorToImageMap.get(selectedContributor);
				}
			}
			return currentContributorImage;
		}
		
		public void clear(){
			currentContributorFilter = null;
			currentContributorImage = null;
		}
	}
	
	private IPackageFragment packageFragment = null;
	private IJavaProject javaProject = null;
	private ResourceSet resourceSet = null;
	private EditDomain editDomain = null;
	private IJavaSearchScope searchScope = null;
	
	private List unmodifieableContributors = null;
	private IChooseBeanContributor selectedContributor = null;
	
	private Button[] contributorStyleButtons = null;
	private String beanName = null; // The below text gets disposed when OK is pressed - keep the text in string
	private Text beanNameText = null;
	
	private ChooseBeanDynamicProvider dynamicProvider = null;
	
	public ChooseBeanTypeSelectionExtension(IChooseBeanContributor[] contributors, IPackageFragment packageFragment, EditDomain editDomain, IJavaSearchScope searchScope){
		this.packageFragment = packageFragment;
		this.javaProject = packageFragment.getJavaProject();
		this.editDomain = editDomain;
		this.resourceSet = JavaEditDomainHelper.getResourceSet(editDomain);
		this.searchScope = searchScope;
		this.unmodifieableContributors = contributors != null ? Arrays.asList(contributors) : Arrays.asList(ChooseBeanDialogUtilities.determineContributors(javaProject));
		if(unmodifieableContributors.size()>0)
			selectContributor((IChooseBeanContributor) unmodifieableContributors.get(0));
	}
	
	public Control createContentArea(Composite parent) {

		// Type selection area
		Composite area = new Composite(parent, SWT.NONE);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		area.setLayout(new GridLayout(2, false));
				
		// Styles group
		if(unmodifieableContributors.size()>0){
			int numColumns = (unmodifieableContributors.size()*2) + 1;
			Composite stylesComposite = new Composite(area, SWT.NONE);
			GridLayout stylesCompositeLayout = new GridLayout(numColumns, false);
			stylesComposite.setLayout(stylesCompositeLayout);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			stylesComposite.setLayoutData(gd);

			Label sectionLabel = new Label(stylesComposite, SWT.NONE);
			GridData sectionLabelData = new GridData(GridData.FILL_HORIZONTAL);
			sectionLabelData.horizontalSpan=numColumns;
			sectionLabel.setLayoutData(sectionLabelData);
			sectionLabel.setText(ChooseBeanMessages.ChooseBeanDialog_Section_Styles); 
			
			Label spacer = new Label(stylesComposite, SWT.NONE);
			spacer.setText(""); // to remove NO READ warning //$NON-NLS-1$
			
			final String contributorKey = "contributor"; //$NON-NLS-1$
			contributorStyleButtons = new Button[unmodifieableContributors.size()];
			for (int i = 0; i < unmodifieableContributors.size(); i++) {
				IChooseBeanContributor contrib = (IChooseBeanContributor) unmodifieableContributors.get(i);
				Label contribImage = new Label(stylesComposite, SWT.NONE);
				ImageDescriptor imageDescriptor = contrib.getImage();
				final Image image = imageDescriptor==null ? null : imageDescriptor.createImage();
				if(image!=null){
					contribImage.setImage(image);
					contribImage.addDisposeListener(new DisposeListener(){
						public void widgetDisposed(DisposeEvent e) {
							image.dispose();
						}
					});
				}
				
				contributorStyleButtons[i] = new Button(stylesComposite, SWT.RADIO);
				contributorStyleButtons[i].setText(contrib.getName());
				contributorStyleButtons[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				contributorStyleButtons[i].setData(contributorKey, contrib);
				contributorStyleButtons[i].addSelectionListener(new SelectionListener(){
					public void widgetSelected(SelectionEvent e) {
						Button b = ((Button)e.getSource());
						if(b.getSelection()){
							IChooseBeanContributor contrib = (IChooseBeanContributor) b.getData(contributorKey);
							selectContributor(contrib);
						}
					}
					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}
				});
			}
			if(contributorStyleButtons.length>0 && selectedContributor!=null){
				int index = unmodifieableContributors.indexOf(selectedContributor);
				contributorStyleButtons[index].setSelection(true);
			}
			
			// Show only beans section
			spacer = new Label(stylesComposite, SWT.NONE);
			spacer.setLayoutData(new GridData());
			spacer.setText(""); //$NON-NLS-1$
			
			Button showBeansButton = new Button(stylesComposite, SWT.CHECK);
			showBeansButton.setText(ChooseBeanMessages.ChooseBeanDialog_Checkbox_ShowValidClasses); 
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = numColumns-1;
			showBeansButton.setLayoutData(gd);
			showBeansButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					getTypeSelectionComponent().triggerSearch();
				}
			});
		}
		
		
		// Variable name section
		if(!VCEPreferences.askForRename()){
			// Bean name dialog will not be used - rename here
			
			Composite beanNameComposite = new Composite(area, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			beanNameComposite.setLayoutData(gd);
			beanNameComposite.setLayout(new GridLayout(2, false));
			Label image = new Label(beanNameComposite, SWT.NONE);
			image.setImage(JavaUI.getSharedImages().getImage(ISharedImages.IMG_FIELD_PUBLIC));
			Label beanNameLabel = new Label(beanNameComposite, SWT.NONE);
			beanNameLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			beanNameLabel.setText(ChooseBeanMessages.ChooseBeanDialog_Label_BeanName); 
			Label spacer = new Label(beanNameComposite, SWT.NONE);
			spacer.setText(""); // to remove NO READ warning //$NON-NLS-1$
		
			beanNameText = new Text(beanNameComposite, SWT.BORDER|SWT.BORDER);
			beanNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			beanNameText.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent e) {
					beanName = beanNameText.getText();
				}
			});
		}
		
		// finished
		return area;
	}
	
	protected ChooseBeanDynamicProvider getDynamicProvider(){
		if(dynamicProvider==null)
			dynamicProvider = new ChooseBeanDynamicProvider();
		return dynamicProvider;
	}

	public ITypeInfoFilterExtension getFilterExtension() {
		return getDynamicProvider();
	}
	
	public ITypeInfoImageProvider getImageProvider() {
		return getDynamicProvider();
	}

	public ISelectionStatusValidator getSelectionValidator() {
		return new ISelectionStatusValidator(){
			public IStatus validate(Object[] selection) {
				if(selection!=null && selection.length>0 && selection[0] instanceof IType){
					IType selectedIType = (IType) selection[0];
					setBeanName(ChooseBeanDialog.getDefaultBeanName(selectedIType.getFullyQualifiedName('.')));
					return ChooseBeanDialogUtilities.getClassStatus(
							selectedIType, 
							packageFragment.getElementName(), 
							resourceSet, 
							searchScope, 
							beanName, 
							editDomain);
					}
				return null;
			}
		};
	}
	
	public void dispose(){
	}

	protected void selectContributor(IChooseBeanContributor contrib) {
		getDynamicProvider().clear();
		selectedContributor = contrib;
		if(selectedContributor!=null && getTypeSelectionComponent()!=null)
			getTypeSelectionComponent().triggerSearch();
	}

	protected void setBeanName(String name){
		beanName = name;
		if(beanNameText!=null && !beanNameText.isDisposed())
			beanNameText.setText(name);
	}
	
	public String getBeanName() {
		return beanName;
	}
}
