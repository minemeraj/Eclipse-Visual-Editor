/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AWTChooseBeanContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-05 18:14:26 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

 
/**
 * 
 * @since 1.0.0
 */
public class AWTChooseBeanContributor extends YesNoListChooseBeanContributor{

	public static String[] AWT_BASE_TYPE_NAMES = {"java.awt", "Component"}; //$NON-NLS-1$ //$NON-NLS-2$
	public AWTChooseBeanContributor(){
		super(ChooseBeanMessages.getString("AWTChooseBeanContributor.Name"),  AWT_BASE_TYPE_NAMES, SwingChooseBeanContributor.SWING_BASE_TYPES); //$NON-NLS-1$
		
	}

}
