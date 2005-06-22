/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.propertysheet;

import org.eclipse.osgi.util.NLS;

public final class PropertysheetMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.propertysheet.messages";//$NON-NLS-1$

	private PropertysheetMessages() {
		// Do not instantiate
	}

	public static String AbstractPropertySheetEntry_DisplayName_StaleEntry;
	public static String AbstractPropertySheetEntry_DisplayName_Error;
	public static String AbstractPropertySheetEntry_ValueAsString_StaleEntry;
	public static String AbstractPropertySheetEntry_ValueAsString_Error;
	public static String display_true;
	public static String display_false;
	public static String display_null;
	public static String bad_bool_WARN_;
	public static String minmax_bad_WARN_;
	public static String min_bad_WARN_;
	public static String max_bad_WARN_;
	public static String not_number_WARN_;
	public static String not_integer_WARN_;
	public static String not_string_WARN_;
	public static String null_invalid_WARN_;
	public static String maxvalue;
	public static String minvalue;
	public static String not_valid_WARN_;
	public static String apply_value;
	public static String reset_value;
	public static String show_nulls_label;
	public static String show_nulls_show_tooltip;
	public static String show_nulls_hide_tooltip;
	public static String show_read_only_label;
	public static String show_read_only_show_tooltip;
	public static String show_read_only_hide_tooltip;
	public static String show_set_values_label;
	public static String show_set_values_show_tooltip;
	public static String show_set_values_hide_tooltip;
	public static String set_nulls_label;
	public static String set_nulls_tooltip;

	static {
		NLS.initializeMessages(BUNDLE_NAME, PropertysheetMessages.class);
	}
}