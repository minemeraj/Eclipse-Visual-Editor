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
 *  $RCSfile: LayoutManagerLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
/**
 * Renderer for the layout property for a Container
 */
public class LayoutManagerLabelProvider extends LabelProvider implements INeedData {

	protected EditDomain editDomain;
	
	public String getText(Object element) {
		IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, JavaEditDomainHelper.getResourceSet(editDomain));
		if (beanProxy != null) {
			String qualifiedName = getQualifiedName(element);
			return LayoutManagerCellEditor.getDisplayName(qualifiedName);
		} else {
			return VisualMessages.getString("Layout.NullLayout"); //$NON-NLS-1$
		}
	}

	public static String getQualifiedName(Object element) {
		String qualifiedName = ""; //$NON-NLS-1$
		IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element);
		if (beanProxy != null) {
			qualifiedName = beanProxy.getTypeProxy().getTypeName();
			// With a BoxLayout, there are two possible display names...
			// based on the axis. Need to get the initialization string.
			if (qualifiedName.equals("javax.swing.BoxLayout")) { //$NON-NLS-1$
				String initString = ((IJavaInstance) element).getInitializationString();
				int lastCommaIndex = initString.lastIndexOf(","); //$NON-NLS-1$
				if (lastCommaIndex != -1) {
					int lastParenthesisIndex = initString.lastIndexOf(")"); //$NON-NLS-1$
					if (lastParenthesisIndex != -1) {
						String axisString = initString.substring(lastCommaIndex + 1, lastParenthesisIndex).trim();
						axisString.trim();
						if (axisString.equals("0") //$NON-NLS-1$
							|| axisString.equals("X_AXIS") //$NON-NLS-1$
							|| axisString.equals("javax.swing.BoxLayout.X_AXIS")) //$NON-NLS-1$
							qualifiedName = "javax.swing.BoxLayoutX_Axis"; //$NON-NLS-1$
						else if (
							axisString.equals("1") //$NON-NLS-1$
								|| axisString.equals("Y_AXIS") //$NON-NLS-1$
								|| axisString.equals("javax.swing.BoxLayout.Y_AXIS")) //$NON-NLS-1$
							qualifiedName = "javax.swing.BoxLayoutY_Axis"; //$NON-NLS-1$
					}
				}
			}
		}
		return qualifiedName;
	}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}