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
 *  $RCSfile: ChooseBeanDialog.java,v $
 *  $Revision: 1.41 $  $Date: 2005-10-20 21:04:09 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.corext.util.*;
import org.eclipse.jdt.internal.ui.JavaUIMessages;
import org.eclipse.jdt.internal.ui.util.*;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.vce.VCEPreferences;
 
/**
 * ChooseBeanDioalog - for selecting existing beans.
 * 
 * @since 1.1
 */
public class ChooseBeanDialog extends SelectionStatusDialog implements SelectionListener {

	// Constructor  input
	private ResourceSet resourceSet;
	private IJavaProject project;
	private IPackageFragment pkg;
	private List unmodifieableContributors = null;
	private int selectedContributor = -1;
	private boolean disableOthers = false;
	private EditDomain editDomain = null ;
	private IJavaSearchScope javaSearchScope = null;

	// UI components
	Button[] contributorStyleButtons = null;
	String beanName = null; // The below text gets disposed when OK is pressed - keep the text in string
	Text beanNameText = null;
	BeanViewer bv = null;
	private static boolean fgFirstTime= true; 
	
	// Status maintainers
	protected String filterString = null;
	private CLabel pkgName;
	private TypeInfoLabelProvider pkgLabelProvider = new TypeInfoLabelProvider(
			TypeInfoLabelProvider.SHOW_TYPE_CONTAINER_ONLY + TypeInfoLabelProvider.SHOW_ROOT_POSTFIX);
	
