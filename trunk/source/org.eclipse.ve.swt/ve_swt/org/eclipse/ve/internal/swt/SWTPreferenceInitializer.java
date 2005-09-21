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
 *  $RCSfile: SWTPreferenceInitializer.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-21 10:39:14 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.*;
 

public class SWTPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		Preferences pluginPreferences = SwtPlugin.getDefault().getPluginPreferences();
		pluginPreferences.setDefault(SwtPlugin.DEFAULT_LAYOUT,SwtPlugin.DEFAULT_LAYOUT_VAUE);
	 
//		IEclipsePreferences node = new DefaultScope().getNode(SwtPlugin.getDefault().getBundle().getSymbolicName());
	 
	}

}
