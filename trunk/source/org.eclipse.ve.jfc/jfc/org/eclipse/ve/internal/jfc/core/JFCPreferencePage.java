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
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class JFCPreferencePage extends org.eclipse.jface.preference.PreferencePage implements IWorkbenchPreferencePage {

	private Preferences fStore;
	private JFCPreferencePageContents pageContents;

	protected Control createContents(Composite parent) {
		pageContents = new JFCPreferencePageContents(parent,SWT.NONE);
		pageContents.init(getStore());
		return pageContents;
	}
	
	private Preferences getStore() {
		if (fStore != null)
			return fStore;
		fStore = JFCVisualPlugin.getPlugin().getPluginPreferences();
		return fStore;
	}	

	public void init(IWorkbench workbench) {
	}
	
	public boolean performOk(){
		fStore.setValue(JFCVisualPlugin.DEFAULT_LAYOUTMANAGER,pageContents.getLayoutManagerName());	
		return true;
	}
	
	protected void performDefaults() {
		pageContents.setLayoutManagerName(JFCVisualPlugin.DEFAULT_LAYOUTMANAGER_VALUE);
		super.performDefaults();
	}

}
