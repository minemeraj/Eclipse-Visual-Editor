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
package org.eclipse.ve.internal.swt;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SWTPreferencePageContents extends Composite {

	private Table table = null;
	private TableItem currentlyCheckedItem = null;
	private Label label = null;
	private static final String TYPE_NAME = "TYPE_NAME";

	public SWTPreferencePageContents(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		label = new Label(this, SWT.NONE);
		label.setText("Default Layout");
		this.setLayout(new GridLayout());
		createTable();
		setSize(new Point(300, 200));
	}

	/**
	 * This method initializes table	
	 *
	 */
	private void createTable() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		table = new Table(this, SWT.BORDER | SWT.CHECK);
		table.setHeaderVisible(false);
		table.setLayoutData(gridData);
		table.setLinesVisible(false);
		table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				TableItem selectedItem = (TableItem)e.item;
				if (selectedItem.getChecked()){
					if(currentlyCheckedItem != null){
						currentlyCheckedItem.setChecked(false);
					}
					currentlyCheckedItem = selectedItem;
				} else {
					currentlyCheckedItem = null;
				}
			}
		});
	}

	public void init(Preferences fStore) {
		
		// Fill the layout table with the allowable layouts
		String[][] layoutItems = SwtPlugin.getDefault().getLayouts();
		String defaultLayoutTypeName = fStore.getString(SwtPlugin.DEFAULT_LAYOUT);		
		for (int i = 0; i < layoutItems[0].length; i++) {
			TableItem item = new TableItem(table,SWT.NONE);
			item.setText(layoutItems[0][i]);
			String typeName = layoutItems[1][i];
			item.setData(TYPE_NAME,typeName);
			if(typeName != null && typeName.equals(defaultLayoutTypeName)){
				item.setChecked(true);
				currentlyCheckedItem = item;
			}
		}
	}
	
	public String getLayoutTypeName() {
		return (String) (currentlyCheckedItem == null ? null : currentlyCheckedItem.getData(TYPE_NAME));
	}
	public void setLayoutTypeName(String layoutManagerName) {
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem item = table.getItem(i);
			if(layoutManagerName == null || layoutManagerName.length()==0){
				if(item.getData(TYPE_NAME) == null){
					item.setChecked(true);
					currentlyCheckedItem = item;
					continue;
				}
			} else {
				if(layoutManagerName.equals(item.getData(TYPE_NAME))){
					item.setChecked(true);
					currentlyCheckedItem = item;
					continue;
				}
			}
			item.setChecked(false);
		}
	}	
}
