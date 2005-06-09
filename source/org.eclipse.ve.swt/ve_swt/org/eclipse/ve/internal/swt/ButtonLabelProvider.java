/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ButtonLabelProvider.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-09 17:32:29 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.DefaultLabelProviderWithNameAndAttribute;
 
/**
 * Button Label provider. It gives a different image depending on the style, e.g. Check, radio.
 * 
 * @since 1.1.0
 */
public class ButtonLabelProvider extends DefaultLabelProviderWithNameAndAttribute {

	public Image getImage(Object element) {
		if (element instanceof IJavaObjectInstance) {
			WidgetProxyAdapter widgetProxyAdapter = 
				(WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) element);
			
			// We may not yet have a proxy adapter because this can be called by rename on drop dialog before we have adapter.
			if (widgetProxyAdapter != null) {
				int style = widgetProxyAdapter.getStyle();

				if ((style & SWT.CHECK) != 0) {
					return ToolItemLabelProvider.getCheckBox();
				} else if ((style & SWT.RADIO) != 0) { return ToolItemLabelProvider.getRadio(); }
			}
		} 
		return super.getImage(element);	
	}
	
	public boolean isLabelProperty(Object element, String property) {
		// The allocation feature change would cause the image to need refresh.
		if (JavaInstantiation.ALLOCATION.equals(property))
			return true;
		return super.isLabelProperty(element, property);
	}	
}
