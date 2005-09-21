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
 *  $RCSfile: JFCPreferenceInitializer.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-21 10:39:46 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.*;
 

public class JFCPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		Preferences pluginPreferences = JFCVisualPlugin.getPlugin().getPluginPreferences();
		pluginPreferences.setDefault(JFCVisualPlugin.DEFAULT_LAYOUTMANAGER,JFCVisualPlugin.DEFAULT_LAYOUTMANAGER_VALUE);
	}
}
