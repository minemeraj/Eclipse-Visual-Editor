package org.eclipse.ve.internal.jface.targetvm;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class JFaceTargetVMMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.jface.targetvm.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private JFaceTargetVMMessages() {
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
