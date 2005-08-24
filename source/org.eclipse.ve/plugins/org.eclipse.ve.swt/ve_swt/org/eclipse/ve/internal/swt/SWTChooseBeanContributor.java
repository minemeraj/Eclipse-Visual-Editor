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
 *  $RCSfile: SWTChooseBeanContributor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.graphics.Image;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.choosebean.YesNoListChooseBeanContributor;

 
/**
 * 
 * @since 1.0.0
 */
public class SWTChooseBeanContributor extends YesNoListChooseBeanContributor{

	public static String[] SWT_BASE_TYPE_NAMES = {"org.eclipse.swt.widgets", "Widget"}; //$NON-NLS-1$ //$NON-NLS-2$
	public SWTChooseBeanContributor(){
		super(SWTMessages.SWTChooseBeanContributor_Name,  SWT_BASE_TYPE_NAMES, null); 
	}
	public Image getImage() {
		return CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/obj16/swtbean.gif"); //$NON-NLS-1$
	}

}
