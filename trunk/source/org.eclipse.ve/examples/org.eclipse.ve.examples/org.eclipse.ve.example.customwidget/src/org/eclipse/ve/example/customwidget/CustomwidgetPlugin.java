package org.eclipse.ve.example.customwidget;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.*;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.core.runtime.*;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class CustomwidgetPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static CustomwidgetPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	private static Image smileyFace;
	
	/**
	 * The constructor.
	 */
	public CustomwidgetPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		try {
			resourceBundle   = ResourceBundle.getBundle("org.eclipse.ve.example.customwidget.CustomwidgetPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static CustomwidgetPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = CustomwidgetPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static Image getSmileyFace() {
		if(smileyFace == null){
			smileyFace = CDEPlugin.getImageFromBundle(getDefault().getBundle(), "icons/custom.gif");			
		}
		return smileyFace;
	}
}
