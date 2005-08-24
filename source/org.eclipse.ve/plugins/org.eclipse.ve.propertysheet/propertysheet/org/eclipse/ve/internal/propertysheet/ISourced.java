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
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: ISourced.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * Objects that implement this interface want to
 * know their sources. These are sometimes needed to
 * perform validations or determining valid selections
 * in a combobox depending on the sources.
 * 
 * It will pass in the array of sources, array of propertySources (associated w/sources), array of descriptors (associated with each property source) 
 *
 * Currently this is checked for on CellEditors and CellEditorValidators.
 */
public interface ISourced {
	/**
	 * Set the sources. The sources will be the editable value.
	 * In other words, IPropertySources will be converted to
	 * editable value before being set.
	 */
	public void setSources(Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors);
}
