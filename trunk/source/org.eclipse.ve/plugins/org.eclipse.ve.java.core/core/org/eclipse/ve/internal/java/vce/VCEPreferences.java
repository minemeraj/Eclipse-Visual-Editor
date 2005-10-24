/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce;
/*
 *  $RCSfile: VCEPreferences.java,v $
 *  $Revision: 1.20 $  $Date: 2005-10-24 18:00:33 $ 
 */


import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.*;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class VCEPreferences {
	
	public static final String SWING_LOOKANDFEEL = "SWING_LOOKANDFEEL"; //$NON-NLS-1$
	public static final String USER_DEFINED_LOOKANDFEEL = "USER_DEFINED_LOOKANDFEEL"; //$NON-NLS-1$	
	public static final String SHOW_LIVE_WINDOW = "SHOW_LIVE_WINDOW"; //$NON-NLS-1$
	public static final String USE_TEXT_FOR_CLIPBOARD = "USE_TEXT_FOR_CLIPBOARD"; //$NON-NLS-1$	
	public static final String OPEN_PROPERTIES_VIEW = "OPEN_PROPERTIES_VIEW"; //$NON-NLS-1$
	public static final String OPEN_JAVABEANS_VIEW = "OPEN_JAVABEANS_VIEW"; //$NON-NLS-1$		
	public static final String NOTEBOOK_PAGE = "NOTEBOOK_PAGE"; //$NON-NLS-1$
	public static final String SELECT_SOURCE = "SELECT_SOURCE"; //$NON-NLS-1$
	public static final String SOURCE_SYNC_DELAY = "SOURCE_SYNC_DELAY_NEW" ; //$NON-NLS-1$
	public static final int	   DEFAULT_SYNC_DELAY = 1000 ;
	public static final String SOURCE_DELAY_FACTOR = "SOURCE_DELAY_FACTOR_NEW" ; //$NON-NLS-1$
    public static final String GENERATE_COMMENT = "GENERATE_EXPRESSION_COMMENT" ; //$NON-NLS-1$
    public static final String GENERATE_TRY_CATCH_BLOCK = "GENERATE_TRY_CATCH_BLOCK"; //$NON-NLS-1$
//    public static String REQUIRE_IVJ_COMPONENTS = "REQUIRE_IVJ_COMPONENTS"; //$NON-NLS-1$
  
    public static final String LOOK_AND_FEEL = "lookandfeel"; //$NON-NLS-1$
    public static final String LF_NAME = "name"; //$NON-NLS-1$
    public static final String LF_CLASS = "class"; //$NON-NLS-1$
    
    public static final String MAX_AWT_COMPONENT_IMAGE_WIDTH = "MAX_AWT_COMPONENT_IMAGE_WIDTH";	//$NON-NLS-1$
    public static final int DEFAULT_MAX_AWT_COMPONENT_IMAGE_WIDTH = 3000;
	public static final String MAX_AWT_COMPONENT_IMAGE_HEIGHT = "MAX_AWT_COMPONENT_IMAGE_HEIGHT";	//$NON-NLS-1$
	public static final int DEFAULT_MAX_AWT_COMPONENT_IMAGE_HEIGHT = 3000;
    
	// Rename instance preferences
	public static final String RENAME_INSTANCE_ASK_KEY ="RENAME_INSTANCE_ASK_KEY"; //$NON-NLS-1$
	public static final boolean RENAME_INSTANCE_ASK_DEFAULT;
	static{
		RENAME_INSTANCE_ASK_DEFAULT = !isLinux(); //Do not allow rename prompt if on Linux cause it crashes - bugzilla 103738
	}
    
    public static final String CODEGEN_CARET_SELECT_UI_KEY = "CODEGEN_CARET_SELECT_UI_KEY";  //$NON-NLS-1$
    public static final boolean CODEGEN_CARET_SELECT_UI_DEFAULT = true;
    
    // The following are run time options
    public static final String DEBUG_CONSOLE_ECHO   = "/debug/logtrace" ; // Dump all logs to console //$NON-NLS-1$
    public static final String DEBUG_XMITEXT_OPTION = "/debug/xmltext" ;    // Provide the XMI text option  //$NON-NLS-1$
	public static final String DEBUG_CLIPBOARDTEXT_OPTION = "/debug/clipboardtext"; // Put text in the clipboard to help debugging //$NON-NLS-1$ 	
    public static final String DEBUG_LIVEWINDOW_OPTION = "/debug/livewindow" ; // Provide the Live Window option  //$NON-NLS-1$
    
	public static final String JVE_PATTERN_STYLE_ID = "JVA_PATTERN_STYLE_ID" ; // The current Style (pattern) style ID //$NON-NLS-1$

public static boolean isLinux(){
	return Platform.getOS().equals(Platform.OS_LINUX);
}
/*
 *  This is a read from the plugin extension point <org.eclipse.ve.java.core.lookandfeel>
 */
public static String[][] getPluginLookAndFeelClasses(){
	
	IExtensionPoint lookAndFeelExtensionPoint = Platform.getExtensionRegistry().getExtensionPoint(getPlugin().getBundle().getSymbolicName(), LOOK_AND_FEEL);
	if ( lookAndFeelExtensionPoint == null ) return new String[0][];
	IConfigurationElement[] lookAndFeelElements = lookAndFeelExtensionPoint.getConfigurationElements();
	String[][] result = new String[lookAndFeelElements.length][];
	for (int i = 0; i < lookAndFeelElements.length; i++) {
		try {
			IConfigurationElement pointConfig = lookAndFeelElements[i];
			// Get the lookandfeel element
			result[i] = new String[] { 
				pointConfig.getAttributeAsIs(LF_NAME),
				pointConfig.getAttributeAsIs(LF_CLASS)
			};
		} catch ( Exception exc ) {
			// Need to log this really to let the user know that their extension point is bad
		}
	}

	return result;
	
}
public static boolean askForRename(){
	if(isLinux()){
		return false;
	} else {
		return JavaVEPlugin.getPlugin().getPluginPreferences().getBoolean(VCEPreferences.RENAME_INSTANCE_ASK_KEY);
	}
}

/** 
 * Return the list of look and feel classes, not including the default ones
 * The resulting of this is a two argument array of name, class
 */
public static String[][] getUserLookAndFeelClasses(){
	
	ArrayList result = new ArrayList(0);
	
	// Get the user defined look and feel classes saved for the current workbench
	String userDefinedLookAndFeel = getPlugin().getPluginPreferences().getString(USER_DEFINED_LOOKANDFEEL);
	if ( userDefinedLookAndFeel != null ) {
		// Iterate over the string
		StringTokenizer tokenizer = new StringTokenizer(userDefinedLookAndFeel,","); //$NON-NLS-1$
		while(tokenizer.hasMoreElements()){
			String lfName = (String)tokenizer.nextElement();
			if ( tokenizer.hasMoreElements() ) {
				String lfClass = (String)tokenizer.nextElement();
				result.add(new String[] { lfName , lfClass });
			}
		}
	}
	
	// Convert the array list into an array for the return value
	if ( result.size() > 0 ){
		return (String[][]) result.toArray(new String[result.size()][]);
	} else {
		return new String[0][];
	}
}
public static void setUserLookAndFeelClasses(String[][] nameClassPairs){
	
	if ( nameClassPairs == null ) {
		getPlugin().getPluginPreferences().setToDefault(USER_DEFINED_LOOKANDFEEL);
	} else {
		// Set the look and feel classes into the store
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < nameClassPairs.length; i++) {
			String[] lfNameClass = nameClassPairs[i];
			// If this is not the first entry preceed it with a ,
			if ( i > 0 ) buffer.append(',');
			buffer.append(lfNameClass[0]);
			buffer.append(',');
			buffer.append(lfNameClass[1]);		
		}
		getPlugin().getPluginPreferences().setValue(USER_DEFINED_LOOKANDFEEL,buffer.toString());
	}
}	
	
