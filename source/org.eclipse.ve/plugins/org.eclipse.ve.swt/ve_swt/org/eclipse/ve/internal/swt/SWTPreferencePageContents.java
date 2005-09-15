package org.eclipse.ve.internal.swt;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Label;

public class SWTPreferencePageContents extends Composite {

	private Table table = null;
	private TableItem currentlyCheckedItem = null;
	private Label label = null;

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
//					Ensure only one item can be checked
					if (currentlyCheckedItem != null){
						currentlyCheckedItem.setChecked(false);
						currentlyCheckedItem = null;
					}					
					currentlyCheckedItem = selectedItem;
				} else if(selectedItem == currentlyCheckedItem){
					currentlyCheckedItem = null;
				}
			}
		});
	}

	public void init(Preferences fStore) {
		
		// Fill the layout table with the allowable layouts
		String[][] layoutItems = new String[][] {
			new String[] {"null","GridLayout","FillLayout","RowLayout","FormLayout"},
			new String[] {null,"org.eclipse.swt.layout.GridLayout","org.eclipse.swt.layout.FillLayout","org.eclipse.swt.layout.RowLayout","org.eclipse.swt.layout.FormLayout"}
		};
		String defaultLayoutTypeName = fStore.getString(SwtPlugin.DEFAULT_LAYOUT);		
		for (int i = 0; i < layoutItems[0].length; i++) {
			TableItem item = new TableItem(table,SWT.NONE);
			item.setText(layoutItems[0][i]);
			String typeName = layoutItems[1][i];
			item.setData("TYPE_NAME",typeName);
			if(typeName != null && typeName.equals(defaultLayoutTypeName)){
				item.setChecked(true);
			}
		}
	}
	
	public String getLayoutTypeName() {
		if (table.getSelectionCount() == 1){
			return (String) table.getSelection()[0].getData("TYPE_NAME");
		} else {
			return null;
		}
	}
}
