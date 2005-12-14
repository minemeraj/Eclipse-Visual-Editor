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
package org.eclipse.ve.internal.cde.core;

import org.eclipse.osgi.util.NLS;

public final class CDEMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.cde.core.messages";//$NON-NLS-1$

	private CDEMessages() {
		// Do not instantiate
	}

	public static String CustomizeLayoutWindow_title;
	public static String CustomizeLayoutWindow_layoutTabTitle;
	public static String CustomizeLayoutWindow_layoutTabToolTip;
	public static String CustomizeLayoutWindow_componentTabTitle;
	public static String CustomizeLayoutWindow_componentTabToolTip;
	public static String CustomizeLayoutWindow_noLayoutText;
	public static String CustomizeLayoutWindow_noComponentText;
	public static String CustomizeLayoutWindowAction_label;
	public static String CustomizeLayoutWindowAction_tooltip_show;
	public static String CustomizeLayoutWindowAction_tooltip_hide;
	public static String AlignmentAction_left_label;
	public static String AlignmentAction_left_tooltip;
	public static String AlignmentAction_center_label;
	public static String AlignmentAction_center_tooltip;
	public static String AlignmentAction_right_label;
	public static String AlignmentAction_right_tooltip;
	public static String AlignmentAction_top_label;
	public static String AlignmentAction_top_tooltip;
	public static String AlignmentAction_middle_label;
	public static String AlignmentAction_middle_tooltip;
	public static String AlignmentAction_bottom_label;
	public static String AlignmentAction_bottom_tooltip;
	public static String AlignmentAction_width_label;
	public static String AlignmentAction_width_tooltip;
	public static String AlignmentAction_height_label;
	public static String AlignmentAction_height_tooltip;
	public static String DistributeAction_horizontal_label;
	public static String DistributeAction_horizontal_tooltip;
	public static String DistributeAction_vertical_label;
	public static String DistributeAction_vertical_tooltip;
	public static String GridPropertiesAction_label;
	public static String GridPropertiesAction_tooltip;
	public static String GridPropertiesAction_image;
	public static String ShowDistributeBoxAction_label;
	public static String ShowDistributeBoxAction_tooltip_show;
	public static String ShowDistributeBoxAction_tooltip_hide;
	public static String RestorePreferredSizeAction_label;
	public static String RestorePreferredSizeAction_tooltip;
	public static String AlignmentXYGridPropertiesPage_title;
	public static String AlignmentXYGridPropertiesPage_width;
	public static String AlignmentXYGridPropertiesPage_height;
	public static String AlignmentXYGridPropertiesPage_margin;
	public static String AlignmentXYGridPropertiesPage_Height_Must_Be_Larger_Than_One;
	public static String AlignmentXYGridPropertiesPage_Height_Must_Be_Integer;
	public static String AlignmentXYGridPropertiesPage_Width_Must_Be_Larger_Than_One;
	public static String AlignmentXYGridPropertiesPage_Width_Must_Be_Integer;
	public static String AlignmentXYGridPropertiesPage_Margin_Must_Be_Positive;
	public static String AlignmentXYGridPropertiesPage_Margin_Must_Be_Integer;
	public static String AlignmentXYGridPropertiesPage_Keep_Width_Height_Same;
	public static String AlignmentXYGridPropertiesPage_Show_Grid;
	public static String AlignmentXYGridPropertiesPage_Snap_To_Grid;
	public static String ShowGridAction_label;
	public static String ShowGridAction_tooltip;
	public static String ShowGridAction_hide_label;
	public static String ShowGridAction_hide_tooltip;
	public static String ShowGridAction_image;
	public static String SnapToGridAction_label;
	public static String SnapToGridAction_tooltip;
	public static String SnapToGridAction_image;
	public static String ZoomAction_label;
	public static String ZoomAction_tooltip;
	public static String ZoomAction_image;
	public static String ZoomInAction_label;
	public static String ZoomInAction_tooltip;
	public static String ZoomInAction_image;
	public static String ZoomOutAction_label;
	public static String ZoomOutAction_tooltip;
	public static String ZoomOutAction_image;
	public static String ZoomHelperDialog_label;
	public static String ZoomHelperDialog_PERCENT;
	public static String Object_noinstantiate_EXC_;
	public static String NotInstance_EXC_;
	public static String PropertyDescriptor_NameInComposition_Default;
	public static String PropertyDescriptor_NameInComposition_NonUnique_INFO_;
	public static String PropertyDescriptor_NameInComposition_DisplayName;
	public static String NOT_FILE_INPUT_ERROR_;
	public static String SAVING_UI_;
	public static String SAVE_FAIL_ERROR_;
	public static String SAVING_AS_UI_;
	public static String ERROR_TITLE_UI_;
	public static String Exception_msg;
	public static String PropertyError_msg;
	public static String AlignmentXYComponentPage_multipleSelection;
	public static String ModelChangeController_EditorBusyAndCannotChangeNow;
	public static String ModelChangeController_EditorCannotBeChangedNow;
	public static String XYLayoutEditPolicy_CursorFeedback_X_Y;
	public static String CustomSashForm_Restore;
	public static String CustomSashForm_Maximize;
	public static String ActionContributor_Status_Creating_label_;	

	static {
		NLS.initializeMessages(BUNDLE_NAME, CDEMessages.class);
	}

	public static String CustomizeLayoutWindowAction_Action_NoLayouts;
	public static String ActionBarActionEditPart_Action_Performed_Msg;
	public static String NoParentContainmentHandler_StopRequest_DropNotAllowed_Msg;
}