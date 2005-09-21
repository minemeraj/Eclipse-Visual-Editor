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
 *  $Revision: 1.7 $  $Date: 2005-09-21 11:37:52 $ 
 */
package org.eclipse.ve.internal.jfc.core;


import org.eclipse.core.runtime.Plugin;

public class JFCVisualPlugin extends Plugin {

	private static JFCVisualPlugin PLUGIN;
	public static final String DEFAULT_LAYOUTMANAGER = "DEFAULT_LAYOUTMANAGER";
	public static final String DEFAULT_LAYOUTMANAGER_VALUE = "java.awt.GridBagLayout";
	public static final String PREFERENCE_PAGE_ID =  "org.eclipse.ve.internal.jfc.PreferencePage"; //$NON-NLS-1$
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
		    new String[] {"null","FlowLayout","BorderLayout","GridBagLayout","GridLayout",
				         "BoxLayout(X_AXIS)","BoxLayout(Y_AXIS)","CardLayout"},
		    new String[] {null,"java.awt.FlowLayout","java.awt.BorderLayout","java.awt.GridBagLayout","java.awt.GridLayout",
				         "javax.swing.BoxLayoutX_Axis","javax.swing.BoxLayoutY_Axis","java.awt.CardLayout"}
		  };
		};
		return layoutManagers;
	}
}
