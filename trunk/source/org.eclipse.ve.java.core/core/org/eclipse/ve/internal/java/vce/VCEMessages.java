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
package org.eclipse.ve.internal.java.vce;

import org.eclipse.osgi.util.NLS;

public final class VCEMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.vce.messages";//$NON-NLS-1$

	private VCEMessages() {
		// Do not instantiate
	}

	public static String PreferencePage_LookAndFeel_Default;
	public static String PreferencePage_EditorGroup_Title;
	public static String PreferencePage_EditorGroup_SplitPaneOrientation;
	public static String PreferencePage_EditorGroup_UseNoteBook;
	public static String PreferencePage_OpenView_Properties;
	public static String PreferencePage_OpenView_JavaBeans;
	public static String PreferencePage_LookAndFeel_Title;
	public static String PreferencePage_ShowLiveWindow;
	public static String PreferencePage_ShowXMLText;
	public static String PreferencePage_ShowClipboardText;
	public static String PreferencePage_ShowGridWhenSelected;
	public static String nameInComposition_displayName;
	public static String PreferencePage_NewExpressionCommentPrompt;
	public static String PreferencePage_GenerateTryCatchBlock;
	public static String PreferencePage_CodeGen_ParsingGeneration_Style_1;
	public static String PreferencePage_CodeGen_Source_Synchronization_Delay;
	public static String PreferencePage_CodeGen_SourceToJavaBeans;
	public static String PreferencePage_CodeGen_Error_DelayTimeMinimum_ERROR_;
	public static String PreferencePage_CodeGen_Error_DelayTimeMustBeInteger_ERROR_;
	public static String LookAndFeelDialog_Shell_Text;
	public static String LookAndFeelDialog_LookAndFeel_Name;
	public static String LookAndFeelDialog_LookAndFeel_Class;
	public static String VCEPreferencePage_Tab_Appearance_Text;
	public static String VCEPreferencePage_Tab_Appearance_Table_LookAndFeel_Name;
	public static String VCEPreferencePage_Checkbox_PromptNameOnCreation_Text;
	public static String VCEPreferencePage_Tab_Appearance_Table_LookAndFeel_Class;
	public static String VCEPreferencePage_Tab_Appearance_Button_New;
	public static String VCEPreferencePage_Tab_Appearance_Button_Edit;
	public static String VCEPreferencePage_Tab_Appearance_Button_Remove;
	public static String VCEPreferencePage_Tab_CodeGeneration_Text;
	public static String VCEPreferencePage_Tab_Styles_Text;
	public static String VCEPreferencePage_Tab_Styles_Table_Styles;
	public static String VCEPreferencePage_NoverifyCheckbox_text;
	public static String VCEPreferencePage_LinkToResources;

	static {
		NLS.initializeMessages(BUNDLE_NAME, VCEMessages.class);
	}

	public static String VCEPreferencePage_XYGridSpacing;
	public static String VCEPreferencePage_CaretSelectUI_Checkbox_Label;
}