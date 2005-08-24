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
 *  $RCSfile: IEToolsPropertyDescriptor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */



import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * This is an extension of IPropertyDescriptor which provides added
 * accessers for the AbstractPropertySheetEntry over the default
 * property descriptor.
 */
public interface IEToolsPropertyDescriptor extends IPropertyDescriptor {
	
	/**
	 * Should an entry for this property be expandable.
	 * In other words, if the first of the compatible descriptors 
	 * for a property sheet entry return true to this method,
	 * then children (sub-entries) to this entry will be
	 * created. The plus sign will be placed on the entry.
	 * Setting false is useful when the cell editor does complete 
	 * customization of the property and it is not desired
	 * to have sub-entries further customize it.
	 *
	 * Descriptors that are not IEToolsPropertyDescriptors will
	 * be treated as if this accesser returns true. This will
	 * preserve the default behavior.
	 */
	public boolean isExpandable();
	
	/**
	 * Should an entry for this property allow nulls.
	 * In other words, if the first of the compatible descriptors 
	 * for a property sheet entry return true to this method,
	 * then null settings won't be allowed. It it is false,
	 * then the cell editor and any applied validators will determine
	 * if null is invalid or not.
	 *
	 * Descriptors that are not IEToolsPropertyDescriptors will
	 * be treated as if this accesser returns true. This will
	 * preserve the default behavior.
	 */
	public boolean areNullsInvalid();	
	
	/**
	 *Is this property read only and not allowing editing
	 *In other words, if the first of the compatible descriptors
	 *for a property sheet entry return true to this method,
	 *and the property sheet doesn't want to show read only properties,
	 *No new entry is created and displayed for this property in the property sheet.
	 */
	public boolean isReadOnly();

}
