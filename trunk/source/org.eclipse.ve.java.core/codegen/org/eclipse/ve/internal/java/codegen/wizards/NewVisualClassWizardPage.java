package org.eclipse.ve.internal.java.codegen.wizards;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NewVisualClassWizardPage.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-28 23:15:59 $ 
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.*;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author JoeWin
 */
public class NewVisualClassWizardPage extends NewClassWizardPage {

    TreeViewer styleTreeViewer;

    CategoryModel treeRoot;

    public static final String CREATE_MAIN = "createMain";

    public static final String CREATE_SUPER_CONSTRUCTORS = "createSuperConstructors";

    public static final String CREATE_INHERITED_ABSTRACT = "createInheritedAbstract";

    boolean isSettingSuperclass;
	
	
	public class TreePrioritySorter extends ViewerSorter {        
	    public int category(Object element) {
	        if(element instanceof CategoryModel)
	            return ((CategoryModel)element).getPriority();
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
                    styleTreeViewer.setExpandedState(
                            (CategoryModel) topLevel[i],
                            ((CategoryModel) topLevel[i]).getDefaultExpand());
    }
	
	protected void createTreeClassComposite(Composite composite, int nColumns) {

        Composite labelGroup = createComposite(composite, 5);
        ((GridLayout) labelGroup.getLayout()).marginHeight = 0;
        GridData labelData = (GridData) labelGroup.getLayoutData();
        labelData.horizontalSpan = 5;
        createLabel(labelGroup, "Style:");

        styleTreeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.BORDER);
        GridData treeGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        treeGridData.verticalSpan = 4;
        treeGridData.widthHint = convertWidthInCharsToPixels(23);
        treeGridData.heightHint = convertHeightInCharsToPixels(16);
        styleTreeViewer.getTree().setLayoutData(treeGridData);

        styleTreeViewer.setContentProvider(new StyleTreeContentProvider());
        styleTreeViewer.setInput(getInitalStyleInput());
        styleTreeViewer.setLabelProvider(new StyleTreeLabelProvider());
        styleTreeViewer.setSorter(new TreePrioritySorter());
        setTreeState();

