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
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.codegen.util.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String VEModelCacheUtility_2;
	public static String VEModelCacheUtility_3;
	public static String VEModelCacheUtility_4;
	public static String ReverseParserJob_0;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}