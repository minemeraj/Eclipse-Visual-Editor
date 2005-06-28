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
package org.eclipse.ve.internal.java.core;

import org.eclipse.osgi.util.NLS;

public final class JavaMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.core.messages";//$NON-NLS-1$

	private JavaMessages() {
		// Do not instantiate
	}

	public static String Action_Customize_Text;
	public static String Action_CustomizeJavaBean_Text;
	public static String Action_CustomizeJavaBean_ToolTipText;
	public static String Action_CustomizeXMLPersistence_Text;
	public static String CellEditor_Dimension_WidthErrorMsg_ERROR_;
	public static String CellEditor_Dimension_HeightErrorMsg_ERROR_;
	public static String CellEditor_Dimension_ErrorMsg_ERROR_;
	public static String CellEditor_Insets_TopErrorMsg_ERROR_;
	public static String CellEditor_Insets_LeftErrorMsg_ERROR_;
	public static String CellEditor_Insets_BottomErrorMsg_ERROR_;
	public static String CellEditor_Insets_RightErrorMsg_ERROR_;
	public static String CellEditor_Insets_ErrorMsg_ERROR_;
	public static String CellEditor_Point_XErrorMsg_ERROR_;
	public static String CellEditor_Point_YErrorMsg_ERROR_;
	public static String CellEditor_Point_ErrorMsg_ERROR_;
	public static String CellEditor_Rectangle_XErrorMsg_ERROR_;
	public static String CellEditor_Rectangle_YErrorMsg_ERROR_;
	public static String CellEditor_Rectangle_WidthErrorMsg_ERROR_;
	public static String CellEditor_Rectangle_HeightErrorMsg_ERROR_;
	public static String CellEditor_Rectangle_ErrorMsg_ERROR_;
	public static String LabelProvider_Enumerated_getText_ERROR_;
	public static String CellEditor_CharJava_InvalidMsg_ERROR_;
	public static String CellEditor_CharJava_ErrMsg_ERROR_;
	public static String NORTH;
	public static String EAST;
	public static String SOUTHEAST;
	public static String SOUTH;
	public static String WEST;
	public static String NORTHWEST;
	public static String NORTHEAST;
	public static String SOUTHWEST;
	public static String CENTER;
	public static String NONE;
	public static String HORIZONTAL;
	public static String BOTH;
	public static String VERTICAL;
	public static String Labelprovider_Y;
	public static String Labelprovider_X;
	public static String Labelprovider_Width;
	public static String Labelprovider_Height;
	public static String DefaultLabelProvider_Label_DottedVersion;
	public static String DefaultLabelProvider_Label_FullVersion;
	public static String LabelPolicy_text_Label;
	public static String LabelPolicy_text_JLabel;
	public static String Proxy_Class_has_Errors_ERROR_;
	public static String AddEventWizard_Title;
	public static String AddEventWizard_Tree_TreeItem_Unknown;
	public static String AddEventWizard_Label_NoDescription;
	public static String AddEventWizard_API_Msg_UseExistingListener;
	public static String AddEventWizard_API_Msg_CreateNewListener;
	public static String AddEventWizard_Extends_Label;
	public static String AddEventWizard_addEventTitle;
	public static String AddEventWizard_Implements_Label;
	public static String JavaBeanCustomizeLayoutPage_multipleSelection;
	public static String JavaBeanEventsObjectActionDelegate_MenuItem_AddEvents_Text;
	public static String AddEventWizard_compositeProperty_label_UseExistingListenerMethod;
	public static String AddEventWizard_compositeProperty_label_CreateNewListenerMethod;
	public static String ToolTipAssistFactory_ToolTip_not_available_1;
	public static String BasicAllocationProcesser_ThisTypeNotFoundOrInvalid_EXC_;
	public static String BasicAllocationProcesser_InvalidAllocationClass_EXC_;
	public static String BasicAllocationProcesser_unknown_ERROR_;
	public static String BeanProxyAdapter_NoBeanInstantiatedForSomeReason_EXC_;
	public static String BeanProxyAdapter2_OverrideProperty_ExpressionMustNotBeNull_EXC_;
	public static String BeanProxyAdapter2_NO_BEAN_DUE_TO_PREVIOUS_ERROR_;
	public static String BeanProxyAdapter2_ShouldNotBeCalled_EXC_;
	public static String AbstractRenameInstanceDialog_Preferences;
	public static String AbstractRenameInstanceDialog_Checkbox_DontAsk;
	public static String AbstractRenameInstanceDialog_ErrorMsg_NotUnique;
	public static String NameInMemberPropertyDescriptor_NameChangeDialog_Dialog_Title;
	public static String NameInMemberPropertyDescriptor_NameChangeDialog_Dialog_Message;
	public static String NameInMemberPropertyDescriptor_NameChangeDialog_Shell_Title;
	public static String BeanProxyAdapter2_RemoveOverrideProperty_ExpressionMustNotBeNull_EXC_;
	public static String CustomizePropertyNotFound_ERROR;

	static {
		NLS.initializeMessages(BUNDLE_NAME, JavaMessages.class);
	}
}