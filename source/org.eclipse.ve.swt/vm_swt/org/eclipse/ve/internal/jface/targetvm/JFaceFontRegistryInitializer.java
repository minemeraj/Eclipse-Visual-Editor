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
 *  $RCSfile: JFaceFontRegistryInitializer.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-07 20:12:15 $ 
 */
package org.eclipse.ve.internal.jface.targetvm;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.ve.internal.swt.targetvm.Environment;
 
/**
 * Initialize the JFace FontRegistry with the JFaceResources fonts from the IDE.
 * 
 * @since 1.1.0
 */
public class JFaceFontRegistryInitializer {

	public static void init(final Map fontDataMap) {
		Environment.getDisplay().asyncExec(new Runnable() {

			public void run() {
				Iterator iter = fontDataMap.keySet().iterator();
				while (iter.hasNext()) {
					String symbolicName = (String) iter.next();
					JFaceResources.getFontRegistry().put(symbolicName, new FontData[] { (FontData) fontDataMap.get(symbolicName)});
				}
			}
		});
	}
}
