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
 *  $RCSfile: VCEPrefContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * @author Gili Mendel
 *
 */
public class VCEPrefContributor extends AbstractStyleContributor {

	public final static String DEFAULT_INIT_METHOD = "DEFAULT_INIT_METHOD"; //$NON-NLS-1$
	public final static String MName = CodegenJavaRulesMessages.getString("VCEPrefContributor.MethodName"); //$NON-NLS-1$
	public final static String MDialogName = CodegenJavaRulesMessages.getString("VCEPrefContributor.Dialog.Title"); //$NON-NLS-1$
	public final static String[] DefaultMethods = { "jbInit", "initComponents", "initialize" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$


	private static final String fbuttonsTxt[] = new String[] {
		/* 0 */
		CodegenJavaRulesMessages.getString("VCEPrefContributor.Add"), //$NON-NLS-1$
		/* 1 */
		CodegenJavaRulesMessages.getString("VCEPrefContributor.Edit"), //$NON-NLS-1$
		/* 2 */
		null,
		/* 3 */
		CodegenJavaRulesMessages.getString("VCEPrefContributor.Remove") //$NON-NLS-1$
	};

	private static String[] currentMethods = null;
	
	private boolean fMethodsAreSaved = true;	// Until we make a change they are considered saved.
	private ArrayList methods;	
	private ListDialogField df;	

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
		java.util.StringTokenizer stok = new java.util.StringTokenizer(methodString, ";"); //$NON-NLS-1$
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
	
	protected void createMethodListDialog(Composite parent) {

		final Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		c.setLayoutData(gd);

		ILabelProvider lp = new LabelProvider();

		IListAdapter la = new IListAdapter() {

			String processEdit(String Title, String input) {
				IInputValidator v = new IInputValidator() {
					public java.lang.String isValid(java.lang.String newText) {
						IStatus stat = JavaConventions.validateMethodName(newText);
						if (stat.isOK()) {
							return null;
						} else
							return stat.getMessage();
					}
				};
				InputDialog d = new InputDialog(c.getShell(), Title, MName, input, v);
				d.open();
				// InputDialog returns null if Cancel is pressed.
				// Hence when Cancel is pressed, return the original value.
				String mod = d.getValue();
				if (mod == null)
					return input;
				else
					return mod;
			}
			String editElement(ListDialogField field) {
				String result = processEdit(MDialogName, (String) field.getSelectedElements().get(0));
				if (result != null) {
					String current = (String) field.getSelectedElements().get(0);
					field.replaceElement(current, result);
					int index = methods.indexOf(current);
					methods.remove(index);
					methods.add(index, result);
					fMethodsAreSaved = false;
				} else
					result = (String) field.getSelectedElements().get(0);
				return result;
			}
			public void customButtonPressed(ListDialogField field, int index) {
				switch (index) {
					// Add
					case 0 :
						String result = processEdit(MDialogName, ""); //$NON-NLS-1$
						if (result != null && result.length() > 0) {
							// Do no add duplicates
							for (Iterator itr = field.getElements().iterator(); itr.hasNext();)
								if (((String) itr.next()).equals(result))
									return;
							field.addElement(result);
							methods.add(result);
							fMethodsAreSaved = false;
						}
						break;
						// Edit 
					case 1 :
						editElement(field);
						break;
						// Remove
					case 3 :
						methods.removeAll(field.getSelectedElements());
						field.removeElements(field.getSelectedElements());
						fMethodsAreSaved = false;
						break;
				}
			}
			public void selectionChanged(ListDialogField field) {
			}
			public void doubleClicked(ListDialogField field) {
				editElement(field);
			}
		};

		String[] current = getMethodsFromStore();
		methods = new ArrayList(current.length);
		for (int i = 0; i < current.length; i++) {
			methods.add(current[i]);
		}
		
		df = new ListDialogField(la, fbuttonsTxt, lp);
		df.setLabelText(CodegenJavaRulesMessages.getString("VCEPrefContributor.InitMethodsList.Text")); //$NON-NLS-1$

		df.setTableColumns(new ListDialogField.ColumnsDescription(new String[] { MName }, true));
		df.setElements(methods);

		GridData lgd = new GridData(GridData.FILL_HORIZONTAL);
		lgd.horizontalSpan = 2;
		Control[] LblListButt = df.doFillIntoGrid(c, 3);
		LblListButt[0].setLayoutData(lgd);

	}

	protected void createPrefTab(TabFolder tabF) {
		TabItem tab = new TabItem(tabF, SWT.NONE);
		tab.setText(CodegenJavaRulesMessages.getString("VCEPrefContributor.Tab.Preference.Text")); //$NON-NLS-1$
		Composite c = new Composite(tabF, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		GridData cGD = new GridData(GridData.FILL_BOTH);
		cGD.widthHint = Dialog.convertWidthInCharsToPixels(getFontMetrics(c), 40);
		cGD.heightHint = Dialog.convertHeightInCharsToPixels(getFontMetrics(c), 40);
		c.setLayoutData(cGD);
		tab.setControl(c);

		createMethodListDialog(c);

	}

	protected void createTemplateTab(TabFolder tabF) {
		TabItem tab = new TabItem(tabF, SWT.NONE);
		tab.setText(CodegenJavaRulesMessages.getString("VCEPrefContributor.Tab.Templates.Text")); //$NON-NLS-1$
		Composite c = new Composite(tabF, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		c.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		c.setLayoutData(gd);
		tab.setControl(c);

		Table templates = new Table(c, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData td = new GridData(GridData.BEGINNING);
		td.widthHint = 200;
		templates.setLayoutData(gd);
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(100, 200));
		templates.setLayout(tableLayout);
		templates.setHeaderVisible(true);
		templates.setLinesVisible(true);

		Label f = createLabel(c, "", null); // Filler //$NON-NLS-1$
		 ((GridData) f.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) f.getLayoutData()).horizontalAlignment = GridData.FILL;

		TableColumn tc = new TableColumn(templates, SWT.NONE);
		tc.setText(CodegenJavaRulesMessages.getString("VCEPrefContributor.Table.Templates.TableColumn.Template")); //$NON-NLS-1$
		tc.setWidth(200);
		TableItem ti = new TableItem(templates, SWT.NONE);
		ti.setText(0, CodegenJavaRulesMessages.getString("VCEPrefContributor.Table.Templates.TableColumn.Sample")); //$NON-NLS-1$
		templates.select(0);

		createLabel(c, CodegenJavaRulesMessages.getString("VCEPrefContributor.TemplatePreview.Label.Text"), null); //$NON-NLS-1$
		Button bEdit = new Button(c, SWT.PUSH);
		GridData bgd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		bEdit.setLayoutData(bgd);
		bEdit.setText(CodegenJavaRulesMessages.getString("VCEPrefContributor.TemplatePreview.Edit.Button.Text")); //$NON-NLS-1$
		SourceViewer sv = createPreview(c, 2, 70, 7);
		Document d = new Document();
		sv.setDocument(d);
		d.set("public  JButton getMyButton() {\n    if (myButton == null) {\n         .....\n    }\n    return myButton ;\n}"); //$NON-NLS-1$

		createCheckBox(c, CodegenJavaRulesMessages.getString("VCEPrefContributor.Check.UseFormatter.Text"), SWT.NONE); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.AbstractStyleContributor#buildUI(Composite)
	 */
	protected Control buildUI(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayout(new org.eclipse.jdt.internal.ui.util.TabFolderLayout());
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
		df.setElements(methods);
	}

}
