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
//		fStore.setValue(JFCVisualPlugin.DEFAULT_LAYOUT,pageContents.getLayoutTypeName());	
		return true;
	}
	
	protected void performDefaults() {
//		fStore.setValue(SwtPlugin.DEFAULT_LAYOUT,"org.eclipse.swt.widgets.layout.GridLayout");
		super.performDefaults();
	}

}
