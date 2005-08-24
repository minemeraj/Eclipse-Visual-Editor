/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SwingChooseBeanContributor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:12 $ 
 */
package org.eclipse.ve.internal.jfc.codegen;

import org.eclipse.swt.graphics.Image;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.choosebean.YesNoListChooseBeanContributor;

import org.eclipse.ve.internal.jfc.core.JFCVisualPlugin;


 
/**
 * 
 * @since 1.0.0
 */
public class SwingChooseBeanContributor extends YesNoListChooseBeanContributor {
	public static String[] SWING_BASE_TYPES = {"javax.swing", "JComponent", "javax.swing", "JFrame", "javax.swing", "JDialog", "javax.swing", "JWindow", "javax.swing", "JApplet", "javax.swing.table", "TableColumn"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
	public SwingChooseBeanContributor(){
		super(JFCCodegenMessages.SwingChooseBeanContributor_Label, SWING_BASE_TYPES, null);  
	}
	public Image getImage() {
		return CDEPlugin.getImageFromPlugin(JFCVisualPlugin.getPlugin(), "icons/full/obj16/swingbean.gif"); //$NON-NLS-1$
	}
}
