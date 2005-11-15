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
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.osgi.util.NLS;

public final class CodegenEditorPartMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.codegen.editorpart.messages";//$NON-NLS-1$

	private CodegenEditorPartMessages() {
		// Do not instantiate
	}

	public static String BeansList_DefaultPageMessage;
	public static String JavaVisualEditorPart_DesignPart;
	public static String JavaVisualEditorPart_SourcePart;
	public static String JavaVisualEditorPart_SetupJVE;
	public static String JavaVisualEditorPart_26;
	public static String JavaVisualEditorPart_27;
	public static String JavaVisualEditorPart_CleanupJVE;
	public static String JavaVisualEditorPart_CreateRemoteVMForJVE;
	public static String JavaVisualEditorVMController_InactiveVMCheckJob_Text;
	public static String JavaVisualEditorPart_InitializingModel;
	public static String JavaVisualEditorPart_ReleasingModel;
	public static String JVE_STATUS_MSG_INSYNC;
	public static String JVE_STATUS_MSG_NOT_IN_SYNC;
	public static String JVE_STATUS_MSG_SYNCHRONIZING;
	public static String JVE_STATUS_MSG_LOAD;
	public static String JVE_STATUS_MSG_PAUSE;
	public static String JVE_STATUS_MSG_RELOAD;
	public static String JVE_STATUS_MSG_PAUSED;
	public static String JVE_STATUS_BAR_MSG_PARSE_ERROR_;
	public static String JVE_STATUS_MSG_ERROR;
	public static String ShowOverviewAction_label;
	public static String ShowOverviewAction_labelOutline;
	public static String ShowOverviewAction_tooltip;
	public static String ShowOverviewAction_tooltipOutline;
	public static String ShowOverviewAction_image;
	public static String Action_Delete_label;
	public static String Action_Delete_tooltip;
	public static String RenameJavaBeanObjectActionDelegate_Shell_Text;
	public static String RenameJavaBeanObjectActionDelegate_FieldNaming_Title;
	public static String RenameJavaBeanObjectActionDelegate_Message;
	public static String JavaVisualEditor_notJavaProject_EXC_;
	public static String JavaVisualEditor_ErrorTitle;
	public static String JavaVisualEditor_ErrorDesc;
	public static String JavaVisualEditor_NoEvents;
	public static String JavaVisualEditor_ShowEvents;
	public static String JavaVisualEditor_ExpertEvents;
	public static String FieldNameValidator_InvalidVariableName_INFO_;
	public static String FieldNameValidator_VariableNameExists_INFO_;
	public static String CollapseAllAction_label;
	public static String CollapseAllAction_toolTip;
	public static String Action_Cut_Label;
	public static String Action_Copy_Label;
	public static String Action_Paste_Label;
	public static String OpenWithAction_label;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CodegenEditorPartMessages.class);
	}

	public static String JavaVisualEditorReloadActionController_PARSE_ERROR;
	public static String JavaVisualEditorReloadActionController_PAUSED;
	public static String JavaVisualEditorOutlinePage_LinkSelectionSourceEditor_Action_Label;
	public static String JavaSourceTranslator_17;
	public static String JavaSourceTranslator_18;
}