package org.eclipse.ve.internal.jfc.core;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class JFCPreferencePageContents extends Composite {

	private Label label1 = null;
	private Tree tree = null;
	private static final String TYPE_NAME = "TYPE_NAME";
	private TreeItem currentlyCheckedItem;
	
	public JFCPreferencePageContents(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridLayout());
		setSize(new org.eclipse.swt.graphics.Point(365,200));
		label1 = new Label(this, SWT.NONE);
		label1.setText("Default Layout Manager");
		createTree();
	}

	public void init(Preferences fStore) {
//		 Fill the layout table with the allowable layouts
		String[][] layoutItems = JFCVisualPlugin.getPlugin().getLayoutManagers();
		String defaultLayoutTypeName = fStore.getString(JFCVisualPlugin.DEFAULT_LAYOUTMANAGER);		
		for (int i = 0; i < layoutItems[0].length; i++) {
			TreeItem item = new TreeItem(tree,SWT.NONE);
			item.setText(layoutItems[0][i]);
			String typeName = layoutItems[1][i];
			item.setData(TYPE_NAME,typeName);
			if(typeName != null && typeName.equals(defaultLayoutTypeName)){
				item.setChecked(true);
			}
		}		
	}

	/**
	 * This method initializes tree	
	 *
	 */
	private void createTree() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tree = new Tree(this, SWT.CHECK | SWT.BORDER);
		tree.setLayoutData(gridData1);
		tree.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				TreeItem selectedItem = (TreeItem)e.item;
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
}  //  @jve:decl-index=0:visual-constraint="10,10"
