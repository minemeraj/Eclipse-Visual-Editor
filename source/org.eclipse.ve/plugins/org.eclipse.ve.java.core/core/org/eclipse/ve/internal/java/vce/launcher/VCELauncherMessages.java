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
package org.eclipse.ve.internal.java.vce.launcher;

import org.eclipse.osgi.util.NLS;

public final class VCELauncherMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.vce.launcher.messages";//$NON-NLS-1$

	private VCELauncherMessages() {
		// Do not instantiate
	}

	public static String JavaBeansFinder_SearchMessage;
	public static String AppletParms_name;
	public static String AppletParms_value;
	public static String AppletParms_novalue_WARN_;
	public static String AppletParms_new;
	public static String AppletParms_nameindex;
	public static String AppletParms_valueindex;
	public static String AppletParms_remove;
	public static String AppletParms_title;
	public static String BeanTab_project_label;
	public static String BeanTab_browse_label;
	public static String BeanTab_bean_label;
	public static String BeanTab_search_label;
	public static String BeanTab_externaljars_label;
	public static String BeanTab_externaljars_tooltip;
	public static String BeanTab_lookfeel_label;
	public static String BeanTab_locale_label;
	public static String BeanTab_default_label;
	public static String BeanTab_localesuggest_label;
	public static String BeanTab_size_label;
	public static String BeanTab_swtsize_label;
	public static String BeanTab_pack_label;
	public static String BeanTab_badlocale_msg_ERROR_;
	public static String BeanTab_search_title;
	public static String BeanTab_search_msg;
	public static String BeanTab_project_search_title;
	public static String BeanTab_project_search_msg;
	public static String BeanTab_badproject_msg_ERROR_;
	public static String BeanTab_nobean_msg_ERROR_;
	public static String BeanTab_badbean_msg_ERROR_;
	public static String BeanTab_title;
	public static String BeanTab_swingtab_text;
	public static String BeanTab_swingtab_tooltip;
	public static String BeanTab_swttab_text;
	public static String BeanTab_swttab_tooltip;
	public static String Launcher_jreerror_msg_ERROR_;
	public static String LaunchConfigurationDelegate_Msg_Launching;
	public static String ErrorDialog_Title;
	public static String Shortcut_ErrDlg_LaunchFailed_Title;
	public static String Shortcut_ErrDlg_LaunchFailed_Msg_NoBeanFound_ERROR_;
	public static String Shortcut_TypeDlg_Title;
	public static String Shortcut_TypeDlg_ChooseDebugType;
	public static String Shortcut_TypeDlg_ChooseRunType;
	public static String Shortcut_ErrDlg_Msg_LaunchFailed_ERROR_;
	public static String Shortcut_ConfigDlg_Title;
	public static String Shortcut_ConfigDlg_Msg_DebugConfiguration;
	public static String Shortcut_ConfigDlg_Msg_RunConfiguration;

	static {
		NLS.initializeMessages(BUNDLE_NAME, VCELauncherMessages.class);
	}
}