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
package org.eclipse.ve.internal.jface;

import org.eclipse.osgi.util.NLS;

public final class JFaceMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.jface.messages";//$NON-NLS-1$

	private JFaceMessages() {
		// Do not instantiate
	}

	public static String RCPViewCreation_RCP_Project_ERROR_;

	static {
		NLS.initializeMessages(BUNDLE_NAME, JFaceMessages.class);
	}
}