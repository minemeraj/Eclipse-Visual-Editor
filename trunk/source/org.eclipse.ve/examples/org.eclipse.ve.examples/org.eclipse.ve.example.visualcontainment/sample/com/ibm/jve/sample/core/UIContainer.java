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
 */
package com.ibm.jve.sample.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author doconnor
 */
public abstract class UIContainer {
	private Composite root = null;
	protected String name = null;
	private boolean isReal = true;

	/**
	 * 
	 */
	protected UIContainer() {			
		this(null);
	}
	
	protected UIContainer(String name){
		super();
		this.name = name;
	}
	
	protected abstract void create();
	
	public final void createContents(Composite parent){
		preCreate(parent);
		create();
		postCreate();
	}
	
	protected final void preCreate(Composite parent){
		if (parent == null) return;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 3;
		gridLayout.marginHeight = 3;
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		
		root = new Composite(parent, SWT.NULL);
		
		root.setLayout(gridLayout);
		root.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected final void postCreate(){
		root.pack();
	}
	
	protected Composite createComposite(Composite parent, int numColumns){
		Composite newComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 7;
		layout.verticalSpacing = 7;
		newComposite.setLayout(layout);
		
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = GridData.BEGINNING;
		newComposite.setLayoutData(data);
		
		return newComposite;		
	}
	
	public Composite createBaseContainer(int numColumns, boolean equalWidth) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = numColumns;
        gridLayout.marginWidth = 3;
        gridLayout.marginHeight = 3;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.makeColumnsEqualWidth = equalWidth;
        // LayoutData
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.BEGINNING;
        
        ((GridLayout)getUIContainer().getLayout()).marginHeight = 0;
        ((GridLayout)getUIContainer().getLayout()).marginWidth = 0;
       
        Composite composite = new Composite(getUIContainer(), SWT.NONE);
        composite.setLayout(gridLayout);
        composite.setLayoutData(gridData);		
        return composite;
    }
	
	public final Composite getUIContainer(){
		return root;
		
	}

	public boolean isReal() {
		return isReal;
	}

	public void setReal(boolean isReal) {
		this.isReal = isReal;
	}
	
}
