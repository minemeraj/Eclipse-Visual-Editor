package org.eclipse.ve.internal.java.codegen.wizards;

/*******************************************************************************
 * Copyright (c) 2004 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - 1.0 implementation
 *******************************************************************************/

import org.eclipse.jface.viewers.*;

/**
 * @author pmuldoon
 *
 * Content Provider for the New Visual Class 
 * Wizard TreeViewer
 */

public class StyleTreeContentProvider implements ITreeContentProvider {

    protected TreeViewer viewer;

    public StyleTreeContentProvider() {
        super();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
     *      java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
        // Only Styles can have children
        if (parentElement instanceof CategoryModel) {
            CategoryModel parentStyle = (CategoryModel) parentElement;
            return parentStyle.getChildren();
        }
        return new Object[0];
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        if (element instanceof VisualElementModel) {
            return ((VisualElementModel) element).getParent();
        } else if (element instanceof CategoryModel)
            return ((CategoryModel) element).getParent();
        
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        // Only Styles can have children
        if (element instanceof CategoryModel)
            if (getChildren(element).length > 0)
                return true;
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }
    
}
