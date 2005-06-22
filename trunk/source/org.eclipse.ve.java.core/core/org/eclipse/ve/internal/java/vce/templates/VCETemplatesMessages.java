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
package org.eclipse.ve.internal.java.vce.templates;

import org.eclipse.osgi.util.NLS;

public final class VCETemplatesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.vce.templates.messages";//$NON-NLS-1$

	private VCETemplatesMessages() {
		// Do not instantiate
	}

	public static String JavaObjectEmiter_Problem_DirectoryDoesntExist_ERROR_;
	public static String JavaObjectEmiter_Problem_Security_EXC_;
	public static String JavaObjectEmiter_Problem_IO_EXC_;

	static {
		NLS.initializeMessages(BUNDLE_NAME, VCETemplatesMessages.class);
	}
}