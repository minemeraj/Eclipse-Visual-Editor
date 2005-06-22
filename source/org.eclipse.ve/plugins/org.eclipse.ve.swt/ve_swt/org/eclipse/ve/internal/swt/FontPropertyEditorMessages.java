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
package org.eclipse.ve.internal.swt;

import org.eclipse.osgi.util.NLS;

public final class FontPropertyEditorMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.swt.fontpropertyeditor";//$NON-NLS-1$

	private FontPropertyEditorMessages() {
		// Do not instantiate
	}

	public static String nameLabel;
	public static String styleLabel;
	public static String sizeLabel;
	public static String previewText;
	public static String normalStyle;
	public static String boldStyle;
	public static String italicStyle;
	public static String boldItalicStyle;
	public static String jfaceBannerFontName;
	public static String jfaceDefaultFontName;
	public static String jfaceDialogFontName;
	public static String jfaceHeaderFontName;
	public static String jfaceTextFontName;
	public static String NamedFontsTab;
	public static String JFaceFontsTab;
	public static String FontCustomPropertyEditor_NameLabel_Text;
	public static String FontCustomPropertyEditor_StyleLabel_text;

	static {
		NLS.initializeMessages(BUNDLE_NAME, FontPropertyEditorMessages.class);
	}
}