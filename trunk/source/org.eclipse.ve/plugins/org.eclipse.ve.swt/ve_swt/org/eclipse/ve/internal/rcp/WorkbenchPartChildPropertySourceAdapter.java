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
 *  $RCSfile: WorkbenchPartChildPropertySourceAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-14 17:33:41 $ 
 */
package org.eclipse.ve.internal.rcp;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This is a property source adapter for the only child of a ViewPart which is the 'parent' composite. 
 * We don't allow changes to this composite so we'll just return an empty array of IPropertyDescriptors.
 * 
 * @since 1.2
 */
public class WorkbenchPartChildPropertySourceAdapter implements IPropertySource {

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {};
	}

	public Object getPropertyValue(Object id) {
		return null;
	}

	public boolean isPropertySet(Object id) {
		return true;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}

}
