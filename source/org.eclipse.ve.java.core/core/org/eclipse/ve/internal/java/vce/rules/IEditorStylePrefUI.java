/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IEditorStylePrefUI.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.vce.rules;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Gili Mendel
 *
 */
public interface IEditorStylePrefUI {
	 
	Control createUI (Composite parent);
	void storeUI();	// If changes are to be stored (like an OK is pressed), given the opportunity to persist
	void restoreDefaults();	// Restore all of the panels to default settings (don't actually save, that will happen later).
}
