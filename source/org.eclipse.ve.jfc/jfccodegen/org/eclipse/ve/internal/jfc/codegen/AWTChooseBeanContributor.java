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
 *  $RCSfile: AWTChooseBeanContributor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-12-02 20:22:22 $ 
 */
package org.eclipse.ve.internal.jfc.codegen;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.choosebean.YesNoListChooseBeanContributor;

import org.eclipse.ve.internal.jfc.core.JFCVisualPlugin;


 
/**
 * 
 * @since 1.0.0
 */
public class AWTChooseBeanContributor extends YesNoListChooseBeanContributor{

	public static String[] AWT_BASE_TYPE_NAMES = {"java.awt", "Component"}; //$NON-NLS-1$ //$NON-NLS-2$
	public AWTChooseBeanContributor(){
		super(JFCCodegenMessages.AWTChooseBeanContributor_Label,  AWT_BASE_TYPE_NAMES, SwingChooseBeanContributor.SWING_BASE_TYPES);  
	}
	public ImageDescriptor getImage() {
		return CDEPlugin.getImageDescriptorFromPlugin(JFCVisualPlugin.getPlugin(), "icons/full/obj16/awtbean.gif"); //$NON-NLS-1$
	}

}