public static JavaVEPlugin getPlugin() {
	return JavaVEPlugin.getPlugin();
}

public static void initializeDefaultPluginPreferences(Preferences aStore) {
	// TODO A lot of these preferences shouldn't be in here. They are really in different plugs, like the source sync
	// and style stuff are in codegen and max_awt... are in visuals.
	aStore.setDefault(VCEPreferences.OPEN_JAVABEANS_VIEW, true);
	aStore.setDefault(VCEPreferences.OPEN_PROPERTIES_VIEW, true);
	aStore.setDefault(VCEPreferences.SOURCE_SYNC_DELAY,VCEPreferences.DEFAULT_SYNC_DELAY) ;
	aStore.setDefault(VCEPreferences.JVE_PATTERN_STYLE_ID, "GetterStyle");		 //$NON-NLS-1$
	aStore.setDefault(VCEPreferences.MAX_AWT_COMPONENT_IMAGE_WIDTH, VCEPreferences.DEFAULT_MAX_AWT_COMPONENT_IMAGE_WIDTH);
	aStore.setDefault(VCEPreferences.MAX_AWT_COMPONENT_IMAGE_HEIGHT, VCEPreferences.DEFAULT_MAX_AWT_COMPONENT_IMAGE_HEIGHT);
	aStore.setDefault(VCEPreferences.RENAME_INSTANCE_ASK_KEY, VCEPreferences.RENAME_INSTANCE_ASK_DEFAULT);
	aStore.setDefault(VCEPreferences.CODEGEN_CARET_SELECT_UI_KEY, VCEPreferences.CODEGEN_CARET_SELECT_UI_DEFAULT);
}

/**
 * @return true/false if a given option is given to the VM
 */
public static boolean isOptionSet(String option) {
    String  fullOption = getPlugin().getBundle().getSymbolicName()+option ;
    return   "true".equalsIgnoreCase(Platform.getDebugOption(fullOption)) ; //$NON-NLS-1$
}

/**
 * Is a VM option given to display the XML Text option
 */
protected static boolean isXMLText ()
{
   return isOptionSet(VCEPreferences.DEBUG_XMITEXT_OPTION) ;
}

/**
 * Is XML Text setting turned on.
 */
public static boolean isXMLTextOn() {
	// Is the debug option on AND is the setting actually set.
	return VCEPreferences.isXMLText() && CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.SHOW_XML);
}

protected static boolean isClipboardText(){
   return isOptionSet(VCEPreferences.DEBUG_CLIPBOARDTEXT_OPTION);
}

/**
 * Is a VM option given to display the Show Live Window option
 */
public static boolean isLiveWindow ()
{
   return isOptionSet(VCEPreferences.DEBUG_LIVEWINDOW_OPTION) ;
}

/**
 * Is Live Window setting turned on.
 */
public static boolean isLiveWindowOn() {
	// Is the debug option on AND is the setting actually set.
	return isLiveWindow() && getPlugin().getPluginPreferences().getBoolean(VCEPreferences.SHOW_LIVE_WINDOW);
}

public static String getStyleID() {
	return getPlugin().getPluginPreferences().getString(VCEPreferences.JVE_PATTERN_STYLE_ID);
}	

}
