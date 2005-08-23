/*
 * Copyright (C) 2005 by David Orme  <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme - Initial API and implementation
 */
package org.eclipse.ve.sweet.objecteditor;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.ScrolledComposite;

public class ObjectEditor extends Composite {

	private Composite linkAreaHolder = null;
	private TabFolder tabFolder = null;
	private TabItem listTabItem = null;
	private TabItem detailTabItem = null;
	private Composite linkArea = null;
	private Composite headerArea = null;
	private Label objectName = null;
	private Label queryPath = null;
	private Composite listHolder = null;
	private Composite detailTab = null;
	private Label searchLabel = null;
	private Text searchText = null;
	private Composite listTableHolder = null;
	private ScrolledComposite detailScroller = null;
	private Composite detailHolder = null;
	
	public ObjectEditor(Composite parent, int style) {
		super(parent, style);
		initialize();
		setBackground(parent.getBackground());
	}

	private void initialize() {
		createObjectEditor();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 10;
		gridLayout.horizontalSpacing = 10;
		createComposite();
		this.setLayout(gridLayout);
		createTabFolder();
		setSize(new org.eclipse.swt.graphics.Point(582,360));
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 0;
		fillLayout.marginWidth = 5;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.widthHint = 120;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		linkAreaHolder = new Composite(this, SWT.BORDER);
		linkAreaHolder.setLayoutData(gridData1);
		linkAreaHolder.setLayout(fillLayout);
		createLinkArea();
	}

	/**
	 * This method initializes tabFolder	
	 *
	 */
	private void createTabFolder() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tabFolder = new TabFolder(this, SWT.BORDER);
		tabFolder.setLayoutData(gridData);
		createComposite4();
		createDetailTab();
		listTabItem = new TabItem(tabFolder, SWT.NONE);
		listTabItem.setText("&List");
		listTabItem.setControl(listHolder);
		detailTabItem = new TabItem(tabFolder, SWT.NONE);
		detailTabItem.setText("&Detail");
		detailTabItem.setControl(detailTab);
	}

	/**
	 * This method initializes linkArea	
	 *
	 */
	private void createLinkArea() {
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.verticalSpacing = 10;
		linkArea = new Composite(linkAreaHolder, SWT.NONE);
		linkArea.setLayout(gridLayout3);
	}

	/**
	 * This method initializes objectEditor	
	 *
	 */
	private void createObjectEditor() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalSpan = 2;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		headerArea = new Composite(this, SWT.BORDER);
		headerArea.setLayoutData(gridData2);
		headerArea.setLayout(gridLayout1);
		objectName = new Label(headerArea, SWT.NONE);
		objectName.setText("Object Name");
		objectName.setFont(JFaceResources.getFontRegistry().get(JFaceResources.HEADER_FONT));
		queryPath = new Label(headerArea, SWT.NONE);
		queryPath.setText("[Query Path]");
		queryPath.setLayoutData(gridData3);
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite4() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		listHolder = new Composite(tabFolder, SWT.NONE);
		listHolder.setLayout(gridLayout2);
		searchLabel = new Label(listHolder, SWT.NONE);
		searchLabel.setText("Search:");
		searchText = new Text(listHolder, SWT.BORDER);
		searchText.setLayoutData(gridData4);
		createListTableHolder();
	}

	/**
	 * This method initializes detailTab	
	 *
	 */
	private void createDetailTab() {
		FillLayout fillLayout1 = new FillLayout();
		fillLayout1.marginHeight = 5;
		fillLayout1.marginWidth = 5;
		detailTab = new Composite(tabFolder, SWT.NONE);
		detailTab.setLayout(fillLayout1);
		createDetailScroller();
	}

	/**
	 * This method initializes listTableHolder	
	 *
	 */
	private void createListTableHolder() {
		FillLayout fillLayout2 = new FillLayout();
		fillLayout2.marginHeight = 5;
		fillLayout2.marginWidth = 5;
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.horizontalSpan = 2;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		listTableHolder = new Composite(listHolder, SWT.NONE);
		listTableHolder.setLayoutData(gridData5);
		listTableHolder.setLayout(fillLayout2);
	}


	/**
	 * Method getListTableHolder.  Returns the list table holder Composite.
	 *
	 * @return the list table holder Composite.
	 */
	public Composite getListTableHolder() {
		return listTableHolder;
	}

	/**
	 * Method getDetailHolder.  Returns the detail holder Composite.
	 *
	 * @return The detail holder Composite.
	 */
	public Composite getDetailHolder() {
		return detailTab;
	}

	/**
	 * Method getLinkArea. Returns the link area Composite.
	 *
	 * @return The link area Composite
	 */
	public Composite getLinkArea() {
		return linkArea;
	}

	/**
	 * Method getObjectName. Returns the object name.
	 *
	 * @return The current object name string
	 */
	public String getObjectName() {
		return objectName.getText();
	}
	
	/**
	 * Method setObjectName. Sets the object name string.
	 *
	 * @param newObjectName the new object name string
	 */
	public void setObjectName(String newObjectName) {
		this.objectName.setText(newObjectName);
	}

	/**
	 * Method getQueryPath.  Returns the query path string
	 *
	 * @return The query path string
	 */
	public String getQueryPath() {
		return queryPath.getText();
	}
	
	/**
	 * Method setQueryPath. Sets the query path string.
	 *
	 * @param newPath The new query path string
	 */
	public void setQueryPath(String newPath) {
		queryPath.setText(newPath);
	}

	/**
	 * Method getSearchText.  Return the searchText control.
	 * 
	 * @return the search text
	 */
	public Text getSearchText() {
		return searchText;
	}
	
	/**
	 * Method getSearchLabel. Returns the search label string.
	 *
	 * @return The search label string.
	 */
	public String getSearchLabel() {
		return searchLabel.getText();
	}
	
	/**
	 * Method setSearchLabel. Sets the text of the search label.
	 *
	 * @param text The new search label text.
	 */
	public void setSearchLabel(String text) {
		searchLabel.setText(text);
	}

	/**
	 * Method getDetailTabLabel. Returns the detail tab's label string
	 *
	 * @return The current detail tab label string.
	 */
	public String getDetailTabLabel() {
		return detailTabItem.getText();
	}

	/**
	 * Method setDetailTabLabel.  Sets the detail tab's label string
	 *
	 * @param detailTabLabel The new detail tab label string
	 */
	public void setDetailTabLabel(String detailTabLabel) {
		this.detailTabItem.setText(detailTabLabel);
	}

	/**
	 * Method getListTabLabel.  Return the list tab's label string
	 *
	 * @return The list tab's label string.
	 */
	public String getListTabLabel() {
		return listTabItem.getText();
	}

	/**
	 * Method setListTabLabel.Set the list tab's label
	 *
	 * @param listTabLabel The new label string
	 */
	public void setListTabLabel(String listTabLabel) {
		this.listTabItem.setText(listTabLabel);
	}

	/**
	 * This method initializes scrolledComposite	
	 *
	 */
	private void createDetailScroller() {
		detailScroller = new ScrolledComposite(getDetailHolder(), SWT.H_SCROLL);
		detailScroller.setLayout(null);
		detailHolder();
		detailScroller.setContent(detailHolder);
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void detailHolder() {
		detailHolder = new Composite(detailScroller, SWT.NONE);
		detailHolder.setBounds(new org.eclipse.swt.graphics.Rectangle(0,0,50,50));
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
