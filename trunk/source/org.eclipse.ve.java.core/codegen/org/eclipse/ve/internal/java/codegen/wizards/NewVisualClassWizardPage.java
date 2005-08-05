package org.eclipse.ve.internal.java.codegen.wizards;

/*******************************************************************************
 * Copyright (c) 2004 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - 1.0 implementation
 *******************************************************************************/
/*
 *  $RCSfile: NewVisualClassWizardPage.java,v $
 *  $Revision: 1.23 $  $Date: 2005-08-05 19:47:09 $ 
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.*;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author JoeWin, pmuldoon
 */
public class NewVisualClassWizardPage extends NewClassWizardPage {

	TreeViewer styleTreeViewer;

	CategoryModel treeRoot;

	boolean isSettingSuperclass;

	private VisualElementModel selectedElement = null;

	private static final String DEFAULT_ELEMENT_KEY = "org.eclipse.ve.core.other-Object-java.lang.Object"; //$NON-NLS-1$

	private boolean useSuperClass;
	private boolean isSelectingTemplate = false;
	
	private IStatus fContributorStatus = StatusInfo.OK_STATUS;
	private IStatus fSourceFolderStatus = StatusInfo.OK_STATUS;

	public class TreePrioritySorter extends ViewerSorter {

		public int category(Object element) {
			if (element instanceof CategoryModel)
				return ((CategoryModel) element).getPriority();
			return 20000;
		}

	}

	/**
	 * Sets the expanded/collapased state of each node
	 */
	protected void setTreeState() {
		Object[] topLevel = treeRoot.getChildren();
		if (topLevel != null)
			for (int i = 0; i < topLevel.length; i++)
				if (topLevel[i] instanceof CategoryModel)
					styleTreeViewer.setExpandedState(topLevel[i], ((CategoryModel) topLevel[i]).getDefaultExpand());
	}

	protected void createTreeClassComposite(Composite composite, int nColumns) {

		Composite labelGroup = createComposite(composite, 5);
		((GridLayout) labelGroup.getLayout()).marginHeight = 0;
		GridData labelData = (GridData) labelGroup.getLayoutData();
		labelData.horizontalSpan = 5;
		createLabel(labelGroup, CodegenWizardsMessages.NewVisualClassWizardPage_Style_Label); 

		styleTreeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.BORDER);
		GridData treeGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		treeGridData.verticalSpan = 4;
		treeGridData.widthHint = convertWidthInCharsToPixels(23);
		treeGridData.heightHint = convertHeightInCharsToPixels(12);
		styleTreeViewer.getTree().setLayoutData(treeGridData);

		styleTreeViewer.setContentProvider(new StyleTreeContentProvider());
		styleTreeViewer.setInput(getInitalStyleInput());
		styleTreeViewer.setLabelProvider(new StyleTreeLabelProvider());
		styleTreeViewer.setSorter(new TreePrioritySorter());
		setTreeState();
		if (selectedElement != null)	// we may have no selected element initially
			styleTreeViewer.setSelection(new StructuredSelection(selectedElement), true);

		styleTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					selectedElement = null;
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					isSelectingTemplate = true;
					for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
						Object domain = iterator.next();
						if (domain instanceof VisualElementModel) {
							selectedElement = (VisualElementModel) domain;
							fContributorStatus = selectedElement.getStatus(getContainerRoot());
							setSuperClass(((VisualElementModel) domain).getSuperClass());							
							handleFieldChanged(null);
						} else {
							fContributorStatus = StatusInfo.OK_STATUS;
							handleFieldChanged(null);							
						}
					}
					isSelectingTemplate = false;
				}
			}
		});
	}
	
	
	
	protected IResource getContainerRoot(){ 
		return getWorkspaceRoot().findMember(getPackageFragmentRootText());
	}

	protected void createSuperClassControls(Composite composite, int nColumns) {

		createTreeClassComposite(composite, nColumns);
		Composite controlsComp = createComposite(composite, nColumns);
		((GridLayout) controlsComp.getLayout()).marginHeight = 0;
		GridData griddata = (GridData) controlsComp.getLayoutData();
		griddata.horizontalSpan = 3;
		griddata.horizontalIndent = 20;
		super.createSuperClassControls(controlsComp, nColumns);
		super.createSuperInterfacesControls(controlsComp, nColumns);
	}

	protected void createSuperInterfacesControls(Composite composite, int nColumns) {

	}

	/**
	 * Finds the parent of the contributed style. This method is used to pair up the visual elements with the style (through the id component
	 * 
	 * @param category -
	 *            the category this elements belongs too
	 * @param treeRoot -
	 *            the CategoryModel linked list to search
	 * @return returns the parenting CategoryModel
	 */
	private CategoryModel findParentStyle(String category, CategoryModel treeRoot) {
		Object[] iterator = treeRoot.getChildren();
		for (int i = 0; i < iterator.length; i++) {
			if (iterator[i] instanceof CategoryModel) {
				if (((CategoryModel) iterator[i]).getId().equalsIgnoreCase(category))
					return (CategoryModel) iterator[i];
			}
		}
		return null;
	}

	/**
	 * Populates the TreeView with the newStyleComponent contributions from the plugin extensions Also sets the selected VisualElementModel if it was
	 * previously selected.
	 * 
	 * @return
	 */
	private CategoryModel getInitalStyleInput() {
		/*
		 * Choosing element model to be selected in the tree viewer is determined by either the super class name passed in from
		 * wizard or from a previous selection when this page was used earlier which is stored in the JavaVEPlugin preferences.
		 */
		String[] previousSelectedElementData = null;
		if (!useSuperClass) {
			Preferences preferences = JavaVEPlugin.getPlugin().getPluginPreferences();
			if (!preferences.getDefaultString(NewVisualClassCreationWizard.VISUAL_CLASS_WIZARD_SELECTED_ELEMENT_KEY).equals(DEFAULT_ELEMENT_KEY))
				preferences.setDefault(NewVisualClassCreationWizard.VISUAL_CLASS_WIZARD_SELECTED_ELEMENT_KEY, DEFAULT_ELEMENT_KEY);
			String previousSelectedElement = preferences.getString(NewVisualClassCreationWizard.VISUAL_CLASS_WIZARD_SELECTED_ELEMENT_KEY);
			// Selected element has 3 parts separated by "-". 1-Category, 2-Name, 3-Superclass
			previousSelectedElementData = previousSelectedElement.split("-"); //$NON-NLS-1$
		}
		
		// Get all the categories and visual elements from the contributors to the "newStyleComponet" extension and store under one root 
		treeRoot = new CategoryModel();
		IExtensionPoint exp = Platform.getExtensionRegistry().getExtensionPoint(JavaVEPlugin.getPlugin().getBundle().getSymbolicName(),
			"newStyleComponent"); //$NON-NLS-1$
		IExtension[] extensions = exp.getExtensions();
		CategoryModel parentStyle = null;
		if (extensions != null && extensions.length > 0) {
			for (int ec = 0; ec < extensions.length; ec++) {
				IConfigurationElement[] configElms = extensions[ec].getConfigurationElements();
				for (int cc = 0; cc < configElms.length; cc++) {
					IConfigurationElement celm = configElms[cc];
					if (celm.getName().equalsIgnoreCase("Category")) { //$NON-NLS-1$
						treeRoot.addStyle(new CategoryModel(celm));
					}
					if (celm.getName().equalsIgnoreCase("visualElement")) { //$NON-NLS-1$
						parentStyle = findParentStyle(celm.getAttribute("category"), treeRoot); //$NON-NLS-1$
						VisualElementModel vem = new VisualElementModel(celm);
						if (parentStyle != null)
							parentStyle.addVisualElement(vem);
						else
							treeRoot.addVisualElement(vem);
						// If this visual element contains the superclass passed into this wizard or was the previous selection,
						// select it so it's highlighted in the tree viewer.
						if (useSuperClass) {
							if (vem.getSuperClass().equals(getSuperClass())) {
								// There is an Eclipse policy where wizards cannot open with errors
								if(vem.getStatus(getContainerRoot()).isOK()){
									selectedElement = vem;
								}
							}
						} else if (vem.getCategory().equals(previousSelectedElementData[0]) && vem.getName().equals(previousSelectedElementData[1])) {
							if(vem.getStatus(getContainerRoot()).isOK()){							
								selectedElement = vem;
								setSuperClass(previousSelectedElementData[2], true);
							}
						}
					}

				}
			}
		}

		return treeRoot;
	}
	
	protected void updateStatus(IStatus[] statusArg) {
		// The array of status objects is the ones that the superclass provides
		IStatus[] status = new IStatus[statusArg.length + 2];
		System.arraycopy(statusArg,0,status,1,statusArg.length);
		status[status.length - 1] = fSourceFolderStatus;
		status[0] = fContributorStatus;  //move our messages to the front
		super.updateStatus(StatusUtil.getMostSevere(status));
	}	

	public void setSuperClass(String name) {
		useSuperClass = true;
		super.setSuperClass(name, true);
	}
	
	protected SelectionButtonDialogFieldGroup fMethodStubsButtonsField;

	protected SelectionButtonDialogFieldGroup getMethodStubsButtonsField() {
		if (fMethodStubsButtonsField == null) {
			// This field is private and inherited so we have to retrieve it using reflection
			try {
				Class newClassWizardPage = Class.forName("org.eclipse.jdt.ui.wizards.NewClassWizardPage"); //$NON-NLS-1$			
				java.lang.reflect.Field field = newClassWizardPage.getDeclaredField("fMethodStubsButtons"); //$NON-NLS-1$
				field.setAccessible(true);
				fMethodStubsButtonsField = (SelectionButtonDialogFieldGroup) field.get(this);
			} catch (Exception exc) {
				JavaVEPlugin.log(exc, Level.FINEST);
			}
		}
		return fMethodStubsButtonsField;
	}

	protected StringButtonDialogField localSuperclassButtonDialogField;

	protected StringButtonDialogField getSuperclassButtonDialogField() {
		if (localSuperclassButtonDialogField == null) {
			// This field is private and inherited so we have to retrieve it using reflection
			try {
				Class newClassWizardPage = Class.forName("org.eclipse.jdt.ui.wizards.NewTypeWizardPage"); //$NON-NLS-1$			
				java.lang.reflect.Field field = newClassWizardPage.getDeclaredField("fSuperClassDialogField"); //$NON-NLS-1$
				field.setAccessible(true);
				localSuperclassButtonDialogField = (StringButtonDialogField) field.get(this);
			} catch (Exception exc) {
				JavaVEPlugin.log(exc, Level.FINEST);
			}
		}
		return localSuperclassButtonDialogField;
	}

	// When the container changes we must re-check whether or not this is a valid project for the template to be created into
	protected void handleFieldChanged(String fieldName) {
		if (fieldName == CONTAINER){
			fSourceFolderStatus = StatusInfo.OK_STATUS;
			try{
				// Check the status of whether or not we are in a proper source folder
				if(getContainerRoot() != null){
					IProject project = getContainerRoot().getProject();
					if(project != null){
						JavaProject javaProject = (JavaProject) project.getNature(JavaCore.NATURE_ID);
						if(javaProject != null){
							if(ProxyPlugin.isPDEProject(javaProject)){
								if (!javaProject.isOnClasspath(getPackageFragmentRoot())){							
									fSourceFolderStatus = new StatusInfo(
										IStatus.ERROR,
										"", 
										JavaVEPlugin.PLUGIN_ID);
								}
								else {
									// Must include the ui plugin									
									HashMap plugins = new HashMap();
									ProxyPlugin.getPlugin().getIDsFound(javaProject, new HashMap(), new HashMap(), plugins, new HashMap());
									boolean uiIncluded = plugins.get("org.eclipse.ui")!=null;//$NON-NLS-1$
									if (!uiIncluded)
										fSourceFolderStatus = new StatusInfo(
												IStatus.ERROR,
												"", 
												JavaVEPlugin.PLUGIN_ID);
								}
							}
						}
					}
				} 
			} catch (CoreException e){
				JavaVEPlugin.log(e, Level.FINEST);				
			}
			// 	Check the status of the contributor as the project may have changed
			if(selectedElement != null){
				fContributorStatus = selectedElement.getStatus(getContainerRoot());				
			}
		} else if (fieldName == SUPER){
			// If the user is changing the superclass by hand then we must blank out the template otherwise we get errors
			// with using the wrong template for the superclass
			if(!isSelectingTemplate && styleTreeViewer != null){
				handleSuperclassChanged();
			}			
		} else if (fieldName == ENCLOSINGSELECTION){
			String ERROR_MESSAGE = CodegenWizardsMessages.NewVisualClassWizardPage_EnclosedType_ERROR_; 
			if(getEnclosingType()!=null){
				fContributorStatus = new Status(IStatus.ERROR, JavaVEPlugin.PLUGIN_ID, IStatus.ERROR, ERROR_MESSAGE , null);
			}else if(fContributorStatus!=null && fContributorStatus.getSeverity()==IStatus.ERROR && fContributorStatus.getMessage().equals(ERROR_MESSAGE)){
				fContributorStatus = StatusInfo.OK_STATUS;
			}
		}
		super.handleFieldChanged(fieldName);
	}

	private void handleSuperclassChanged() {
		
		boolean superCheckNeeded = true;
		if(selectedElement!=null){
			String selectedSuperString = selectedElement.getSuperClass();
			String textSuperString = getSuperClass();
			if(textSuperString!=null)
				superCheckNeeded = !textSuperString.equals(selectedSuperString);
		}
		
		if(superCheckNeeded){
			boolean setProperly = false;
			if(treeRoot!=null){
				String textSuperString = getSuperClass();
				Object[] styles = treeRoot.getStyles();
				if(styles!=null){
					VisualElementModel toSelectVisualElement = null;
					for (int stylesCount = 0; stylesCount < styles.length; stylesCount++) {
						Object[] children = ((CategoryModel)styles[stylesCount]).getChildren();
						if(children!=null){
							for (int childCount = 0; childCount < children.length; childCount++) {
								VisualElementModel childElementModel = (VisualElementModel) children[childCount];
								if(textSuperString.equals(childElementModel.getSuperClass())){
									toSelectVisualElement = childElementModel;
									break;
								}
							}
						}
						if(toSelectVisualElement!=null)
							break;
					}
					
					if(toSelectVisualElement!=null){
						setProperly = true;
						styleTreeViewer.setSelection(new StructuredSelection(toSelectVisualElement), true);
					}
				}
			}
			
			if(!setProperly)
				styleTreeViewer.setSelection(null);
		}
	}

	protected Composite createComposite(Composite aParent, int numColumns) {
		Composite group = new Composite(aParent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		gridLayout.marginWidth = 1;
		group.setLayout(gridLayout);
		GridData data = new GridData();
		//		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		return group;
	}

	protected Group createGroup(Composite aParent, String title, int numColumns) {
		Group group = new Group(aParent, SWT.NONE);
		if (title != null) {
			group.setText(title);
		}
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		group.setLayout(gridLayout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		return group;
	}

	protected void createLabel(Composite parent, String text) {
		Label spacer = new Label(parent, SWT.NONE);
		if (text != null) {
			spacer.setText(text);
		}
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		spacer.setLayoutData(data);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 * 
	 * Overridden here because we have to get the the private field NewClassWizard.fMethodStubsButton in order to reset its layout data so that the
	 * method stubs selections will fit next to the Style tree and not below it.
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		createMethodStubSelectionControls(parent, 2);
	}

	private void createMethodStubSelectionControls(Composite composite, int nColumns) {
		SelectionButtonDialogFieldGroup fg = getMethodStubsButtonsField();
		if (fg == null)
			return;
		Control labelControl = fg.getLabelControl(composite);
		LayoutUtil.setHorizontalSpan(labelControl, nColumns);
		LayoutUtil.setHorizontalIndent(labelControl, 20);

		Control buttonGroup = fg.getSelectionButtonsGroup(composite);
		LayoutUtil.setHorizontalSpan(buttonGroup, nColumns);
		LayoutUtil.setHorizontalIndent(buttonGroup, 60);
	}

	/**
	 * Constructs the argument matrix HashMap from the method controls argument
	 * 
	 * @return argument hashmap
	 */
	public HashMap getArgumentMatrix() {
		HashMap argumentMatrix = new HashMap();
		argumentMatrix.put(IVisualClassCreationSourceGenerator.CREATE_MAIN, isCreateMain() == true ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
		argumentMatrix.put(IVisualClassCreationSourceGenerator.CREATE_SUPER_CONSTRUCTORS, isCreateConstructors() == true ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
		argumentMatrix.put(IVisualClassCreationSourceGenerator.CREATE_INHERITED_ABSTRACT, isCreateInherited() == true ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
		argumentMatrix.put(IVisualClassCreationSourceGenerator.TARGET_PACKAGE_NAME, getPackageText());
		return argumentMatrix;
	}

	/**
	 * @return Returns the VisualEelementModel of the tree selection
	 */
	public VisualElementModel getSelectedElement() {
		return selectedElement;
	}
}