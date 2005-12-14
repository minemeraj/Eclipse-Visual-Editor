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
package org.eclipse.ve.internal.java.codegen.wizards;

import org.eclipse.osgi.util.NLS;

public final class CodegenWizardsMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.codegen.wizards.messages";//$NON-NLS-1$

	private CodegenWizardsMessages() {
		// Do not instantiate
	}

	public static String StyleTreeLabelProvider_StyleNameError_ERROR_;
	public static String StyleTreeLabelProvider_ElementNameError_ERROR_;
	public static String SourceFolder_RCP_ERROR;
	public static String SourceFolder_NON_UI_ERROR;
	public static String NewVisualClassWizardPage_EnclosedType_ERROR_;
	public static String NewVisualClassWizardPage_Style_Label;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CodegenWizardsMessages.class);
	}

	public static String VisualClassExampleWizardPage_title;
	public static String VisualClassExampleWizardPage_description;
	public static String NewVisualClassWizardPage_title;
	public static String NewVisualClassWizardPage_description;
	public static String NewVisualClassWizardPage_label_whichMethodStubs;
	public static String NewVisualClassWizardPage_checkbox_need_main;
	public static String NewVisualClassWizardPage_checkbox_super_constructors;
	public static String NewVisualClassWizardPage_checkbox_inherited_abstract_methods;
}