	protected ChooseBeanDialog(Shell parent, boolean multi, IJavaSearchScope scope) {
		super(parent);//, multi, PlatformUI.getWorkbench().getProgressService(), scope, elementKinds);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		setTitle(ChooseBeanMessages.MainDialog_title); 
		setMessage(ChooseBeanMessages.MainDialog_message); 
		setStatusLineAboveButtons(true);
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
	protected ChooseBeanDialog(Shell shell, IPackageFragment packageFragment, IChooseBeanContributor[] contributors, int choice, boolean disableOthers){
		this(shell, false,
				SearchEngine.createJavaSearchScope(new IJavaElement[]{packageFragment.getJavaProject()}));
		
		this.selectedContributor = choice;
		this.pkg = packageFragment;
		this.project = packageFragment.getJavaProject();
		this.disableOthers = disableOthers;
		this.unmodifieableContributors = contributors != null ? Arrays.asList(contributors) : Arrays.asList(ChooseBeanDialogUtilities.determineContributors(project));
		if(unmodifieableContributors==null || unmodifieableContributors.isEmpty())
			selectedContributor = -1;
		else if(!isValidContributor())
			selectedContributor = 0;
	}
	
	protected Control createDialogArea(Composite parent) {
		// Type selection area
		Composite top = (Composite) super.createDialogArea(parent);

		Composite area = new Composite(top, SWT.NONE);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		area.setLayout(new GridLayout(2, false));
				
		Label label = new Label(area, SWT.NONE);
		label.setText(JavaUIMessages.OpenTypeAction_dialogMessage);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		
		Text filterText = new Text(area, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		filterText.setLayoutData(gd);
		filterText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				bv.setSearchPattern(((Text)e.getSource()).getText());
			}
		});
		filterText.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					bv.setFocus();
				}
			}
		});
	
		label = new Label(area, SWT.NONE);
		label.setText(JavaUIMessages.TypeSelectionComponent_label);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label progressLabel = new Label(area, SWT.NONE);
		progressLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		progressLabel.setAlignment(SWT.RIGHT);
		
		bv = new BeanViewer(area, progressLabel, javaSearchScope, pkg, resourceSet, editDomain); 
		gd = new GridData(GridData.FILL_BOTH);
		PixelConverter converter= new PixelConverter(bv.getTable());
		gd.widthHint= converter.convertWidthInCharsToPixels(70);
		gd.heightHint= SWTUtil.getTableHeightHint(bv.getTable(), 10);
		gd.horizontalSpan=2;
		bv.getTable().setLayoutData(gd);
		bv.getTable().addSelectionListener(this);
		
		ViewForm pkgViewForm = new ViewForm(area, SWT.BORDER|SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		pkgViewForm.setLayoutData(gd);
		pkgName = new CLabel(pkgViewForm, SWT.NONE);
		pkgViewForm.setContent(pkgName);
		
		// Styles group
		if(unmodifieableContributors.size()>0){
			int numColumns = (unmodifieableContributors.size()*2) + 1;
			Composite stylesComposite = new Composite(area, SWT.NONE);
			GridLayout stylesCompositeLayout = new GridLayout(numColumns, false);
			stylesComposite.setLayout(stylesCompositeLayout);
			gd = new GridData(GridData.FILL_HORIZONTAL);
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
				Image image = ChooseBeanDialogUtilities.getContributorImage(contrib);
				if(image!=null)
					contribImage.setImage(image);
				
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
				if(disableOthers && i!=selectedContributor)
					contributorStyleButtons[i].setEnabled(false);
			}
			if(contributorStyleButtons.length>0){
				if(selectedContributor>contributorStyleButtons.length-1 || selectedContributor<0)
					selectedContributor = 0;
				contributorStyleButtons[selectedContributor].setSelection(true);
				selectContributor((IChooseBeanContributor) unmodifieableContributors.get(selectedContributor));
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
					Button b = (Button) e.getSource();
					bv.showOnlyBeans(b.getSelection());
					if(selectedContributor>-1)
						bv.updateContributor((IChooseBeanContributor) unmodifieableContributors.get(selectedContributor));
				}
			});
		}
		
		
		// Variable name section
		if(!VCEPreferences.askForRename()){
			// Bean name dialog will not be used - rename here
			
			Composite beanNameComposite = new Composite(area, SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
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
					updateStatus();
		}
			});
		}
		
		// finished
		if(filterString!=null){
			filterText.setText(filterString);
		}
        applyDialogFont(top);
		return top;
	}
	
	protected void selectContributor(IChooseBeanContributor contrib) {
		selectedContributor = unmodifieableContributors.indexOf(contrib);
		if(unmodifieableContributors.contains(contrib))
			bv.updateContributor(contrib);
	}

	public Object[] getResult() {
		Object[] results = super.getResult();
		if(resourceSet!=null){
			Object[] newResults = new Object[results.length*2];
			for(int i=0;i<results.length;i++){
				if (results[i] instanceof IType) {
					IType type = (IType) results[i];
					String realFQN = type.getFullyQualifiedName('$');
					JavaClass javaClass = Utilities.getJavaClass(realFQN, resourceSet);					
					EObject eObject = javaClass.getEPackage().getEFactoryInstance().create(javaClass);
					if(beanName==null || beanName.trim().length()<1){
						beanName = getDefaultBeanName(((IJavaObjectInstance) eObject).getJavaType().getJavaName());
					}
					ChooseBeanDialogUtilities.setBeanName(eObject, beanName, editDomain);
					newResults[(i*2)] = eObject;
					newResults[(i*2)+1] = javaClass;
				}
			}
			return newResults;
		}else{
			return results;
		}
	}
	
	protected String getDefaultBeanName(String qualTypeName) {
		String defaultName = qualTypeName;
		if (defaultName.indexOf('.') > 0)
			defaultName = defaultName.substring(defaultName.lastIndexOf('.') + 1);
		defaultName = CDEUtilities.lowCaseFirstCharacter(defaultName);
		return defaultName;
	}
	
	protected boolean isValidContributor(){
		return selectedContributor > -1 && selectedContributor < unmodifieableContributors.size() ;
	}

