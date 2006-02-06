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
/*
 *  $RCSfile: FactorySelectorDialogArea.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:38 $ 
 */

package org.eclipse.ve.internal.java.core;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * Factory Selector Dialog area
 * 
 * @since 1.2.0
 */
class FactorySelectorDialogArea extends Composite {

	private Label label = null;
	private List list = null;
	private ListViewer listViewer = null;
	private final EditDomain domain;
	private Object selectedFactory;

	public FactorySelectorDialogArea(Composite parent, int style, EditDomain domain) {
		super(parent, style);
		this.domain = domain;
		initialize();
	}

	private void initialize() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		label = new Label(this, SWT.NONE);
		label.setText("Select the factory for the new component:");
		list = new List(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		listViewer = new ListViewer(list);
		listViewer.setContentProvider(new IStructuredContentProvider() {
		
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
			}
		
			public void dispose() {
			}
		
			public Object[] getElements(Object inputElement) {
				return (Object[]) inputElement;
			}
		
		});
		listViewer.setLabelProvider(new ILabelProvider() {
		
			public void removeListener(ILabelProviderListener listener) {
			}
		
			public boolean isLabelProperty(Object element, String property) {
				return true;
			}
		
			public void dispose() {
			}
		
			public void addListener(ILabelProviderListener listener) {
			}

			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				IJavaObjectInstance jo = (IJavaObjectInstance) element;
				ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(domain).getLabelProvider(jo.eClass());
				if ( labelProvider != null ) {
					return labelProvider.getText(element);	
				} else { 
					// If no label provider exists use the toString of the target VM JavaBean itself
					return BeanProxyUtilities.getBeanProxy(jo).toBeanString(); 
				}
			}
		});
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selectedFactory = ((IStructuredSelection) event.getSelection()).getFirstElement();
			}
		
		});
		list.setLayoutData(gridData);
		this.setLayout(new GridLayout());
	}
	
	public void setFactories(java.util.List factories) {
		listViewer.setInput(factories.toArray());
		listViewer.setSelection(new StructuredSelection(factories.get(0)),true);
	}
	
	public Object getSelectedFactory() {
		return selectedFactory;
	}

}
