package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ImageIconLabelProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2004-05-18 18:15:17 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

public class ImageIconLabelProvider extends LabelProvider {

	public String getText(Object element) {

		if (element instanceof IJavaObjectInstance) {
			IJavaObjectInstance imageIcon = (IJavaObjectInstance) element;
			return ImageIconCellEditor.getPathFromInitializationAllocation(imageIcon.getAllocation());
		}
		return ""; //$NON-NLS-1$
	}

}