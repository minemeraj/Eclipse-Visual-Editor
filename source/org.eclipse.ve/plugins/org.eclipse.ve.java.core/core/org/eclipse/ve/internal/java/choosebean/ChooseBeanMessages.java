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
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.osgi.util.NLS;

public final class ChooseBeanMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.choosebean.messages";//$NON-NLS-1$

	private ChooseBeanMessages() {
		// Do not instantiate
	}

	public static String MainDialog_title;
	public static String MainDialog_message;
	public static String ToolSelector_SelectionLogMessage;
	public static String SelectionAreaHelper_SecondaryMsg_NoSelectionMade;
	public static String SelectionAreaHelper_SecondaryMsg_NoPublicNullConstructor;
	public static String SelectionAreaHelper_SecondaryMsg_TypeNonPublic;
	public static String SelectionAreaHelper_SecondaryMsg_Unknown_ERROR_;
	public static String ChooseBeanDialog_Group_Properties_Title;
	public static String ChooseBeanDialog_Checkbox_ShowValidClasses;
	public static String ChooseBeanDialog_Group_Properties_VariableName_text;
	public static String ChooseBeanDialog_Message_AbstractType;
	public static String ChooseBeanDialog_Section_Styles;
	public static String ChooseBeanDialog_Label_BeanName;
	public static String ChooseBeanDialog_Message_NonStaticType;
	public static String AllTypesChooseBeanContributor_Name;
	public static String YesNoFilter_DependenciesMonitor_Message;

	static {
		NLS.initializeMessages(BUNDLE_NAME, ChooseBeanMessages.class);
	}
}