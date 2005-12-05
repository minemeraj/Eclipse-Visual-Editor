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
 *  $Revision: 1.27 $  $Date: 2005-12-05 20:55:12 $ 
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin.FoundIDs;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author JoeWin, pmuldoon
 */
public class NewVisualClassWizardPage extends NewTypeWizardPage {

	TreeViewer styleTreeViewer;

	CategoryModel treeRoot;

	boolean isSettingSuperclass;

	private VisualElementModel selectedElement = null;

	private static final String DEFAULT_ELEMENT_KEY = "org.eclipse.ve.core.other-Object-java.lang.Object"; //$NON-NLS-1$

	private boolean useSuperClass;
	private boolean isSelectingTemplate = false;
	
	private IStatus fContributorStatus = StatusInfo.OK_STATUS;
	private IStatus fSourceFolderStatus = StatusInfo.OK_STATUS;

	private final static String PAGE_NAME= "NewVisualClassWizardPage"; //$NON-NLS-1$
	
	private final static String SETTINGS_CREATEMAIN= "create_main"; //$NON-NLS-1$
	private final static String SETTINGS_CREATECONSTR= "create_constructor"; //$NON-NLS-1$
	private final static String SETTINGS_CREATEUNIMPLEMENTED= "create_unimplemented"; //$NON-NLS-1$

	private boolean createMain = false, createConstructors = false, createInherited = true;
	private Button fMainCheckbox, fCtorsCheckbox, fInheritedCheckbox;

	public NewVisualClassWizardPage() {
		super(true, PAGE_NAME);
		
		setTitle("Java Visual Class"); 
		setDescription("Create a new Java Visual Class."); 
	}
	
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
	private void setTreeState() {
		Object[] topLevel = treeRoot.getChildren();
		if (topLevel != null)
			for (int i = 0; i < topLevel.length; i++)
				if (topLevel[i] instanceof CategoryModel)
					styleTreeViewer.setExpandedState(topLevel[i], ((CategoryModel) topLevel[i]).getDefaultExpand());
	}

	private void createTreeClassComposite(Composite composite, int nColumns) {

		Label styleLabel = new Label(composite, SWT.NONE);
		styleLabel.setText(CodegenWizardsMessages.NewVisualClassWizardPage_Style_Label);
		GridData styleLabelGridData = new GridData();
		styleLabelGridData.horizontalSpan = 3;
		styleLabel.setLayoutData(styleLabelGridData);
		Label filler = new Label(composite, SWT.NONE);
		GridData fillerGridData = new GridData();
		fillerGridData.horizontalSpan = 2;
		fillerGridData.grabExcessHorizontalSpace = true;
		filler.setLayoutData(fillerGridData);
		filler = new Label(composite, SWT.NONE);
		fillerGridData = new GridData();
		fillerGridData.horizontalSpan = nColumns-5;
		filler.setLayoutData(fillerGridData);
		
		
		styleTreeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.BORDER);
		GridData treeGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		treeGridData.verticalSpan = 6;
		treeGridData.verticalAlignment = SWT.FILL;
		treeGridData.widthHint = convertWidthInCharsToPixels(23);
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
		
