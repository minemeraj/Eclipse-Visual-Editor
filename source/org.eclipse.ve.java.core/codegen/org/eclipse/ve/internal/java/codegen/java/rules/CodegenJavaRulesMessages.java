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
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.osgi.util.NLS;

public final class CodegenJavaRulesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.codegen.java.rules.messages";//$NON-NLS-1$

	private CodegenJavaRulesMessages() {
		// Do not instantiate
	}

	public static String VCEPrefContributor_MethodName;
	public static String VCEPrefContributor_Dialog_Title;
	public static String VCEPrefContributor_Add;
	public static String VCEPrefContributor_Edit;
	public static String VCEPrefContributor_Remove;
	public static String VCEPrefContributor_InitMethodsList_Text;
	public static String VCEPrefContributor_Tab_Preference_Text;
	public static String VCEPrefContributor_Tab_Templates_Text;
	public static String VCEPrefContributor_Table_Templates_TableColumn_Template;
	public static String VCEPrefContributor_Table_Templates_TableColumn_Sample;
	public static String VCEPrefContributor_TemplatePreview_Label_Text;
	public static String VCEPrefContributor_TemplatePreview_Edit_Button_Text;
	public static String VCEPrefContributor_Check_UseFormatter_Text;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CodegenJavaRulesMessages.class);
	}
}