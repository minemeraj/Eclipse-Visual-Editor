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
 * Created on Apr 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.jve.sample.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author doconnor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class UIPartBase implements FocusListener {
	private Composite parent = null;
	private Composite container = null;
	private Control[] controls = null;
	
	private Label label = null;
	private String title = null;
	
	protected UIPartBase(Composite parent){
		super();
		this.parent = parent;
	}
	
	protected UIPartBase(Composite parent, String title){
		this(parent);
		this.title = title;
	}
	
	
	protected void createLabel(){
		if(this.title != null){
			label = new Label(getContainer(), SWT.NULL);
			label.setText(this.title);
		}
	}
	
	/**
	 * Initialize a container widget.
	 */
	protected void initializeContainer(int numColumns) {
		initializeContainer(numColumns, false);
	}
	protected void initializeContainer(int numColumns, boolean fill) {
		if(container != null){
			return;
		}
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.verticalSpacing = 3;
		
		GridData gridData = null;
		if(fill){
			gridData = new GridData(GridData.FILL_BOTH);
		}
		else {
			gridData = new GridData(
				GridData.GRAB_HORIZONTAL |
				GridData.HORIZONTAL_ALIGN_FILL |
				GridData.VERTICAL_ALIGN_CENTER);
		}
		
		container = new Composite(parent, SWT.NULL);
		container.setLayout(gridLayout);
		container.setLayoutData(gridData);
	}
	
		
	public void focusGained(FocusEvent e) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		//Do Something
	}

	/**
	 * @return Returns the container.
	 * 
	 * NOTE: This is the getter that we are concerned with. It is the 
	 * contents of this Composite that we want to visualize as 
	 * the "part"
	 */
	public Composite getContainer() {
		return container;
	}
	/**
	 * @param container The container to set.
	 */
	public void setContainer(Composite container) {
		this.container = container;
	}
	/**
	 * @return Returns the parent.
	 */
	public Composite getParent() {
		return parent;
	}
	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Composite parent) {
		this.parent = parent;
	}
	/**
	 * @return Returns the controls.
	 */
	public Control[] getControls() {
		return controls;
	}
	/**
	 * @param controls The controls to set.
	 */
	public void setControls(Control[] controls) {
		this.controls = controls;
	}
	/**
	 * @return Returns the label.
	 */
	public Label getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(Label label) {
		this.label = label;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
