/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: BooleanJavaLabelProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:45 $ 
 */

import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBooleanBeanProxy;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.propertysheet.BooleanLabelProvider;
import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * This is a generic cell renderer that can format boolean correctly
 * It goes to the bean proxy to get the boolean value, however because it casts to the
 * the specifc type it doesn't actually trip to the target VM as the value of the boolean
 * is stored in the IDE's implementation of IBeanProxy
 */
public class BooleanJavaLabelProvider extends BooleanLabelProvider implements INeedData {

	private static Image TRUE_IMAGE;
	private static Image FALSE_IMAGE;

	protected EditDomain editDomain;

	public String getText(Object element) {

		if (element instanceof IJavaInstance) {
			IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, JavaEditDomainHelper.getResourceSet(editDomain));
			if (proxy == null)
				return ""; // It shouldn't be null. //$NON-NLS-1$
			else if (proxy instanceof IBooleanBeanProxy)
				return super.getText(((IBooleanBeanProxy) proxy).getBooleanValue());
			else
				return proxy.toBeanString();
		} else
			return super.getText(element);
	}
	public Image getImage(Object element) {
		// Too many problems with images now - just do nothing right now
		if (true)
			return null;
		if (element instanceof IJavaInstance) {
			IBooleanBeanProxy proxy =
				(IBooleanBeanProxy) BeanProxyUtilities.getBeanProxy(
					(IJavaInstance) element,
					JavaEditDomainHelper.getResourceSet(editDomain));
			if (proxy.getBooleanValue().booleanValue()) {
				return getTrueImage();
			} else {
				return getFalseImage();
			}
		}
		return null;
	}
	public static Image getTrueImage() {
		if (TRUE_IMAGE == null) {
			TRUE_IMAGE = getBooleanImage(true);
		}
		return TRUE_IMAGE;
	}
	public static Image getFalseImage() {
		if (FALSE_IMAGE == null) {
			FALSE_IMAGE = getBooleanImage(false);
		}
		return FALSE_IMAGE;
	}
	private static Image getBooleanImage(boolean aBool) {

		String url = null;
		if (aBool) {
			url = "icons/booleantrue.gif"; //$NON-NLS-1$
		} else {
			url = "icons/booleanfalse.gif"; //$NON-NLS-1$
		}
		return CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), url);
	}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}
