/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FrameSelectorDialogArea.java,v $
 *  $Revision: 1.4 $  $Date: 2006-05-17 20:14:58 $ 
 */

package org.eclipse.ve.internal.jfc.core;

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
 * Frame Selector Dialog area
 * 
 * @since 1.2.0
 */
class FrameSelectorDialogArea extends Composite {

	private Label label = null;
	private List list = null;
	private ListViewer listViewer = null;
	private final EditDomain domain;
	private Object selectedFrame;

	public FrameSelectorDialogArea(Composite parent, int style, EditDomain domain) {
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
		label.setText(JFCMessages.FrameSelectorDialogArea_OverviewLabel_Msg);
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
				selectedFrame = ((IStructuredSelection) event.getSelection()).getFirstElement();
			}
		
		});
		list.setLayoutData(gridData);
		this.setLayout(new GridLayout());
	}
	
	public void setFrames(java.util.List frames) {
		listViewer.setInput(frames.toArray());
		listViewer.setSelection(new StructuredSelection(frames.get(0)),true);
	}
	
	public Object getSelectedFrame() {
		return selectedFrame;
	}

}
