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
 *  $RCSfile: SwingChooseBeanContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-05 18:14:26 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

 
/**
 * 
 * @since 1.0.0
 */
public class SwingChooseBeanContributor extends YesNoListChooseBeanContributor {
	public static String[] SWING_BASE_TYPES = {"javax.swing", "JComponent", "javax.swing", "JFrame", "javax.swing", "JDialog", "javax.swing", "JWindow", "javax.swing", "JApplet", "javax.swing.table", "TableColumn"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
	public SwingChooseBeanContributor(){
		super(ChooseBeanMessages.getString("SwingChooseBeanContributor.Name"), SWING_BASE_TYPES, null); //$NON-NLS-1$
	}
}
