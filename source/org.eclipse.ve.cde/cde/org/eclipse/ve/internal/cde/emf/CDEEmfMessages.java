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
package org.eclipse.ve.internal.cde.emf;

import org.eclipse.osgi.util.NLS;

public final class CDEEmfMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.cde.emf.messages";//$NON-NLS-1$

	private CDEEmfMessages() {
		// Do not instantiate
	}

	public static String Resource_changed_please_close_editor_message_WARN_;
	public static String Warning_message_dialog_title_WARN_;
	public static String Line_number;
	public static String List_of_errors;
	public static String Error_text_message_ERROR_;
	public static String Warning_exception_bad_type_EXC_;
	public static String Warning_exception_bad_value_EXC_;
	public static String Warning_exception_missing_class_EXC_;
	public static String Warning_exception_missing_feature_EXC_;
	public static String Warning_exception_missing_namespace_EXC_;
	public static String Warning_exception_missing_package_EXC_;
	public static String Warning_exception_bad_reference_EXC_;
	public static String Warning_exception_error_EXC_;
	public static String ClassDecoratorIterator_UnsupportedRemove_Object__EXC_;
	public static String FILE_DELETED_TITLE_UI_;
	public static String FILE_DELETED_WITHOUT_SAVE_INFO_;
	public static String SAVE_BUTTON_UI_;
	public static String CLOSE_BUTTON_UI;
	public static String DefaultLabelProvider_getText;
	public static String DefaultTreeEditPart_getText_NoLabelProvider;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CDEEmfMessages.class);
	}
}