		// to cause an extra column after tree viewer.
		filler = new Label(composite, SWT.NONE);
		fillerGridData = new GridData();
		fillerGridData.verticalSpan = 6;
		filler.setLayoutData(fillerGridData);

	}
	
	
	
	private IResource getContainerRoot(){ 
		return getWorkspaceRoot().findMember(getPackageFragmentRootText());
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
								selectedElement = vem;
							}
						} else if (vem.getCategory().equals(previousSelectedElementData[0]) && vem.getName().equals(previousSelectedElementData[1])) {
							selectedElement = vem;
							setSuperClass(previousSelectedElementData[2], true);
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
		super.updateStatus(getMostSevere(status));
	}	

	public void setSuperClass(String name) {
		useSuperClass = true;
		super.setSuperClass(name, true);
	}

	// When the container changes we must re-check whether or not this is a valid project for the template to be created into
	protected void handleFieldChanged(String fieldName) {
		if (fieldName == CONTAINER){
			fSourceFolderStatus = StatusInfo.OK_STATUS;
			try{
				// Check the status of whether or not we are in a proper source folder
				if(getContainerRoot() != null){
					IProject project = getContainerRoot().getProject();
					if(project != null && project.hasNature(JavaCore.NATURE_ID)){
						IJavaProject javaProject = JavaCore.create(project);
						if(ProxyPlugin.isPDEProject(javaProject)){
							if (!javaProject.isOnClasspath(getPackageFragmentRoot())){	
								// TODO Why is this here? It needs to be verified if needed. The root should of already evaluated no matter whether a pde or not.
								fSourceFolderStatus = new StatusInfo(
									IStatus.ERROR,
									"", 
									JavaVEPlugin.PLUGIN_ID);
							}
							else {
								// TODO KLUDGE https://bugs.eclipse.org/bugs/show_bug.cgi?id=90750
								// It does a check to see if the project is a PDE project, 
								// and if it is, does it have "org.eclipse.ui" in it. 
								// This was added as a kludge to be able to create SWT controls 
								// in PDE projects because since we currently can't update the 
								// PDE plugin.xml there was no way to make sure that SWT was in the classpath.
								// This should of been done through the contributor instead. 
								// This is not the proper place for it. It should be removed from here.
								//
								//Must include the ui plugin	
								FoundIDs foundIds = ProxyPlugin.getPlugin().getIDsFound(javaProject);
								boolean uiIncluded = foundIds.pluginIds.get("org.eclipse.ui") == Boolean.TRUE;//$NON-NLS-1$
								if (!uiIncluded)
									fSourceFolderStatus = new StatusInfo(
											IStatus.ERROR,
											"", 
											JavaVEPlugin.PLUGIN_ID);
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
		doStatusUpdate();
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 * 
	 * Overridden here to include all the controls from the NewClassWizardPage.
	 * If the NewClassWizardPage changes, this is where the changes need to be applied.
	 * 
	 * Method stubs selections will fit next to the Style tree and not below it.
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite topComposite= new Composite(parent, SWT.NONE);
		topComposite.setFont(parent.getFont());
		
		int nColumns= 4;
		
		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;		
		topComposite.setLayout(layout);
		
		createContainerControls(topComposite, nColumns);	
		createPackageControls(topComposite, nColumns);	
		createEnclosingTypeControls(topComposite, nColumns);
				
		createSeparator(topComposite, nColumns);
		createTypeNameControls(topComposite, nColumns);
		createModifierControls(topComposite, nColumns);

		Composite bottomComposite= new Composite(topComposite, SWT.NONE);
		bottomComposite.setFont(parent.getFont());
		GridData gd = new GridData();
		gd.horizontalSpan = nColumns;
		gd.horizontalAlignment= SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		bottomComposite.setLayoutData(gd);
		
		layout= new GridLayout();
		int bottomColumns = 6;
		layout.numColumns= bottomColumns;		
		bottomComposite.setLayout(layout);
		
		createTreeClassComposite(bottomComposite, bottomColumns);

		createSuperClassControls(bottomComposite, bottomColumns-2);
		createSuperInterfacesControls(bottomComposite, bottomColumns-2);
				
		createMethodStubSelectionControls(bottomComposite, bottomColumns-2);
		
		createCommentControls(bottomComposite, bottomColumns-2);
		enableCommentControl(true);
		
		setControl(bottomComposite);
			
		Dialog.applyDialogFont(bottomComposite);
	}

	/*
	 * Create label and checkboxes for method stub selections
	 * Includes:
	 * - Label ("Which method stubs would you like to create?")
	 * - Checkbox ("public static void main(String[] args)")
	 * - Checkbox ("Constructors from superclass")
	 * - Checkbox ("Inherited abstract methods")
	 * 
	 */
	private void createMethodStubSelectionControls(Composite composite, int nColumns) {
		IDialogSettings dialogSettings= getDialogSettings();
		if (dialogSettings != null) {
			IDialogSettings section= dialogSettings.getSection(PAGE_NAME);
			if (section != null) {
				createMain= section.getBoolean(SETTINGS_CREATEMAIN);
				createConstructors= section.getBoolean(SETTINGS_CREATECONSTR);
				createInherited= section.getBoolean(SETTINGS_CREATEUNIMPLEMENTED);
			}
		}

		Label label= new Label(composite, SWT.LEFT | SWT.WRAP);
		label.setFont(composite.getFont());
		label.setText("Which method stubs would you like to create?");
		GridData gd = new GridData();
		gd.horizontalSpan = nColumns;
		label.setLayoutData(gd);

		new Label(composite, SWT.NONE);
		// Create composite for the three checkboxes
		Composite comp = new Composite(composite, SWT.None);
		comp.setFont(composite.getFont());
		gd = new GridData();
		gd.horizontalSpan = nColumns-2;
		comp.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		comp.setLayout(layout);
		
		// Create the three checkboxes
		fMainCheckbox = new Button(comp, SWT.CHECK);
		fMainCheckbox.setText("public static void main(String[] args)");
		fMainCheckbox.setSelection(createMain);
		fCtorsCheckbox = new Button(comp, SWT.CHECK);
		fCtorsCheckbox.setText("Constructors from superclass");
		fCtorsCheckbox.setSelection(createConstructors);
		fInheritedCheckbox = new Button(comp, SWT.CHECK);
		fInheritedCheckbox.setText("Inherited abstract methods");
		fInheritedCheckbox.setSelection(createInherited);
		
		SelectionListener listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() == fMainCheckbox)
					createMain = fMainCheckbox.getSelection();
				else if (e.getSource() == fCtorsCheckbox)
					createConstructors = fCtorsCheckbox.getSelection();
				else if (e.getSource() == fInheritedCheckbox)
					createInherited = fInheritedCheckbox.getSelection();
			}
		};
		fMainCheckbox.addSelectionListener(listener);
		fCtorsCheckbox.addSelectionListener(listener);
		fInheritedCheckbox.addSelectionListener(listener);
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
	
	/*
	 * Set visible on the wizard.
	 * 
	 * Overridden because Eclipse doesn't like to show error messages when you launch a wizard
	 * but in the case where this wizard is launched via the New-->Other wizard, superclass parms
	 * are passed in which may not work correctly if the project is not configured with correct plugins
	 * or classpath. So there may be errors from selecting the specific element from the tree. 
	 * In this case we want the errors to show and prevent the user from continuing with the wizard.
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setFocus();
		} else {
			IDialogSettings dialogSettings= getDialogSettings();
			if (dialogSettings != null) {
				IDialogSettings section= dialogSettings.getSection(PAGE_NAME);
				if (section == null) {
					section= dialogSettings.addNewSection(PAGE_NAME);
				}
				section.put(SETTINGS_CREATEMAIN, isCreateMain());
				section.put(SETTINGS_CREATECONSTR, isCreateConstructors());
				section.put(SETTINGS_CREATEUNIMPLEMENTED, isCreateInherited());
			}
		}
		if (selectedElement != null) {
			IStatus status = selectedElement.getStatus(getContainerRoot());
			if(!(status.isOK())){
				// TODO hack mode - force selection again to get the error message to show
				styleTreeViewer.setSelection(new StructuredSelection(selectedElement), true);
			}
		}
	}
	/*
	 * Returns the current selection state of the 'Create Main' checkbox.
	 * 
	 * @return the selection state of the 'Create Main' checkbox
	 */
	public boolean isCreateMain() {
		return createMain;
	}

	/*
	 * Returns the current selection state of the 'Create Constructors' checkbox.
	 * 
	 * @return the selection state of the 'Create Constructors' checkbox
	 */
	public boolean isCreateConstructors() {
		return createConstructors;
	}
	
	/*
	 * Returns the current selection state of the 'Create inherited abstract methods' 
	 * checkbox.
	 * 
	 * @return the selection state of the 'Create inherited abstract methods' checkbox
	 */
	public boolean isCreateInherited() {
		return createInherited;
	}

	// -------- Initialization ---------
	
	/*
	 * The wizard owning this page is responsible for calling this method with the
	 * current selection. The selection is used to initialize the fields of the wizard 
	 * page.
	 * 
	 * @param selection used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem= getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		doStatusUpdate();
	}
	
	// ------ validation --------
	private void doStatusUpdate() {
		// status of all used components
		IStatus[] status= new IStatus[] {
			fContainerStatus,
			isEnclosingTypeSelected() ? fEnclosingTypeStatus : fPackageStatus,
			fTypeNameStatus,
			fModifierStatus,
			fSuperClassStatus,
			fSuperInterfacesStatus
		};
		
		// the mode severe status will be displayed and the OK button enabled/disabled.
		updateStatus(status);
	}
	
	// ---- creation ----------------
	
	/*
	 * @see NewTypeWizardPage#createTypeMembers
	 */
	protected void createTypeMembers(IType type, ImportsManager imports, IProgressMonitor monitor) throws CoreException {
		boolean doMain= isCreateMain();
		boolean doConstr= isCreateConstructors();
		boolean doInherited= isCreateInherited();
		createInheritedMethods(type, doConstr, doInherited, imports, new SubProgressMonitor(monitor, 1));

		if (doMain) {
			StringBuffer buf= new StringBuffer();
			final String lineDelim= "\n"; // OK, since content is formatted afterwards //$NON-NLS-1$
			String comment= CodeGeneration.getMethodComment(type.getCompilationUnit(), type.getTypeQualifiedName('.'), "main", new String[] {"args"}, new String[0], Signature.createTypeSignature("void", true), null, lineDelim); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (comment != null) {
				buf.append(comment);
				buf.append(lineDelim);
			}
			buf.append("public static void main("); //$NON-NLS-1$
			buf.append(imports.addImport("java.lang.String")); //$NON-NLS-1$
			buf.append("[] args) {"); //$NON-NLS-1$
			buf.append(lineDelim);
			final String content= CodeGeneration.getMethodBodyContent(type.getCompilationUnit(), type.getTypeQualifiedName('.'), "main", false, "", lineDelim); //$NON-NLS-1$ //$NON-NLS-2$
			if (content != null && content.length() != 0)
				buf.append(content);
			buf.append(lineDelim);
			buf.append("}"); //$NON-NLS-1$
			type.createMethod(buf.toString(), null, false, null);
		}
		
		if (monitor != null) {
			monitor.done();
		}	
	}
	/*
	 * Finds the most severe status from a array of stati.
	 * An error is more severe than a warning, and a warning is more severe
	 * than ok.
	 */
	private static IStatus getMostSevere(IStatus[] status) {
		IStatus max= null;
		for (int i= 0; i < status.length; i++) {
			IStatus curr= status[i];
			if (curr.matches(IStatus.ERROR)) {
				return curr;
			}
			if (max == null || curr.getSeverity() > max.getSeverity()) {
				max= curr;
			}
		}
		return max;
	}
	
}