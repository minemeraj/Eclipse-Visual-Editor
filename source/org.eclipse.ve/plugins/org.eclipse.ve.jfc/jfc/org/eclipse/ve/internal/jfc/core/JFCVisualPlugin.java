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
/*
 *  $RCSfile: JFCVisualPlugin.java,v $
 *  $Revision: 1.9 $  $Date: 2005-12-14 21:37:04 $ 
 */
package org.eclipse.ve.internal.jfc.core;


import org.eclipse.core.runtime.Plugin;

public class JFCVisualPlugin extends Plugin {

	private static JFCVisualPlugin PLUGIN;
	public static final String DEFAULT_LAYOUTMANAGER = "DEFAULT_LAYOUTMANAGER"; //$NON-NLS-1$
	public static final String DEFAULT_LAYOUTMANAGER_VALUE = "java.awt.GridBagLayout"; //$NON-NLS-1$
	public static final String PREFERENCE_PAGE_ID =  "org.eclipse.ve.internal.jfc.PreferencePage"; //$NON-NLS-1$
	public static final String NULL_LAYOUT =  "null"; //$NON-NLS-1$	
	private String[][] layoutManagers;

	public JFCVisualPlugin() {
		PLUGIN = this;
	}

	public static JFCVisualPlugin getPlugin() {
		return PLUGIN;
	}

	public String[][] getLayoutManagers() {
		if(layoutManagers == null){
		  layoutManagers = new String[][] {
		    new String[] {JFCMessages.JFCVisualPlugin_layout_NULL,JFCMessages.JFCVisualPlugin_layout_Flow,JFCMessages.JFCVisualPlugin_layout_Border,JFCMessages.JFCVisualPlugin_layout_GridBag,JFCMessages.JFCVisualPlugin_layout_Grid,
				         JFCMessages.JFCVisualPlugin_layout_Box_XAxis,JFCMessages.JFCVisualPlugin_layout_Box_YAxis,JFCMessages.JFCVisualPlugin_layout_Card},
		    new String[] {NULL_LAYOUT,"java.awt.FlowLayout","java.awt.BorderLayout","java.awt.GridBagLayout","java.awt.GridLayout", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				         "javax.swing.BoxLayoutX_Axis","javax.swing.BoxLayoutY_Axis","java.awt.CardLayout"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		  };
		};
		return layoutManagers;
	}
}
