/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VCEPrefContributor.java,v $
 *  $Revision: 1.9 $  $Date: 2006-05-17 20:14:52 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import com.ibm.icu.util.StringTokenizer;

/**
 * @author Gili Mendel
 *
 */
public class VCEPrefContributor extends AbstractStyleContributor {

	public final static String DEFAULT_INIT_METHOD = "DEFAULT_INIT_METHOD"; //$NON-NLS-1$
	public final static String MName = CodegenJavaRulesMessages.VCEPrefContributor_MethodName; 
	public final static String MDialogName = CodegenJavaRulesMessages.VCEPrefContributor_Dialog_Title; 
	public final static String[] DefaultMethods = { "jbInit", "initComponents", "initialize" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$


	private static final String fbuttonsTxt[] = new String[] {
		CodegenJavaRulesMessages.VCEPrefContributor_Add, 
		CodegenJavaRulesMessages.VCEPrefContributor_Edit, 
		/* 2 */
		null,
		CodegenJavaRulesMessages.VCEPrefContributor_Remove
	};

	private static String[] currentMethods = null;
	
	private boolean fMethodsAreSaved = true;	// Until we make a change they are considered saved.
	private ArrayList methods;
	private TableViewer tv;
	
	/**
	 * @param useCache if false will read the Store
	 * @return the list of initialization methods.
	 */
	public static String[] getMethodsFromStore() {

		if (currentMethods == null) {
			// Need to build the current methods from the store.
			String initMethods = primGetPrefStore().getString(DEFAULT_INIT_METHOD);
			if (initMethods.length() == 0) {
				currentMethods = DefaultMethods;
			} else {
				ArrayList methods = extractMethods(initMethods);
				currentMethods = (String[]) methods.toArray(new String[methods.size()]);
			}
			
			// Make the default for default init be the DefaultMethods so that future restore to defaults
			// will see these and know it is the default.
			primGetPrefStore().setDefault(DEFAULT_INIT_METHOD, makeStoreDefaultInit(DefaultMethods).toString());			
		}
		
		return currentMethods;
	}
	
	private static ArrayList extractMethods(String methodString) {
		ArrayList methods = new ArrayList(3);
		StringTokenizer stok = new StringTokenizer(methodString, ";"); //$NON-NLS-1$
		// Skip the first element.  We will always store a dummy first element
		// so that we can distinguish between a "" of no methods, or "" Nothing in the Store,
		// which requires the usage of the defaults above.
		if (stok.hasMoreTokens())
			stok.nextToken();
		while (stok.hasMoreTokens()) {
			methods.add(stok.nextToken());
		}
		return methods;
	}
	
	protected void setMethodsToStore() {

		if (fMethodsAreSaved)
			return;
		
		currentMethods = (String[]) methods.toArray(new String[methods.size()]);	// This also becomes the current methods.
		getPrefStore().setValue(DEFAULT_INIT_METHOD, makeStoreDefaultInit(currentMethods).toString());			
		fMethodsAreSaved = true;

	}
	
	private static StringBuffer makeStoreDefaultInit(String[] methods) {
		StringBuffer buf = new StringBuffer(50);
		// Store at least one element
		buf.append("&Methods&"); //$NON-NLS-1$
		for (int i = 0; i < methods.length; i++) {
			buf.append(';'); //$NON-NLS-1$
			buf.append(methods[i]);
		}
		return buf;
	}
	
	private String processEdit(Shell parentShell, String Title, String input) {
		IInputValidator v = new IInputValidator() {
			public java.lang.String isValid(java.lang.String newText) {
				IStatus stat = JavaConventions.validateMethodName(newText);
				if (stat.isOK()) {
					return null;
				} else
					return stat.getMessage();
			}
		};
		InputDialog d = new InputDialog(parentShell, Title, MName, input, v);
		d.open();
		// InputDialog returns null if Cancel is pressed.
		// Hence when Cancel is pressed, return the original value.
		String mod = d.getValue();
		if (mod == null)
			return input;
		else
			return mod;
	}

	protected void createMethodListDialog(Composite parent) {
		String[] current = getMethodsFromStore();
		methods = new ArrayList(current.length);
		for (int i = 0; i < current.length; i++) {
			methods.add(current[i]);
		}
		
		// top Composite
		Composite top = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan=2;
		top.setLayoutData(gd);
		top.setLayout(new GridLayout(2, false));
		
		// label
		Label label = new Label(top, SWT.NONE);
		label.setText(CodegenJavaRulesMessages.VCEPrefContributor_InitMethodsList_Text);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		
		// list of methods
		Table table = new Table(top, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan=1;
		gd.verticalSpan=3;
		table.setLayoutData(gd);
		TableColumn tc = new TableColumn(table, SWT.NONE);
		tc.setText(MName);
		tv = new TableViewer(table);
		tv.setLabelProvider(new ITableLabelProvider(){
			public Image getColumnImage(Object element, int columnIndex) {return null;}
			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof String) {
					String methodName = (String) element;
					return methodName;
				}
				return null;
			}
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public boolean isLabelProperty(Object element, String property) {return true;}
			public void removeListener(ILabelProviderListener listener) {}
		});
		tv.setContentProvider(new IStructuredContentProvider(){
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					List list = (List) inputElement;
					return list.toArray();
				}
				return null;
			}
			public void dispose() {}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		});
		tv.setInput(methods);
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(1, false));
		table.setLayout(tableLayout);
						
		// add button
		final Button addButton = new Button(top, SWT.PUSH);
		addButton.setText(fbuttonsTxt[0]);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_VERTICAL);
		gd.verticalAlignment = SWT.END;
		addButton.setLayoutData(gd);
		
		// edit button
		final Button editButton = new Button(top, SWT.PUSH);
		editButton.setText(fbuttonsTxt[1]);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		editButton.setLayoutData(gd);
		
		// add button
		final Button removeButton = new Button(top, SWT.PUSH);
		removeButton.setText(fbuttonsTxt[3]);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_VERTICAL);
		gd.verticalAlignment = SWT.BEGINNING;
		removeButton.setLayoutData(gd);
		
		// button listeners
		addButton.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				String newMethodName = processEdit(addButton.getShell(), MDialogName, ""); //$NON-NLS-1$
				methods.add(newMethodName);
				fMethodsAreSaved = false;
				tv.refresh();
				editButton.setEnabled(!tv.getSelection().isEmpty());
				removeButton.setEnabled(!tv.getSelection().isEmpty());
			}
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
		});
		editButton.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				if (tv.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection strSel = (IStructuredSelection) tv.getSelection();
					String originalMethodName = (String)strSel.getFirstElement();
					String newMethodName = processEdit(editButton.getShell(), MDialogName, originalMethodName);
					if(!newMethodName.equals(originalMethodName)){
						methods.add(methods.indexOf(originalMethodName), newMethodName);
						methods.remove(originalMethodName);
						fMethodsAreSaved = false;
						tv.refresh();
						editButton.setEnabled(!tv.getSelection().isEmpty());
						removeButton.setEnabled(!tv.getSelection().isEmpty());
					}
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
		});
		removeButton.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				if (tv.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection strSel = (IStructuredSelection) tv.getSelection();
					String originalMethodName = (String)strSel.getFirstElement();
					methods.remove(originalMethodName);
					fMethodsAreSaved = false;
					tv.refresh();
					editButton.setEnabled(!tv.getSelection().isEmpty());
					removeButton.setEnabled(!tv.getSelection().isEmpty());
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
		});
		
		// table selection listener
		addButton.setEnabled(true);
		editButton.setEnabled(!tv.getSelection().isEmpty());
		removeButton.setEnabled(!tv.getSelection().isEmpty());
		tv.getTable().addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				Object selection = null;
				if (tv.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection strSel = (IStructuredSelection) tv.getSelection();
					selection = strSel.getFirstElement();
				}
				addButton.setEnabled(true);
				editButton.setEnabled(selection!=null);
				removeButton.setEnabled(selection!=null);
			}
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			
		});
	}

	protected void createPrefTab(TabFolder tabF) {
		TabItem tab = new TabItem(tabF, SWT.NONE);
		tab.setText(CodegenJavaRulesMessages.VCEPrefContributor_Tab_Preference_Text); 
		Composite c = new Composite(tabF, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		GridData cGD = new GridData(GridData.FILL_BOTH);
		cGD.widthHint = Dialog.convertWidthInCharsToPixels(getFontMetrics(c), 40);
		cGD.heightHint = Dialog.convertHeightInCharsToPixels(getFontMetrics(c), 40);
		c.setLayoutData(cGD);
		tab.setControl(c);

		createMethodListDialog(c);

	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.AbstractStyleContributor#buildUI(Composite)
	 */
	protected Control buildUI(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		createPrefTab(tabFolder);
		//		TODO need to allow updating templates.		    
		//	    createTemplateTab(tabFolder) ;	  
		return tabFolder;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStylePrefUI#storeUI()
	 */
	public void storeUI() {
		setMethodsToStore();
		saveStore();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStylePrefUI#restoreDefaults()
	 */
	public void restoreDefaults() {
		// Restore in the panels to the DefaultMethods.
		fMethodsAreSaved = false;	// Since we've changed things.
		methods.clear();
		for (int i = 0; i < DefaultMethods.length; i++) {
			methods.add(DefaultMethods[i]);
		}
		if(tv!=null)
			tv.setInput(methods);
	}

}
