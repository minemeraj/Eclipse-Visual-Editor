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
 *  $RCSfile: IDescriptorPropertySheetEntry.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:44:29 $ 
 */


import org.eclipse.ui.views.properties.*;
/**
 * The interface for property sheet entries that work with IPropertyDescriptors.
 */
public interface IDescriptorPropertySheetEntry extends IPropertySheetEntry {
	/**
	 * Get the id of the descriptors that this entry is handling.
	 */
	public Object getId();

	/**
	 * Answer whether the entry is currently stale or not.
	 */
	public boolean isStale();
	
	/**
	 * Mark this and any children entries as stale.
	 * This would be called by the parent if the parent went stale,
	 * or by itself if it went stale. Stale means don't show any
	 * displayName, images, or valueAsString because the entry
	 * may no longer be valid until the refresh has been completed.
	 * This is because the refresh is queued up until later, but
	 * during the time between a refresh has been queued up
	 * and the refresh, we don't want to use the old values because
	 * they MAY be invalid now.
	 */
	public void markStale();

	/**
	 * Return the array of property sources corresponding to
	 * the values. This is used by children to get there sources
	 * of their properties.
	 */
	public IPropertySource[] getPropertySources();
	
	/**
	 * Set the list of descriptors that this entry will handle.
	 */
	public void setDescriptors(IPropertyDescriptor[] descriptors);
				
	/**
	 * Refresh the entry tree from the root down
	 */
	public void refreshFromRoot();
	
	/**
	 * Refresh the values of just this entry on down.
	 */
	public void refreshValues();
	
	/**
	 * Is an editor activated.
	 */
	public boolean isEditorActivated();
	
	/**
	 * Set whether null values should be displayed with a special string,
	 * e.g. "<null>" or should they be changed to just "" (empty string).
	 * This is useful so that users can tell which are explicitly nulls.
	 */
	public void setShowNulls(boolean showNulls);
	
	/**
	 * Set whether set values should be indicated with a special string,
	 * e.g. "*value" 
	 * This is useful so that users can tell which are explicitly set.
	 */
	public void setShowSetValues(boolean showSetValues);	
	
	/**
	 * Set the value to null if this is the active cell.
	 */
	public void setToNull();
	
	/**
	 * Are nulls invalid to be set. True if cannot set to null.
	 */
	public boolean areNullsInvalid();
	
	/**
	 * Set whether to show read only entries.
	 */
	public void setShowReadOnly(boolean showReadOnly);
	
	/**
	 * Get the data. This is an arbitrary piece of data stored in the
	 * property sheet entry. If it is null, then it will go to the
	 * parent, and keep going until it gets to the parent.
	 */
	public Object getData();
	
	/**
	 * Set the arbitrary piece of data into the entry.
	 */
	public void setData(Object data);
	
	/**
	 * Get the parent property sheet entry.
	 */
	public IDescriptorPropertySheetEntry  getParent();
	
	/**
	 * Answer the values that were set into the entry.
	 */
	public Object[] getValues();
	
	/**
	 * Get the sort display name, not the adorned property sheet entry displayname. This is so
	 * that sorting (using EToolsPropertySheetSorter) will be done on this instead of the adorned 
	 * displayname. For example, we may put '>' in front to indicate a changed property, but
	 * we don't want the '>' to participate in the sort.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public String getSortDisplayName();
				
}