        styleTreeViewer
                .addSelectionChangedListener(new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent event) {
                        if (event.getSelection() instanceof IStructuredSelection) {
                            IStructuredSelection selection = 
                                (IStructuredSelection) event.getSelection();
                            for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
                                Object domain = iterator.next();
                                if (domain instanceof VisualElementModel) {
                                	setSuperClass(((VisualElementModel) domain).getSuperClass());
                                	getSuperclassButtonDialogField().setEnabled(false);
                                }
                            }
                        }
                    }
                });
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
	
	protected void createSuperInterfacesControls(Composite composite, int nColumns)
	{	  

	}
	
	/**
	 * Finds the parent of the contributed style. This method is 
	 * used to pair up the visual elements with the style (through
	 * the id component
	 * @param category - the category this elements belongs too
	 * @param treeRoot - the CategoryModel linked list to search
	 * @return returns the parenting CategoryModel
	 */
	private CategoryModel findParentStyle(String category,
            CategoryModel treeRoot) {
        Object[] iterator = treeRoot.getChildren();
        for (int i = 0; i < iterator.length; i++) {
            if (iterator[i] instanceof CategoryModel) {
                if (((CategoryModel) iterator[i]).getId().
                        equalsIgnoreCase(category))
                    return (CategoryModel) iterator[i];
            }
        }
        return null;
    }
	
    /**
     * Populates the TreeView with the newStyleComponent
     * contributions
     * @return
     */
    private CategoryModel getInitalStyleInput() {

        treeRoot = new CategoryModel();
        IExtensionPoint exp = Platform.getExtensionRegistry()
                .getExtensionPoint(
                        JavaVEPlugin.getPlugin().getBundle().getSymbolicName(),
                        "newStyleComponent"); //$NON-NLS-1$
        IExtension[] extensions = exp.getExtensions();
        CategoryModel parentStyle = null;
        if (extensions != null && extensions.length > 0) {
            for (int ec = 0; ec < extensions.length; ec++) {
                IConfigurationElement[] configElms = extensions[ec]
                        .getConfigurationElements();
                for (int cc = 0; cc < configElms.length; cc++) {
                    IConfigurationElement celm = configElms[cc];
                    if (celm.getName().equalsIgnoreCase("Category")) {
                        treeRoot.addStyle(new CategoryModel(celm.getAttribute("name"), 
                                celm.getAttribute("id"),
                                celm.getAttribute("priority"), 
                                celm.getAttribute("defaultExpand")));
                    }
                    if (celm.getName().equalsIgnoreCase("visualElement")) {

                        parentStyle = findParentStyle(celm.getAttribute("category"), 
                                treeRoot);
                        if (parentStyle != null)
                            parentStyle
                                    .addVisualElement(new VisualElementModel(
                                            celm.getAttribute("name"), 
                                            celm.getAttribute("type"),
                                            celm.getAttribute("contributor")));
                        else
                        {
                            treeRoot.addVisualElement(new VisualElementModel(
                                    celm.getAttribute("name"), 
                                    celm.getAttribute("type"),
                                    celm.getAttribute("contributor")));
                        }
                    }

                }
            }
        }

        return treeRoot;
    }

  
	protected void setSuperClass(String name){
		isSettingSuperclass = true;
		super.setSuperClass(name,true);
		isSettingSuperclass = false;
	}
	
	protected SelectionButtonDialogFieldGroup fMethodStubsButtonsField;
	protected SelectionButtonDialogFieldGroup getMethodStubsButtonsField(){
		if ( fMethodStubsButtonsField == null ) {
			// This field is private and inherited so we have to retrieve it using reflection
			try {
				Class newClassWizardPage = Class.forName("org.eclipse.jdt.ui.wizards.NewClassWizardPage"); //$NON-NLS-1$			
				java.lang.reflect.Field field = newClassWizardPage.getDeclaredField("fMethodStubsButtons"); //$NON-NLS-1$
				field.setAccessible(true);
				fMethodStubsButtonsField = (SelectionButtonDialogFieldGroup)field.get(this);
			} catch ( Exception exc ) {
			    JavaVEPlugin.log(exc, Level.FINEST);
			}
		}
		return fMethodStubsButtonsField;
	}
	
	
	protected StringButtonDialogField localSuperclassButtonDialogField;
	protected StringButtonDialogField getSuperclassButtonDialogField(){
		if ( localSuperclassButtonDialogField == null ) {
			// This field is private and inherited so we have to retrieve it using reflection
			try {
				Class newClassWizardPage = Class.forName("org.eclipse.jdt.ui.wizards.NewTypeWizardPage"); //$NON-NLS-1$			
				java.lang.reflect.Field field = newClassWizardPage.getDeclaredField("fSuperClassDialogField"); //$NON-NLS-1$
				field.setAccessible(true);
				localSuperclassButtonDialogField = (StringButtonDialogField)field.get(this);
			} catch ( Exception exc ) {
			    JavaVEPlugin.log(exc, Level.FINEST);
			}
		}
		return localSuperclassButtonDialogField;
	}
	
	// We need to know when the superclass text field has changed so that we can deselect the buttons
	// This will help the user to know that they are no longer subclassing a Frame or Panel or Applet
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);	

	}
	
	protected Composite createComposite(Composite aParent, int numColumns){
		Composite group = new Composite(aParent,SWT.NONE);
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
	protected Group createGroup(Composite aParent, String title, int numColumns){
		Group group = new Group(aParent,SWT.NONE);
		if ( title != null ) {
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
			
	protected void createLabel(Composite parent,String text) {
		Label spacer = new Label(parent, SWT.NONE);
		if ( text != null ) {
			spacer.setText(text);
		}
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		spacer.setLayoutData(data);
	}		
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 * 
	 * Overridden here because we have to get the the private field NewClassWizard.fMethodStubsButton in order to 
	 * reset its layout data so that the method stubs selections will fit next to the Style tree and not below it.
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
	 * Constructs the argument matrix HashMap from the
	 * method controls argument
	 * @return argument hashmap
	 */
	public HashMap getArgumentMatrix()
	{
	    HashMap argumentMatrix = new HashMap();
        argumentMatrix.put(CREATE_MAIN, isCreateMain() == true 
                ? "true" : "false");
        argumentMatrix.put(CREATE_SUPER_CONSTRUCTORS,
                isCreateConstructors() == true ? "true" : "false");
        argumentMatrix.put(CREATE_INHERITED_ABSTRACT,
                isCreateInherited() == true ? "true" : "false");
	    return argumentMatrix;	
	}
	
}
