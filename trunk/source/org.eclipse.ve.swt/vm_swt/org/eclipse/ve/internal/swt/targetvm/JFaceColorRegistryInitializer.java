/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JFaceColorRegistryInitializer.java,v $
 *  $Revision: 1.1 $  $Date: 2005-04-01 19:48:03 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.RGB;

/**
 * Initialize the JFace ColorRegistry with the JFacePreferences colors from the IDE.
 * 
 * @since 1.1.0
 */
public class JFaceColorRegistryInitializer {

	public static void init(final Map rgbMap) {
		Environment.getDisplay().asyncExec(new Runnable() {

			public void run() {
				Iterator iter = rgbMap.keySet().iterator();
				while (iter.hasNext()) {
					String symbolicName = (String) iter.next();
					JFaceResources.getColorRegistry().put(symbolicName, (RGB) rgbMap.get(symbolicName));
				}
			}
		});
	}
}
