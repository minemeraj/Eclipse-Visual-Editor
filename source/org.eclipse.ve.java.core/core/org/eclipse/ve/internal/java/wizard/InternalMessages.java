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
package org.eclipse.ve.internal.java.wizard;

import org.eclipse.osgi.util.NLS;

public final class InternalMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.wizard.messages";//$NON-NLS-1$

	private InternalMessages() {
		// Do not instantiate
	}

	public static String ClasspathWizardPage_PageName;
	public static String ClasspathWizardPage_NoConfigData_ErrorMessage_ERROR_;
	public static String ClasspathWizardPage_NoConfigData_Label_ErrorMessage_ERROR_;
	public static String ClasspathWizardPage_Group_Buildpath_Includes;
	public static String ClasspathWizardPage_BuildPath_SourceAttachments_Message;
	public static String ClasspathWizardPage_Palette_Category_Added_Message;
	public static String ClasspathWizardPage_LoadPaletteExtension_Exception_Message1_EXC_;
	public static String ClasspathWizardPage_LoadPaletteExtension_Exception_Message2;
	public static String ClasspathWizardPage_LoadPaletteExtension_Exception_Message3;
	public static String RegisteredClasspathContainerWizardPage_Title;

	static {
		NLS.initializeMessages(BUNDLE_NAME, InternalMessages.class);
	}
}