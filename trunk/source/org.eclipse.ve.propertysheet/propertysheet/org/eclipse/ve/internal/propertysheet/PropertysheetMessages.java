/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: PropertysheetMessages.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:33:36 $ 
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertysheetMessages {

	private static final String BUNDLE_NAME =
		"org.eclipse.ve.internal.propertysheet.messages"; //$NON-NLS-1$
	public static final String
		DISPLAY_TRUE = "display_true", //$NON-NLS-1$
		DISPLAY_FALSE = "display_false", //$NON-NLS-1$
		DISPLAY_NULL = "display_null", //$NON-NLS-1$
		NOT_BOOL = "bad_bool_WARN_", //$NON-NLS-1$

		MINMAX_BAD = "minmax_bad_WARN_", //$NON-NLS-1$
		MIN_BAD = "min_bad_WARN_", //$NON-NLS-1$
		MAX_BAD = "max_bad_WARN_", //$NON-NLS-1$
		NOT_NUMBER = "not_number_WARN_", //$NON-NLS-1$
		
		NOT_INTEGER = "not_integer_WARN_", //$NON-NLS-1$
		
		NOT_STRING = "not_string_WARN_", //$NON-NLS-1$
		
		NULL_INVALID = "null_invalid_WARN_", //$NON-NLS-1$
		
		NOT_VALID = "not_valid_WARN_", //$NON-NLS-1$
		
		APPLY_VALUE = "apply_value", //$NON-NLS-1$
		RESET_VALUE = "reset_value", //$NON-NLS-1$
		
		SHOW_NULLS_LABEL = "show_nulls.label", //$NON-NLS-1$
		SHOW_NULLS_SHOW_TOOLTIP = "show_nulls.show.tooltip", //$NON-NLS-1$
		SHOW_NULLS_HIDE_TOOLTIP = "show_nulls.hide.tooltip", //$NON-NLS-1$
		
		SHOW_READ_ONLY_LABEL = "show_read_only.label", //$NON-NLS-1$
		SHOW_READ_ONLY_SHOW_TOOLTIP = "show_read_only_show.tooltip", //$NON-NLS-1$
		SHOW_READ_ONLY_HIDE_TOOLTIP = "show_read_only_hide.tooltip", //$NON-NLS-1$
		

		SHOW_SET_VALUES_LABEL = "show_set_values.label", //$NON-NLS-1$
		SHOW_SET_VALUES_SHOW_TOOLTIP = "show_set_values.show.tooltip", //$NON-NLS-1$
		SHOW_SET_VALUES_HIDE_TOOLTIP = "show_set_values.hide.tooltip", //$NON-NLS-1$
		
		SET_NULLS_LABEL = "set_nulls.label", //$NON-NLS-1$
		SET_NULLS_TOOLTIP = "set_nulls.tooltip"; //$NON-NLS-1$
	
	private static final ResourceBundle RESOURCE_BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);

	private PropertysheetMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
