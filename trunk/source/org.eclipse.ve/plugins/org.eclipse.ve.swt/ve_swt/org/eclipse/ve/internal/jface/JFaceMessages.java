/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jface;

import org.eclipse.osgi.util.NLS;

public final class JFaceMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.jface.messages";//$NON-NLS-1$

	private JFaceMessages() {
		// Do not instantiate
	}

	public static String RCPViewCreation_RCP_Project_ERROR_;
	public static String Correct_Empty_Parent_Msg;

	static {
		NLS.initializeMessages(BUNDLE_NAME, JFaceMessages.class);
	}

	public static String ComboViewerEditPartContributorFactory_WithViewerAttached_Title;
	public static String ComboViewerEditPartContributorFactory_TooltipLabel_SelectViewer_Msg;
	public static String ComboViewerEditPartContributorFactory_TooltipLabel_ConvertToViewer_Msg;
	public static String ComboViewerEditPartContributorFactory_Button_CreateViewer_Text;
	public static String TableViewerEditPartContributorFactory_WithTableViewer_Msg;
	public static String TableViewerEditPartContributorFactory_TooltipLabel_SelectViewer_Msg;
	public static String TableViewerEditPartContributorFactory_TooltipLabel_ConvertToViewer_Msg;
	public static String TableViewerEditPartContributorFactory_Button_AttachViewer_Text;
	public static String TreeViewerEditPartContributorFactory_WithViewer_Msg;
	public static String TreeViewerEditPartContributorFactory_TooltipLabel_SelectViewer_Msg;
	public static String TreeViewerEditPartContributorFactory_TooltipLabel_ConvertToViewer_Msg;
	public static String TreeViewerEditPartContributorFactory_Button_AttachViewer_Text;
	public static String ViewerContainmentHandler_StopRequest_InvalidParentForViewer;
	public static String ListViewerEditPartContributorFactory_WithViewer_Msg;
	public static String ListViewerEditPartContributorFactory_TooltipLabel_SelectViewer_Msg;
	public static String ListViewerEditPartContributorFactory_TooltipLabel_ConvertToViewer_Msg;
	public static String ListViewerEditPartContributorFactory_Button_AttachViewer_Text;
}
