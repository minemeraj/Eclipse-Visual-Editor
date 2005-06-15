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
 *  $Revision: 1.2 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.jface.targetvm;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.ve.internal.swt.targetvm.Environment;

/**
 * Initialize the JFace ColorRegistry with the JFacePreferences colors from the IDE.
 * 
 * @since 1.1.0
 */
public class JFaceColorRegistryInitializer {

	public static void init(final Map rgbMap, Environment environment) {
		environment.getDisplay().asyncExec(new Runnable() {

			public void run() {
				Iterator iter = rgbMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Entry) iter.next();
					JFaceResources.getColorRegistry().put((String) entry.getKey(), (RGB) entry.getValue());
				}
			}
		});
	}
}
