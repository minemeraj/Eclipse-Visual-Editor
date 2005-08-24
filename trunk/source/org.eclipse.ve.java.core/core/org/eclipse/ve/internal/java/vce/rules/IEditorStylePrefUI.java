/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IEditorStylePrefUI.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
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
