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
 *  $RCSfile: StandardPropertySheetEntry.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */


import org.eclipse.ui.views.properties.*;
import java.util.Arrays;
/**
 * This is a standard property sheet entry that doesn't support commands.
 * It uses straight IPropertySource methods to update directly.
 */

public class StandardPropertySheetEntry extends AbstractPropertySheetEntry {
/**
 * Create an entry.
 */
public StandardPropertySheetEntry(StandardPropertySheetEntry parent, IPropertySourceProvider provider) {
	super(parent, provider);
}

/**
 * Create a property sheet entry of this type.
 * Use the provider passed in.
 *
 * It is used to create children of this current entry. 
 */
protected IDescriptorPropertySheetEntry createPropertySheetEntry(IPropertySourceProvider provider) {
	return new StandardPropertySheetEntry(this, provider);
}

protected boolean isPropertySet() {
	IPropertySource[] propSources = parent.getPropertySources();
	return propSources[0].isPropertySet(fDescriptors[0].getId());
}

/**
 * Reset all of the values to thier sources.
 */
protected boolean primResetPropertyValues() {
	IPropertySource[] propSources = parent.getPropertySources();	// Get parent sources to apply against
	boolean changed = false;
	for (int i = 0; i < propSources.length; i++) {
		Object id = fDescriptors[i].getId();
		if (propSources[i].isPropertySet(id)) {
			propSources[i].resetPropertyValue(id);
			changed = true;
		}
	}
	
	((StandardPropertySheetEntry) parent).childChanged();	// Let the parent know the child changed.
	return changed;
}

/**
 * Child was changed. Apply the values at this level to propagate change
 * back to the root. If this is the root, then no apply needed.
 */
public void childChanged() {
	if (parent != null)
		primApplyValues();
}

/**
 * Apply all of the values to thier sources.
 */
protected void primApplyValues() {	
	IPropertySource[] propSources = parent.getPropertySources();	// Get parent sources to apply against
	for (int i = 0; i < propSources.length; i++) {
		Object id = fDescriptors[i].getId();		
		propSources[i].setPropertyValue(id, getEditValue(i));
	}
	
	((StandardPropertySheetEntry) parent).childChanged();	// Let the parent know the child changed.
}

/**
 * Retrieve new values and return the array.
 */
protected Object[] primGetValues() {
	IPropertySource[] propSources = parent.getPropertySources();	// Get parent sources to apply against
	Object[] newValues = new Object[propSources.length];
	for (int i = 0; i < propSources.length; i++) {
		Object id = fDescriptors[i].getId();		
		if (propSources[i].isPropertySet(id))
			newValues[i] = propSources[i].getPropertyValue(id);
	}
	return newValues;
	
}

/**
 * Given the edit value, fill in the passed in array
 * with the appropriate values. This is used to take
 * the value from the editor and to propagate it to
 * all of the values for this entry.
 */
protected void primFillValues(Object newEditValue, Object[] valuesArray) {
	Arrays.fill(valuesArray, newEditValue);
}
}
