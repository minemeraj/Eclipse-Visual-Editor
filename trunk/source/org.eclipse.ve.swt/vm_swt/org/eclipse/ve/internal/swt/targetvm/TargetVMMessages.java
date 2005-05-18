package org.eclipse.ve.internal.swt.targetvm;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class TargetVMMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.swt.targetvm.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private TargetVMMessages() {
	}

	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
