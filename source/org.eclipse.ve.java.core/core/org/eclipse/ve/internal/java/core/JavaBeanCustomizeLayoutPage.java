/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaBeanCustomizeLayoutPage.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-09 14:09:17 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.CustomizeLayoutPage;
import org.eclipse.ve.internal.cde.core.EditDomain;
 

/**
 * @since 1.0.0
 *
 */
public abstract class JavaBeanCustomizeLayoutPage extends CustomizeLayoutPage {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getLabelForSelection(org.eclipse.jface.viewers.ISelection)
	 */
	protected String getLabelForSelection(ISelection newSelection) {
		if (newSelection != null && newSelection instanceof IStructuredSelection) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			if (editparts.size() == 0) {
				return null;
			} else if (editparts.size() > 1) {
				return JavaMessages.getString("JavaBeanCustomizeLayoutPage.multipleSelection"); //$NON-NLS-1$
			} else {
				EditPart editPart = (EditPart)editparts.get(0);
				if (editPart.getModel() instanceof IJavaObjectInstance) {
					IJavaObjectInstance javaBean = (IJavaObjectInstance)editPart.getModel();
					if ( javaBean != null ) {
						return BeanUtilities.getLabel(javaBean, EditDomain.getEditDomain(editPart));
					}
				}
			}
		}
		return null;
	}
}
