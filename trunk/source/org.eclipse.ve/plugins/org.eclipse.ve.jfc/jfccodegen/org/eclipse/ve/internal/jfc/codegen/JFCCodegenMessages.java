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
package org.eclipse.ve.internal.jfc.codegen;

import org.eclipse.osgi.util.NLS;

public final class JFCCodegenMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.jfc.codegen.messages";//$NON-NLS-1$

	private JFCCodegenMessages() {
		// Do not instantiate
	}

	public static String AWTChooseBeanContributor_Label;
	public static String SwingChooseBeanContributor_Label;

	static {
		NLS.initializeMessages(BUNDLE_NAME, JFCCodegenMessages.class);
	}
}