//	public IStatus validate(Object[] selection) {
//		IStatus validate = null;
//		beanName = "";
//		if(selection!=null && selection.length>0){
//			validate = ChooseBeanDialogUtilities.getClassStatus(selection[0], pkg.getElementName(), resourceSet, javaSearchScope);
//			if(validate.getSeverity()==IStatus.OK){
//				if (selection[0] instanceof TypeInfo) {
//					TypeInfo ti = (TypeInfo) selection[0];
//					beanName = ChooseBeanDialogUtilities.getFieldProposal(ti.getFullyQualifiedName(), editDomain, resourceSet);
//					if(beanNameText!=null && !beanNameText.isDisposed())
//						beanNameText.setText(beanName==null?new String():beanName);
//				}
//			}
//		}
//		return validate;
//	}

	/*
	 * @deprecated
	 * Got over from TypeSelectionDialog2.computeResult(). Should not need to do this
	 * once we have extension mechanism to the type selection dialog via bug 93162.
	 */
	protected void computeResult() {
		TypeInfo[] selected= bv.getSelection();
		if (selected == null || selected.length == 0) {
			setResult(null);
			return;
		}
		
		TypeInfoHistory history= TypeInfoHistory.getInstance();
		List result= new ArrayList(selected.length);
		if (result != null) {
			for (int i= 0; i < selected.length; i++) {
				try {
					TypeInfo typeInfo= selected[i];
					history.accessed(typeInfo);
					IType type= typeInfo.resolveType(javaSearchScope);
					if (type == null) {
						String title= JavaUIMessages.TypeSelectionDialog_errorTitle; 
						String message= Messages.format(JavaUIMessages.TypeSelectionDialog_dialogMessage, typeInfo.getPath()); 
						MessageDialog.openError(getShell(), title, message);
						setResult(null);
					} else {
						result.add(type);
					}
				} catch (JavaModelException e) {
					String title= JavaUIMessages.MultiTypeSelectionDialog_errorTitle; 
					String message= JavaUIMessages.MultiTypeSelectionDialog_errorMessage; 
					ErrorDialog.openError(getShell(), title, message, e.getStatus());
				}
			}
		}
		setResult(result);
	}

	/*
	 * @deprecated
	 * Got over from TypeSelectionDialog2.open(). Should not need to do this
	 * once we have extension mechanism to the type selection dialog via bug 93162.
	 */
	public int open() {
		try {
			ensureConsistency();
		} catch (InvocationTargetException e) {
			ExceptionHandler.handle(e, JavaUIMessages.TypeSelectionDialog_error3Title, JavaUIMessages.TypeSelectionDialog_error3Message); 
			return CANCEL;
		} catch (InterruptedException e) {
			// cancelled by user
			return CANCEL;
		}
		return super.open();
	}

	/*
	 * @deprecated
	 * Got over from TypeSelectionDialog2.close(). Should not need to do this
	 * once we have extension mechanism to the type selection dialog via bug 93162.
	 */
	public boolean close() {
		TypeInfoHistory.getInstance().save();
		return super.close();
	}
	
	/*
	 * @deprecated
	 * Got over from TypeSelectionDialog2.ensureConsistency(). Should not need to do this
	 * once we have extension mechanism to the type selection dialog via bug 93162.
	 */
	private void ensureConsistency() throws InvocationTargetException, InterruptedException {
		// we only have to ensure histroy consistency here since the search engine
		// takes care of working copies.
		IRunnableWithProgress runnable= new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				TypeInfoHistory history= TypeInfoHistory.getInstance();
				if (fgFirstTime || history.isEmpty()) {
					monitor.beginTask(JavaUIMessages.TypeSelectionDialog_progress_consistency, 100);
					refreshSearchIndices(new SubProgressMonitor(monitor, 90));
					history.checkConsistency(new SubProgressMonitor(monitor, 10));
					monitor.done();
					fgFirstTime= false;
				} else {
					history.checkConsistency(monitor);
				}
			}
			private void refreshSearchIndices(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					new SearchEngine().searchAllTypeNames(
						null, 
						// make sure we search a concrete name. This is faster according to Kent  
						"_______________".toCharArray(), //$NON-NLS-1$
						SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE, 
						IJavaSearchConstants.ENUM,
						SearchEngine.createWorkspaceScope(), 
						new TypeNameRequestor() {}, 
						IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, 
						monitor);
				} catch (JavaModelException e) {
					throw new InvocationTargetException(e);
				}
			}
		};
		IRunnableContext context=PlatformUI.getWorkbench().getProgressService();
		context.run(true, true, runnable);
	}

	public void setFilter(String string) {
		filterString = string;
	}
	
	protected void typeSelectionChange(TypeInfo selectedType){
		if(selectedType==null){
			pkgName.setImage(null);
			pkgName.setText("");
			beanName = "";
		}else{
			pkgName.setImage(pkgLabelProvider.getImage(selectedType));
			pkgName.setText(pkgLabelProvider.getText(selectedType));
			beanName = getDefaultBeanName(selectedType.getTypeName());
		}
		if(beanNameText!=null && !beanNameText.isDisposed()){
			beanNameText.setText(beanName);
		}
		updateStatus();
	}

	protected void updateStatus(){
		TypeInfo[] typeSelection = bv.getSelection();
		TypeInfo selected = (typeSelection==null || typeSelection.length<1) ? null : typeSelection[0];
		updateStatus(ChooseBeanDialogUtilities.getClassStatus(selected, pkg.getElementName(), resourceSet, javaSearchScope, beanName, editDomain));
	}

	public void widgetSelected(SelectionEvent e) {
		TypeInfo[] typeSelection = bv.getSelection();
		TypeInfo selected = (typeSelection==null || typeSelection.length<1) ? null : typeSelection[0];
		typeSelectionChange(selected);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
}
